package in.mywebdomain.infocollect.entities;

import android.content.ContentValues;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TheClient implements Serializable {
    private final String name;
    private final String pno;
    private final String dob;
    private final String lat;
    private final String lng;
    private final String address;

    public TheClient(String name, String pno, String dob, String lat, String lng, String address) {
        this.name = name;
        this.pno = pno;
        this.dob = dob;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
    }

    public String getFName() {
        return name;
    }

    public String getPno() {
        return pno;
    }

    public String getDob() {
        return dob;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getAddress() {
        return address;
    }

    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        cv.put("fname", getFName());
        cv.put("pno", getPno());
        cv.put("address", getAddress());
        cv.put("dob", getDob());
        cv.put("lat", getLat());
        cv.put("lng", getLng());
        return cv;
    }

    public Map<String,Object> getAsMap() {
        Map<String, Object> asMap = new HashMap<>();
        asMap.put("name", name);
        asMap.put("pno", pno);
        asMap.put("address", address);
        asMap.put("dob", dob);
        asMap.put("lat", lat);
        asMap.put("lng", lng);
        return asMap;
    }
}
