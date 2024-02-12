package Business.RacingManager.Car;

import java.util.Random;

public class GT extends Car{

    private float detTax; //Tax of deterioration

    //Constructors
    public GT(){
        super();
        this.detTax = 0;
    }

    public GT(String brand, String model, int engCapacity, int power, float reliability,float PAC,int detTax){
        super(brand,model,engCapacity,power,reliability,PAC);
        this.detTax = detTax;
    }

    public GT(GT p){
        super(p);
        this.detTax = p.getDetTax();
    }

    //Getters
    public float getDetTax(){
        return this.detTax;
    }

    //Setters
    public void setDetTax(float detTax){
        this.detTax = detTax;
    }

    @Override
    //Equals
    public boolean equals(Object o){
        if(this == o) return true;
        if((o == null) || (this.getClass() != o.getClass())) return false;
        GT c = (GT) o;
        return (this.detTax == c.getDetTax() && super.equals(c));
    }

    //clone
    public GT clone() {
        return new GT(this);
    }

    public boolean DNF(int lap, float SVA, float CTS, int weather){
        Random rand = new Random();
        float reliability = (float)((100000 / (float)super.getEngCapicity()) * 0.0255);
        float det = (lap + 1) * this.detTax;
        return (0.7 < (reliability - det));
    }
}