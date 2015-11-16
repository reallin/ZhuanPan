package com.example.linxj.surfacezhuanpan;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by linxj on 2015/11/14.
 */
public class SufaceViewTemplate extends SurfaceView implements SurfaceHolder.Callback,Runnable{
    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    private boolean mIsDrawing;
    private int x;
    private int y;
    private Path mPath;
    private Paint mPaint;
    public SufaceViewTemplate(Context context) {
        super(context);
        initView();
    }

    public SufaceViewTemplate(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SufaceViewTemplate(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        mHolder = getHolder();
        mHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
        mPath = new Path();
        mPaint = new Paint();
        //mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);


    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsDrawing = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void run() {
        while(mIsDrawing){
            long start = System.currentTimeMillis();
            draw();
            long end = System.currentTimeMillis();
            if(end - start < 100){
                try{
                    Thread.sleep(100-(end-start));
                }catch (Exception e){

                }
            }
          /*  x+=1;
            y = (int) (100*Math.sin(x*2*Math.PI/180)+400);
            mPath.lineTo(x,y);*/
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(x,y);break;
            case MotionEvent.ACTION_MOVE:

                mPath.lineTo(x,y);break;
            case MotionEvent.ACTION_UP:break;
        }
        return true;
    }

    private void draw(){
        try{
            mCanvas = mHolder.lockCanvas(); //获得Canvas对象
            mCanvas.drawColor(Color.WHITE); //刷新画布
            mCanvas.drawPath(mPath,mPaint);
        }catch (Exception e){

        }finally {
        if(mCanvas != null){
                mHolder.unlockCanvasAndPost(mCanvas);//对画布内容进行提交
            }
        }
    }
}
