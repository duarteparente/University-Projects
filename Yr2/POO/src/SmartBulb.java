
public class SmartBulb extends SmartDevice {
    public static final int Warm = 2;
    public static final int Cold = 1;
    public static final int Neutral = 0;

    private int tone;
    private int dimension;

    //Construtor smartbulb
    public SmartBulb() {
        super();
        this.tone = Neutral;
        this.dimension = 60; // ??
    }
    //Construtor smartBulb
    public SmartBulb(String id){
        super(id);
        this.tone = Neutral;
        this.dimension = 60;
    }

    //Construtor smartBulb
    public SmartBulb(String id, int tone, int dimension){
        super(id);
        this.tone = tone;
        this.dimension = dimension;
    }

    //Construtor smartBulb
    public SmartBulb(String id, double cost, boolean isOn, double consumption, int tone, int dimension){
        super(id, cost, isOn, consumption);
        this.tone = tone;
        this.dimension = dimension;
    }

    //construtor smartBulb
    public SmartBulb(SmartBulb clone) {
        super(clone.getId(), clone.getCost(), clone.isOn(), clone.getConsumption());
        this.tone = clone.getTone();
        this.dimension = clone.getDimension();
    }

    // Get da tonalidade da Lâmpada
    public int getTone() {
        return this.tone;
    }

    // Set da tonalidade da Lâmpada
    public void setTone(int tone) {
        if (tone>Warm) this.tone = Warm;
        else this.tone = Math.max(tone, Neutral);
    }

    // Get da Dimensão da Lâmpada
    public int getDimension(){
        return this.dimension;
    }

    // Set da Dimensão da Lâmpada
    public void setDimension(int dimension){
        this.dimension = dimension;
    }

    @Override
    // Custo diário da Lâmpada
    public double dailyCost(double fixedValue){
        if (super.isOn()) return fixedValue * (this.tone * 0.15);
        return 0;
    }

    @Override
    // Equals da Lâmpada
    public boolean equals(Object o){
        if(this == o) return true;
        if((o == null) || (this.getClass() != o.getClass())) return false;
        SmartBulb c = (SmartBulb) o;
        return (this.tone == c.getTone() && this.dimension == c.getDimension() && super.equals(c));
    }

    @Override
    // Clone da Lâmpada
    public SmartBulb clone () {
        return new SmartBulb( this ) ;
    }

}
