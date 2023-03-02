package nl.danielvdspoel.slotmachine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Button runButton;
    HashMap<Integer, ImageView> slots;
    HashMap<Integer, Integer> times;
    HashMap<Integer, CountEvent> events;

    HashMap<Integer, Drawable> images;

    Handler handler;

    Integer speed = 100;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        runButton = findViewById(R.id.runButton);

//        time = savedInstanceState != null ? savedInstanceState.getInt("time", 0) : 0;
//        timeView.setText(String.valueOf(time));

        slots = new HashMap<>();
        slots.put(0, findViewById(R.id.leftSlotView));
        slots.put(1, findViewById(R.id.middleSlotView));
        slots.put(2, findViewById(R.id.rightSlotView));

        times = new HashMap<>();
        times.put(0, 0);
        times.put(1, 0);
        times.put(2, 0);

        images = new HashMap<>();
        images.put(0, getDrawable(R.drawable.cherry));
        images.put(1, getDrawable(R.drawable.grape));
        images.put(2, getDrawable(R.drawable.pear));
        images.put(3, getDrawable(R.drawable.strawberry));

        events = new HashMap<>();
        handler = new Handler();

        runButton.setOnClickListener(v -> {

            if (runButton.getText().equals("Run!")) {
                for (Map.Entry<Integer, ImageView> slot : slots.entrySet()) {
                    CountEvent event = new CountEvent(slot.getKey());
                    events.put(slot.getKey(), event);
                    handler.postDelayed(event, speed);
                }
                runButton.setText("Stop");
            } else {
                runButton.setText("Run!");

                if (Objects.equals(times.get(0), times.get(1)) && Objects.equals(times.get(1), times.get(2))) {
                    Toast t = Toast.makeText(getApplicationContext(), "JACKPOT!!!", Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    t.show();
                } else if (Objects.equals(times.get(0), times.get(1)) || Objects.equals(times.get(0), times.get(2)) || Objects.equals(times.get(1), times.get(2))) {
                    Toast toast = Toast.makeText(getApplicationContext(), "SEMI-JACKPOT!!!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

                for (Map.Entry<Integer, CountEvent> event : events.entrySet()) {
                    handler.removeCallbacks(event.getValue());
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        //outState.putInt("time", time);
        super.onSaveInstanceState(outState);
    }

    public static double getRandom(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("Invalid range [" + min + ", " + max + "]");
        }
        return min + Math.random() * (max - min);
    }
    private class CountEvent implements Runnable {

        Integer number;
        Double speedMultiplier;

        public CountEvent(Integer number) {
            this.number = number;
            this.speedMultiplier = getRandom(1, 2);
        }

        @Override
        public void run() {
            ImageView imageView = slots.get(number);
            Integer time = times.get(number);
            if (time < 3) {
                time += 1;
            } else {
                time = 0;
            }
            imageView.setImageDrawable(images.get(time));
            times.put(number, time);
            handler.postDelayed(this, (long) (speed * speedMultiplier));
        }
    }
}