package proxy.zj.com.networklivedata.okgo;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.OkGo;

import proxy.zj.com.networklivedata.R;
import proxy.zj.com.networklivedata.bean.TestData;

public class OkGoActivity extends AppCompatActivity {

    private TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_go);

        content = findViewById(R.id.content);
        //"https://api.douban.com/v2/movie/top250?start=0&count=10"

        OkGo.init(this.getApplication());

        //***************************************获取数据********************************************
        final OkGoViewModel model = ViewModelProviders.of(this).get(OkGoViewModel.class);
        findViewById(R.id.getdata).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MutableLiveData data = model.getData();
                data.observe(OkGoActivity.this, new Observer<TestData>() {
                    @Override
                    public void onChanged(@Nullable TestData o) {
                        content.setText(o.getContent());
                    }
                });
            }

        });
        //*******************************************************************************************


    }

}
