package entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by tank on 14.11.17.
 */
public class Program {
    private ArrayList<Store> stores = new ArrayList<Store>();
    private ArrayList<Magazine> magazines = new ArrayList<Magazine>();
    private ArrayList<Road> roads = new ArrayList<Road>();

    public ArrayList<Store> getStores() {
        return stores;
    }

    public void setStores(ArrayList<Store> stores) {
        this.stores = stores;
    }

    public ArrayList<Magazine> getMagazines() {
        return magazines;
    }

    public void setMagazines(ArrayList<Magazine> magazines) {
        this.magazines = magazines;
    }

    public ArrayList<Road> getRoads() {
        return roads;
    }

    public void setRoads(ArrayList<Road> roads) {
        this.roads = roads;
    }

    public Program(ArrayList<Store> stores, ArrayList<Magazine> magazines, ArrayList<Road> roads) {
        this.stores = stores;
        this.magazines = magazines;
        this.roads = roads;
    }

    public Program() {
        this.stores = new ArrayList<Store>();
        this.magazines = new ArrayList<Magazine>();
        this.roads = new ArrayList<Road>();
    }

    public Program loadFromFile(File file) throws IOException {
        //Этот спец. объект для построения строки
        StringBuilder sb = new StringBuilder();
        try {
            //Объект для чтения файла в буфер
            BufferedReader in = new BufferedReader(new FileReader( file.getAbsoluteFile()));
            try {
                //В цикле построчно считываем файл
                String s;
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                }
            } finally {
                //Также не забываем закрыть файл
                in.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        ObjectMapper mapper = new ObjectMapper();
        return (Program) mapper.readValue(sb.toString(),Program.class);

    }

    public void saveInFile(File file)
    {
        //work with file
        try {
            PrintWriter writer = new PrintWriter(file.getAbsoluteFile());
            ObjectMapper mapper = new ObjectMapper();
            writer.print(mapper.writeValueAsString(this));
            writer.close();
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        } catch (JsonProcessingException e2) {
            e2.printStackTrace();
        }


    }

    public void addMagazine(Magazine magazine)
    {
        if(!magazines.contains(magazine))magazines.add(new Magazine(magazine.getValue()));
    }
    public void addStore(Store store)
    {
        if(!stores.contains(store))stores.add(new Store(store.getValue()));
    }
    public void addRoad(Road road)
    {
        if(!roads.contains(road))roads.add(new Road(road.getStore(),road.getMagazine(),road.getValue()));
    }
}

