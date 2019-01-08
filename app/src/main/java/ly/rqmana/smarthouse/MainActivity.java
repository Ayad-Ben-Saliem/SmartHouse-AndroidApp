package ly.rqmana.smarthouse;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private final static String mURL = "https://smarthouse.serveo.net/smarthouseserver";

    private AppCompatImageView doorLightIV;
    private AppCompatImageView fenceLightIV;
    private AppCompatImageView gateIV;

    int gateIndex = 0;
    int gateImageResIdes[] = {
            R.drawable.gate_1,
            R.drawable.gate_2,
            R.drawable.gate_3,
            R.drawable.gate_4,
            R.drawable.gate_5,
            R.drawable.gate_6,
            R.drawable.gate_7,
            R.drawable.gate_8,
            R.drawable.gate_9,
            R.drawable.gate_10,
            R.drawable.gate_11,
            R.drawable.gate_12,
            R.drawable.gate_13,
            R.drawable.gate_14,
            R.drawable.gate_15,
            R.drawable.gate_16,
            R.drawable.gate_17,
            R.drawable.gate_18,
            R.drawable.gate_19,
            R.drawable.gate_20,
            R.drawable.gate_21};

    private final static Timer TIMER = new Timer();
    private Thread thread;
    private InterruptableRunnable interruptableRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        doorLightIV = findViewById(R.id.doorLight);
        fenceLightIV = findViewById(R.id.fenceLight);
        gateIV = findViewById(R.id.gateIV);

        Switch doorLightSwitch = findViewById(R.id.doorLightSwitch);
        Switch fenceLightSwitch = findViewById(R.id.fenceLightSwitch);
        Switch gateSwitch = findViewById(R.id.gateSwitch);

        doorLightSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                on("11");
                doorLightIV.setVisibility(View.VISIBLE);
            } else {
                off("11");
                doorLightIV.setVisibility(View.INVISIBLE);
            }
        });

        fenceLightSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                on("12");
                fenceLightIV.setVisibility(View.VISIBLE);
            } else {
                off("12");
                fenceLightIV.setVisibility(View.INVISIBLE);
            }
        });

        gateSwitch.setOnCheckedChangeListener((compoundButton, b) -> {

            new Thread(() -> {

                if (thread != null && thread.isAlive()) {
                    interruptableRunnable.interrupt();
                    while (thread.isAlive());
                }

                int startIndex, endIndex, inc;
                if (b) {
                    on("13");
                    startIndex = 0;
                    endIndex = 20;
                    inc = 1;
                } else {
                    off("13");
                    startIndex = 20;
                    endIndex = 0;
                    inc = -1;
                }

                interruptableRunnable = new InterruptableRunnable() {
                    @Override
                    public void run() {
                        for (int i = startIndex; i != endIndex; i+=inc) {
                            Log.d("TAG-"+i, "" + i);
                            if (this.isInterrupted())
                                break;
                            setGate(i);
                            try { Thread.sleep(50); }
                            catch (InterruptedException ignored) { }
                        }
                    }
                };

                thread = new Thread(interruptableRunnable);
                thread.start();

            }).start();
        });
    }

    private void setGate(int index) {
        if (index < 0 || index > 20) {
            TIMER.cancel();
            TIMER.purge();
        } else {
            gateIV.setImageResource(gateImageResIdes[index]);
        }
    }

    public void on(String pin) {
        sendRequest(mURL + "/on/" + pin + "/");
    }

    private void off(String pin){
        sendRequest(mURL + "/off/" + pin + "/");
    }

    private void sendRequest(String url) {
        new Thread(() -> {
            try {
                URLConnection connection = new URL(url).openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String output;
                while ((output = reader.readLine()) != null) {
                    Log.d("MyTAGmsg:", output);
                }
                reader.close();
            }catch (Throwable ignored){ }
        }).start();
    }
}
