package be.ehb.wifree_2.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import be.ehb.wifree_2.User;
import be.ehb.wifree_2.WifiPlace;

public class FirebaseDatabase {
    FirebaseFirestore db = FirebaseFirestore.getInstance(); // initializing instance of Firebase Firestore

    public FirebaseDatabase() {}

    // method to add a user in the database
    public Task<Void> add(User user){
        return db.collection("Users").document(user.getUid()).set(user);
    }

    // method to add a marker in the database
    public Task<Void> addMarker(WifiPlace place){
        return db.collection("Places").document().set(place);
    }

    // method to count to users in the database and return it back
    public MutableLiveData<Integer> getUserCount() {
        MutableLiveData<Integer> count = new MutableLiveData<Integer>(); // new list
        CollectionReference collectionReference = db.collection("Users"); // get all users
        AggregateQuery countQuery = collectionReference.count(); // counting the users
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                AggregateQuerySnapshot snapshot = task.getResult();
                count.setValue((int) snapshot.getCount());
            }
        });
        return count; // return the amount of users as a integer
    }

    // method that gets user by uuid (string)
    public Task<User> getUserFromDbByUidTask(String uid) {
        // gets the user by a uid -> which is given by us
        return db.collection("Users").document(uid).get().continueWith(new Continuation<DocumentSnapshot, User>() {
            @Override
            public User then(@NonNull Task<DocumentSnapshot> task) throws Exception {
                if (!task.isSuccessful()) { // checks if task is successful -> not than error
                    throw task.getException(); // throws exception
                }

                DocumentSnapshot document = task.getResult();
                User user = document.toObject(User.class);
                return user; // returns the user that matches the uid
            }
        });
    }
}
