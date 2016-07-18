package it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata;


import java.util.ArrayList;
import java.util.List;

public class CityList {
    private List<City> cities = new ArrayList<>();

    public void addCity(City city){
        cities.add(city);
    }

    public List<City> getCities(){
        return cities;
    }
}
