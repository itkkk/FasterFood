package it.uniba.di.ivu.sms16.gruppo3.fasterfood.locals_screen;

public class Frequency_localOrd{
    private String name;
    private Integer freq;

    public Frequency_localOrd(String name,Integer freq){
        this.name=name;
        this.freq=freq;
    }

    public String getName(){
        return name;
    }

    public Integer getFreq(){
        return freq;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setFreq(int freq){
        this.freq=freq;
    }
}
