package com.pioneeriot.pioneeriot.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;

import com.pioneeriot.pioneeriot.constant.Constant;
import com.pioneeriot.pioneeriot.network.NetClient;
import com.pioneeriot.pioneeriot.bean.NormalResult;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by LiYuliang on 2017/9/22 0022.
 * 程序崩溃处理,输出到日志，上传到服务器
 *
 * @author LiYuliang
 * @version 2017/11/11
 */

public class CrashHandler implements UncaughtExceptionHandler {

    /**
     * 系统默认的UncaughtException处理类
     */
    private UncaughtExceptionHandler mDefaultHandler;

    /**
     * CrashHandler实例
     */
    private static CrashHandler instance;

    /**
     * 程序的Context对象
     */
    private Context mContext;

    /**
     * Map表：用来存储设备信息和异常信息
     */
    private Map<String, String> infos = new HashMap<>();

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        if (instance == null) {
            instance = new CrashHandler();
        }
        return instance;
    }

    /**
     * CrashHandler构造器：无实现， 保证只有一个CrashHandler实例
     */
    private CrashHandler() {

    }

    /**
     * 初始化：设置UncaughtException为程序“未补捉异常”的默认处理器
     */
    public void init(Context context) {
        mContext = context;
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // 收集设备参数信息
        collectDeviceInfo(mContext);
        // 保存日志文件
        saveCatchInfo2File(ex);
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex 异常
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 收集设备参数信息
        collectDeviceInfo(mContext);
        // 保存日志文件
        saveCatchInfo2File(ex);
        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx Context对象
     */
    private void collectDeviceInfo(Context ctx) {
        try {
            // 获取包信息
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        // 获取Build的所有信息
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex 错误信息
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCatchInfo2File(Throwable ex) {
        // 拼接字符串
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }
        // 拼接异常信息
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            // 将崩溃信息写入txt文件
            String model = android.os.Build.MODEL;
            String fileName = "PioneerIoT-" + TimeUtils.getCurrentFormatDateTime() + "-" + model + ".txt";
            String path = getRoot();
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(path + File.separator + fileName);
            fos.write(sb.toString().getBytes());
            // 发送给开发人员
            sendCrashLog2PM(path + File.separator + fileName);
            fos.close();
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将捕获的导致崩溃的错误信息发送给开发人员 目前只将log日志保存在sdcard 和输出到LogCat中，并未发送给后台。
     */
    private void sendCrashLog2PM(String fileName) {

        // 文件内容字符串
        String content = "";
        InputStream instream = null;
        BufferedReader buffreader = null;

        // 打开文件
        File file = new File(fileName);
        // 创建 RequestBody，用于封装构建RequestBody
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // MultipartBody.Part  和后端约定好Key，这里的partName是用image
        MultipartBody.Part body = MultipartBody.Part.createFormData("uploadfile", file.getName(), requestFile);
        Call<NormalResult> call = NetClient.getInstances(NetClient.BASE_URL_PROJECT).getNjMeterApi().uploadCrashFiles(body);
        call.enqueue(new Callback<NormalResult>() {
            @Override
            public void onResponse(@NotNull Call<NormalResult> call, @NotNull Response<NormalResult> response) {
                LogUtils.d("retrofit", response.toString());
                if (response.isSuccessful()) {
                    NormalResult normalResult = response.body();
                    if (normalResult == null) {
                        LogUtils.d("retrofit", "返回值异常，上传失败" + response.message());
                    } else {
                        String result = normalResult.getResult();
                        switch (result) {
                            case Constant.SUCCESS:
                                LogUtils.d("retrofit", "错误日志上传成功");
                                break;
                            case Constant.FAIL:
                                LogUtils.d("retrofit", "服务器保存异常，上传失败");
                                break;
                            default:
                                LogUtils.d("retrofit", "未知错误，上传失败");
                                break;
                        }
                    }
                } else {
                    LogUtils.d("retrofit", "上传失败，" + response.message());
                }
            }

            @Override
            public void onFailure(@NotNull Call<NormalResult> call, @NotNull Throwable throwable) {
                LogUtils.d("retrofit", "上传失败，" + throwable.getMessage());
            }
        });


        // 如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory()) {
        } else {
            try {
                instream = new FileInputStream(file);
                if (instream != null) {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    buffreader = new BufferedReader(inputreader);
                    String line;
                    // 分行读取
                    while ((line = buffreader.readLine()) != null) {
                        content += line + "\n";
                    }
                    instream.close();
                }
            } catch (java.io.FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // 关闭流
                try {
                    if (buffreader != null) {
                        buffreader.close();
                    }
                    if (instream != null) {
                        instream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getRoot() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().getAbsolutePath() + "/ata/crash";
        } else {
            return "/ata/crash";
        }
    }

}
