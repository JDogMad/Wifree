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
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public FirebaseDatabase() {}

    public Task<Void> add(User user){
        return db.collection("Users").document(user.getUid()).set(user);
    }

    public Task<Void> addMarker(WifiPlace place){
        return db.collection("Places").document().set(place);
    }

    public MutableLiveData<Integer> getUserCount() {
        MutableLiveData<Integer> count = new MutableLiveData<Integer>();
        CollectionReference collectionReference = db.collection("Users");
        AggregateQuery countQuery = collectionReference.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                AggregateQuerySnapshot snapshot = task.getResult();
                count.setValue((int) snapshot.getCount());
            }
        });
        return count;
    }

    public MutableLiveData<User> getUserFromDbByUid(String uid){
        MutableLiveData<User> user = new MutableLiveData<>();
        DocumentReference documentReference = db.collection("Users").document(uid);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user.setValue(documentSnapshot.toObject(User.class));
            }
        });
        return user;
    }

    public Task<User> getUserFromDbByUidTask(String uid) {
        return db.collection("Users").document(uid).get().continueWith(new Continuation<DocumentSnapshot, User>() {
            @Override
            public User then(@NonNull Task<DocumentSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                DocumentSnapshot document = task.getResult();
                User user = document.toObject(User.class);
                return user;
            }
        });
    }
}
