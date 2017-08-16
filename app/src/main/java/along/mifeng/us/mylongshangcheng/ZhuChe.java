package along.mifeng.us.mylongshangcheng;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by 21903 on 2017/7/4.
 */

public class ZhuChe extends Activity implements View.OnClickListener {

    private EditText yaoqingma;
    private EditText shoujihao;
    private EditText mima;
    private EditText xingming;
    private Button chuangjian;
    private RadioButton nan;
    private RadioButton nv;
    private ImageButton fanhui;
    String xing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhuche);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView();
    }

    private void initView() {
        yaoqingma = (EditText) findViewById(R.id.yaoqingma);
        shoujihao = (EditText) findViewById(R.id.shoujihao);
        mima = (EditText) findViewById(R.id.mima);
        xingming = (EditText) findViewById(R.id.name);
        chuangjian = (Button) findViewById(R.id.chuangjian);
        nan = (RadioButton) findViewById(R.id.nan);
        nv = (RadioButton) findViewById(R.id.nv);
        fanhui = (ImageButton) findViewById(R.id.fanhui);
        fanhui.setOnClickListener(this);
        chuangjian.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fanhui:
                finish();
                break;
            case R.id.chuangjian:
                if (yaoqingma.getText().toString().equals("")){
                    Toast.makeText(ZhuChe.this,"邀请码不能为空！",Toast.LENGTH_LONG).show();
                }else if (shoujihao.getText().toString().equals("")){
                    Toast.makeText(ZhuChe.this,"手机号不能为空！",Toast.LENGTH_LONG).show();
                }else if (mima.getText().toString().equals("")){
                    Toast.makeText(ZhuChe.this,"密码不能为空！",Toast.LENGTH_LONG).show();
                }else if (xingming.getText().toString().equals("")){
                    Toast.makeText(ZhuChe.this,"姓名不能为空！",Toast.LENGTH_LONG).show();
                }else if (nan.isChecked()==false&&nv.isChecked()==false){
                    Toast.makeText(ZhuChe.this,"请选择性别！",Toast.LENGTH_LONG).show();
                }else {
                    if (nan.isChecked()){
                        xing="男";
                    }
                    if (nv.isChecked()){
                        xing="女";
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String s ="&code=" + URLEncoder.encode(yaoqingma.getText().toString(), "UTF-8") + "&mobile=" + URLEncoder.encode(shoujihao.getText().toString(), "UTF-8")
                                        + "&name=" + URLEncoder.encode(xingming.getText().toString(), "UTF-8")+ "&password=" + URLEncoder.encode(mima.getText().toString(), "UTF-8")
                                        + "&gender=" + URLEncoder.encode(xing, "UTF-8");
                                byte[] bytes = s.getBytes();
                                URL url = new URL("http://192.168.190.188/Goods/app/common/register.json");
                                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                                http.setDoInput(true);
                                http.setDoOutput(true);
                                OutputStream os = http.getOutputStream();
                                os.write(bytes);
                                os.flush();
                                os.close();
                                InputStream is = http.getInputStream();
                                byte[] by = new byte[1024];
                                int ii = 0;
                                final StringBuffer sb = new StringBuffer();
                                while ((ii = is.read(by)) != -1) {
                                    sb.append(new String(by, 0, ii));
                                }
                                Log.e("e", sb.toString());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject jsonObject = new JSONObject(sb.toString());
                                            String status = jsonObject.getString("status");
                                            String info = jsonObject.getString("info");
                                            if (status.equals("200")){
                                                Toast.makeText(ZhuChe.this,info,Toast.LENGTH_LONG).show();
                                                final ProgressDialog progressDialog = new ProgressDialog(ZhuChe.this,
                                                        R.style.AppTheme_Dark_Dialog);
                                                progressDialog.setIndeterminate(true);
                                                progressDialog.setMessage("账号创建中...");
                                                progressDialog.show();
                                                new Handler().postDelayed(
                                                        new Runnable() {
                                                            public void run() {
                                                                progressDialog.dismiss();
                                                                finish();
                                                            }
                                                        }, 3000);
                                            }else {
                                                Toast.makeText(ZhuChe.this,info,Toast.LENGTH_LONG).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }

                break;
        }
    }
}
