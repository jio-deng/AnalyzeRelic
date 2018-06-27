package com.opengl.deng.testnewrelic.analyzesdk.bean;

/**
 * @Description user location
 * Created by deng on 2018/6/27.
 */
public class LocationBean {
    private double latitude;
    private double longitude;
    private String cityName;
    private String cityCode;
    private String adCode;
    private String address;
    private String district;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Override
    public String toString() {
        return "{" + "latitude : " + latitude + ","
                + "longitude : " + longitude + ","
                + "cityName : " + cityName + ","
                + "cityCode : " + cityCode + ","
                + "adCode : " + adCode + ","
                + "address : " + address + ","
                + "district : " + district + "}";
    }
}
