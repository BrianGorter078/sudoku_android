package nl.brian.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
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
        List<List<Integer>> sudokuStart = sudoku.getStart();

        for(int i = 0; i < sudokuStart.size(); i+=1){
            System.out.println(sudokuStart.get(i));
        }

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


