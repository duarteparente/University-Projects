package Business.RacingManager.Championship;

import View.UI;

import java.util.*;

public class Championship {

    private String name;
    private List<Track> tracks;
    private Map<String,Entry> entries;
    private Map<String, Integer> result;
    private Map<String, Integer> resultH;
    private int next;

    //Constructors
    public Championship(){
        this.name = "";
        this.tracks = new ArrayList<>();
        this.entries = new HashMap<>();
        this.result = new HashMap<>();
        this.resultH = new HashMap<>();
    }

    public Championship(String name, List<Track> tracks, Map<String,Entry> entries){
        this.name = name;
        this.tracks = tracks;
        this.entries = entries;
        this.result = new HashMap<>();
        this.resultH = new HashMap<>();
    }

    //Getters
    public String getName(){
        return this.name;
    }

    public ArrayList<Track> getTracks(){
        ArrayList<Track> aux = new ArrayList<Track>();
        aux.addAll(this.tracks);
        return aux;
    }

    public Map<String, Integer> getResult() {
        HashMap<String,Integer> aux = new HashMap<String, Integer>();
        for(String g : this.result.keySet())
            aux.put(g, this.result.get(g));
        return aux;
    }

    public Map<String, Integer> getResultH() {
        HashMap<String,Integer> aux = new HashMap<String, Integer>();
        for(String g : this.resultH.keySet())
            aux.put(g, this.resultH.get(g));
        return aux;
    }

    //Setters

    public void setName(String name) {
        this.name = name;
    }

    public void setResult(Map<String, Integer> averageTimes) {
        this.result = new HashMap<>();
        this.result.putAll(averageTimes);
    }

    public void setResultH(Map<String, Integer> averageTimes) {
        this.resultH = new HashMap<>();
        this.resultH.putAll(averageTimes);
    }

    public void updateResults(){}

    public void updateResultsH(){}

    public void simRace(){}

    public void simNextRace(){}

    public void printResults(){
        System.out.println("-------------------- FINAL RESULTS --------------------");
        for (String key : this.result.keySet()){
            System.out.println(key + " - > " + result.get(key) + " points");
        }
    }

    public void updateRank(List<Entry> ranking){
        for(int i = 0;i<ranking.size();i++){
            for(String s : entries.keySet()){
                if(ranking.get(i).equals(entries.get(s)))
                    result.put(s,result.get(s)+ranking.size()*10-i);
            }
        }
    }
    @Override
    //Equals
    public boolean equals(Object o){
        if(this == o) return true;
        if((o == null) || (this.getClass() != o.getClass())) return false;
        Championship c = (Championship) o;
        return (this.name.equals(c.getName()) && this.tracks == c.getTracks() && this.result == c.getResult() && this.resultH == c.getResultH());
    }

    public void printChampionship(){
        UI.clearScreen();
        System.out.println("|Name-> " + this.name);
    }

    public void simulate(){
        for(String s: this.entries.keySet())
            this.result.put(s,0);
        for(Track track : this.tracks){
            Race race = new Race(track);
            updateRank(race.simulateRace(entries,result));
        }

    }
}