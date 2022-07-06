package com.example.myamaptestdemo;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DragFloatActionButton extends FloatingActionButton {
    private int parentHeight;
    private int parentWidth;
    private float lastX;
    private float lastY;
    private boolean isDrag;

    final private int layout_margin = 16; // 为与上下左右边缘各形成间隔,添加偏移量 16

    public DragFloatActionButton(@NonNull Context context) {
        super(context);
    }

    public DragFloatActionButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DragFloatActionButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private boolean isNotDrag() {
        return !isDrag && (getY() <= layout_margin || (getX() <= parentWidth - getWidth()));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        float rawX = ev.getRawX();
        float rawY = ev.getRawY();

        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                setPressed(true);
                isDrag = false;
                getParent().requestDisallowInterceptTouchEvent(true);
                lastX = rawX;
                lastY = rawY;
                ViewGroup parent;
                if (getParent() != null) {
                    parent = (ViewGroup) getParent();
                    // 为与边缘形成间隔,添加偏移量 layout_margin
                    parentHeight = parent.getHeight() - layout_margin;
                    parentWidth = parent.getWidth() - layout_margin;
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                if (parentHeight <= 0 || parentWidth <= 0) {
                    isDrag = false;
                    break;
                } else {
                    isDrag = true;
                }

                // 计算移动的坐标差
                float dx = rawX - lastX;
                float dy = rawY - lastY;

                // 此处作用为修复某些华为手机无法触发点击事件
                double dis = Math.sqrt(dx * dx + dy * dy);
//                if (dis < 1f) {
                if (dis == 0) {
                    isDrag = false;
                    break;
                }

                float x = getX() + dx;
                float y = getY() + dy;
//                Log.e("DragFB", "(xx = " + x + " | yy = " + y + ")"
//                        + "\n(getX() = " + getX() + " | getY() = " + getY() + ")"
//                        + "\n(dx = " + dx + " | dy = " + dy + ")"
//                        + "\ndis = " + dis);

                // 到达边缘检测
                x = x < layout_margin ? layout_margin : x > parentWidth - getWidth() ? parentWidth - getWidth() : x;
                y = y < layout_margin ? layout_margin : y > parentHeight - getHeight() ? parentHeight - getHeight() : y;

                setX(x);
                setY(y);
                lastX = rawX;
                lastY = rawY;
//                Log.e("DragFB", "isDrag = " + isDrag
//                        + "\ngetX = " + getX()
//                        + "\ngetY = " + getY()
//                        + "\nparentWidth = " + parentWidth
//                        + "\nparentHeight = " + parentHeight
//                        + "\nx = " + x
//                        + "\ny = " + y);
                break;
            }

            case MotionEvent.ACTION_UP: {
                if (!isNotDrag()) {
                    // 恢复按压效果
                    setPressed(false);
                    if (rawX >= parentWidth/2) {
                        // 吸附到右边
                        animate().setInterpolator(new DecelerateInterpolator())
                                .setDuration(500)
                                .xBy(parentWidth - getWidth() - getX())
                                .start();
                    } else {
                        // 吸附到左边
                        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "x",
                                getX(), layout_margin); // 为与边缘形成间隔,添加偏移量 layout_margin
                        objectAnimator.setInterpolator(new DecelerateInterpolator());
                        objectAnimator.setDuration(500);
                        objectAnimator.start();
                    }
                }
                break;
            }
        }

        // 如果是拖拽则消耗事件，否则正常传递即可
        return !isNotDrag() || super.onTouchEvent(ev);
//        return ev.getAction() != MotionEvent.ACTION_UP && (isNotDrag() || super.onTouchEvent(ev));


        // return false：表明没有消费该事件，事件将会以冒泡的方式一直被传递到上层的view或Activity中的onTouchEvent事件处理。
        // 如果最上层的view或Activity中的onTouchEvent还是返回false。则该事件将消失。
        // 接下来来的一系列事件都将会直接被上层的onTouchEvent方法捕获
        //
        // return true： 表明消费了该事件，事件到此结束。
        //
        // return super.onTouchEvent(event)：默认情况，和return false一样。
//        return super.onTouchEvent(ev);
    }
}
