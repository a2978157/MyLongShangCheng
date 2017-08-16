package along.mifeng.us.mylongshangcheng;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import java.util.Timer;
import java.util.TimerTask;

import MyFragment.FenLeiFragment;
import MyFragment.GouWuFragment;
import MyFragment.SildingFragment;
import MyFragment.WoDeFragment;
import MyFragment.ZhuYeFragment;

/**
 * Created by 21903 on 2017/4/21.
 */

public class MyZhuYe extends SlidingFragmentActivity implements View.OnClickListener {

    private FrameLayout fl;
    private LinearLayout zhuye;
    private LinearLayout gouwuche;
    private LinearLayout fenlei;
    private LinearLayout wode;
    private ImageView zhuyeimg;
    private ImageView fenleiimg;
    private ImageView gouwucheimg;
    private ImageView wodeimg;
    private TextView zhuyetxt;
    private TextView fenleitxt;
    private TextView gouwuchetxt;
    private TextView wodetxt;
    private FragmentManager fm;
    private ZhuYeFragment zhuYeFragment;
    private FenLeiFragment fenLeiFragment;
    private GouWuFragment gouWuFragment;
    private WoDeFragment woDeFragment;
    private static Boolean isExit = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//手机旋转不触发横屏 android:screenOrientation="portrait"清单文件设置竖屏
        //给抽屉设置布局
        setBehindContentView(R.layout.silding);
        FragmentTransaction ft1=getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.fll, new SildingFragment());
        ft1.commit();
        //用SlidingMenu把fragment隐藏
        final SlidingMenu sm = getSlidingMenu();
        //SlidingMenu划出时主页面显示的剩余宽度
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        //设置阴影的宽度
        sm.setShadowWidthRes(R.dimen.shadow_width);
        //sm.setBehindScrollScale(0.25f);
        //SlidingMenu滑动时的渐变程度
        sm.setFadeDegree(0.35f);
        //侧滑剩余部分添加阴影淡入淡出
        sm.setOffsetFadeDegree(0.4f);
        //滑动的模式TOUCHMODE_FULLSCREEN全屏可滑动
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        sm.setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);


        setContentView(R.layout.myzhuye);
        initView();
        fm = getSupportFragmentManager();
        zhuYeFragment = new ZhuYeFragment();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fl,zhuYeFragment);
        ft.commit();
    }

    private void initYan() {
        zhuyeimg.setImageResource(R.mipmap.shouye);
        fenleiimg.setImageResource(R.mipmap.fenlei);
        gouwucheimg.setImageResource(R.mipmap.gouwuche);
        wodeimg.setImageResource(R.mipmap.wode);
        zhuyetxt.setTextColor(Color.BLACK);
        fenleitxt.setTextColor(Color.BLACK);
        gouwuchetxt.setTextColor(Color.BLACK);
        wodetxt.setTextColor(Color.BLACK);
    }

    private void initView() {
        fl = (FrameLayout) findViewById(R.id.fl);
        zhuye = (LinearLayout) findViewById(R.id.zhuye);
        fenlei = (LinearLayout) findViewById(R.id.fenlei);
        gouwuche = (LinearLayout) findViewById(R.id.gouwuche);
        wode = (LinearLayout) findViewById(R.id.wode);
        zhuyeimg = (ImageView) findViewById(R.id.zhuyeimg);
        fenleiimg = (ImageView) findViewById(R.id.fenleiimg);
        gouwucheimg = (ImageView) findViewById(R.id.gouwucheimg);
        wodeimg = (ImageView) findViewById(R.id.wodeimg);
        zhuyetxt = (TextView) findViewById(R.id.zhuyetxt);
        fenleitxt = (TextView) findViewById(R.id.fenleitxt);
        gouwuchetxt = (TextView) findViewById(R.id.gouwuchetxt);
        wodetxt = (TextView) findViewById(R.id.wodetxt);
        zhuye.setOnClickListener(this);
        fenlei.setOnClickListener(this);
        gouwuche.setOnClickListener(this);
        wode.setOnClickListener(this);
    }
    private void hindAll(){
        FragmentTransaction ft=fm.beginTransaction();
        if(zhuYeFragment!=null){
            ft.hide(zhuYeFragment);
        }
        if(fenLeiFragment!=null){
            ft.hide(fenLeiFragment);
        }
        if(gouWuFragment!=null){
            ft.hide(gouWuFragment);
        }
        if(woDeFragment!=null){
            ft.hide(woDeFragment);
        }
        ft.commit();
    }

    @Override
    public void onClick(View view) {
        initYan();
        hindAll();
        FragmentTransaction ft = fm.beginTransaction();
        switch (view.getId()){
            case R.id.zhuye:
                if (zhuYeFragment==null){
                    /*zhuYeFragment = new ZhuYeFragment();*/
                    ft.add(R.id.fl,zhuYeFragment);
                }else {
                    ft.show(zhuYeFragment);
                }
                zhuyetxt.setTextColor(Color.RED);
                zhuyeimg.setImageResource(R.mipmap.shouye1);
                ft.commit();
                break;
            case R.id.fenlei:
                if (fenLeiFragment==null){
                    fenLeiFragment = new FenLeiFragment();
                    ft.add(R.id.fl,fenLeiFragment);
                }else {
                    ft.show(fenLeiFragment);
                }
                fenleitxt.setTextColor(Color.RED);
                fenleiimg.setImageResource(R.mipmap.fenlei1);
                ft.commit();
                break;
            case R.id.gouwuche:
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("us.mifeng");
                registerReceiver(new MyReceiver(),intentFilter);
                if (gouWuFragment==null){
                    gouWuFragment = new GouWuFragment();
                    ft.add(R.id.fl,gouWuFragment);
                }else {
                    ft.show(gouWuFragment);
                }
                gouwuchetxt.setTextColor(Color.RED);
                gouwucheimg.setImageResource(R.mipmap.gouwuche1);
                ft.commit();
                break;
            case R.id.wode:
                if (woDeFragment==null){
                    woDeFragment = new WoDeFragment();
                    ft.add(R.id.fl,woDeFragment);
                }else {
                    ft.show(woDeFragment);
                }
                wodetxt.setTextColor(Color.RED);
                wodeimg.setImageResource(R.mipmap.wode1);
                ft.commit();
                break;
        }

    }
    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click(); // 调用双击退出函数
        }
        return false;
    }

    /**
     * 双击退出函数
     */
    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 3000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }
    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            unregisterReceiver(this);
            initYan();
            hindAll();
            FragmentTransaction ft = fm.beginTransaction();
            if (zhuYeFragment==null){
                    /*zhuYeFragment = new ZhuYeFragment();*/
                ft.add(R.id.fl,zhuYeFragment);
            }else {
                ft.show(zhuYeFragment);
            }
            zhuyetxt.setTextColor(Color.RED);
            zhuyeimg.setImageResource(R.mipmap.shouye1);
            ft.commit();
        }
    }
}
