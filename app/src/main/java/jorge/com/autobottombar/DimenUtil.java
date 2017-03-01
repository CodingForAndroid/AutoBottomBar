package jorge.com.autobottombar;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

/**
 * Created by zj on 2017/3/1.
 */

public class DimenUtil {
    public static int dipToPx(Context context, float dpValue) {
        if(context!=null){
            final float d = 0.5f;
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + d);
        }
        return (int) (dpValue * 2);

    }

    /**
     * 在非ImageView上展示图片
     *
     * @param t    一般用this即可。
     * @param url  网络Url
     * @param view View 对象
     * @param <T>
     */
    public static  <T> void showImageOnCustomView(final T t, String url, View view) {
        ViewTarget viewTarget = new ViewTarget<View, GlideDrawable>(view) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                //>=API 16 this.view.setBackground(resource.getCurrent());
//                view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,DimenUtil.dipToPx((Context) t,90)));
                this.view.setBackgroundDrawable(resource.getCurrent());
            }
        };

        if (t instanceof FragmentActivity) {
            Glide.with((FragmentActivity) t).load(url).into(viewTarget);
        } else if (t instanceof Activity) {
            Glide.with((Activity) t).load(url).into(viewTarget);
        } else if (t instanceof Fragment) {
            Glide.with((Fragment) t).load(url).into(viewTarget);
        } else if (t instanceof Context) {
            Glide.with((Context) t).load(url).into(viewTarget);
        } else if (t instanceof android.app.Fragment) {
            Glide.with((android.app.Fragment) t).load(url).into(viewTarget);
        }

    }
}
