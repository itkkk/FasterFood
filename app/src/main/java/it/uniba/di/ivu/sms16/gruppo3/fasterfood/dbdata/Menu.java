package it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    private List<MenuItem> menu = new ArrayList<>();

    public void addItem(MenuItem item){
        menu.add(item);
    }

    public List<MenuItem> getMenu(){
        return menu;
    }
}
