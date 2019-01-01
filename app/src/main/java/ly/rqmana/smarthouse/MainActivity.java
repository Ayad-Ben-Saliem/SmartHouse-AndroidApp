package ly.rqmana.smarthouse;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    private final static String mURL = "https://smarthouse.serveo.net/smarthouseserver";

    EditText editText;
    Switch mySwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        mySwitch = findViewById(R.id.onOffswitch);

        mySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.d("MyTAG: onCCL", "" + isChecked);
            if (editText.getText().toString().isEmpty()){
                Toast.makeText(MainActivity.this, "أدخل رقم المنفذ أولا", Toast.LENGTH_LONG).show();
            } else {
                String pin = editText.getText().toString();
                Log.d("MyTAG: pin", pin);
                if (isChecked) {
                    on(pin);
                } else {
                    off(pin);
                }
            }
        });
    }

    public void on(final String pin) {
        Toast.makeText(this, "in on method", Toast.LENGTH_LONG).show();
        Log.d("MyTAG", "onnnnnnnnnn");
        new Thread(() -> {
            try {
                URL url = new URL(mURL + "/on/"+ pin + "/");
                URLConnection connection = url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    Log.d("MyTAGmsg:", inputLine);
                }
                in.close();
            }catch (Throwable t){
                throw new RuntimeException(t);
            }
        }).start();
    }

    private void off(final String pin){
        new Thread(() -> {
            try {
                new URL(mURL + "/off/" + pin + "/").openConnection();
            }catch (Throwable t){
                throw new RuntimeException(t);
            }
        }).start();
    }
}
