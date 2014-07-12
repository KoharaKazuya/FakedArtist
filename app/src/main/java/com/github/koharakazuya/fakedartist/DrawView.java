package com.github.koharakazuya.fakedartist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;


/**
 * 絵を書くためのキャンバス
 */
public class DrawView extends View implements View.OnTouchListener {

    private Paint paint;

    private ArrayList<Path> pathList = new ArrayList<Path>();
    private ArrayList<Integer> colorList = new ArrayList<Integer>();
    private Path path;
    private int playerColor;

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOnTouchListener(this);

        // 線の設定
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAlpha(127);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(8);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < pathList.size(); ++i) {
            Path path = pathList.get(i);
            int color = colorList.get(i);
            paint.setColor(color);
            paint.setAlpha(127);
            canvas.drawPath(path, paint);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // タッチしたとき
                path = new Path();
                path.moveTo(motionEvent.getX(), motionEvent.getY());
                pathList.add(path);
                colorList.add(playerColor);
                Log.d("DrawView", "Touch!");
                break;
            case MotionEvent.ACTION_MOVE:
                // タッチしたまま動かしたとき
                path.lineTo(motionEvent.getX(), motionEvent.getY());
                break;
            case MotionEvent.ACTION_UP:
                // 指を離したとき
                path.lineTo(motionEvent.getX(), motionEvent.getY());
                // ターンを進める
                ((DrawActivity) view.getContext()).nextTurn();
                break;
            default:
        }
        invalidate();
        return true;
    }

    /**
     * 書いた絵を一筆戻す
     *
     * @return 戻せたかどうか
     */
    public boolean rollback() {
        if (pathList.size() > 0) {
            pathList.remove(pathList.size() - 1);
            invalidate();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 筆の色を変更
     */
    public void setColor(int color) {
        this.playerColor = color;
    }
}
