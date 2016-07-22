package it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata;

public class OrderItem {
    private String nome;
    private Float prezzo;
    private Integer quantita;

    OrderItem() {
    }

    public String getNome() {
        return nome;
    }

    public Float getPrezzo() {
        return prezzo;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPrezzo(Float prezzo) {
        this.prezzo = prezzo;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }
}
