package jorge.com.autobottombar;

import android.annotation.SuppressLint;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志工具类
 * 
 * @author xiangming
 * 
 */
@SuppressLint("SimpleDateFormat")
public class LogUtils {

	public static boolean IsOpenLog = false;

	public static boolean IsOpenLogFile = false;
	
	private static final String tag_debug = "womai_debug";

	private static final String tag_error = "womai_error";
	
	private static final String ErrorLogPath = "womai/log/error";
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");

	public static void d(String s) {
		if (IsOpenLog) {
			Log.d(tag_debug, s);
		}
	}

	public static void e(String e) {
		if (IsOpenLog) {
			Log.e(tag_error, e);
			writeErrorLogFile(e);
		}
	}

	public static void e(Exception ex) {
		if (IsOpenLog) {
			Log.e(tag_error, "exception name-------------->" + ex.getMessage());
			Writer writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			ex.printStackTrace(printWriter);
			Throwable cause = ex.getCause();
			while (cause != null) {
				cause.printStackTrace(printWriter);
				cause = cause.getCause();
			}
			printWriter.close();
			String c = writer.toString();
			Log.e(tag_error, c);
			writeErrorLogFile(c);
		}
	}

	public static void e(Throwable ex) {
		if (IsOpenLog) {
			Writer writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			ex.printStackTrace(printWriter);
			Throwable cause = ex.getCause();
			while (cause != null) {
				cause.printStackTrace(printWriter);
				cause = cause.getCause();
			}
			printWriter.close();
			String c = writer.toString();
			Log.e(tag_error, c);
			writeErrorLogFile(c);
		}
	}
	
	private static void writeErrorLogFile(String e) {
		if (IsOpenLogFile) {
			String time = sdf.format(new Date(System.currentTimeMillis()));
			StringBuffer buf = new StringBuffer();
			buf.append(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>").append("\n");
			buf.append(time).append("\n");
			buf.append(e).append("\n");
			buf.append("\n");
			String filename = new StringBuffer(ErrorLogPath).append("/error ").append(time.substring(0, 13)).append(".txt").toString();
			FileUtils.write(filename, buf.toString(), true);
		}
	}
}
