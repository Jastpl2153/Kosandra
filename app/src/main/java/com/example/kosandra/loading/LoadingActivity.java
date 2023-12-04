package com.example.kosandra.loading;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kosandra.MainActivity;
import com.example.kosandra.R;
import com.example.kosandra.databinding.ActivityLoadingBinding;

import java.time.LocalTime;
import java.util.Random;

public class LoadingActivity extends AppCompatActivity {
    private ActivityLoadingBinding binding;
    private LocalTime nowTime = LocalTime.now();
    private int timeOfDay = nowTime.getHour();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoadingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.greeting.setText(initGreeting());
        binding.quote.setText(randomQuote());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(LoadingActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, 6000);
    }

    private String initGreeting(){
        String greeting;
        if (timeOfDay >= 5 && timeOfDay < 12) {
            greeting = getString(R.string.good_morning);
        } else if (timeOfDay >= 12 && timeOfDay < 18) {
            greeting = getString(R.string.good_afternoon);
        } else if (timeOfDay >= 18 && timeOfDay < 21) {
            greeting = getString(R.string.good_evening);
        } else {
            greeting = getString(R.string.good_night);
        }
        return greeting;
    }

    private String randomQuote(){
        String[] quotesArray = getResources().getStringArray(R.array.quotes_array);
        int randomQuote = new Random().nextInt(quotesArray.length);
        return quotesArray[randomQuote];
    }
}
