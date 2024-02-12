import java.util.List;

public class Frame {

    public int tag;
    public List<byte[]> data;
        
    public Frame(int tag, List<byte[]> data) { 
        this.tag = tag; 
        this.data = data; 
    }
    
}
