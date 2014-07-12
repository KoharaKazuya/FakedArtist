package com.github.koharakazuya.fakedartist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * お絵かき
 */
public class DrawActivity extends Activity {

    private DrawView drawView;
    private int playerNum;
    private int turn;
    private String category;
    private int[] colors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // フルスクリーンにする
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_draw);

        drawView = (DrawView) findViewById(R.id.drawView);

        // ルールの取得
        Intent intent = getIntent();
        playerNum = intent.getIntExtra("PlayerNum", 5);
        category = intent.getStringExtra("Category");
        colors = intent.getIntArrayExtra("Colors");

        turn = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();

        setPlayerColor();
        showNextPlayer();

        // カテゴリの表示
        ((TextView) findViewById(R.id.category)).setText("大テーマ： " + category);
    }

    @Override
    public void onBackPressed() {
        if (--turn < 0 || !drawView.rollback()) {
            super.onBackPressed();
        } else {
            showNextPlayer();
        }
    }

    public void nextTurn() {
        ++turn;
        if (turn < playerNum * 2) {
            showNextPlayer();
            setPlayerColor();
        } else {
            // ダイアログを表示する
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Finish");
            alertDialogBuilder.setMessage("誰がエセ芸術家か決定して下さい。");
            alertDialogBuilder.setPositiveButton("次へ",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            nextActivity();
                        }
                    }
            );
            alertDialogBuilder.setNegativeButton("戻る",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }
            );
            alertDialogBuilder.setCancelable(true);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    /**
     * 次のプレイヤーを表示する
     */
    private void showNextPlayer() {
        // ダイアログを表示する
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Next Player");
        alertDialogBuilder.setMessage("Player" + turn % playerNum + ", draw on your sensibility!");
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
    }

    private void nextActivity() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("FakedArtist", getIntent().getIntExtra("FakedArtist", -1));
        intent.putExtra("Theme", getIntent().getStringExtra("Theme"));
        startActivity(intent);
    }

    private void setPlayerColor() {
        drawView.setColor(colors[turn % playerNum]);
    }
}
