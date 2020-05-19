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
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.programmers.enums.Difficulty;
import com.programmers.game.Field;
import com.programmers.interfaces.Procedure;
import com.programmers.interfaces.SpecificCode;
import com.programmers.screens.GameScreen;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public final class AndroidSpecificCode implements SpecificCode {

    private final static String roomCollection = "rooms";
    private final static String dataRoomCollection = "data";

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
    public boolean roomExists(String name) {
        final Boolean[] temp = { null };
        firebaseFirestore.collection(roomCollection).document(name).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        temp[0] = documentSnapshot.exists();
                    }
                });
        while (temp[0] == null);
        return temp[0];
    }

    @Override
    public void deleteRoom(String name) {
        firebaseFirestore.collection(roomCollection).document(name).delete();
        firebaseFirestore.collection(roomCollection).document(name)
                .collection(dataRoomCollection).document(dataRoomCollection).delete();
    }

    @Override
    public boolean addPlayerToRoom(final Room room) {
        final Boolean[] temp = { null };
        firebaseFirestore.collection(roomCollection).document(room.getName())
                .get().addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            room.setNowPlayers(room.getNowPlayers() + 1);
                            firebaseFirestore.collection(roomCollection)
                                    .document(room.getName()).set(room);
                            temp[0] = true;
                        } else {
                            temp[0] = false;
                        }
                    }
                });
        while (temp[0] == null);
        return temp[0];
    }

    @Override
    public boolean removePlayerFromRoom(final Room room) {
        final Boolean[] temp = { null };
        firebaseFirestore.collection(roomCollection).document(room.getName())
                .get().addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            room.setNowPlayers(room.getNowPlayers() - 1);
                            firebaseFirestore.collection(roomCollection)
                                    .document(room.getName()).set(room);
                            temp[0] = true;
                        } else {
                            temp[0] = false;
                        }
                    }
                });
        while (temp[0] == null);
        return temp[0];
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

    private ListenerRegistration listenerRegistration;

    @Override
    public void addRoomChangedListener(final Room room, final Procedure procedure) {
        listenerRegistration = firebaseFirestore.collection(roomCollection)
                .document(room.getName()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e == null && documentSnapshot != null && documentSnapshot.exists()) {
                    Room newRoom = documentSnapshot.toObject(Room.class);
                    if (newRoom != null) {
                        room.setName(newRoom.getName());
                        room.setPlayersCount(newRoom.getPlayersCount());
                        room.setDifficulty(newRoom.getDifficulty());
                        room.setNowPlayers(newRoom.getNowPlayers());
                        procedure.call();
                    }
                }
            }
        });
    }

    @Override
    public void removeListener(Room room) {
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }

    @Override
    public void sendFieldData(Room room, com.programmers.game.Field field) {
        Field fieldData = new Field(field);
        firebaseFirestore.collection(roomCollection).document(room.getName())
                .collection(dataRoomCollection).document(dataRoomCollection).set(fieldData);
    }

    @Override
    public Field getFieldData(Room room) {
        final Field[] field = { null };
        firebaseFirestore.collection(roomCollection).document(room.getName())
                .collection(dataRoomCollection).document(dataRoomCollection)
                .get().addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        field[0] = documentSnapshot.toObject(Field.class);
                    }
                }
        );
        while (field[0] == null);
        return field[0];
    }
}
