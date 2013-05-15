package com.comsysto.findbuddies.android.activity;

import android.os.Bundle;
import com.comsysto.dalli.android.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * Created with IntelliJ IDEA.
 * User: stefandjurasic
 * Date: 14.03.13
 * Time: 15:42
 * To change this template use File | Settings | File Templates.
 */
public class BuddiesMapActivity extends AbstractActivity {

    static final  LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buddies_map);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();

        if (map!=null){
            Marker hamburg = map.addMarker(new MarkerOptions().position(HAMBURG)
                    .title("Hamburg"));
            Marker kiel = map.addMarker(new MarkerOptions()
                    .position(KIEL)
                    .title("Kiel")
                    .snippet("Kiel is cool")
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.androidmarker)));
        }

    }
}
