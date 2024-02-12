package Business.RacingManager.Car;

import java.util.Random;

public class C1 extends Car {

    private int tuning;
    //Constructors
    public C1(){
        super();
    }

    public C1(String brand, String model, int engCapacity, int power, float reliability,float PAC, int tuning){
        super(brand,model,engCapacity,power,reliability,PAC);
        this.tuning = tuning;
    }

    public C1(C1 p){
        super(p);
    }

    public int getTuning(){
        return this.tuning;
    }
    @Override
    //Equals
    public boolean equals(Object o){
        if(this == o) return true;
        if((o == null) || (this.getClass() != o.getClass())) return false;
        C1 c = (C1) o;
        return (super.equals(c));
    }

    //clone
    public C1 clone(){
        return new C1(this);
    }

    @Override
    public boolean DNF(int lap, float SVA, float CTS, int weather) {
        Random rand=new Random();
        float fiabilidade = (super.getReliability() + ((float)super.getEngCapicity()/1200) + ((float)this.tuning/10))/10;
        return (0.95 < fiabilidade);
    }
}