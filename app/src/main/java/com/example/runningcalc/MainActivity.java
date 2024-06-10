package com.example.runningcalc;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextTime;
    private EditText editTextDistance;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link UI elements
        editTextTime = findViewById(R.id.editTextTime);
        editTextDistance = findViewById(R.id.editTextDistance);
        Button buttonCalculate = findViewById(R.id.buttonCalculate);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);

        // Set up button click listener
        buttonCalculate.setOnClickListener(v -> {
            // Clear previous results
            textView1.setText("");
            textView2.setText("");
            textView3.setText("");

            try {
                String time = editTextTime.getText().toString();
                String distance = editTextDistance.getText().toString();

                // Parse time in mm:ss format
                String[] timeParts = time.split(":");
                int minutes = Integer.parseInt(timeParts[0]);
                int seconds = Integer.parseInt(timeParts[1]);
                int totalSeconds = minutes * 60 + seconds;

                // Parse distance
                double distanceValue = Double.parseDouble(distance);

                // Calculate paces
                double pacePer400m = (totalSeconds / distanceValue) * 400;
                double pacePerKm = (totalSeconds / distanceValue) * 1000;
                double pacePerMile = (totalSeconds / distanceValue) * 1609.34;

                // Convert paces to m:ss format
                String pace400mStr = formatSecondsToMMSS(pacePer400m);
                String pacePerKmStr = formatSecondsToMMSS(pacePerKm);
                String pacePerMileStr = formatSecondsToMMSS(pacePerMile);

                String paces = "Paces:" + "\n" +
                        "400m: " + pace400mStr + "\n" +
                        "KM: " + pacePerKmStr + "\n" +
                        "Mile: " + pacePerMileStr;

                // Update textView1 with the result
                textView1.setText(paces);

                // Calculate equivalent times using Riegel's formula
                double time800m = calculateRiegelsFormula(totalSeconds, distanceValue, 800);
                double time1600m = calculateRiegelsFormula(totalSeconds, distanceValue, 1600);
                double time3200m = calculateRiegelsFormula(totalSeconds, distanceValue, 3200);
                double time5000m = calculateRiegelsFormula(totalSeconds, distanceValue, 5000);
                double timeCrystal = time3200m / 0.605;
                double timeLynbrook = timeCrystal * 0.637;
                double timeBaylands = timeCrystal * 1.01;

                String equivalentTimes = "Race Predictor:" + "\n" +
                        "800m: " + formatSecondsToMMSS(time800m) + "\n" +
                        "1600m: " + formatSecondsToMMSS(time1600m) + "\n" +
                        "3200m: " + formatSecondsToMMSS(time3200m) + "\n" +
                        "5000m: " + formatSecondsToMMSS(time5000m) + "\n" +
                        "Lynbrook: " + formatSecondsToMMSS(timeLynbrook) + "\n" +
                        "Crystal: " + formatSecondsToMMSS(timeCrystal) + "\n" +
                        "Baylands: " + formatSecondsToMMSS(timeBaylands) + "\n";

                // Update textView2 with the equivalent times
                textView2.setText(equivalentTimes);

                // Calculate Easy Training Pace range
                double easyPaceMin = time1600m * 1.36;
                double easyPaceMax = time1600m * 1.57;
                double tempoPaceMin = time1600m * 1.22;
                double tempoPaceMax = time1600m * 1.35;
                double thresholdPaceMin = time1600m * 1.14;
                double thresholdPaceMax = time1600m * 1.21;
                double intervalPace = time1600m * 1.07;
                double interval400Pace = intervalPace / 4.02335;

                String trainingPace = "Training Paces:" + "\n" +
                        "Easy Run: " + formatSecondsToMMSS(easyPaceMin) + " - " + formatSecondsToMMSS(easyPaceMax) + "\n" +
                        "Tempo: " + formatSecondsToMMSS(tempoPaceMin) + " - " + formatSecondsToMMSS(tempoPaceMax) + "\n" +
                        "Threshold: " + formatSecondsToMMSS(thresholdPaceMin) + " - " + formatSecondsToMMSS(thresholdPaceMax) + "\n" +
                        "VO2 Max: " + formatSecondsToMMSS(intervalPace) + " " + "(per 400: " + formatSecondsToMMSS(interval400Pace) + ")";

                // Update textView3 with the training paces
                textView3.setText(trainingPace);
            } catch (Exception e) {
                textView1.setText("Invalid input format");
                textView2.setText("");
                textView3.setText("");
            }
        });
    }

    private double calculateRiegelsFormula(double T1, double D1, double D2) {
        return T1 * Math.pow(D2 / D1, 1.095);
    }

    private String formatSecondsToMMSS(double totalSeconds) {
        int minutes = (int) totalSeconds / 60;
        int seconds = (int) totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }
}
