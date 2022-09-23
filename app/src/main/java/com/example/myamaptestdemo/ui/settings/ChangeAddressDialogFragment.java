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
import androidx.lifecycle.Observer;

import com.example.myamaptestdemo.R;
import com.example.myamaptestdemo.data.AppDatabase;
import com.example.myamaptestdemo.data.User;
import com.example.myamaptestdemo.databinding.DialogChangeAddressBinding;

public class ChangeAddressDialogFragment extends DialogFragment {

    private static final String TARGET = "ChangeAddrDialogFrag";
    private DialogChangeAddressBinding dialogChangeAddressBinding;

    private boolean mIsShowAnimation = true;

    private OnAddressSetListener mOnAddressSetListener;

    public interface OnAddressSetListener {
        void onAddressSet(String address);
    }

    public void setOnAddressSetListener(OnAddressSetListener onAddressSetListener) {
        mOnAddressSetListener = onAddressSetListener;
    }

    public void showAnimation(boolean show) {
        mIsShowAnimation = show;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dialogChangeAddressBinding = DialogChangeAddressBinding.inflate(inflater, container, false);
        return dialogChangeAddressBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dialogChangeAddressBinding.btnDialogAddressCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        //监视本地数据库登录用户数据变动
        AppDatabase.getInstance(requireActivity()).myDao().getLoggedUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                // 获取数据库中的用户地址并显示
                //......

                if ( user!= null) {
//                    if (user.address.)
//                    if (user.userSex != null) {
//                        if (user.userSex.matches("男")) {
//                            dialogChangeSexBinding.switchMale.setChecked(true);
//                            dialogChangeSexBinding.switchFemale.setChecked(false);
//                        } else if (user.userSex.matches("女")) {
//                            dialogChangeSexBinding.switchFemale.setChecked(true);
//                            dialogChangeSexBinding.switchMale.setChecked(false);
//                        } else {
//                            dialogChangeSexBinding.switchMale.setChecked(false);
//                            dialogChangeSexBinding.switchFemale.setChecked(false);
//                        }
//                    }
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

        dialog.setContentView(R.layout.dialog_change_address);
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
