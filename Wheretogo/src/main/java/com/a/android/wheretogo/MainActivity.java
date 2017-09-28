package com.a.android.wheretogo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.a.android.wheretogo.item.Item;
import com.a.android.wheretogo.item.Item2;
import com.a.android.wheretogo.item.Item3;
import com.a.android.wheretogo.item.Item5;
import com.a.android.wheretogo.search.CategorySearcher;
import com.a.android.wheretogo.search.OnCategorySearchListener;
import com.a.android.wheretogo.search.OnKeywordSearchListener;
import com.a.android.wheretogo.search.keywordSearcher;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MapView.MapViewEventListener, MapView.POIItemEventListener, MapView.CurrentLocationEventListener {

    private static final String DAUM_API_KEY = "f2f1bfbda1aff34eefe71a20907a75ad";
    private static final String KAKAO_REST_API_KEY = "5a864b3b966b1a002cd608539159683c";
    private HashMap<Integer, Item> mTagItemMap = new HashMap<Integer, Item>();
    private HashMap<Integer, Item2> mTagItemMap2 = new HashMap<Integer, Item2>();
    private HashMap<Integer, Item3> mTagItemMap3 = new HashMap<Integer, Item3>();
    private HashMap<Integer, Item5> mTagItemMap8 = new HashMap<Integer, Item5>();
    private MapView myMapView;
    private net.daum.android.map.MapView mapview;

    double mLatitude;  //위도
    double mLongitude; //경도
    public double currentLat;
    public double currentLon;
    public double latitude;
    public double longitude;
    int count;
    private boolean flag = true;
    int radius = 500; // 중심 좌표부터의 반경거리. 특정 지역을 중심으로 검색하려고 할 경우 사용. meter 단위 (0 ~ 10000)
    int page = 1;
    CategorySearcher categorySearcher;
    Snackbar mSnackBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton now_location_btn = (FloatingActionButton) findViewById(R.id.now_location_btn);
        now_location_btn.setImageResource(R.drawable.now_location_btn2);

        FloatingActionButton refresh_btn = (FloatingActionButton) findViewById(R.id.refresh_btn);
        refresh_btn.setImageResource(R.drawable.refresh);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View nav_header_view = navigationView.getHeaderView(0);
        Button change_passwd_btn = (Button) nav_header_view.findViewById(R.id.changeButton);

        change_passwd_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent changeIntent = new Intent(MainActivity.this, PasswdCheckActivity.class);
                startActivity(changeIntent);
            }
        });


        myMapView = new MapView(this);
        myMapView.setDaumMapApiKey(DAUM_API_KEY);

        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.map_view);
        viewGroup.addView(myMapView);

        myMapView.setMapViewEventListener(this); // this에 MapView.MapViewEventListener 구현.
        myMapView.setPOIItemEventListener(this);
        myMapView.setCurrentLocationEventListener(this);
        categorySearcher = new CategorySearcher(); // net.daum.android.map.openapi.search.Searcher

        now_location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,"Hi! I’m Snackbar!",Snackbar.LENGTH_INDEFINITE).show();
                flag = true;
                myMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);

            }
        });

        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapPoint.GeoCoordinate geoCoordinate = myMapView.getMapCenterPoint().getMapPointGeoCoord();
                latitude = geoCoordinate.latitude; // 현재 중심 기준 위도
                longitude = geoCoordinate.longitude; // 현재 중심 기준 경도
                MapPoint centerPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);
                myMapView.setMapCenterPoint(centerPoint, true);
                categorySearcher.searchCategory(getApplicationContext(), "FD6", longitude, latitude, radius, page, new OnCategorySearchListener() {

                    @Override
                    public void onSuccess(List<Item> itemList, List<Item2> itemList5, List<Item3> itemList6) {
                        myMapView.removeAllPOIItems(); // 기존 검색 결과 삭제
                        showResult(itemList, itemList5, itemList6); // 검색 결과 보여줌
                    }

                    @Override
                    public void onFail() {
                        showToast("API Fail");
                    }
                });
            }
        });


    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float accuracyInMeters) {

        MapPoint.GeoCoordinate mapPointGeo = currentLocation.getMapPointGeoCoord();
        currentLat = mapPointGeo.latitude;
        currentLon = mapPointGeo.longitude;

        if (flag) {
            MapPoint centerPoint = MapPoint.mapPointWithGeoCoord(currentLat, currentLon);
            mapView.setMapCenterPoint(centerPoint, true);

            categorySearcher.searchCategory(getApplicationContext(), "FD6", currentLon, currentLat, radius, page, new OnCategorySearchListener() {

                @Override
                public void onSuccess(List<Item> itemList, List<Item2> itemList5, List<Item3> itemList6) {
                    myMapView.removeAllPOIItems(); // 기존 검색 결과 삭제
                    showResult(itemList, itemList5, itemList6); // 검색 결과 보여줌
                }
                @Override
                public void onFail() {
                    showToast("API Fail");
                }
            });
            flag = !flag;
        }
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {


    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                hideKeyboard();
                if (query == null || query.length() == 0) {  //지금 이 기능이 안됨
                    return true;
                }
                MapPoint.GeoCoordinate geoCoordinate = myMapView.getMapCenterPoint().getMapPointGeoCoord();
                latitude = geoCoordinate.latitude; // 현재 중심 기준 위도
                longitude = geoCoordinate.longitude; // 현재 중심 기준 경도
                int radius = 10000; // 중심 좌표부터의 반경거리. 특정 지역을 중심으로 검색하려고 할 경우 사용. meter 단위 (0 ~ 10000)
                int page = 1; // 페이지 번호 (1 ~ 3). 한페이지에 15개

                keywordSearcher keywordSearcher = new keywordSearcher(); // net.daum.android.map.openapi.search.Searcher
                keywordSearcher.searchKeyword(getApplicationContext(), query, "FD6", longitude, latitude, radius, page, new OnKeywordSearchListener() {
                    @Override
                    public void onSuccess(List<Item5> itemList8) {
                        myMapView.removeAllPOIItems(); // 기존 검색 결과 삭제
                        searchResult(itemList8); // 검색 결과 보여줌
                    }

                    @Override
                    public void onFail() {
                        showToast("API Fail");
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }

        });
        return true;
    }

    private void hideKeyboard() {    //검색 시에 키보드 숨기기(왜 clearfocus로는 안될까...)
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_nearborhood_store) {

        } else if (id == R.id.nav_dev_setting) {
            Intent settingIntent = new Intent(MainActivity.this,SettingActivity.class);
            MainActivity.this.startActivity(settingIntent);

        } else if (id == R.id.nav_id_logout) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {
        // Move
        MapPoint myPoint = MapPoint.mapPointWithGeoCoord(mLatitude, mLongitude);
        mapView.setMapCenterPoint(myPoint, true);
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);
    }


    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem marker) {

        RelativeLayout container = (RelativeLayout) findViewById(R.id.multi);
        //레이아웃 인플레이션을 통해 부분 화면으로 추가한다
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    private void showResult(List<Item> itemList, List<Item2> itemList5, List<Item3> itemList6) { //마지막으로 추가한 부분(가게정보 집어넣기)
        MapPointBounds mapPointBounds = new MapPointBounds();

        for (int i = 0; i < itemList.size(); i++) {
            Item item = itemList.get(i);

            MapPOIItem poiItem = new MapPOIItem();
            poiItem.setItemName(item.title);
            poiItem.setTag(i);
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(item.latitude, item.longitude);
            poiItem.setMapPoint(mapPoint);
            mapPointBounds.add(mapPoint);
            poiItem.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            poiItem.setCustomImageResourceId(R.drawable.green_circle);
            poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
            poiItem.setCustomImageAutoscale(false);
            poiItem.setCustomImageAnchor(0.5f, 1.0f);
            myMapView.addPOIItem(poiItem);
            mTagItemMap.put(poiItem.getTag(), item);
        }

        for (int i = 0; i < itemList5.size(); i++) {
            Item2 item2 = itemList5.get(i);

            MapPOIItem poiItem2 = new MapPOIItem();
            poiItem2.setItemName(item2.title);
            poiItem2.setTag(i);
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(item2.latitude, item2.longitude);
            poiItem2.setMapPoint(mapPoint);
            mapPointBounds.add(mapPoint);
            poiItem2.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            poiItem2.setCustomImageResourceId(R.drawable.green_circle);
            poiItem2.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
            poiItem2.setCustomImageAutoscale(false);
            poiItem2.setCustomImageAnchor(0.5f, 1.0f);
            myMapView.addPOIItem(poiItem2);
            mTagItemMap2.put(poiItem2.getTag(), item2);
        }
        for (int i = 0; i < itemList6.size(); i++) {
            Item3 item3 = itemList6.get(i);

            MapPOIItem poiItem3 = new MapPOIItem();
            poiItem3.setItemName(item3.title);
            poiItem3.setTag(i);
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(item3.latitude, item3.longitude);
            poiItem3.setMapPoint(mapPoint);
            mapPointBounds.add(mapPoint);
            poiItem3.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            poiItem3.setCustomImageResourceId(R.drawable.green_circle);
            poiItem3.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
            poiItem3.setCustomImageAutoscale(false);
            poiItem3.setCustomImageAnchor(0.5f, 1.0f);
            myMapView.addPOIItem(poiItem3);
            //count = poiItem3.getTag()+30;
            mTagItemMap3.put(poiItem3.getTag(), item3);
        }

        myMapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds));

        MapPOIItem[] poiItems = myMapView.getPOIItems();
        if (poiItems.length > 0) {
            myMapView.selectPOIItem(poiItems[0], false);
        }
    }

    private void searchResult(List<Item5> itemList8) { //키워드 검색하여 나온 정보 맵에 집어넣기)
        MapPointBounds mapPointBounds = new MapPointBounds();

        for (int i = 0; i < itemList8.size(); i++) {
            Item5 item5 = itemList8.get(i);

            MapPOIItem poiItem = new MapPOIItem();
            poiItem.setItemName(item5.title);
            poiItem.setTag(i);
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(item5.latitude, item5.longitude);
            poiItem.setMapPoint(mapPoint);
            mapPointBounds.add(mapPoint);
            poiItem.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            poiItem.setCustomImageResourceId(R.drawable.green_circle);
            poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
            poiItem.setCustomImageAutoscale(false);
            poiItem.setCustomImageAnchor(0.5f, 1.0f);
            myMapView.addPOIItem(poiItem);
            mTagItemMap8.put(poiItem.getTag(), item5);
        }

        myMapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds));

        MapPOIItem[] poiItems = myMapView.getPOIItems();
        if (poiItems.length > 0) {
            myMapView.selectPOIItem(poiItems[0], false);
        }
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

        Set<Map.Entry<Integer, Item>> set = mTagItemMap.entrySet();
        Iterator<Map.Entry<Integer, Item>> it = set.iterator();

        while (it.hasNext()) {
            Map.Entry<Integer, Item> e = (Map.Entry<Integer, Item>) it.next();

        }

        Snackbar snackbar;
        Item item = mTagItemMap.get(mapPOIItem.getTag());
        Item2 item2 = mTagItemMap2.get(mapPOIItem.getTag());
        Item3 item3 = mTagItemMap3.get(mapPOIItem.getTag());
        Item5 item5 = mTagItemMap8.get(mapPOIItem.getTag());

        StringBuilder sb = new StringBuilder();
        if(item.title==mapPOIItem.getItemName())
        {
            Intent popIntent = new Intent(MainActivity.this,PopActivity.class);
            popIntent.putExtra("title",item.title);
            popIntent.putExtra("category",item.category);
            popIntent.putExtra("phone",item.phone);
            popIntent.putExtra("newAddress",item.newAddress);
            popIntent.putExtra("distance",item.distance);
            popIntent.putExtra("place_url",item.place_url);
           MainActivity.this.startActivity(popIntent);


        }
        else if(item2.title==mapPOIItem.getItemName())
        {
            Intent popIntent = new Intent(MainActivity.this,PopActivity.class);
            popIntent.putExtra("title",item2.title);
            popIntent.putExtra("category",item2.category);
            popIntent.putExtra("phone",item2.phone);
            popIntent.putExtra("newAddress",item2.newAddress);
            popIntent.putExtra("distance",item2.distance);
            popIntent.putExtra("place_url",item2.place_url);
            MainActivity.this.startActivity(popIntent);


        }
        else if(item3.title==mapPOIItem.getItemName())
        {
            Intent popIntent = new Intent(MainActivity.this,PopActivity.class);
            popIntent.putExtra("title",item3.title);
            popIntent.putExtra("category",item3.category);
            popIntent.putExtra("phone",item3.phone);
            popIntent.putExtra("newAddress",item3.newAddress);
            popIntent.putExtra("distance",item3.distance);
            popIntent.putExtra("place_url",item3.place_url);
            MainActivity.this.startActivity(popIntent);


        }

        else if(item5.title==mapPOIItem.getItemName())
        {
            Intent popIntent = new Intent(MainActivity.this,PopActivity.class);
            popIntent.putExtra("title",item5.title);
            popIntent.putExtra("category",item5.category);
            popIntent.putExtra("phone",item5.phone);
            popIntent.putExtra("newAddress",item5.newAddress);
            popIntent.putExtra("distance",item5.distance);
            popIntent.putExtra("place_url",item5.place_url);
            MainActivity.this.startActivity(popIntent);

        }

        snackbar=Snackbar.make(mapView,sb.toString(),Snackbar.LENGTH_INDEFINITE).setAction("YES", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        //할거
                    }
                });
        View snackView = snackbar.getView();
        snackView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        snackbar.show();

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem marker, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }


}
