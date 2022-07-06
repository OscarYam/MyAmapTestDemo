package com.example.myamaptestdemo.ui.settings;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;

import com.example.myamaptestdemo.R;
import com.example.myamaptestdemo.data.AppDatabase;
import com.example.myamaptestdemo.data.User;
import com.example.myamaptestdemo.databinding.DialogChangeSexBinding;

public class ChangeSexDialogFragment extends DialogFragment {
    private DialogChangeSexBinding dialogChangeSexBinding;
    private boolean mIsShowAnimation = true;

    private static final String MALE = "男";
    private static final String FEMALE = "女";
    private OnSexChooseListener mOnSexChooseListener;

    public interface OnSexChooseListener {
        void onSexChoose(String sex);
    }

    public void setOnSexChooseListener(OnSexChooseListener onSexChooseListener) {
        mOnSexChooseListener = onSexChooseListener;
    }

    public void showAnimation(boolean show) {
        mIsShowAnimation = show;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        dialogChangeSexBinding = DialogChangeSexBinding.inflate(inflater, container, false);
        return dialogChangeSexBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dialogChangeSexBinding.btnDialogSexCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        dialogChangeSexBinding.switchMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (!compoundButton.isPressed()) {
                    return;
                }

                dialogChangeSexBinding.switchFemale.setChecked(!checked);

                if (checked) {
                    mOnSexChooseListener.onSexChoose(MALE);
                } else {
                    mOnSexChooseListener.onSexChoose(FEMALE);
                }

                dismiss();
            }
        });

        dialogChangeSexBinding.switchFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (!compoundButton.isPressed()) {
                    return;
                }

                dialogChangeSexBinding.switchMale.setChecked(!checked);

                if (checked) {
                    mOnSexChooseListener.onSexChoose(FEMALE);
                } else {
                    mOnSexChooseListener.onSexChoose(MALE);
                }

                dismiss();
            }
        });


        // 监视本地数据库登录用户数据变动
        AppDatabase.getInstance(requireActivity()).myDao().getLoggedUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if ( user!= null) {
                    if (user.userSex != null) {
                        if (user.userSex.matches("男")) {
                            dialogChangeSexBinding.switchMale.setChecked(true);
                            dialogChangeSexBinding.switchFemale.setChecked(false);
                        } else if (user.userSex.matches("女")) {
                            dialogChangeSexBinding.switchFemale.setChecked(true);
                            dialogChangeSexBinding.switchMale.setChecked(false);
                        } else {
                            dialogChangeSexBinding.switchMale.setChecked(false);
                            dialogChangeSexBinding.switchFemale.setChecked(false);
                        }
                    }
                }
            }
        });




    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.NormalBottomDialog);
//        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定

        dialog.setContentView(R.layout.dialog_change_sex);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消

        Window window = dialog.getWindow();
        if (window != null) {
            if (mIsShowAnimation) {
                window.getAttributes().windowAnimations = R.style.NormalDialogAnim;
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
