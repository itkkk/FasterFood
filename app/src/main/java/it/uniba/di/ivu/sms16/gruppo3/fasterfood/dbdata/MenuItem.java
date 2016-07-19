package it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata;

public class MenuItem {
    private String descrizione;
    private String nome;
    private String prezzo;

    MenuItem() {
        // empty default constructor, necessary for Firebase to be able to deserialize db tuples    }
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getNome() {
        return nome;
    }

    public String getPrezzo() {
        return prezzo;
    }
}
