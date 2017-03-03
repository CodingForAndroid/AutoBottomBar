package jorge.com.autobottombar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import jorge.com.autobottombar.bean.BottomMenuItem;
import jorge.com.autobottombar.bean.BottomTabInfo;

/**
 * 启动页
 */
public class StartActivity extends AppCompatActivity {
    private  SharedPreferences sp ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        sp = getSharedPreferences("bottom_bar", Context.MODE_PRIVATE);
//        SharedPreferences.Editor edit = sp.edit();
//        edit.putString("","").commit();
        // 首先判断本地是否有下载好的导航图标，SharedPreferences 存储下载好的图标

        BottomTabInfo bottomTabInfo = BottomMenuUtil.createData();
        String backgroundPic = bottomTabInfo.bottom.backgroundPic;
        // 背景是否下载成功
        boolean has_download_backgroundPic = sp.getBoolean("backgroundPic", false);
        if(has_download_backgroundPic&&hasDownloadIcon(bottomTabInfo)){
             // 可以再MainActivity 中展示下载的图标。
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("show_old_bar",false);
            startActivity(intent);
        }else {
               //没有下载完成，MainActivity需要展示默认的图标，并且要下载 新的图标
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("show_old_bar",true);
            startActivity(intent);
            // 后台下载图标
        }

    }

    /**
     * 判断导航图标是否下载成功
     * @param bottomTabInfo
     * @return
     */
    private boolean hasDownloadIcon( BottomTabInfo bottomTabInfo){
        for (BottomMenuItem bottomMenuItem:bottomTabInfo.bottom.bottomMenu ){
            boolean has_download_defaultPic = sp.getBoolean(bottomMenuItem.defaultPic, false);
            boolean has_download_choosePic = sp.getBoolean(bottomMenuItem.choosePic, false);
            if(has_download_defaultPic&&has_download_choosePic){
                // 说明下载成功
            }else {
                // 说明未下载成功
                return false;
            }

        }
        // 说明所有图标下载成功
        return  true;
    }
}
