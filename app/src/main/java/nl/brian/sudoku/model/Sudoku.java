package nl.brian.sudoku.model;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;

import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.Space;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nl.brian.sudoku.R;

public class Sudoku {
    private List<List<Integer>> start;
    private List<List<Integer>> current;
    private List<List<Integer>> solution;
    private Context ctx;
    private JSONArray sudokus = null;

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
        List<List<Integer>> sudokuStart = this.start;

        DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels;

        int topId = space.getId();
        for(int i = 0; i < sudokuStart.size(); i+=1) {
            int prevViewId = -1;
            for (int j = 0; j < 9; j += 1) {
                if(j==0){
                    View prevView = CreateGridItem(dpWidth,String.valueOf(sudokuStart.get(i).get(j)),parentLayout,topId,-1,i,j);
                    prevViewId = prevView.getId();
                    continue;
                }

                View view = CreateGridItem(dpWidth,String.valueOf(sudokuStart.get(i).get(j)),parentLayout,topId,prevViewId,i,j);
                prevViewId = view.getId();
                if(j == 8){
                    topId = view.getId();
                }
            }
        }
    }

    private static String AssetJSONFile (Context ctx) throws IOException {
        AssetManager manager = ctx.getAssets();
        InputStream file = manager.open("android_sudoku.json");
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();

        return new String(formArray);
    }

    private View CreateGridItem(float dpWidth, String input, ConstraintLayout parentLayout, int topId, int leftId, int i, int j){
        ConstraintSet set = new ConstraintSet();

        EditText editText = getViewItem(ctx, (int) dpWidth, input, i,j);

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

    private EditText getViewItem(Context ctx, int dpWidth, String input, int i, int j) {
        final EditText editText = new EditText(ctx);
        if(!input.equals("0")){
            editText.setText(input);
            editText.setEnabled(false);
            editText.setInputType(InputType.TYPE_NULL);
            editText.setTextColor(Color.BLACK);
            editText.setTypeface(null, Typeface.BOLD);
        }

        editText.setWidth((dpWidth / 9));
        editText.setId(View.generateViewId());
        editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);

        setBackground(ctx, i, j, editText);

        editText.setHint("0");
        editText.setKeyListener(DigitsKeyListener.getInstance("123456789"));
        editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) });


        editText.addTextChangedListener(sudokuTextWatcher(i,j));
        return editText;
    }

    private TextWatcher sudokuTextWatcher(final int i, final int j){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                List<Integer> currentList = current.get(i);
                String value = s.toString();
                if(value.equals("")){
                    value = "0";
                }
                currentList.set(j,Integer.parseInt(value));
                current.set(i,currentList);

                if(current.equals(solution)){
                    System.out.println("Victory");
                }
            }
        };
    }

    private void setBackground(Context ctx, int i, int j, EditText editText) {
        if(i == 8 && j == 0){
            editText.setBackground(ContextCompat.getDrawable(ctx, R.drawable.edittext_border_bottom_left));
        } else if (i == 0 && j == 0){
            editText.setBackground(ContextCompat.getDrawable(ctx, R.drawable.edittext_border_top_left));
        } else if (i == 0 && j == 8){
            editText.setBackground(ContextCompat.getDrawable(ctx, R.drawable.edittext_border_top_right));
        } else if (i == 8 && j == 8){
            editText.setBackground(ContextCompat.getDrawable(ctx, R.drawable.edittext_border_bottom_right));
        } else if ((i + 1) % 3 == 0 && (j + 1) % 3 == 0){
            editText.setBackground(ContextCompat.getDrawable(ctx, R.drawable.edittext_border_bottom_right));
        } else if ((j + 1) % 3 == 0 && i == 0){
            editText.setBackground(ContextCompat.getDrawable(ctx, R.drawable.edittext_border_top_right));
        } else if ((j + 1) % 3 == 0 && i == 8){
            editText.setBackground(ContextCompat.getDrawable(ctx, R.drawable.edittext_border_bottom_right));
        } else if ((j + 1) % 3 == 0){
            editText.setBackground(ContextCompat.getDrawable(ctx, R.drawable.edittext_border_right));
        } else if ((i + 1) % 3 == 0 && j == 0){
            editText.setBackground(ContextCompat.getDrawable(ctx, R.drawable.edittext_border_bottom_left));
        } else if ((i + 1) % 3 == 0 && j == 8){
            editText.setBackground(ContextCompat.getDrawable(ctx, R.drawable.edittext_border_bottom_right));
        } else if ((i + 1) % 3 == 0){
            editText.setBackground(ContextCompat.getDrawable(ctx, R.drawable.edittext_border_bottom));
        } else if (i == 0){
            editText.setBackground(ContextCompat.getDrawable(ctx, R.drawable.edittext_border_top));
        } else if (j == 0){
            editText.setBackground(ContextCompat.getDrawable(ctx, R.drawable.edittext_border_left));
        } else if (j == 8){
            editText.setBackground(ContextCompat.getDrawable(ctx, R.drawable.edittext_border_right));
        } else if (i == 8){
            editText.setBackground(ContextCompat.getDrawable(ctx, R.drawable.edittext_border_bottom));
        } else {
            editText.setBackground(null);
        }
    }
}
