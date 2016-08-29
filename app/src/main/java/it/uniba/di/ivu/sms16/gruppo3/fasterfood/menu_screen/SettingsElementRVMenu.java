package it.uniba.di.ivu.sms16.gruppo3.fasterfood.menu_screen;

public class SettingsElementRVMenu {
    // Inseriti gli elementi differenti tra una CardView e l'altra, quali immagini, nome e prezzo
    private String mName;
    private String mPrice;
    private String mDescription;
    private String mImage;
    private String chain;

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

    public String getmName() { return mName; }

    public String getmPrice() {
        return mPrice;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmImage() {
        return mImage;
    }

    public String getChain() {
        return chain;
    }
}
