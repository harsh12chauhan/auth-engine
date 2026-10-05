using app_engine.Models;
using Microsoft.EntityFrameworkCore;

namespace app_engine.Data
{
    public class ApplicationDbContext: DbContext
    {

        public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options) : base(options) { }

        public DbSet<User> Users { set; get; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);

            modelBuilder.Entity<User>(entity =>
            {
                entity.ToTable("users", "public");

                entity.Property(u => u.Id).HasColumnName("id");
                entity.Property(u => u.Username).HasColumnName("username");
                entity.Property(u => u.Email).HasColumnName("email");
                entity.Property(u => u.PasswordHash).HasColumnName("hashed_password");
                entity.Property(u => u.Role).HasColumnName("role").HasConversion<string>(); ;
                entity.Property(u => u.IsEnabled).HasColumnName("is_enabled");
                entity.Property(u => u.CreatedAt).HasColumnName("created_at");

                // adding Index on email column for faster response
                entity.HasIndex(u => u.Email).IsUnique().HasDatabaseName("idx_users_email");
            });                                      
        }
    }
}
