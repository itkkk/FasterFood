package it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata;


import java.util.ArrayList;
import java.util.List;

public class OrderList {
    private List<Order> orders = new ArrayList<>();

    public void addOrder(Order order){
        orders.add(order);
    }

    public List<Order> getOrders(){
        return orders;
    }
}
