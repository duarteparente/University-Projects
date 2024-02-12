import java.io.Serializable;
import java.util.Objects;

public abstract class SmartDevice implements Serializable {

    private String id;
    private double cost;
    private boolean On;
    private double consumption;

    public abstract double dailyCost(double fixedValue);

    // Construtor SmartDevice
    public SmartDevice() {
        this.id = "";
        this.cost = 0;
        this.On = false;
        this.consumption=0;
    }

    // Construtor SmartDevice
    public SmartDevice(String id) {
        this.id = id;
        this.cost = 0;
        this.On = false;
        this.consumption=0;
    }

    // Construtor SmartDevice
    public SmartDevice(String s, boolean isOn, double consumption) {
        this.id = s;
        this.cost = 0;
        this.On = isOn;
        this.consumption = consumption;
    }

    // Construtor SmartDevice
    public SmartDevice(String s, double cost, boolean isOn, double consumption) {
        this.id = s;
        this.cost = cost;
        this.On = isOn;
        this.consumption = consumption;
    }

    // Construtor SmartDevice
    public SmartDevice(SmartDevice s) {
        this.id = s.getId();
        this.On = s.isOn();
        this.consumption = s.getConsumption();
    }

    // Get do ID do Device
    public String getId() {
        return this.id;
    }

    // Set do ID do Device
    public void setId(String id) {
        this.id = id;
    }

    // Get do Custo do Device
    public double getCost() {
        return this.cost;
    }

    // Set do Custo do Device
    public void setCost(double cost){
        this.cost = cost;
    }

    // Verifica se o Device está Ligado
    public boolean isOn() {
        return this.On;
    }

    // Liga o Device
    public void setOn(boolean on){
        this.On = on;
    }

    // Get do Consumo do Device
    public double getConsumption(){
        return this.consumption;
    }

    // Set do Consumo do Device
    public void setConsumption(double consumption){
        this.consumption=consumption;
    }

    // Get do Custo Diário do Device
    public double getDailyCost(){
        return this.dailyCost(this.getConsumption());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cost, On);
    }

    @Override
    // Equals do Device
    public boolean equals(Object o){
        if(this == o) return true;
        if((o == null) || (this.getClass() != o.getClass())) return false;
        SmartDevice c = (SmartDevice) o;
        return ((this.On == c.isOn()) && (this.id.equals(c.getId())) && this.cost == c.getCost() && this.dailyCost(this.getConsumption()) == c.getDailyCost());
    }

    //Clone do Device
    public abstract SmartDevice clone();
}
