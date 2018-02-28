package proxy.zj.com.networklivedata.okgo;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import java.util.Date;

import okhttp3.Call;
import okhttp3.Response;
import proxy.zj.com.networklivedata.TimeUtils;
import proxy.zj.com.networklivedata.bean.TestData;

/**
 * Created by thinkpad on 2018/2/27.
 */

public class OkGoViewModel extends AndroidViewModel {

    private MutableLiveData<TestData> data = new MutableLiveData<>();

    public OkGoViewModel(Application application) {
        super(application);
    }

//    public OkGoViewModel(Application application) {
//        super(application);
//    }

    public MutableLiveData getData() {
        loadData();
        return data;
    }

    private void loadData() {
        OkGo.get("https://api.douban.com/v2/movie/top250?start=0&count=10")  // 请求方式和请求url
                .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                .cacheKey("cacheKey")            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .execute(new StringCallback() {

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        TestData ts = new TestData(TimeUtils.date2String(new Date(System.currentTimeMillis())) + "  >>>>>  " + s);
                        data.setValue(ts);
                    }

                });
    }

}
