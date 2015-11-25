package zenghao.com.androidasynchttp.zenghao.com.httpmanager;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeHierarchy;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.GenericDraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import zenghao.com.androidasynchttp.MyApplication;
import zenghao.com.androidasynchttp.R;
import zenghao.com.androidasynchttp.zenghao.com.http.LocalImageRequestBuilder;

/**
 * Fresco封装类
 *
 * @author jiwei@breadtrip.com
 * @version V1.0
 * @Project: BreadTripAndroid
 * @Package com.breadtrip.http
 * @Description: Fresco封装类，builder模式
 * @date 15/10/24 下午3:39
 */
public class FrescoManager {

    private static final String LOCAL_SCHEME = "file://";
    private static final String RES_SCHEME = "res://";

    private static Resources mResources;
    private static String mPackageName;

    /**
     * 加载uri
     *
     * @param uri uri
     * @return FrescoCreator
     */
    public static FrescoCreator load(@NonNull Uri uri) {
        return new FrescoCreator(uri);
    }

    /**
     * 加载网络图片
     *
     * @param url 图片地址
     * @return FrescoCreator
     */
    public static FrescoCreator loadUrl(@NonNull String url) {
        return load(Uri.parse(url));
    }

    /**
     * 加载本地文件
     *
     * @param filePath 文件路径
     * @return FrescoCreator
     */
    public static FrescoCreator loadFilePath(@NonNull String filePath) {
        if (!filePath.startsWith(LOCAL_SCHEME)) {
            filePath = new StringBuilder().append(LOCAL_SCHEME).append(filePath).toString();
        }
        return load(Uri.parse(filePath));
    }

    /**
     * 加载图片资源
     * <p>
     * 注意：像ShapeDrawable这样声明在XML中的drawable可能引起困惑。这毕竟不是图片，如果想把这样的drawable作为图像显示。
     * 那么把这个drawable设置为占位图，然后把URI设置为null。
     * </p>
     *
     * @param resId 资源id
     * @return FrescoCreator
     */
    public static FrescoCreator loadDrawableRes(@DrawableRes int resId) {
        String res = new StringBuilder().append(RES_SCHEME).append(getPackageName()).append("/").append(resId).toString();
        return load(Uri.parse(res));
    }

    /**
     * 获取Resources
     *
     * @return Resources
     */
    private static Resources getResource() {
        if (mResources == null) {
            mResources = MyApplication.getInstance().getResources();

        }
        return mResources;
    }

    /**
     * 获取包名
     *
     * @return 包名
     */
    private static String getPackageName() {
        if (mPackageName == null) {
            mPackageName = MyApplication.getInstance().getPackageName();
        }
        return mPackageName;
    }

    /**
     * fresco构造器
     */
    public static class FrescoCreator {

        /**
         * URI
         */
        private Uri mUri;
        /**
         * 缩放宽
         */
        private int mScaleWidth;
        /**
         * 缩放高
         */
        private int mScaleHeight;
        /**
         * 是否旋转
         */
        private boolean autoRotate;
        /**
         * 默认占位图
         */
        private Drawable mPlaceholderImage;
        /**
         * 加载失败占位图
         */
        private Drawable mFailureImage;
        /**
         * 点击重新加载
         */
        private boolean tapToRetry;
        /**
         * 缩放类型
         */
        private ScalingUtils.ScaleType mScaleType;
        /**
         * 圆圈
         */
        private boolean roundAsCircle;
        /**
         * 圆角
         */
        private float cornersRadius;
        /**
         * 动画自动播放
         */
        private boolean autoPlayAnimations;
        /**
         * 本地缩略图预览
         */
        private boolean localThumbnailPreviews;
        /**
         * 宽高比
         */
        private float aspectRatio;
        /**
         * 下载事件监听器
         */
        private BaseControllerListener mBaseControllerListener;
        /**
         * 图片后处理器
         */
        private BasePostprocessor mBasePostprocessor;

        FrescoCreator(@NonNull Uri uri) {
            this.mUri = uri;
        }

        /**
         * 缩放
         *
         * @param width  缩放宽
         * @param height 缩放高
         * @return FrescoCreator
         */
        public FrescoCreator resize(@IntRange(from = 1) int width, @IntRange(from = 1) int height) {
            this.mScaleWidth = width;
            this.mScaleHeight = height;
            return this;
        }

        /**
         * 是否自动旋转
         *
         * @param autoRotate 自动旋转
         * @return FrescoCreator
         */
        public FrescoCreator autoRotate(boolean autoRotate) {
            this.autoRotate = autoRotate;
            return this;
        }

        /**
         * 设置占位图
         *
         * @param placeholderImageRes 默认图图片资源id
         * @return FrescoCreator
         */
        public FrescoCreator placeholderImage(@DrawableRes int placeholderImageRes) {
            try {
                Drawable drawable = getResource().getDrawable(placeholderImageRes);
                if (drawable != null) {
                    return placeholderImage(drawable);
                }
            } catch (Resources.NotFoundException e) {
                // 避免传无效资源id，比如0
            }
            return this;
        }

        /**
         * 设置占位图
         *
         * @param placeholderImage 默认图drawable
         * @return FrescoCreator
         */
        public FrescoCreator placeholderImage(@NonNull Drawable placeholderImage) {
            this.mPlaceholderImage = placeholderImage;
            return this;
        }

        /**
         * 设置错误占位图
         *
         * @param failureImage 占位图resid
         * @return FrescoCreator
         */
        public FrescoCreator failureImage(@DrawableRes int failureImage) {
            Drawable drawable = getResource().getDrawable(failureImage);
            if (drawable != null) {
                return failureImage(drawable);
            }
            return this;
        }

        /**
         * 设置错误占位图
         *
         * @param failureImage 占位图drawable
         * @return FrescoCreator
         */
        public FrescoCreator failureImage(@NonNull Drawable failureImage) {
            this.mFailureImage = failureImage;
            return this;
        }

        public FrescoCreator tapToRetry(boolean tapToRetry) {
            this.tapToRetry = tapToRetry;
            return this;
        }

        /**
         * 设置缩放类型
         *
         * @param scaleType 缩放类型
         * @return FrescoCreator
         */
        public FrescoCreator scaleType(@NonNull ScalingUtils.ScaleType scaleType) {
            this.mScaleType = scaleType;
            return this;
        }

        /**
         * 设置图片圆圈
         *
         * @param roundAsCircle 圆圈
         * @return FrescoCreator
         */
        public FrescoCreator roundAsCircle(boolean roundAsCircle) {
            this.roundAsCircle = roundAsCircle;
            return this;
        }

        /**
         * 设置圆角
         * @param cornersRadius 圆角
         * @return FrescoCreator
         */
        public FrescoCreator cornersRadius(float cornersRadius) {
            this.cornersRadius = cornersRadius;
            return this;
        }

        /**
         * 自动播放动画和gif
         *
         * @param autoPlayAnimations 是否自动播放
         * @return FrescoCreator
         */
        public FrescoCreator autoPlayAnimations(boolean autoPlayAnimations) {
            this.autoPlayAnimations = autoPlayAnimations;
            return this;
        }

        /**
         * 缩略图预览
         * <p>
         * 本功能仅支持本地URI，并且是JPEG图片格式
         * 如果本地JPEG图，有EXIF的缩略图，image pipeline 会立刻返回一个缩略图。完整的清晰大图，在decode完之后再显示。
         * </p>
         *
         * @param localThumbnailPreviews 预览缩略图
         * @return FrescoCreator
         */
        public FrescoCreator localThumbnailPreviews(boolean localThumbnailPreviews) {
            this.localThumbnailPreviews = localThumbnailPreviews;
            return this;
        }

        /**
         * 设置宽高比
         *
         * @param aspectRatio 宽高比
         * @return FrescoCreator
         */
        public FrescoCreator aspectRatio(@FloatRange(from = 0) float aspectRatio) {
            this.aspectRatio = aspectRatio;
            return this;
        }

        /**
         * 设置下载监听器
         *
         * @param controllerListener 下载监听器
         * @return FrescoCreator
         */
        public FrescoCreator controllerListener(BaseControllerListener controllerListener) {
            this.mBaseControllerListener = controllerListener;
            return this;
        }

        /**
         * 添加图片后处理器
         * @param postprocessor 后处理器
         * @return FrescoCreator
         */
        public FrescoCreator postprocessor(BasePostprocessor postprocessor) {
            this.mBasePostprocessor = postprocessor;
            return this;
        }

        /**
         * 加载到fresco图片控件
         * <p><b>在这个方法触发加载，使用时这个方法在最后调用。这个方法之后的参数，都会被忽略</b></p>
         *
         * @param draweeView fresco图片控件
         */
        public void into(DraweeView draweeView) {

            if (draweeView instanceof GenericDraweeView) {
                if (aspectRatio > 0) {
                    ((GenericDraweeView) draweeView).setAspectRatio(aspectRatio);
                }
            }

            DraweeHierarchy hierarchy = initDraweeHierarchy(draweeView);
            if (hierarchy != null) {
                draweeView.setHierarchy(hierarchy);
            }

            initRoundingParams(draweeView);

            ImageRequest imageRequest = initImageRequest();

            PipelineDraweeController controller = initDraweeController(draweeView, imageRequest);

            draweeView.setController(controller);

        }

        /**
         * 初始化Hierarchy
         *
         * @param draweeView fresco图片控件
         * @return Hierarchy
         */
        private DraweeHierarchy initDraweeHierarchy(DraweeView draweeView) {
            if (draweeView instanceof GenericDraweeView) {
                GenericDraweeHierarchy hierarchy = ((GenericDraweeView) draweeView).getHierarchy();
                if (hierarchy == null) {
                    Resources resources = null;
                    if (draweeView.getResources() != null) {
                        resources = draweeView.getResources();
                    } else {
                        resources = getResource();
                    }
                    GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(resources);
                    hierarchy = builder.build();
                }
                if (mPlaceholderImage != null) {
                    hierarchy.setPlaceholderImage(mPlaceholderImage);
                }
                if (mFailureImage != null) {
                    hierarchy.setFailureImage(mFailureImage);
                }
                if (mScaleType != null) {
                    hierarchy.setActualImageScaleType(mScaleType);
                }
                return hierarchy;
            }
            // 目前都是基本用法，暂未考虑直接使用DraweeView的情况
            return null;
        }

        /**
         * 初始化圆角和圆圈相关参数
         *
         * @param draweeView Fresco图片控件
         */
        private void initRoundingParams(@NonNull DraweeView draweeView) {
            if (draweeView instanceof SimpleDraweeView && (roundAsCircle || cornersRadius > 0)) {
                RoundingParams roundingParams = ((SimpleDraweeView) draweeView).getHierarchy().getRoundingParams();
                if (roundingParams == null) {
                    roundingParams = new RoundingParams();
                }
                roundingParams.setRoundAsCircle(roundAsCircle);
                if (cornersRadius > 0) {
                    roundingParams.setCornersRadius(cornersRadius);
                }
                ((SimpleDraweeView) draweeView).getHierarchy().setRoundingParams(roundingParams);
            }
        }

        /**
         * 初始化ImageRequest，系统的Builder对本地文件路径支持有问题，
         * 但系统ImageRequestBuilder私有构造函数，不能继承，单独写了一个LocalImageRequestBuilder解决这个问题。
         *
         * @return ImageRequest
         */
        private ImageRequest initImageRequest() {
            if (UriUtil.isLocalFileUri(mUri)) {
                LocalImageRequestBuilder requestBuilder = LocalImageRequestBuilder.newBuilderWithSource(mUri);
                if (mScaleWidth > 0 && mScaleHeight > 0) {
                    requestBuilder.setResizeOptions(new ResizeOptions(mScaleWidth, mScaleHeight));
                }
                if (autoRotate) {
                    requestBuilder.setAutoRotateEnabled(autoRotate);
                }
                if (localThumbnailPreviews) {
                    requestBuilder.setLocalThumbnailPreviewsEnabled(localThumbnailPreviews);
                }
                if (mBasePostprocessor != null) {
                    requestBuilder.setPostprocessor(mBasePostprocessor);
                }
                return requestBuilder.build();
            } else {
                ImageRequestBuilder requestBuilder = ImageRequestBuilder.newBuilderWithSource(mUri);
                if (mScaleWidth > 0 && mScaleHeight > 0) {
                    requestBuilder.setResizeOptions(new ResizeOptions(mScaleWidth, mScaleHeight));
                }
                if (autoRotate) {
                    requestBuilder.setAutoRotateEnabled(autoRotate);
                }
                if (mBasePostprocessor != null) {
                    requestBuilder.setPostprocessor(mBasePostprocessor);
                }
                return requestBuilder.build();
            }
        }

        /**
         * 初始化Controller
         *
         * @param draweeView   fresco图片控件
         * @param imageRequest fresco图片请求
         * @return Controller
         */
        private PipelineDraweeController initDraweeController(DraweeView draweeView, ImageRequest imageRequest) {
            PipelineDraweeControllerBuilder controllerBuilder = Fresco.newDraweeControllerBuilder();
            controllerBuilder.setOldController(draweeView.getController());
            controllerBuilder.setImageRequest(imageRequest);
            if (tapToRetry) {
                controllerBuilder.setTapToRetryEnabled(tapToRetry);
            }
            if (autoPlayAnimations) {
                controllerBuilder.setAutoPlayAnimations(autoPlayAnimations);
            }
            if (mBaseControllerListener != null) {
                controllerBuilder.setControllerListener(mBaseControllerListener);
            }
            return (PipelineDraweeController) controllerBuilder.build();
        }
    }

    /**
     * 示例代码
     */
    private void demo() {
        DraweeView view = new DraweeView(MyApplication.getInstance());

        // 通用加载图片
        Uri uri = Uri.parse("");
        FrescoManager.load(uri).resize(100, 100).aspectRatio(2.5f).autoRotate(true)
                .placeholderImage(R.drawable.ic_launcher).into(view);

        // 加载网络图片
        String url = "www.xxx.com/a.jpg";
        FrescoManager.loadUrl(url).into(view);

        // 加载本地图片
        String filePath = "";
        FrescoManager.loadFilePath(filePath).into(view);

        // 加载默认图
        int resId = R.drawable.ic_launcher;
        FrescoManager.loadDrawableRes(resId).into(view);
    }
}
