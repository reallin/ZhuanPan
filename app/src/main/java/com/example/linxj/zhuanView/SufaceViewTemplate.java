package com.example.linxj.zhuanView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.example.linxj.surfacezhuanpan.R;

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
    private Paint mTextPaint;
    private int mCenter;
    private int mPadding;
    private int raidus;
    private int mCount = 16;
    private boolean isStart = false;
    private boolean isShouldEnd = false;
    private String info;
    private Context mContext;

    /**
     * 滚动的速度
     */
    private double mSpeed;
    private volatile float mStartAngle = 0;

    /**
     * 绘制盘块的范围
     */
    private RectF mRange = new RectF();
    /**
     * 每个盘块的颜色
     */
    private int[] mColors = new int[] { 0xFFFFC300, 0xFFF17E01};
    /**
     * 抽奖的文字
     */
    private String[] mStrs = new String[] { "扫地", "拖地", "倒垃圾", "抹桌子1",
            "抹桌子2", "抹桌子3","抹桌子4","倒水","换水","整理冰箱","打水","套袋子","boss房间","抹桌子5","抹桌子6","抹桌子7" };

    /**
     * 文字的大小,把sp转化成像素
     */
    private float  mTextSize = (float)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,15,getResources().getDisplayMetrics());

    private Bitmap bg = BitmapFactory.decodeResource(getResources(), R.drawable.bg2);
    public SufaceViewTemplate(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public SufaceViewTemplate(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public SufaceViewTemplate(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView(){
        mHolder = getHolder();
        mHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);



    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsDrawing = true;
        mPath = new Path();
        mPaint = new Paint();
        mTextPaint = new Paint();
        //mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(Color.WHITE);

        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        //整个圆
        mRange = new RectF(getPaddingLeft(),getPaddingLeft(),raidus+getPaddingLeft(),raidus+getPaddingLeft());

        new Thread(this).start();
    }
/**
*设置转盘画在一个矩形内
 */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int min = Math.min(getMeasuredWidth(),getMeasuredHeight());
        int width = min-getPaddingLeft()-getPaddingRight();
        mCenter = min/2;
        raidus = width;
        // padding值
        mPadding = getPaddingLeft();
        setMeasuredDimension(min, min);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;
    }

    @Override
    public void run() {
        while(mIsDrawing){
            long start = System.currentTimeMillis();
            draw();
            long end = System.currentTimeMillis();
            if(end - start < 50){
                try{
                    Thread.sleep(50-(end-start));
                }catch (Exception e){
                    e.printStackTrace();
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
               // mPath.moveTo(x,y);break;
            case MotionEvent.ACTION_MOVE:

               // mPath.lineTo(x,y);break;
            case MotionEvent.ACTION_UP:break;
        }
        return true;
    }

    private void draw(){
        try{
            mCanvas = mHolder.lockCanvas(); //获得Canvas对象
            if(mCanvas != null){
                mCanvas.drawColor(0xffffffff);
                mCanvas.drawBitmap(bg, null, new Rect(mPadding / 2,
                        mPadding / 2, getMeasuredWidth() - mPadding / 2,
                        getMeasuredWidth() - mPadding / 2), null);

                float temp = mStartAngle;
                float tempItem = (float)360/mCount;
                for(int i = 0;i < mCount;i++){
                    mPaint.setColor(mColors[i % 2]);
                    mCanvas.drawArc(mRange, temp, tempItem, true, mPaint);
                    //画字
                    Path textPath = new Path();
                    textPath.addArc(mRange,temp,tempItem);
                    float textWidth = mTextPaint.measureText(mStrs[i]);
                    float x = (float)(raidus*Math.PI/mCount/2 - textWidth/2);
                    float y = (float)raidus/2/6;
                    mCanvas.drawTextOnPath(mStrs[i],textPath,x,y,mTextPaint);
                    temp += tempItem;
                }
                //计算并让转盘停下来
                // 点击停止时，设置mSpeed为递减，为0值转盘停止
                mStartAngle += mSpeed;
                if (isShouldEnd)
                {
                    mSpeed -= 1;
                }
                if (mSpeed <= 0)
                {
                    mSpeed = 0;
                    isShouldEnd = false;
                }
               info = culArea(mStartAngle);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
        if(mCanvas != null){
                mHolder.unlockCanvasAndPost(mCanvas);//对画布内容进行提交
            }
        }
    }
    public String culArea(float angle){
        float roate = angle/360;

        for(int i = 0;i < mCount;i++){
            float from = 360/mCount*i;
            float to = from + 360/mCount;
            if(roate>=from && roate < to){
                return mStrs[i];
            }
        }
        return "没找到";
    }
    public void Start(){
        isStart = true;
        float from = (float)Math.random()*50;

        // 停下来时旋转的距离
         mSpeed = from;


       // mSpeed = (float) (v1 + Math.random() * (v2 - v1));
       // isShouldEnd = false;
    }

    public void luckyEnd()
    {
        mStartAngle = 0;
        isShouldEnd = true;
        isStart = false;
    }

    public boolean isStart()
    {
        return mSpeed != 0;
    }

    public boolean isShouldEnd()
    {
        return isShouldEnd;
    }
}
