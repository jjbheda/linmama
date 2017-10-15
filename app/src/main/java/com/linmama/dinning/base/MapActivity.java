package com.linmama.dinning.base;

import android.os.Bundle;

import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;
import com.linmama.dinning.R;

/**
 * Created by jiangjingbo on 2017/8/6.
 */

public class MapActivity extends com.tencent.tencentmap.mapsdk.map.MapActivity {
    private MapView mapView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.map_activity);
            mapView = (MapView) findViewById(R.id.mapview);
            //获取TencentMap实例
            TencentMap tencentMap = mapView.getMap();

            //设置地图中心点
            tencentMap.setCenter(new LatLng(39.87803,116.19025));

            //设置缩放级别
            tencentMap.setZoom(15);
            Marker marker = tencentMap.addMarker(new MarkerOptions()
                    .position(new LatLng(39.87803,116.19025))
                    .title("上海")
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker())
                    .draggable(true));
            marker.showInfoWindow();// 设置默认显示一个infoWindow
        }
}
