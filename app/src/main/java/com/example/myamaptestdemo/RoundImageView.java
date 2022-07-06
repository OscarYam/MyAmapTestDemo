package com.example.myamaptestdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.Nullable;

public class RoundImageView extends androidx.appcompat.widget.AppCompatImageView {

    /**
     * 圆形模式
     */
    private static final int MODE_CIRCLE = 0x01;
    /**
     * 普通模式
     */
    private static final int MODE_NONE = 0x00;
    /**
     * 圆角模式
     */
    private static final int MODE_ROUND = 0x02;
    /**
     * 修改头像
     * */
    private static final int MODE_ROUND_EDIT = 0x03;

    private Paint mPaint;
    private Paint mPaintForgeGround;
    private int currMode = 0;
    /**
     * 圆角半径
     */
    private int currRound = dp2px(10);

    public RoundImageView(Context context) {
        super(context);
        initViews();
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyledAttrs(context, attrs, defStyleAttr);
        initViews();
    }


    private void obtainStyledAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView, defStyleAttr, 0);
        currMode = a.hasValue(R.styleable.RoundImageView_type) ? a.getInt(R.styleable.RoundImageView_type, MODE_NONE) : MODE_NONE;
        currRound = a.hasValue(R.styleable.RoundImageView_radius) ? a.getDimensionPixelSize(R.styleable.RoundImageView_radius, currRound) : currRound;
        a.recycle();
    }

    private void initViews() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaintForgeGround = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /**
         * 当模式为圆形模式的时候，我们强制让宽高一致
         */
        if (currMode == MODE_CIRCLE) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int result = Math.min(getMeasuredHeight(), getMeasuredWidth());
            setMeasuredDimension(result, result);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        Drawable mDrawable = getDrawable();
        Matrix mDrawMatrix = getImageMatrix();
        if (mDrawable == null) {
            return; // couldn't resolve the URI
        }

        if (mDrawable.getIntrinsicWidth() == 0 || mDrawable.getIntrinsicHeight() == 0) {
            return; // nothing to draw (empty bounds)
        }

        if (mDrawMatrix == null && getPaddingTop() == 0 && getPaddingLeft() == 0) {
            mDrawable.draw(canvas);
        } else {
            final int saveCount = canvas.getSaveCount();
            canvas.save();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                if (getCropToPadding()) {
                    final int scrollX = getScrollX();
                    final int scrollY = getScrollY();
                    canvas.clipRect(scrollX + getPaddingLeft(), scrollY + getPaddingTop(),
                            scrollX + getRight() - getLeft() - getPaddingRight(),
                            scrollY + getBottom() - getTop() - getPaddingBottom());
                }
            }
            canvas.translate(getPaddingLeft(), getPaddingTop());
            if (currMode == MODE_CIRCLE) {
                //当为圆形模式的时候
                int bgWidth = getWidth();
                int bgHeight = getHeight();
                Bitmap bitmap = drawable2Bitmap(mDrawable);
                mPaint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                canvas.drawCircle(bgWidth / 2, bgHeight / 2, bgWidth / 2, mPaint);

            } else if (currMode == MODE_ROUND_EDIT) {
                //当为修改头像模式的时候
                int bgWidth = getWidth();
                int bgHeight = getHeight();
                Bitmap bitmap = drawable2Bitmap(mDrawable);
                mPaint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                canvas.drawCircle(bgWidth / 2, bgHeight / 2, bgWidth / 2, mPaint);

                mPaintForgeGround.setAlpha(155);
                Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.edit);
                mPaintForgeGround.setShader(new BitmapShader(bitmap2, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
//                canvas.drawBitmap(bitmap2, 0, 0,mPaintForgeGround);
                canvas.drawCircle(bgWidth / 2, bgHeight / 2, bgWidth, mPaintForgeGround);
                canvas.save();

            } else if (currMode == MODE_ROUND) {
                //当为圆角模式的时候
                Bitmap bitmap = drawable2Bitmap(mDrawable);
                mPaint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                canvas.drawRoundRect(new RectF(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom()),
                        currRound, currRound, mPaint);
            } else {
                if (mDrawMatrix != null) {
                    canvas.concat(mDrawMatrix);
                }
                mDrawable.draw(canvas);
            }
            canvas.restoreToCount(saveCount);
        }

    }

    /**
     * drawable转换成bitmap
     */
    private Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        //根据传递的 scaleType 获取matrix对象，设置给 bitmap
        Matrix matrix = getImageMatrix();
        if (matrix != null) {
            canvas.concat(matrix);
        }
        drawable.draw(canvas);
        return bitmap;
    }

    private int dp2px(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }


    /**
     * 水印功能
     * */
    private static Bitmap toConformBitmap(Bitmap background, Bitmap foreground) {
        if( background == null ) {
            return null;
        }

        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();
        //create the new blank bitmap 创建一个新的和SRC长度宽度一样的位图
        Bitmap newbmp = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newbmp);
        //draw bg into
        cv.drawBitmap(background, 0, 0, null);//在 0,0坐标开始画入bg
        //draw fg into
        cv.drawBitmap(foreground, 0, 0, null);//在 0,0坐标开始画入fg, 可以从任意位置画入
        //save all clip
//        cv.save(Canvas.ALL_SAVE_FLAG);//保存
        cv.save();
        //store
        cv.restore();//存储
        return newbmp;
    }

}
