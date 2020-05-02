package nl.brian.sudoku;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.Space;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.List;

import nl.brian.sudoku.model.Sudoku;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final AdView adView = findViewById(R.id.adView);

        adView.setVisibility(View.INVISIBLE);

        final Sudoku sudoku = new Sudoku().getSudoku(this);

        ConstraintLayout constraintLayout = findViewById(R.id.mainConstraint);
        Space space = findViewById(R.id.spacer);
        sudoku.render(constraintLayout,space);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

                // Create an ad request.
                AdRequest.Builder adRequestBuilder = new AdRequest.Builder();

                // Start loading the ad.
                adView.loadAd(adRequestBuilder.build());
                adView.setVisibility(View.VISIBLE);
            }
        });

    }



}


