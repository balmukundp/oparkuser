package in.user.parking;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Daffodil on 7/28/2018.
 */

@SuppressWarnings("unused")
public class NearestParking {
    @SerializedName("availableSlots")
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
    @SerializedName("distance1")
    private String distance1;
    @SerializedName("cityId")
    private String cityId;
    @SerializedName("vendorId")
    private String vendorId;

    @SerializedName("text1")
    private String text1;

    @SerializedName("text2")
    private String text2;

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

    public String getDistance1() {
        return distance1;
    }

    public void setDistance1(String distance1) {
        this.distance1 = distance1;
    }

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



    /*String vendorId,distance,timetoTravel,parkingId,latitude,longitude,availableSlots,distance1,address,cityId;

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTimetoTravel() {
        return timetoTravel;
    }

    public void setTimetoTravel(String timetoTravel) {
        this.timetoTravel = timetoTravel;
    }

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(String availableSlots) {
        this.availableSlots = availableSlots;
    }

    public String getDistance1() {
        return distance1;
    }

    public void setDistance1(String distance1) {
        this.distance1 = distance1;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }*/
}
