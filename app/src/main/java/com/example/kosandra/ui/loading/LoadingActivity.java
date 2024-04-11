package com.example.kosandra.ui.loading;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kosandra.MainActivity;
import com.example.kosandra.R;
import com.example.kosandra.databinding.ActivityLoadingBinding;

import java.time.LocalTime;
import java.util.Random;

/**
 * LoadingActivity is responsible for displaying a loading screen with a greeting and a random quote before redirecting to the MainActivity.
 * <p>
 * The LoadingActivity class extends AppCompatActivity to provide an activity layout on the screen.
 * <p>
 * It uses ActivityLoadingBinding to bind layout elements, LocalTime to get the current time, and a Handler to handle time delays.
 */
public class LoadingActivity extends AppCompatActivity {
    private ActivityLoadingBinding binding; // Binding for the elements in the loading screen layout
    private LocalTime nowTime = LocalTime.now();// Current local time
    private int timeOfDay = nowTime.getHour();// Hour of the day extracted from the current time

    /**
     * onCreate method is called when the activity is created. It initializes the layout, sets greeting and quote texts,
     * <p>
     * and redirects to the MainActivity after a delay.
     *
     * @param savedInstanceState Bundle of data used for activity state restoration.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoadingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.greeting.setText(initGreeting()); // Set greeting text based on the time of the day
        binding.quote.setText(randomQuote());// Set a random quote text

// Redirect to MainActivity after 4 seconds delay using a Handler
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(LoadingActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, 4000);
    }

    /**
     * initGreeting method sets the appropriate greeting based on the time of the day.
     *
     * @return String containing the appropriate greeting message.
     */
    private String initGreeting() {
        String greeting;
        if (timeOfDay >= 5 && timeOfDay < 12) {
            greeting = getString(R.string.greeting_good_morning);
        } else if (timeOfDay >= 12 && timeOfDay < 18) {
            greeting = getString(R.string.greeting_good_afternoon);
        } else if (timeOfDay >= 18 && timeOfDay < 21) {
            greeting = getString(R.string.greeting_good_evening);
        } else {
            greeting = getString(R.string.greeting_good_night);
        }
        return greeting;
    }

    /**
     * randomQuote method randomly selects a quote from the quotes array.
     *
     * @return String containing a randomly selected quote.
     */
    private String randomQuote() {
        String[] quotesArray = getResources().getStringArray(R.array.quotes_array);
        int randomQuote = new Random().nextInt(quotesArray.length);
        return quotesArray[randomQuote];
    }
}
