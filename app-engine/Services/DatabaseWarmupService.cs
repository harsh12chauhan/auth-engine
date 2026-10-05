using app_engine.Data;
using Microsoft.EntityFrameworkCore;
using System.Diagnostics;

namespace app_engine.Services;

public class DatabaseWarmupService : IHostedService
{
    private readonly IServiceScopeFactory _scopeFactory;
    private readonly ILogger<DatabaseWarmupService> _logger;

    public DatabaseWarmupService(
        IServiceScopeFactory scopeFactory,
        ILogger<DatabaseWarmupService> logger)
    {
        _scopeFactory = scopeFactory;
        _logger = logger;
    }
    public async Task StartAsync(CancellationToken cancellationToken)
    {
        try
        {
            using var scope = _scopeFactory.CreateScope();

            var db = scope.ServiceProvider
                .GetRequiredService<ApplicationDbContext>();

            var stopwatch = Stopwatch.StartNew();

            await db.Database.OpenConnectionAsync(cancellationToken);

            _logger.LogInformation(
                "Database connection warmed up in {ElapsedMs} ms",
                stopwatch.ElapsedMilliseconds
            );

            stopwatch.Restart();

            await db.Users
                .AsNoTracking()
                .Select(x => x.Id)
                .FirstOrDefaultAsync(cancellationToken);

            stopwatch.Stop();

            _logger.LogInformation(
                "EF query warmed up in {ElapsedMs} ms",
                stopwatch.ElapsedMilliseconds
            );

            await db.Database.CloseConnectionAsync();
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Database warm-up failed");
        }
    }

    public Task StopAsync(CancellationToken cancellationToken)
    {
        return Task.CompletedTask;
    }
}