package cn.zucc.qwmcql.personalassistant.anime;
/**
 * Created by My PC on 2017/5/21.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import cn.zucc.qwmcql.personalassistant.LoginActivity;
import cn.zucc.qwmcql.personalassistant.R;


public class AnimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_anime);
        initImage();
    }

    private void initImage() {
        ImageView iv_start = (ImageView) findViewById(R.id.iv_start);
        iv_start.setImageResource(R.drawable.anime);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.4f, 1.0f, 1.4f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(4000);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        iv_start.startAnimation(scaleAnimation);
    }

    private void startActivity() {
        Intent intent = new Intent(AnimeActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}


