package com.hhd.breath.app.net;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by familylove on 2016/5/22.
 */
public class ManagerRxRequest {

    private static ManagerRxRequest instance = null ;
    private RequestRxApi requestRxApi ;

    public static ManagerRxRequest getInstance(){
        if (instance == null)
            instance = new ManagerRxRequest() ;
        return instance ;
    }

    private ManagerRxRequest() {


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder() ;

        Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(NetConfig.URL_PREFIX)
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                                        .client(httpClient.build())
                                        .build() ;
        requestRxApi = retrofit.create(RequestRxApi.class) ;
    }

    /**
     *
     * @return RequestRxApi
     */
    public static RequestRxApi getRequestRxApi(){
        if (instance==null)
            instance = new ManagerRxRequest() ;

        return instance.requestRxApi ;
    }

    public void testObservable(){
       // Observable.just("hello world").map(url->Observable.from());
    }
}
