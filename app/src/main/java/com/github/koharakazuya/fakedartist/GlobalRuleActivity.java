package com.github.koharakazuya.fakedartist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Random;

public class GlobalRuleActivity extends ActionBarActivity {

    private int playerNum = 5;
    // 大テーマ
    private String category;
    // 小テーマ
    private String theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_rule);

        selectTheme();

        // ルールが決定したときに、次へ移るボタンの動作を定義
        findViewById(R.id.nextFromGlobalRule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RoleActivity.class);
                intent.putExtra("PlayerNum", playerNum);
                intent.putExtra("Category", category);
                intent.putExtra("Theme", theme);
                startActivity(intent);
            }
        });

        // スライダを動かした時にプレイヤー数を変更
        ((SeekBar) findViewById(R.id.seekBar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                updatePlayerNum(seekBar);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                updatePlayerNum(seekBar);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            private void updatePlayerNum(SeekBar seekBar) {
                // プレイヤー数を変更
                playerNum = 5 + seekBar.getProgress();

                // 表示を変更
                TextView text = (TextView) findViewById(R.id.playerNum);
                text.setText(getString(R.string.player) + ": " + playerNum);

                // 画面遷移を許可
                findViewById(R.id.nextFromGlobalRule).setEnabled(true);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.global_rule, menu);
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

    private void selectTheme() {
        String[] category_and_themes = getResources().getStringArray(R.array.themes);
        int r = (new Random()).nextInt(category_and_themes.length);
        String[] category_and_theme = category_and_themes[r].split("/");
        category = category_and_theme[0];
        theme = category_and_theme[1];
    }
}
