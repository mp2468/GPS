package com.example.mark.sensorapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener {

    private LocationManager locationmanager_;

    private SensorManager sensormanager_;
    private Sensor accelerometer_;
    private EditText acclxe, acclye, acclze, latitudee, longitudee;
    private final int DELAY = 100;
    private Handler myHandler = new Handler();

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        sensormanager_ = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer_ = sensormanager_.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //now register with sensor service
        sensormanager_.registerListener(this, accelerometer_, SensorManager.SENSOR_DELAY_NORMAL, DELAY);

        //acclxe = (EditText) findViewById(R.id.acclxe);
        //acclye = (EditText) findViewById(R.id.acclye);
        //acclze = (EditText) findViewById(R.id.acclze);
        latitudee = (EditText) findViewById(R.id.eLatitude);
        longitudee = (EditText) findViewById(R.id.eLongitude);

    }

    @Override
    public void onLocationChanged(Location location) {

        //in here need to do some stuff to make it output, also threadsafe

        Location mySensor = location;
        myHandler.post(new GPSWork(location));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class GPSWork implements Runnable{
        private Location event_;

        public GPSWork(Location event)
        {
            event_ = event;
        }

        public void run() {

            double Lat = event_.getLatitude();
            double Lon = event_.getLongitude();

            latitudee.setText(new Double(Lat).toString());
            longitudee.setText(new Double(Lon).toString());

        }


    }

/*
    private class AcclWork implements Runnable {
        private SensorEvent event_;

        public AcclWork(SensorEvent event) {
            event_ = event;
        }

        @Override
        public void run() {
            double acclx = event_.values[0];
            double accly = event_.values[0];
            double acclz = event_.values[0];


            acclxe.setText(new Double(acclx).toString());
            acclye.setText(new Double(accly).toString());
            acclze.setText(new Double(acclz).toString());

        }
*/


    @Override
    protected void onResume() {
        super.onResume();
        sensormanager_ = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer_ = sensormanager_.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //now register with sensor service
        sensormanager_.registerListener(this, accelerometer_, SensorManager.SENSOR_DELAY_NORMAL, DELAY);

        locationmanager_ = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationmanager_.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 0, this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensormanager_.unregisterListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            /*
            double acclx = event.values[0];
            double accly = event.values[0];
            double acclz = event.values[0];


            acclxe.setText(new Double(acclx).toString());
            acclye.setText(new Double(accly).toString());
            acclze.setText(new Double(acclz).toString());
            */
           // myHandler.post(new AcclWork(event));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
