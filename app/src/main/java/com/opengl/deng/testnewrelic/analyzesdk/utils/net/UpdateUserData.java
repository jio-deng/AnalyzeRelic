package com.opengl.deng.testnewrelic.analyzesdk.utils.net;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @Description interface for update
 * Created by deng on 2018/6/25.
 */
public interface UpdateUserData {

    /**
     * 向服务器发送用户行为信息
     * @param basicData 用户基本信息
     * @param performData 用户行为信息
     * @return Call
     */
    @FormUrlEncoded
    @POST("analyze/userPerform")
    Observable<String> updateUserPerform(@Field("basicData") String basicData, @Field("performData") String performData);
}
