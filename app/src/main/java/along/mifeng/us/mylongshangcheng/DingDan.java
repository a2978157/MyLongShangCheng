package along.mifeng.us.mylongshangcheng;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 订单界面
 * Created by 21903 on 2017/5/12.
 */

public class DingDan extends Activity implements View.OnClickListener {

    private TextView jifen;
    private TextView mdizhi;
    private Dialog dialog;
    private WindowManager.LayoutParams wl;
    private TextView baocun;
    private TextView quexiao;
    private EditText xingming;
    private EditText dianhua;
    private EditText dizhi;
    private TextView shoujianren;
    private TextView dianhua1;
    private TextView heji;
    private Float ii;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dingdan);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView();
        LayoutInflater inflater = LayoutInflater.from(DingDan.this);
        // 引入窗口配置文件
        View vv = inflater.inflate(R.layout.dizhi, null);
        baocun = (TextView) vv.findViewById(R.id.baocun);
        quexiao = (TextView) vv.findViewById(R.id.quexiao);
        TextView baocun= (TextView) vv.findViewById(R.id.baocun);
        xingming = (EditText) vv.findViewById(R.id.xingming);
        dianhua = (EditText) vv.findViewById(R.id.dianhua);
        dizhi = (EditText) vv.findViewById(R.id.dizhi);
        baocun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String xingmingText = xingming.getText().toString();
                String dianhuaText = dianhua.getText().toString();
                String dizhiText = dizhi.getText().toString();
                if (xingmingText.equals("")){
                    Toast.makeText(DingDan.this,"收货人不能为空",Toast.LENGTH_SHORT).show();
                }else if (dianhuaText.equals("")){
                    Toast.makeText(DingDan.this,"电话不能为空",Toast.LENGTH_SHORT).show();
                }else if (dizhiText.equals("")){
                    Toast.makeText(DingDan.this,"地址不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    shoujianren.setText("收货人："+xingmingText);
                    dianhua1.setText("电话："+dianhuaText);
                    mdizhi.setText("收货地址："+dizhiText);
                    dialog.dismiss();
                }
            }
        });
        quexiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog = new Dialog(DingDan.this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(vv, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        wl = window.getAttributes();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    private void initView() {
        ImageView fanhui = (ImageView) findViewById(R.id.fanhui);
        ImageView jia = (ImageView) findViewById(R.id.jia);
        ImageView jian = (ImageView) findViewById(R.id.jian);
        LinearLayout shezhidizhi = (LinearLayout) findViewById(R.id.shezhidizhi);
        mdizhi = (TextView) findViewById(R.id.mdizhi);
        jifen = (TextView) findViewById(R.id.jifen);
        shoujianren = (TextView) findViewById(R.id.shoujianren);
        dianhua1 = (TextView) findViewById(R.id.dianhua);
        TextView tijiao= (TextView) findViewById(R.id.tijiao);
        heji = (TextView) findViewById(R.id.heji);
        Intent intent = getIntent();
        String jiage = intent.getStringExtra("jiage");
        ii = Float.valueOf(jiage).floatValue();
        String s=String.valueOf(ii -10);
        heji.setText(s);
        fanhui.setOnClickListener(this);
        jia.setOnClickListener(this);
        jian.setOnClickListener(this);
        shezhidizhi.setOnClickListener(this);
        tijiao.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fanhui:
                finish();
                break;
            case R.id.tijiao:
                Toast.makeText(DingDan.this,"提交成功",Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.jia:
                int i = Integer.valueOf(jifen.getText().toString()).intValue();
                int a=i + 1;
                Float iii=Float.valueOf(heji.getText().toString()).floatValue();
                String sss=String.valueOf(iii+ii-10);
                heji.setText(sss);
                String s = String.valueOf(a);
                jifen.setText(s);
                break;
            case R.id.jian:
                int iiiii = Integer.valueOf(jifen.getText().toString()).intValue();
                if (iiiii > 1) {
                    int aa=iiiii - 1;
                    Float iiii=Float.valueOf(heji.getText().toString()).floatValue();
                    String ssss=String.valueOf(iiii-(ii-10));
                    heji.setText(ssss);
                    String ss = String.valueOf(aa);
                    jifen.setText(ss);
                }
                break;
            case R.id.shezhidizhi:
                dialog.onWindowAttributesChanged(wl);
                // 设置点击外围解散
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                break;
        }

    }
}
