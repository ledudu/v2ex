package org.wzy.v2ex.support;

import java.lang.ref.WeakReference;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

public class AvatarBitmapDrawable extends ColorDrawable {
	private final WeakReference<AvatarBitmapWorkerTask> bitmapDownloaderTaskReference;
	
	public AvatarBitmapDrawable(AvatarBitmapWorkerTask bitmapDownloaderTask) {
		super(Color.TRANSPARENT);
		bitmapDownloaderTaskReference =
				new WeakReference<AvatarBitmapWorkerTask>(bitmapDownloaderTask);
	}
	
	public AvatarBitmapWorkerTask getBitmapDownloaderTask() {
		return bitmapDownloaderTaskReference.get();
	}
}
