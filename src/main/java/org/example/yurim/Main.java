package org.example.yurim;

import org.example.api.KakaoAPI;

import java.io.*;
import java.net.URISyntaxException;


public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {

        //rest_api_key 받아오는 함수
        KakaoAPI.getAPIKey();

        //기본 입력
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("위치 키워드를 입력하세요: ");
        String arrival = br.readLine();
        System.out.print("검색 반경을 입력하세요(1000:1km): ");
        int radius = Integer.parseInt(br.readLine());

        //주소로부터 x좌표와 y좌표 정보 받는 함수
        KakaoAPI.get_X_Y(arrival);

        //x좌표와 y좌표를 이용해 근방의 장소 정보 출력 함수
        KakaoAPI.searchPlace(arrival, radius);

        //kakaoMap url을 통해 브라우저 실행 함수
        KakaoAPI.runUrl();

    }
}
