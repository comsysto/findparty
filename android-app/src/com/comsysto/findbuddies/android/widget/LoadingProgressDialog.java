package com.comsysto.findbuddies.android.widget;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created with IntelliJ IDEA.
 * User: stefandjurasic
 * Date: 23.03.13
 * Time: 15:04
 * To change this template use File | Settings | File Templates.
 */
public class LoadingProgressDialog extends ProgressDialog {

    public LoadingProgressDialog(Context context, String text, boolean showImmediatly) {
        super(context);
        setMessage(text);
        setCancelable(false);
        if (showImmediatly) {
            show();
        }
    }
}
