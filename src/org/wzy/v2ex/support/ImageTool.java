package org.wzy.v2ex.support;

import org.wzy.v2ex.http.HttpUtility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageTool {
	public static Bitmap getSmallAvatarWithRoundedCorner(String url) {
		if (!FileManager.isExternalStorageMounted()) {
			return null;
		}
		
		String absoluteFilePath = FileManager.getFileFromUrl(url, FileLocationMethod.avatar_small);
		absoluteFilePath = absoluteFilePath + ".png";
		
		Bitmap bitmap = BitmapFactory.decodeFile(absoluteFilePath);
		
		if (bitmap == null) {
			boolean result = getBitmapFromNetWork(url, absoluteFilePath, null);
			if (result) {
				bitmap = BitmapFactory.decodeFile(absoluteFilePath);
			} else {
				return null;
			}
		}
		
		return bitmap;
	}
	
	private static boolean getBitmapFromNetWork(String url, String path,
			FileDownloaderHttpHelper.DownloadListener downloadListener) {
		for (int i = 0; i < 5; i++) {
			boolean result = HttpUtility.getInstance().executeDownloadTask(url, path, downloadListener);
			if (result)
				return true;
		}
		
		return false;
	}
}
