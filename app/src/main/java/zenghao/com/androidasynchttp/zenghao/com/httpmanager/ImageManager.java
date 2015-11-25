package zenghao.com.androidasynchttp.zenghao.com.httpmanager;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;

import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 图片请求，查看 {@link FrescoManager}
 */
public class ImageManager {
	private static final String LOCAL_SCHEME = "file://";

	/**
	 * @deprecated 查看 {@link FrescoManager#loadFilePath(String)}，使用builder模式请求
	 */
	@Deprecated
	public static Uri getLocalUri(String filePath) {
		if (!TextUtils.isEmpty(filePath)) {
			if (!filePath.startsWith(LOCAL_SCHEME)) {
				filePath = new StringBuilder().append(LOCAL_SCHEME)
						.append(filePath).toString();
			}
			return Uri.parse(filePath);
		}
		return null;
	}

	/**
	 * @deprecated 查看 {@link FrescoManager#loadUrl(String)}，使用builder模式请求
	 */
	@Deprecated
	public static Uri getNetUri(String url) {
		if (!TextUtils.isEmpty(url)) {
			return Uri.parse(url);
		}
		return null;
	}

	/**
	 * @deprecated 查看 {@link FrescoManager#loadDrawableRes(int)}，使用builder模式请求
	 */
	@Deprecated
	public static Uri getResUri(String packageName,int resId){
		return Uri.parse("res://"+packageName+"/"+resId);
	}

	/**
	 * @deprecated 查看 {@link FrescoManager}，使用builder模式请求
	 */
	@Deprecated
	public static void loadImage(SimpleDraweeView imageView,
			int defaultImageRes, Uri uri) {
		loadImage(imageView, defaultImageRes, uri, 0);
	}

	/**
	 * @deprecated 查看 {@link FrescoManager}，使用builder模式请求
	 */
	@Deprecated
	public static void loadImage(SimpleDraweeView imageView,
			int defaultImageRes, Uri uri, float aspectRatio) {
		FrescoManager.load(uri).placeholderImage(defaultImageRes).aspectRatio(aspectRatio).into(imageView);
	}
	/**
	 * @deprecated 查看 {@link FrescoManager}，使用builder模式请求
	 */
	@Deprecated
	public static void setImageResource(SimpleDraweeView imageView,
			int defaultImageRes) {
		FrescoManager.loadDrawableRes(defaultImageRes).into(imageView);
	}

	/**
	 * @deprecated 查看 {@link FrescoManager}，使用builder模式请求
	 */
	@Deprecated
	public static void loadImage(SimpleDraweeView imageView,
			int defaultImageRes, Uri uri, int width, int height) {
		FrescoManager.load(uri).resize(width, height).placeholderImage(defaultImageRes).into(imageView);
	}

	/**
	 * @deprecated 查看 {@link FrescoManager}，使用builder模式请求
	 */
	@Deprecated
	public static void loadImage(SimpleDraweeView imageView,
			int defaultImageRes, Uri uri, int width, int height,boolean rotate) {
		FrescoManager.load(uri).resize(width, height).autoRotate(rotate).placeholderImage(defaultImageRes).into(imageView);
	}

	/**
	 * @deprecated 查看 {@link FrescoManager}，使用builder模式请求
	 */
	@Deprecated
	public static void loadImageNoPlaceholder(SimpleDraweeView imageView, Uri uri, int width, int height,boolean rotate) {
		FrescoManager.load(uri).resize(width, height).autoRotate(rotate).into(imageView);
	}

	/**
	 * @deprecated 查看 {@link FrescoManager}，使用builder模式请求
	 */
	@Deprecated
	public static void loadLocalImageNoPlaceholder(SimpleDraweeView imageView, Uri uri, int width, int height,boolean rotate) {
		FrescoManager.load(uri).resize(width, height).autoRotate(rotate).into(imageView);
	}

	/**
	 * @deprecated 查看 {@link FrescoManager}，使用builder模式请求
	 */
	@Deprecated
	public static void loadLocalImage(SimpleDraweeView imageView,
			int defaultImageRes, Uri uri, int width, int height,boolean rotate) {
		FrescoManager.load(uri).resize(width, height).autoRotate(rotate).placeholderImage(defaultImageRes).into(imageView);
	}

	/**
	 * @deprecated 查看 {@link FrescoManager}，使用builder模式请求
	 */
	@Deprecated
	public static void loadLocalGif(SimpleDraweeView imageView,Uri uri){
		FrescoManager.load(uri).autoPlayAnimations(true).into(imageView);
	}

	/**
	 * @deprecated 查看 {@link FrescoManager}，使用builder模式请求
	 */
	@Deprecated
	public static void loadImage(SimpleDraweeView imageView,
			int defaultImageRes, Uri uri, int width, int height, BaseControllerListener<ImageInfo> listener) {
		FrescoManager.load(uri).resize(width, height).controllerListener(listener).placeholderImage(defaultImageRes).into(imageView);
	}

	/**
	 * 从网络获取图片
	 * @param url
	 * @return
	 */
	public static Bitmap requestBitmapFromNet(String url, int width, int height) {
		URL u = null;
		try {
			u = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(is, null, opt);
			double tempRateWidth = 1.0 * opt.outWidth / width;
			double tempRateHeight = 1.0 * opt.outHeight / height;
			opt.inSampleSize = Math.max(1, Math.max((int)tempRateWidth, (int)tempRateHeight));
			opt.inPreferredConfig = Config.RGB_565;
			opt.inJustDecodeBounds = false;
			is.close();
			conn.disconnect();
			
			HttpURLConnection conn2 = (HttpURLConnection) u.openConnection();
			conn2.setDoInput(true);
			conn2.connect();
			InputStream is2 = conn2.getInputStream();
			BitmapFactory.Options opt2 = new BitmapFactory.Options();
			opt2.inSampleSize = Math.max(1, Math.max((int)tempRateWidth, (int)tempRateHeight));
			opt2.inPreferredConfig = Config.RGB_565;
			Bitmap bitmap2 = BitmapFactory.decodeStream(is2, null, opt2);
			is.close();
			is2.close();
			conn2.disconnect();
			return bitmap2;
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}
}
