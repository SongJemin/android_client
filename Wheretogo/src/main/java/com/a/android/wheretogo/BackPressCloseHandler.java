package com.a.android.wheretogo;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by JAMCOM on 2017-09-05.
 */

public class BackPressCloseHandler {

    private long backKeyPressedTime = 0;
    private Toast toast;

    private Activity activity;

    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            //android.os.Process.killProcess(android.os.Process.myPid());
            activity.finish();
            toast.cancel();
        }
    }

    public void showGuide() {
        toast = Toast.makeText(activity,
                "\'뒤로\'버튼을 한번 더 누르시면 로그인 화면으로 돌아갑니다.", Toast.LENGTH_SHORT);
        //toast.show();
    }
}

