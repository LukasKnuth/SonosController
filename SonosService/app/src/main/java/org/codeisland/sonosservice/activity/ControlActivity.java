package org.codeisland.sonosservice.activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.codeisland.sonosservice.BuildConfig;
import org.codeisland.sonosservice.R;
import org.codeisland.sonosservice.service.ControlService;

public class ControlActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnStartStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control_activity);

        TextView serverAddress = (TextView) findViewById(R.id.server_address);
        btnStartStop = (Button) findViewById(R.id.start_stop_service);

        serverAddress.setText(BuildConfig.BASE_URL);
        btnStartStop.setOnClickListener(this);
        btnStartStop.setText(isServiceRunning() ? R.string.btn_service_stop : R.string.btn_service_start);
    }

    @Override
    public void onClick(View v) {
        if (v == btnStartStop){
            Intent serviceName = new Intent(this, ControlService.class);
            if (isServiceRunning()){
                if (!stopService(serviceName)) {
                    Toast.makeText(this, R.string.error_stopping_service, Toast.LENGTH_SHORT).show();
                }
                btnStartStop.setText(R.string.btn_service_start);
            } else {
                if (startService(serviceName) == null){
                    // Couldn't start server
                    Toast.makeText(this, R.string.error_starting_service, Toast.LENGTH_SHORT).show();
                    btnStartStop.setText(R.string.btn_service_start);
                } else {
                    btnStartStop.setText(R.string.btn_service_stop);
                }
            }
        }
    }

    /**
     * Check whether the service is already running.
     * @see <a href="http://stackoverflow.com/a/5921190/717341">SO answer</a>
     */
    public boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        String serviceName = ControlService.class.getName();
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
