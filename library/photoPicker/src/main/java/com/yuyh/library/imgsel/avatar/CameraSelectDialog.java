package com.yuyh.library.imgsel.avatar;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.yuyh.library.imgsel.R;

/**
 * 图库选择对话框
 */
@SuppressWarnings("ALL")
public class CameraSelectDialog extends Dialog implements View.OnClickListener {

    private OnCameraSelectListener listener;

    public OnCameraSelectListener getListener() {
        return listener;
    }

    public void setListener(OnCameraSelectListener listener) {
        this.listener = listener;
    }

    protected CameraSelectDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initSheetDialog(context);
    }

    public CameraSelectDialog(Context context) {
        super(context);
        initSheetDialog(context);
    }

    public CameraSelectDialog(Context context, int themeResId) {
        super(context, themeResId);
        initSheetDialog(context);
    }

    private void initSheetDialog(Context context) {
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = getLayoutInflater().inflate(R.layout.sp_photo_choose_dialog, null);
        setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        Window window = getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.main_menu_animstyle);
            WindowManager.LayoutParams wl = window.getAttributes();
            wl.x = 0;
            wl.y = ((AppCompatActivity) context).getWindowManager().getDefaultDisplay().getHeight();
            wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
            wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            onWindowAttributesChanged(wl);
        }
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        Button btnCamera = (Button) view.findViewById(R.id.btn_to_camera);
        btnCamera.setOnClickListener(this);

        Button btnPhoto = (Button) view.findViewById(R.id.btn_to_photo);
        btnPhoto.setOnClickListener(this);

        Button btnCancel = (Button) view.findViewById(R.id.btn_to_cancel);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (listener == null)
            return;

        if (view.getId() == R.id.btn_to_camera) {
            dismiss();
            listener.onCamera();
        } else if (view.getId() == R.id.btn_to_photo) {
            dismiss();
            listener.onPhoto();
        } else if (view.getId() == R.id.btn_to_cancel) {
            dismiss();
        }
    }

    public interface OnCameraSelectListener {
        void onCamera();

        void onPhoto();
    }
}
