using app_engine.Common;
using app_engine.Dtos;
using app_engine.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace app_engine.Controller
{
    [ApiController]
    [Route("/api/auth/")]
    public class AuthController(IAuthService authService) : ControllerBase
    {
        [HttpPost("register")]
        public async Task<ActionResult<ApiResponse<UserResponse>>> Register(CreateUserRequest createUserRequest)
        {
            var response = await authService.CreateUser(createUserRequest);

            return Ok(
               ApiResponse<UserResponse>.SuccessResponse(
                       response,
                       "User registered successfully"
                   )
               );
        }

        [HttpPost("login")]
        public async Task<ActionResult<ApiResponse<UserResponse>>> Login(UserLoginRequest userLoginRequest)
        {
            var response = await authService.AuthenticateUser(userLoginRequest);

            return Ok(
                ApiResponse<UserResponse>.SuccessResponse(
                        response,
                        "Login successful"
                    )
                );
        }      

        [HttpGet("test")]
        public async Task<IActionResult> Test()
        {
            return Ok(
                ApiResponse<string>.SuccessResponse(
                        "testing",
                        "Test successful"
                    )
                );
        }

        [HttpGet("test-error")]
        public IActionResult TestError()
        {
            throw new Exception("Something went wrong");
        }
    }
}
