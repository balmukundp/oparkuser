package in.user;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Paul on 9/7/15.
 */
public class ClusterMarkerLocation implements ClusterItem {

    private LatLng position;
    String title;

    @SerializedName("availableSlot")
    private String mAvailableSlots;
    @SerializedName("distance")
    private String mDistance;
    @SerializedName("latitude")
    private String mLatitude;
    @SerializedName("longitude")
    private String mLongitude;
    @SerializedName("timetoTravel")
    private String mTimetoTravel;
    @SerializedName("address")
    private String address;

    @SerializedName("parkingId")
    private String parkingId;
//    @SerializedName("distance1")
//    private String distance1;
    @SerializedName("cityId")
    private String cityId;
    @SerializedName("vendorId")
    private String vendorId;

    @SerializedName("text1")
    private String text1;

    @SerializedName("text2")
    private String text2;

    public ClusterMarkerLocation() {

    }


    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

//    public String getDistance1() {
//        return distance1;
//    }
//
//    public void setDistance1(String distance1) {
//        this.distance1 = distance1;
//    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getAvailableSlots() {
        return mAvailableSlots;
    }

    public void setAvailableSlots(String availableSlots) {
        mAvailableSlots = availableSlots;
    }

    public String getDistance() {
        return mDistance;
    }

    public void setDistance(String distance) {
        mDistance = distance;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public void setLatitude(String latitude) {
        mLatitude = latitude;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public void setLongitude(String longitude) {
        mLongitude = longitude;
    }

    public String getTimetoTravel() {
        return mTimetoTravel;
    }

    public void setTimetoTravel(String timetoTravel) {
        mTimetoTravel = timetoTravel;
    }

    public ClusterMarkerLocation(LatLng latLng) {
        position = latLng;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    public ClusterMarkerLocation(String title, LatLng position) {
        this.position = position;
        this.title = title;
    }

    public ClusterMarkerLocation(LatLng position, String title, String mAvailableSlots, String mDistance, String mTimetoTravel,
                                   String parkingId , String vendorId, String text1, String text2, String mLatitude, String mLongitude) {
        this.position = position;
        this.title = title;
        this.mAvailableSlots = mAvailableSlots;
        this.mDistance = mDistance;
        this.mTimetoTravel = mTimetoTravel;
        this.parkingId = parkingId;
        this.vendorId = vendorId;
        this.text1 = text1;
        this.text2 = text2;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }
}
