package it.uniba.di.ivu.sms16.gruppo3.fasterfood.db;

import java.io.File;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.ChainList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.LocalsList;

public class ScambiaDati {
    //private static  ScambiaDati scambiaDati = null;
    private static LocalsList localsList;
    private static ChainList chainList;
    private static File[] logosFile = new File[3]; //1-> McDonald's 2->Burger King 3->Bacio di Latte

    public static void setLocalsList(LocalsList localsList) {
        ScambiaDati.localsList = localsList;
    }

    public static void setChainList(ChainList chainList) {
        ScambiaDati.chainList = chainList;
    }

    public static void setFile(File f, int i){
        if(i<3){
            logosFile[i]=f;
        }
    }

    public static LocalsList getLocalsList() {
        return localsList;
    }

    public static ChainList getChainList() {
        return chainList;
    }

    public static File getLogo(int i){
        return logosFile[i];
    }
}
