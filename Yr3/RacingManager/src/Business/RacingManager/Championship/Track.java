package Business.RacingManager.Championship;


public class Track {

    private String name;
    private int distance;
    private int nrLaps;

    //Constructors
    public Track(){
        this.name = "";
        this.distance = 0;
        this.nrLaps = 0;
    }

    public Track(String name, int distance, int nrLaps){
        this.name = name;
        this.distance = distance;
        this.nrLaps = nrLaps;
    }

    public Track (Track t){
        this.name = t.getName();
        this.distance = t.getDistance();
        this.nrLaps = t.getNrLaps();
    }

    //Getters
    public String getName() {
        return this.name;
    }

    public int getDistance(){
        return this.distance;
    }

    public int getNrLaps(){
        return this.nrLaps;
    }

    //Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setNrLaps(int nrLaps) {
        this.nrLaps = nrLaps;
    }


    @Override
    //Equals
    public boolean equals(Object o){
        if(this == o) return true;
        if((o == null) || (this.getClass() != o.getClass())) return false;
        Track c = (Track) o;
        return (this.name.equals(c.getName()) && this.distance == c.getDistance() && this.nrLaps == c.getNrLaps());
    }

    @Override
    //clone
    public Track clone(){
        return new Track(this);
    }

    public void printTrack(){
        System.out.println("|Name-> " + this.name + "     Distance -> " + this.distance + "     Laps -> " + this.nrLaps);
    }
}
