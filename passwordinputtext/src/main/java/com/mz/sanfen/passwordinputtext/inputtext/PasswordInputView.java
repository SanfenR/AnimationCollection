package com.mz.sanfen.passwordinputtext.inputtext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;


import com.mz.sanfen.passwordinputtext.R;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

/**
 * @author mw
 * @desc mw
 * @time 2016/12/16 0016 10:27
 */
public class PasswordInputView extends AppCompatEditText {

    private int backgroundColor = 0xFFCCCCCC;
    private Drawable backgroundDrawable = null;

    private int passwordWidth = 8;
    private int passwordHeight = 8;
    private int passwordDistance = 2;

    private Drawable passwordIconDrawable = null;
    private int passwordIconWidth = -1;
    private int passwordIconHeight = -1;


    private Paint passwordPaint = new Paint(ANTI_ALIAS_FLAG);
    private Paint backgroundPaint = new Paint(ANTI_ALIAS_FLAG);
    private Paint passwordIconPaint = new Paint(ANTI_ALIAS_FLAG);


    private int textLength;

    private int passwordRadius = 0; //字符方块的边角圆弧

    private int maxLength;


    public PasswordInputView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PasswordInputView, 0, 0);
        passwordWidth = (int) a.getDimension(R.styleable.PasswordInputView_passwordWidth, passwordWidth);
        passwordHeight = (int) a.getDimension(R.styleable.PasswordInputView_passwordHeight, passwordHeight);
        passwordDistance = (int) a.getDimension(R.styleable.PasswordInputView_passwordDistance, passwordDistance);
        passwordRadius = (int) a.getDimension(R.styleable.PasswordInputView_passwordRadius, passwordRadius);

        passwordIconDrawable = a.getDrawable(R.styleable.PasswordInputView_passwordIcon);
        passwordIconWidth = (int) a.getDimension(R.styleable.PasswordInputView_passwordIconWidth, -1);
        passwordIconHeight = (int) a.getDimension(R.styleable.PasswordInputView_passwordIconHeight, -1);

        if (3 == a.getType(R.styleable.PasswordInputView_backgroundColor)) {
            backgroundDrawable = a.getDrawable(R.styleable.PasswordInputView_backgroundColor);
        } else {
            backgroundColor = a.getColor(R.styleable.PasswordInputView_backgroundColor, backgroundColor);
        }
        a.recycle();

        if (backgroundDrawable == null) {
            backgroundPaint.setColor(backgroundColor);
        } else {
            backgroundPaint.setShader(new BitmapShader(drawableToBitmap(backgroundDrawable,
                    passwordWidth, passwordHeight),
                    BitmapShader.TileMode.CLAMP,
                    BitmapShader.TileMode.CLAMP));
        }

        if (passwordIconDrawable != null) {
            if (passwordIconWidth == -1 || passwordIconWidth > passwordWidth) {
                passwordIconWidth = passwordWidth / 2;
            }
            if (passwordIconHeight == -1 || passwordIconHeight > passwordHeight) {
                passwordIconHeight = passwordHeight / 2;
            }

            passwordIconPaint.setShader(new BitmapShader(drawableToBitmap(passwordIconDrawable,
                    passwordIconWidth, passwordIconHeight),
                    BitmapShader.TileMode.CLAMP,
                    BitmapShader.TileMode.CLAMP));
        }

        passwordPaint.setTextSize(getTextSize());
        passwordPaint.setColor(getCurrentTextColor());
        passwordPaint.setTextAlign(Paint.Align.CENTER);

        maxLength = getMaxLength();
        setBackground(null);
        setPadding(0, 0, 0, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景
        for (int i = 0; i < maxLength; i++) {
            canvas.drawRoundRect(0, 0, (float) passwordWidth, (float) passwordHeight, passwordRadius, passwordRadius, backgroundPaint);
            canvas.translate(passwordWidth + passwordDistance, 0); //画笔移动到下个位置
        }
        //画笔重新移动到星星的开始位置
        canvas.translate(-1 * ((passwordWidth + passwordDistance) * maxLength), 0);


        //icon
        for (int i = 0; i < textLength; i++) {
            String text = String.valueOf(getText().charAt(i));
            if (isPasswordInputType(getInputType())) {
                if (passwordIconDrawable != null) {
                    if (i == 0) {
                        canvas.translate(((float) passwordWidth - passwordIconWidth) / 2,
                                ((float) passwordHeight - passwordIconHeight) / 2);
                    }
                    canvas.drawRect(0, 0,
                            (float) passwordIconWidth, (float) passwordIconHeight, passwordIconPaint);
                    canvas.translate(passwordWidth + passwordDistance, 0); //画笔移动到下个位置
                    continue;
                }
                text = "*";
            }

            Paint.FontMetricsInt fontMetrics = passwordPaint.getFontMetricsInt();
            int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
            canvas.drawText(text, passwordWidth / 2, baseline, passwordPaint);
            canvas.translate(passwordWidth + passwordDistance, 0); //画笔移动到下个位置
        }

    }

    private static boolean isPasswordInputType(int inputType) {
        final int variation =
                inputType & (EditorInfo.TYPE_MASK_CLASS | EditorInfo.TYPE_MASK_VARIATION);
        return variation
                == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD)
                || variation
                == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD)
                || variation
                == (EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(passwordWidth * maxLength + passwordDistance * (maxLength - 1),
                passwordHeight);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        this.textLength = text.toString().length();
        postInvalidate();
    }



    public int getMaxLength() {
        InputFilter[] filters = getFilters();
        for (InputFilter filter : filters) {
            if (filter instanceof InputFilter.LengthFilter) {
                InputFilter.LengthFilter lengthFilter = (InputFilter.LengthFilter) filter;
                return lengthFilter.getMax();
            }
        }
        return -1;
    }

    /**
     * drawable转bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitmap(Drawable drawable, int width, int height) {
        if (drawable == null) return null;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

}
