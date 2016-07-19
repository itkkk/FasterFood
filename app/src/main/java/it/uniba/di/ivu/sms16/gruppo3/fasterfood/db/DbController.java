package it.uniba.di.ivu.sms16.gruppo3.fasterfood.db;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
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

public class DbController extends Application{
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

}
