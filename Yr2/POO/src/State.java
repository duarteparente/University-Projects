import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

public class State {
    //parser do ficheiro de input
    public SmartCity parser() throws ParsingException, EntityMissingException {

        List<String> lines = readFile("data/logs.csv");
        Map<String, Supplier> suppliers = new HashMap<>();
        List<SmartHouse> smartHouses = new ArrayList<>();
        String[] parsedLine;
        int id = 0;
        String room = null;
        Supplier pin;
        SmartHouse holder = null;
        for (String line : lines) {
            parsedLine = line.split(":", 2);
            switch(parsedLine[0]){
                case "Fornecedor":
                    pin = parseSupplier(parsedLine[1]);
                    suppliers.put(pin.getCompany(), pin.clone());
                    break;
                case "Casa":
                    if (holder != null) smartHouses.add(holder.clone());
                    holder = parseSmartHouse(parsedLine[1], suppliers);
                    break;
                case "Divisao":
                    if (holder == null) throw new ParsingException("Erro ao ler 'Casa'");
                    else{
                        room = parsedLine[1];
                        holder.addRoom(room);
                    }
                    break;
                case "SmartBulb":
                    if (room == null || holder == null) throw new ParsingException("Erro ao ler 'Casa' ou 'Divisao");
                    else{
                        SmartBulb sd = parseSmartBulb(parsedLine[1], id);
                        holder.addDeviceToRoom(room, sd);
                        id++;
                    }
                    break;
                case "SmartCamera":
                    if (room == null || holder == null) throw new ParsingException("Erro ao ler 'Casa' ou 'Divisao");
                    else{
                        SmartCamera sd = parseSmartCamera(parsedLine[1], id);
                        holder.addDeviceToRoom(room,sd);
                        id++;
                    }
                    break;
                case "SmartSpeaker":
                    if (room == null || holder == null) throw new ParsingException("Erro ao ler 'Casa' ou 'Divisao");
                    else{
                        SmartSpeaker sd = parseSmartSpeaker(parsedLine[1], id);
                        holder.addDeviceToRoom(room,sd);
                        id++;
                    }
                    break;
                default:
                    throw new ParsingException("Linha Inválida:" + line);
            }
        }
        if (holder != null) smartHouses.add(holder.clone());
        return new SmartCity(LocalDate.now(), suppliers, smartHouses);
    }

    //metodo que complementa o parser do ficheiro
    public List<String> readFile(String fileName) {
        List<String> lines;
        try { lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8); }
        catch(IOException exc) { lines = new ArrayList<>(); }
        return lines;
    }

    //parser do Supplier
    public Supplier parseSupplier(String input){
        String[] parts = input.split(",");
        String company = parts[0];
        double baseCost = Double.parseDouble(parts[1]);
        double taxFactor = Double.parseDouble(parts[2]);
        return new Supplier(company,baseCost,taxFactor);
    }

    //parser da Smarthouse
    public SmartHouse parseSmartHouse(String input, Map<String, Supplier> suppliers){
        String[] parts = input.split(",");
        String nome = parts[0];
        int nif = Integer.parseInt(parts[1]);
        String supplier = parts[2];
        Supplier s = suppliers.get(supplier);
        Owner owner = new Owner(nome,nif);
        return new SmartHouse(owner,s);
    }

    //parser da Smartbulb
    public SmartBulb parseSmartBulb(String input, int id){
        String[] parts = input.split(",");
        int dimension = Integer.parseInt(parts[1]);
        double consumption = Double.parseDouble(parts[2]);
        double cost = Math.random() * (0.75 - 0.5) + 0.25; // random number [0.05 , 0.15]
        boolean on = ((id % 5) == 0);
        int tone;
        switch(parts[0]){
            case "Warm": tone = 2; break;
            case "Cold": tone = 1; break;
            default: tone = 0; break;
        }
        return new SmartBulb(String.valueOf(id),cost,on,consumption,tone,dimension);
    }

    //parser da SmartSpeaker
    public SmartSpeaker parseSmartSpeaker(String input, int id){
        String[] parts = input.split(",");
        double cost = Math.random() * (0.75 - 0.5) + 0.25; // random number [0.25 , 0.75]
        boolean on = ((id % 5) == 0);
        int volume = Integer.parseInt(parts[0]);
        String chanel = parts[1];
        String brand = parts[2];
        double consumption = Double.parseDouble(parts[3]);
        return new SmartSpeaker(String.valueOf(id),cost,on,consumption,chanel,volume,brand);
    }

    //parser da SmartCamera
    public SmartCamera parseSmartCamera(String input, int id){
        String[] parts = input.split(",");
        double cost = Math.random() * (0.75 - 0.5) + 0.25; // random number [0.25 , 0.75]
        boolean on = ((id % 5) == 0);
        int length = parts[0].length();
        String resolution = parts[0].substring(1,length-2);
        int filesize = Integer.parseInt(parts[1]);
        double consumption = Double.parseDouble(parts[2]);
        return new SmartCamera(String.valueOf(id),cost,on,consumption,resolution,filesize);
    }
}
