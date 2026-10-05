using app_engine.Common;
using app_engine.Dtos;
using app_engine.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace app_engine.Controllers
{
    [ApiController]
    [Route("/api/user/")]
    public class UserController(IUserService userService) : ControllerBase
    {
        [HttpGet("all")]
        public async Task<ActionResult<ApiResponse<List<UserResponse>>>> GetUsers()
        {
            var response = await userService.AllUser();

            return Ok(
                ApiResponse<List<UserResponse>>.SuccessResponse(
                        response,
                        "Users retrieved successfully"
                    )
                );
        }
    }
}
