package pl.artur.hungryhero.module.helper;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import pl.artur.hungryhero.models.Contact;
import pl.artur.hungryhero.models.Localization;
import pl.artur.hungryhero.models.Menu;
import pl.artur.hungryhero.models.MenuItem;
import pl.artur.hungryhero.models.OpeningHours;
import pl.artur.hungryhero.models.Reservation;
import pl.artur.hungryhero.models.ReservationData;
import pl.artur.hungryhero.models.Restaurant;
import pl.artur.hungryhero.models.Reviews;
import pl.artur.hungryhero.models.Table;
import pl.artur.hungryhero.models.User;

public class FirebaseHelper {
    private final FirebaseAuth mAuth;
    private final FirebaseFirestore db;

    @Inject
    public FirebaseHelper(FirebaseAuth mAuth, FirebaseFirestore db) {
        this.mAuth = mAuth;
        this.db = db;
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public String getCurrentUid() {
        return mAuth.getCurrentUser().getUid();
    }

    public void logout() {
        mAuth.signOut();
    }

    public Task<AuthResult> signInWithEmailAndPassword(String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> createUserWithEmailAndPassword(String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    public Task<Void> updateUserProfile(UserProfileChangeRequest profileUpdates) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            return currentUser.updateProfile(profileUpdates);
        } else {
            return null;
        }
    }

    public Task<Void> setUserDocument(User user) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            return db.collection("Users").document(currentUser.getUid()).set(user);
        } else {
            return null;
        }
    }

    public Task<DocumentSnapshot> getUserDocument() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            return db.collection("Users").document(currentUser.getUid()).get();
        } else {
            return null;
        }
    }

    public DocumentReference getUserDocumentRef() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            return db.collection("Users").document(currentUser.getUid());
        } else {
            return null;
        }
    }

    public boolean isRestaurant(DocumentSnapshot document) {
        return "Restauracja".equals(document.getString("accountType"));
    }

    public void isRestaurant(Callback<Boolean> callback) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            db.collection("Users").document(currentUser.getUid()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        String accountType = document.getString("accountType");
                        callback.onResult("Restauracja".equals(accountType));
                    } else {
                        callback.onResult(false);
                    }
                } else {
                    callback.onResult(false);
                }
            });
        } else {
            callback.onResult(false);
        }
    }

    public interface Callback<T> {
        void onResult(T result);
    }

    public boolean isUser(DocumentSnapshot document) {
        return "Użytkownik".equals(document.getString("accountType"));
    }

    public UserProfileChangeRequest createProfileUpdateWithDisplayName(String displayName) {
        return new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build();
    }

    public void createEmptyRestaurantDocument() {
        FirebaseUser currentUser = getCurrentUser();
        if (currentUser != null) {
            db.collection("Restaurant").document(currentUser.getUid()).set(new HashMap<>());
        }
    }

    public Task<DocumentSnapshot> getRestaurantData() {
        FirebaseUser currentUser = getCurrentUser();
        if (currentUser != null) {
            DocumentReference restaurantRef = db.collection("Restaurant").document(currentUser.getUid());
            return restaurantRef.get();
        } else {
            return null;
        }
    }

    public Task<Void> updateRestaurantData(String name, String description, String photoUrl) {
        FirebaseUser currentUser = getCurrentUser();
        if (currentUser != null) {
            DocumentReference restaurantRef = db.collection("Restaurant").document(currentUser.getUid());
            return restaurantRef.update("name", name, "description", description, "photoUrl", photoUrl);
        } else {
            return null;
        }
    }

    public Task<Void> updateLocalizationData(Localization localization) {
        FirebaseUser currentUser = getCurrentUser();
        if (currentUser != null) {
            DocumentReference localizationRef = db.collection("Restaurant").document(currentUser.getUid());
            return localizationRef.update("localization", localization.toMap());
        } else {
            return null;
        }
    }

    public Task<Void> updateOpeningHoursData(OpeningHours openingHours) {
        FirebaseUser currentUser = getCurrentUser();
        if (currentUser != null) {
            DocumentReference localizationRef = db.collection("Restaurant").document(currentUser.getUid());
            return localizationRef.update("openingHours", openingHours.toMap());
        } else {
            return null;
        }
    }

    public Task<Void> updateContactData(Contact contact) {
        FirebaseUser currentUser = getCurrentUser();
        if (currentUser != null) {
            DocumentReference localizationRef = db.collection("Restaurant").document(currentUser.getUid());
            return localizationRef.update("contact", contact.toMap());
        } else {
            return null;
        }
    }

    public CollectionReference getTablesCollectionRef() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return db.collection("Restaurant").document(currentUser.getUid()).collection("tables");
        }
        return null;
    }

    public Task<DocumentReference> addTable(Table table) {
        FirebaseUser currentUser = getCurrentUser();
        if (currentUser != null) {
            DocumentReference restaurantRef = db.collection("Restaurant").document(currentUser.getUid());
            return restaurantRef.collection("tables").add(table);
        }
        return null;
    }

    public Task<Void> deleteTable(String tableId) {
        return getTablesCollectionRef().document(tableId).delete();
    }

    public Task<DocumentSnapshot> getTableDocument(String tableId) {
        FirebaseUser currentUser = getCurrentUser();
        if (currentUser != null) {
            DocumentReference tableRef = db.collection("Restaurant")
                    .document(currentUser.getUid())
                    .collection("tables")
                    .document(tableId);
            return tableRef.get();
        } else {
            return null;
        }
    }

    public Task<QuerySnapshot> getTablesForRestaurant(String restaurantId) {
        return db.collection("Restaurant")
                .document(restaurantId)
                .collection("tables")
                .get();
    }

    public Task<QuerySnapshot> getReservationForTables(DocumentSnapshot tableDoc, long todayTimestamp) {
        return tableDoc.getReference().collection("reservation").whereGreaterThanOrEqualTo("date", todayTimestamp).get();
    }

    public void updateTable(String tableId, Table table) {
        FirebaseUser currentUser = getCurrentUser();
        if (currentUser != null) {
            DocumentReference tableRef = db.collection("Restaurant").document(currentUser.getUid())
                    .collection("tables").document(tableId);
            Map<String, Object> updates = new HashMap<>();
            updates.put("number", table.getNumber());
            updates.put("capacity", table.getCapacity());
            tableRef.update(updates);
        }
    }

    public Task<DocumentReference> addMenuCategory(String menuName) {
        FirebaseUser currentUser = getCurrentUser();
        if (currentUser != null) {
            DocumentReference restaurantRef = db.collection("Restaurant").document(currentUser.getUid());
            Menu menuCategory = new Menu(menuName);
            return restaurantRef.collection("menu").add(menuCategory);
        }
        return null;
    }

    public Task<DocumentReference> addReservation(Reservation reservation, String restaurantId, String tableId) {
        CollectionReference reservationRef = db.collection("Restaurant").document(restaurantId).collection("tables").document(tableId).collection("reservation");
        return reservationRef.add(reservation.toMap());
    }

    public CollectionReference getMenuCategoriesCollectionRef() {
        FirebaseUser currentUser = getCurrentUser();
        if (currentUser != null) {
            return db.collection("Restaurant").document(currentUser.getUid()).collection("menu");
        }
        return null;
    }

    public CollectionReference getMenuCategoriesCollectionRef(String restaurantId) {
        return db.collection("Restaurant").document(restaurantId).collection("menu");
    }

    public CollectionReference getDishesCollectionRef(String menuId, String restaurantId) {
        return db.collection("Restaurant")
                .document(restaurantId)
                .collection("menu")
                .document(menuId)
                .collection("menuItem");
    }

    public Task<Void> updateMenuName(String menuId, String newMenuName) {
        CollectionReference menuCategoryRef = getMenuCategoriesCollectionRef();
        DocumentReference menuRef = menuCategoryRef.document(menuId);
        return menuRef.update("menuName", newMenuName);
    }

    public Task<DocumentReference> addMenuItem(String menuId, MenuItem menuItem) {
        FirebaseUser currentUser = getCurrentUser();
        if (currentUser != null) {
            DocumentReference menuRef = db.collection("Restaurant").document(currentUser.getUid())
                    .collection("menu").document(menuId);
            return menuRef.collection("menuItem").add(menuItem);
        }
        return null;
    }

    public void updateMenuItem(String menuId, String menuItemId, MenuItem menuItem) {
        FirebaseUser currentUser = getCurrentUser();
        if (currentUser != null) {
            DocumentReference menuItemRef = db.collection("Restaurant").document(currentUser.getUid())
                    .collection("menu").document(menuId)
                    .collection("menuItem").document(menuItemId);
            menuItemRef.update(menuItem.toMap());
        }
    }

    public void deleteMenuItem(String menuId, String menuItemId) {
        FirebaseUser currentUser = getCurrentUser();
        if (currentUser != null) {
            DocumentReference menuItemRef = db.collection("Restaurant").document(currentUser.getUid())
                    .collection("menu").document(menuId)
                    .collection("menuItem").document(menuItemId);
            menuItemRef.delete();
        }
    }

    public CollectionReference getReviewsCollectionRef(String restaurantId) {
        return db.collection("Restaurant")
                .document(restaurantId)
                .collection("reviews");
    }

    public Task<DocumentReference> addReview(String restaurantId, Reviews reviews) {
        return db.collection("Restaurant").document(restaurantId)
                .collection("reviews").add(reviews);
    }

    public void fetchUserReservations(String userId, OnReservationsFetchedListener listener) {
        db.collectionGroup("reservation")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<ReservationData> reservationDataList = new ArrayList<>();
                    AtomicInteger counter = new AtomicInteger(queryDocumentSnapshots.size());

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Reservation reservation = document.toObject(Reservation.class);

                        // Przetwarzaj tylko jeśli userId i restaurantId nie są null
                        if (reservation.getUserId() != null && reservation.getRestaurantId() != null) {
                            fetchUserData(reservation.getUserId(), user -> {
                                fetchRestaurantData(reservation.getRestaurantId(), restaurant -> {
                                    ReservationData reservationData = new ReservationData(reservation, user, restaurant);
                                    reservationDataList.add(reservationData);
                                    if (counter.decrementAndGet() == 0) {
                                        listener.onReservationsFetched(reservationDataList);
                                    }
                                });
                            });
                        } else {
                            // Jeśli userId lub restaurantId są null, zmniejsz licznik i kontynuuj
                            if (counter.decrementAndGet() == 0) {
                                listener.onReservationsFetched(reservationDataList);
                            }
                        }
                    }
                })
                .addOnFailureListener(listener::onError);
    }


    public void fetchAllReservations(OnReservationsFetchedListener listener) {
        FirebaseUser currentUser = getCurrentUser();
        if (currentUser == null || currentUser.getUid() == null) {
            listener.onReservationsFetched(new ArrayList<>());
            return;
        }

        db.collection("Restaurant").document(currentUser.getUid()).collection("tables")
                .get()
                .addOnSuccessListener(tableQueryDocumentSnapshots -> {
                    List<ReservationData> allReservationData = new ArrayList<>();
                    AtomicInteger counter = new AtomicInteger();

                    for (QueryDocumentSnapshot tableDocument : tableQueryDocumentSnapshots) {
                        tableDocument.getReference().collection("reservation")
                                .get()
                                .addOnSuccessListener(reservationQueryDocumentSnapshots -> {
                                    reservationQueryDocumentSnapshots.forEach(reservationDocument -> counter.incrementAndGet());
                                    for (QueryDocumentSnapshot reservationDocument : reservationQueryDocumentSnapshots) {
                                        Reservation reservation = reservationDocument.toObject(Reservation.class);
                                        if (reservation.getUserId() != null) {
                                            fetchUserData(reservation.getUserId(), user -> {
                                                if (reservation.getRestaurantId() != null) {
                                                    fetchRestaurantData(reservation.getRestaurantId(), restaurant -> {
                                                        ReservationData reservationData = new ReservationData(reservation, user, restaurant);
                                                        allReservationData.add(reservationData);
                                                        if (counter.decrementAndGet() == 0) {
                                                            listener.onReservationsFetched(allReservationData);
                                                        }
                                                    });
                                                } else {
                                                    counter.decrementAndGet(); // Dekrementuj licznik, jeśli restaurantId jest null
                                                }
                                            });
                                        } else {
                                            counter.decrementAndGet(); // Dekrementuj licznik, jeśli userId jest null
                                            if (reservation.getRestaurantId() != null) {
                                                counter.decrementAndGet(); // Dekrementuj drugi raz, jeśli restaurantId nie jest null
                                            }
                                        }
                                    }
                                })
                                .addOnFailureListener(listener::onError);
                    }
                })
                .addOnFailureListener(listener::onError);
    }


    private void fetchUserData(String userId, OnUserDataFetchedListener userDataListener) {
        db.collection("Users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                    userDataListener.onUserDataFetched(user);
                })
                .addOnFailureListener(e -> {});
    }

    private void fetchRestaurantData(String restaurantId, OnRestaurantDataFetchedListener restaurantDataListener) {
        db.collection("Restaurant").document(restaurantId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Restaurant restaurant = documentSnapshot.toObject(Restaurant.class);
                    restaurantDataListener.onRestaurantDataFetched(restaurant);
                })
                .addOnFailureListener(e -> {});
    }

    public void fetchReservationsForMonth(int year, int month, OnReservationsFetchedListener listener) {
        FirebaseUser currentUser = getCurrentUser();
        if (currentUser == null)
            return;

        Calendar startOfMonth = Calendar.getInstance();
        startOfMonth.set(year, month, 1, 0, 0, 0);
        long startTime = startOfMonth.getTimeInMillis();

        Calendar endOfMonth = Calendar.getInstance();
        endOfMonth.set(year, month, startOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
        long endTime = endOfMonth.getTimeInMillis();

        db.collectionGroup("reservation")
                .whereEqualTo("restaurantId", currentUser.getUid())
                .whereGreaterThanOrEqualTo("date", startTime)
                .whereLessThanOrEqualTo("date", endTime)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Reservation> reservations = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        reservations.add(document.toObject(Reservation.class));
                    }
                    listener.onReservationsFetched2(reservations);
                })
                .addOnFailureListener(e -> {
                    // Obsługa błędu
                });
    }


    public interface OnUserDataFetchedListener {
        void onUserDataFetched(User user);
    }

    public interface OnRestaurantDataFetchedListener {
        void onRestaurantDataFetched(Restaurant restaurant);
    }

    public interface OnReservationsFetchedListener {
        void onReservationsFetched(List<ReservationData> reservationDataList);
        void onReservationsFetched2(List<Reservation> reservations);
        void onError(Exception e);
    }


}
