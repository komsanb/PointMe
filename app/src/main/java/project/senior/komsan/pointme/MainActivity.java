package project.senior.komsan.pointme;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;

import java.util.ArrayList;


public class MainActivity extends FragmentActivity{

    GoogleMap mMap;
    Marker mMarker;
    LocationManager lm;
    double lat, lng;
    LocationListener listener = new LocationListener() {
        public void onLocationChanged(Location loc) {
            LatLng coordinate = new LatLng(loc.getLatitude()
                    , loc.getLongitude());
            lat = loc.getLatitude();
            lng = loc.getLongitude();

            if (mMarker != null)
                mMarker.remove();

            mMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lng)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    coordinate, 16));
        }

        public void onStatusChanged(String provider, int status
                , Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };
    Direction md;
    LatLng startPosition = new LatLng(13.687140112679154, 100.53525868803263);
    LatLng endPosition = new LatLng(13.683660045847258, 100.53900808095932);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMap = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();



        Resources resources = this.getResources();
        final Drawable currentLocationIcon = resources.getDrawable(R.drawable.current_location_icon);
        Drawable searchIcon = resources.getDrawable(R.drawable.search_icon);
        Drawable favoriteIcon = resources.getDrawable(R.drawable.favorite_icon);
        Drawable directionIcon = resources.getDrawable(R.drawable.direction_icon);

        final TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        final TabHost.TabSpec currentLocationTab = tabHost.newTabSpec("current location");
        currentLocationTab.setIndicator("", currentLocationIcon);
        currentLocationTab.setContent(R.id.currentLocationTab);
        tabHost.addTab(currentLocationTab);

        final TabHost.TabSpec searchTab = tabHost.newTabSpec("search");
        searchTab.setIndicator("", searchIcon);
        searchTab.setContent(R.id.searchTab);
        tabHost.addTab(searchTab);

        TabHost.TabSpec favoriteTab = tabHost.newTabSpec("favorite");
        favoriteTab.setIndicator("", favoriteIcon);
        favoriteTab.setContent(R.id.favoriteTab);
        tabHost.addTab(favoriteTab);

        TabHost.TabSpec directionTab = tabHost.newTabSpec("direction");
        directionTab.setIndicator("", directionIcon);
        directionTab.setContent(R.id.directionTab);
        tabHost.addTab(directionTab);


        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {


                if (tabId.equals("current location")){

                }

                if (tabId.equals("search")){

                    Intent intent = new Intent(getApplication(), Search.class);
                    startActivity(intent);
                }

//                if (tabId.equals("favorite")){
//
//                    Intent intent = new Intent(getApplication(), Favorite.class);
//                    startActivity(intent);
//                }
//
                if (tabId.equals("direction")) {
                    if (android.os.Build.VERSION.SDK_INT > 9) {
                        StrictMode.ThreadPolicy policy
                                = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                    }

                    md = new Direction();
                    mMap = ((SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map)).getMap();

                    LatLng coordinates = new LatLng(13.685400079263206, 100.537133384495975);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 16));

                    mMap.addMarker(new MarkerOptions().position(startPosition).title("Start"));
                    mMap.addMarker(new MarkerOptions().position(endPosition).title("End"));

                    Document doc = md.getDocument(startPosition
                            , endPosition, Direction.MODE_DRIVING);
                    int duration = md.getDurationValue(doc);
                    String distance = md.getDistanceText(doc);
                    String start_address = md.getStartAddress(doc);
                    String copy_right = md.getCopyRights(doc);

                    ArrayList<LatLng> directionPoint = md.getDirection(doc);
                    PolylineOptions rectLine = new PolylineOptions().width(10).color(Color.BLUE).geodesic(true);

                    for (int i = 0; i < directionPoint.size(); i++) {
                        rectLine.add(directionPoint.get(i));
                    }

                    mMap.addPolyline(rectLine);
                }
            }
        });
    }

    public void onResume() {
        super.onResume();

        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        boolean isNetwork =
                lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean isGPS =
                lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(isNetwork) {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER
                    , 5000, 10, listener);
            Location loc = lm.getLastKnownLocation(
                    LocationManager.NETWORK_PROVIDER);
            if(loc != null) {
                lat = loc.getLatitude();
                lng = loc.getLongitude();
            }
        }

        if(isGPS) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER
                    , 5000, 10, listener);
            Location loc = lm.getLastKnownLocation(
                    LocationManager.GPS_PROVIDER);
            if(loc != null) {
                lat = loc.getLatitude();
                lng = loc.getLongitude();
            }
        }
    }

    public void onPause() {
        super.onPause();
        lm.removeUpdates(listener);
    }

}
