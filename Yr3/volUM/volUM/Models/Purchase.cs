using System.ComponentModel.DataAnnotations;

namespace volUM.Models
{
    public class Purchase
    {
        [Required]
        public int Id { get; set; }

        [Required]
        public DateOnly Date { get; set; }

        [Required]
        public float Value { get; set; }

        [Required]
        [StringLength(15)]
        public string PaymentMethod { get; set; }

        [Required]
        public int UserId { get; set; }


        public Purchase() 
        { 
        }

        public Purchase(int id, DateOnly date, float value, string paymentMethod, int userId)
        {
            Id = id;
            Date = date;
            Value = value;
            PaymentMethod = paymentMethod;
            UserId = userId;
        }

        public Purchase(DateOnly date, float value, string paymentMethod, int userId)
        {
            Date = date;
            Value = value;
            PaymentMethod = paymentMethod;
            UserId = userId;
        }
    }
}
