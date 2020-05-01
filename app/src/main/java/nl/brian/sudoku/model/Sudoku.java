package nl.brian.sudoku.model;

import android.content.Context;
import android.content.res.AssetManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Sudoku {
    private List<List<Integer>> start;
    private List<List<Integer>> solution;

    public Sudoku() {
    }

    public Sudoku getSudoku(Context ctx){
        try {
            String SudokuJson = AssetJSONFile("android_sudoku.json", ctx);
            JSONArray arrayOfSudokus = new JSONArray(SudokuJson);

            Random rnd =  new Random();
            int rndInt = rnd.nextInt(arrayOfSudokus.length());

            JSONObject sudoku = (JSONObject) arrayOfSudokus.get(rndInt);

            char[] startChars = sudoku.getString("quizzes").toCharArray();
            char[] solutionChars = sudoku.getString("solutions").toCharArray();

            List<List<Integer>> start = new ArrayList<>();
            List<List<Integer>> solution = new ArrayList<>();

            for (int i = 0; i < 9; i+=1){
                List<Integer> startRow = new ArrayList<>();
                List<Integer> solutionRow = new ArrayList<>();
                for(int j = 0; j < 9; j+=1) {
                    startRow.add(Integer.valueOf(String.valueOf(startChars[i*9+j])));
                    solutionRow.add(Integer.valueOf(String.valueOf(solutionChars[i*9+j])));
                }
                start.add(startRow);
                solution.add(solutionRow);
            }

            this.start = start;
            this.solution = solution;

            return this;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<List<Integer>> getStart() {
        return start;
    }

    public List<List<Integer>> getSolution() {
        return solution;
    }

    private static String AssetJSONFile (String filename, Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(filename);
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();

        return new String(formArray);
    }
}
