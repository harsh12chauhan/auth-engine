using app_engine.Configuration;
using app_engine.Data;
using app_engine.Interfaces;
using app_engine.Middleware;
using app_engine.Services;
using Microsoft.EntityFrameworkCore;
using System.Text.Json.Serialization;
using YourProject.Middleware;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.ConfigureApiBehavior();

// Register Db Context
builder.Services.AddDbContext<ApplicationDbContext>(options =>
    options.UseNpgsql(builder.Configuration.GetConnectionString("DefaultConnection")));

builder.Services.AddHostedService<DatabaseWarmupService>();

builder.Services.AddControllers().AddJsonOptions(options =>
    options.JsonSerializerOptions.Converters.Add(new JsonStringEnumConverter())
);

builder.Services.AddScoped<IAuthService, AuthService>();
builder.Services.AddScoped<IUserService, UserService>();

builder.Services.AddAuthorization();

var app = builder.Build();

app.UseMiddleware<GlobalExceptionHandler>();
app.UseMiddleware<RequestTimingMiddleware>();

app.UseHttpsRedirection();

app.UseAuthentication();
app.UseAuthorization();

app.MapControllers();

app.Run();
