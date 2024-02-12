import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

public class SmartCity implements Serializable {
    private LocalDate date;
    private Map<String, Supplier> suppliers;
    private List<SmartHouse> smartHouses;

    public SmartCity(){
        this.date = LocalDate.now();
        Map<String,Supplier> suppliers = new HashMap<>();
        List<SmartHouse> smartHouses = new ArrayList<>();
    }

    // Construtor Smartcity
    public SmartCity(LocalDate date, Map<String, Supplier> suppliers, List<SmartHouse> smartHouses) throws EntityMissingException {
        this.date = date;
        this.setSuppliers(suppliers);
        this.setSmartHouses(smartHouses);
        this.connectSmartHouses();
    }

    // Adiciona casa às casas do fornecedor
    public void connectSmartHouses() throws EntityMissingException {
        for(SmartHouse i : this.smartHouses ){
            if (!this.suppliers.containsKey(i.getSupplier().getCompany()))
                throw new EntityMissingException("Fornecedor nao existe");
            this.suppliers.get(i.getSupplier().getCompany()).addSmartHouse(i);
        }
    }

    public void connectBills(){
        for(Map.Entry<String, Supplier> i : this.suppliers.entrySet()){
            Map<Owner, SmartHouse> pin = i.getValue().getSmartHouses();
            for(Map.Entry<Owner, SmartHouse> j : pin.entrySet()){
                this.smartHouses.get(this.smartHouses.indexOf(j.getValue())).setBills(j.getValue().getBills());
            }
        }
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    // Set dos fornecedores
    public void setSuppliers(Map<String, Supplier> ls){
        this.suppliers = new HashMap<>();
        for(Map.Entry<String,Supplier> i : ls.entrySet()){
            this.suppliers.put(i.getKey(), i.getValue().clone());
        }
    }


    // Set da SmartHouse
    public void setSmartHouses(List<SmartHouse> ls){
        this.smartHouses = new ArrayList<>();
        for(SmartHouse i : ls){
            this.smartHouses.add(i.clone());
        }
    }

    // Gera as faturas relativas ao tempo dado como argumento
    public void moveTime(LocalDate start, LocalDate end){
        for(Map.Entry<String,Supplier> i : this.suppliers.entrySet()){
            i.getValue().generateBills(start,end);
        }
        this.connectBills();
    }

    //Retorna o fornecedor cujo nome é passado por argumento
    public Supplier findSupplier(String supp) throws EntityMissingException {
        if (!this.suppliers.containsKey(supp))
            throw new EntityMissingException("Fornecedor nao existe");
        return this.suppliers.get(supp);
    }

    // Devolve a casa que gastou mais
    public SmartHouse houseMostSpentInSimulation(){
        List<SmartHouse> novo = this.smartHouses.stream().sorted(SmartHouse::compareTo).collect(Collectors.toList());
        return novo.get(0);
    }

    // Devolve o fornecedor com maior volume de faturação
    public String[] higherTotalBillingSupplier(){
        String[] supplier = new String[2];
        double total=0;
        for(Map.Entry<String, Supplier> i : this.suppliers.entrySet()){
            if(i.getValue().totalBilling()>total){
                supplier[0]=i.getKey();
                supplier[1]=Double.toString(i.getValue().totalBilling());
                total = i.getValue().totalBilling();
            }
        }
        return supplier;
    }

    // Devolve o conjunto dos maiores Consumidores
    public List<SmartHouse> biggestEnergyConsumers(LocalDate startDate, LocalDate endDate) {
        Iterator<SmartHouse> iterator = this.smartHouses.iterator();
        List<SmartHouse> result = new ArrayList<>();
        while (iterator.hasNext()) {
            SmartHouse i = iterator.next();
            Set<Bill> bills = i.getBillsWDates(startDate, endDate);
            SmartHouse novo = new SmartHouse(i.getOwner(),i.getDevices(),i.getHouseParts(),bills,i.getSupplier());
            result.add(novo.clone());
        }
        result.sort(SmartHouse::compareTo);
        return result;
    }

    // Devolve a diferença entre duas datas (em dias)
    public static int dayDifference(LocalDate start, LocalDate end){
        long days = DAYS.between(start, end);
        return (int) days;
    }

    // Guarda o estado
    public void saveState() throws IOException {
        FileOutputStream f = new FileOutputStream("data/state.obj");
        ObjectOutputStream o = new ObjectOutputStream(f);
        o.writeObject(this);
        o.flush();
        o.close();
    }
    
    // Carrega o estado
    public SmartCity loadState(String file) throws IOException, ClassNotFoundException {
        FileInputStream f = new FileInputStream(file);
        ObjectInputStream o = new ObjectInputStream(f);
        SmartCity sc = (SmartCity) o.readObject();
        o.close();
        return sc;
    }
}
