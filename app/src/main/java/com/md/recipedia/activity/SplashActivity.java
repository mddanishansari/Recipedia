package com.md.recipedia.activity;

import android.content.Intent;
import android.os.Bundle;


public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

                Intent home = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(home);
                finish();
  

    }
}
