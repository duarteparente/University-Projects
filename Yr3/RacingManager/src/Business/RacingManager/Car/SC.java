package Business.RacingManager.Car;

import java.util.Random;

public class SC extends Car{

    //Constructors
    public SC(){
        super();
    }

    public SC(String brand, String model, int engCapacity, int power, float reliability,float PAC){
        super(brand, model, engCapacity, power, reliability,PAC);
    }

    public SC(SC p){
        super(p);
    }

    @Override
    //Equals
    public boolean equals(Object o){
        if(this == o) return true;
        if((o == null) || (this.getClass() != o.getClass())) return false;
        SC c = (SC) o;
        return (super.equals(c));
    }

    //clone
    public SC clone(){
        return new SC(this);
    }

    public boolean DNF(int volta, float SVA, float CTS, int weather) {
        Random rand=new Random();
        float quality;
        if(weather == 1)
            quality = CTS;
        else
            quality = SVA;

        float reliability = (float)(quality*0.75) + (float)((super.getEngCapicity()/10)*0.25);

        return (0.73 < reliability);
    }
}