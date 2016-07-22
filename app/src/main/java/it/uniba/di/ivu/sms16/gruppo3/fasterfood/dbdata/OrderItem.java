package it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata;

public class OrderItem {
    private String nome;
    private String prezzo;
    private String quantita;

    OrderItem() {
    }

    public String getNome() {
        return nome;
    }

    public String getPrezzo() {
        return prezzo;
    }

    public String getQuantita() {
        return quantita;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPrezzo(String prezzo) {
        this.prezzo = prezzo;
    }

    public void setQuantita(String quantita) {
        this.quantita = quantita;
    }
}
