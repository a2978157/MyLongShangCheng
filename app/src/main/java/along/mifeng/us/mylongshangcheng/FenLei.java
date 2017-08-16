package along.mifeng.us.mylongshangcheng;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import MyFragment.Jsonben;

/**
 * Created by 21903 on 2017/5/11.
 */

public class FenLei extends Activity {
    ArrayList<Jsonben> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private Dialog dialog;
    private WindowManager.LayoutParams wl;
    private Button sanchu;
    private Button quexiao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myfeilei);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        LayoutInflater inflater = LayoutInflater.from(FenLei.this);
        // 引入窗口配置文件
        View vv = inflater.inflate(R.layout.sanchu, null);
        sanchu = (Button) vv.findViewById(R.id.sanchu);
        quexiao = (Button) vv.findViewById(R.id.quexiao);
        dialog = new Dialog(FenLei.this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(vv, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        wl = window.getAttributes();
        wl.x = 0;
        wl.y =getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        initView();
        initWang();
    }

    private void initWang() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://shop.sdlinwang.com/index.php?m=Api&c=Index&a=homePage");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    InputStream inputStream = conn.getInputStream();
                    byte[] by = new byte[1024];
                    int len = 0;
                    StringBuffer sb = new StringBuffer();
                    while ((len = inputStream.read(by)) != -1) {
                        sb.append(new String(by, 0, len));
                    }
                    inputStream.close();
                    JSONObject jsonObject = new JSONObject(sb.toString());
                    JSONObject result = jsonObject.getJSONObject("result");
                    final JSONArray high_quality_goods = result.getJSONArray("high_quality_goods");
                    for (int i = 0; i < high_quality_goods.length(); i++) {
                        JSONObject jsonObject1 = high_quality_goods.getJSONObject(i);
                        String goods_id = jsonObject1.getString("goods_id");
                        String goods_name = jsonObject1.getString("goods_name");
                        String original_img = jsonObject1.getString("original_img");
                        String shop_price = jsonObject1.getString("shop_price");
                        Jsonben jsonben = new Jsonben();
                        jsonben.setGoods_id(goods_id);
                        jsonben.setGoods_name(goods_name);
                        jsonben.setOriginal_img("http://shop.sdlinwang.com" + original_img);
                        jsonben.setShop_price(shop_price);
                        list.add(jsonben);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setLayoutManager(new LinearLayoutManager(FenLei.this));
                            final MyAdapter myAdapter = new MyAdapter(FenLei.this, list);
                            recyclerView.setAdapter(myAdapter);
                            //设置条目动画：默认的
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.addItemDecoration(new DividerItemDecoration(
                                    FenLei.this,DividerItemDecoration.VERTICAL_LIST));
                            myAdapter.setOnItemClickListener(new MyAdapter.onItemClickListerner() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Intent intent = new Intent();
                                    intent.setClass(FenLei.this,MyXiangQing.class);
                                    intent.putExtra("id",list.get(position).getGoods_id());
                                    startActivity(intent);
                                }

                                @Override
                                public void onLongItemClick(View view, final int position) {
                                    // TODO Auto-generated method stub
                                    dialog.onWindowAttributesChanged(wl);
                                    // 设置点击外围解散
                                    dialog.setCanceledOnTouchOutside(true);
                                    dialog.show();
                                    sanchu.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            //调用适配器里面的删除的方法
                                            myAdapter.RemoveView(position);
                                            dialog.dismiss();
                                        }
                                    });
                                    quexiao.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    });
                                }
                            });

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        ImageView fanhui= (ImageView) findViewById(R.id.fanhui);
        TextView fenlei= (TextView) findViewById(R.id.fenlei);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        fenlei.setText(name);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
