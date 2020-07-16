package in.user.login;

/**
 * Created by Daffodil on 7/27/2018.
 */

public class LoginModel {

    String userId,userRole,userName,userContactNo,vendorId,vendorName,city,cityId,isUserVerified;

    public String getIsUserVerified() {
        return isUserVerified;
    }

    public void setIsUserVerified(String isUserVerified) {
        this.isUserVerified = isUserVerified;
    }

    public String getAgentId() {
        return userId;
    }

    public void setAgentId(String agentId) {
        this.userId = agentId;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserContactNo() {
        return userContactNo;
    }

    public void setUserContactNo(String userContactNo) {
        this.userContactNo = userContactNo;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
}
