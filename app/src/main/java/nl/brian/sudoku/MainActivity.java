package nl.brian.sudoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels;
        float dpWidth = displayMetrics.widthPixels;

        ConstraintLayout parentLayout = findViewById(R.id.mainConstraint);
        int topId = adView.getId();
        for(int i = 0; i < sudokuStart.size(); i+=1) {
            int prevViewId = -1;
            for (int j = 0; j < 9; j += 1) {
                if(j==0){
                    View prevView = CreateGridItem(this,dpWidth,String.valueOf(sudokuStart.get(i).get(j)),parentLayout,topId,-1);
                    prevViewId = prevView.getId();
                    continue;
                }

                View view = CreateGridItem(this,dpWidth,String.valueOf(sudokuStart.get(i).get(j)),parentLayout,topId,prevViewId);
                prevViewId = view.getId();
                if(j == 8){
                    topId = view.getId();
                }
            }
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


    private View CreateGridItem(Context ctx, float dpWidth, String input,ConstraintLayout parentLayout,int topId, int leftId){
        ConstraintSet set = new ConstraintSet();

        EditText editText = new EditText(ctx);
        if(!input.equals("0")){
            editText.setText(input);
        }
        editText.setWidth(((int)dpWidth / 9));
        editText.setId(View.generateViewId());

        parentLayout.addView(editText);
        set.clone(parentLayout);
        set.connect(editText.getId(),ConstraintSet.TOP,topId,ConstraintSet.BOTTOM);
        if(leftId > -1){
            set.connect(editText.getId(),ConstraintSet.LEFT, leftId, ConstraintSet.RIGHT);
        }

        set.applyTo(parentLayout);

        return editText;
    }

}


