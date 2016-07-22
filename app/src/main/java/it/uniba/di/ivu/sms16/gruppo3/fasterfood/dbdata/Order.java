package it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata;


import java.util.ArrayList;
import java.util.List;

public class Order {
    private String data;
    private Long num_items;
    private String locale;
    private String stato;
    private String totale;
    List<OrderItem> items = new ArrayList<>();

    public String getData() {
        return data;
    }

    public Long getNum_items() {
        return num_items;
    }

    public String getLocale() {
        return locale;
    }

    public String getStato() {
        return stato;
    }

    public String getTotale() {
        return totale;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setNum_items(Long num_items) {
        this.num_items = num_items;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public void setTotale(String totale) {
        this.totale = totale;
    }

    public void addOrderItem(OrderItem item){
        items.add(item);
    }

    public List<OrderItem> getItems(){
        return items;
    }
}
