package com.mygdx.game;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.programmers.enums.Difficulty;
import com.programmers.interfaces.Procedure;
import com.programmers.interfaces.SpecificCode;

import java.util.LinkedList;
import java.util.List;

public final class AndroidSpecificCode implements SpecificCode {

    private final static String roomCollection = "rooms";

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final Activity activity;

    AndroidSpecificCode(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void registerAnon() {
        firebaseAuth.signInAnonymously().addOnCompleteListener(activity,
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) { }
        });
    }

    @Override
    public boolean createNewRoom(final String name, final int playersCount, final Difficulty difficulty) {
        final Boolean[] temp = { null };
        firebaseFirestore.collection(roomCollection).document(name).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            temp[0] = false;
                        } else {
                            temp[0] = true;
                            Room room = new Room(name, playersCount, difficulty);
                            firebaseFirestore.collection(roomCollection).document(name).set(room);
                        }
                    }
                });
        while (temp[0] == null);
        return temp[0];
    }

    @Override
    public void deleteRoom(String name) {
        firebaseFirestore.collection(roomCollection).document(name).delete();
    }

    @Override
    public void addPlayerToRoom(Room room) {
        room.setNowPlayers(room.getNowPlayers() + 1);
        firebaseFirestore.collection(roomCollection).document(room.getName()).set(room);
    }

    @Override
    public void removePlayerFromRoom(Room room) {
        room.setNowPlayers(room.getNowPlayers() - 1);
        firebaseFirestore.collection(roomCollection).document(room.getName()).set(room);
    }

    @Override
    public LinkedList<Room> findRooms() {
        final boolean[] temp = { false };
        final LinkedList<Room> linkedList = new LinkedList<>();
        firebaseFirestore.collection(roomCollection).get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult() != null) {
                            List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();
                            for (int i = 1; i < documentSnapshots.size(); i++) {
                                linkedList.add(documentSnapshots.get(i).toObject(Room.class));
                            }
                        }
                        temp[0] = true;
                    }
                });
        while (!temp[0]);
        return linkedList;
    }

    @Override
    public ChangesListener getListener(final Room room, final Procedure procedure) {
        final ChangesListener changesListener = new ChangesListener() {
            @Override
            public void call() {
                procedure.call();
            }
        };
        firebaseFirestore.collection(roomCollection).document(room.getName()).addSnapshotListener(
                new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e == null && documentSnapshot != null && documentSnapshot.exists()) {
                            room.setNowPlayers(documentSnapshot.toObject(Room.class).getNowPlayers());
                            changesListener.call();
                        }
                    }
                });
        return changesListener;
    }
}
