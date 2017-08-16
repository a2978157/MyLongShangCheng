package along.mifeng.us.mylongshangcheng;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


/**
 * 商品详情界面
 * Created by 21903 on 2017/5/8.
 */

public class MyXiangQing extends Activity implements View.OnClickListener {

    private String id;
    private TextView xqtv;
    private TextView jiage;
    private TextView jiage2;
    private TextView zhekou;
    private ImageView iv;
    private WebView iv2;
    private ImageView xing;
    int a = 0;
    private PopupWindow pop;
    private TextView goumai;
    private String shop_price;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xiangqing);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView();
        Intent intent = getIntent();
        id = intent.getStringExtra("id").toString();
        new Thread(new Runnable() {
            private StringBuffer sb;

            @Override
            public void run() {
                try {
                    String s1 = MD5Util.md5(id + TimeUtil.getTime() + "shop.sdlinwang.com");
                    String s = "id=" + URLEncoder.encode(id, "UTF-8") + "&time=" + URLEncoder.encode(String.valueOf(TimeUtil.getTime()), "UTF-8") + "&sign=" + URLEncoder.encode(s1, "UTF-8");
                    System.out.println(s);
                    byte[] bytes = s.getBytes();
                    URL url = new URL("http://shop.sdlinwang.com/index.php?m=Api&c=Goods&a=goodsInfo");
                    HttpURLConnection http = (HttpURLConnection) url.openConnection();
                    http.setDoInput(true);
                    http.setDoOutput(true);
                    OutputStream os = http.getOutputStream();
                    os.write(bytes);
                    os.flush();
                    os.close();
                    InputStream is = http.getInputStream();
                    byte[] by = new byte[1024];
                    int i = 0;
                    sb = new StringBuffer();
                    while ((i = is.read(by)) != -1) {
                        sb.append(new String(by, 0, i));
                    }
                    JSONObject jsonObject = new JSONObject(sb.toString());
                    JSONObject result = jsonObject.getJSONObject("result");
                    JSONObject goods = result.getJSONObject("goods");
                    final String discount = goods.getString("discount");
                    final String goods_name = goods.getString("goods_name");
                    final String market_price = goods.getString("market_price");
                    shop_price = goods.getString("shop_price");
                    final String goods_content = goods.getString("goods_content");
                    final String original_img = goods.getString("original_img");
                    final String goods_id = goods.getString("goods_id");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            xqtv.setText(goods_name);
                            jiage.setText(shop_price);
                            jiage2.setText(market_price);
                            zhekou.setText(discount);
                            System.out.println(goods_content);
                            Glide.with(MyXiangQing.this).load(original_img).placeholder(R.mipmap.tupian).thumbnail(0.01f).into(iv);
                            WebSettings ws = iv2.getSettings();
                            ws.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                            ws.setJavaScriptEnabled(true);
                            ws.setDatabaseEnabled(true);
                            iv2.loadUrl("http://shop.sdlinwang.com/index.php/Mobile/Goods/goodsInfo2" + "/id/" + goods_id);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void initView() {
        xqtv = (TextView) findViewById(R.id.xqtv);
        jiage = (TextView) findViewById(R.id.jiage);
        jiage2 = (TextView) findViewById(R.id.jiage2);
        zhekou = (TextView) findViewById(R.id.zhikou);
        xing = (ImageView) findViewById(R.id.xing);
        LinearLayout shoucang = (LinearLayout) findViewById(R.id.shoucang);
        LinearLayout kefu = (LinearLayout) findViewById(R.id.kefu);
        goumai = (TextView) findViewById(R.id.goumai);
        goumai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyXiangQing.this, DingDan.class);
                intent.putExtra("jiage",shop_price);
                startActivity(intent);
            }
        });
        LayoutInflater inflater = LayoutInflater.from(MyXiangQing.this);
        // 引入窗口配置文件
        View vv = inflater.inflate(R.layout.keyfu, null);
        Button boda = (Button) vv.findViewById(R.id.boda);
        Button quexiao = (Button) vv.findViewById(R.id.quexiao);
        //创建PopupWindow对象，指定宽度和高度(new PopupWindow(view, 400, 600)后2个参数是指定宽高;
        pop = new PopupWindow(vv, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, false);
        // 设置点击窗口外边窗口消失
        pop.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        pop.setFocusable(true);
        //更新popupwindow的状态
        pop.update();
        kefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 显示窗口在布局底部
                pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            }
        });
        boda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //用intent启动拨打电话  
                String s = "15138337441";
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_CALL);
                //url:统一资源定位符  
                //uri:统一资源标示符（更广）  
                intent.setData(Uri.parse("tel:"+s));
                //开启系统拨号器  
                startActivity(intent);
                pop.dismiss();
            }
        });
        quexiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop.dismiss();
            }
        });
        shoucang.setOnClickListener(MyXiangQing.this);
        iv = (ImageView) findViewById(R.id.iv);
        ImageView fanhui = (ImageView) findViewById(R.id.fanhui);
        iv2 = (WebView) findViewById(R.id.iv2);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (a == 0) {
            a = 1;
            Toast.makeText(MyXiangQing.this,"已收藏",Toast.LENGTH_SHORT).show();
            xing.setBackgroundResource(R.mipmap.collection2);
        } else {
            a = 0;
            Toast.makeText(MyXiangQing.this,"已取消收藏",Toast.LENGTH_SHORT).show();
            xing.setBackgroundResource(R.mipmap.collection);
        }

    }
}
