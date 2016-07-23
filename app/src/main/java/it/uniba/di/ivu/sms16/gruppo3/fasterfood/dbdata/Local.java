package it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata;

//classe che contiene tutte le informazioni per modellare un locale
public class Local {
    private String categoria;
    private String citta;
    private String nome;
    private String via;
    private String valutazione;
    private String numVal;
    private String orari;
    private String posti;

    Local() {
        // empty default constructor, necessary for Firebase to be able to deserialize db tuples    }
    }

    public String getNome() {
        return nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getCitta() {
        return citta;
    }

    public String getVia() {
        return via;
    }

    public Float getValutazione() {
        return Float.valueOf(valutazione);
    }

    public Integer getNumVal() {
        return Integer.parseInt(numVal);
    }

    public String getOrari() {
        return orari;
    }

    public String getPosti() {
        return posti;
    }


}
