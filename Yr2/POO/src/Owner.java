import java.io.Serializable;
import java.util.Objects;

public class Owner implements Serializable {

    private String name;
    private int NIF;

    // Construtor Owner
    public Owner(){
        this.name = "";
        this.NIF = -1;
    }

    // Construtor Owner
    public Owner(String name, int NIF){
        this.name = name;
        this.NIF = NIF;
    }

    // Construtor Owner
    public Owner(Owner o){
        this.name = o.getName();
        this.NIF = o.getNIF();
    }

    // Get do Nome do proprietario
    public String getName(){
        return this.name;
    }

    // Set do Nome do proprietario
    public void setName(String name){
        this.name = name;
    }

    // Get do NIF do proprietario
    public int getNIF(){
        return this.NIF;
    }

    // Set do NIF do proprietario
    public void setNIF(int NIF) {
        this.NIF = NIF;
    }

    @Override
    public int hashCode(){
        return Objects.hash(name, NIF);
    }

    @Override
    //Equals do proprietario
    public boolean equals(Object o){
        if(this == o) return true;
        if((o == null) || (this.getClass() != o.getClass())) return false;
        Owner c = (Owner) o;
        return ( this.name.equals(c.getName()) && this.NIF == c.getNIF() );
    }

    @Override
    //Clone do proprietario
    public Owner clone(){
        return new Owner(this);
    }

    @Override
    // Print dos valores incluidos no proprietario
    public String toString(){
        return "Nome: " + this.name + "\nNIF: " + this.NIF + " \n";
    }
}
