using System.ComponentModel.DataAnnotations;

namespace volUM.Models
{
    public class Delivery
    {
        [Required]
        public int Id { get; set; }

        [Required]
        public DateOnly Date { get; set; }

        [Required]
        [StringLength(45)]
        public string DeliveryMethod { get; set; }

        [Required]
        public int IdPurchase { get; set; }

        [Required]
        public int IdProduct { get; set; }

        [Required]
        public int IdUser { get; set; }

        
        public Delivery() 
        {
        }

        public Delivery(int id, DateOnly date, string deliveryMethod, int idPurchase, int idProduct, int idUser)
        {
            Id = id;
            Date = date;
            DeliveryMethod = deliveryMethod;
            IdPurchase = idPurchase;
            IdProduct = idProduct;
            IdUser = idUser;
        }
    }
}