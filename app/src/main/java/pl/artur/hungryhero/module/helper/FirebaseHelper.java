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

    public Task<Void> setUserDocument(String userId, User user) {
        return db.collection("Users").document(userId).set(user);
    }

    public Task<Void> createEmptyRestaurantDocument(String userId) {
        return db.collection("restaurants").document(userId).set(new HashMap<>());
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


}
