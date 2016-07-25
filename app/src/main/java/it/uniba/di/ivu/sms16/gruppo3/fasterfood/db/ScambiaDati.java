package it.uniba.di.ivu.sms16.gruppo3.fasterfood.db;

import java.io.File;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.ChainList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.City;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.CityList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.LocalsList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.Menu;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.OrderList;

public class ScambiaDati {
    //private static  ScambiaDati scambiaDati = null;
    private static LocalsList localsList;
    private static ChainList chainList;
    private static CityList cityList;
    private static Menu menu;
    private static OrderList orderList;
    private static File[] logosFile = new File[4]; //0-> Bacio di Latte 1-> McDonald's 2->Burger King 3-> All Chains

    public static void setLocalsList(LocalsList localsList) {
        ScambiaDati.localsList = localsList;
    }

    public static void setChainList(ChainList chainList) {
        ScambiaDati.chainList = chainList;
    }

    public static void setCityList(CityList cityList) {
        ScambiaDati.cityList = cityList;
    }

    public static void setMenu(Menu menu){
        ScambiaDati.menu = menu;
    }

    public static void setOrderList(OrderList orderList) {
        ScambiaDati.orderList = orderList;
    }

    public static void setFile(File f, int i){
        if(i<4){
            logosFile[i]=f;
        }
    }

    public static LocalsList getLocalsList() {
        return localsList;
    }

    public static ChainList getChainList() {
        return chainList;
    }

    public static CityList getCityList() {
        return cityList;
    }

    public static Menu getMenu(){
        return menu;
    }

    public static OrderList getOrderList() {
        return orderList;
    }

    public static File getLogo(int i){
        return logosFile[i];

    }
}
