package org.wzy.v2ex.support;

import java.io.File;
import java.io.IOException;

import org.wzy.v2ex.GlobalContext;
import org.wzy.v2ex.utils.AppLogger;

import android.os.Environment;

public class FileManager {
	private static final String AVATAR_SMALL = "avatar_small";
	
	private static String getSdCardPath() {
		if (isExternalStorageMounted()) {
            if (GlobalContext.getInstance().getExternalCacheDir() != null)
			return GlobalContext.getInstance().getExternalCacheDir().getAbsolutePath();
		} else {
			return "";
		}
        return "";
	}
	
	public static boolean isExternalStorageMounted() {
		boolean canRead = Environment.getExternalStorageDirectory().canRead();
		boolean onlyRead = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED_READ_ONLY);
		boolean unMounted = Environment.getExternalStorageState().equals(
				Environment.MEDIA_UNMOUNTED);
		
		return canRead && !onlyRead && !unMounted;
	}
	
	public static File createNewFileInSDCard(String absolutePath) {
		if (!isExternalStorageMounted()) {
			AppLogger.e("sd card unavailiable");
			return null;
		}
		
		File file = new File(absolutePath);
		if (file.exists()) {
			return file;
		} else {
			File dir = file.getParentFile();
			if (!dir.exists()) {
				dir.mkdirs();
			}
			
			try {
				if (file.createNewFile()) {
					return file;
				}
			} catch (IOException e) {
				AppLogger.d(e.getMessage());
				return null;
			}
		}
		
		return null;
	}
	
	public static String getFileFromUrl(String url, FileLocationMethod method) {
		if (!isExternalStorageMounted()) {
			return "";
		}
		
		int slashIndex = url.lastIndexOf('/');
		int pointIndex = url.lastIndexOf('.');
		String oldPath = url.substring(slashIndex + 1, pointIndex);
		
		String newPath = "";
		switch (method) {
		case avatar_small:
			newPath = File.separator + AVATAR_SMALL + File.separator + oldPath;
			break;
		}
		
		return getSdCardPath() + newPath;
	}
}
