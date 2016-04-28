package test.wifi_rssi.com.wifi_rssi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText point;
    String ap_point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        point = (EditText)findViewById(R.id.point);
        Button begin = (Button) findViewById(R.id.button_begin);

        long start = System.currentTimeMillis();
        final long end = start + 40*1000;

        begin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int count = 0;
                ap_point = point.getText().toString();
                while (System.currentTimeMillis() < end){
                    WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                    wifiManager.startScan();
                    List<ScanResult> apList = wifiManager.getScanResults();
                    Log.d("size", String.valueOf(apList.size()));
                    Log.d("counter", String.valueOf(count++));
                    writeToSDFile(apList, ap_point);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(MainActivity.this, "Recorded!", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void writeToSDFile(List<ScanResult> apList, String ap_point){

        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File (root.getAbsolutePath() + "/download");
        dir.mkdirs();
        File file = new File(dir, "LIB_FIRST_FLOOR.txt");

        try {
            FileOutputStream os = new FileOutputStream(file, true);
            OutputStreamWriter out = new OutputStreamWriter(os);
            for(int i = 0; i < apList.size(); i++){
                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();

                out.write(ap_point + "," + apList.get(i).BSSID + "," + apList.get(i).level + "\n");
                Log.d("FILE", ap_point + "," + apList.get(i).BSSID + "," + apList.get(i).level);
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
