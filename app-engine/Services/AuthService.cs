using app_engine.Data;
using app_engine.Dtos;
using app_engine.Enums;
using app_engine.Exceptions;
using app_engine.Interfaces;
using app_engine.Models;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using System.Diagnostics;

namespace app_engine.Services
{
    public class AuthService(ApplicationDbContext _context) : IAuthService
    {
        public async Task<UserResponse> AuthenticateUser(UserLoginRequest userLoginRequest)
        {
            var total = Stopwatch.StartNew();

            var connection = _context.Database.GetDbConnection();

            var connectionTimer = Stopwatch.StartNew();

            if (connection.State != System.Data.ConnectionState.Open)
            {
                await connection.OpenAsync();
            }

            connectionTimer.Stop();

            Console.WriteLine(
                $"DB connection open: {connectionTimer.ElapsedMilliseconds} ms"
            );

            var queryTimer = Stopwatch.StartNew();

            var user = await _context.Users
                .AsNoTracking()
                .FirstOrDefaultAsync(x => x.Email == userLoginRequest.Email);

            queryTimer.Stop();

            Console.WriteLine(
                $"DB query: {queryTimer.ElapsedMilliseconds} ms"
            );

            total.Stop();

            Console.WriteLine(
                $"DB total: {total.ElapsedMilliseconds} ms"
            );

            if (user is null)
            {
                throw new ApiException("Invalid user credentials", StatusCodes.Status401Unauthorized);
            }

            // later on will use Bcrypt, as in java we are using bcrypt (package to include : BCrypt.Net-Next)
            //var isPasswordValid = BCrypt.Net.BCrypt.Verify(userLoginRequest.Password,user.PasswordHash);
            //if (!isPasswordValid){
            //    throw new ApiException("Invalid user credentials",StatusCodes.Status401Unauthorized);
            //}

            var isPasswordValid = new PasswordHasher<User>().VerifyHashedPassword(user, user.PasswordHash,userLoginRequest.Password);

            if (isPasswordValid == PasswordVerificationResult.Failed) {
                throw new ApiException("Invalid user credentials", StatusCodes.Status401Unauthorized);
            }

            //Console.WriteLine($"Password verification: {sw.ElapsedMilliseconds} ms");
            //sw.Restart();

            return new UserResponse
            {
                Id = user.Id,
                Username = user.Username,
                Email = user.Email,
                Role = user.Role,
                IsEnabled = user.IsEnabled,
                CreatedAt = user.CreatedAt
            };
        }

        public async Task<UserResponse> CreateUser(CreateUserRequest createUserRequest)
        {
            var IsEmailAlreadyExist = await _context.Users
                .AsNoTracking()
                .AnyAsync(u => u.Email == createUserRequest.Email);

            var IsUsrenameAlreadyExist = await _context.Users
                .AsNoTracking()
                .AnyAsync(u => u.Username == createUserRequest.Username);

            if (IsEmailAlreadyExist || IsUsrenameAlreadyExist)
            {

                throw new ApiException("user with email or username already exits",StatusCodes.Status409Conflict);
            }

            User newUser = new User
            {
                Username = createUserRequest.Username,
                Email = createUserRequest.Email,
                Role = UserRole.USER
            };

            // later on will use Bcrypt, as in java we are using bcrypt (package to include : BCrypt.Net-Next)
            //newUser.PasswordHash = BCrypt.Net.BCrypt.HashPassword(createUserRequest.Password);

            newUser.PasswordHash = new PasswordHasher<User>().HashPassword(newUser, createUserRequest.Password);

            _context.Users.Add(newUser);
            await _context.SaveChangesAsync();

            return new UserResponse
            {
                Id = newUser.Id,
                Username = newUser.Username,
                Email = newUser.Email,
                Role = newUser.Role,
                IsEnabled = newUser.IsEnabled,
                CreatedAt = newUser.CreatedAt
            };
        }
    }
}
