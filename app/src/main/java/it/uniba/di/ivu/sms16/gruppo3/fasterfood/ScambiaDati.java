package it.uniba.di.ivu.sms16.gruppo3.fasterfood;


public class ScambiaDati {
    private static  ScambiaDati scambiaDati = null;
    private static LocalsList localsList;

    protected ScambiaDati(){}

    public static void setLocalsList(LocalsList localsList) {
        ScambiaDati.localsList = localsList;
    }

    public static ScambiaDati getScambiaDati(){
        if (scambiaDati == null){
            scambiaDati = new ScambiaDati();
        }
        return scambiaDati;
    }

    public static LocalsList getLocalsList() {
        return localsList;
    }
}
