package it.uniba.di.ivu.sms16.gruppo3.fasterfood;

public class SettingsElement {
    private String name;
    private int photoRes;

    public SettingsElement(int photoRes, String name) {
        this.photoRes = photoRes;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getPhotoRes() {
        return photoRes;
    }
}
