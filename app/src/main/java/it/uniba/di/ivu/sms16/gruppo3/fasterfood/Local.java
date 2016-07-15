package it.uniba.di.ivu.sms16.gruppo3.fasterfood;

//classe che contiene tutte le informazioni per modellare un locale
public class Local {
    String categoria;
    String citta;
    String nome;
    String via;
    String valutazione;
    String numVal;
    String orari;

    Local() {
        // empty default constructor, necessary for Firebase to be able to deserialize db tuples    }
    }

    String getNome() {
        return nome;
    }

    String getCategoria() {
        return categoria;
    }

    String getCitta() {
        return citta;
    }

    String getVia() { return via; }

    String getValutazione() { return valutazione;  }

    String getNumVal() {  return numVal;  }

    String getOrari() {  return orari;  }

}
