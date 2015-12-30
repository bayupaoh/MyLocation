package id.or.codelabs.mylocation;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private LocationManager location;
    TextView latitude;
    TextView longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //dekslarikan widget
        latitude = (TextView) findViewById(R.id.latitude);
        longitude = (TextView) findViewById(R.id.longitude);

        location = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        String bestProvider = location.getBestProvider(criteria, true);

        //aksi bila user tidak mengaktifkan GPS
        if ((!location.isProviderEnabled(LocationManager.GPS_PROVIDER))) {
            showSettingAlertGps();
        }

        //aksi ini hanya untuk Android M / API 23 untuk masalah Permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }

        //mengambil lokasi terbaru
        location.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        Location locationNow = location.getLastKnownLocation(bestProvider);

        if (locationNow != null) {
            onLocationChanged(locationNow);
        }

        //request update lokasi  tiap 1000 ms


    }

    public void showSettingAlertGps() {

        //deklarasikan alert
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        // title Alertnya
        alertDialog.setTitle("GPS Setting");
        // pesan alert
        alertDialog.setMessage("GPS is not active. What do you want to setting ?");

        alertDialog.setPositiveButton("Setting", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //arahkan user untuk mengaktifkan lokasi
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
        });

    }

    public void tampilkanLokasi(Location lokasi){
        //getLatitude dan getLongitude
        Double longitude = lokasi.getLongitude();
        Double latitude = lokasi.getLongitude();

        //tampilkan dilayar
        this.latitude.setText("Latitude :"+String.valueOf(latitude));
        this.longitude.setText("Longitude :"+String.valueOf(longitude));

    }

    //method bila lokasi berubah
    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(getApplicationContext(),"Lokasi telah berubah",Toast.LENGTH_LONG).show();
        tampilkanLokasi(location);
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
