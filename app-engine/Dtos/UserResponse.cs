using app_engine.Enums;

namespace app_engine.Dtos
{
    public class UserResponse
    {
        public Guid Id { set; get; }

        public string Username { set; get; } = string.Empty;

        public string Email { set; get; } = string.Empty;

        public bool IsEnabled { set; get; }

        public UserRole Role { set; get; }

        public DateTime CreatedAt { set; get; }
    }
}
