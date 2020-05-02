package nl.brian.sudoku.model;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.Space;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Sudoku {
    private List<List<Integer>> start;
    private List<List<Integer>> current;
    private List<List<Integer>> solution;
    private Context ctx;
    private JSONArray sudokus = null;
    private long startDate;
    private ConstraintLayout layout;

    public Sudoku getSudoku(Context ctx){
        try {
            if(sudokus == null){
                String SudokuJson = AssetJSONFile(ctx);
                sudokus = new JSONArray(SudokuJson);
            }

            JSONObject sudoku = getRandomSudoku();

            char[] startChars = sudoku.getString("quizzes").toCharArray();
            char[] solutionChars = sudoku.getString("solutions").toCharArray();

            List<List<Integer>> start = getSudokuAsList(startChars);
            List<List<Integer>> solution = getSudokuAsList(solutionChars);
            // Copying start will link them -> new entry in current will be available in start
            List<List<Integer>> current = getSudokuAsList(startChars);

            this.ctx = ctx;
            this.start = start;
            this.solution = solution;
            this.current = current;

            return this;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<List<Integer>> getSudokuAsList(char[] Chars) {
        List<List<Integer>> list = new ArrayList<>();
        for (int i = 0; i < 9; i+=1){
            List<Integer> row = new ArrayList<>();
            for(int j = 0; j < 9; j+=1) {
                row.add(Integer.valueOf(String.valueOf(Chars[i*9+j])));
            }
            list.add(row);
        }
        return list;
    }

    private JSONObject getRandomSudoku() throws JSONException {
        if(sudokus == null){
            return null;
        }
        Random rnd =  new Random();
        int rndInt = rnd.nextInt(sudokus.length());

        return (JSONObject) sudokus.get(rndInt);
    }

    public List<List<Integer>> getStart() {
        return start;
    }

    public List<List<Integer>> getSolution() {
        return solution;
    }

    public void render(ConstraintLayout parentLayout, Space space){
        this.layout = parentLayout;
        List<List<Integer>> sudokuStart = this.start;

        DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels;

        int topId = space.getId();
        for(int i = 0; i < sudokuStart.size(); i+=1) {
            int prevViewId = -1;
            for (int j = 0; j < 9; j += 1) {
                Map<String,Integer> coords = new HashMap<>();
                coords.put("x",i);
                coords.put("y",j);

                if(j==0){
                    View prevView = CreateGridItem(dpWidth,String.valueOf(sudokuStart.get(i).get(j)),parentLayout,topId,-1,coords);
                    prevViewId = prevView.getId();
                    continue;
                }

                View view = CreateGridItem(dpWidth,String.valueOf(sudokuStart.get(i).get(j)),parentLayout,topId,prevViewId,coords);
                prevViewId = view.getId();
                if(j == 8){
                    topId = view.getId();
                }
            }
        }

        this.startDate = System.currentTimeMillis();
    }

    private static String AssetJSONFile (Context ctx) throws IOException {
        AssetManager manager = ctx.getAssets();
        InputStream file = manager.open("android_sudoku.json");
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();

        return new String(formArray);
    }

    private View CreateGridItem(float dpWidth, String input, ConstraintLayout parentLayout, int topId, int leftId, Map<String, Integer> coords){
        ConstraintSet set = new ConstraintSet();

        SudokuItem sudokuItem = new SudokuItem(ctx,(int) dpWidth,input,coords.get("x"),coords.get("y"),sudokuTextWatcher(coords));
        EditText editText = sudokuItem.getEditText();

        parentLayout.addView(editText);
        set.clone(parentLayout);

        int editTextId = editText.getId();

        set.connect(editTextId,ConstraintSet.TOP,topId,ConstraintSet.BOTTOM);

        if(leftId > -1){
            set.connect(editTextId,ConstraintSet.LEFT, leftId, ConstraintSet.RIGHT);
        }

        set.applyTo(parentLayout);

        return editText;
    }

    private TextWatcher sudokuTextWatcher(final Map<String,Integer> coords){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                handleInput(s, coords);
            }
        };
    }

    private void handleInput(Editable s, Map<String, Integer> coords) {
        List<Integer> currentList = current.get(coords.get("x"));
        String value = s.toString();
        if(value.equals("")){
            value = "0";
        }
        currentList.set(coords.get("y"),Integer.parseInt(value));
        current.set(coords.get("x"),currentList);

        if(current.equals(solution)){
            isFinished();
        }
    }

    private void isFinished(){
        long elaspsedTime = System.currentTimeMillis() - startDate;
        long minutes = (elaspsedTime / 1000) / 60;
        long seconds = (elaspsedTime / 1000) % 60;

        if(this.layout == null){
            return;
        }

//        this.layout.addView();
    }
}
