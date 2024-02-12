import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class Supplier implements Comparable<Supplier>, Serializable {

    private String company;
    private double baseCost;
    private double taxFactor;
    private Set<Bill> bills;
    private Map<Owner, SmartHouse> smartHouses;

    // Construtor Supplier
    public Supplier(){
        this.company = "EDP Comercial";
        this.baseCost = 0.15;
        this.taxFactor = 2.85;
        this.bills = new TreeSet<>();
        this.smartHouses = new HashMap<>();
    }

    // Construtor Supplier
    public Supplier(String company, double baseCost, double taxFactor){
        this.company = company;
        this.baseCost = baseCost;
        this.taxFactor = taxFactor;
        this.bills = new TreeSet<>();
        this.smartHouses = new HashMap<>();
    }

    // Construtor Supplier
    public Supplier(Supplier s){
        this.company = s.getCompany();
        this.baseCost = s.getBaseCost();
        this.taxFactor = s.getTaxFactor();
        this.bills = s.getBills();
        this.setSmartHouses(s.getSmartHouses());
    }

    // Get do fornecedor
    public String getCompany(){
        return this.company;
    }

    // Set do fornecedor
    public void setCompany(String company){
        this.company = company;
    }

    // Devolve o custo base
    public double getBaseCost(){
        return this.baseCost;
    }

    // Set do custo base
    public void setBaseCost(double baseCost) {
        this.baseCost = baseCost;
    }

    // Get do imposto
    public double getTaxFactor() {
        return this.taxFactor;
    }

    // Set do imposto
    public void setTaxFactor(double taxFactor){
        this.taxFactor = taxFactor;
    }

    // Get das Faturas
    public Set<Bill> getBills() {
        Set<Bill> novo = new TreeSet<>();
        for(Bill i : this.bills){
            novo.add(i.clone());
        }
        return novo;
    }

    // Get das Casas pertencentes ao fornecedor
    public Map<Owner, SmartHouse> getSmartHouses() {
        Map<Owner, SmartHouse> novo = new HashMap<>();
        for(Map.Entry<Owner, SmartHouse> i : this.smartHouses.entrySet()){
            novo.put(i.getKey().clone(), i.getValue().clone());
        }
        return novo;
    }

    // Adiciona uma Casa
    public void addSmartHouse(SmartHouse i){
        this.smartHouses.put(i.getOwner(), i.clone());
    }

    // Set das Casas
    public void setSmartHouses(Map<Owner, SmartHouse> n) {
        this.smartHouses = new HashMap<>();
        for(Map.Entry<Owner, SmartHouse> i : n.entrySet()){
            this.smartHouses.put(i.getKey().clone(), i.getValue().clone());
        }
    }

    // custo diario
    public double PricePerDay(SmartDevice sd){
        return this.baseCost * sd.getDailyCost() * this.taxFactor * 0.15;
    }

    // Gera uma Fatura
    public Bill generateBill(LocalDate start, LocalDate end, SmartHouse sm, Owner owner){
        int days = SmartCity.dayDifference(start,end);
        double value = 0;
        for(int i=0; i<days; i++){
            value += sm.totalConsumption();
        }
        return new Bill(start,end,value,this.company,owner);
    }

    // Gera as várias Faturas
    public void generateBills(LocalDate start, LocalDate end){
        for(Map.Entry<Owner, SmartHouse> i : this.smartHouses.entrySet()){
            Bill b = generateBill(start,end,i.getValue(),i.getKey());
            this.bills.add(b.clone());
            i.getValue().addBill(b.clone());
        }
    }

    // Devolve o valor total das Faturas
    public double totalBilling(){
        double counter = 0;
        for(Bill i : this.bills){
            counter += i.getValue();
        }
        return counter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(company, baseCost, taxFactor);
    }

    @Override
    // Equals do Suprimentos
    public boolean equals(Object o){
        if(this == o) return true;
        if((o == null) || (this.getClass() != o.getClass())) return false;
        Supplier c = (Supplier) o;
        return ( this.company.equals(c.getCompany()) && this.baseCost == c.getBaseCost() && this.taxFactor == c.getTaxFactor() && this.bills.equals(c.getBills()));
    }

    @Override
    // Clone dos Suprimentos
    public Supplier clone(){
        return new Supplier( this );
    }

    @Override
    //Compare dos Suprimentos
    public int compareTo(Supplier supplier) {
        return Double.compare(supplier.totalBilling(), this.totalBilling());
    }
}
