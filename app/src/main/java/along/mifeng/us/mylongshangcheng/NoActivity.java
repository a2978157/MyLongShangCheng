package along.mifeng.us.mylongshangcheng;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import Utils.SPUtils;

/**
 * Created by 21903 on 2017/7/4.
 */

public class NoActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String no = new SPUtils().getShared2(this, "No");
        if (no.equals("")){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }else {
            startActivity(new Intent(this,HuanYing.class));
            finish();
        }
    }
}
