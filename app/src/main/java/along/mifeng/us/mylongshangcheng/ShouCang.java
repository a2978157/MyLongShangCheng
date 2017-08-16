package along.mifeng.us.mylongshangcheng;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import MyFragment.Jsonben;

import static along.mifeng.us.mylongshangcheng.R.id.sl;

/**
 * Created by 21903 on 2017/5/11.
 */

public class ShouCang extends Activity {
    ArrayList<Jsonben> list = new ArrayList<>();
    private ListView recyclerView;
    private Dialog dialog;
    private WindowManager.LayoutParams wl;
    private Button sanchu;
    private Button quexiao;
    private MySwipeAdapter myAdapter;
    private SwipeRefreshLayout sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoucang);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        LayoutInflater inflater = LayoutInflater.from(ShouCang.this);
        // 引入窗口配置文件
        View vv = inflater.inflate(R.layout.sanchu, null);
        sanchu = (Button) vv.findViewById(R.id.sanchu);
        quexiao = (Button) vv.findViewById(R.id.quexiao);
        dialog = new Dialog(ShouCang.this, R.style.transparentFrameWindowStyle);
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
                            /*recyclerView.setLayoutManager(new LinearLayoutManager(ShouCang.this));*/
                            myAdapter = new MySwipeAdapter();
                            recyclerView.setAdapter(myAdapter);
                            /*//设置条目动画：默认的
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.addItemDecoration(new DividerItemDecoration(
                                    ShouCang.this,DividerItemDecoration.VERTICAL_LIST));*/
                            //布局动画管理器
                            LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(ShouCang.this, R.anim.listdonghua));
                            //设置布局动画的顺序，一共有三种
                            lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
                            //布局动画设置到listview上
                            recyclerView.setLayoutAnimation(lac);
                            //开启动画
                            recyclerView.startLayoutAnimation();

                           /* myAdapter.setOnItemClickListener(new MyAdapter.onItemClickListerner() {
                                @Override
                                public void onItemClick(View view, int position) {
                                        Intent intent = new Intent();
                                        intent.setClass(ShouCang.this,MyXiangQing.class);
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
                            });*/

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void initView() {
        recyclerView = (ListView) findViewById(R.id.recyclerview);
        sw = (SwipeRefreshLayout) findViewById(R.id.swipe);
        sw.setProgressBackgroundColorSchemeResource(android.R.color.white);
        sw.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        sw.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        sw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sw.setRefreshing(true);
                handler.sendEmptyMessageDelayed(1,2000);
            }
        });
        ImageView fanhui= (ImageView) findViewById(R.id.fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                myAdapter.notifyDataSetChanged();
                sw.setRefreshing(false);
            }
        }
    };
    public class MySwipeAdapter extends BaseSwipeAdapter {
        boolean bb=false;

        @Override
        public int getSwipeLayoutResourceId(int position) {
            return sl;
        }

        @Override
        public View generateView(int position, ViewGroup parent) {
            View v = View.inflate(ShouCang.this, R.layout.listitmell, null);
            SwipeLayout sl = (SwipeLayout) v.findViewById(getSwipeLayoutResourceId(position));
            sl.setShowMode(SwipeLayout.ShowMode.PullOut);
            sl.setClickToClose(true);
            sl.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onStartOpen(SwipeLayout layout) {
                }

                @Override
                public void onOpen(SwipeLayout layout) {
                    bb=true;
                }

                @Override
                public void onStartClose(SwipeLayout layout) {
                }

                @Override
                public void onClose(SwipeLayout layout) {
                    bb=false;
                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

                }
            });
            return v;
        }

        @Override
        public void fillValues(final int position, View convertView) {
            TextView dakai=(TextView) convertView.findViewById(R.id.dakai);
            TextView shanchu=(TextView) convertView.findViewById(R.id.shanchu);
            LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.lll);
            TextView tv= (TextView) convertView.findViewById(R.id.tvv);
            TextView jiage= (TextView) convertView.findViewById(R.id.jiage);
            ImageView iv= (ImageView) convertView.findViewById(R.id.iv);
            tv.setText(list.get(position).getGoods_name());
            jiage.setText(list.get(position).getShop_price());
            Glide.with(ShouCang.this).load(list.get(position).getOriginal_img()).thumbnail(0.1f).into(iv);
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myAdapter.closeAllItems();
                    if (bb==false){
                        Intent intent = new Intent();
                        intent.setClass(ShouCang.this,MyXiangQing.class);
                        intent.putExtra("id",list.get(position).getGoods_id());
                        startActivity(intent);
                    }
                }
            });
            shanchu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 设置点击外围解散
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                    sanchu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            myAdapter.closeAllItems();
                            list.remove(position);
                            myAdapter.notifyDataSetChanged();
                            dialog.onWindowAttributesChanged(wl);
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
            dakai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myAdapter.closeAllItems();
                }
            });
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }
    }
}
