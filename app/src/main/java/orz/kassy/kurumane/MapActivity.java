package orz.kassy.kurumane;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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

import butterknife.ButterKnife;
import butterknife.OnClick;
import orz.kassy.kurumane.flow.LatLngName;

public class MapActivity extends AppCompatActivity
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

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private static MapActivity sSelf;
    private String mTappedMarkerTitle = "";

    Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        sSelf = this;
        // Google maps api v2 の各種設定
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        // 地図APIの現在地ボタン これないと現在地にポインタでない
        mMap.setMyLocationEnabled(true);

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnCameraChangeListener(this);

        // 現在地追跡
        mMap.setOnMyLocationChangeListener(this);

        // マーカータップ
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(mTappedMarkerTitle.equals(marker.getTitle())) {
                    //Toast.makeText(getApplicationContext(), "マーカータップ", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(sSelf, GsDetailActivity.class);
                    startActivity(intent);
                } else {
                    mTappedMarkerTitle = marker.getTitle();
                }
                return false;
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        sSelf = this;

        ButterKnife.inject(this);
    }

    /**
     * 現在地に地図を移動させる
     */
    private void moveToCurrentPoint() {
        Location loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LatLng curr = new LatLng(loc.getLatitude(), loc.getLongitude());
        CameraPosition start =
                new CameraPosition.Builder().target(curr)
                        .zoom(15.5f)
                        .bearing(0)
                        .build();
        changeCamera(CameraUpdateFactory.newCameraPosition(start), null);
    }

    private void changeCamera(CameraUpdate update, GoogleMap.CancelableCallback callback) {
        mMap.animateCamera(update, callback);
    }

    @Override
    public void onResume() {
        super.onResume();

        // GoogleAPIクライアント接続
        mGoogleApiClient.connect();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                moveToCurrentPoint();
            }
        }, 1000);

    }


    @Override
    public void onConnected(Bundle bundle) {
        moveToCurrentPoint();
        new SearchConvenienceTask().execute();
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
        moveToCurrentPoint();
        new SearchConvenienceTask().execute();
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

        private static final String TAG = "";
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
                Map<?,?> rootMap = mapper.readValue(json, Map.class);
                ArrayList nextArray = (ArrayList)rootMap.get("results");
                mLatlngList = new ArrayList<LatLngName>();

                for(int i =0; i < nextArray.size(); i++) {
                    Map<?, ?> thirdMap = (Map<?, ?>) nextArray.get(i);
                    Map<?, ?> forthMap = (Map<?, ?>) ((Map<?, ?>) thirdMap.get("geometry")).get("location");
                    Double lat = (Double) forthMap.get("lat");
                    Double lng = (Double) forthMap.get("lng");
                    String name = (String)thirdMap.get("name");
                    Log.i(TAG, "lat=" + lat + " lng=" + lng);
                    mLatlngList.add(new LatLngName(lat, lng, name));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return 0l;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
            for(int i=0; i<mLatlngList.size()-3; i+=3) {
                sSelf.mMap.addMarker(new MarkerOptions()
                        .position(mLatlngList.get(i).getLatLng())
                        .title("ガソリンスタンド"+i)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.gas128)));
                sSelf.mMap.addMarker(new MarkerOptions()
                        .position(mLatlngList.get(i+1).getLatLng())
                        .title("ガソリンスタンド"+(i+1))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.gas125)));
                if(mLatlngList.get(i+2) == null)return;
                sSelf.mMap.addMarker(new MarkerOptions()
                        .position(mLatlngList.get(i + 2).getLatLng())
                        .title("ガソリンスタンド" + (i + 2))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.gas122)));
            }
        }
    }

}
