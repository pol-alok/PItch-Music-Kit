/*
 *  Author(s): Takamitsu Mizutori, Goldrush Computing Inc.
 */

package com.yurisuzuki;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.yurisuzuki.fragment.FragmentMenu;
import com.yurisuzuki.playsound.R;

public class MainActivity extends Activity {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.app.FragmentManager fm = getFragmentManager();
        FragmentMenu fragment = (FragmentMenu) fm.findFragmentByTag("FragmentMenu");
        if (fragment == null) {
            fragment = FragmentMenu.newInstance();
        }

        if(!fragment.isAdded()){
            fm.beginTransaction()
                    .add(R.id.main_container, fragment, "FragmentMenu")
                    .commit();
        }

    }


    public void showInstruction(String tag) {
        Intent intent = new Intent(this, ActivityIntro.class);
        intent.putExtra("type", tag);
        startActivity(intent);
        //finish();
        //overridePendingTransition(R.anim.fade_in, R.anim.scale_out);
    }

    public void showMakeMarkerTop(){
        Intent intent = new Intent(this, ActivityMakeMarkerTop.class);
        startActivity(intent);
    }

    public void showAbout(){
        Intent intent = new Intent(this, ActivityAbout.class);
        startActivity(intent);
    }
}

