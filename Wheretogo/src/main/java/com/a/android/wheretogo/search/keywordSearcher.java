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

	public static final String KAKAO_LOCAL_KEYWORD_SEARCH_API_FORMAT = "https://dapi.kakao.com/v2/local/search/keyword.json?query=%s&category_group_code=%s&x=%f&y=%f&radius=%d&page=%d";
	public static final String KAKAO_LOCAL_CATEGORY_SEARCH_API_FORMAT = "https://dapi.kakao.com/v2/local/search/category.json?category_group_code=%s&x=%f&y=%f&radius=%d&page=%d";

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
						JSONArray objects = reader.getJSONArray("documents");
						for (int i = 0; i < objects.length(); i++) {
							JSONObject object = objects.getJSONObject(i);
							Item5 item5 = new Item5();
							item5.title = object.getString("place_name");
							item5.address = object.getString("address_name");
							item5.newAddress = object.getString("road_address_name");
							item5.phone = object.getString("phone");
							item5.category = object.getString("category_name");
							item5.latitude = object.getDouble("y");
							item5.longitude = object.getDouble("x");
				item5.id = object.getString("id");

				item5.place_url = object.getString("place_url");
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
