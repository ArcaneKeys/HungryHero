package pl.artur.hungryhero;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pl.artur.hungryhero.adapters.IngredientsAdapter;
import pl.artur.hungryhero.models.MenuItem;
import pl.artur.hungryhero.module.helper.FirebaseHelper;

@AndroidEntryPoint
public class AddDishActivity extends AppCompatActivity {

    private List<String> ingredients;
    private IngredientsAdapter ingredientsAdapter;

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
        setContentView(R.layout.activity_add_dish);

        TextInputEditText editTextDishName = findViewById(R.id.editTextDishName);
        TextInputEditText editTextDescription = findViewById(R.id.editTextDescription);
        TextInputEditText editTextPrice = findViewById(R.id.editTextPrice);
        RecyclerView ingredientsRecyclerView = findViewById(R.id.ingredientsRecyclerView);
        Button buttonAddIngredient = findViewById(R.id.buttonAddIngredient);
        Button buttonAddPhoto = findViewById(R.id.buttonAddPhoto);
        Button buttonSubmit = findViewById(R.id.buttonSubmit);
        imageViewPhoto = findViewById(R.id.imageViewPhoto);

        String menuId = getIntent().getStringExtra("menuId");
        String menuItemId = getIntent().getStringExtra("menuItemId");
        MenuItem parcelableMenuItem = getIntent().getParcelableExtra("menuItem");

        if (menuItemId != null) {
            if (parcelableMenuItem != null) {
                editTextDishName.setText(parcelableMenuItem.getDishName());
                editTextDescription.setText(parcelableMenuItem.getDescription());
                editTextPrice.setText(String.valueOf(parcelableMenuItem.getPrice()));
                ingredients = new ArrayList<>(parcelableMenuItem.getIngredients());
                ingredientsAdapter = new IngredientsAdapter(ingredients);
                ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                ingredientsRecyclerView.setAdapter(ingredientsAdapter);
                imageUrl = "";
                buttonSubmit.setText("Zapisz");

                if (parcelableMenuItem.getPhotoUrl() != null && !parcelableMenuItem.getPhotoUrl().isEmpty()) {
                    imageUrl = parcelableMenuItem.getPhotoUrl();
                    Glide.with(this)
                            .load(parcelableMenuItem.getPhotoUrl())
                            .into(imageViewPhoto);
                }

                if (selectedImageUri != null) {
                    buttonSubmit.setOnClickListener(v -> {
                        uploadImage(selectedImageUri, () -> {
                            String dishName = editTextDishName.getText().toString();
                            String dishDescription = editTextDescription.getText().toString();
                            String priceString = editTextPrice.getText().toString();
                            double price = 0;
                            if (!priceString.isEmpty()) {
                                price = Double.parseDouble(priceString);
                            }
                            MenuItem menuItem = new MenuItem(dishName, dishDescription, ingredients, price, imageUrl);
                            firebaseHelper.updateMenuItem(menuId, menuItemId, menuItem);
                            finish();
                        });
                    });
                } else {
                    buttonSubmit.setOnClickListener(v -> {
                        String dishName = editTextDishName.getText().toString();
                        String dishDescription = editTextDescription.getText().toString();
                        String priceString = editTextPrice.getText().toString();
                        double price = 0;
                        if (!priceString.isEmpty()) {
                            price = Double.parseDouble(priceString);
                        }
                        MenuItem menuItem = new MenuItem(dishName, dishDescription, ingredients, price, imageUrl);
                        firebaseHelper.updateMenuItem(menuId, menuItemId, menuItem);
                        finish();
                });
                }
            }
        } else {
            ingredients = new ArrayList<>();
            ingredientsAdapter = new IngredientsAdapter(ingredients);

            ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            ingredientsRecyclerView.setAdapter(ingredientsAdapter);

            buttonSubmit.setOnClickListener(v -> {
                uploadImage(selectedImageUri, () -> {
                    String dishName = editTextDishName.getText().toString();
                    String dishDescription = editTextDescription.getText().toString();
                    String priceString = editTextPrice.getText().toString();
                    double price = 0;
                    if (!priceString.isEmpty()) {
                        price = Double.parseDouble(priceString);
                    }
                    MenuItem menuItem = new MenuItem(dishName, dishDescription, ingredients, price, imageUrl);
                    firebaseHelper.addMenuItem(menuId, menuItem);
                    finish();
                });
            });
        }

        buttonAddIngredient.setOnClickListener(v -> {
            ingredients.add("");
            ingredientsAdapter.notifyItemInserted(ingredients.size() - 1);
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

    public void uploadImage(Uri imageUri, Runnable onSuccess) {
        String uriName = firebaseHelper.getCurrentUid();
        StorageReference userMenuRef = storageReference.child(uriName).child("menu");
        String fileName = getFileName(imageUri);
        StorageReference fileRef = userMenuRef.child(fileName);

        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                imageUrl = uri.toString();
                onSuccess.run();
            }).addOnFailureListener(e -> {

            });
        }).addOnFailureListener(e -> {

        });
    }

}