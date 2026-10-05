using app_engine.Common;
using app_engine.Exceptions;
using System.Text.Json;

namespace app_engine.Middleware
{
    public class GlobalExceptionHandler
    {
        private readonly RequestDelegate _next;
        private readonly ILogger<GlobalExceptionHandler> _logger;

        public GlobalExceptionHandler(RequestDelegate next, ILogger<GlobalExceptionHandler> logger)
        {

            _next = next;
            _logger = logger;

        }

        public async Task InvokeAsync(HttpContext context) {

            try 
            {
                await _next(context);                
            } 
            catch (Exception ex) 
            {
                _logger.LogError(ex,"Unhandled exception occurred while processing request");

                await HandleExceptionAsync(context, ex);  
            }
        }

        private static async Task HandleExceptionAsync(HttpContext context, Exception exception) { 
            
            var statusCode = StatusCodes.Status500InternalServerError;
            var message = "An unexpected error occurred.";

            if (exception is ApiException apiException)
            {
                statusCode = apiException.StatusCode;
                message = apiException.Message;
            }

            var response = ApiResponse<object>.FailureResponse(message);

            context.Response.StatusCode = statusCode;
            context.Response.ContentType = "application/json";

            await context.Response.WriteAsync(JsonSerializer.Serialize(response));
        }
    }
}
