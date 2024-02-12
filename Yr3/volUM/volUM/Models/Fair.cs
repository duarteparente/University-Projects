using Microsoft.AspNetCore.Identity;
using System.ComponentModel.DataAnnotations;
using System.Net.Http.Headers;

namespace volUM.Models
{
    public class Fair
    {
        [Required]
        public int Id { get; set; }

        [Required]
        [StringLength(45)]
        public string Name { get; set; }

        [Required]
        [StringLength(45)]
        public string Location { get; set; }

        [Required]
        [StringLength(60)]
        public string Description { get; set; }

        [Required]
        public int Capacity { get; set; }

        [Required]
        public DateOnly Date { get; set; }

        [Required]
        public int Duration { get; set; }

        [Required]
        [StringLength(45)]
        public string Category { get; set; }

        [Required]
        public int IdUser { get; set; }


        public Fair()
        {
        }

        public Fair(int id, string name, string location, string description, int capacity, DateOnly date, int duration, string category, int idUser)
        {
            Id = id;
            Name = name;
            Location = location;
            Description = description;
            Capacity = capacity;
            Date = date;
            Duration = duration;
            Category = category;
            IdUser = idUser;
        }
    }
}
