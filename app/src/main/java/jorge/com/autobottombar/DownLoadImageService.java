package jorge.com.autobottombar;

/**
 * @author zj on 2017-2-27.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片下载
 */
public class DownLoadImageService implements Runnable {
    private String url;
    private Context context;
    private ImageDownLoadCallBack callBack;
    private File currentFile;

    public DownLoadImageService(Context context, String url, ImageDownLoadCallBack callBack) {
        this.url = url;
        this.callBack = callBack;
        this.context = context;
    }

    @Override
    public void run() {
        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
            if (bitmap != null) {
                // 在这里执行图片保存方法
                saveImageToGallery(context, bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bitmap != null && currentFile.exists()) {
                callBack.onDownLoadSuccess(bitmap);
                callBack.onDownLoadSuccess(url,currentFile.getAbsolutePath());
            } else {
                callBack.onDownLoadFailed();
            }
        }
    }

    public synchronized void saveImageToGallery(Context context, Bitmap bmp) {

        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile();//注意小米手机必须这样获得public绝对路径
        String fileName = "womai";
        File appDir = new File(file, fileName);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        int i = url.lastIndexOf("/");
        String[] split = url.substring(i+1, url.length() - 1).split("\\.");
        Log.e("onDownLoadSuccess"," url = "+url +"   fileName ="+fileName +"  split[0]="+split[0]) ;
//        fileName = System.currentTimeMillis() + ".jpg";
        fileName = split[0]+ ".jpg";
        ImageUtils.saveImage(bmp,"com.womai/"+fileName);
        currentFile = new File(appDir, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(currentFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(new File(currentFile.getPath()))));
    }
}

interface ImageDownLoadCallBack {
    void onDownLoadSuccess(Bitmap bitmap);
    void onDownLoadSuccess(String url, String fileName);
    void onDownLoadFailed();
}
