package proxy.zj.com.networklivedata.rx;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TimeUtils;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

import proxy.zj.com.networklivedata.R;
import proxy.zj.com.networklivedata.bean.TestData;
import proxy.zj.com.networklivedata.okgo.OkGoActivity;
import proxy.zj.com.networklivedata.okgo.OkGoViewModel;

public class RxActivity extends AppCompatActivity {

    private TextView content;
    private String TAG = "RxActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx);

        content = findViewById(R.id.content);

        final RxJavaViewModel model = ViewModelProviders.of(this).get(RxJavaViewModel.class);

        findViewById(R.id.getdata).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                MutableLiveData data = model.getData();

                data.observe(RxActivity.this, new Observer<TestData>() {
                    @Override
                    public void onChanged(@Nullable TestData o) {
                        Date a = new Date(System.currentTimeMillis());
                        content.setText( proxy.zj.com.networklivedata.TimeUtils.date2String(a) +"   >>>>>   "+o.getContent());
                    }
                });

            }

        });

    }






}
