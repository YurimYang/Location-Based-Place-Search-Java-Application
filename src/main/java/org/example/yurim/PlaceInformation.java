package org.example.yurim;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PlaceInformation {
    String place_url; //장소 URL
    String place_name; //상호명
    String address_name; //주소
    String phone; //전화번호
    String distance; //거리(km)
}
