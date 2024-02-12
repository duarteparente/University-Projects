import java.util.ArrayList;
import java.util.List;

/**
* Class that represents Cache system.
* @author Duarte Parente
* @since 07/11/2022
* Last change: 15/11/2022
*/
public class Cache {
    
    List <CacheEntry> table;

    public Cache(){
        this.table = new ArrayList<>();
        this.table.add(new CacheEntry());
    }

    
    // Search entry in cache that matches 'name' and 'type', after 'index' positions 
    public synchronized Integer searchEntry(Integer index, String name, String type){
        for(int i=index-1; i<this.table.size(); i++){
            CacheEntry pin = this.table.get(i);
            if (pin.isValid()){
                if (pin.getOrigin().equals("OTHERS") && pin.isExpired()) pin.setStatus("FREE");
                else if (pin.getName().equals(name) && pin.getType().equals(type)) return (i+1);
            }
        }
        return 0;
    }

    
    // Add entry to cache
    public synchronized void addEntry(String name, String type, String value, Integer TTL, Integer order, String origin){
        TimeStamp newTime = new TimeStamp();
        if (origin.equals("FILE") || origin.equals("SP")){
            int index = this.findFirstAvailable();
            CacheEntry newEntry = new CacheEntry(name,type,value,TTL,order,origin,newTime,index);
            if (index == 0){
                index = this.table.size();
                newEntry.setIndex(index);
                this.table.add(newEntry);
            }
            else this.table.set(index,newEntry);
        }
        else{
            int index = this.searchEntry(1, name, type);
            if (index != 0){
                if (this.table.get(index-1).getOrigin().equals("OTHERS")){
                    this.table.get(index-1).setTimeStamp(newTime);
                    this.table.get(index-1).setStatus("VALID");
                }
            }
            else{
                int ind = this.findLastAvailable();
                CacheEntry newEntry = new CacheEntry(name,type,value,TTL,order,origin,newTime,ind);
                if (ind == 0){
                    ind = this.table.size();
                    newEntry.setIndex(ind);
                    this.table.add(newEntry);
                }
                else this.table.set(ind,newEntry);
            }
        }

    }

    
    // Find first FREE entry in cache
    public synchronized Integer findFirstAvailable(){
        for(int i=0; i<this.table.size(); i++){
            if (this.table.get(i).getStatus().equals("FREE")) return (i+1);
        }
        return 0;
    }

    
    // Find last FREE entry in cache
    public synchronized Integer findLastAvailable(){
        for(int i=this.table.size()-1; i>=0; i--){
            if (this.table.get(i).getStatus().equals("FREE")) return (i+1);
        }
        return 0;
    }
}
