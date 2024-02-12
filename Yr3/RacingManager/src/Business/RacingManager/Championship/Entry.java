package Business.RacingManager.Championship;

import Business.RacingManager.Car.Car;

public class Entry {
    private Car car;
    private Driver driver;
    private long time = 0;
    private long lapTime = 0;

    public Entry(Car car, Driver driver){
        this.car = car;
        this.driver = driver;
    }

    public Entry(Entry entry){
        this.car = entry.getCar();
        this.driver = entry.getDriver();
    }

    public Car getCar(){
        return this.car;
    }

    public Driver getDriver(){
        return this.driver;
    }

    public long getTime(){
        return this.time;
    }

    public void setTime(long lapTime){
        this.time += lapTime;
        this.lapTime = lapTime;
    }

    public boolean DNF(int lap,int weather){
        return this.car.DNF(lap,this.driver.getSVA(),this.driver.getCTS(),weather);
    }

    public long nextLapTime(int weather) {
        long minimum = 0, maximum = 5000;
        long luck = minimum + Double.valueOf(Math.random() * (maximum - minimum)).intValue();
        long maximum_rain = 2000;
        long rain_luck = minimum + Double.valueOf(Math.random() * (maximum_rain - minimum)).intValue();

        return (long) ((((this.car.getEngCapicity() / this.car.getPower()) - this.driver.getSVA()) * 1000) - luck
                + (weather * (this.driver.getCTS() * 1000)) - rain_luck);
    }

    public int compareTo(Entry e)
    {
        return Double.compare(this.time, e.getTime());
    }

    public boolean equals(Object o){
        if(this == o) return true;
        if((o == null) || (this.getClass() != o.getClass())) return false;
        Entry c = (Entry) o;
        return (this.car.equals(c.getCar()) && this.driver.equals(c.getDriver()));
    }


    public Entry clone(){
        return new Entry(this);
    }
}
