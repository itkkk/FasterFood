package it.uniba.di.ivu.sms16.gruppo3.fasterfood;


import android.app.Application;
import android.content.Context;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class ConnectionDB extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        //inizializzazione Firebase
        Firebase.setAndroidContext(getApplicationContext());
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
    }

    public ConnectionDB(){

    }

    /*
        Firebase ref;
        void initDB(String URLLocation){
            Firebase.getDefaultConfig().setPersistenceEnabled(true);
            ref = new Firebase("https://earnest-star-132315.firebaseio.com/" + URLLocation);
            ref.keepSynced(true);
        }
    */

    LocalsList queryLocal(){

        final LocalsList locals = new LocalsList();

        Firebase localiRef = new Firebase("https://earnest-star-132315.firebaseio.com/Locali");
        localiRef.keepSynced(true);


        localiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot localSnapshot : snapshot.getChildren()){
                    Local local = localSnapshot.getValue(Local.class);
                    locals.addLocal(local);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return locals;
    }



}
