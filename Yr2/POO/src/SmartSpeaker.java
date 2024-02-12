
public class SmartSpeaker extends SmartDevice {
    
    private int volume;
    private String chanel;
    private String brand;


    @Override
    public double dailyCost(double fixedValue) {
        if (super.isOn()) return (this.volume*0.5 / 30.0) + this.getConsumption();
        return 0;
    }

    //Construtor SmartSpeaker
    public SmartSpeaker() {
        super();
        this.chanel = "";
        this.volume = 0;
        this.brand = "";
    }

    //Construtor SmartSpeaker
    public SmartSpeaker(String id) {
        super(id);
        this.chanel = "";
        this.volume = 10;
        this.brand = "";
    }

    //Construtor SmartSpeaker
    public SmartSpeaker(String id, String chanel, int volume, String brand) {
        super(id);
        this.chanel = chanel;
        this.volume = volume;
        this.brand = brand;
    }

    //Construtor SmartSpeaker
    public SmartSpeaker(String id, double cost, boolean isOn, double consumption, String chanel, int volume, String brand){
        super(id, cost, isOn, consumption);
        this.chanel = chanel;
        this.volume = volume;
        this.brand = brand;
    }

    //Construtor SmartSpeaker
    public SmartSpeaker(SmartSpeaker clone) {
        super(clone.getId(), clone.getCost(), clone.isOn(),clone.getConsumption());
        this.volume = clone.getVolume();
        this.chanel= clone.getChannel();
        this.brand = clone.getBrand();
    }

    //aumenta o volume da Coluna
    public void volumeUp() {
        this.volume++;
    }
    
    //diminui o volume da Coluna
    public void volumeDown() {
        if (this.volume>0) this.volume--;
    }
    
    // set do volume da Coluna
    public void setVolume(int volume){
        this.volume = volume;
    }
    
    // get do volume da Coluna
    public int getVolume() {
        return this.volume;
    }
    
    // get do nome da estacao de radio
    public String getChannel() {
        return this.chanel;
    }
    
    // set do nome da estacao de radio
    public void setChannel(String c) {
        this.chanel = c;
    }
    
    // get da Marca da Coluna
    public String getBrand(){
        return this.brand;
    }
    
    // set da Marca da Coluna
    public void setBrand(String brand){
        this.brand=brand;
    }

    // equals da Coluna
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || this.getClass() != o.getClass()) return false;
        SmartSpeaker s = (SmartSpeaker) o;
        return (super.equals(o)
                 && this.volume == s.getVolume()
                 && this.chanel.equals(s.getChannel()))
                 && this.brand.equals(s.getBrand());
    
        }

    @Override
    // clone da Coluna
    public SmartSpeaker clone() {
        return new SmartSpeaker(this);
    }

}
