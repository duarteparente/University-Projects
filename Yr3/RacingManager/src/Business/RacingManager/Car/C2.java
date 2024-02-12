package Business.RacingManager.Car;

import java.util.Random;

public class C2 extends Car{

    private int tuning;

    //Constructors
    public C2(){
        super();
    }

    public C2(String brand, String model, int engCapacity, int power, float reliability,float PAC, int tuning){
        super(brand,model,engCapacity,power,reliability,PAC);
        this.tuning = tuning;
    }

    public C2(C2 p){
        super(p);
        this.tuning = p.getTuning();
    }

    public int getTuning(){
        return this.tuning;
    }

    public void setTuning(int tuning){
        this.tuning = tuning;
    }

    @Override
    //Equals
    public boolean equals(Object o){
        if(this == o) return true;
        if((o == null) || (this.getClass() != o.getClass())) return false;
        C2 c = (C2) o;
        return (super.equals(c) && this.tuning == c.getTuning());
    }

    //clone
    public C2 clone(){
        return new C2(this);
    }

    @Override
    public boolean DNF(int lap, float SVA, float CTS, int weather) {
        Random rand=new Random();
        float fiabilidade = (float) (super.getReliability() + (float)(super.getEngCapicity()/1200) + (float)(this.tuning/10));
        return (0.8 < fiabilidade);
    }
}