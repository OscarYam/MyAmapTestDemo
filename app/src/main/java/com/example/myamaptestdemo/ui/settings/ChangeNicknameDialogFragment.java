package com.example.myamaptestdemo.ui.settings;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myamaptestdemo.R;
import com.example.myamaptestdemo.data.AppDatabase;
import com.example.myamaptestdemo.data.User;
import com.example.myamaptestdemo.databinding.DialogChangeNicknameBinding;

public class ChangeNicknameDialogFragment extends DialogFragment {

    private static final String TARGET = "ChangeNicknameDialogFrg";

    private DialogChangeNicknameBinding dialogChangeNicknameBinding;
    private UserInfoViewModel userInfoViewModel;
    private boolean mIsShowAnimation = true;

    private OnNicknameSetListener mOnNicknameSetListener;

    private String old_Nickname;

    public interface OnNicknameSetListener {
        void onNicknameSet(String nickname);
    }

    public void setOnNicknameSetListener (OnNicknameSetListener onNicknameSetListener) {
        mOnNicknameSetListener = onNicknameSetListener;
    }

    public void showAnimation(boolean show) {
        mIsShowAnimation = show;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dialogChangeNicknameBinding = DialogChangeNicknameBinding.inflate(inflater, container, false);
        return dialogChangeNicknameBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        String old_Nickname;
//        try {
//             old_Nickname = requireArguments().getString("old_Nickname");
//        } catch (IllegalStateException illegalStateException) {
//            illegalStateException.printStackTrace();
//            Log.e(TARGET, illegalStateException.getMessage());
//            return;
//        }

        userInfoViewModel = new ViewModelProvider(requireActivity()).get(UserInfoViewModel.class);

        dialogChangeNicknameBinding.btnDialogNicknameCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        userInfoViewModel.getNicknameFormState().observe(requireActivity(), new Observer<NicknameFormState>() {
            @Override
            public void onChanged(NicknameFormState nicknameFormState) {

                if (nicknameFormState.getNicknameError() != null
                        && dialogChangeNicknameBinding.editTextDialogNicknameSet.getText().toString().length() > 0) {
                    dialogChangeNicknameBinding.editTextDialogNicknameSet.setError(nicknameFormState.getNicknameError());
                }

            }
        });

        dialogChangeNicknameBinding.editTextDialogNicknameSet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                userInfoViewModel.nicknameDataChanged(old_Nickname, dialogChangeNicknameBinding.editTextDialogNicknameSet.getText().toString());
            }
        });

        dialogChangeNicknameBinding.editTextDialogNicknameSet.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (userInfoViewModel.getNicknameFormState().getValue() != null &&
                            userInfoViewModel.getNicknameFormState().getValue().isDataValid()) {
                        // 仅当修改昵称合法时执行
                        mOnNicknameSetListener.onNicknameSet(dialogChangeNicknameBinding.editTextDialogNicknameSet.getText().toString());
                        dismiss();
                    }
                }
                return false;
            }
        });


        // 监视本地数据库登录用户数据变动
        AppDatabase.getInstance(requireActivity()).myDao().getLoggedUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if ( user!= null) {
                    old_Nickname = user.userName;
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

        dialog.setContentView(R.layout.dialog_change_nickname);
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
