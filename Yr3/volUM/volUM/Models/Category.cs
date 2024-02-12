using System.ComponentModel.DataAnnotations;

namespace volUM.Models
{
    public class Category
    {
        [Required]
        [StringLength(20)]
        public string Name { get; set; }

        [Required]
        public int IdFair { get; set; }

        
        public Category()
        {
        }

        public Category(string name, int idFair)
        {
            this.name = name;
            this.idFair = idFair;
        }
    }
}
