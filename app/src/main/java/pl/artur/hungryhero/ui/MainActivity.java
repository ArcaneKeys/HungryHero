package pl.artur.hungryhero.ui;

import android.os.Bundle;

import pl.artur.hungryhero.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseLayout(R.layout.activity_main);
    }
}