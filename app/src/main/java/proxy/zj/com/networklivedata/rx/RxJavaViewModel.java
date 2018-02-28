package proxy.zj.com.networklivedata.rx;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;


import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import proxy.zj.com.networklivedata.bean.TestData;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by thinkpad on 2018/2/27.
 */
public class RxJavaViewModel extends ViewModel {

    private static final String TAG = "RxJavaViewModel";

    private MutableLiveData<TestData> data = new MutableLiveData<>();

    RequestApi retrofitPostApi = null;

    public RxJavaViewModel() {
        init();
    }

    public MutableLiveData getData() {
        loadData();
        return data;
    }

    private void init() {
        //初始化Retrofit实例
        Retrofit retrofit = initRetrofit();

        //这里采用的是Java的动态代理模式，得到请求接口对象
        retrofitPostApi = retrofit.create(RequestApi.class);
    }

    private void loadData() {
        String url = "v2/book/1003078";
        retrofitPostApi.getPathDoubadataRx(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG,"onSubscribe");
                    }

                    @Override
                    public void onNext(String value) {
                        Log.e(TAG,"onNext");
                        data.setValue(new TestData(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG,"onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG,"onComplete");
                    }
                });
    }

    /**
     * 初始化Retrofit实例
     * @return
     */
    private Retrofit initRetrofit() {
        //日志拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                //打印retrofit日志
                Log.i(TAG, "retrofitBack = " + message);
            }
        });
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        OkHttpClient client = builder
                //超时时间 不设置的话 默认30秒
                .addInterceptor(loggingInterceptor)
//                .connectTimeout(mTimeOut, TimeUnit.SECONDS)
//                .readTimeout(mTimeOut, TimeUnit.SECONDS)
//                .writeTimeout(mTimeOut, TimeUnit.SECONDS)
                .build();

        String BASE_URL = "https://api.douban.com/"; //豆瓣接口

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(BASE_URL)//baseurl设置
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())//增加返回值为Gson的支持（返回实体类）
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//这个adapter很重要，专门找了方案来解决这个问题
                .build();

        return retrofit;
    }


//    /**
//     * 创建retrofit
//     *
//     * @return
//     */
//    @NonNull
//    private Retrofit createRetrofit() {
//        String BASE_URL = "https://api.douban.com/"; //豆瓣接口
//        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
//
//        builder.readTimeout(10, TimeUnit.SECONDS);
//        builder.connectTimeout(9, TimeUnit.SECONDS);
//
//        return new Retrofit.Builder()
//                .client(builder.build())
//                .baseUrl(BASE_URL)//baseurl设置
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())//增加返回值为Gson的支持（返回实体类）
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//这个adapter很重要，专门找了方案来解决这个问题
//                .build();
//    }

}
