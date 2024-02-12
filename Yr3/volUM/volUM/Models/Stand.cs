using System.ComponentModel.DataAnnotations;

namespace volUM.Models
{
    public class Stand
    {
        [Required]
        public int Id { get; set; }

        [Required]
        [StringLength(45)]
        public string Name { get; set; }

        [Required]
        public int NumEmployees { get; set; }

        [Required]
        [StringLength(60)]
        public string Description { get; set; }

        [Required]
        public int IdFair { get; set; }


        public Stand()
        {
        }

        public Stand(int id, string name, int numEmployees, string description, int idFair)
        {
            Id = id;
            Name = name;
            NumEmployees = numEmployees;
            Description = description;
            IdFair = idFair;
        }
    }
}
