package com.gps;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.hunter.sensor.SensorException;
import com.hunter.sensor.SensorSupport;

import java.text.SimpleDateFormat;
import java.util.Locale;

import static android.content.ContentValues.TAG;
import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by BG2CYR on 2016/11/5.
 */

public class Sensor_If implements SensorSupport {
    private Context con;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = new AMapLocationClientOption();
    private TextView tvResult=null,dirResult=null;
    private SensorManager sm;
    private Sensor aSensor,mSensor;
    private float Oridata;
    float[] magneticFieldValues=new float[3],accelerometerValues=new float[3];
    private static final String TAG = "sensor";
    private  void calculateOrientation() {
        float[] values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
        SensorManager.getOrientation(R, values);

        // 要经过一次数据格式的转换，转换为度
        values[0] = (float) Math.toDegrees(values[0]);
        Oridata=values[0];
        Log.i(TAG, values[0]+"");
        //values[1] = (float) Math.toDegrees(values[1]);
        //values[2] = (float) Math.toDegrees(values[2]);
        if(dirResult!=null)
        {
            dirResult.setText(""+(int)Oridata);
        }
        if(values[0] >= -5 && values[0] < 5){
            Log.i(TAG, "正北");
        }
        else if(values[0] >= 5 && values[0] < 85){
            Log.i(TAG, "东北");
        }
        else if(values[0] >= 85 && values[0] <=95){
            Log.i(TAG, "正东");
        }
        else if(values[0] >= 95 && values[0] <175){
            Log.i(TAG, "东南");
        }
        else if((values[0] >= 175 && values[0] <= 180) || (values[0]) >= -180 && values[0] < -175){
            Log.i(TAG, "正南");
        }
        else if(values[0] >= -175 && values[0] <-95){
            Log.i(TAG, "西南");
        }
        else if(values[0] >= -95 && values[0] < -85){
            Log.i(TAG, "正西");
        }
        else if(values[0] >= -85 && values[0] <-5){
            Log.i(TAG, "西北");
        }
    }
    private SensorEventListener slistener= new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                magneticFieldValues = sensorEvent.values;
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                accelerometerValues = sensorEvent.values;
            calculateOrientation();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
    public Sensor_If(Context con)
    {
        this.con=con;
        initLocation();

        sm = (SensorManager) con.getSystemService(SENSOR_SERVICE);
        aSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sm.registerListener(slistener,aSensor,SensorManager.SENSOR_DELAY_FASTEST);
        sm.registerListener(slistener,mSensor,SensorManager.SENSOR_DELAY_FASTEST);
        this.startLocation();
    }
    //数据区
    //基本数据
    final int nogps=100;
    int typenum=999;
    public String fullData="";
    public String type="";
    public double Longtitude=0.0;
    public double Latitude=0.0;
    public double Accuracy=0.0;
    public String Provider="";
    //GPS
    public double Speed=0.0;
    public double Bearing=0.0;
    public int Satellites=0;
    //网络数据
    public String Country;
    public String Province;
    public String City;
    public String cityCode;
    public String District;
    public String AdCode;
    public String Address;
    public String PoiName;
    //数据区结束
    private String analyzeData(AMapLocation location)
    {
        if(null == location){
            return null;
        }
        StringBuffer sb = new StringBuffer();
        //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
        if(location.getErrorCode() == 0){
            sb.append("定位成功" + "\n");
            typenum=location.getLocationType();

            switch (typenum)
            {
                case 0:type="定位失败";break;
                case 1:type="GPS定位结果";break;
                case 2:type="前次定位结果";break;
                case 4:type="缓存定位结果";break;
                case 5:type="Wifi定位结果";break;
                case 6:type="基站定位结果";break;
                case 8:type="离线定位结果";break;
                default:type="";
            }
            sb.append("定位类型: " + type + "\n");
            Longtitude=location.getLongitude();
            sb.append("经    度    : " + Longtitude + "\n");
            Latitude=location.getLatitude();
            sb.append("纬    度    : " + Latitude + "\n");
            Accuracy=location.getAccuracy();
            sb.append("精    度    : " + Accuracy + "米" + "\n");
            Provider=location.getProvider();
            sb.append("提供者    : " + Provider + "\n");

            if (location.getProvider().equalsIgnoreCase(
                    android.location.LocationManager.GPS_PROVIDER)) {
                // 以下信息只有提供者是GPS时才会有
                Speed=location.getSpeed();
                sb.append("速    度    : " + Speed + "米/秒" + "\n");
                Bearing=location.getBearing();
                sb.append("角    度    : " + Bearing + "\n");
                // 获取当前提供定位服务的卫星个数
                Satellites=location.getSatellites();
                sb.append("星    数    : "
                        + Satellites + "\n");
            } else {
                // 提供者是GPS时是没有以下信息的
                Country=location.getCountry();
                sb.append("国    家    : " + Country + "\n");
                Province=location.getProvince();
                sb.append("省            : " + Province + "\n");
                City=location.getCity();
                sb.append("市            : " + City + "\n");
                cityCode=location.getCityCode();
                sb.append("城市编码 : " + cityCode + "\n");
                District=location.getDistrict();
                sb.append("区            : " + District + "\n");
                AdCode=location.getAdCode();
                sb.append("区域 码   : " + AdCode + "\n");
                Address=location.getAddress();
                sb.append("地    址    : " + Address + "\n");
                PoiName=location.getPoiName();
                sb.append("兴趣点    : " + PoiName + "\n");
                //定位完成的时间
                sb.append("定位时间: " + formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
            }
        } else {
            //定位失败
            typenum=nogps;
            sb.append("定位失败" + "\n");
            sb.append("错误码:" + location.getErrorCode() + "\n");
            sb.append("错误信息:" + location.getErrorInfo() + "\n");
            sb.append("错误描述:" + location.getLocationDetail() + "\n");
        }
        sb.append("回调时间: " + formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");
        return sb.toString();
    }
    public void setShowlocID(TextView id)
    {
        this.tvResult=id;
    }
    public void setShowDirectionID(TextView id)
    {
        this.dirResult=id;
    }
    public void initLocation(){
        //初始化client

        locationClient = new AMapLocationClient(con.getApplicationContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }
    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        //mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        return mOption;
    }
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {

            if (null != loc) {
                //解析定位结果
                String result = analyzeData(loc);
                fullData=result;
                if(tvResult!=null) {
                    tvResult.setText(result);
                }
            } else {
                if(tvResult!=null) {
                    tvResult.setText("定位失败，loc is null");
                }
            }
        }
    };
    private void resetOption() {
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(false);//实际发布时改为true
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(false);
        //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
        locationOption.setOnceLocationLatest(false);
        //设置是否使用传感器
        //locationOption.setSensorEnable(cbSensorAble.isChecked());
        String strInterval = "1000";
        if (!TextUtils.isEmpty(strInterval)) {
            try{
                // 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
                locationOption.setInterval(Long.valueOf(strInterval));
            }catch(Throwable e){
                e.printStackTrace();
            }
        }

        String strTimeout ="30000";
        if(!TextUtils.isEmpty(strTimeout)){
            try{
                // 设置网络请求超时时间
                locationOption.setHttpTimeOut(Long.valueOf(strTimeout));
            }catch(Throwable e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 开始定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    public void startLocation(){
        //根据控件的选择，重新设置定位参数
        resetOption();
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 停止定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    public void stopLocation(){
        // 停止定位
        locationClient.stopLocation();
    }

    /**
     * 销毁定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    public void destroyLocation(){
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    private static SimpleDateFormat sdf = null;
    public synchronized static String formatUTC(long l, String strPattern) {
        if (TextUtils.isEmpty(strPattern)) {
            strPattern = "yyyy-MM-dd HH:mm:ss";
        }
        if (sdf == null) {
            try {
                sdf = new SimpleDateFormat(strPattern, Locale.CHINA);
            } catch (Throwable e) {
            }
        } else {
            sdf.applyPattern(strPattern);
        }
        return sdf == null ? "NULL" : sdf.format(l);
    }

    @Override
    public int checkSensor() {
        if(typenum==nogps)
            return 0;
        if(typenum==999||typenum==0)
            return 1;
        return 2;
    }

    @Override
    public double getLongitude() throws SensorException {
        return this.Longtitude;
    }

    @Override
    public double getLatitude() throws SensorException {
        return this.Latitude;
    }

    @Override
    public int getDirection() throws SensorException {
        return (int)Oridata;
    }
}
