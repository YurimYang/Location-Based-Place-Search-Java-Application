package org.example.yurim;

public class PlaceInformation {
    String place_url; //장소 URL
    String place_name; //상호명
    String address_name; //주소
    String phone; //전화번호
    String distance; //거리(km)

    public PlaceInformation(String place_url, String place_name,String address_name, String phone, String distance){
        this.place_url = place_url;
        this.place_name = place_name;
        this.address_name = address_name;
        this.phone = phone;
        this.distance = distance;
    }


    public String printPlaceInfo(){
        return "장소 URL(지도 위치): " + this.place_url + "\n" +
                "상호명: " + this.place_name + "\n" +
                "주소: " + this.address_name + "\n" +
                "전화번호: " + this.phone + "\n" +
                "거리(km): " + (Integer.parseInt(this.distance) / 1000d) + "km";
    }
}
