package com.pioneeriot.pioneeriot.network;

import com.pioneeriot.pioneeriot.bean.NormalResult;
import com.pioneeriot.pioneeriot.bean.OneWaterMeterInformation;
import com.pioneeriot.pioneeriot.bean.WaterCompanyHierarchy;
import com.pioneeriot.pioneeriot.bean.WaterMeterCommitInformation;
import com.pioneeriot.pioneeriot.bean.WaterMeterLastCommitInformation;
import com.pioneeriot.pioneeriot.bean.WaterMeterLoginResult;
import com.pioneeriot.pioneeriot.bean.WaterMeterWarning;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * Retrofit网络请求构建接口
 * Created by LiYuliang on 2017/10/28.
 *
 * @author LiYuliang
 * @version 2017/10/19
 */

public interface NjMeterApi {

    /**
     * 下载软件
     *
     * @param filePath 文件路径
     * @return 文件
     */
    @GET
    Call<ResponseBody> downloadFile(@Url String filePath);


    /******************************************************************   水表平台相关接口  ******************************************************************

     /**
     * 水表平台登录
     *
     * @param params 参数
     * @return 返回值
     */
    @FormUrlEncoded
    @POST("android/androidLogin.do")
    Call<WaterMeterLoginResult> loginWaterMeter(@FieldMap Map<String, String> params);

    /**
     * 查询水司的层级关系
     *
     * @param params 参数
     * @return 返回值
     */
    @FormUrlEncoded
    @POST("android/findLvInfo.do")
    Call<WaterCompanyHierarchy> searchAllHierarchy(@FieldMap Map<String, String> params);

    /**
     * 查询水司24小时内有数据上传的水表
     *
     * @param params 参数
     * @return 返回值
     */
    @FormUrlEncoded
    @POST("android/findLastReportData.do")
    Call<WaterMeterLastCommitInformation> searchMeterLastCommitInformation(@FieldMap Map<String, String> params);

    /**
     * 查询已抄到数据的水表信息
     *
     * @param params  参数
     * @return 返回值
     */
    @FormUrlEncoded
    @POST("android/findNewLastReportData.do")
    Call<WaterMeterLastCommitInformation> searchMeterLastReportYiChao(@FieldMap Map<String, String> params);

    /**
     * 查询未抄到数据的水表信息
     *
     * @param params 参数
     * @return 返回值
     */
    @FormUrlEncoded
    @POST("android/findNewLastReportDataWeichaodao.do")
    Call<WaterMeterLastCommitInformation> searchMeterLastReportWeiChao(@FieldMap Map<String, String> params);

    /**
     * 查询所有水表异常信息、采集器异常信息、读表异常信息
     *
     * @param params 参数
     * @return 返回值
     */
    @FormUrlEncoded
    @POST("android/findMeterWarningData.do")
    Call<WaterMeterWarning> searchAllMeterWarning(@FieldMap Map<String, String> params);

    /**
     * 查询指定水表的基础信息
     *
     * @param params 参数
     * @return 返回值
     */
    @FormUrlEncoded
    @POST("android/findUserMeterInfo.do")
    Call<OneWaterMeterInformation> searchOneWaterMeterInformation(@FieldMap Map<String, String> params);

    /**
     * 查询一只水表指定日期的提交记录
     *
     * @param params 参数
     * @return 返回值
     */
    @FormUrlEncoded
    @POST("android/findOneMeterReportData.do")
    Call<WaterMeterCommitInformation> searchMeterCommitInformation(@FieldMap Map<String, String> params);

    /**
     * 查询一只水表指定日期区间的上传记录
     *
     * @param params 参数
     * @return 返回值
     */
    @FormUrlEncoded
    @POST("android/findNewOneMeterReportData.do")
    Call<WaterMeterCommitInformation> searchMeterCommitDataInformation(@FieldMap Map<String, String> params);

    /**
     * 上传错误日志文件
     *
     * @param file 错误日志文件
     * @return 上传结果
     */
    @Multipart
    @POST("AndroidController/uploadAndroidLog.do")
    Call<NormalResult> uploadCrashFiles(@Part MultipartBody.Part file);
}
