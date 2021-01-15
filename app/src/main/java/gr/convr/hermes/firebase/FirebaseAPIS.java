package gr.convr.hermes.firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseAPIS {


    public static void fieldQuery(String mail, OnCompleteListener<DocumentSnapshot> task){

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        DocumentReference docRef = db.collection("users").document(mail);

        docRef.get().addOnCompleteListener(task);

    }

    public static void fieldQueryOnThread(String mail, OnCompleteListener<DocumentSnapshot> task){

        Thread thread = new Thread(){

            @Override
            public void run(){
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("users").document(mail);

                docRef.get().addOnCompleteListener(task);
            }
        };


        thread.start();

    }

    public static void updateField(String mail, String field, Object value, OnCompleteListener<Void> task){

        Thread thread = new Thread(){

            @Override
          public void run(){
              FirebaseFirestore db = FirebaseFirestore.getInstance();


              DocumentReference docRef = db.collection("users").document(mail);
              docRef.get().addOnCompleteListener(task0 -> {
                  if(task0.isSuccessful()){
                      docRef.update(field, value).addOnCompleteListener(task);
                  }
              });
          }

        };
        thread.start();
    }


}
