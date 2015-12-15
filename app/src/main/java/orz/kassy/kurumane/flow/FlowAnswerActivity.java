package orz.kassy.kurumane.flow;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import orz.kassy.kurumane.R;

public class FlowAnswerActivity extends AppCompatActivity
        implements
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnCameraChangeListener,
        LocationListener,
        GoogleMap.OnMyLocationChangeListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        CompoundButton.OnCheckedChangeListener,
        View.OnClickListener,
        SensorEventListener {


    private static final String TAG = "FlowAnswerActivity";

    private static FlowAnswerActivity sSelf;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_answer);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        // Google maps api v2 の各種設定
        mMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        // 地図APIの現在地ボタン これないと現在地にポインタでない
        mMap.setMyLocationEnabled(true);

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnCameraChangeListener(this);

        // 現在地追跡
        mMap.setOnMyLocationChangeListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        new SearchConvenienceTask().execute();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationChange(Location location) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    /**
     * AsyncTask まだ実験
     */
    private static class SearchConvenienceTask extends AsyncTask<URL, Integer, Long> {

        // コンビニの緯度経度リスト
        private ArrayList<LatLngName> mLatlngList;

        protected Long doInBackground(URL... urls) {
            InputStream is;

            // JSONのダウンロード（一旦ファイルに保存するのは非効率かもしれない）
            try {
                // URL生成（現在地を元に）
                Location loc = LocationServices.FusedLocationApi.getLastLocation(sSelf.mGoogleApiClient);
                double lat = loc.getLatitude();
                double lng = loc.getLongitude();
                StringBuilder urlStrBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/search/json");
                urlStrBuilder.append("?location=" + lat + "," + lng);
                urlStrBuilder.append("&sensor=true&rankby=distance&types=convenience_store&key=AIzaSyDLY9COvhnmysMUGp6ps0ADbbx4OFY0JxM");
                URL u = new URL(urlStrBuilder.toString());

                HttpURLConnection con = (HttpURLConnection) u.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                is = new BufferedInputStream(con.getInputStream());

                // Path and File where to download the APK
                String path = Environment.getExternalStorageDirectory() + "/kurumane/";
                String fileName = "2.json";
                File dir = new File(path);
                dir.mkdirs(); // creates the download directory if not exist
                File outputFile = new File(dir, fileName);
                FileOutputStream fos = new FileOutputStream(outputFile);

                // Save file from URL to download directory on external storage
                int bytesRead = -1;
                byte[] buffer = new byte[1024];
                while ((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
                fos.flush();
                fos.close();
                is.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // JSONパース
            try {
                // ファイル読み込み
                FileInputStream fileInputStream;
                String path = Environment.getExternalStorageDirectory() + "/kurumane/";
                String fileName = "2.json";
                File dir = new File(path);
                File inputFile = new File(dir, fileName);
                fileInputStream = new FileInputStream(inputFile);
                byte[] readBytes = new byte[fileInputStream.available()];
                fileInputStream.read(readBytes);
                String json = new String(readBytes);

                // JSONのパース with Jackson
                ObjectMapper mapper = new ObjectMapper();
                Object root = mapper.readValue(json, Object.class);
                Map<?, ?> rootMap = mapper.readValue(json, Map.class);
                ArrayList nextArray = (ArrayList) rootMap.get("results");
                mLatlngList = new ArrayList<LatLngName>();

                for (int i = 0; i < nextArray.size(); i++) {
                    Map<?, ?> thirdMap = (Map<?, ?>) nextArray.get(i);
                    Map<?, ?> forthMap = (Map<?, ?>) ((Map<?, ?>) thirdMap.get("geometry")).get("location");
                    Double lat = (Double) forthMap.get("lat");
                    Double lng = (Double) forthMap.get("lng");
                    String name = (String) thirdMap.get("name");
                    Log.i(TAG, "lat=" + lat + " lng=" + lng);
                    mLatlngList.add(new LatLngName(lat, lng, name));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return 0l;
        }

    }
}
