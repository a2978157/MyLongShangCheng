package along.mifeng.us.mylongshangcheng;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

public class MyApplication extends Application{
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		SDKInitializer.initialize(this);
	}
}
