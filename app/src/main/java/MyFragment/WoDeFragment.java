package MyFragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import along.mifeng.us.mylongshangcheng.DengLu;
import along.mifeng.us.mylongshangcheng.MainActivity;
import along.mifeng.us.mylongshangcheng.MipcaActivityCapture;
import along.mifeng.us.mylongshangcheng.MyDps;
import along.mifeng.us.mylongshangcheng.QianBao;
import along.mifeng.us.mylongshangcheng.R;
import along.mifeng.us.mylongshangcheng.RoundBitmapUtils;
import along.mifeng.us.mylongshangcheng.ShouCang;
import along.mifeng.us.mylongshangcheng.WoDeDingDan;

/**
 * Created by 21903 on 2017/4/21.
 */

public class WoDeFragment extends Fragment implements View.OnClickListener {

    private ImageView zhaopian;
    private PopupWindow pop;
    public final static int ALBUM_REQUEST_CODE = 1;
    public final static int CROP_REQUEST = 2;
    public final static int CAMERA_REQUEST_CODE = 3;
    private static final int CROP_SMALL_PICTURE = 2;
    public static String SAVED_IMAGE_DIR_PATH =
            Environment.getExternalStorageDirectory().getPath()
                    + "/AppName/camera/";// 拍照路径
    static String cameraPath;//指定存储位置图片的存储路径
    private String xiangcepath;//图片在相册中的存储路径
    private static File outFile;//拍照的图片生成的文件
    private static final int SCALE = 2;//照片缩小比例
    private File f;
    protected static Uri tempUri;
    private Uri uri;
    private Dialog dialog;
    private WindowManager.LayoutParams wl;
    private TextView denglu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = View.inflate(getActivity(), R.layout.wodefragment, null);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("us.jianbao");
        Receiver receiver = new Receiver();
        getActivity().registerReceiver(receiver, intentFilter);
        initView(v);
        return v;
    }

    private void initView(View v) {
        zhaopian = (ImageView) v.findViewById(R.id.zhaopian);
        Resources res = getResources();
        Bitmap bit = BitmapFactory.decodeResource(res, R.mipmap.a2);
        zhaopian.setImageBitmap(RoundBitmapUtils.getBitmap(bit));
        denglu = (TextView) v.findViewById(R.id.denglu);
        LinearLayout qianbao = (LinearLayout) v.findViewById(R.id.qianbao);
        LinearLayout dingdan = (LinearLayout) v.findViewById(R.id.dingdan);
        LinearLayout shoucang = (LinearLayout) v.findViewById(R.id.shoucang);
        LinearLayout huanying = (LinearLayout) v.findViewById(R.id.huanying);
        LinearLayout shao = (LinearLayout) v.findViewById(R.id.shao);
        LinearLayout dps = (LinearLayout) v.findViewById(R.id.dps);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        // 引入窗口配置文件
        View vv = inflater.inflate(R.layout.touxiang, null);
        Button xiangche = (Button) vv.findViewById(R.id.xiangche);
        Button paizhao = (Button) vv.findViewById(R.id.paizhao);
        dialog = new Dialog(getActivity(), R.style.transparentFrameWindowStyle);
        dialog.setContentView(vv, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        wl = window.getAttributes();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        zhaopian.setOnClickListener(this);
        denglu.setOnClickListener(this);
        qianbao.setOnClickListener(this);
        dingdan.setOnClickListener(this);
        shoucang.setOnClickListener(this);
        xiangche.setOnClickListener(this);
        paizhao.setOnClickListener(this);
        dps.setOnClickListener(this);
        shao.setOnClickListener(this);
        huanying.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.huanying:
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
                break;
            case R.id.shao:
                Intent intent = new Intent();
                intent.setClass(getActivity(), MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, 111);
                break;
            case R.id.zhaopian:
                // 设置显示位置
                dialog.onWindowAttributesChanged(wl);
                // 设置点击外围解散
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                break;
            case R.id.xiangche:
                StartXC();
                break;
            case R.id.paizhao:
                startCamera();
                break;
            case R.id.qianbao:
                startActivity(new Intent(getActivity(), QianBao.class));
                break;
            case R.id.dingdan:
                startActivity(new Intent(getActivity(), WoDeDingDan.class));
                break;
            case R.id.shoucang:
                startActivity(new Intent(getActivity(), ShouCang.class));
                break;
            case R.id.denglu:
                startActivity(new Intent(getActivity(), DengLu.class));
                break;
            case R.id.dps:
                startActivity(new Intent(getActivity(), MyDps.class));
                break;
        }
    }

    public void startCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            cameraPath = SAVED_IMAGE_DIR_PATH + System.currentTimeMillis() + ".png";
            Intent intent = new Intent();
            // 指定开启系统相机的Action
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            String out_file_path = SAVED_IMAGE_DIR_PATH;
            File dir = new File(out_file_path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            f = new File(cameraPath);
            // 把文件地址转换成Uri格式
            tempUri = Uri.fromFile(new File(cameraPath));
            // 设置系统相机拍摄照片完成后图片文件的存放地址
            intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "请确认已经插入SD卡",
                    Toast.LENGTH_LONG).show();
        }
    }

    /*
     * 调用系统相册的方法
     * */
    public void StartXC() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, ALBUM_REQUEST_CODE);

    }

    /*
     * 获取相册中图片绝对路径的方法
     * */
    public String getAbsolutePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /*
     * 从相机和系统中获取图片的方法
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == 111) {
            if (data!=null){
                Bundle bundle = data.getExtras();
                //用默认浏览器打开扫描得到的地址
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(bundle.getString("result").toString());
                intent.setData(content_url);
                startActivity(intent);
            }

        }
       /* if (resultCode == Activity.RESULT_OK) {
            //调用相册中图片
            if (requestCode ==ALBUM_REQUEST_CODE) {
                try {
                    Uri uri = data.getData();
                    xiangcepath= getAbsolutePath(getActivity(), uri);
                    Bitmap bit= BitmapFactory.decodeFile(xiangcepath);
                    if (bit != null) {
                        //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                        Bitmap smallBitmap = ImageTools.zoomBitmap(bit, bit.getWidth() / SCALE, bit.getHeight() / SCALE);
                        //释放原始图片占用的内存，防止out of memory异常发生
                        bit.recycle();
                        zhaopian.setImageBitmap(RoundBitmapUtils.getBitmap(smallBitmap));
                        pop.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //手机拍照获取图片
            if (requestCode == CAMERA_REQUEST_CODE) {
                Bitmap bit=BitmapFactory.decodeFile(f.getAbsolutePath());
                Bitmap newBitmap = ImageTools.zoomBitmap(bit, bit.getWidth() / SCALE, bit.getHeight() / SCALE);
                bit.recycle();
                ImageTools.savePhotoToSDCard(newBitmap, Environment.getExternalStorageDirectory().getAbsolutePath(), String.valueOf(System.currentTimeMillis()));
                zhaopian.setImageBitmap(RoundBitmapUtils.getBitmap(newBitmap));
                pop.dismiss();
            }

        }*/
        if (resultCode == Activity.RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case ALBUM_REQUEST_CODE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CAMERA_REQUEST_CODE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                        dialog.dismiss();
                    }
                    break;
            }
        }
    }

    /* 裁剪图片方法实现
    *
    * @param uri
    */
    protected void startPhotoZoom(Uri uri) {
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param
     * @param
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
            Bitmap smallBitmap = ImageTools.zoomBitmap(photo, photo.getWidth() / SCALE, photo.getHeight() / SCALE);
            //释放原始图片占用的内存，防止out of memory异常发生
            photo.recycle();
            zhaopian.setImageBitmap(RoundBitmapUtils.getBitmap(smallBitmap));
            /*saveBitmap(photo);*/
        }
    }

    /*public void saveBitmap(Bitmap mBitmap) {
        File f = new File(Environment.getExternalStorageDirectory(),"face.jpg");
        try {
            f.createNewFile();
            FileOutputStream fOut = null;
            fOut = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    class Receiver extends BroadcastReceiver {
        String ss;

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO 自动生成的方法存根
            ss = intent.getStringExtra("ss");
            denglu.setText(ss);
        }

    }
}
