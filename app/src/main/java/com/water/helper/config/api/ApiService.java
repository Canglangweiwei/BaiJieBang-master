package com.water.helper.config.api;

import com.jaydenxiao.common.basebean.BaseResponse;
import com.water.helper.bean.BaseModel;
import com.water.helper.bean.HotelBean;
import com.water.helper.bean.HotelLzBean;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * ==========================================================<br>
 * <b>版权</b>：　　　魏巍 版权所有(c)2016<br>
 * <b>作者</b>：　　  weiwei<br>
 * <b>创建日期</b>：　17-11-13<br>
 * <b>描述</b>：　　　用户信息<br>
 * <b>版本</b>：　    V1.0<br>
 * <b>修订历史</b>：　<br>
 * ==========================================================<br>
 */
public interface ApiService {

    /**
     * 获取版本信息
     */
    @GET("version/findAppVersion.php")
    Observable<BaseModel> Get_Version();

    /**
     * 获取用户信息
     */
    @FormUrlEncoded
    @POST("common/login.php")
    Observable<BaseModel> userLogin(@Field("username") String username, @Field("password") String password);

    /**
     * 获取宾馆列表信息
     */
    @GET("xidi/binguan.php")
    Observable<BaseResponse<List<HotelBean>>> Get_BinguanInfo();

    /**
     * 获取楼层列表信息
     */
    @GET("xidi/louceng.php")
    Observable<BaseResponse<List<HotelLzBean>>> Get_LoucengInfo(@Query("hid") int hid);

    /**
     * 获取收货列表信息
     */
    @GET("xidi/shouhuoList.php")
    Observable<BaseModel> Get_ShouhuoList(@Query("hid") int hid, @Query("selectDate") String selectDate, @Query("username") String username);

    /**
     * 获取发货列表信息
     */
    @GET("xidi/fahuoList.php")
    Observable<BaseModel> Get_FahuoList(@Query("bName") String bName, @Query("selectDate") String selectDate, @Query("username") String username);

    /**
     * 获取发货列表信息
     */
    @GET("xidi/addType.php")
    Observable<BaseModel> Get_AddType(@Query("hid") int hid);

    /**
     * 获取发货列表信息
     */
    @FormUrlEncoded
    @POST("xidi/add.php")
    Observable<BaseModel> Upload(@Field("dataJson") String dataJson, @Field("username") String username,
                                 @Field("hid") int hid, @Field("lc") int lc, @Field("beizhu") String beizhu);
}
