import java.io.Serializable;
import java.time.LocalDate;

public class Bill implements Comparable<Bill>, Serializable {


    private LocalDate startDate;
    private LocalDate finalDate;
    private double value;
    private String company;
    private Owner owner;

    // Construtor Bill
    public Bill(){
        this.startDate = LocalDate.now();
        this.finalDate = LocalDate.now();
        this.value = 0;
        this.company= "";
    }

    // Construtor Bill
    public Bill(LocalDate startDate, LocalDate finalDate, double value, String company, Owner owner){
        this.startDate = startDate;
        this.finalDate = finalDate;
        this.value = value;
        this.company=company;
        this.owner = owner.clone();
    }

    // Construtor Bill
    public Bill(Bill b){
        this.startDate = b.getStartDate();
        this.finalDate = b.getFinalDate();
        this.value = b.getValue();
        this.company = b.getCompany();
        this.owner = b.getOwner();
    }
    
    // get da Data Inicial da Fatura
    public LocalDate getStartDate(){
        return this.startDate;
    }
    
    // set da Data Inicial da Fatura
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    // get da Data FInal da Fatura
    public LocalDate getFinalDate() {
        return this.finalDate;
    }
    
    // set da Data Final da Fatura
    public void setFinalDate(LocalDate finalDate){
        this.finalDate = finalDate;
    }
    
    // get do valor da Fatura
    public double getValue() {
        return this.value;
    }
    
    // set do valor da Fatura
    public void setValue(double value) {
        this.value = value;
    }
    
    // get do fornecedor da Fatura
    public String getCompany(){ 
        return this.company;
    }
    
    // set do fornecedor da Fatura
    public void setCompany(String company){ 
        this.company=company;
    }
    
    // get do proprietario da casa cuja fatura pertence
    public Owner getOwner() { 
        return this.owner.clone();
    }
    
    
    @Override 
    // compare dos valores das Faturas
    public int compareTo(Bill bill) {
        return Double.compare(this.value, bill.getValue());
    }

    @Override 
    //print das informações incluidas nas Faturas
    public String toString(){
        return "{ Period: \n From: " + this.startDate + "\nTo: " + this.finalDate + "\nValue: " + this.value + ", Company: " + this.company + "}\n";
    }

    @Override 
    // equals das Faturas
    public boolean equals(Object o){
        if(this == o) return true;
        if((o == null) || (this.getClass() != o.getClass())) return false;
        Bill c = (Bill) o;
        return ( this.startDate.equals(c.getStartDate()) && this.finalDate.equals(c.getFinalDate()) && this.value == c.getValue() && this.company.equals(c.getCompany()));
    }

    @Override 
    //clone das Faturas
    public Bill clone(){
        return new Bill (this);
    }
}
