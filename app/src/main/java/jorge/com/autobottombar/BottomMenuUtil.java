package jorge.com.autobottombar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.RadioButton;
import java.io.File;
import java.util.List;

import jorge.com.autobottombar.bean.Bottom;
import jorge.com.autobottombar.bean.BottomMenuItem;
import jorge.com.autobottombar.bean.BottomTabInfo;

/**
 * @author zj on 2017-2-24.
 */

public class BottomMenuUtil {

public static long delayTime = 1000L;
private static final int MSG_VISIBLE = 1;
private static final int MSG_ERROR = 0;
private static final int MSG_PATH = -1;
    private static Drawable drawableDefault;
    private static Drawable drawableChecked;


    public static void setBackGround(Context context, RadioButton radioButton , String pathDefault , String pathChecked, String text){
        StateListDrawable drawable = new StateListDrawable();
//        try {
//            Bitmap  bitmap = Glide.with(context)
//                      .load(pathDefault)
//                      .asBitmap()
//                      .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                      .get();
//            drawableDefault = new BitmapDrawable(bitmap);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

        Bitmap bitmap = loadImage(pathDefault);
        drawableDefault = new BitmapDrawable(bitmap);
//        try {
//            Bitmap  bitmap = Glide.with(context)
//                    .load(pathChecked)
//                    .asBitmap()
//                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                    .get();
//            drawableChecked = new BitmapDrawable(bitmap);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        Bitmap bitmap = loadImage(pathDefault);
        drawableChecked = new BitmapDrawable(loadImage(pathChecked));

//        Bitmap bitmap = ImageUtils.loadImage(pathDefault);
        Log.e("setBackGround","bitmap = null?"+bitmap);

        //Non focused states
        drawable.addState(new int[]{android.R.attr.state_checked},
                drawableChecked);
        drawable.addState(new int[]{ - android.R.attr.state_checked},
                drawableDefault);
        //Focused states
//        drawable.addState(new int[]{android.R.attr.state_checked,android.R.attr.state_focused,android.R.attr.state_selected, android.R.attr.state_pressed},
//                drawableChecked);
//        drawable.addState(new int[]{android.R.attr.state_checked,android.R.attr.state_focused,android.R.attr.state_selected, android.R.attr.state_pressed},
//                drawableChecked);
////        //Pressed
//        drawable.addState(new int[]{android.R.attr.state_checked,android.R.attr.state_selected, android.R.attr.state_pressed},
//                drawableChecked);
//        drawable.addState(new int[]{android.R.attr.state_checked,android.R.attr.state_pressed},
//                drawableChecked);

//        TextView textView = (TextView) findViewById(R.id.TextView_title);
//           rb.setBackgroundDrawable(drawableDefault);
//        rb.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
//        rb.setBackgroundResource(drawable);
//          radioButton.setBackgroundDrawable(drawable);
//        width = width == 0 ? drawable.getIntrinsicWidth() : width;
//        height = height == 0 ? drawable.getIntrinsicHeight() : height;
//        drawable.setBounds(0, 0, width, height);

        if("圆孔".equals(text)){
            int width = radioButton.getWidth();
            int height = radioButton.getHeight();
            Log.e("BottomMenuUtil","width="+drawable.getIntrinsicWidth());
            Log.e("BottomMenuUtil","height="+drawable.getMinimumHeight());
//            Drawable drawale = context.getResources().getDrawable(R.drawable.ic_launcher);
//            drawable.setBounds(0,0,74,74*4);
//            drawable.setBounds(0,0,SysUtils.dipToPx(context,49),SysUtils.dipToPx(context,80));
//            drawable.setBounds(0,0,drawable.getIntrinsicWidth()*2,drawable.getMinimumHeight()*3);
//            radioButton.setCompoundDrawables(null,drawable,null,null);
            radioButton.setBackgroundDrawable(drawable);
//            radioButton.setBackgroundResource(R.drawable._01saomiaogou);
            radioButton.setText("");
          //  layout_gravity
//            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,SysUtils.dipToPx(context,48));
//            layoutParams.addRule(Gravity.BOTTOM);
//            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, SysUtils.dipToPx(context, 80));
////            params.gravity =Gravity.BOTTOM;
//            radioButton.setLayoutParams(params);
        }else {
            drawable.setBounds(0,0,DimenUtil.dipToPx(context,30),DimenUtil.dipToPx(context,30));
            radioButton.setCompoundDrawables(null,drawable,null,null);
            radioButton.setText(text);
        }


//        rb.setba
//        imageView.setImageBitmap(bitmap);
//        imageView.setImageBitmap(bitmap);
    }
    /**
     * 启动图片下载线程
     */
    public static void onDownLoad(String url, final Handler handler, Context context) {
        DownLoadImageService service = new DownLoadImageService(context,
                url,
                new ImageDownLoadCallBack() {

                    @Override
                    public void onDownLoadSuccess(Bitmap bitmap) {
                        // 在这里执行图片保存方法
                        Message message = new Message();
                        message.what = MSG_VISIBLE;
                        handler.sendMessageDelayed(message, delayTime);
                    }

                    @Override
                    public void onDownLoadSuccess(String url, String fileName) {
                        // 图片路径
                        Message message = new Message();
                        message.what = MSG_PATH;
                        message.obj = fileName;
                        Bundle data = new Bundle();
                        data.putString("url_key",url);
                        data.putString("file_path",fileName);
                        message.setData(data);
                        handler.sendMessageDelayed(message, delayTime);
                        int i = url.lastIndexOf("/");
                        String[] split = url.substring(i+1, url.length() - 1).split("\\.");
                        Log.e("onDownLoadSuccess"," url = "+url +"   fileName ="+fileName +"  split[0]="+split[0]) ;

                    }


                    @Override
                    public void onDownLoadFailed() {
                        // 图片保存失败
                        Message message = new Message();
                        message.what = MSG_ERROR;
                        handler.sendMessageDelayed(message, delayTime);
                    }
                });
        //启动图片下载线程
        new Thread(service).start();
    }




    public static BottomTabInfo createData(){
        BottomTabInfo bottomTabInfo = new BottomTabInfo();
        Bottom bottom = new Bottom();
        //背景图
        bottom.backgroundPic = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1488389806044&di=763cb4d53c2d6f6b8bec7e7b91a6da50&imgtype=0&src=http%3A%2F%2Fimage.tupian114.com%2F20090923%2F10570446.jpg" ;//;http://bpic.588ku.com/back_pic/00/04/13/75/281620f66c78c64275a91318911773f0.jpg";
        List<BottomMenuItem> bottomMenu = bottom.bottomMenu;

        BottomMenuItem bottomMenuItem1=


                new BottomMenuItem("微信",
                "http://bpic.588ku.com/element_pic/00/91/37/6456f169194d862.jpg",
                "http://bpic.588ku.com/element_pic/00/38/86/1256d5a3dda7758.jpg",
                "",
                "",
                "0",
                "",
                "");

        BottomMenuItem bottomMenuItem2 =

                new BottomMenuItem("QQ",
                        "http://bpic.588ku.com/element_pic/00/86/90/3056ec5a38e3705.jpg",
                        "http://bpic.588ku.com/element_pic/01/29/74/13573af0bb11835.jpg",
                        "",
                        "",
                        "0",
                        "",
                        "");


        BottomMenuItem bottomMenuItem3 =

                new BottomMenuItem("大圆",
                        "http://bpic.588ku.com/element_origin_pic/16/12/22/c3ae28f097f1c342e38c4e1cba0d2b76.png",
                        "http://bpic.588ku.com/element_origin_pic/16/08/18/c10c7c9b9e332e8f26cf996d8788e97b.png",
                        "",
                        "",
                        "1",
                        "",
                        "");


        BottomMenuItem bottomMenuItem4 =

                new BottomMenuItem("QQ空间",
                        "http://bpic.588ku.com/element_pic/00/86/90/3456ec5a68f1c1a.jpg",
                        "http://bpic.588ku.com/element_pic/00/37/27/5956d53a63909f1.jpg",
                        "",
                        "",
                        "0",
                        "",
                        "");

        BottomMenuItem bottomMenuItem5 =

                new BottomMenuItem("支付宝",
                        "http://bpic.588ku.com/element_pic/00/16/06/1957666421d6810.jpg",
                        "http://bpic.588ku.com/element_pic/01/31/99/41573b5e7d55c6a.jpg",
                        "",
                        "",
                        "0",
                        "",
                        "");

        bottom.bottomMenu.clear();
        bottom.bottomMenu.add(bottomMenuItem1);
        bottom.bottomMenu.add(bottomMenuItem2);
        bottom.bottomMenu.add(bottomMenuItem3);
        bottom.bottomMenu.add(bottomMenuItem4);
        bottom.bottomMenu.add(bottomMenuItem5);
        bottomTabInfo.bottom = bottom;

        return  bottomTabInfo ;
    }



    static  boolean isFolderExists(String strFolder)
    {
        File file = new File(strFolder);

        if (!file.exists())
        {
                return false;
        }
        return true;
    }

    /**
     * 加载bitmap
     * @param fileName
     * @return
     */
    private  static Bitmap loadImage(String fileName){
        Log.e("setBackGround","253"+ fileName  +  "SdcardUtils.isMount() ="+SdcardUtils.isMount() + "SdcardUtils.root() ="+SdcardUtils.root());
        Bitmap bitmap = null;
        if (fileName != null && fileName.length() > 0 && SdcardUtils.isMount()) {
            if (new File(fileName).exists()) {
                bitmap = BitmapFactory.decodeFile(fileName);
                Log.e("setBackGround","258");
            }
        }
        Log.e("setBackGround","261");
        return bitmap;
    }



}

