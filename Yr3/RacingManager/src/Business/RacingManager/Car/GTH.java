package Business.RacingManager.Car;

import java.util.Random;

public class GTH extends GT implements Hibrid{

    private int electricEngine;

    //Constructors
    public GTH(){
        super();
        this.electricEngine = 0;
    }

    public GTH(String brand, String model, int engCapacity, int power, float reliability,float PAC ,int detTax, int electricEngine){
        super(brand, model, engCapacity, power,reliability,PAC, detTax);
        this.electricEngine = electricEngine;
    }

    public GTH(GTH p){
        super(p);
        this.electricEngine = p.getElectricEnginePower();
    }


    public int getElectricEnginePower() {
        return this.electricEngine;
    }

    public void setElectricEnginePower(int electricEngine){
        this.electricEngine = electricEngine;
    }

    @Override
    //Equals
    public boolean equals(Object o){
        if(this == o) return true;
        if((o == null) || (this.getClass() != o.getClass())) return false;
        GTH c = (GTH) o;
        return (super.equals(c));
    }

    //clone
    public GTH clone(){
        return new GTH(this);
    }

    @Override
    public boolean DNF(int lap, float SVA, float CTS, int weather) {
        Random rand = new Random();
        float reliability = (float)((100000 / (float)super.getEngCapicity()) * 0.0255);
        float det = (lap + 1) * super.getDetTax();
        return (0.75 < (reliability - det));
    }
}
