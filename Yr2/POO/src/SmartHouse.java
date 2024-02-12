import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


public class SmartHouse implements Comparable<SmartHouse>, Serializable {

    private Owner owner;
    private Map<String, SmartDevice> devices;
    private Map<String, List<String>> houseParts;
    private Set<Bill> bills;
    private Supplier supplier;

    public SmartHouse(){
        this.owner = new Owner();
        this.devices = new HashMap();
        this.houseParts = new HashMap();
        this.bills = new TreeSet<>();
        this.supplier = new Supplier();
    }

    // Construtor smartHouse
    public SmartHouse(Owner owner, Map<String, SmartDevice> devices, Map<String, List<String>> houseParts, Set<Bill> bills, Supplier supplier){
        this.owner = owner.clone();
        this.setDevices(devices);
        this.setHouseParts(houseParts);
        this.setBills(bills);
        this.supplier = supplier.clone();

    }
    // Construtor smartHouse
    public SmartHouse(SmartHouse h){
        this.owner = h.getOwner();
        this.setDevices(h.getDevices());
        this.setHouseParts(h.getHouseParts());
        this.setBills(h.getBills());
        this.setSupplier(h.getSupplier());
    }

    // Construtor smartHouse
    public SmartHouse(Owner owner, Supplier supplier){
        this.devices = new HashMap();
        this.houseParts = new HashMap();
        this.bills = new TreeSet<>();
        this.owner=owner.clone();
        this.supplier=supplier.clone();
    }

    // Get do proprietario da Casa
    public Owner getOwner(){
        return this.owner.clone();
    }

    // Set do proprietario da Casa
    public void setOwner(Owner o){
        this.owner = o.clone();
    }

    // Get dos dispositivos da Casa
    public Map<String, SmartDevice> getDevices(){
        Map<String, SmartDevice> d = new HashMap<>();
        for(Map.Entry<String, SmartDevice> i : this.devices.entrySet()){
            d.put(i.getKey(), i.getValue().clone());
        }
        return d;
    }

    // Set dos dispositivos da Casa
    public void setDevices(Map<String, SmartDevice> devices) {
        this.devices = new HashMap<>();
        for(Map.Entry<String, SmartDevice> i : devices.entrySet()){
            this.devices.put(i.getKey(), i.getValue().clone());
        }
    }

    // Get das divisoes da Casa
    public Map<String, List<String>> getHouseParts(){
        Map<String, List<String>> h = new HashMap<>();
        for(Map.Entry<String, List<String>> i : houseParts.entrySet()){
            h.put(i.getKey(), i.getValue());
        }
        return h;
    }

    // Set das divisoes da Casa
    public void setHouseParts(Map<String, List<String>> houseParts){
        this.houseParts = new HashMap<>();
        for(Map.Entry<String, List<String>> i : houseParts.entrySet()){
            this.houseParts.put(i.getKey(), i.getValue());
        }
    }

    // Get das Faturas da Casa
    public Set<Bill> getBills() {
        Set<Bill> novo = new TreeSet<>();
        for(Bill i : this.bills){
            novo.add(i.clone());
        }
        return novo;
    }

    //devolve as faturas cujas datas estao entre as passadas como argumento
    public Set<Bill> getBillsWDates(LocalDate start, LocalDate end){
        return this.bills.stream().map(Bill::clone).filter(v -> (!v.getStartDate().isBefore(start) && !v.getFinalDate().isAfter(end))).collect(Collectors.toSet());
    }

    // Set das Faturas da Casa
    public void setBills(Set<Bill> bills) {
        this.bills = new TreeSet<>();
        for(Bill i : bills) {
            this.bills.add(i.clone());
        }
    }

    // Get do fornecedor
    public Supplier getSupplier(){
        return this.supplier.clone();
    }

    // Set do fornecedor
    public void setSupplier(Supplier supplier){
        this.supplier = supplier.clone();
    }


    // Get do dispositivo cujo nome é passado como argumento
    public SmartDevice getDevice(String s) {
        return this.devices.get(s).clone();
    }

    // adiciona uma divisao á Casa
    public void addRoom(String s) {
        this.houseParts.put(s,new ArrayList<>());
    }

    // Verifica se a casa tem a divisao
    public boolean hasRoom(String s) {
        return this.houseParts.containsKey(s);
    }

    // Verifica se a divisao tem o dispositivo
    public boolean roomHasDevice (String room, String device) {
        return this.houseParts.get(room).contains(device) && this.devices.containsKey(device);
    }

    // Adiciona um dispositivo a uma divisao
    public void addDeviceToRoom (String room, SmartDevice device) {
        this.houseParts.get(room).add(device.getId());
        this.devices.put(device.getId(), device.clone());
    }

    // turns off/on device
    public void changeStateDevice(String id, boolean b) throws EntityMissingException {
        if (!this.devices.containsKey(id))
            throw new EntityMissingException("Device nao existe nesta casa");
        this.devices.get(id).setOn(b);
    }

    // turns off/on all devices in room
    public void changeStateRoom(String room, boolean b) throws EntityMissingException {
        if (!this.houseParts.containsKey(room))
            throw new EntityMissingException("Divisao nao existe nesta casa");
        List<String> ids = this.houseParts.get(room);
        for(String i : ids){
            this.devices.get(i).setOn(b);
        }
    }

    // Devolve o Consumo Total da Casa
    public double totalConsumption(){
        double value = 0;
        for(Map.Entry<String, SmartDevice> i : this.devices.entrySet()){
            value += this.getSupplier().PricePerDay(i.getValue());
        }
        return value;
    }

    // Adiciona a Fatura á casa
    public void addBill(Bill b){
        this.bills.add(b.clone());
    }

    // Devolve o custo total de instalacao dos dispositivos da casa
    public double totalInstallationCost(){
        double counter = 0;
        for(Map.Entry<String,SmartDevice> i : this.devices.entrySet()){
            counter += i.getValue().getCost();
        }
        return counter;
    }

    // Devolve o valor total das faturas
    public double totalBilling(){
        double counter = 0;
        for(Bill i : this.bills){
            counter += i.getValue();
        }
        return counter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, supplier);
    }


    @Override
    // Equals da Casa
    public boolean equals(Object o){
        if(this == o) return true;
        if((o == null) || (this.getClass() != o.getClass())) return false;
        SmartHouse c = (SmartHouse) o;
        return (this.owner.equals(c.getOwner()) && this.devices.equals(c.getDevices()) && this.houseParts.equals(c.getHouseParts()) && this.supplier.equals(c.getSupplier()));
    }

    @Override
    // Clone da Casa
    public SmartHouse clone(){
        return new SmartHouse( this );
    }


    @Override
    // Compara as casas
    public int compareTo(SmartHouse smartHouse) {
        return Double.compare((smartHouse.totalBilling() + smartHouse.totalInstallationCost()), (this.totalBilling() + this.totalInstallationCost()));
    }
}
