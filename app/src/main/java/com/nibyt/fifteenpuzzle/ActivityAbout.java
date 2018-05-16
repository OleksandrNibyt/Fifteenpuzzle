/*
Name: Oleksandr Nibyt
CIS 135 OL
File Name: ActivityAbout.java
File Description
Assignment Final
Date:  05/22/2016
*/

package com.nibyt.fifteenpuzzle;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import android.net.Uri;


public class ActivityAbout extends Activity {

    private Button ButtonFifteenPuzzleWiki;
    private static final String LOGTAG = "-> AboutFifteenPazzle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ButtonFifteenPuzzleWiki = (Button) findViewById(R.id.FifteenPuzzleWiki);

        // setting image resource from drawable
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.fifteen);

        Log.d(LOGTAG, " onCreate");


        //
        imageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Clicked Image",
                        Toast.LENGTH_SHORT).show();
                Intent onImageClick = new Intent(view.getContext(),ActivityFifteenPuzzle.class);
                startActivity(onImageClick);
            }

        });
        //
        ButtonFifteenPuzzleWiki.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(), "Clicked Button",
                //        Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse("https://en.wikipedia.org/wiki/15_puzzle");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
}
