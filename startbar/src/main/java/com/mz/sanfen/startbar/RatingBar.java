package com.mz.sanfen.startbar;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class RatingBar extends View {
    private int starDistance = 0; //星星间距
    private int starCount = 5;  //星星个数

    private int starWidth; //星星的宽度
    private int starHeight; //星星的高度

    private float starMark = 0.0F;   //评分星星
    private Bitmap starFillBitmap; //亮星星
    private Bitmap starEmptyBitmap; //暗星星
    private OnStarChangeListener onStarChangeListener;//监听星星变化

    public float rating; // 星星之间的差值
    public boolean isIndicator; // 是否可以点击

    private Paint emptyPaint;
    private Paint fillPaint;         //绘制评分星星

    public RatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化UI組件
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        setClickable(true);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
        starDistance = (int) typedArray.getDimension(R.styleable.RatingBar_starDistance, 0);
        starWidth = (int) typedArray.getDimension(R.styleable.RatingBar_starWidth, 20);
        starHeight = (int) typedArray.getDimension(R.styleable.RatingBar_starHeight, 20);
        starCount = typedArray.getInteger(R.styleable.RatingBar_starCount, 5);
        starEmptyBitmap = drawableToBitmap(typedArray.getDrawable(R.styleable.RatingBar_starEmpty));
        starFillBitmap = drawableToBitmap(typedArray.getDrawable(R.styleable.RatingBar_starFill));
        rating = typedArray.getFloat(R.styleable.RatingBar_rating, 0.5f);
        isIndicator = typedArray.getBoolean(R.styleable.RatingBar_isIndicator, true);
        typedArray.recycle();

        emptyPaint = new Paint();
        emptyPaint.setAntiAlias(true);
        emptyPaint.setShader(new BitmapShader(starEmptyBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));

        fillPaint = new Paint();
        fillPaint.setAntiAlias(true);
        fillPaint.setShader(new BitmapShader(starFillBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
    }


    /**
     * 设置显示的星星的分数
     *
     * @param mark
     */
    public void setStarMark(float mark) {
        float starMark = Math.round(mark / rating) * rating * 1.0f;

        if (starMark == this.starMark) {
            return;
        }
        this.starMark = starMark;
        if (this.onStarChangeListener != null) {
            this.onStarChangeListener.onStarChange(starMark);  //设置监听
        }
        invalidate();
    }

    /**
     * 获取星星数量
     *
     * @return starMark
     */
    public float getStarMark() {
        return starMark;
    }


    /**
     * 星星数量改变的回调
     */
    public interface OnStarChangeListener {
        void onStarChange(float mark);
    }

    /**
     * 设置监听
     *
     * @param onStarChangeListener
     */
    public void setOnStarChangeListener(OnStarChangeListener onStarChangeListener) {
        this.onStarChangeListener = onStarChangeListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(starWidth * starCount + starDistance * (starCount - 1), starHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (starFillBitmap == null || starEmptyBitmap == null) {
            return;
        }
        for (int i = 0; i < starCount; i++) {
            canvas.drawRect(0, 0, starWidth, starHeight, emptyPaint);
            canvas.translate(starDistance + starWidth, 0); //画笔移动到星星的末尾
        }

        //画笔重新移动到星星的开始位置
        canvas.translate(-1 * ((starWidth + starDistance) * starCount), 0);

        if (starMark > 1) {
            canvas.drawRect(0, 0, starWidth, starHeight, fillPaint);
            if (starMark - (int) (starMark) == 0) {
                for (int i = 1; i < starMark; i++) {
                    canvas.translate(starDistance + starWidth, 0);
                    canvas.drawRect(0, 0, starWidth, starHeight, fillPaint);
                }
            } else {
                for (int i = 1; i < starMark - 1; i++) {
                    canvas.translate(starDistance + starWidth, 0);
                    canvas.drawRect(0, 0, starWidth, starHeight, fillPaint);
                }
                canvas.translate(starDistance + starWidth, 0);
                canvas.drawRect(0, 0, starWidth * (Math.round((starMark - (int) (starMark)) * 10) * 1.0f / 10), starHeight, fillPaint);
            }
        } else {
            canvas.drawRect(0, 0, starWidth * starMark, starHeight, fillPaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        if (x < 0) x = 0;
        if (x > getMeasuredWidth()) x = getMeasuredWidth();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if (isIndicator)
                    setStarMark(x * 1.0f / (getMeasuredWidth() * 1.0f / starCount));
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (isIndicator)
                    setStarMark(x * 1.0f / (getMeasuredWidth() * 1.0f / starCount));
                break;
            }
            case MotionEvent.ACTION_UP: {
                break;
            }
        }
        invalidate();
        return super.onTouchEvent(event);
    }

    /**
     * drawable转bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) return null;
        Bitmap bitmap = Bitmap.createBitmap(starWidth, starHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, starWidth, starHeight);
        drawable.draw(canvas);
        return bitmap;
    }
}