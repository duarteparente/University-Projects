/**
* Class that represents an entry of the cache.
* @author Duarte Parente
* @since 07/11/2022
* Last change: 15/11/2022
*/
public class CacheEntry {

    private String name;
    private String type;
    private String value;
    private Integer TTL;
    private Integer order;
    private String origin;
    private TimeStamp timeStamp;
    private Integer index;
    private String status;

    public CacheEntry(){
        this.index = 1;
        this.status = "FREE";
    }

    public CacheEntry(String name, String type, String value, Integer TTL, Integer order, String origin, TimeStamp timeStamp, Integer index){
        this.name = name;
        this.type = type;
        this.value = value;
        this.TTL = TTL;
        this.order = order;
        this.origin = origin;
        this.timeStamp = timeStamp;
        this.index = index;
        this.status = "VALID";
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getType(){
        return this.type;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getTTL() {
        return this.TTL;
    }

    public void setTTL(Integer TTL) {
        this.TTL = TTL;
    }

    public Integer getOrder() {
        return this.order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getOrigin() {
        return this.origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public TimeStamp getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(TimeStamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Integer getIndex() {
        return this.index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isValid(){
        return this.status.equals("VALID");
    }

    // TO DO
    public boolean isExpired(){
        return true;
    }


}
