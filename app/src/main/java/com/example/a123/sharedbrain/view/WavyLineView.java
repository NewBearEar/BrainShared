package com.example.a123.sharedbrain.view;

import android.annotation.TargetApi;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.content.Context;
import android.os.Build;

import com.example.a123.sharedbrain.R;


/**
 * 自定义波浪线
 */
public class WavyLineView extends View{

    public static final int   DEF_DRAW_SIZE = 50;
    public static final int   DEF_COLOR = Color.BLACK;
    public static final int   DEF_X_GAP = 1;
    public static final int   DEF_AMPLITUDE = 20;
    public static final float DEF_STROKE_WIDTH = 2.0f;
    public static final float DEF_PERIOD = (float) (2 * Math.PI / 180);//Mathi.pi圆周率

    private Path mPath;
    private Paint mPaint;
    private int mAmplitude;
    private int mColor = DEF_COLOR;
    private float mPeriod;
    private float mStrokeWidth = DEF_STROKE_WIDTH;


    public WavyLineView(Context context) {
        this(context, null);
    }

    public WavyLineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WavyLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WavyLineView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }
    /**
     *初始化view控件
     */
    private void init(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.WavyLineView);
        mColor = ta.getColor(R.styleable.WavyLineView_strokeColor, DEF_COLOR);
        mPeriod = ta.getFloat(R.styleable.WavyLineView_period, DEF_PERIOD);
        mAmplitude = ta.getDimensionPixelOffset(R.styleable.WavyLineView_amplitude, DEF_AMPLITUDE);
        mStrokeWidth = ta.getDimensionPixelOffset(R.styleable.WavyLineView_strokeWidth, dp2px(getContext(), DEF_STROKE_WIDTH));
        ta.recycle();

        mPath = new Path();//创建路径
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//创建画笔
        mPaint.setStyle(Paint.Style.STROKE);//设置画笔风格，空心或者实心
        mPaint.setStrokeCap(Paint.Cap.ROUND);//设置画笔的类型
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(dp2px(getContext(), mStrokeWidth));
        /** setAntiAlias: 设置画笔的锯齿效果。
         setColor: 设置画笔颜色
         setARGB:  设置画笔的a,r,p,g值。
         setAlpha:  设置Alpha值
         setTextSize: 设置字体尺寸。
         setStyle:  设置画笔风格，空心或者实心。
         setStrokeWidth: 设置空心的边框宽度。
         getColor:  得到画笔的颜色
         getAlpha:  得到画笔的Alpha值
         */

    }
    /**
     *测量控件
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureSize(widthMeasureSpec), measureSize(heightMeasureSpec));
    }
    /**
     *绘制控件
     */
    @Override
    protected void onDraw(Canvas canvas) {
        calculatePath();
        canvas.drawPath(mPath, mPaint);
    }

    private int measureSize(int measureSpec) {
        int defSize = dp2px(getContext(), DEF_DRAW_SIZE);
        int specSize = MeasureSpec.getSize(measureSpec);
        int specMode = MeasureSpec.getMode(measureSpec);

        int result = 0;
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                result = Math.min(defSize, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }
    //绘制从左上角开始根据上下左右绘制
    private void calculatePath() {
        mPath.reset();

        float y;
        float left = getPaddingLeft();
        float right = getMeasuredWidth() - getPaddingRight();
        float top = getPaddingTop();
        float bottom = getMeasuredHeight() - getPaddingBottom();
        mPath.moveTo(left, (top + bottom) / 2);
        //通过正弦函数绘制x,y路径
        for (float x = 0; x <= right; x += DEF_X_GAP) {
            y = (float) (mAmplitude * Math.sin(mPeriod * x) + (top + bottom) / 2);
            mPath.lineTo(x + left, y);
        }
    }
    /**
     *设置波浪上下波动
     */
    public void setAmplitude(int amplitude) {
        this.mAmplitude = amplitude;
        invalidate();
    }
    /**
     *设置波浪频率
     */
    public void setPeriod(float T) {
        this.mPeriod = T;
        invalidate();
    }

    public void setColor(int color) {
        this.mColor = color;
        mPaint.setColor(mColor);
        invalidate();
    }

    public void setStrokeWidth(float strokeWidth) {
        this.mStrokeWidth = strokeWidth;
        mPaint.setStrokeWidth(mStrokeWidth);
        invalidate();
    }
    /**
     *dp->px工具方法
     */
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}