using app_engine.Data;
using app_engine.Dtos;
using app_engine.Interfaces;
using Microsoft.EntityFrameworkCore;

namespace app_engine.Services
{
    public class UserService(ApplicationDbContext _context) : IUserService
    {
        public async Task<List<UserResponse>> AllUser()
        {
            var users = await _context.Users
                .AsNoTracking()
                .Select(user => new UserResponse
                {
                    Id = user.Id,
                    Username = user.Username,
                    Email = user.Email,
                    Role = user.Role,
                    IsEnabled = user.IsEnabled,
                    CreatedAt = user.CreatedAt
                }
                ).ToListAsync();

            return users;
        }
    }
}
