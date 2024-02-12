package Business.RacingManager.Car;

import java.util.Random;

public class C1H extends C1 implements Hibrid{

    private int electricEngine;

    //Constructors
    public C1H(int i){
        super();
        this.electricEngine = 0;
    }

    public C1H(String brand, String model, int engCapacity, int power, float reliability,float PAC, int tuning, int electricEngine){
        super(brand,model,engCapacity,power,reliability,PAC,tuning);
        this.electricEngine = electricEngine;
    }

    public C1H(C1H c){
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
        C1H c = (C1H) o;
        return (super.equals(c) && this.electricEngine == c.getElectricEnginePower());
    }

    //clone
    public C1H clone(){
        return new C1H(this);
    }

    public boolean DNF(int lap, float SVA, float CTS, int weather) {
        Random rand=new Random();
        float fiabilidade = (super.getReliability() + (float)(super.getEngCapicity()/1200) + (float)(super.getTuning()/10));
        return (0.98 < fiabilidade);
    }
}
