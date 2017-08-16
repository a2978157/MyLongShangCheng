package along.mifeng.us.mylongshangcheng;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;

public class RoundBitmapUtils {
	public static Bitmap getBitmap(Bitmap bitmap){
		Bitmap b = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_4444);
		Canvas canvas = new Canvas(b);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.BLUE);
		canvas.drawARGB(0, 0, 0, 0);
		float radius = Math.min(bitmap.getWidth(), bitmap.getHeight())/2;
		canvas.drawCircle(bitmap.getWidth()/2, bitmap.getHeight()/2, radius, paint);
		paint.reset();
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, 0,0, paint);
		return b;
	}
}