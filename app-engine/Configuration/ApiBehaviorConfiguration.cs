using app_engine.Common;
using Microsoft.AspNetCore.Mvc;

namespace app_engine.Configuration
{
    public static class ApiBehaviorConfiguration
    {
        public static IServiceCollection ConfigureApiBehavior(this IServiceCollection services)
        {
            services.AddControllers().ConfigureApiBehaviorOptions(options =>
                {
                    options.InvalidModelStateResponseFactory = context =>
                    {
                        var errors = context.ModelState
                            .Where(x => x.Value?.Errors.Count > 0)
                            .ToDictionary(
                                x => x.Key,
                                x => x.Value!.Errors
                                    .Select(e => e.ErrorMessage)
                                    .ToArray()
                            );

                        var response =
                            ApiResponse<Dictionary<string, string[]>>
                                .FailureResponse("Validation failed");

                        response.Data = errors;

                        return new BadRequestObjectResult(response);
                    };
                });

            return services;
        }
    }
}