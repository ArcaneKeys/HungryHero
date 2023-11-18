package pl.artur.hungryhero.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.StorageReference;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pl.artur.hungryhero.R;
import pl.artur.hungryhero.module.helper.FirebaseHelper;

@AndroidEntryPoint
public class AddOrEditInfoActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextDescription;
    private Button buttonSave, buttonAddPhoto;
    private Uri selectedImageUri;
    private String imageUrl;
    private ImageView imageViewPhoto;

    @Inject
    StorageReference storageReference;
    @Inject
    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_info);

        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonSave = findViewById(R.id.buttonSave);
        buttonAddPhoto = findViewById(R.id.buttonAddPhoto);
        imageViewPhoto = findViewById(R.id.imageViewPhoto);

        firebaseHelper.getRestaurantData().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String name = documentSnapshot.getString("name");
                String description = documentSnapshot.getString("description");
                String photoUrl = documentSnapshot.getString("photoUrl");

                if (photoUrl != null && !photoUrl.isEmpty()) {
                    Glide.with(this)
                            .load(photoUrl)
                            .into(imageViewPhoto);
                    imageUrl = photoUrl;
                }

                editTextName.setText(name);
                editTextDescription.setText(description);
            } else {
                firebaseHelper.createEmptyRestaurantDocument();
            }
        });

        buttonSave.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            String description = editTextDescription.getText().toString();

            uploadImage(selectedImageUri, () -> {
                firebaseHelper.updateRestaurantData(name, description, imageUrl)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(AddOrEditInfoActivity.this, "Dane zaktualizowane pomyślnie", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(AddOrEditInfoActivity.this, "Błąd podczas aktualizacji danych", Toast.LENGTH_SHORT).show();
                        });
            }, () -> {
                firebaseHelper.updateRestaurantData(name, description, "")
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(AddOrEditInfoActivity.this, "Dane zaktualizowane pomyślnie", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(AddOrEditInfoActivity.this, "Błąd podczas aktualizacji danych", Toast.LENGTH_SHORT).show();
                        });
            });

        });

        buttonAddPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            mGetContent.launch(intent);
        });
    }

    private final ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    Glide.with(this)
                            .load(selectedImageUri)
                            .into(imageViewPhoto);
                }
            }
    );

    private String getFileName(Uri uri) {
        String result = null;
        if (uri != null && uri.getScheme() != null && uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (columnIndex != -1) {
                        result = cursor.getString(columnIndex);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null && uri != null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public void uploadImage(Uri imageUri, Runnable onSuccess, Runnable onFailure) {
        String uriName = firebaseHelper.getCurrentUid();
        StorageReference userMenuRef = storageReference.child(uriName).child("restaurantPhotos");
        String fileName = getFileName(imageUri);
        StorageReference fileRef = userMenuRef.child(fileName);

        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                imageUrl = uri.toString();
                onSuccess.run();
            }).addOnFailureListener(e -> {
                onFailure.run();
            });
        }).addOnFailureListener(e -> {
            onFailure.run();
        });
    }
}