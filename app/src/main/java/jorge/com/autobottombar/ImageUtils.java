package jorge.com.autobottombar;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 图片工具类
 * 
 * @author xiangming
 * 
 */
public class ImageUtils {

	// private static final String JPG = ".JPG";
	// private static final String PNG = ".PNG";

	/**
	 * 图片压缩质量
	 */
	private static final int COMPRESS_QUALITY = 100;

	/**
	 * 系统相册路径
	 */
	public static final String SYSTEM_IMG_PATH = "/DCIM/Camera";

	/**
	 * 获取图片大小(bit)
	 * 
	 * @param bitmap
	 * @return
	 */
	public static int imageSize(Bitmap bitmap) {
		int size = 0;
		if (bitmap != null) {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.PNG, COMPRESS_QUALITY, baos);
				size = baos.size();
				baos.flush();
				baos.close();
			} catch (Exception e) {
				LogUtils.e(e);
			}
		}
		return size;
	}

	/**
	 * 从文件里加载图片
	 * 
	 * @param filename
	 * @return
	 */
	public static Bitmap loadImage(String filename) {
		Bitmap bitmap = null;
		if (filename != null && filename.length() > 0 && SdcardUtils.isMount()) {
			if (FileUtils.exist(filename)) {
				bitmap = BitmapFactory.decodeFile(SdcardUtils.root() + "/" + filename);
			}
		}
		return bitmap;
	}

	public static Bitmap loadImage(ContentResolver cr, Uri uri, int width, int height) {
		InputStream input = null;
		BitmapFactory.Options options = null;
		try {
			input = cr.openInputStream(uri);
			options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(input, null, options);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
					input = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Bitmap bitmap = null;
		if (options != null) {
			try {
				input = cr.openInputStream(uri);
				options.inSampleSize = calculateInSampleSize(options, width, height);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(input, null, options);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (input != null) {
					try {
						input.close();
						input = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return bitmap;
	}

	/**
	 * 获取指定宽和高的压缩比的位图
	 * 
	 * @param filename
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap loadImage(String filename, int width, int height) {
		try {
			if (filename != null && filename.length() > 0 && SdcardUtils.isMount()) {
				if (new File(filename).exists()) {
					// First decode with inJustDecodeBounds=true to check
					// dimensions
					final BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeFile(filename, options);
					// Calculate inSampleSize
					options.inSampleSize = calculateInSampleSize(options, width, height);
					// Decode bitmap with inSampleSize set
					options.inJustDecodeBounds = false;
					return BitmapFactory.decodeFile(filename, options);
				} else {
					return null;
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取压缩比
	 * 
	 * @param options
	 * @param width
	 * @param height
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int width, int height) {
		try {
			int outHeight = options.outHeight;
			int outWidth = options.outWidth;
			int inSampleSize = 1;
			if (outHeight > height || outWidth > width) {
				if (outWidth > outHeight) {
					inSampleSize = Math.round((float) outHeight / (float) height);
				} else {
					inSampleSize = Math.round((float) outWidth / (float) width);
				}
			}
			return inSampleSize;
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
	}

	public static Bitmap scaleImage(Bitmap bitmap, int width, int height) {
		try {
			int oldWidth = bitmap.getWidth();
			int oldHeight = bitmap.getHeight();
			float scaleWidth = ((float) width) / oldWidth;
			float scaleHeight = ((float) height) / oldHeight;
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);

			return Bitmap.createBitmap(bitmap, 0, 0, oldWidth, oldHeight, matrix, true);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 保存图片 PNG格式的
	 * 
	 * @param bitmap
	 * @param filename
	 * @return
	 */
	public static boolean saveImage(Bitmap bitmap, String filename) {
		if (bitmap != null && filename != null && filename.length() > 0 && SdcardUtils.isMount()) {
			try {
				File file = FileUtils.create(filename);
				if (file != null) {
					OutputStream outStream = new FileOutputStream(file);
					bitmap.compress(Bitmap.CompressFormat.PNG, 0, outStream);
					outStream.flush();
					outStream.close();
					return true;
				}
			} catch (Exception e) {
				LogUtils.e(e);
			}
		}
		return false;
	}

	/**
	 * 保存图片JPEG格式
	 *
	 * @param bitmap
	 * @param filename
	 * @return
	 */
	public static boolean saveImageJPEG(Bitmap bitmap, String filename) {
		if (bitmap != null && filename != null && filename.length() > 0 && SdcardUtils.isMount()) {
			try {
				File file = FileUtils.create(filename);
				if (file != null) {
					OutputStream outStream = new FileOutputStream(file);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
					outStream.flush();
					outStream.close();
					return true;
				}
			} catch (Exception e) {
				LogUtils.e(e);
			}
		}
		return false;
	}

	/**
	 * 保存图片到系统相册
	 *
	 * @param bitmap
	 * @param filename
	 * @return
	 */
	public static boolean saveImageToSystem(Context context, Bitmap bitmap, String filename) {
		if (filename != null && filename.length() > 0 && bitmap != null) {
			try {
				ContentResolver cr = context.getContentResolver();
				String filePath = MediaStore.Images.Media.insertImage(cr, bitmap, filename, "");
				if (null != filePath) {
					try {
						context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
								Uri.parse("file://" + Environment.getExternalStorageDirectory() + filePath)));
					} catch (Exception e) {
						LogUtils.e(e);
					}
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				LogUtils.e(e);
			}
		}
		return false;
	}

	/**
	 * 读取网络图片
	 *
	 * @param url
	 * @return
	 */
	public static Bitmap downloadImage(String url) {
		Bitmap bitmap = null;
		if (url != null && url.length() > 0) {
			try {
				URL aURL = new URL(url);
				HttpURLConnection con = (HttpURLConnection) aURL.openConnection();
				con.setDoInput(true);
				con.connect();
				InputStream inputStream = con.getInputStream();
				if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
					bitmap = BitmapFactory.decodeStream(inputStream);
				}
				inputStream.close();
			} catch (Exception e) {
				LogUtils.e(e);
			}
		}
		return bitmap;
	}

	/**
	 * 读取网络图片
	 *
	 * @param url
	 *            地址
	 * @param timeout
	 *            超时（秒）
	 * @return
	 */
	public static Bitmap downloadImage(String url, int timeout) {
		Bitmap bitmap = null;
		if (url != null && url.length() > 0) {
			try {
				URL aURL = new URL(url);
				HttpURLConnection con = (HttpURLConnection) aURL.openConnection();
				con.setConnectTimeout(timeout * 1000);
				con.setRequestMethod("GET");
				con.setDoInput(true);
				con.connect();
				InputStream inputStream = con.getInputStream();
				if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
					bitmap = BitmapFactory.decodeStream(inputStream);
				}
				inputStream.close();
			} catch (Exception e) {
				LogUtils.e(e);
			}
		}
		return bitmap;
	}

	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			LogUtils.e(e);
		}
		return result;
	}

	/**
	 * 把Drawble转化成Bitmap
	 *
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 图片圆角矩形
	 */
	public static Bitmap getRoundBitmap(Bitmap bitmap, int roundPx) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 图片剪成圆型
	 */
	public static Bitmap getCircleBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			left = 0;
			top = 0;
			right = width;
			bottom = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xffffffff;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
		// final RectF rectF = new RectF(dst);
		paint.setAntiAlias(true);// 设置画笔无锯齿
		canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas
		paint.setColor(color);
		canvas.drawCircle(roundPx, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
		canvas.drawBitmap(bitmap, src, dst, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

		return output;
	}

	private static Drawable createDrawable(Drawable d, Paint p) {

		BitmapDrawable bd = (BitmapDrawable) d;
		Bitmap b = bd.getBitmap();
		Bitmap bitmap = Bitmap.createBitmap(bd.getIntrinsicWidth(), bd.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(b, 0, 0, p); // 关键代码，使用新的Paint画原图，

		return new BitmapDrawable(bitmap);
	}

	/** 设置Selector。 本次只增加点击变暗的效果，注释的代码为更多的效果 */
	public static StateListDrawable createSLD(Drawable drawable) {
		StateListDrawable bg = new StateListDrawable();
		Paint p = new Paint();
		p.setColor(0x40222222); // Paint ARGB色值，A = 0x40 不透明。RGB222222 暗色

		Drawable normal = drawable;
		Drawable pressed = createDrawable(drawable, p);
		// p = new Paint();
		// p.setColor(0x8000FF00);
		// Drawable focused = createDrawable(drawable, p);
		// p = new Paint();
		// p.setColor(0x800000FF);
		// Drawable unable = createDrawable(drawable, p);
		// View.PRESSED_ENABLED_STATE_SET
		bg.addState(new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled }, pressed);
		// View.ENABLED_FOCUSED_STATE_SET
		// bg.addState(new int[] { android.R.attr.state_enabled,
		// android.R.attr.state_focused }, focused);
		// View.ENABLED_STATE_SET
		bg.addState(new int[] { android.R.attr.state_enabled }, normal);
		// View.FOCUSED_STATE_SET
		// bg.addState(new int[] { android.R.attr.state_focused }, focused);
		// // View.WINDOW_FOCUSED_STATE_SET
		// bg.addState(new int[] { android.R.attr.state_window_focused },
		// unable);
		// View.EMPTY_STATE_SET
		bg.addState(new int[] {}, normal);
		return bg;
	}

	/**
	 * 获取带点击效果的图片对象
	 */
	public static StateListDrawable getStateListDrawable(Drawable drawable, Context context) {
		if (drawable != null) {
			return createSLD(drawable);
		} else {
			return null;
		}
	}

	/**
	 * 根据ResourceID获取Drawable
	 * 
	 * @param resId
	 * @param context
	 * @return
	 */
	public static Drawable getDrawable(int resId, int width, int height, Context context) {
		Drawable drawable = context.getResources().getDrawable(resId);
		width = width == 0 ? drawable.getIntrinsicWidth() : width;
		height = height == 0 ? drawable.getIntrinsicHeight() : height;
		drawable.setBounds(0, 0, width, height);
		return drawable;
	}

	/**
	 * 默认的宽高
	 * 
	 * @param resId
	 * @param context
	 * @return
	 */
	public static Drawable getDrawable(int resId, Context context) {
		return getDrawable(resId, 0, 0, context);
	}

	/**
	 * 计算Bitmap占用的内存空间
	 * 
	 * @param bitmap
	 * @return
	 */
	public static long getBitmapsize(Bitmap bitmap) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			return bitmap.getByteCount();
		}
		// Pre HC-MR1
		return bitmap.getRowBytes() * bitmap.getHeight();

	}
}
