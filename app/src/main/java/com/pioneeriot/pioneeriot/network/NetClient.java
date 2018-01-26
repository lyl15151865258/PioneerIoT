package com.pioneeriot.pioneeriot.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pioneeriot.pioneeriot.retrofit.ProgressListener;
import com.pioneeriot.pioneeriot.retrofit.ProgressResponseBody;
import com.pioneeriot.pioneeriot.retrofit.StringConverterFactory;
import com.pioneeriot.pioneeriot.utils.LogUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 请求接口URL
 * Created by Li Yuliang on 2017/2/17 0017.
 *
 * @author LiYuliang
 * @version 2017/10/27
 */

public class NetClient {

    public static final String TAG_POST = "Post方式";
    public static final String NETWORK_ERROR = "无网络连接";
    public static final String ADDRESS_ERROR = "网络不好或请求地址错误";
    public static final String SERVER_ERROR = "服务器异常，返回值不是200";
    public static final String SECRET_KRY = "jsmt";

    private static NetClient mNetClient;
    private NjMeterApi njMeterApi;
    private final Retrofit mRetrofit;
    private static String defaultUrl = "";

    private NetClient(String baseUrl) {

        // HttpLoggingInterceptor的初始化
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //设置日志打印级别
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(NetWork.TIME_OUT_HTTP, TimeUnit.SECONDS)
                .readTimeout(NetWork.TIME_OUT_HTTP, TimeUnit.SECONDS)
                .writeTimeout(NetWork.TIME_OUT_HTTP, TimeUnit.SECONDS)
                .build();
        //设置Gson的非严格模式
        Gson gson = new GsonBuilder().setLenient().create();
        // 初始化Retrofit
        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(new StringConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    /**
     * 获取单例的NetClient对象
     *
     * @param baseUrl 基础Url
     * @return NetClient对象
     */
    public static synchronized NetClient getInstances(String baseUrl) {
        if (mNetClient == null || !defaultUrl.equals(baseUrl)) {
            mNetClient = new NetClient(baseUrl);
            defaultUrl = baseUrl;
        }
        LogUtils.d("retrofit", baseUrl);
        return mNetClient;
    }

    public NjMeterApi getNjMeterApi() {
        if (njMeterApi == null) {
            njMeterApi = mRetrofit.create(NjMeterApi.class);
        }
        return njMeterApi;
    }

    /**
     * 主账号基础Url不带项目名（用于图像链接中）
     */
    public static final String BASE_URL = "http://" + NetWork.SERVER_HOST_MAIN + ":" + NetWork.SERVER_PORT_MAIN + "/";

    /**
     * 主账号基础Url带项目名
     */
    public static final String BASE_URL_PROJECT = "http://" + NetWork.SERVER_HOST_MAIN + ":" + NetWork.SERVER_PORT_MAIN + "/" + NetWork.PROJECT_MAIN + "/";

    /**
     * 拼接通用基础Url
     *
     * @param serverHost  IP地址（域名）
     * @param httpPort    端口号
     * @param serviceName 项目名
     * @return 拼接后的Url
     */
    public static String getBaseUrl(String serverHost, String httpPort, String serviceName) {
        return "http://" + serverHost + ":" + httpPort + "/" + serviceName + "/";
    }

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(NetWork.TIME_OUT_HTTP, TimeUnit.MILLISECONDS)
            .readTimeout(NetWork.TIME_OUT_HTTP, TimeUnit.MILLISECONDS)
            .writeTimeout(NetWork.TIME_OUT_HTTP, TimeUnit.MILLISECONDS).build();

    /**
     * Retrofit带进度的下载方法
     *
     * @param filePath 文件路径
     * @param listener 进度监听器
     * @param callback 请求结果回调
     */
    public static void downloadFileProgress(String filePath, final ProgressListener listener, Callback<ResponseBody> callback) {
        OkHttpClient client = okHttpClient.newBuilder().addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                okhttp3.Response response = chain.proceed(chain.request());
                return response.newBuilder().body(new ProgressResponseBody(response.body(), listener)).build();
            }
        }).build();
        //设置Gson的非严格模式
        Gson gson = new GsonBuilder().setLenient().create();
        // 初始化Retrofit
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(new StringConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        Call<ResponseBody> downloadCall = mRetrofit.create(NjMeterApi.class).downloadFile(filePath);
        downloadCall.enqueue(callback);
    }

}
