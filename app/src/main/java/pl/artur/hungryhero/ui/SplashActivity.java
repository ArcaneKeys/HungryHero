package pl.artur.hungryhero.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pl.artur.hungryhero.module.helper.FirebaseHelper;
import pl.artur.hungryhero.module.helper.PreferencesHelper;
import pl.artur.hungryhero.R;
import pl.artur.hungryhero.ui.splashFragment.FinalFragment;
import pl.artur.hungryhero.ui.splashFragment.InfoFragment;
import pl.artur.hungryhero.ui.splashFragment.MenuFragment;
import pl.artur.hungryhero.ui.splashFragment.PhotosFragment;
import pl.artur.hungryhero.ui.splashFragment.RegisterRestaurantWelcomeFragment;
import pl.artur.hungryhero.ui.splashFragment.WelcomeFragment;

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
public class SplashActivity extends AppCompatActivity {

    @Inject
    FirebaseHelper firebaseHelper;

    private ViewPager2 viewPager;
    private Button nextButton;
    private LinearLayout dotsLayout;
    private static final int TOTAL_STEPS = 5;
    private boolean showOnlyWelcomeScreen = false;


    @Inject
    PreferencesHelper preferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        viewPager = findViewById(R.id.viewPager);
        nextButton = findViewById(R.id.nextButton);
        dotsLayout = findViewById(R.id.dotsLayout);

        firebaseHelper.getUserDocument().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    if (firebaseHelper.isUser(document)) {
                        preferencesHelper.setSeenSplash(true);
                    }
                }
            }
        });

        if (preferencesHelper.hasSeenSplash()) {
            showOnlyWelcomeScreen = true;
        } else {
            preferencesHelper.setSeenSplash(true);
        }

        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if (showOnlyWelcomeScreen) {
                    return new WelcomeFragment();
                }

                switch (position) {
                    case 0:
                        return new RegisterRestaurantWelcomeFragment();
                    case 1:
                        return new InfoFragment();
                    case 2:
                        return new MenuFragment();
                    case 3:
                        return new PhotosFragment();
                    case 4:
                        return new FinalFragment();
                    default:
                        return new WelcomeFragment();
                }
            }

            @Override
            public int getItemCount() {
                return showOnlyWelcomeScreen ? 1 : TOTAL_STEPS;
            }
        });

        addDotsIndicator(0);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                addDotsIndicator(position);
                if (position == getTotalSteps() - 1) {
                    nextButton.setText(R.string.start);
                } else {
                    nextButton.setText(R.string.app_next);
                }
            }
        });

        nextButton.setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();

            if (currentItem < getTotalSteps() - 1) {
                viewPager.setCurrentItem(currentItem + 1);
            } else {
                firebaseHelper.getUserDocument().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            if (firebaseHelper.isRestaurant(document)) {
                                Intent intent = new Intent(SplashActivity.this, RestaurantMainActivity.class);
                                startActivity(intent);
                                finish();
                            } else if (firebaseHelper.isUser(document)) {
                                Intent intent = new Intent(SplashActivity.this, UserMainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                });
            }
        });
    }

    private void addDotsIndicator(int position) {
        TextView[] dots = new TextView[getTotalSteps()];
        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.colorInactiveDot));
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.colorActiveDot));
        }
    }

    private int getTotalSteps() {
        return viewPager.getAdapter().getItemCount();
    }
}