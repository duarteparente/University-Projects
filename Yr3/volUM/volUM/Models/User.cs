using System.ComponentModel.DataAnnotations;

namespace volUM.Models
{
    public class User
    {
        [Required]
        public int Id { get; set; }

        [Required]
        [StringLength(45)]
        public string Name { get; set; }

        [Required]
        [StringLength(9)]
        public string NumPhone { get; set; }

        [Required]
        [StringLength(45)]
        public string Email { get; set; }

        [Required]
        [StringLength(75)]
        public string Address { get; set; }

        [Required]
        [StringLength(15)]
        public string PostalCode { get; set; }

        [Required]
        [StringLength(45)]
        public string City { get; set; }

        [Required]
        [StringLength(30)]
        public string Password { get; set; }


        public User()
        {
        }

        public User(int id, string name, string numPhone, string email, string address, string postalCode, string city, string password)
        {
            Id = id;
            Name = name;
            NumPhone = numPhone;
            Email = email;
            Address = address;
            PostalCode = postalCode;
            City = city;
            Password = password;
        }
    }
}
