package Business.RacingManager.Car;

import java.util.Random;

public class C2H extends C2 implements Hibrid{

    private int electricEngine;

    //Constructors
    public C2H(){
        super();
        this.electricEngine = 0;
    }

    public C2H(String brand, String model, int engCapacity, int power, float reliability,float PAC, int tuning, int electricEngine){
        super(brand, model, engCapacity, power, reliability,PAC,tuning);
        this.electricEngine = electricEngine;
    }

    public C2H(C2H c){
        super(c);
        this.electricEngine = c.getElectricEnginePower();
    }

    public int getElectricEnginePower(){
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
        C2H c = (C2H) o;
        return (super.equals(c));
    }

    public boolean DNF(int lap, float SVA, float CTS, int weather) {
        Random rand=new Random();
        float fiabilidade = super.getReliability() + (float)(super.getEngCapicity()/1200) + (float)(super.getTuning()/10);
        return (0.83 < fiabilidade);
    }
}
