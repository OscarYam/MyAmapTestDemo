package com.example.myamaptestdemo.ui.settings;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myamaptestdemo.R;
import com.example.myamaptestdemo.databinding.DialogChangeBirthdayBinding;

public class ChangeBirthdayDialogFragment extends DialogFragment {
    private DialogChangeBirthdayBinding dialogChangeBirthdayBinding;

    private int mSelectedYear = -1, mSelectedMonth = -1, mSelectedDay = -1;
    private OnDateChooseListener mOnDateChooseListener;
    private boolean mIsShowAnimation = true;

    public interface OnDateChooseListener {
        void onDateChoose(int year, int month, int day);
    }

    public void setOnDateChooseListener(OnDateChooseListener onDateChooseListener) {
        mOnDateChooseListener = onDateChooseListener;
    }

    public void showAnimation(boolean show) {
        mIsShowAnimation = show;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        dialogChangeBirthdayBinding = DialogChangeBirthdayBinding.inflate(inflater, container, false);
        return dialogChangeBirthdayBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dialogChangeBirthdayBinding.datePicker.setShowCurtainBorder(false);

//        dialogChangeBirthdayBinding.lldatePicker.getBackground().setAlpha(80);
//        dialogChangeBirthdayBinding.btnDialogDateCancel.getBackground().setAlpha(80);
//        dialogChangeBirthdayBinding.btnDialogDateDecide.getBackground().setAlpha(80);

        dialogChangeBirthdayBinding.btnDialogDateCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        dialogChangeBirthdayBinding.btnDialogDateDecide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnDateChooseListener != null) {
                    mOnDateChooseListener.onDateChoose(dialogChangeBirthdayBinding.datePicker.getYear(),
                            dialogChangeBirthdayBinding.datePicker.getMonth(),
                            dialogChangeBirthdayBinding.datePicker.getDay());
                }
                dismiss();
            }
        });

        if (mSelectedYear > 0) {
            setSelectedDate();
        }
    }

    public void setSelectedDate(int year, int month, int day) {
        mSelectedYear = year;
        mSelectedMonth = month;
        mSelectedDay = day;
        setSelectedDate();
    }

    private void setSelectedDate() {
        dialogChangeBirthdayBinding.datePicker.setDate(mSelectedYear, mSelectedMonth, mSelectedDay, false);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.DatePickerBottomDialog);
//        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定

        dialog.setContentView(R.layout.dialog_change_birthday);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消

        Window window = dialog.getWindow();
        if (window != null) {
            if (mIsShowAnimation) {
                window.getAttributes().windowAnimations = R.style.DatePickerDialogAnim;
            }
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.BOTTOM; // 紧贴底部
            lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
//            lp.dimAmount = 0.35f; // 背景亮度
            lp.dimAmount = 0; // 不调低背景亮度
//            lp.alpha = 0.5f; // 透明度
            window.setAttributes(lp);
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }

        return dialog;
    }

}
