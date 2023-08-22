package org.example.api;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.example.yurim.PlaceInformation;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.example.yurim.Statics.*;

public class KakaoAPI {
    static String x;
    static String y;
    static List<PlaceInformation> placeList = new ArrayList<PlaceInformation>();

    /** rest_api_key 받아오는 함수 **/
    public static void getAPIKey() throws IOException {
        Properties pro = new Properties();
        FileInputStream fis = new FileInputStream(PROPERTIES);
        pro.load(new BufferedInputStream(fis));
        REST_API_KEY = pro.getProperty("REST_API");
    }

    /** 주소로부터 x좌표와 y좌표 정보 받는 함수 **/
    public static void get_X_Y(String arrival) throws IOException {
        //1. Http client 생성
        CloseableHttpClient client = HttpClients.custom().build();

        //2. Request 생성
        HttpUriRequest request = RequestBuilder.get()
                .setUri(GET_ADDRESS_BY_KEYWORD_URL)
                .setHeader("Authorization", REST_API_KEY)
                .addParameter("query", arrival)
                .build();

        //3. Response 회수 (IOException)
        CloseableHttpResponse response = client.execute(request);

        //4. Response string화
        BufferedReader reader = new BufferedReader(new InputStreamReader
                (response.getEntity().getContent()));

        String input;
        StringBuffer responseSb = new StringBuffer();

        while ((input = reader.readLine()) != null) {
            responseSb.append(input);
        }
        reader.close();
        client.close();

        //5. Response로부터 x,y 추출
        JSONObject resJsonObj = new JSONObject(responseSb.toString());
        JSONArray resJsonArr = resJsonObj.getJSONArray("documents");
        JSONObject subJsonObj = new JSONObject(resJsonArr.get(0).toString());
        x = subJsonObj.getString("x");
        y = subJsonObj.getString("y");
    }

    /** x좌표와 y좌표를 이용해 근방의 장소 정보 출력 함수 **/
    public static void searchPlace(String arrival, int radius) throws IOException {
        //1. Http client 생성
        CloseableHttpClient client = HttpClients.custom().build();

        //2. Request 생성
        HttpUriRequest request = RequestBuilder.get()
                .setUri(GET_ADDRESS_BY_CATEGORY_URL)
                .setHeader("Authorization", REST_API_KEY)
                .addParameter("query", "약국")
                .addParameter("category_group_code", "PM9")
                .addParameter("x", x)
                .addParameter("y", y)
                .addParameter("radius", String.valueOf(radius))
                .addParameter("size", String.valueOf(10))
                .build();

        //3. Response 회수 (IOException)
        CloseableHttpResponse response = client.execute(request);

        //4. Response string화
        BufferedReader reader = new BufferedReader(new InputStreamReader
                (response.getEntity().getContent()));

        String input;
        StringBuffer responseSb = new StringBuffer();

        while ((input = reader.readLine()) != null) {
            responseSb.append(input);
        }
        reader.close();
        client.close();

        //5. Response로부터 place 정보 추출
        /*
         * 장소 URL : place_url
         * 상호명 : place_name
         * 전화번호 : phone
         * 거리 : distance
         */

        JSONObject resJsonObj = new JSONObject(responseSb.toString());
        JSONArray resJsonArr = resJsonObj.getJSONArray("documents");
        int cnt = 0;
        for (int i = 0; i < 10; i++) {
            JSONObject subJsonObj = resJsonArr.getJSONObject(i);
            String placeUrl = subJsonObj.getString("place_url");
            String placeName = subJsonObj.getString("place_name");
            String addressName = subJsonObj.getString("address_name");
            String phone = subJsonObj.getString("phone");
            String distance = subJsonObj.getString("distance");
            PlaceInformation place = new PlaceInformation(placeUrl, placeName, addressName, phone, distance);
            placeList.add(place);
        }

        //6. place 정보 출력
        System.out.println();
        System.out.println("입력한 위치 키워드: " + arrival);
        System.out.println("검색 반경: " + radius / 1000d + "km");

        System.out.println();
        System.out.println("**약국 검색 결과**");

        for (PlaceInformation place : placeList) {
            System.out.println(place.printPlaceInfo());
            System.out.println("--------------------------------");
        }

    }

    /** kakaoMap url을 통해 브라우저 실행 함수 **/
    public static void runUrl() throws IOException, URISyntaxException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("kakomap URL(장소URL):");
            String URL = br.readLine();
            if (URL.equals("exit")) {
                break;
            } else {
                Desktop.getDesktop().browse(new URI(URL));
            }
        }
        System.out.println("프로그램 종료");
    }
}
