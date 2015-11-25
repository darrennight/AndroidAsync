package zenghao.com.androidasynchttp.zenghao.com.http;

import android.net.Uri;
import android.net.Uri.Builder;

import com.facebook.common.internal.Preconditions;
import com.facebook.common.util.UriUtil;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequest.ImageType;
import com.facebook.imagepipeline.request.ImageRequest.RequestLevel;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;

import java.io.File;

public class LocalImageRequestBuilder {
	private Uri mSourceUri = null;
	private RequestLevel mLowestPermittedRequestLevel;
	private boolean mAutoRotateEnabled;
	private ResizeOptions mResizeOptions;
	private ImageDecodeOptions mImageDecodeOptions;
	private ImageType mImageType;
	private boolean mProgressiveRenderingEnabled;
	private boolean mLocalThumbnailPreviewsEnabled;
	private Priority mRequestPriority;
	private Postprocessor mPostprocessor;

	public static LocalImageRequestBuilder newBuilderWithSource(Uri uri) {
		return (new LocalImageRequestBuilder()).setSource(uri);
	}

	public static LocalImageRequestBuilder newBuilderWithResourceId(int resId) {
		Uri uri = (new Builder()).scheme("res").path(String.valueOf(resId)).build();
		return newBuilderWithSource(uri);
	}

	public static LocalImageRequestBuilder fromRequest(ImageRequest imageRequest) {
		return newBuilderWithSource(imageRequest.getSourceUri())
				.setAutoRotateEnabled(imageRequest.getAutoRotateEnabled())
				.setImageDecodeOptions(imageRequest.getImageDecodeOptions()).setImageType(imageRequest.getImageType())
				.setLocalThumbnailPreviewsEnabled(imageRequest.getLocalThumbnailPreviewsEnabled())
				.setLowestPermittedRequestLevel(imageRequest.getLowestPermittedRequestLevel())
				.setPostprocessor(imageRequest.getPostprocessor())
				.setProgressiveRenderingEnabled(imageRequest.getProgressiveRenderingEnabled())
				.setRequestPriority(imageRequest.getPriority()).setResizeOptions(imageRequest.getResizeOptions());
	}

	private LocalImageRequestBuilder() {
		this.mLowestPermittedRequestLevel = RequestLevel.FULL_FETCH;
		this.mAutoRotateEnabled = false;
		this.mResizeOptions = null;
		this.mImageDecodeOptions = ImageDecodeOptions.defaults();
		this.mImageType = ImageType.DEFAULT;
		this.mProgressiveRenderingEnabled = false;
		this.mLocalThumbnailPreviewsEnabled = false;
		this.mRequestPriority = Priority.HIGH;
		this.mPostprocessor = null;
	}

	public LocalImageRequestBuilder setSource(Uri uri) {
		Preconditions.checkNotNull(uri);
		this.mSourceUri = uri;
		return this;
	}

	public Uri getSourceUri() {
		return this.mSourceUri;
	}

	public LocalImageRequestBuilder setLowestPermittedRequestLevel(RequestLevel requestLevel) {
		this.mLowestPermittedRequestLevel = requestLevel;
		return this;
	}

	public RequestLevel getLowestPermittedRequestLevel() {
		return this.mLowestPermittedRequestLevel;
	}

	public LocalImageRequestBuilder setAutoRotateEnabled(boolean enabled) {
		this.mAutoRotateEnabled = enabled;
		return this;
	}

	public boolean isAutoRotateEnabled() {
		return this.mAutoRotateEnabled;
	}

	public LocalImageRequestBuilder setResizeOptions(ResizeOptions resizeOptions) {
		this.mResizeOptions = resizeOptions;
		return this;
	}

	public ResizeOptions getResizeOptions() {
		return this.mResizeOptions;
	}

	public LocalImageRequestBuilder setImageDecodeOptions(ImageDecodeOptions imageDecodeOptions) {
		this.mImageDecodeOptions = imageDecodeOptions;
		return this;
	}

	public ImageDecodeOptions getImageDecodeOptions() {
		return this.mImageDecodeOptions;
	}

	public LocalImageRequestBuilder setImageType(ImageType imageType) {
		this.mImageType = imageType;
		return this;
	}

	public ImageType getImageType() {
		return this.mImageType;
	}

	public LocalImageRequestBuilder setProgressiveRenderingEnabled(boolean enabled) {
		this.mProgressiveRenderingEnabled = enabled;
		return this;
	}

	public boolean isProgressiveRenderingEnabled() {
		return this.mProgressiveRenderingEnabled;
	}

	public LocalImageRequestBuilder setLocalThumbnailPreviewsEnabled(boolean enabled) {
		this.mLocalThumbnailPreviewsEnabled = enabled;
		return this;
	}

	public boolean isLocalThumbnailPreviewsEnabled() {
		return this.mLocalThumbnailPreviewsEnabled;
	}

	public boolean isDiskCacheEnabled() {
		return UriUtil.isNetworkUri(this.mSourceUri);
	}

	public LocalImageRequestBuilder setRequestPriority(Priority requestPriority) {
		this.mRequestPriority = requestPriority;
		return this;
	}

	public Priority getRequestPriority() {
		return this.mRequestPriority;
	}

	public LocalImageRequestBuilder setPostprocessor(Postprocessor postprocessor) {
		this.mPostprocessor = postprocessor;
		return this;
	}

	public Postprocessor getPostprocessor() {
		return this.mPostprocessor;
	}

	public ImageRequest build() {
		this.validate();
		ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(mSourceUri).setImageType(mImageType)
				.setProgressiveRenderingEnabled(mProgressiveRenderingEnabled)
				.setLocalThumbnailPreviewsEnabled(mLocalThumbnailPreviewsEnabled)
				.setImageDecodeOptions(mImageDecodeOptions).setResizeOptions(mResizeOptions)
				.setAutoRotateEnabled(mAutoRotateEnabled).setRequestPriority(mRequestPriority)
				.setLowestPermittedRequestLevel(mLowestPermittedRequestLevel).setPostprocessor(mPostprocessor);

		return new LocalImageRequest(builder);
	}

	protected void validate() {
		if (this.mSourceUri == null) {
			throw new LocalImageRequestBuilder.BuilderException("Source must be set!");
		} else {
			if (UriUtil.isLocalResourceUri(this.mSourceUri)) {
				if (!this.mSourceUri.isAbsolute()) {
					throw new LocalImageRequestBuilder.BuilderException("Resource URI path must be absolute.");
				}

				if (this.mSourceUri.getPath().isEmpty()) {
					throw new LocalImageRequestBuilder.BuilderException("Resource URI must not be empty");
				}

				try {
					Integer.parseInt(this.mSourceUri.getPath().substring(1));
				} catch (NumberFormatException var2) {
					throw new LocalImageRequestBuilder.BuilderException("Resource URI path must be a resource id.");
				}
			}

			if (UriUtil.isLocalAssetUri(this.mSourceUri) && !this.mSourceUri.isAbsolute()) {
				throw new LocalImageRequestBuilder.BuilderException("Asset URI path must be absolute.");
			}
		}
	}

	public static class BuilderException extends RuntimeException {
		public BuilderException(String message) {
			super("Invalid request builder: " + message);
		}
	}

	public static class LocalImageRequest extends ImageRequest {

		protected LocalImageRequest(ImageRequestBuilder builder) {
			super(builder);
		}

		@Override
		public synchronized File getSourceFile() {
			File file = super.getSourceFile();
			if (file == null || !file.exists()) {
				file = new File(getSourceUri().getEncodedPath());
			}
			return file;
		}
	}
}
