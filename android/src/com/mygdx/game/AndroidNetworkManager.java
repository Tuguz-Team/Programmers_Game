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
import com.programmers.game.online.OnlineGameController;
import com.programmers.interfaces.Procedure;
import com.programmers.interfaces.NetworkManager;

import java.util.LinkedList;
import java.util.List;

public final class AndroidNetworkManager implements NetworkManager {

    private final static String rooms = "rooms";
    private final static String fieldData = "fieldData";
    private final static String gameData = "gameData";
    private final static String playersData = "playersData";
    private final static String cardsData = "cardsData";

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final Activity activity;

    AndroidNetworkManager(Activity activity) {
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
        firebaseFirestore.collection(rooms).document(name).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            temp[0] = false;
                        } else {
                            temp[0] = true;
                            Room room = new Room(name, playersCount, difficulty);
                            firebaseFirestore.collection(rooms).document(name).set(room);
                        }
                    }
                });
        while (temp[0] == null);
        return temp[0];
    }

    @Override
    public boolean roomExists(String name) {
        final Boolean[] temp = { null };
        firebaseFirestore.collection(rooms).document(name).get()
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
        firebaseFirestore.collection(rooms).document(name).delete();
    }

    @Override
    public void launchRoom(Room room) {
        room.setLaunched(true);
        firebaseFirestore.collection(rooms).document(room.getName()).set(room);
    }

    @Override
    public boolean addPlayerToRoom(final Room room) {
        final Boolean[] temp = { null };
        firebaseFirestore.collection(rooms).document(room.getName())
                .get().addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            room.setNowPlayers(room.getNowPlayers() + 1);
                            firebaseFirestore.collection(rooms)
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
        firebaseFirestore.collection(rooms).document(room.getName())
                .get().addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            room.setNowPlayers(room.getNowPlayers() - 1);
                            firebaseFirestore.collection(rooms)
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
        firebaseFirestore.collection(rooms).get().addOnCompleteListener(
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
        listenerRegistration = firebaseFirestore.collection(rooms)
                .document(room.getName())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e == null && documentSnapshot != null && documentSnapshot.exists()) {
                    Room newRoom = documentSnapshot.toObject(Room.class);
                    if (newRoom != null) {
                        room.set(newRoom);
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
        FieldData fieldData = new FieldData(field);
        firebaseFirestore.collection(rooms).document(room.getName())
                .collection(AndroidNetworkManager.fieldData)
                .document(AndroidNetworkManager.fieldData)
                .set(fieldData);
    }

    @Override
    public FieldData getFieldData(Room room) {
        final FieldData[] fieldData = { null };
        firebaseFirestore.collection(rooms).document(room.getName())
                .collection(AndroidNetworkManager.fieldData)
                .document(AndroidNetworkManager.fieldData)
                .get().addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        fieldData[0] = documentSnapshot.toObject(FieldData.class);
                    }
                }
        );
        while (fieldData[0] == null);
        return fieldData[0];
    }

    @Override
    public void sendGameData(Room room, OnlineGameController onlineGameController) {
        GameData gameData = new GameData(onlineGameController);
        firebaseFirestore.collection(rooms).document(room.getName())
                .collection(AndroidNetworkManager.gameData)
                .document(playersData).set(gameData.getPlayersData());
        firebaseFirestore.collection(rooms).document(room.getName())
                .collection(AndroidNetworkManager.gameData)
                .document(cardsData).set(gameData.getCardsData());
    }

    @Override
    public GameData getGameData(Room room) {
        final GameData[] gameData = { null };
        final GameData.PlayersData[] playersData = { null };
        firebaseFirestore.collection(rooms).document(room.getName())
                .collection(AndroidNetworkManager.gameData)
                .document(AndroidNetworkManager.playersData)
                .get().addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        playersData[0] = documentSnapshot.toObject(GameData.PlayersData.class);
                    }
                }
        );
        while (playersData[0] == null);
        firebaseFirestore.collection(rooms).document(room.getName())
                .collection(AndroidNetworkManager.gameData)
                .document(cardsData)
                .get().addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        gameData[0] = new GameData(
                                playersData[0],
                                documentSnapshot.toObject(GameData.CardsData.class)
                        );
                    }
                }
        );
        while (gameData[0] == null);
        return gameData[0];
    }
}
