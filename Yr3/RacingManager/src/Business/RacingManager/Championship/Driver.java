package Business.RacingManager.Championship;


public class Driver {

    private String name;
    private String country;
    private float CTS; //chuva vs tempo seco
    private float SVA; //segurança vs agressividade

    //Constructors
    public Driver(){
        this.name = "";
        this.country = "";
        this.CTS = 0;
        this.SVA = 0;
    }

    public Driver(String name, String country, float CTS, float SVA){
        this.name = name;
        this.country = country;
        this.CTS = CTS;
        this.SVA = SVA;
    }

    public Driver(Driver d){
        this.name = d.getName();
        this.country = d.getCountry();
        this.CTS = d.getCTS();
        this.SVA = d.getSVA();
    }

    //Getters
    public String getName(){
        return this.name;
    }

    public String getCountry() {
        return this.country;
    }

    public float getCTS() {
        return this.CTS;
    }

    public float getSVA() {
        return this.SVA;
    }

    //Setters
    public void setName(String name){
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCTS(float CTS) {
        this.CTS = CTS;
    }

    public void setSVA(float SVA) {
        this.SVA = SVA;
    }

    @Override
    //Equals
    public boolean equals(Object o){
        if(this == o) return true;
        if((o == null) || (this.getClass() != o.getClass())) return false;
        Driver c = (Driver) o;
        return (this.name.equals(c.getName()) && this.country.equals(c.getCountry()) && this.CTS == c.getCTS() && this.SVA == c.getSVA());
    }

    //clone
    @Override
    public Driver clone(){
        return new Driver(this);
    }

    public String printDriver(){
        return("[Name]-> "+this.name+"   [Country]-> "+this.country+"   [CTS]-> "+this.CTS+"   [SVA]-> "+this.SVA);
    }
}

