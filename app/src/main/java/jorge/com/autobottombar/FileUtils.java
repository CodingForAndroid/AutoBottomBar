package jorge.com.autobottombar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 文件工具类
 * 
 * @author xiangming
 * 
 */
public class FileUtils {

	/**
	 * 文件是否存在
	 * 
	 * @param filename
	 * @return
	 */
	public static boolean exist(String filename) {
		return new File(SdcardUtils.root() + "/" + filename).exists();
	}

	/**
	 * 创建目录
	 * 
	 * @param directory
	 * @return
	 */
	public static boolean createDirectory(String directory) {
		return new File(SdcardUtils.root() + "/" + directory).mkdirs();
	}

	/**
	 * 检查目录是否存在
	 * 
	 * @param directory
	 * @return
	 */
	public static boolean checkDirectory(String directory) {
		if (!exist(directory)) {
			return createDirectory(directory);
		}
		return true;
	}

	/**
	 * 创建文件
	 * 
	 * @param filename
	 * @return
	 */
	public static File create(String filename) {
		try {
			if (checkDirectory(directory(filename))) {
				File file = new File(SdcardUtils.root() + "/" + filename);
				if (!file.exists()) {
					file.createNewFile();
				}
				return file;
			}
		} catch (Exception e) {
			LogUtils.e(e);
		}
		return null;
	}

	/**
	 * 删除文件
	 * 
	 * @param file
	 */
	public static void delete(File file) {
		if (file.exists()) {
			if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (File subfile : files) {
					delete(subfile);
				}
			}
			file.delete();
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filename
	 */
	public static void delete(String filename) {
		delete(new File(SdcardUtils.root() + "/" + filename));
	}
	
	/**
	 * 删除目录下所有文件
	 * @param path
	 */
	public static void deleteAllImage(Activity context, String path) {
		File file = new File(SdcardUtils.root() + "/" + path);
		if (file.exists()) {
			if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (File subfile : files) {
					delete(subfile);
					context.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Media.DISPLAY_NAME + "='" + subfile.getName()+"'", null);
				}
			}
		}
	}

	/**
	 * 获取文件的md5值
	 * @param filename
	 * @return
	 */
		public static String getFileMD5(String filename) {
			File file = new File(SdcardUtils.root() + "/" + filename);
			if (file != null && file.exists()){
			MessageDigest digest = null;
			FileInputStream in = null;
			byte buffer[] = new byte[1024];
			int len;
			try {
				digest = MessageDigest.getInstance("MD5");
				in = new FileInputStream(file);
				while ((len = in.read(buffer, 0, 1024)) != -1) {
					digest.update(buffer, 0, len);
					}
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
					return "";
				}
				BigInteger bigint = new BigInteger(1, digest.digest());
				return bigint.toString(16);
			}else{
				return "";
			}
		}

	/**
	 * 修改文件的最后修改时间
	 * 
	 * @param filename
	 *            文件名
	 * @param modifyTime
	 *            修改间隔(毫秒)
	 */
	public static void updateModifyTime(String filename, long modifyTime) {
		File file = new File(SdcardUtils.root() + "/" + filename);
		file.setLastModified(modifyTime);
	}

	/**
	 * 删除指定目录下的过期文件
	 * 
	 * @param directory
	 *            目录
	 * @param timeDiff
	 *            时间间隔(毫秒)
	 */
	public static void clearExpiredFile(String directory, long timeDiff) {
		File dir = new File(SdcardUtils.root() + "/" + directory);
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			if (files != null) {
				int forLen = files.length;
				for (int i = 0; i < forLen; i++) {
					if (files[i].isFile()) {
						if (System.currentTimeMillis() - files[i].lastModified() > timeDiff) {
							files[i].delete();
						}
					}
				}
			}
		}
	}

	/**
	 * 获取文件大小(bit)
	 * 
	 * @param filename
	 * @return
	 */
	public static int fileSize(String filename) {
		try {
			if (exist(filename)) {
				FileInputStream stream = new FileInputStream(SdcardUtils.root() + "/" + filename);
				int result = stream.available();
				stream.close();
				return result;
			}
		} catch (Exception e) {
			LogUtils.e(e);
		}
		return 0;
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param filename
	 * @return
	 */
	public static String fileExtName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot);
			}
		}
		return "";
	}

	/**
	 * 获取文件名的所在路径
	 * 
	 * @param filename
	 * @return
	 */
	public static String directory(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('/');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(0, dot);
			}
		}
		return "";
	}

	/**
	 * 文件按最后修改的时间排序后，根据factor值删除指定目录下的文件
	 * 
	 * @param directory
	 *            目录
	 * @param factor
	 *            删除百分比，例如：0.4(40%)
	 */
	public static void releaseDirectorySpace(String directory, double factor) {
		File dir = new File(SdcardUtils.root() + "/" + directory);
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			if (files != null && files.length > 2) {
				int removeCount = (int) ((factor * files.length) + 1);
				Arrays.sort(files, new FileLastModifSort());
				for (int i = 0; i < removeCount; i++) {
					files[i].delete();
				}
			}
		}
	}

	/**
	 * 文件按最后修改时间序排序类
	 * 
	 * @author xiangming
	 * 
	 */
	public static class FileLastModifSort implements Comparator<File> {
		/**
		 * 比较最后的修改时间
		 */
		public int compare(File arg0, File arg1) {
			if (arg0.lastModified() > arg1.lastModified()) {
				return 1;
			} else if (arg0.lastModified() == arg1.lastModified()) {
				return 0;
			} else {
				return -1;
			}
		}
	}

	/**
	 * 写文件
	 * 
	 * @param filename
	 *            文件名
	 * @param text
	 *            内容
	 * @param append
	 *            true:追加 false:覆盖
	 * @return
	 */
	public static boolean write(String filename, String text, boolean append) {
		if (SdcardUtils.isMount()) {
			try {
				File file = create(filename);
				if (file != null) {
					FileOutputStream stream = new FileOutputStream(file, append);
					byte[] buf = text.getBytes();
					stream.write(buf);
					stream.close();
					return true;
				}
			} catch (Exception e) {
				LogUtils.e(e);
			}
		}
		return false;
	}


	/**
	 * 更新womai的照片目录
	 * @param activity
	 * @param path
	 */
	public static void updateGallery(Context activity, String path){
		path = SdcardUtils.root() + "/" + path;
		File file = new File(path);
		if (file.exists()) {
			if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (File subfile : files) {
					Uri uri = Uri.fromFile(subfile);
					Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
					intent.setData(uri);
					activity.sendBroadcast(intent);
				}
			}
		}
	}
}
