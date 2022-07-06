package com.example.myamaptestdemo.ui.settings;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
import androidx.lifecycle.ViewModelProvider;

import com.example.myamaptestdemo.R;
import com.example.myamaptestdemo.databinding.DialogChangeUserimgBinding;

import java.io.File;

public class ChangeUserImageDialogFragment extends DialogFragment {
    private static final String TARGET = "ChangeUserImageDialog";

    private DialogChangeUserimgBinding mDialogChangeUserImgBinding;
    private UserInfoViewModel userInfoViewModel;

    private static final int CHOOSE_BIG_PICTURE = 0x01;
//    private static final int CHOOSE_SMALL_PICTURE = 0x02;
    private static final int REQUEST_IMAGE_PICK = 0x03;
    private static final int REQUEST_IMAGE_CAMERA = 0x04;

    private File tmpJpg = null;
    private boolean mIsShowAnimation = true;

    private OnUserImageSetListener mOnUserImageSetListener;

    public interface OnUserImageSetListener {
        void onUserImageSet(File userImage);
    }

    public void setOnUserImageSetListener (OnUserImageSetListener onUserImageSetListener) {
        mOnUserImageSetListener = onUserImageSetListener;
    }

    public void showAnimation(boolean show) {
        mIsShowAnimation = show;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        mDialogChangeUserImgBinding = DialogChangeUserimgBinding.inflate(inflater, container, false);
        return mDialogChangeUserImgBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userInfoViewModel = new ViewModelProvider(requireActivity()).get(UserInfoViewModel.class);
        userInfoViewModel.getUserTmpJpg().observe(requireActivity(), new Observer<File>() {
            @Override
            public void onChanged(File file) {
                tmpJpg = file;
            }
        });

//        String userImgPath = requireArguments().getString("UserImg_Path");
//        tmpJpg = new File(userImgPath);

        mDialogChangeUserImgBinding.btnDialogUserimgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mDialogChangeUserImgBinding.llChooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 从相册选择图片
                imageFromChooser();
            }
        });

        mDialogChangeUserImgBinding.llTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 从相机拍照
                imageFromCamera();
            }
        });





    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.NormalBottomDialog);
//        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定

        dialog.setContentView(R.layout.dialog_change_userimg);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        Log.e("onActivityResult", "resultCode = " + resultCode + "\n data = " + data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CHOOSE_BIG_PICTURE:
                    if (tmpJpg.exists()) {
                        mOnUserImageSetListener.onUserImageSet(tmpJpg);
                        dismiss();
                    }
                    break;

//                case CHOOSE_SMALL_PICTURE:
//                    if (data != null) {
//                        Bitmap bitmap = data.getParcelableExtra("data");
//                        imageViewUser.setImageBitmap(bitmap);
//                    } else {
//                        Log.e("CHOOSE_SMALL_PICTURE", "CHOOSE_SMALL_PICTURE: data = " + data);
//                    }
//                    break;

                case REQUEST_IMAGE_PICK:
                    if (data != null) {
                        imageCrop(data.getData(), tmpJpg);
                    }
                    break;

                case REQUEST_IMAGE_CAMERA:
                    if (tmpJpg.exists()) {
                        imageCrop(Uri.fromFile(tmpJpg), tmpJpg);
                    }
                    break;

                default:
                    break;
            }
        } else {
            Log.e(TARGET, "ActivityResultCode = " + resultCode);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 从相册获取图片
     * */
    private void imageFromChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        /**
         * 下面这句话，与其它方式写是一样的效果，如果：
         * intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
         * intent.setType(""image/*");设置数据类型
         * 要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
         */
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    /**
     * 从照相机拍照获图像
     * */
    private void imageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tmpJpg)); // 指定调用相机拍照后的照片存储的路径
            startActivityForResult(intent, REQUEST_IMAGE_CAMERA); // REQUEST_IMAGE_CAMERA 是用作判断返回结果的标识
        } else {
            Log.e("ACTION_IMAGE_CAPTURE", "No Activity found to handle Intent!");
        }
    }

    /**
     * 图片裁剪
     * */
    private void imageCrop(Uri SrcImageUri, File destFile) {
        Intent intent = new Intent("com.android.camera.action.CROP", null);
        intent.setDataAndType(SrcImageUri, "image/*");
        intent.putExtra("crop", "true"); // 发送裁剪信号
        intent.putExtra("circleCrop", "true");
        intent.putExtra("aspectX", 1); // X方向上的比例
        intent.putExtra("aspectY", 1); // Y方向上的比例
        intent.putExtra("outputX", 300); // 裁剪区的宽度
        intent.putExtra("outputY", 300); // 裁剪区的高度
        intent.putExtra("scale", true); // 是否保留比例

        intent.putExtra("return-data", false); // 是否将数据保留在Bitmap中返回

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destFile)); // 将URI指向相应的file

        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, CHOOSE_BIG_PICTURE);
    }

}
