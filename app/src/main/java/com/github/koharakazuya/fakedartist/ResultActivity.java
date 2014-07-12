package com.github.koharakazuya.fakedartist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ResultActivity extends ActionBarActivity {

    private int fakedArtist;
    private String theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        fakedArtist = intent.getIntExtra("FakedArtist", -1);
        theme = intent.getStringExtra("Theme");

        final TextView fakedArtistText = (TextView) findViewById(R.id.fakedArtist);
        final TextView themeText = (TextView) findViewById(R.id.theme);

        findViewById(R.id.buttonFakedArtist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fakedArtistText.setText("エセ芸術家は " + getString(R.string.player) + fakedArtist + " です。");
            }
        });
        findViewById(R.id.buttonTheme).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themeText.setText("小テーマは " + theme + " です。");
            }
        });

        findViewById(R.id.button_game_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), TitleActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
