package along.mifeng.us.mylongshangcheng;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

/**
 * Created by 21903 on 2017/7/4.
 */

public class HuanYing extends Activity {
    int index=3;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.huanying);
        tv = (TextView) findViewById(R.id.tv);
        handler.sendEmptyMessageDelayed(0, 3000);
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (index > 1) {
                    try {
                        Thread.sleep(1000);
                        index--;

                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        msg.obj = index;
                        handler.sendMessage(msg);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (msg.what == 0) {
                Intent intent = new Intent(HuanYing.this, MyZhuYe.class);
                startActivity(intent);
                finish();
            }
            if (msg.what == 1) {
                int shuzi = (Integer) msg.obj;
                tv.setText(shuzi + "");
            }
        }
    };
}
