package com.opengl.deng.testnewrelic;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.opengl.deng.testnewrelic.analyzesdk.callback.IUserDefineName;

/**
 * @Description test IUserDefineName interface
 * Created by deng on 2018/6/23.
 */
public class WebviewActivity extends Activity implements IUserDefineName {
    String title;
    String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
    }

    @Override
    public String getSurfaceName() {
        return this.getClass().getName() + ":" + title;
    }
}
