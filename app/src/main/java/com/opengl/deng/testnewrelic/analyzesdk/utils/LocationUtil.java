package com.opengl.deng.testnewrelic.analyzesdk.utils;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.opengl.deng.testnewrelic.analyzesdk.bean.LocationBean;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

/**
 * @Description used for location locate
 * Created by deng on 2018/6/27.
 */
public class LocationUtil {
    private static final String TAG = "LocationUtil";

    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_CITY_NAME = "city_name";
    private static final String KEY_DISTRICT = "district";
    private static final String KEY_CITY_CODE = "city_code";
    private static final String KEY_AD_CODE = "ad_code";
    private static final String KEY_ADDRESS = "address";

    /** 单例模式 */
    private static final LocationUtil ourInstance = new LocationUtil();
    private LocationUtil(){}
    public static LocationUtil getInstance() {
        return ourInstance;
    }

    private AMapLocationClient mapLocationClient;
    private AMapLocationClientOption option;

    /**
     * 获取当前定位信息
     * @param context activity
     * @return locationBean
     */
    public Observable<LocationBean> getLocation(final Context context) {
        return Observable.create(new ObservableOnSubscribe<AMapLocation>() {
            @Override
            public void subscribe(final ObservableEmitter<AMapLocation> e) throws Exception {
                if (option == null) {
                    option = new AMapLocationClientOption();
                    option.setNeedAddress(true);
                    option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                    option.setOnceLocation(true);
                }

                if (mapLocationClient == null) {
                    mapLocationClient = new AMapLocationClient(context.getApplicationContext());
                    mapLocationClient.setLocationOption(option);
                }

                mapLocationClient.setLocationListener(new AMapLocationListener() {
                    @Override
                    public void onLocationChanged(AMapLocation aMapLocation) {
                        Log.i(TAG, "onLocationChanged:" + aMapLocation.toString());
                        e.onNext(aMapLocation);
                        e.onComplete();
                    }
                });
                mapLocationClient.startLocation();
            }
        }).map(new Function<AMapLocation, LocationBean>() {
            @Override
            public LocationBean apply(AMapLocation aMapLocation) throws Exception {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        LocationBean bean = new LocationBean();
                        bean.setLatitude(aMapLocation.getLatitude());
                        bean.setLongitude(aMapLocation.getLongitude());
                        bean.setCityName(aMapLocation.getCity());
                        bean.setCityCode(aMapLocation.getCityCode());
                        bean.setAdCode(aMapLocation.getAdCode());
                        bean.setAddress(aMapLocation.getAddress());
                        bean.setDistrict(aMapLocation.getDistrict());
                        return bean;
                    } else {
                        throw new Exception("定位失败，错误码:" + aMapLocation.getErrorCode() + " " + aMapLocation.getErrorInfo());
                    }
                } else {
                    throw new Exception("定位失败，返回为null");
                }
            }
        });
//        .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends LocationBean>>() {
//            @Override
//            public ObservableSource<? extends LocationBean> apply(Throwable throwable) throws Exception {
//                return Observable.error(throwable);
//            }
//        }).retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
//            @Override
//            public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
//                return throwableObservable.zipWith(Observable.range(1, 3), new BiFunction<Throwable, Integer, Throwable>() {
//                    @Override
//                    public Throwable apply(Throwable throwable, Integer integer) throws Exception {
//                        if (integer == 3) {
//                            return new RetryFailedException(throwable);
//                        }
//                        return throwable;
//                    }
//                }).flatMap(new Function<Throwable, ObservableSource<?>>() {
//                    @Override
//                    public ObservableSource<?> apply(Throwable throwable) throws Exception {
//                        if (throwable instanceof RetryFailedException) {
//                            return Observable.error(throwable.getCause());
//                        }
//                        return Observable.just(1);
//                    }
//                });
//            }
//        });
    }

    class RetryFailedException extends Exception {
        RetryFailedException(Throwable cause) {
            super(cause);
        }
    }

}
