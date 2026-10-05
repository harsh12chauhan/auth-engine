using System.ComponentModel.DataAnnotations;

namespace app_engine.Dtos
{
    public class UserLoginRequest
    {        
        [Required]
        [EmailAddress]
        public string Email { set; get; } = string.Empty;

        [Required]
        [MinLength(8)]
        public string Password { set; get; } = string.Empty;
    }
}
