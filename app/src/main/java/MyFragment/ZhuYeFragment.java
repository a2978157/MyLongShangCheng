package MyFragment;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import Utils.AppToast;
import Utils.NetUtils;
import along.mifeng.us.mylongshangcheng.GuangKaoWei;
import along.mifeng.us.mylongshangcheng.MyXiangQing;
import along.mifeng.us.mylongshangcheng.R;

/**
 * Created by 21903 on 2017/4/21.
 */

public class ZhuYeFragment extends Fragment implements PullToRefreshBase.OnRefreshListener2<ScrollView> {
    private View v;
    private StringBuffer sb;
    ArrayList<String> vplistimg = new ArrayList<>();
    ArrayList<String> aplistimg = new ArrayList<>();
    ArrayList<String> guanggaolistimg = new ArrayList<>();
    ArrayList<Jsonben> hot_goodslsit = new ArrayList<>();
    ArrayList<Jsonben> chuxiaolsit = new ArrayList<>();
    ArrayList<Jsonben> jingpinglsit = new ArrayList<>();
    ArrayList<Jsonben> zuixinglsit = new ArrayList<>();
    private ImageView giv1;
    private ImageView giv2;
    private ImageView giv3;
    private ImageView giv4;
    private ImageView giv5;
    private LinearLayout leibiao;
    private MyList lv;
    private PopupWindow pop;
    private TextView shangping;
    Handler hd=new Handler();
    private PullToRefreshScrollView sv;
    private SliderLayout mSliderLayout;
    private PagerIndicator indicator;
    private Dialog dialog;
    private WindowManager.LayoutParams wl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = View.inflate(getActivity(), R.layout.zhuyefragment, null);
        boolean connected = NetUtils.isConnected(getActivity());
        if (connected) {
            boolean wifi = NetUtils.isWifi(getActivity());
            boolean rd = NetUtils.is3rd(getActivity());
            if (wifi) {
                AppToast.makeShortToast(getActivity(), "WIFI已经连接");
            }else if (rd) {
                AppToast.makeShortToast(getActivity(), "手机流量已经连接");
            }
        } else {
            AppToast.makeShortToast(getActivity(), "网络连接不可用，请检查网络设置");
            NetUtils.openSetting(getActivity());

        }
        initView();
        initWang();
        return v;
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
                    sb = new StringBuffer();
                    while ((len = inputStream.read(by)) != -1) {
                        sb.append(new String(by, 0, len));
                    }
                    inputStream.close();
                    JSONObject jsonObject = new JSONObject(sb.toString());
                    JSONObject result = jsonObject.getJSONObject("result");
                    JSONArray ad = result.getJSONArray("ad");
                    for (int i = 0; i < ad.length(); i++) {
                        JSONObject jsonObject1 = ad.getJSONObject(i);
                        String ad_code = jsonObject1.getString("ad_code");
                        String ad_name = jsonObject1.getString("ad_name");
                        aplistimg.add(ad_name);
                        vplistimg.add(ad_code);
                    }
                    JSONArray getCustomAd = result.getJSONArray("getCustomAd");
                    for (int i = 0; i < getCustomAd.length(); i++) {
                        JSONObject jsonObject1 = getCustomAd.getJSONObject(i);
                        String ad_code = jsonObject1.getString("ad_code");
                        guanggaolistimg.add(ad_code);
                    }
                    JSONArray hot_goods = result.getJSONArray("hot_goods");
                    for (int i = 0; i < hot_goods.length(); i++) {
                        JSONObject jsonObject1 = hot_goods.getJSONObject(i);
                        String goods_id = jsonObject1.getString("goods_id");
                        String goods_name = jsonObject1.getString("goods_name");
                        String original_img = jsonObject1.getString("original_img");
                        String shop_price = jsonObject1.getString("shop_price");
                        Jsonben jsonben = new Jsonben();
                        jsonben.setGoods_id(goods_id);
                        jsonben.setGoods_name(goods_name);
                        jsonben.setOriginal_img("http://shop.sdlinwang.com" + original_img);
                        jsonben.setShop_price(shop_price);
                        hot_goodslsit.add(jsonben);
                    }
                    JSONArray promotion_goods = result.getJSONArray("promotion_goods");
                    for (int i = 0; i < promotion_goods.length(); i++) {
                        JSONObject jsonObject1 = promotion_goods.getJSONObject(i);
                        String goods_id = jsonObject1.getString("goods_id");
                        String goods_name = jsonObject1.getString("goods_name");
                        String original_img = jsonObject1.getString("original_img");
                        String shop_price = jsonObject1.getString("shop_price");
                        Jsonben jsonben = new Jsonben();
                        jsonben.setGoods_id(goods_id);
                        jsonben.setGoods_name(goods_name);
                        jsonben.setOriginal_img("http://shop.sdlinwang.com" + original_img);
                        jsonben.setShop_price(shop_price);
                        chuxiaolsit.add(jsonben);
                    }
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
                        jingpinglsit.add(jsonben);
                    }
                    JSONArray zuixin_goods = result.getJSONArray("zuixin_goods");
                    for (int i = 0; i < zuixin_goods.length(); i++) {
                        JSONObject jsonObject1 = zuixin_goods.getJSONObject(i);
                        String goods_id = jsonObject1.getString("goods_id");
                        String goods_name = jsonObject1.getString("goods_name");
                        String original_img = jsonObject1.getString("original_img");
                        String shop_price = jsonObject1.getString("shop_price");
                        Jsonben jsonben = new Jsonben();
                        jsonben.setGoods_id(goods_id);
                        jsonben.setGoods_name(goods_name);
                        jsonben.setOriginal_img("http://shop.sdlinwang.com" + original_img);
                        jsonben.setShop_price(shop_price);
                        zuixinglsit.add(jsonben);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(getActivity()).load(guanggaolistimg.get(0)).thumbnail(0.1f).into(giv1);
                            Glide.with(getActivity()).load(guanggaolistimg.get(1)).thumbnail(0.1f).into(giv2);
                            Glide.with(getActivity()).load(guanggaolistimg.get(2)).thumbnail(0.1f).into(giv3);
                            Glide.with(getActivity()).load(guanggaolistimg.get(3)).thumbnail(0.1f).into(giv4);
                            Glide.with(getActivity()).load(guanggaolistimg.get(4)).thumbnail(0.1f).into(giv5);
                            lv.setAdapter(new MyListAdapter(getActivity(), hot_goodslsit));
                            lv.setFocusable(true);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent intent = new Intent();
                                    intent.setClass(getActivity(), MyXiangQing.class);
                                    intent.putExtra("id", hot_goodslsit.get(i).getGoods_id());
                                    startActivity(intent);

                                }
                            });
                            initSlider();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void initView() {
        mSliderLayout = (SliderLayout) v.findViewById(R.id.slider);
        indicator = (PagerIndicator) v.findViewById(R.id.custom_indicator);
        giv1 = (ImageView) v.findViewById(R.id.giv1);
        giv2 = (ImageView) v.findViewById(R.id.giv2);
        giv3 = (ImageView) v.findViewById(R.id.giv3);
        giv4 = (ImageView) v.findViewById(R.id.giv4);
        giv5 = (ImageView) v.findViewById(R.id.giv5);
        giv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GuangKaoWei.class);
                startActivity(intent);
            }
        });
        giv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GuangKaoWei.class);
                startActivity(intent);
            }
        });
        giv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GuangKaoWei.class);
                startActivity(intent);
            }
        });
        giv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GuangKaoWei.class);
                startActivity(intent);
            }
        });
        giv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GuangKaoWei.class);
                startActivity(intent);
            }
        });
        giv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GuangKaoWei.class);
                startActivity(intent);
            }
        });
        shangping = (TextView) v.findViewById(R.id.shangping);
        leibiao = (LinearLayout) v.findViewById(R.id.leibiao);
        lv = (MyList) v.findViewById(R.id.lv);
        sv = (PullToRefreshScrollView) v.findViewById(R.id.sv);
        //*sv.smoothScrollTo(0,0);*//*
        ILoadingLayout loadingLayoutProxy = sv
                .getLoadingLayoutProxy();
        // 提示用户松手刷新时的文本
        loadingLayoutProxy.setReleaseLabel("释放时刷新");
        // 正在加载数据时显示的文本
        loadingLayoutProxy.setRefreshingLabel("正在刷新");
        // 下拉时显示的文本
        loadingLayoutProxy.setPullLabel("继续拉刷新");
        // 获取一个bitmap对象
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.mipmap.pull_icon_big);
        // 设置刷新动画中的图片
        loadingLayoutProxy.setLoadingDrawable(new BitmapDrawable(
                getResources(), bitmap));
        sv.setOnRefreshListener(this);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        // 引入窗口配置文件
        View v = inflater.inflate(R.layout.leibiaoduihuakuang, null);
        TextView zuire = (TextView) v.findViewById(R.id.zuire);
        TextView chuxiao = (TextView) v.findViewById(R.id.chuxiao);
        TextView jingping = (TextView) v.findViewById(R.id.jingping);
        TextView zuixing = (TextView) v.findViewById(R.id.zuixing);
        //创建PopupWindow对象，指定宽度和高度(new PopupWindow(view, 400, 600)后2个参数是指定宽高;
        pop = new PopupWindow(v, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, false);
        // 设置点击窗口外边窗口消失
        pop.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        pop.setFocusable(true);
        //更新popupwindow的状态
        pop.update();
        zuire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shangping.setText("最热商品");
                lv.setAdapter(new MyListAdapter(getActivity(), hot_goodslsit));
                pop.dismiss();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), MyXiangQing.class);
                        intent.putExtra("id", hot_goodslsit.get(i).getGoods_id().toString());
                        startActivity(intent);
                    }
                });
            }
        });
        chuxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shangping.setText("促销商品");
                lv.setAdapter(new MyListAdapter(getActivity(), chuxiaolsit));
                pop.dismiss();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), MyXiangQing.class);
                        intent.putExtra("id", chuxiaolsit.get(i).getGoods_id().toString());
                        startActivity(intent);
                        System.out.println("55555555555555");
                    }
                });
            }
        });
        jingping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shangping.setText("精品商品");
                lv.setAdapter(new MyListAdapter(getActivity(), jingpinglsit));
                pop.dismiss();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), MyXiangQing.class);
                        intent.putExtra("id", jingpinglsit.get(i).getGoods_id().toString());
                        startActivity(intent);
                        System.out.println("55555555555555");
                    }
                });
            }
        });
        zuixing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shangping.setText("最新商品");
                lv.setAdapter(new MyListAdapter(getActivity(), zuixinglsit));
                pop.dismiss();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), MyXiangQing.class);
                        intent.putExtra("id", zuixinglsit.get(i).getGoods_id().toString());
                        startActivity(intent);
                        System.out.println("55555555555555");
                    }
                });
            }
        });
        leibiao.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (pop.isShowing()) {
                    pop.dismiss();
                } else {
                    pop.showAsDropDown(v);
                }

            }
        });
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                vplistimg.clear();
                aplistimg.clear();
                guanggaolistimg.clear();
                hot_goodslsit.clear();
                chuxiaolsit.clear();
                jingpinglsit.clear();
                zuixinglsit.clear();
                initWang();
                sv.onRefreshComplete();
            }
        },2000);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                sv.onRefreshComplete();
            }
        },1000);
    }



    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            lv.setFocusable(false);
        }
    }

    private void initSlider() {
        mSliderLayout.removeAllSliders();
        for (int i = 0; i < aplistimg.size(); i++) {
            /*DefaultSliderView defaultSliderView = new DefaultSliderView(getActivity());
            defaultSliderView.image(vplistimg.get(i));*/
            TextSliderView textSliderView = new TextSliderView(getActivity());
            textSliderView.image(vplistimg.get(i));
            textSliderView.description(aplistimg.get(i));
            final int ii=i;
            textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView baseSliderView) {
                    Toast.makeText(getActivity(), "a"+ii, Toast.LENGTH_LONG).show();
                }
            });
            //添加到容器中
            mSliderLayout.addSlider(textSliderView);
        }
        //使用默认的指示器
        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
        mSliderLayout.setCustomIndicator(indicator);
        //阴影显示的动画效果
        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        //图片的转场效果
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        mSliderLayout.setDuration(6000);

        mSliderLayout.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {


                Log.d("e", "onPageScrolled");

            }

            @Override
            public void onPageSelected(int i) {

                Log.d("e", "onPageSelected");
            }

            @Override
            public void onPageScrollStateChanged(int i) {

                Log.d("e", "onPageScrollStateChanged");
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //停止循环
        mSliderLayout.stopAutoCycle();
    }
}
