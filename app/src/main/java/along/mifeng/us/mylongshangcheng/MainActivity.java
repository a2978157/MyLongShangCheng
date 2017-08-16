package along.mifeng.us.mylongshangcheng;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import Utils.SPUtils;

public class MainActivity extends Activity {
    int[] ii = {R.mipmap.welcome, R.mipmap.lunbo1, R.mipmap.lunbo2, R.mipmap.lunbo3};
    View[] views = new View[ii.length];
    private ViewPager vp;
    private TextView tv;
    int nxl = 0;
    boolean isEe = true;
    boolean isLo = false;
    private static final int SCALE = 2;//照片缩小比例
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if (isLo)
                    return;
                if (!isEe)
                    return;
                nxl++;
                if (nxl == 3) {
                    tv.setVisibility(View.VISIBLE);
                    isEe = false;
                }
                vp.setCurrentItem(nxl);
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//手机旋转不触发横屏
        new SPUtils().setShared("No","111",this);
        vp = (ViewPager) findViewById(R.id.vp);
        tv = (TextView) findViewById(R.id.tv);
        initImg();
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                nxl = position;
                if (nxl == 3) {
                    tv.setVisibility(View.VISIBLE);
                    isEe = false;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    isLo = false;
                } else {
                    isLo = true;
                }
            }
        });
        vp.setAdapter(new VpAdapter());
        startTime();
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MyZhuYe.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initImg() {
        for (int i = 0; i < ii.length; i++) {
            ImageView img = new ImageView(this);
            Resources res = getResources();
            Bitmap bit = BitmapFactory.decodeResource(res, ii[i]);
            /*//为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
            Bitmap smallBitmap = ImageTools.zoomBitmap(bit, bit.getWidth() / SCALE, bit.getHeight() / SCALE);
            //释放原始图片占用的内存，防止out of memory异常发生
            bit.recycle();*/
            /*ByteArrayOutputStream baos =new ByteArrayOutputStream();
            bit.compress(Bitmap.CompressFormat.PNG,100,baos);
            byte[] bytes = baos.toByteArray();
            Glide.with(MainActivity.this).load(bytes).into(img);*/
            img.setImageBitmap(bit);
            img.setScaleType(ImageView.ScaleType.FIT_XY);
            views[i] = img;
        }

    }

    private class VpAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return views.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            int p = position % views.length;
            View imageView = views[p];
            ViewParent parent = imageView.getParent();
            if (parent != null) {
                ViewGroup vp = (ViewGroup) parent;
                vp.removeView(imageView);
            }
            container.addView(views[p]);
            return views[p];
        }
    }

    private class TimeThread extends Thread {
        @Override
        public void run() {
            while (isEe) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        }
    }

    private void startTime() {
        new TimeThread().start();
    }
}
