using app_engine.Dtos;

namespace app_engine.Interfaces
{
    public interface IUserService
    {
        public Task<List<UserResponse>> AllUser();
    }
}
