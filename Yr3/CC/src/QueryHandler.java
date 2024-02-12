import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
* Class used to hold and represent the DNS QUERY
* @author Duarte Parente, Ana Rita, Inês Castro
* @since 07/11/2022
* Last change: 07/12/2022
*/
public class QueryHandler {

    private Integer messageID;
    private Integer flags;
    private Integer responseCode;
    private Integer nrOfValues;
    private Integer nrOfAuthorities;
    private Integer nrOfExtraValues;
    private String queryInfoName;
    private String queryInfoType;
    private List<String> responseValues;
    private List<String> authoritiesValues;
    private List<String> extraValues;


    public QueryHandler(byte[] data){
        this.messageID = 0;
        this.messageID = (this.messageID << 8) + (data[0] & 0xFF);
        this.messageID = (this.messageID << 8) + (data[1] & 0xFF);
        this.flags = 0;
        this.flags = (this.flags << 8) + (data[2] & 0xFF);
        this.responseCode = 0;
        this.responseCode = (this.responseCode << 8) + (data[3] & 0xFF);
        this.nrOfValues = 0;
        this.nrOfValues = (this.nrOfValues << 8) + (data[4] & 0xFF);
        this.nrOfAuthorities = 0;
        this.nrOfAuthorities = (this.nrOfAuthorities << 8) + (data[5] & 0xFF);
        this.nrOfExtraValues = 0;
        this.nrOfExtraValues =(this.nrOfExtraValues << 8) + (data[6] & 0xFF);
        ByteBuffer skipHeader = ByteBuffer.wrap(data,7,data.length-7);
        byte[] pduData = new byte[data.length-7];
        skipHeader.get(pduData,0,pduData.length);
        String dataParts = new String(pduData,0,pduData.length).trim();
        String[] parts = dataParts.split(";");
        String[] queryInfoParts = parts[1].split(",");
        this.queryInfoName = queryInfoParts[0];
        this.queryInfoType = queryInfoParts[1];
        this.responseValues = new ArrayList<>();
        int control_index = 2;
        if (nrOfValues > 0){
            String[] rValuesParts = parts[control_index].split(",");
            for(String i : rValuesParts) this.responseValues.add(i);
            control_index++;
        }
        this.authoritiesValues = new ArrayList<>();
        if (nrOfAuthorities > 0){
            String[] AuthoritiesParts = parts[control_index].split(",");
            for(String i : AuthoritiesParts) this.authoritiesValues.add(i);
            control_index++;
        }
        this.extraValues = new ArrayList<>();
        if (nrOfExtraValues > 0){
            String[] ExtraParts = parts[control_index].split(",");
            for(String i : ExtraParts) this.extraValues.add(i);
        }
    }

    public QueryHandler(String serverIP, String Name, String typeOfValue, Character flag){
        this.messageID = (int) (Math.random() * (65534)) + 1;
        this.flags = (int) flag;
        this.responseCode = 0;
        this.nrOfValues = 0;
        this.nrOfAuthorities = 0;
        this.nrOfExtraValues = 0;
        this.queryInfoName = Name;
        this.queryInfoType = typeOfValue;
        this.responseValues = new ArrayList<>();
        this.authoritiesValues = new ArrayList<>();
        this.extraValues = new ArrayList<>();
    }

    public QueryHandler(String query){
        String parts[] = query.split(";");
        String headerParts[] = parts[0].split(",");
        String queryInfoParts[] = parts[1].split(",");
        this.messageID = Integer.parseInt(headerParts[0]);
        this.flags = (int) headerParts[1].charAt(0);
        this.responseCode = Integer.parseInt(headerParts[2]);
        this.nrOfValues = Integer.parseInt(headerParts[3]);
        this.nrOfAuthorities = Integer.parseInt(headerParts[4]);
        this.nrOfExtraValues = Integer.parseInt(headerParts[5]);
        this.queryInfoName = queryInfoParts[0];
        this.queryInfoType = queryInfoParts[1];
        this.responseValues = new ArrayList<>();
        int control_index = 2;
        if (nrOfValues > 0){
            String[] rValuesParts = parts[control_index].split(",");
            for(String i : rValuesParts) this.responseValues.add(i);
            control_index++;
        }
        this.authoritiesValues = new ArrayList<>();
        if (nrOfAuthorities > 0){
            String[] AuthoritiesParts = parts[control_index].split(",");
            for(String i : AuthoritiesParts) this.authoritiesValues.add(i);
            control_index++;
        }
        this.extraValues = new ArrayList<>();
        if (nrOfExtraValues > 0){
            String[] ExtraParts = parts[control_index].split(",");
            for(String i : ExtraParts) this.extraValues.add(i);
        }
    }

    public void setFlagA(){
        this.flags = 65;
    }

    public void setResponseCode(int r){
        this.responseCode = r;
    }

    public String queryType(){
        if (this.flags == 81) return "QR";
        return "RP";
    }

    public void addResponseValue(String ans){
        this.responseValues.add(ans);
        this.nrOfValues++;
    }

    public void addAuthoritiesValue(String ans){
        this.authoritiesValues.add(ans);
        this.nrOfAuthorities++;
    }

    public void addExtraValue(String ans){
        if (!this.extraValues.contains(ans)){
            this.extraValues.add(ans);
            this.nrOfExtraValues++;
        }
    }

    public Integer getNrValues(){
        return this.nrOfValues;
    }

    public Integer getMessageID(){
        return this.messageID;
    }

    public Integer getResponseCode(){
        return this.responseCode;
    }

    public String getQueryInfoName() {
        return this.queryInfoName;
    }

    public String getQueryInfoType() {
        return this.queryInfoType;
    }


    public Integer getdbVersion(){
        int r = 0;
        if (this.nrOfValues != 0){
            String[] parts = this.responseValues.get(0).split(" ");
            r = Integer.parseInt(parts[2]);
        }
        return r;
    }

    public List<String> getExtraIPs(){
        List<String> res = new ArrayList<>();
        for (String i : this.extraValues){
            String[] parts = i.split(" ");
            res.add(parts[2]);
        }
        return res;
    }


    public byte[] queryToByteArray() throws IOException{
        byte[] header = new byte [7];
        header[0] = (byte) (this.messageID >> 8);
        header[1] = this.messageID.byteValue();
        header[2] = this.flags.byteValue();
        header[3] = this.responseCode.byteValue();
        header[4] = this.nrOfValues.byteValue();
        header[5] = this.nrOfAuthorities.byteValue();
        header[6] = this.nrOfExtraValues.byteValue();
        byte[] data = this.dataToString().getBytes();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(header);
        out.write(data);
        byte[] query = out.toByteArray();
        return query;
    }

    // returns true only if the flag represents a query
    public boolean isQuery(){
        return (this.flags == 'Q');
    }

    // Format the query output to be shown in CL
    public void showOutput(){
        StringBuilder s = new StringBuilder();
        s.append("\n    # Header\n    MESSAGE-ID = " + this.messageID + ", FLAGS = " + Character.toString(this.flags) + ", RESPONSE-CODE = " + this.responseCode);
        s.append("\n    N-VALUES = " + this.nrOfValues + ", N-AUTHORITIES = " + this.nrOfAuthorities + ", N-EXTRA-VALUES = " + this.nrOfExtraValues);
        s.append(";\n    # Data: Query Info\n    QUERY-INFO.NAME = " + this.queryInfoName + ", QUERY-INFO.TYPE = " + this.queryInfoType);
        s.append(";\n    # Data: List of Response, Authorities and Extra Values\n");
        for(String i : this.responseValues){
            s.append("    RESPONSE-VALUES = " + i).append(",\n");
        }
        s.replace(s.length()-2, s.length()-1, ";");
        for(String i : this.authoritiesValues){
            s.append("    AUTHORITIES-VALUES = " + i).append(",\n");
        }
        s.replace(s.length()-2, s.length()-1, ";");
        for(String i : this.extraValues){
            s.append("    EXTRA-VALUES = " + i).append(",\n");
        }
        s.replace(s.length()-2, s.length()-1, ";");
        System.out.println(s);
    }

    public String headerToString(){
        StringBuilder s = new StringBuilder();
        s.append(this.messageID).append(",").append(Character.toString(this.flags)).append(",").append(this.responseCode).append(",");
        s.append(this.nrOfValues).append(",").append(this.nrOfAuthorities).append(",").append(this.nrOfExtraValues);
        return s.toString();
    }


    public String dataToString(){
        StringBuilder s = new StringBuilder();
        s.append(";").append(this.queryInfoName).append(",").append(this.queryInfoType).append(";");
        for(String i : this.responseValues){
            s.append(i).append(",");
        }
        s.replace(s.length()-1, s.length(), ";");
        for(String i : this.authoritiesValues){
            s.append(i).append(",");
        }
        s.replace(s.length()-1, s.length(), ";");
        for(String i : this.extraValues){
            s.append(i).append(",");
        }
        s.replace(s.length()-1, s.length(), ";");
        return s.toString();
    }


    @Override
    public String toString(){
        String header = this.headerToString();
        String data = this.dataToString();
        return header + data;
    }

    
}
