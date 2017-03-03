package jorge.com.autobottombar;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import jorge.com.autobottombar.bean.BottomMenuItem;
import jorge.com.autobottombar.bean.BottomTabInfo;

public class MainActivity extends Activity {

    //1 .  首先 要遍历
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    Toast.makeText(MainActivity.this,"fail",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(MainActivity.this,"ok",Toast.LENGTH_SHORT).show();
                    break;
                case -1:
                    Bundle msgData = msg.getData();
                    String url = (String) msgData.get("url_key");
                    String fileName = (String) msgData.get("file_path");
                    Log.e("Tab2Activity"," file_path =" +fileName +" url = "+ url);
                    boolean folderExists = BottomMenuUtil.isFolderExists(fileName);
                    Log.e("Tab2Activity","folderExists="+folderExists +" fileName =" +fileName);
                    break;
                case 4:
//                    Bitmap bitmap = loadImage(path);
//                    Message msg =Message.obtain();
//                    msg.what = 4;
                    Bitmap bitmap = (Bitmap) msg.obj;
                    imageBit.setImageBitmap(bitmap);
                    break;
            }
        }
    };
    private ImageView imageBit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imageBit = (ImageView) findViewById(R.id.imageBitmap);


        RadioButton rb01 = (RadioButton) findViewById(R.id.tab_1);
        RadioButton rb02 = (RadioButton) findViewById(R.id.tab_2);
        RadioButton rb03 = (RadioButton) findViewById(R.id.tab_3);
        RadioButton rb04 = (RadioButton) findViewById(R.id.tab_4);
        RadioButton rb05 = (RadioButton) findViewById(R.id.tab_5);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rg);

        BottomTabInfo bottomTabInfo = BottomMenuUtil.createData();
        //bottom.backgroundPic = "http://bpic.588ku.com/back_pic/00/04/13/75/281620f66c78c64275a91318911773f0.jpg";
        BottomMenuUtil.onDownLoad(bottomTabInfo.bottom.backgroundPic,handler,this);


        for (BottomMenuItem bottomMenuItem :bottomTabInfo.bottom.bottomMenu ){
            //先判断是否 不存在，如果不存在 下载
            String choosePic = bottomMenuItem.choosePic;
            String defaultPic = bottomMenuItem.defaultPic;
            BottomMenuUtil.onDownLoad(choosePic,handler,this);
            BottomMenuUtil.onDownLoad(defaultPic,handler,this);
            // 如果存在 则 加载 导航
        }

        BottomMenuUtil.setBackGround(this,rb01,"/storage/emulated/0/Pictures/womai/6456f169194d862.jpg","/storage/emulated/0/Pictures/womai/1256d5a3dda7758.jpg","微信");
        BottomMenuUtil.setBackGround(this,rb02,"/storage/emulated/0/Pictures/womai/3056ec5a38e3705.jpg","/storage/emulated/0/Pictures/womai/13573af0bb11835.jpg","QQ");
        BottomMenuUtil.setBackGround(this,rb04,"/storage/emulated/0/Pictures/womai/3456ec5a68f1c1a.jpg","/storage/emulated/0/Pictures/womai/5956d53a63909f1.jpg","空间");
        BottomMenuUtil.setBackGround(this,rb05,"/storage/emulated/0/Pictures/womai/1957666421d6810.jpg","/storage/emulated/0/Pictures/womai/41573b5e7d55c6a.jpg","支付宝");
//        BottomMenuUtil.setBackGround(this,rb03,"/storage/emulated/0/Pictures/womai/1957666421d6810.jpg","/storage/emulated/0/Pictures/womai/41573b5e7d55c6a.jpg","圆孔");
        BottomMenuUtil.setBackGround(this,rb03,"/storage/emulated/0/Pictures/womai/3456ec5a68f1c1a.jpg","/storage/emulated/0/Pictures/womai/5956d53a63909f1.jpg","圆孔");
//        BottomMenuUtil.setBackGround(this,rb03,"/storage/emulated/0/Pictures/womai/c3ae28f097f1c342e38c4e1cba0d2b76.jpg","/storage/emulated/0/Pictures/womai/c3ae28f097f1c342e38c4e1cba0d2b76.jpg","圆孔");
        DimenUtil.showImageOnCustomView(this,"/storage/emulated/0/Pictures/womai/sy_80219211654.jpg",radioGroup);
        ///storage/emulated/0/Pictures/womai/281620f66c78c64275a91318911773f0.jpg
    }
}
