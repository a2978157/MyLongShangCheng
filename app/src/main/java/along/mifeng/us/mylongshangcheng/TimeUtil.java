package along.mifeng.us.mylongshangcheng;

/**
 * Created by 21903 on 2017/5/8.
 */

public class TimeUtil {
    //获取当前时间戳
    public static String getTime() {
        //去掉1000返回的是13位的时间戳
        long time = (System.currentTimeMillis()) / 1000;
        return Long.toString(time);
    }

}
