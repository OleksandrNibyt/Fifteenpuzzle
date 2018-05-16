/*
Name: Oleksandr Nibyt
CIS 135 OL
File Name: ActivityFifteenPuzzle.java
File Description
Assignment Final
Date:  05/22/2016
*/

package com.nibyt.fifteenpuzzle;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;
import android.graphics.Typeface;
import java.util.Arrays;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class ActivityFifteenPuzzle extends Activity {


    // define puzzle
    private int[][] puzzle = new int[4][4];
    // define SharedPreferences object
    private SharedPreferences savedValues;
    private static final String LOGTAG = "-> FifteenPuzzle";
    private int clickCount;
    private TextView clickCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifteen_puzzle);

        Log.d(LOGTAG, " onCreate");
        clickCount = 0;
        clickCountTextView = (TextView) findViewById(R.id.ClickCounterTextView);

        randomizeNumbers();

        //get SharedPref. object
        savedValues = getSharedPreferences("SavedValues",MODE_PRIVATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fifteen_puzzle, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_about:
                //Intent intent = new Intent(this, ActivityForItemOne.class);
                //this.startActivity(intent);
                Toast.makeText(this,"MenuAbout_selected",Toast.LENGTH_LONG).show();

                startActivity((new Intent(getApplicationContext(),ActivityAbout.class)));
                break;
            case R.id.action_settings:
                // another startActivity, this is for item with id "menu_item2"
                Toast.makeText(this,"Futures Settings Under Development",Toast.LENGTH_LONG).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    @Override
    protected void onPause(){
        // save the instance variable
        // String LogString = "";
        Editor editor = savedValues.edit();

        for (int i = 0; i<4 ; i++) {
            for (int j = 0; j < 4; j++) {
                editor.putInt(Integer.toString(i) + Integer.toString(j),puzzle[i][j] );
                //LogString = LogString + "," + Integer.toString(puzzle[i][j]);
            }
        }
        editor.putInt("ClickCounter",clickCount);

        //Log.d(LOGTAG, " onPause " + LogString);
        editor.commit();
        super.onPause();

    }

    @Override
    protected void onResume() {
        //
        //String LogString = "";
        super.onResume();

        for (int i = 0; i<4 ; i++) {
            for (int j = 0; j < 4; j++) {

                puzzle[i][j] = savedValues.getInt(Integer.toString(i) + Integer.toString(j),0);
                //LogString = LogString + "," + Integer.toString(puzzle[i][j]);
            }
        }
        clickCount = savedValues.getInt("ClickCounter",0);

        updateButtons();
        //Log.d(LOGTAG, " onResume " + LogString);
    }

    /*
     * @param row
     * @param col
     */
    public char canMove(int row, int col) {
        Log.d(""+this.getClass(), "Value is: " + puzzle[row][col]);
        printArray(puzzle);
        if (puzzle[row][col] == 0) {
            Log.d(""+this.getClass(), "You cannot move.");
            return 'N';
        } else if (col + 1 < puzzle[0].length && puzzle[row][col + 1] == 0) {
            Log.d(""+this.getClass(), "You can move to the right.");
            return 'R';
        } else if (col - 1 >= 0 && puzzle[row][col - 1] == 0) {
            Log.d(""+this.getClass(), "You can move to the left.");
            return 'L';
        } else if (row + 1 < puzzle.length && puzzle[row + 1][col] == 0) {
            Log.d(""+this.getClass(), "You can move down.");
            return 'D';
        } else if (row - 1 >= 0 && puzzle[row - 1][col] == 0) {
            Log.d(""+this.getClass(), "You can move up.");
            return 'U';
        } else {
            Log.d(""+this.getClass(), "You cannot move. Last else.");
            return 'N';
        }
    }

    /*
     * @param fromRow
     * @param fromCol
     * @param direction
     */
    public void moveNumberFrom(int fromRow, int fromCol, char direction) {
        printArray(puzzle);

        //Temp record of number from the current row and col
        int num = puzzle[fromRow][fromCol];

        switch(direction) {
            case 'L': puzzle[fromRow][fromCol] = puzzle[fromRow][fromCol - 1];
                puzzle[fromRow][fromCol - 1] = num;
                break;
            case 'R': puzzle[fromRow][fromCol] = puzzle[fromRow][fromCol + 1];
                puzzle[fromRow][fromCol + 1] = num;
                break;
            case 'U': puzzle[fromRow][fromCol] = puzzle[fromRow - 1][fromCol];
                puzzle[fromRow - 1][fromCol] = num;
                break;
            case 'D': puzzle[fromRow][fromCol] = puzzle[fromRow + 1][fromCol];
                puzzle[fromRow + 1][fromCol] = num;
                break;
            default: //do nothing
                break;
        }

        printArray(puzzle);
    }


    public void printArray(int[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                System.out.print(a[i][j] + " ");
            }
            System.out.println("");
        }
    }

    /*
     * Respond to a user touching a button
     */
    public void buttonClicked(View view) {
        int buttonId = view.getId();
        Button btn = (Button)view.findViewById(buttonId);
        int[][] arr1 = {{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,14,15,0}};

        if (buttonId == R.id.randomizeButton) {
            randomizeNumbers();
            clickCount = 0;
        } else {
            Log.d(""+this.getClass(), "Button tag is: " + btn.getTag());
            String[] rowColumn = btn.getTag().toString().split(",");
            int row = Integer.parseInt(rowColumn[0]);
            int col = Integer.parseInt(rowColumn[1]);
            char direction = canMove(row, col);
            if (direction != 'N') {
                moveNumberFrom(row, col, direction);
                updateButtons();
            }
            clickCount++;
        }
        clickCountTextView.setText(Integer.toString(clickCount));

        // 7 clicks test
        /*
        if (clickCount == 7){
            puzzle[0][0]=1;puzzle[0][1]=2;puzzle[0][2]=3;puzzle[0][3]=4;
            puzzle[1][0]=5;puzzle[1][1]=6;puzzle[1][2]=7;puzzle[1][3]=8;
            puzzle[2][0]=9;puzzle[2][1]=10;puzzle[2][2]=11;puzzle[2][3]=12;
            puzzle[3][0]=13;puzzle[3][1]=14;puzzle[3][2]=15;puzzle[3][3]=0;
        }
        */
        if (Arrays.deepEquals(arr1, puzzle)) {
            Log.d(LOGTAG, " clickCount " + "Solved");
            Toast.makeText(this, "Fifteen Puzzle is solved", Toast.LENGTH_LONG).show();

            AlertDialog alertDialog = new AlertDialog.Builder(this).create();

            // Setting Title
            alertDialog.setTitle("Puzzle solved in " + Integer.toString(clickCount) + " clicks");

            // Setting Message
            alertDialog.setMessage("Click Ok and Randomize for new game");

            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to execute after dialog closed
                    Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }
        else
            Log.d(LOGTAG, " clickCount " + clickCount);
    }

    /*
     * Randomly fills the view's buttons with numbers from 1-15 and one empty slot
     */
    public void randomizeNumbers() {

        List<Integer> availableNumbers = new LinkedList<Integer>();
        for (int i = 0; i <= 15; i++) {
            availableNumbers.add(i);
        }

        Random r = new Random();
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[0].length; j++) {
                //Choose a random number within the range of availableNumber's indices
                int num = r.nextInt(availableNumbers.size());
                //Set the puzzle array's value
                puzzle[i][j] = availableNumbers.get(num);
                //Remove value from availableNumbers so the same number cannot be chosen twice
                availableNumbers.remove(num);
            }
        }
        //Update button text
        updateButtons();
        Log.d(LOGTAG, "Finished randomizing array");
    }

    /*
     * Updates buttons' numbers with respective puzzle array button
     */
    public void updateButtons() {
        int count = 0x0;
        Typeface font_style = Typeface.createFromAsset(getAssets(), "HNHeavy.ttf");


        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[0].length; j++) {
                //Set button in view
                Button btn = (Button)findViewById(R.id.button0 + count);
                btn.setTypeface(font_style);

                if (puzzle[i][j] == 0) {
                    btn.setText(" ");
                } else {
                    btn.setText(""+puzzle[i][j]);
                }
                //Increment counter so the next button is chosen
                count += 0x1;
            }
        }
        clickCountTextView.setText(Integer.toString(clickCount));
    }
}
