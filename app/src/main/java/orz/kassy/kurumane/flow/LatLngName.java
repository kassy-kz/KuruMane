package orz.kassy.kurumane.flow;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by kashimoto on 2015/10/17.
 */
public class LatLngName {
    private final double mLatitude;
    private final double mLongitude;
    private final String mName;

    public LatLngName(double latitude, double longitude, String name) {
        mLatitude = latitude;
        mLongitude = longitude;
        mName = name;
    }

    public String getName () {
        return mName;
    }

    public LatLng getLatLng() {
        return new LatLng(mLatitude, mLongitude);
    }
}

