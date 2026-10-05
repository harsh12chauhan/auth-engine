using app_engine.Enums;
using System.ComponentModel.DataAnnotations;

namespace app_engine.Models
{
    public class User
    {
        public Guid Id { set; get; }

        [Required]
        [MinLength(3)]
        public string Username { set; get; } = string.Empty;

        [Required]
        [EmailAddress]
        public string Email { set; get; } = string.Empty;

        [Required]
        [MinLength(8)]
        public string PasswordHash { set; get; } = string.Empty;

        public bool IsEnabled { set; get; } = true;
       
        public UserRole Role { set; get; } = UserRole.USER;

        public DateTime CreatedAt { set; get; } = DateTime.UtcNow;

    }
}
