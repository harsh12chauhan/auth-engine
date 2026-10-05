using static System.Runtime.InteropServices.JavaScript.JSType;

namespace app_engine.Common
{
    public class ApiResponse<T>
    {
        public bool Success { get; set; }

        public string? Message { get; set; } = string.Empty;
        public T? Data {  get; set; }

        public DateTime TimeStamp { get; set; } = DateTime.UtcNow;

        public static ApiResponse<T> SuccessResponse(T data, string message = "Request successful") {
            return new ApiResponse<T>
            {
                Success = true,
                Message = message,
                Data = data                
            };
        }

        public static ApiResponse<T> FailureResponse(string message) {
            return new ApiResponse<T>
            {
                Success = false,
                Message = message,
                Data = default               
            };
        }
    }
}
