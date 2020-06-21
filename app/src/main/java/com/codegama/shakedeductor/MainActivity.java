package com.codegama.shakedeductor;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import safety.com.br.android_shake_detector.core.ShakeCallback;
import safety.com.br.android_shake_detector.core.ShakeDetector;
import safety.com.br.android_shake_detector.core.ShakeOptions;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.lottieOne)
    LottieAnimationView lottieOne;
    @BindView(R.id.lottieTwo)
    LottieAnimationView lottieTwo;
    @BindView(R.id.layoutPoints)
    LinearLayout layoutPoints;
    @BindView(R.id.points)
    TextView points;
    @BindView(R.id.close)
    Button close;
    @BindView(R.id.tryAgain)
    Button tryAgain;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.pointsTitle)
    TextView pointsTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private ShakeDetector shakeDetector;
    long totalDuration = 0;
    public static int count = 0;
    int generatedNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ShakeOptions options = new ShakeOptions()
                .background(true)
                .interval(1000)
                .shakeCount(2)
                .sensibility(2.0f);

        shakeDetector = new ShakeDetector(options).start(getApplicationContext(), new ShakeCallback() {
            @Override
            public void onShake() {
                imageView.setBackgroundResource(R.drawable.egg_animation_file);

                final AnimationDrawable frameAnimation = (AnimationDrawable) imageView.getBackground();
                frameAnimation.start();
                Random r = new Random();
                generatedNumber = r.nextInt(90 - 50) + 65;

//                if (count == 3) {
                    generatedNumber = 0;
//                    count = 0;
//                }

                count++;
                points.setText(String.format("%s %s", String.valueOf(generatedNumber), "Points"));

                for (int i = 0; i < frameAnimation.getNumberOfFrames(); i++) {
                    totalDuration += frameAnimation.getDuration(i);
                    Timer timer = new Timer();
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(() -> {
                                title.setVisibility(View.INVISIBLE);
                                toolbar.setTitle(getString(R.string.congragulations));
                                lottieOne.setVisibility(generatedNumber == 0 ? View.GONE : View.VISIBLE);
                                lottieTwo.setVisibility(generatedNumber == 0 ? View.GONE : View.VISIBLE);
                                layoutPoints.setVisibility(View.VISIBLE);
                                close.setText(getString(R.string.done));
                                description.setText(getString(R.string.thanksForParticipation));
                                if (generatedNumber == 0) {
                                    pointsTitle.setText(getString(R.string.better_luck_next_time));
                                }
                            });
                        }
                    };
                    timer.schedule(timerTask, 2020);
                }
            }
        });

        tryAgain.setOnClickListener(view -> {
            finish();
            startActivity(getIntent());
        });

        close.setOnClickListener(view -> finish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shakeDetector.destroy(getApplicationContext());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
