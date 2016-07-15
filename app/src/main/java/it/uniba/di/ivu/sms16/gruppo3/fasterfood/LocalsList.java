package it.uniba.di.ivu.sms16.gruppo3.fasterfood;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//lista di locali
public class LocalsList{
    private List<Local> locals = new ArrayList<>();

    void addLocal(Local local){
        locals.add(local);
    }

    List<Local> getLocals(){
        return locals;
    }
}
