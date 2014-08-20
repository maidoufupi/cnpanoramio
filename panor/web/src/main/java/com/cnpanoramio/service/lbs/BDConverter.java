package com.cnpanoramio.service.lbs;

public class BDConverter {

	private static double lat = 31.22997;
    private static double lon = 121.640756;
    
    public static double x_pi = lat * lon / 180.0;
    
    public static void main(String[] args) {
        System.out.println("摩卡坐标经纬度:"+lat+","+lon);
        System.out.println("火星坐标经纬度:"+bd_decrypt(lat,lon));
    }
    
    //解密成为火星坐标
    public static LatLng bd_decrypt(double bd_lat, double bd_lon) { 
        double x = bd_lon - 0.0065, y = bd_lat - 0.006; 
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi); 
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi); 
        double gg_lon = z * Math.cos(theta); 
        double gg_lat = z * Math.sin(theta);
        
        return new LatLng(gg_lat, gg_lon);
//        return gg_lat+","+gg_lon;
    }
    
    //加密成为摩卡托坐标
    public static LatLng bd_encrypt(double gg_lat, double gg_lon) { 
        double x = gg_lon, y = gg_lat; 
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi); 
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi); 
        double bd_lon = z * Math.cos(theta) + 0.0065; 
        double bd_lat = z * Math.sin(theta) + 0.006; 
        
        return new LatLng(bd_lat, bd_lon);
//        return gg_lat+","+gg_lon;
    } 
    
}
