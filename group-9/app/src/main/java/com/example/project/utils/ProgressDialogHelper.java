/**
 * author: Hang Xu and Haoyu Wang
 * date: 2019.11.20
 * used to set Progress Dialog Helper object and attributes
 */
package com.example.project.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

/**
 * 抽取加载圈的辅助类
 *
 *
 */

public class ProgressDialogHelper {

    private static final String TAG = "ProgressDialogHelper";

    private ProgressDialog pd;

    private Context mContext;

    public ProgressDialogHelper(Context context) {
        this.mContext = context;
        pd=  new ProgressDialog(context);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
    }

    /**
     * 展示加载圈
     *
     * @param title
     * @param msg
     */
    public void show(String title, String msg){
        Log.i(TAG, "show: ==" + pd.isShowing());
        if (pd != null && !pd.isShowing()) {
            pd.setTitle(title);
            pd.setMessage(msg);
            pd.show();
//            pd.show(mContext, title, msg);
        }
    }

    /**
     * Sets the maximum allowed progress value.
     *
     * @param max
     */
    public void setMax(int max) {
        if (pd != null) {
            pd.setMax(max);
        }
    }


    /**
     * Sets the current progress.
     *
     * @param progress
     */
    public void setProgress(int progress){
        if (pd != null) {
            pd.setProgress(progress);
        }
    }


    /**
     * 隐藏加载全
     *
     */
    public void dismiss(){
        Log.i(TAG, "dismiss: ==" + pd.isShowing());
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

}
