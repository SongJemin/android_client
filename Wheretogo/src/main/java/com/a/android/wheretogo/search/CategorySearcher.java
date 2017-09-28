package com.a.android.wheretogo.search;

import android.content.Context;
import android.os.AsyncTask;

import com.a.android.wheretogo.item.Item;
import com.a.android.wheretogo.item.Item2;
import com.a.android.wheretogo.item.Item3;

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

public class CategorySearcher {

	public static final String KAKAO_LOCAL_KEYWORD_SEARCH_API_FORMAT = "https://dapi.kakao.com/v2/local/search/keyword.json?query=%s&category_group_code=%s&x=%f&y=%f&radius=%d&page=%d";
	public static final String KAKAO_LOCAL_CATEGORY_SEARCH_API_FORMAT = "https://dapi.kakao.com/v2/local/search/category.json?category_group_code=%s&x=%f&y=%f&radius=%d&page=%d";

	private static final String HEADER_NAME_X_APPID = "x-appid";
	private static final String HEADER_NAME_X_PLATFORM = "x-platform";
	private static final String HEADER_VALUE_X_PLATFORM_ANDROID = "android";

	OnCategorySearchListener onCategorySearchListener;
	SearchTask searchTask;
	String appId;

	private class SearchTask extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... urls) {
			String url = urls[0];
			Map<String, String> header = new HashMap<String, String>();
			header.put(HEADER_NAME_X_APPID, appId);
			header.put(HEADER_NAME_X_PLATFORM, HEADER_VALUE_X_PLATFORM_ANDROID);
			String json = fetchData(url, header);

			url = url.replace("page=1","page=2");
			String json2 = fetchData(url, header);

			url = url.replace("page=2","page=3");
			String json3 = fetchData(url, header);

			url = url.replace("page=3","page=4");
			String json4 = fetchData(url,header);

			List<Item> itemList = parse(json);
			List<Item2> itemList5 = parse2(json2);
			List<Item3> itemList6 = parse3(json3);
			List<Item> itemList7 = parse4(json4);
			if (onCategorySearchListener != null) {
				if (itemList == null) {
					onCategorySearchListener.onFail();
				} else {
					onCategorySearchListener.onSuccess(itemList, itemList5, itemList6);
				}
			}
			return null;
		}
	}

	public void searchCategory(Context applicationContext, String categoryCode, double latitude, double longitude, int radius, int page, OnCategorySearchListener onFinishSearchListener) {
		this.onCategorySearchListener = onFinishSearchListener;
		
		if (searchTask != null) {
			searchTask.cancel(true);
			searchTask = null;
		}
		
		if (applicationContext != null) {
			appId = applicationContext.getPackageName();
		}

		String url = buildCategorySearchApiUrlString(categoryCode, latitude, longitude, radius, page);
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
	
	private String buildCategorySearchApiUrlString(String categoryCode, double latitude, double longitude, int radius, int page) {
		return String.format(KAKAO_LOCAL_CATEGORY_SEARCH_API_FORMAT, categoryCode, latitude, longitude, radius, page);
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
    
	private List<Item> parse(String jsonString) {

					List<Item> itemList = new ArrayList<Item>();
					try {
						JSONObject reader = new JSONObject(jsonString);
						//JSONObject documents = reader.getJSONObject("meta");
						JSONArray objects = reader.getJSONArray("documents");
						for (int i = 0; i < objects.length(); i++) {
							JSONObject object = objects.getJSONObject(i);
							Item item = new Item();
							item.title = object.getString("place_name");
							item.address = object.getString("address_name");
							item.newAddress = object.getString("road_address_name");
							//item.zipcode = object.getString("category_group_code");
							item.phone = object.getString("phone");
							item.category = object.getString("category_name");
							item.latitude = object.getDouble("y");
							item.longitude = object.getDouble("x");
				item.distance = object.getDouble("distance");
				item.id = object.getString("id");

				item.place_url = object.getString("place_url");
				itemList.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return itemList;
	}

	private List<Item2> parse2(String jsonString) {
		List<Item2> itemList = new ArrayList<Item2>();
		try {
			JSONObject reader = new JSONObject(jsonString);
			JSONArray objects = reader.getJSONArray("documents");
			for (int j = 0; j < objects.length(); j++) {
				JSONObject object = objects.getJSONObject(j);
				Item2 item2 = new Item2();
				item2.title = object.getString("place_name");
				item2.address = object.getString("address_name");
				item2.newAddress = object.getString("road_address_name");
				item2.phone = object.getString("phone");
				item2.category = object.getString("category_name");
				item2.latitude = object.getDouble("y");
				item2.longitude = object.getDouble("x");
				item2.distance = object.getDouble("distance");
				item2.id = object.getString("id");
				item2.place_url = object.getString("place_url");
				itemList.add(item2);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return itemList;
	}

	private List<Item3> parse3(String jsonString) {

		List<Item3> itemList = new ArrayList<Item3>();
		try {
			JSONObject reader = new JSONObject(jsonString);
			JSONArray objects = reader.getJSONArray("documents");
			for (int k= 0; k < objects.length(); k++) {
				JSONObject object = objects.getJSONObject(k);
				Item3 item3 = new Item3();
				item3.title = object.getString("place_name");
				item3.address = object.getString("address_name");
				item3.newAddress = object.getString("road_address_name");
				item3.phone = object.getString("phone");
				item3.category = object.getString("category_name");
				item3.latitude = object.getDouble("y");
				item3.longitude = object.getDouble("x");
				item3.distance = object.getDouble("distance");
				item3.id = object.getString("id");
				item3.place_url = object.getString("place_url");
				itemList.add(item3);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return itemList;
	}

	private List<Item> parse4(String jsonString) {
		List<Item> itemList = new ArrayList<Item>();
		try {
			JSONObject reader = new JSONObject(jsonString);
			JSONArray objects = reader.getJSONArray("documents");
			for (int a= 0; a < objects.length(); a++) {
				JSONObject object = objects.getJSONObject(a);
				Item item4 = new Item();
				item4.title = object.getString("place_name");
				item4.address = object.getString("address_name");
				item4.newAddress = object.getString("road_address_name");
				item4.phone = object.getString("phone");
				item4.category = object.getString("category_name");
				item4.latitude = object.getDouble("y");
				item4.longitude = object.getDouble("x");
				item4.distance = object.getDouble("distance");
				item4.id = object.getString("id");
				item4.place_url = object.getString("place_url");
				itemList.add(item4);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return itemList;
	}

	public void cancel() {
		if (searchTask != null) {
			searchTask.cancel(true);
			searchTask = null;
		}
	}
}
