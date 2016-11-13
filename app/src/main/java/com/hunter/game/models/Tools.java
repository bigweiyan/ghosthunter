package com.hunter.game.models;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * 实用小工具.
 * Created by weiyan on 2016/11/13.
 */

public class Tools {

    /**
     * 判断输入是否为纯数字+英文.
     * @param s 输入字符串
     * @return true:纯数字+英文;false:非法字符
     */
    public static boolean checkAlpha(String s) {
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if((ch < 'a' || ch > 'z') && (ch < '0' || ch > '9') && (ch < 'A' || ch > 'Z')) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断输入是否为纯数字.
     * @param s 输入字符串
     * @return true:纯数字;false:非法字符
     */
    public static boolean checkInt(String s) {
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if(ch < '0' || ch > '9') {
                return false;
            }
        }
        return true;
    }

    /**
     * 显示对话框,内含一个确定和一个返回.
     * @param activity 在该活动中显示.
     * @param title 对话框标题.
     * @param message 对话框文字.
     */
    public static void showDialog(final Activity activity, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title).setMessage(message).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
            }
        }).show();
    }
}
