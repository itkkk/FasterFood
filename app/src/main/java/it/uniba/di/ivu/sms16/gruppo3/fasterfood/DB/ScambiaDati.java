package it.uniba.di.ivu.sms16.gruppo3.fasterfood.db;


import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.ChainList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.LocalsList;

public class ScambiaDati {
    private static LocalsList localsList;
    private static ChainList chainList;

    public static void setLocalsList(LocalsList localsList) {
        ScambiaDati.localsList = localsList;
    }

    public static void setChainList(ChainList chainList) {
        ScambiaDati.chainList = chainList;
    }

    public static LocalsList getLocalsList() {
        return localsList;
    }

    public static ChainList getChainList() {
        return chainList;
    }
}
