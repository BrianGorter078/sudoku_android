package nl.brian.sudoku.model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import java.util.HashMap;

import java.util.Map;

import nl.brian.sudoku.R;

public class SudokuItem {
    private Map<String,Integer> coords;
    private EditText editText;

    public SudokuItem(Context ctx, int dpWidth, String input, int i, int j, TextWatcher sudokuTextWatcher) {
        final EditText editText = getEditText(ctx, dpWidth, input, i, j, sudokuTextWatcher);

        Map<String,Integer> coords = new HashMap<>();
        coords.put("x",i);
        coords.put("y",j);

        this.coords = coords;
        this.editText = editText;
    }

    private EditText getEditText(Context ctx, int dpWidth, String input, int i, int j, TextWatcher sudokuTextWatcher) {
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


        editText.addTextChangedListener(sudokuTextWatcher);
        return editText;
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

    public Map<String, Integer> getCoords() {
        return coords;
    }

    public EditText getEditText(){
        return editText;
    }
}
