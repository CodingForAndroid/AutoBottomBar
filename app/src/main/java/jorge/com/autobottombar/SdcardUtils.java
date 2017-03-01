package jorge.com.autobottombar;

import android.os.Environment;

/**
 * @author zj on 2017-3-1.
 */

public class SdcardUtils {

    /**
     * 是否装载sdcard
     *
     * @return
     */
    public static boolean isMount() {
        final String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED) && !state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            return true;
        }
        return false;
    }



    /**
     * 获取sdcard根目录
     *
     * @return
     */
    public static String root() {
        int p = 4;
        String root = Environment.getExternalStorageDirectory().toString();
        if (root.substring(0, p).equals("/mnt")) {
            root = root.substring(4, root.length());
        }
        return root;
    }
}
