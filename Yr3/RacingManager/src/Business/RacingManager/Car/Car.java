package Business.RacingManager.Car;

import java.util.List;

import Business.RacingManager.Championship.Track;

public abstract class Car {

    //private int id;
    private String brand;
    private String model;
    private String tires;
    private int engCapacity;
    private int power;
    private float reliability;
    private float PAC;
    private boolean DNF;
    private long time;
    //private float tuning;

    //Constructors
    public Car(){
    //    this.id = 0;
        this.brand = "";
        this.model = "";
        this.tires = "";
        this.engCapacity = 0;
        this.power = 0;
        this.reliability = 0;
        this.PAC = 0;
        this.DNF = false;
        this.time = 0;
        //this.tuning = 0;
    }

    public Car(String brand, String model, int engCapacity, int power, float reliability,float PAC){
        this.brand = brand;
        this.model = model;
        this.tires = "";
        this.engCapacity = engCapacity;
        this.power = power;
        this.reliability = reliability;
        this.PAC = PAC;
        this.DNF = false;
        this.time = 0;
       // this.tuning = tuning;
    }

    public Car(Car c){
        //this.id = c.getId();
        this.brand = c.getBrand();
        this.model = c.getModel();
        this.tires = c.getTires();
        this.engCapacity = c.getEngCapicity();
        this.power = c.getPower();
        this.reliability = c.getReliability();
        this.PAC = c.getPAC();
        this.DNF = c.getDNF();
        this.time = c.getTime();
        //this.tuning = c.getTuning();
    }

    //Getters
    //public int getId() {return this.id;}

    public String getBrand(){
        return this.brand;
    }

    public String getModel(){
        return this.model;
    }

    public String getTires(){
        return this.tires;
    }

    public int getEngCapicity(){return this.engCapacity;}

    public int getPower(){return this.power;}

    public float getReliability(){return this.reliability;}

    public float getPAC(){
        return this.PAC;
    }

    public boolean getDNF(){
        return this.DNF;
    }

    public long getTime(){
        return this.time;
    }

    //public float getTuning(){return this.tuning;}

    //Setters
    //public void setId(int id){this.id = id;}

    public void setBrand(String brand){
        this.brand = brand;
    }

    public void setModel(String model){
        this.model = model;
    }

    public void setTire(String tires){
        this.tires = tires;
    }

    public void setEngCapacity(int engCapacity){this.engCapacity = engCapacity;}

    public void setPower(int power){this.power = power;}

    public void setReliability(float reliability){this.reliability = reliability;}

    public void setPAC(float PAC){
        this.PAC = PAC;
    }

    public void setDNF(boolean DNF){
        this.DNF = DNF;
    }

    public void setTime(long time){
        this.time = time;
    }

    //public void setTuning(float tuning){this.tuning = tuning;}


    public void changePAC (float newPAC){
        this.PAC = newPAC;
    }

    public long nextLapTime (Track t, int lap, int weather){
        return 0;
    }

    public boolean carDNF(){
        return false;
    }

    //public void changeTuning(float newTuning){this.tuning = newTuning;}

    public void chooseTires(List<String> tires){

    }

    @Override
    //Equals
    public boolean equals(Object o){
        if(this == o) return true;
        if((o == null) || (this.getClass() != o.getClass())) return false;
        Car c = (Car) o;
        return (this.brand.equals(c.getBrand()) && this.model.equals(c.getModel()) && this.tires.equals(c.getTires()) && this.engCapacity == c.getEngCapicity() && this.power == c.getPower() && this.reliability == c.getReliability() && this.PAC == c.getPAC() && this.DNF == c.getDNF() && this.time == c.getTime());// && this.tuning == c.getTuning());
    }

    //clone
    @Override
    public abstract Car clone();

    public String printCar(){
        return("[Brand]-> " + this.brand + "   [Model]-> "+ this.model +"   [Engine capacity]-> "+this.engCapacity+"   [Power]-> "+this.power);
    }

    public abstract boolean DNF(int lap, float SVA, float CTS, int weather);
}
