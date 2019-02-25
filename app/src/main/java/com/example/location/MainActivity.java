package com.example.location;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    Button click;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.lattitude);
        click = (Button)findViewById(R.id.click);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        LocationManager locMan;
        String[] mockProviders = {LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER};

        try {
            locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            for (String p : mockProviders) {
                if (p.contentEquals(LocationManager.GPS_PROVIDER))
                    locMan.addTestProvider(p, false, false, false, false, true, true, true, 1,
                            android.hardware.SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
                else
                    locMan.addTestProvider(p, false, false, false, false, true, true, true, 1,
                            android.hardware.SensorManager.SENSOR_STATUS_ACCURACY_LOW);

                locMan.setTestProviderEnabled(p, true);
                locMan.setTestProviderStatus(p, android.location.LocationProvider.AVAILABLE, Bundle.EMPTY,
                        java.lang.System.currentTimeMillis());
            }
        } catch (Exception ignored) {
            // here you should show dialog which is mean the mock location is not enable
        }
        // Keep track of user location.
        // Use callback/listener since requesting immediately may return null location.
        // IMPORTANT: TO GET GPS TO WORK, MAKE SURE THE LOCATION SERVICES ON YOUR PHONE ARE ON.
        // FOR ME, THIS WAS LOCATED IN SETTINGS > LOCATION.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, new Listener());
        // Have another for GPS provider just in case.
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, new Listener());
        // Try to request the location immediately
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (location != null) {
            handleLatLng(location.getLatitude(), location.getLongitude());
        }
        Toast.makeText(getApplicationContext(),
                "Trying to obtain GPS coordinates. Make sure you have location services on.",
                Toast.LENGTH_SHORT).show();
    }


    public static Boolean isMockLocationEnabled(Context context) {
        return !Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).equals("0");
    }

    /**
     * For Build.VERSION.SDK_INT >= 18 i.e. JELLY_BEAN_MR2
     * Check if the location recorded is a mocked location or not
     *
     * @param location Pass Location object received from the OS's onLocationChanged() callback
     * @return Returns a boolean indicating if the received location is mocked
     */

    public static boolean isMockLocation(Location location) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && location != null && location.isFromMockProvider();


    }

    private void handleLatLng(double latitude, double longitude) {

    }

    private class Listener implements LocationListener {

        public void onLocationChanged(Location location) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                textView.append("long:" + location.getLatitude() + " - lat:" + location.getLongitude() + " - isMock :" + location.isFromMockProvider() + "\n");
            }
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

    }
}

