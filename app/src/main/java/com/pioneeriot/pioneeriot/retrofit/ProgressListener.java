package com.pioneeriot.pioneeriot.retrofit;

/**
 * Created by  on 2017/6/27.
 */
public interface ProgressListener {
    void onProgress(long currentBytes, long contentLength, boolean done);
}
