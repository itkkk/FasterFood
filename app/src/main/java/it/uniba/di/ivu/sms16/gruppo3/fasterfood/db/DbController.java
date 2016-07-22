package it.uniba.di.ivu.sms16.gruppo3.fasterfood.db;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.Chain;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.ChainList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.City;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.CityList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.Local;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.LocalsList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.Menu;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.MenuItem;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.Order;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.OrderItem;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.OrderList;


public class DbController extends Application{
    static boolean connected;

    @Override
    public void onCreate() {
        super.onCreate();
        //inizializzazione Firebase
        Firebase.setAndroidContext(getApplicationContext());
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
    }

    public DbController(){

    }

    static public Boolean isConnected(String DBUrl){
        Firebase connectedRef = new Firebase(DBUrl);
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                connected = snapshot.getValue(Boolean.class);
            }
            @Override
            public void onCancelled(FirebaseError error) {
            }
        });
        Thread thread = Thread.currentThread();
        try{
            thread.sleep(100);
        }catch (InterruptedException e){}
        finally {
            return connected;
        }
    }

    public LocalsList queryLocals(String DBUrl){
        final LocalsList locals = new LocalsList();

        Firebase localsRef = new Firebase(DBUrl);
        localsRef.keepSynced(true);

        localsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot localSnapshot : snapshot.getChildren()){
                    Local local = localSnapshot.getValue(Local.class);
                    locals.addLocal(local);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) { }
        });
        return locals;
    }

    public ChainList queryChains(String DBUrl){
        final ChainList chains = new ChainList();

        Firebase chainsRef = new Firebase(DBUrl);
        chainsRef.keepSynced(true);

        chainsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot chainSnapshot : snapshot.getChildren()){
                    Chain chain = chainSnapshot.getValue(Chain.class);
                    chains.addChain(chain);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) { }
        });
        return chains;
    }

    public CityList queryCities(String DBUrl) {
        final CityList cities = new CityList();

        Firebase chainsRef = new Firebase(DBUrl);
        chainsRef.keepSynced(true);

        chainsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot citySnapshot : snapshot.getChildren()) {
                    City city = citySnapshot.getValue(City.class);
                    cities.addCity(city);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
        return cities;
    }

    public Menu queryMenu(String DBUrl){
        final Menu menu = new Menu();

        Firebase chainsRef = new Firebase(DBUrl);
        chainsRef.keepSynced(true);

        chainsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot menuSnapshot : snapshot.getChildren()){
                    MenuItem menuItem = menuSnapshot.getValue(MenuItem.class);
                    menu.addItem(menuItem);
                }

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) { }
        });
        return menu;
    }

    public File getLogoFile(String fileUrl, String filename, Context context){
        final File logo = new File(context.getFilesDir(), filename);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference logoReference = storage.getReferenceFromUrl(fileUrl);
        logoReference.getFile(logo).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
        return logo;
    }

    public void setProductImage(String fileUrl, String filename, final Context context, final ImageView imageView){
        final File logo = new File(context.getFilesDir(), filename);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference logoReference = storage.getReferenceFromUrl(fileUrl);
        logoReference.getFile(logo).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                final Bitmap logoBitmap = BitmapFactory.decodeFile(logo.getAbsolutePath());
                imageView.setImageBitmap(logoBitmap);
                imageView.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    /**
     *
     * @param DBUrl punta al nodo Ordini
     */
    public void addOrder(String DBUrl, String status, String totalPrice,String localName,
                         ArrayList<String> names, ArrayList<String> quantities,
                         ArrayList<String> prices){
        //Mi collego al nodo "Ordini" del db
        Firebase ref = new Firebase(DBUrl);
        //Ottengo il riferimento all'utente connesso, alla data corrente e genero la chiave primaria
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Calendar rightNow = Calendar.getInstance();
        String date = rightNow.get(Calendar.DATE) + "-"
                + (rightNow.get(Calendar.MONTH)+1) + "-"
                + rightNow.get(Calendar.YEAR) + "_"
                + rightNow.get(Calendar.HOUR) + ":"
                + rightNow.get(Calendar.MINUTE) + ":"
                + rightNow.get(Calendar.SECOND) + "_"
                + rightNow.get(Calendar.AM_PM);
        String pk = user.getUid() + "_" + date;

        //creo un nodo con valore pk
        Firebase orderRef = ref.child(pk);
        orderRef.child("email").setValue(user.getEmail());
        orderRef.child("stato").setValue(status);
        orderRef.child("totale").setValue(totalPrice);
        orderRef.child("data").setValue(date);
        orderRef.child("locale").setValue(localName);
        orderRef.child("items").setValue(names.size());
        for(int i = 0; i < names.size(); i++){
            Firebase itemRef = orderRef.child(names.get(i));
            itemRef.child("nome").setValue(names.get(i));
            itemRef.child("quantita").setValue(quantities.get(i));
            itemRef.child("prezzo").setValue(prices.get(i));
        }
        return;
    }

    public OrderList getOrders(String DBUrl){
        final OrderList orders = new OrderList();

        Firebase orderRef = new Firebase(DBUrl);
        orderRef.keepSynced(true);

        final String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot orderSnapshot : snapshot.getChildren()){
                    String email = (String) orderSnapshot.child("email").getValue();
                    if(userEmail.equals(email)){
                        Order order = new Order();
                        order.setData((String)orderSnapshot.child("data").getValue());
                        order.setLocale((String)orderSnapshot.child("locale").getValue());
                        order.setNum_items((Long) orderSnapshot.child("items").getValue());
                        order.setStato((String)orderSnapshot.child("stato").getValue());
                        order.setTotale((String) orderSnapshot.child("totale").getValue());
                        for(DataSnapshot children : orderSnapshot.getChildren()) {
                            if (children.hasChildren()){
                                OrderItem item = new OrderItem();
                                item.setNome((String) children.child("nome").getValue());
                                item.setPrezzo((String) children.child("prezzo").getValue());
                                item.setQuantita((String) children.child("quantita").getValue());
                                order.addOrderItem(item);
                            }
                        }
                        orders.addOrder(order);
                    }
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) { }
        });

        return orders;
    }

}
