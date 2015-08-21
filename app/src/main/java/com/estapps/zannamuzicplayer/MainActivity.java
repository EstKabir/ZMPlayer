package com.estapps.zannamuzicplayer;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Aderindo layout
        setContentView(R.layout.activity_main);
    }



    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

}