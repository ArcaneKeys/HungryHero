package pl.artur.hungryhero.module.helper;

import android.content.SharedPreferences;

import javax.inject.Inject;

public class PreferencesHelper {

    private final SharedPreferences sharedPreferences;

    @Inject
    public PreferencesHelper(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void setSeenSplash(boolean value) {
        sharedPreferences.edit().putBoolean("seen_splash", value).apply();
    }

    public boolean hasSeenSplash() {
        return sharedPreferences.getBoolean("seen_splash", false);
    }
}
