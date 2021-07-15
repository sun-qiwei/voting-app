/** author: lice Liu and Nicole Ni
 * date: 2019.10.30
 * This class used to set SPTool to use interact with other activities
 */
package com.example.project.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

/**
 */

public class SPTool {

    private final String TAG = "SPTool";

    private SharedPreferences mSharedPreferences;// 单例
    private Context mContext;

    private SPTool() {
    }

    private static class Holder {
        private static SPTool singleton = new SPTool();
    }

    public static SPTool getInstanse() {
        return Holder.singleton;
    }

    public void init(Context context){
        if (context == null) {
            throw new IllegalArgumentException();
        }
        this.mContext = context;
        mSharedPreferences = context.getSharedPreferences("sp_cache",
                Context.MODE_PRIVATE);
    }


    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     */
    public void setParam(String key, Object object) {

        String type = object.getClass().getSimpleName();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        } else if (object instanceof List) {
            editor.putString(key, new Gson().toJson(object));
        }

        editor.commit();
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key
     * @param defaultObject
     * @return
     */
    public Object getParam(String key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        if ("String".equals(type)) {
            return mSharedPreferences.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return mSharedPreferences.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return mSharedPreferences.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return mSharedPreferences.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return mSharedPreferences.getLong(key, (Long) defaultObject);
        }
        return null;
    }


    /**
     * 清除所有数据
     */
    public void clear() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear().commit();
    }


    /**
     * 清除指定的数据
     *
     * @param key
     */
    public void remove(String key) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param key
     * @return
     */
    public boolean contains(String key) {
        return mSharedPreferences.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @return
     */
    public Map<String, ?> getAllKey() {
        return mSharedPreferences.getAll();
    }

    /**
     *
     * 设置bitmap
     *
     * @param key
     * @param object
     */
    public void setBitmap(String key, Bitmap object) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        String imgBase64 = "";
        if (object != null) {
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
            object.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
            imgBase64 = new String(Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
        }
        editor.putString(key, imgBase64);
        editor.commit();
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     * @param key
     * @return
     */
    public Bitmap getBitmap(String key) {
        String imgBase64 = mSharedPreferences.getString(key, "");
        if (TextUtils.isEmpty(imgBase64))
            return  null;
        byte[] bytes = Base64.decode(imgBase64.getBytes(),1);
        //  byte[] bytes =imgBase64.getBytes();
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }
}
