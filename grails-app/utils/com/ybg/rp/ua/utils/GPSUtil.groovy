package com.ybg.rp.ua.utils

import static java.lang.Math.*

/**
 * Created by yangbagang on 16/7/17.
 */
class GPSUtil {

    //static double EARTH_RADIUS = 6378140 //地球赤道半径
    static double EARTH_RADIUS = 6371004 //地球平均半径

    static double rad(double d) {
        return d * PI / 180.0;
    }

    static Long getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s.longValue();
    }

}
