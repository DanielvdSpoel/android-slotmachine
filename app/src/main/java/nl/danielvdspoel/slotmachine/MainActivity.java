package nl.danielvdspoel.slotmachine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView timeView;
    Button zeroButton;
    Button upButton;
    Button downButton;

    SeekBar speedSeekBar;
    TextView speedValueText;

    int time;
    int speed;

    int changer;

    CountEvent event;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeView = findViewById(R.id.timeView);

        upButton = findViewById(R.id.upButon);
        downButton = findViewById(R.id.downButton);
        zeroButton = findViewById(R.id.zeroButton);
        speedSeekBar = findViewById(R.id.speedSeekBar);
        speedValueText = findViewById(R.id.speedValueText);
        changer = 1;

        time = savedInstanceState != null ? savedInstanceState.getInt("time", 0) : 0;
        speed = savedInstanceState != null ? savedInstanceState.getInt("speed", 1000) : 1000;

        timeView.setText(String.valueOf(time));
        speedValueText.setText(speed + " ms");

        event = new CountEvent();
        handler = new Handler();

        speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speed = progress;
                speedValueText.setText(speed + " ms");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        zeroButton.setOnClickListener(v -> {
            time = 0;
            timeView.setText(String.valueOf(time));
        });

        upButton.setOnClickListener(v -> {
            if (upButton.getText().equals("Up")) {
                handler.removeCallbacks(event);
                downButton.setText("Down");

                upButton.setText("Stop");
                changer = 1;
                handler.postDelayed(event, speed);
            } else {
                upButton.setText("Up");
                handler.removeCallbacks(event);
            }
        });

        downButton.setOnClickListener(v -> {
            if (downButton.getText().equals("Down")) {
                handler.removeCallbacks(event);
                upButton.setText("Up");

                downButton.setText("Stop");
                changer = -1;
                handler.postDelayed(event, speed);
            } else {
                downButton.setText("Down");
                handler.removeCallbacks(event);
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("time", time);
        outState.putInt("speed", speed);
        super.onSaveInstanceState(outState);
    }
    private class CountEvent implements Runnable {

        @Override
        public void run() {
            time += changer;
            timeView.setText(time + "");
            handler.postDelayed(this, speed);
        }
    }
}