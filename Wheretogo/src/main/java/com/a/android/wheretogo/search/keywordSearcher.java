package com.a.android.wheretogo.search;

import android.content.Context;
import android.os.AsyncTask;

import com.a.android.wheretogo.item.Item5;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class keywordSearcher {

    // http://dna.daum.net/apis/local
	//public static final String DAUM_MAPS_LOCAL_KEYWORD_SEARCH_API_FORMAT = "https://dapi.kakao.com/v2/local/search/keyword.json?query=%s&location=%f,%f&radius=%d&page=%d&apikey=%s";
	//public static final String DAUM_MAPS_LOCAL_KEYWORD_SEARCH_API_FORMAT = "https://apis.daum.net/local/v1/search/keyword.json?query=%s&location=%f,%f&radius=%d&page=%d&apikey=%s";
	public static final String KAKAO_LOCAL_KEYWORD_SEARCH_API_FORMAT = "https://dapi.kakao.com/v2/local/search/keyword.json?query=%s&category_group_code=%s&x=%f&y=%f&radius=%d&page=%d";
	//public static final String DAUM_MAPS_LOCAL_CATEGORY_SEARCH_API_FORMAT = "https://apis.daum.net/local/v1/search/category.json?code=%s&location=%f,%f&radius=%d&page=%d&apikey=%s";
	public static final String KAKAO_LOCAL_CATEGORY_SEARCH_API_FORMAT = "https://dapi.kakao.com/v2/local/search/category.json?category_group_code=%s&x=%f&y=%f&radius=%d&page=%d";

	//String postParameters= "?x=" + lon + "&y=" + lat;

	/** category codes
	MT1 대형마트
	CS2 편의점
	PS3 어린이집, 유치원
	SC4 학교
	AC5 학원
	PK6 주차장
	OL7 주유소, 충전소
	SW8 지하철역
	BK9 은행
	CT1 문화시설
	AG2 중개업소
	PO3 공공기관
	AT4 관광명소
	AD5 숙박
	FD6 음식점
	CE7 카페
	HP8 병원
	PM9 약국
	 */

	public static int page2=1;
	private static final String HEADER_NAME_X_APPID = "x-appid";
	private static final String HEADER_NAME_X_PLATFORM = "x-platform";
	private static final String HEADER_VALUE_X_PLATFORM_ANDROID = "android";

	OnKeywordSearchListener onKeywordSearchListener;
	SearchTask searchTask;
	String appId;

	private class SearchTask extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... urls) {
			String url = urls[0];
			Map<String, String> header = new HashMap<String, String>();
			header.put(HEADER_NAME_X_APPID, appId);
			header.put(HEADER_NAME_X_PLATFORM, HEADER_VALUE_X_PLATFORM_ANDROID);

			// String json4 = fetchData("https://dapi.kakao.com/v2/local/search/category.json?category_group_code=FD6&x=127.062528&y=37.656441&radius=500&page=4",header);
			String json8 = fetchData(url,header);

			List<Item5> itemList8 = parse8(json8);
			if (onKeywordSearchListener != null) {
				if (itemList8 == null) {
					onKeywordSearchListener.onFail();
				} else {
					onKeywordSearchListener.onSuccess(itemList8);
				}
			}
			return null;
		}
	}

	public void searchKeyword(Context applicationContext, String query,String category_group_code, double latitude, double longitude, int radius, int page,  OnKeywordSearchListener onKeywordSearchListener) {
    	this.onKeywordSearchListener = onKeywordSearchListener;
    	
		if (searchTask != null) {
			searchTask.cancel(true);
			searchTask = null;
		}
		
		if (applicationContext != null) {
			appId = applicationContext.getPackageName();
		}

			String url = buildKeywordSearchApiUrlString(query, category_group_code, latitude, longitude, radius, page);

			searchTask = new SearchTask();
			searchTask.execute(url);


    }
	

    
	private String buildKeywordSearchApiUrlString(String query, String category_group_code, double longitude, double latitude, int radius, int page) {
    	String encodedQuery = "";
		try {
			encodedQuery = URLEncoder.encode(query, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	return String.format(KAKAO_LOCAL_KEYWORD_SEARCH_API_FORMAT, encodedQuery, category_group_code, longitude, latitude, radius, page);
    }
	

	private String fetchData(String urlString, Map<String, String> header) {
		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(4000 /* milliseconds */);
			conn.setConnectTimeout(7000 /* milliseconds */);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", "KakaoAK 5a864b3b966b1a002cd608539159683c");
			conn.setDoInput(true);
			if (header != null) {
				for (String key : header.keySet()) {
					conn.addRequestProperty(key, header.get(key));
				}
			}
			conn.connect();
			InputStream is = conn.getInputStream();
			@SuppressWarnings("resource")
            Scanner s = new Scanner(is);
			s.useDelimiter("\\A");
			String data = s.hasNext() ? s.next() : "";
			is.close();
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
    
	private List<Item5> parse8(String jsonString) {
					List<Item5> itemList8 = new ArrayList<Item5>();
					try {
						JSONObject reader = new JSONObject(jsonString);
						//JSONObject documents = reader.getJSONObject("meta");
						JSONArray objects = reader.getJSONArray("documents");
						for (int i = 0; i < objects.length(); i++) {
							JSONObject object = objects.getJSONObject(i);
							Item5 item5 = new Item5();
							item5.title = object.getString("place_name");
							//item.imageUrl = object.getString("place_url");
							item5.address = object.getString("address_name");
							item5.newAddress = object.getString("road_address_name");
							//item.zipcode = object.getString("category_group_code");
							item5.phone = object.getString("phone");
							item5.category = object.getString("category_name");
							item5.latitude = object.getDouble("y");
							item5.longitude = object.getDouble("x");
				item5.distance = object.getDouble("distance");
				//item.direction = object.getString("direction");
				item5.id = object.getString("id");

				item5.place_url = object.getString("place_url");
				//item.addressBCode = object.getString("addressBCode");
				itemList8.add(item5);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return itemList8;
	}

	public void cancel() {
		if (searchTask != null) {
			searchTask.cancel(true);
			searchTask = null;
		}
	}
}
