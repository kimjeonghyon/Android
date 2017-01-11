package com.john.android.parkinginfo;

/**
 * Created by jeonghyonkim on 2016. 10. 10..
 */

/*
 * 쓰이는 데아타 structure
 * http://data.seoul.go.kr/ 참조
 * 주차장코드
 * 주차장명
 * 최대주차대수
 * 잔여주차대수
 * 주소
 * 전화
 * 대표명
 * 기관명
 * 경도
 * 위도
 * 거리
 *
 */

public class DataInfo {
    private String 	parkmaster_cd;
    private String 	park_name;
    private int		max_parking_cnt;
    private int 	parking_cnt;
    private String 	park_address;
    private String 	tel_no;
    private String 	owner_name;
    private String 	company_nm;
    private String	lng;
    private String	lat;
    private double	distance;

    public String getParkmaster_cd() {
        return parkmaster_cd;
    }

    public void setParkmaster_cd(String parkmaster_cd) {
        this.parkmaster_cd = parkmaster_cd;
    }

    public String getPark_name() {
        return park_name;
    }

    public void setPark_name(String park_name) {
        this.park_name = park_name;
    }

    public int getMax_parking_cnt() {
        return max_parking_cnt;
    }

    public void setMax_parking_cnt(int max_parking_cnt) {
        this.max_parking_cnt = max_parking_cnt;
    }

    public int getParking_cnt() {
        return parking_cnt;
    }

    public void setParking_cnt(int parking_cnt) {
        this.parking_cnt = parking_cnt;
    }

    public String getPark_address() {
        return park_address;
    }

    public void setPark_address(String park_address) {
        this.park_address = park_address;
    }

    public String getTel_no() {
        return tel_no;
    }

    public void setTel_no(String tel_no) {
        this.tel_no = tel_no;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getCompany_nm() {
        return company_nm;
    }

    public void setCompany_nm(String company_nm) {
        this.company_nm = company_nm;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
