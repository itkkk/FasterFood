package it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata;


import java.util.ArrayList;
import java.util.List;

//lista di locali
public class LocalsList{
    private List<Local> locals = new ArrayList<>();

    public void addLocal(Local local){
        locals.add(local);
    }

    public List<Local> getLocals(){
        return locals;
    }
}
