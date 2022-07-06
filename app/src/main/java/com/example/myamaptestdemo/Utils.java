/**
 * 
 */
package com.example.myamaptestdemo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;

import com.amap.api.location.AMapLocation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 辅助工具类
 * @创建时间： 2015年11月24日 上午11:46:50
 * @项目名称： AMapLocationDemo2.x
 * @author hongming.wang
 * @文件名称: Utils.java
 * @类型名称: Utils
 */
public class Utils {
	/**
	 *  开始定位
	 */
	public final static int MSG_LOCATION_START = 0;
	/**
	 * 定位完成
	 */
	public final static int MSG_LOCATION_FINISH = 1;
	/**
	 * 停止定位
	 */
	public final static int MSG_LOCATION_STOP= 2;
	
	public final static String KEY_URL = "URL";
	public final static String URL_H5LOCATION = "file:///android_asset/sdkLoc.html";
	/**
	 * 根据定位结果返回定位信息的字符串
	 * @param location
	 * @return
	 */
	public synchronized static String getLocationStr(AMapLocation location){
		if(null == location){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		//errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
		if(location.getErrorCode() == 0){
			sb.append("定位成功" + "\n");
			sb.append("定位类型: " + location.getLocationType() + "\n");
			sb.append("经    度    : " + location.getLongitude() + "\n");
			sb.append("纬    度    : " + location.getLatitude() + "\n");
			sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
			sb.append("提供者    : " + location.getProvider() + "\n");

			sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
			sb.append("角    度    : " + location.getBearing() + "\n");
			// 获取当前提供定位服务的卫星个数
			sb.append("星    数    : " + location.getSatellites() + "\n");
			sb.append("国    家    : " + location.getCountry() + "\n");
			sb.append("省            : " + location.getProvince() + "\n");
			sb.append("市            : " + location.getCity() + "\n");
			sb.append("城市编码 : " + location.getCityCode() + "\n");
			sb.append("区            : " + location.getDistrict() + "\n");
			sb.append("区域 码   : " + location.getAdCode() + "\n");
			sb.append("地    址    : " + location.getAddress() + "\n");
			sb.append("兴趣点    : " + location.getPoiName() + "\n");
			//定位完成的时间
			sb.append("定位时间: " + formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
		} else {
			//定位失败
			sb.append("定位失败" + "\n");
			sb.append("错误码:" + location.getErrorCode() + "\n");
			sb.append("错误信息:" + location.getErrorInfo() + "\n");
			sb.append("错误描述:" + location.getLocationDetail() + "\n");
		}
		//定位之后的回调时间
		sb.append("回调时间: " + formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");
		return sb.toString();
	}

	private static SimpleDateFormat sdf = null;
	public  static String formatUTC(long l, String strPattern) {
		if (TextUtils.isEmpty(strPattern)) {
			strPattern = "yyyy-MM-dd HH:mm:ss";
		}
		if (sdf == null) {
			try {
				sdf = new SimpleDateFormat(strPattern, Locale.CHINA);
			} catch (Throwable e) {
			}
		} else {
			sdf.applyPattern(strPattern);
		}
		return sdf == null ? "NULL" : sdf.format(l);
	}

	/**
	 * 获取app的名称
	 * @param context
	 * @return
	 */
	public static String getAppName(Context context) {
		String appName = "";
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			appName =  context.getResources().getString(labelRes);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return appName;
	}

	/**
	 * 隐藏软键盘的方法
	 * @param context context
	 * */
	public static void hideKeyboard(Activity context) {
		InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		// 隐藏软键盘
		inputMethodManager.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(), 0);
	}


	/**
	 * 为frame添加补间动画
	 *
	 * @param view View
	 * @param visibility View.VISIBLE
	 * @param animationType int : 0:activity up or down | 1: activity left or right
	 * */
	public static void addAnimation(View view, int visibility, int animationType) {
		int mVisibility = view.getVisibility();
		Context mContext = view.getContext();

		if (visibility == mVisibility) {
			return;
		}

		if (mVisibility != View.VISIBLE) {
			view.setVisibility(View.VISIBLE);
		}

		switch (animationType) {
			case 0:
				if (visibility == View.VISIBLE) {
					view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.activity_down_to_up_enter));
				} else {
					view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.activity_up_to_down_exit));
				}
				break;

			case 1:
				if (visibility == View.VISIBLE) {
					view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.activity_right_to_left_enter));
				} else {
					view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.activity_left_to_right_exit));
				}
				break;
		}

		if (visibility != View.VISIBLE) {
			view.getAnimation().setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					view.setVisibility(View.GONE);
				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}
			});
		}
	}


	//手指向右滑动时的最小速度
	private static final int XSPEED_MIN = 200;
	//手指向右滑动时的最小距离
	private static final int XDISTANCE_MIN = 150;
	//记录手指按下时的横坐标。
	private static float xDown;
	//用于计算手指滑动的速度。
	private static VelocityTracker mVelocityTracker;

	/**
	 * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。
	 *
	 * @param event
	 *
	 */
	private static void createVelocityTracker(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}

	/**
	 * 回收VelocityTracker对象。
	 */
	private static void recycleVelocityTracker() {
		mVelocityTracker.recycle();
		mVelocityTracker = null;
	}

	/**
	 * 获取手指在content界面滑动的速度。
	 *
	 * @return 滑动速度，以每秒钟移动了多少像素值为单位。
	 */
	private static int getScrollVelocity() {
		mVelocityTracker.computeCurrentVelocity(1000);
		int velocity = (int) mVelocityTracker.getXVelocity();
		return Math.abs(velocity);
	}

	/**
	 * 判定返回上一层 frame 的动作是否有效
	 *
	 * @param motionEvent 触摸事件
	 * @return 有效返回true, 否则放回 false
	 * */
	public static boolean back2PreviousFrame(MotionEvent motionEvent) {
		createVelocityTracker(motionEvent);
		switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:
				xDown = motionEvent.getRawX();
				break;
			case MotionEvent.ACTION_MOVE:
				//记录手指移动时的横坐标。
				float xMove = motionEvent.getRawX();
				//活动的距离
				int distanceX = (int) (xMove - xDown);
				//获取顺时速度
				int xSpeed = getScrollVelocity();
				//当滑动的距离大于我们设定的最小距离且滑动的瞬间速度大于我们设定的速度时,需要返回上一层 frame
				if(distanceX > XDISTANCE_MIN && xSpeed > XSPEED_MIN) {
					return true;
				}
				break;
			case MotionEvent.ACTION_UP:
				recycleVelocityTracker();
				break;
			default:
				break;
		}
		return false;
	}


	/**
	 * 创建外部存储空间中的应用专属目录
	 * @param context Context
	 * @param albumName 目录名称
	 * */
	@Nullable
	public static File getAppSpecificAlbumStorageDir(Context context, String albumName) {
		// Get the pictures directory that's inside the app-specific directory on external storage.
		File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), albumName);
		if (file == null || !file.mkdirs()) {
			Log.e("Utils", "Directory not created");
		}
		return file;
	}


	/**
	 * 创建内部存储空间中的应用专属目录
	 * @param context Context
	 * @param albumName 目录名称
	 * */
	@Nullable
	public static File getAppSpecificStorageDir(Context context, String albumName) {
		File file = new File(context.getFilesDir(), albumName);
		if (file == null || !file.mkdirs()) {
			Log.e("UserinfoFragment", "Directory not created");
		}
		return file;
	}

//	private static final int originStackIndex = 2;
//	public static String LOG_TAG = Thread.currentThread().getStackTrace()[originStackIndex].getFileName() + "( Line: " + Thread.currentThread().getStackTrace()[2].getLineNumber() + " )";


//	public static void copyFile(String src,String target)
//	{
//		File srcFile = new File(src);
//		File targetFile = new File(target);
//		try {
//			InputStream in = new FileInputStream(srcFile);
//			OutputStream out = new FileOutputStream(targetFile);
//			byte[] bytes = new byte[1024];
//			int len = -1;
//			while((len=in.read(bytes))!=-1)
//			{
//				out.write(bytes, 0, len);
//			}
//			in.close();
//			out.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
////		System.out.println("文件复制成功");
//		Log.e("Utils", "文件复制成功");
//
//
//
//	}




//	/**
//	 * 将图片文件转为字符串
//	 * @param imgFile
//	 * @return
//	 */
//	public static String getImageStr(String imgFile) {
//		// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
//		//String imgFile = "d:\\111.jpg";// 待处理的图片
//		InputStream in = null;
//		byte[] data = null;
//		// 读取图片字节数组
//		try {
//			in = new FileInputStream(imgFile);
//			data = new byte[in.available()];
//			in.read(data);
//			in.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		// 对字节数组Base64编码
//		BASE64Encoder encoder = new BASE64Encoder();
//		// 返回Base64编码过的字节数组字符串
//		return encoder.encode(data);
//	}
//
//
//	/**
//	 * 将图片文件转为byte数字
//	 * @param imgFile
//	 * @return
//	 */
//	public static byte[] getImageByte(String imgFile) {
//		// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
//		//String imgFile = "d:\\111.jpg";// 待处理的图片
//		InputStream in = null;
//		byte[] data = null;
//		// 读取图片字节数组
//		try {
//			in = new FileInputStream(imgFile);
//			data = new byte[in.available()];
//			in.read(data);
//			in.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		// 返回Base64编码过的字节数组字符串
//		return data;
//	}
//
//
//	/**
//	 * 将字符串转为图片
//	 * @param imgStr
//	 * @return
//	 */
//	public static boolean generateImage(String imgStr,String imgFile)throws Exception {
//		// 对字节数组字符串进行Base64解码并生成图片
//		if (imgStr == null) // 图像数据为空
//			return false;
//		BASE64Decoder decoder = new BASE64Decoder();
//		try {
//			// Base64解码
//			byte[] b = decoder.decodeBuffer(imgStr);
//			for (int i = 0; i < b.length; ++i) {
//				if (b[i] < 0) {// 调整异常数据
//					b[i] += 256;
//				}
//			}
//			// 生成jpeg图片
//			String imgFilePath = imgFile;// 新生成的图片
//			OutputStream out = new FileOutputStream(imgFilePath);
//			out.write(b);
//			out.flush();
//			out.close();
//			return true;
//		} catch (Exception e) {
//			throw e;
//		}
//	}
//
//
//	/**
//	 * 图片是否符合 jpg gjf png格式
//	 * @param imgStr
//	 * @return
//	 */
//	public static boolean isRightFormat(String format){
//
//		return (format.equals("jpg") || format.equals("gif") || format.equals("png"))?true:false;
//	}
//
//
//
//	/**
//	 * 对图片进行放大
//	 * @param originalImage 原始图片
//	 * @param times 放大倍数
//	 * @return
//	 */
//
//	public static BufferedImage  zoomInImage(BufferedImage  originalImage, Integer times){
//
//		int width = originalImage.getWidth()*times;
//		int height = originalImage.getHeight()*times;
//
//		BufferedImage newImage = new BufferedImage(width,height,originalImage.getType());
//		Graphics g = newImage.getGraphics();
//		g.drawImage(originalImage, 0,0,width,height,null);
//		g.dispose();
//		return newImage;
//	}
//
//
//	/**
//	 * 对图片进行放大
//	 * @param srcPath 原始图片路径(绝对路径)
//	 * @param newPath 放大后图片路径（绝对路径）
//	 * @param times 放大倍数
//	 * @return 是否放大成功
//	 */
//
//	public static boolean zoomInImage(String srcPath,String newPath,Integer times){
//
//		BufferedImage bufferedImage = null;
//		try{
//
//			File of = new File(srcPath);
//			if(of.canRead()){
//
//				bufferedImage =  ImageIO.read(of);
//
//			}
//		}catch(Exception e){
//			//TODO: 打印日志
//			return false;
//		}
//		if(bufferedImage != null){
//
//			bufferedImage = zoomInImage(bufferedImage,times);
//			try {
//				//TODO: 这个保存路径需要配置下子好一点
//				//保存修改后的图像,全部保存为JPG格式
//				ImageIO.write(bufferedImage, "JPG", new File(newPath));
//			} catch (IOException e) {
//				// TODO 打印错误信息
//				return false;
//			}
//		}
//		return true;
//
//	}
//
//
//	/**
//	 * 对图片进行缩小
//	 * @param originalImage 原始图片
//	 * @param times 缩小倍数
//	 * @return 缩小后的Image
//	 */
//	public static BufferedImage  zoomOutImage(BufferedImage  originalImage, Integer times){
//		int width = originalImage.getWidth()/times;
//		int height = originalImage.getHeight()/times;
//		BufferedImage newImage = new BufferedImage(width,height,originalImage.getType());
//		Graphics g = newImage.getGraphics();
//		g.drawImage(originalImage, 0,0,width,height,null);
//		g.dispose();
//		return newImage;
//	}
//
//	/**
//	 * 对图片进行放大
//	 * @param srcPath 原始图片路径(绝对路径)
//	 * @param newPath 放大后图片路径（绝对路径）
//	 * @param times 放大倍数
//	 * @return 是否放大成功
//	 */
//
//	public static boolean zoomOutImage(String srcPath,String newPath,Integer times){
//		BufferedImage bufferedImage = null;
//		try {
//			File of = new File(srcPath);
//			if(of.canRead()){
//				bufferedImage =  ImageIO.read(of);
//			}
//		} catch (IOException e) {
//			//TODO: 打印日志
//			return false;
//		}
//		if(bufferedImage != null){
//			bufferedImage = zoomOutImage(bufferedImage,times);
//			try {
//				//TODO: 这个保存路径需要配置下子好一点
//				//保存修改后的图像,全部保存为JPG格式
//				ImageIO.write(bufferedImage, "JPG", new File(newPath));
//			} catch (IOException e) {
//				// TODO 打印错误信息
//				return false;
//			}
//		}
//		return true;
//	}


}
