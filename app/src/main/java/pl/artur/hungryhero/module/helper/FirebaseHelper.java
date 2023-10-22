package pl.artur.hungryhero.module.helper;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import javax.inject.Inject;

import pl.artur.hungryhero.models.Localization;
import pl.artur.hungryhero.models.OpeningHours;
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

    public Task<Void> updateRestaurantData(String name, String description) {
        FirebaseUser currentUser = getCurrentUser();
        if (currentUser != null) {
            DocumentReference restaurantRef = db.collection("Restaurant").document(currentUser.getUid());
            return restaurantRef.update("name", name, "description", description);
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

}
