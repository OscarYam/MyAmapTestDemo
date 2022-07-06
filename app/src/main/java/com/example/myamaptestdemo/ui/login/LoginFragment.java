package com.example.myamaptestdemo.ui.login;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myamaptestdemo.Utils;
import com.example.myamaptestdemo.databinding.FragmentLoginBinding;
import com.example.myamaptestdemo.R;


public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private FragmentLoginBinding fragmentLoginBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        fragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false);
        return fragmentLoginBinding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginViewModel = new ViewModelProvider(requireActivity(), new LoginViewModelFactory()).get(LoginViewModel.class);

        final EditText usernameEditText = fragmentLoginBinding.username;
        final EditText passwordEditText = fragmentLoginBinding.password;
        final Button loginButton = fragmentLoginBinding.login;
        final ProgressBar loadingProgressBar = fragmentLoginBinding.loading;

        // 设置用户外部存储空间中的应用专属图片目录
        loginViewModel.setUserImageExternalFilesDir(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES));

        fragmentLoginBinding.container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (Utils.back2PreviousFrame(motionEvent)) {
                    // 隐藏本层 frame, 并显示上一层 frame
//                    Log.e("LoginFragment", "返回上一层 frame");
                    loginViewModel.getFrameVisibility().postValue(View.GONE);
                } else {
//                    Log.e("LoginFragment", "空白处被点击");
                    // 隐藏软键盘
                    Utils.hideKeyboard(requireActivity());
                }
                return true;
            }
        });

        loginViewModel.getLoginFormState().observe(requireActivity(), new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {

                if (loginFormState == null) {
                    return;
                }

                if (!loginViewModel.isLoggedIn()) {

                    loginButton.setEnabled(loginFormState.isDataValid());

                    // 只在未登录时检查输入如容的合法性
                    if (loginFormState.getUsernameError() != null
                            && usernameEditText.getText().toString().length() > 0) {
                        usernameEditText.setError(getString(loginFormState.getUsernameError()));
                    }

                    if (loginFormState.getPasswordError() != null
                            && passwordEditText.getText().toString().length() > 0) {
                        passwordEditText.setError(getString(loginFormState.getPasswordError()));
                    }
                }

            }
        });

        loginViewModel.getLoginResult().observe(requireActivity(), new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {

                if (loginResult == null) {
                    return;
                }

                loadingProgressBar.setVisibility(View.GONE);

                if (loginResult.getError() != null) {
                    // 登录失败
                    if (getContext() != null && getContext().getApplicationContext() != null) {
                        Toast.makeText(getContext().getApplicationContext(), loginResult.getError(), Toast.LENGTH_LONG).show();
                    }
                }

                if (loginResult.getSuccess() != null) {
                    // 登录成功
                    updateUiWithUser(loginResult.getSuccess());
                }

                passwordEditText.setText(""); // 保证安全性,无论是否登录成功都应清除 passwordEditText 中的密码

            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (loginButton.isEnabled()) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                loginViewModel.login(usernameEditText.getText().toString(),
                                        passwordEditText.getText().toString());
                            }
                        }).start();

                        loadingProgressBar.setVisibility(View.VISIBLE);
                    }
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loginViewModel.login(usernameEditText.getText().toString(),
                                passwordEditText.getText().toString());
                    }
                }).start();

                loadingProgressBar.setVisibility(View.VISIBLE);

//                // 隐藏软键盘
//                InputMethodManager inputMethodManager = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputMethodManager.hideSoftInputFromWindow(requireActivity().getWindow().getDecorView().getWindowToken(), 0);

                // 隐藏软键盘
                Utils.hideKeyboard(requireActivity());
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + " " + model.getDisplayName();
        // TODO : initiate successful logged in experience
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(getContext().getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
//            Toast.makeText(requireContext(), welcome, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        loginViewModel.dbLogout();
        fragmentLoginBinding = null;

//        Log.e("LoginFragment", "onDestroyView");
    }
}