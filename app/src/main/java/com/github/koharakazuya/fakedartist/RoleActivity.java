package com.github.koharakazuya.fakedartist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Random;

public class RoleActivity extends ActionBarActivity implements View.OnClickListener {

    private ArrayList<Button> buttons = new ArrayList<Button>();
    private int playerNum;
    private boolean[] confirmedPlayers;
    private int fakedArtist;
    private String category;
    private String theme;
    private int[] colors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);

        // ルールを取得
        Intent intent = getIntent();
        playerNum = intent.getIntExtra("PlayerNum", 5);
        category = intent.getStringExtra("Category");
        theme = intent.getStringExtra("Theme");

        // エセ芸術家を決定
        fakedArtist = (new Random()).nextInt(playerNum);

        confirmedPlayers = new boolean[playerNum];

        // 役割の確認が終了した際に次へ移るボタンの動作を定義
        findViewById(R.id.nextFromRole).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DrawActivity.class);
                intent.putExtra("PlayerNum", playerNum);
                intent.putExtra("FakedArtist", fakedArtist);
                intent.putExtra("Category", category);
                intent.putExtra("Theme", theme);
                intent.putExtra("Colors", colors);

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 各プレイヤーの筆の色を決定
        colors = new int[playerNum];
        for (int i = 0; i < colors.length; ++i) {
            float h = 360.0f / playerNum * i;
            colors[i] = Color.HSVToColor(new float[] {h, 1.0f, 1.0f});
        }

        // ボタンを作成
        LinearLayout list = (LinearLayout) findViewById(R.id.playerList);
        list.removeAllViews();
        buttons.clear();
        for (int i = 0; i < playerNum; ++i) {
            Button btn = new Button(this);
            btn.setText(getString(R.string.player) + i);
            btn.setOnClickListener(this);
            buttons.add(btn);
            list.addView(btn);
        }
        validateButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.role, menu);
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

    @Override
    public void onClick(View view) {

        // プレイヤーごとの情報を作成
        String title = "エラー";
        String message = "エラー";
        int i = buttons.indexOf(view);
        title = getString(R.string.player) + i;
        if (i != fakedArtist) {
            message = "あなたは 芸術家 です。\n\n大テーマは " + category + " で、小テーマは " + theme + " です。\nエセ芸術家は小テーマを知らないので秘密にして下さい。";
        } else {
            message = "あなたは エセ芸術家 です。\n\n大テーマは " + category + " で、小テーマは不明です。\nあなただけが小テーマを知りません。";
        }

        // ダイアログを表示する
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("確認",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }
        );
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        confirmedPlayers[i] = true;

        validateButtons();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("PlayerNum", playerNum);
        outState.putBooleanArray("ConfirmedPlayers", confirmedPlayers);
        outState.putInt("FakedArtist", fakedArtist);
        outState.putString("Category", category);
        outState.putString("Theme", theme);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        playerNum = savedInstanceState.getInt("PlayerNum");
        confirmedPlayers = savedInstanceState.getBooleanArray("ConfirmedPlayers");
        fakedArtist = savedInstanceState.getInt("FakedArtist");
        category = savedInstanceState.getString("Category");
        theme = savedInstanceState.getString("Theme");
    }

    /**
     * 全員がチェックを終えたかどうか確認する
     */
    private void checkAllConfirms() {
        // 全プレイヤーが役割を確認したら画面遷移を許可
        boolean allDone = true;
        for (boolean check : confirmedPlayers) {
            if (!check)
                allDone = false;
        }
        if (allDone)
            findViewById(R.id.nextFromRole).setEnabled(true);
    }

    /**
     * 確認したかどうかによってボタンの有効無効を切り替える
     */
    private void validateButtons() {
        for (int i = 0; i < playerNum; ++i) {
            buttons.get(i).setEnabled(!confirmedPlayers[i]);
        }
        checkAllConfirms();
    }
}
