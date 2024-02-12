public class SmartCamera extends SmartDevice{
    private String resolution;
    private int filesize;

    //Construtor SmartCamera
    public SmartCamera(){
        super();
        this.resolution = "";
        this.filesize = 400;
    }

    //Construtor SmartCamera
    public SmartCamera(String id){
        super(id);
        this.resolution = "";
        this.filesize = 400;
    }

    //Construtor SmartCamera
    public SmartCamera(String id, String resolution, int filesize){
        super(id);
        this.resolution = resolution;
        this.filesize = filesize;
    }

    //Construtor SmartCamera
    public SmartCamera(String id, double cost, boolean isOn, double consumption, String resolution, int filesize){
        super(id, cost, isOn, consumption);
        this.resolution = resolution;
        this.filesize = filesize;
    }

    //Construtor SmartCamera
    public SmartCamera(SmartCamera clone) {
        super(clone.getId(), clone.getCost(), clone.isOn(), clone.getConsumption());
        this.resolution = clone.getResolution();
        this.filesize = clone.getFilesize();
    }

    // Get da Resolução da Câmara
    public String getResolution() {
        return this.resolution;
    }

    // Set da Resolução da Câmara
    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    // Get Tamanho do ficheiro que guarda os eventos registados
    public int getFilesize(){
        return this.filesize;
    }

    // Set Tamanho do ficheiro que guarda os eventos registados
    public void setFilesize(int filesize){
        this.filesize = filesize;
    }

     @Override
    // Custo Diário da Câmara
     public double dailyCost(double fixedValue){
        if (super.isOn()){
            String[] parts = this.resolution.split("x");
            int res1 = Integer.parseInt(parts[0]);
            int res2 = Integer.parseInt(parts[0]);
            return  fixedValue*0.2 + ( (this.filesize*res1*res2) / 10000000000.0);
         }
        else return 0;
     }

    @Override
    // Equals da Câmara
    public boolean equals(Object o){
        if(this == o) return true;
        if((o == null) || (this.getClass() != o.getClass())) return false;
        SmartCamera c = (SmartCamera) o;
        return (this.resolution.equals(c.getResolution()) && this.filesize == c.getFilesize() && super.equals(c));
    }

    @Override
    // Clone da Câmara
    public SmartCamera clone () {
        return new SmartCamera( this ) ;
    }


}
