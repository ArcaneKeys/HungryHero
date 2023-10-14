package pl.artur.hungryhero.ui.splashFragment;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Random;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pl.artur.hungryhero.R;

@AndroidEntryPoint
public class WelcomeFragment extends Fragment {
    @Inject
    StorageReference storageReference;
    String backgroundUrl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.splash_fragment_welcome, container, false);

        // Losowanie życzenia
        String[] wishesArray = getResources().getStringArray(R.array.wishes);
        Random random = new Random();
        String randomWishData = wishesArray[random.nextInt(wishesArray.length)];
        String[] splitData = randomWishData.split("\\|");
        String randomWish = splitData[0];
        String backgroundUrlString = splitData[1];

        // Ustawienie życzenia w TextView
        TextView wishTextView = view.findViewById(R.id.today_wish_text);
        wishTextView.setText(randomWish);

        StorageReference fileRef = storageReference.child(backgroundUrlString);
            fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            backgroundUrl = uri.toString();

            Glide.with(WelcomeFragment.this)
                    .load(backgroundUrl)
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            view.setBackground(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
        }).addOnFailureListener(Throwable::printStackTrace);

        return view;
    }
}
