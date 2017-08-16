package along.mifeng.us.mylongshangcheng;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapCompress {
	public Bitmap imgCompress(Context context){
		BitmapFactory.Options options= new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(context.getResources(), R.mipmap.dingwei, options);
		int outWidth = options.outWidth;
		int outHeight = options.outHeight;
		int num = outHeight>outWidth?outWidth/100:outHeight/100;
		options.inSampleSize = num;
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.dingwei, options);
		return bitmap;
	}
}
