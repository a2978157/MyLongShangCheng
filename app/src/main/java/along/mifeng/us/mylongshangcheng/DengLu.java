package along.mifeng.us.mylongshangcheng;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by 21903 on 2017/5/12.
 */

public class DengLu extends Activity implements View.OnClickListener {
    private boolean isHidden = true;
    private EditText zhanghao;
    private EditText mima;
    private RadioButton display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.denglu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView();

    }

    private void initView() {
        ImageButton fanhui = (ImageButton) findViewById(R.id.fanhui);
        TextView zhuche = (TextView) findViewById(R.id.zhuche);
        zhanghao = (EditText) findViewById(R.id.zhanghao);
        mima = (EditText) findViewById(R.id.mima);
        Button denglu = (Button) findViewById(R.id.denglu);
        display = (RadioButton) findViewById(R.id.ib_display);
        fanhui.setOnClickListener(this);
        denglu.setOnClickListener(this);
        display.setOnClickListener(this);
        zhuche.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.zhuche:
                startActivity(new Intent(DengLu.this,ZhuChe.class));
                break;
            case R.id.fanhui:
                finish();
                break;
            case R.id.ib_display:
                if (isHidden) {
                    mima.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    display.setChecked(false);
                    mima.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                isHidden = !isHidden;
                mima.postInvalidate();
                CharSequence charSequence = mima.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
                break;
            case R.id.denglu:

                if (zhanghao.getText().toString().equals("")) {
                    Toast.makeText(DengLu.this, "账号不能为空", Toast.LENGTH_SHORT).show();
                } else if (mima.getText().toString().equals("")) {
                    Toast.makeText(DengLu.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String s ="&username=" + URLEncoder.encode(zhanghao.getText().toString(), "UTF-8") + "&password=" + URLEncoder.encode(mima.getText().toString(), "UTF-8");
                                byte[] bytes = s.getBytes();
                                URL url = new URL("http://192.168.190.188/Goods/app/common/login.json");
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
                                StringBuffer sb = new StringBuffer();
                                while ((ii = is.read(by)) != -1) {
                                    sb.append(new String(by, 0, ii));
                                }
                                Log.e("e", sb.toString());
                                final JSONObject jsonObject = new JSONObject(sb.toString());
                                final String status = jsonObject.getString("status");
                                final String info = jsonObject.getString("info");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (status.equals("200")) {
                                            try {
                                                JSONObject data = jsonObject.getJSONObject("data");
                                                String name = data.getString("name");
                                                String token = jsonObject.getString("token");
                                                Toast.makeText(DengLu.this, "登录成功", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent();
                                                intent.setAction("us.jianbao");
                                                intent.putExtra("ss", name);
                                                sendBroadcast(intent, null);
                                                final ProgressDialog progressDialog = new ProgressDialog(DengLu.this);
                                                progressDialog.setIndeterminate(true);
                                                progressDialog.setMessage("登录中...");
                                                progressDialog.show();
                                                new Handler().postDelayed(
                                                        new Runnable() {
                                                            public void run() {
                                                                progressDialog.dismiss();
                                                                finish();
                                                            }
                                                        }, 2000);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        } else {
                                            Toast.makeText(DengLu.this, info, Toast.LENGTH_SHORT).show();
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
