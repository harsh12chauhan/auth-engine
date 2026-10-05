using app_engine.Dtos;

namespace app_engine.Interfaces
{
    public interface IAuthService
    {
        public Task<UserResponse> AuthenticateUser(UserLoginRequest userLoginRequest);
        public Task<UserResponse> CreateUser(CreateUserRequest createUserRequest);
     
    }
}
