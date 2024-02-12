package Business.RacingManager.Championship;

import Business.RacingManager.Car.*;
import View.UI;

import java.text.SimpleDateFormat;
import java.util.*;

public class Race {

    private Track track;
    private int weather;
    private List<Entry> results;
    private Map<Entry,Integer> dnf;

    //Constructors
    public Race() {
        this.track = new Track();
        this.weather = 0;
    }

    public Race(Track track){
        this.track = new Track(track);
        this.weather = (int) Math.round(Math.random());
        this.results = new ArrayList<>();
        this.dnf = new HashMap<>();
    }

    //Getters

    public Track getTrack(){
        return this.track;
    }

    public int getWeather(){
        return this.weather;
    }

    //Setters
    public void setTrack(Track track){
        this.track = track;
    }

    public void setWeather(int weather){
        this.weather = weather;
    }

    public void printResults(){
        int i = 1;
        for(Entry e : this.results) {
            System.out.println("#" + i + " "+e.getDriver().getName() + " -> " + toTimeFormat(e.getTime()));
            i++;
        }
    }

    public void printDNF() {
        for (Entry e : this.dnf.keySet())
            System.out.println("DNF-> " + e.getDriver().getName());
    }

    public static String toTimeFormat(long t)
    {
        return (new SimpleDateFormat("mm:ss:SSS")).format(new Date(t));
    }

    public List<Entry> simulateRace(Map<String,Entry> entries, Map<String,Integer> result){
        int laps = this.track.getNrLaps();
        long t_volta;
        String time_lap = null;
        ArrayList<Entry> aux = new ArrayList<>();
        List<Entry> rank = new ArrayList<>();
        Comparator<Entry> resultsCompare = Comparator.comparing(Entry::getTime);

        for(String key : entries.keySet()) {
            Entry e = entries.get(key);
            aux.add(e.clone());
        }
        for(int i=0; i<laps; i++) {
            for(Entry e : aux) {
                if(!e.getCar().getDNF()){
                        if(e.DNF(i,this.weather)){
                            e.getCar().setDNF(true);
                            this.dnf.put(e.clone(),i);
                        }
                        else {
                            Car c = e.getCar();
                            if(c instanceof C1H || c instanceof C2H || c instanceof GTH) {
                                int motor = ((Hibrid) c).getElectricEnginePower();
                                t_volta = (e.nextLapTime(this.weather) - motor/10);
                                time_lap = toTimeFormat(t_volta);
                            }
                            else {
                                t_volta = e.nextLapTime(this.weather);
                                time_lap = toTimeFormat(t_volta);
                            }
                            e.setTime(t_volta);
                            this.results.add(e);
                        }
                }
            }
            int j = i+1;
            System.out.println("Lap: "+j);
            printResults();
            printDNF();
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.results = new ArrayList<>();
        }
        Collections.sort(this.results,resultsCompare);
        for(Entry e : this.results)
            rank.add(e.clone());
         return rank;
    }

}
