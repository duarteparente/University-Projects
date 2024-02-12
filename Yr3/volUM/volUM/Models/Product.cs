using System.ComponentModel.DataAnnotations;

namespace volUM.Models
{
    public class Product
    {
        [Required]
        public int Id { get; set; }

        [Required]
        public int Stock { get; set; }

        [Required]
        public float Price { get; set; }

        [Required]
        [StringLength(45)]
        public string ProductionPlace { get; set; }

        [Required]
        [StringLength(60)]
        public string Description { get; set; }


        public Product()
        {
        }

        public Product(int id, int stock, float price, string productionPlace, string description)
        {
            Id = id;
            Stock = stock;
            Price = price;
            ProductionPlace = productionPlace;
            Description = description;
        }
    }
}
