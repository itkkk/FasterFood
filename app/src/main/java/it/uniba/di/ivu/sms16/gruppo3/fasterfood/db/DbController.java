package it.uniba.di.ivu.sms16.gruppo3.fasterfood.db;

import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.Chain;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.ChainList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.Local;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.LocalsList;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.Menu;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.dbdata.MenuItem;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.menu_screen.MenuFragment;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.restaurant_screen.RestaurantDetailFragment;

public class DbController extends Application{
    boolean connected;

    @Override
    public void onCreate() {
        super.onCreate();
        //inizializzazione Firebase
        Firebase.setAndroidContext(getApplicationContext());
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
    }

    public DbController(){

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

    public boolean retrieveMenu(String DBUrl, final FragmentManager fragmentManager, final String category, final Context context){
        final Menu menu = new Menu();

        Firebase menuRef = new Firebase(DBUrl);
        menuRef.keepSynced(true);
        menuRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot menuSnapshot : snapshot.getChildren()){
                    MenuItem menuItem = menuSnapshot.getValue(MenuItem.class);
                    menu.addItem(menuItem);
                }

                if(menu.getMenu().size() == 0){
                    connected = false;
                }else{
                    connected = true;
                }

                ScambiaDati.setMenu(menu);
                MenuFragment menuFragment = new MenuFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("chain", category);
                menuFragment.setArguments(bundle1);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, menuFragment)
                        .addToBackStack("")
                        .commit();
                            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
/*
        if(menuRef){
            Toast.makeText(context,"Non connesso", Toast.LENGTH_LONG).show();
        }

*/
        return connected;
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
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_food));
            }
        });
    }

}
