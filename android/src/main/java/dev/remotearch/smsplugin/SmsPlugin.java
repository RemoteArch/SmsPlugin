package dev.remotearch.smsplugin;

import android.util.Log;

public class SmsPlugin {

    public String echo(String value) {
        Log.i("Echo", value);
        return value;
    }
}
