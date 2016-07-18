package it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata;

import java.util.ArrayList;
import java.util.List;

public class ChainList {
    private List<Chain> chains = new ArrayList<>();

    public void addChain(Chain chain){
        chains.add(chain);
    }

    public List<Chain> getChains(){
        return chains;
    }
}
