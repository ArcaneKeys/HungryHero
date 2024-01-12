package pl.artur.hungryhero.ui;

import static pl.artur.hungryhero.utils.ValidationUtils.isValidEmail;
import static pl.artur.hungryhero.utils.ValidationUtils.isValidPhoneNumber;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.validator.routines.UrlValidator;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pl.artur.hungryhero.R;
import pl.artur.hungryhero.models.Contact;
import pl.artur.hungryhero.models.Restaurant;
import pl.artur.hungryhero.module.helper.FirebaseHelper;

@AndroidEntryPoint
public class AddOrEditContactActivity extends AppCompatActivity {

    private EditText phoneEditText, emailEditText, facebookEditText, websiteEditText, instagramEditText, webMenuEditText;
    private Toolbar toolbar;

    @Inject
    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_contact);

        initializeUI();

        firebaseHelper.getRestaurantData().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Restaurant restaurant = documentSnapshot.toObject(Restaurant.class);
                if (restaurant != null) {
                    Contact contact = restaurant.getContact();
                    if (contact != null) {
                        fillFormFields(contact);
                    }
                }
                else {
                    Toast.makeText(AddOrEditContactActivity.this, "Błąd podczas konwertowania dokumentu na obiekt Restaurant.", Toast.LENGTH_SHORT).show();
                }
            } else {
                firebaseHelper.createEmptyRestaurantDocument();
            }
        });
    }

    private void fillFormFields(Contact contact) {
        phoneEditText.setText(contact.getPhone() != null ? contact.getPhone() : "");
        emailEditText.setText(contact.getEmail() != null ? contact.getEmail() : "");
        facebookEditText.setText(contact.getFacebook() != null ? contact.getFacebook() : "https://facebook.com/");
        instagramEditText.setText(contact.getInstagram() != null ? contact.getInstagram() : "https://instagram.com/");
        websiteEditText.setText(contact.getWebsite() != null ? contact.getWebsite() : "");
        webMenuEditText.setText(contact.getWebMenu() != null ? contact.getWebMenu() : "");
    }

    private void initializeUI() {
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        facebookEditText = findViewById(R.id.facebookEditText);
        websiteEditText = findViewById(R.id.websiteEditText);
        instagramEditText = findViewById(R.id.instagramEditText);
        webMenuEditText = findViewById(R.id.webMenuEditText);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Button saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(v -> {
            if (isValidInput()) {
                Contact contact = getContactFromForm();
                firebaseHelper.updateContactData(contact)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(AddOrEditContactActivity.this, "Dane zaktualizowane pomyślnie", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(AddOrEditContactActivity.this, "Błąd podczas aktualizacji danych", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(AddOrEditContactActivity.this, "Proszę wprowadzić poprawne dane", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidInput() {
        String phone = String.valueOf(phoneEditText.getText());
        String email = String.valueOf(emailEditText.getText());
        String facebook = String.valueOf(facebookEditText.getText());
        String website = String.valueOf(websiteEditText.getText());
        String instagram = String.valueOf(instagramEditText.getText());
        String webMenu = String.valueOf(webMenuEditText.getText());

        UrlValidator urlValidator = new UrlValidator();

        if (!isValidEmail(email) && !email.isEmpty()) {
            emailEditText.setError("Nieprawidłowy adres email");
            return false;
        }

        if (!isValidPhoneNumber(phone) && !phone.isEmpty()) {
            phoneEditText.setError("Nieprawidłowy numer telefonu");
            return false;
        }

        if (!urlValidator.isValid(facebook) && !facebook.isEmpty()) {
            facebookEditText.setError("Nieprawidłowy link do faacebook");
            return false;
        }

        if (!urlValidator.isValid(website) && !website.isEmpty()) {
            websiteEditText.setError("Nieprawidłowy link do strony");
            return false;
        }

        if (!urlValidator.isValid(instagram) && !instagram.isEmpty()) {
            instagramEditText.setError("Nieprawidłowy link do instagram");
            return false;
        }

        if (!urlValidator.isValid(webMenu) && !webMenu.isEmpty()) {
            webMenuEditText.setError("Nieprawidłowy link do strony menu");
            return false;
        }

        return true;
    }

    private Contact getContactFromForm() {
        String phone = String.valueOf(phoneEditText.getText());
        String email = String.valueOf(emailEditText.getText());
        String facebook = String.valueOf(facebookEditText.getText());
        String website = String.valueOf(websiteEditText.getText());
        String instagram = String.valueOf(instagramEditText.getText());
        String webMenu = String.valueOf(webMenuEditText.getText());

        return new Contact(phone, email, facebook, website, instagram, webMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}