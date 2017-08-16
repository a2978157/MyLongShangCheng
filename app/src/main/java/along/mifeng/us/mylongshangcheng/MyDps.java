package along.mifeng.us.mylongshangcheng;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;

/**
 * Created by 21903 on 2017/5/30.
 */

public class MyDps extends Activity {
    private MapView mapView;
    private BaiduMap baiduMap;
    private LocationClient locationClient;
    Boolean isXx = true;
    private Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dps);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mapView = (MapView) findViewById(R.id.id_bmapView);
        bt = (Button) findViewById(R.id.bt);
        baiduMap = mapView.getMap();
        baiduMap.setTrafficEnabled(true);
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        locationClient = new LocationClient(getApplicationContext());
        initLocation();
        /*locationClient.registerLocationListener(new MyBdlLocationClient());
        locationClient.start();
        if (locationClient != null && locationClient.isStarted()) {
            locationClient.requestLocation();
        }*/
    }

    public void initLocation() {
        LocationClientOption option = new LocationClientOption();
        // 定义类型
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // 可选，默认gcj02，设置返回的定位结果坐标系
        option.setCoorType("bd09ll");
        // 设置多长时间定位一次
        option.setScanSpan(2000);
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        // 将定义好的客户端参数信息，放到客户端
        locationClient.setLocOption(option);
    }

    class MyBdlLocationClient implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            baiduMap.addOverlay(null);
            // 获取本地的维度
            double latitude = location.getLatitude();
            // 获取本地的经度
            double longitude = location.getLongitude();
            final LatLng latLng = new LatLng(latitude, longitude);
            /*if (isXx) {
                isXx = false;
                MapStatusUpdate newLatLng = MapStatusUpdateFactory.newLatLng(latLng);
                baiduMap.animateMapStatus(newLatLng);
            }*/
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BitmapDescriptor descriptor = BitmapDescriptorFactory
                            .fromBitmap(new BitmapCompress()
                                    .imgCompress(getApplicationContext()));
                    OverlayOptions options2 = new MarkerOptions()
                            .position(latLng).icon(descriptor);
                    // 添加覆盖物
                    OverlayOptions options = new TextOptions().position(latLng).text("王璞个七孙在这儿").fontColor(Color.RED).fontSize(30);
                    baiduMap.addOverlay(options);
                    baiduMap.addOverlay(options2);
                    MapStatusUpdate newLatLng = MapStatusUpdateFactory.newLatLng(latLng);
                    baiduMap.animateMapStatus(newLatLng);
                }
            });
        }

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mapView.onDestroy();
        mapView = null;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mapView.onPause();
    }
}
