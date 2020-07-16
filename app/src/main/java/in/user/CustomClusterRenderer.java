package in.user;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: Anton Shkurenko (tonyshkurenko)
 * Project: ClusterManagerDemo
 * Date: 6/7/16
 * Code style: SquareAndroid (https://github.com/square/java-code-styles)
 * Follow me: @tonyshkurenko
 */
public class CustomClusterRenderer extends DefaultClusterRenderer<ClusterMarkerLocation> {

    private final IconGenerator mClusterIconGenerator;
    private final Context mContext;
    private static final int[] CLUSTER_THRESHOLDS = {10, 30, 60, 100};

    private List<ClusterMarkerLocation> nearestParkingArrayList = new ArrayList<>();

    public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<ClusterMarkerLocation> clusterManager) {
        super(context, map, clusterManager);


        mContext = context;
        mClusterIconGenerator = new IconGenerator(mContext.getApplicationContext());


    }

    @Override
    protected void onBeforeClusterItemRendered(ClusterMarkerLocation item, MarkerOptions markerOptions) {

        String availableSlot = item.getAvailableSlots();
        ClusterMarkerLocation pos = item;

        if (availableSlot.equals("2Wheeler:0") || availableSlot.equals("4Wheeler:0") || availableSlot.equals("2Wheeler:0,4Wheeler:0")) {
            final BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
            markerOptions.icon(markerDescriptor).snippet(item.title);
        } else {
            final BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
            markerOptions.icon(markerDescriptor).snippet(item.title);
        }


        //   final BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
//
//    markerOptions.icon(markerDescriptor).snippet(item.title);
    }


//    @Override
//    protected boolean shouldRenderAsCluster(Cluster<ClusterMarkerLocation> cluster) {
//        //start clustering if at least 2 items overlap
//        return cluster.getSize() >= 2;
//    }

    @Override
    protected void onBeforeClusterRendered(Cluster<ClusterMarkerLocation> cluster, MarkerOptions markerOptions) {


        mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_circle));

        mClusterIconGenerator.setTextAppearance(R.style.MyTheme);

        final Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));

    }
}