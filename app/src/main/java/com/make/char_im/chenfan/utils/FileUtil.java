package com.make.char_im.chenfan.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class FileUtil {

    private static File path;
    private final static String CACHE_DIR_NAME = File.separator + "cache";
    private static File appPath;
    private final static String APP_FILE_TOP_NAME = "le_meng";

    /**
     * 获取保存文件位置根路径
     *
     * @param context
     * @param isOutPath 网络相关的请不要使用true!!!!!,其它可考虑是否保存在app外(沙盒)
     * @return File()
     */
    public static File getSaveRootPath(Context context, boolean isOutPath) {

        if (isOutPath) {
            if (path == null) {
                if (hasSDCard()) { // SD card
                    path = new File(getSDCardPath() + File.separator + APP_FILE_TOP_NAME);
                    path.mkdir();
                } else {
                    path = Environment.getDataDirectory();
                }
            }
            return path;
        } else {
            if (appPath == null) {
                appPath = context.getFilesDir();
            }
            return appPath;
        }
    }

    public static String getSaveCrashPath(Context context) {
        String date = DateUtils.formatDateTime(context, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME);
        return getCrashRootPath(context) + File.separator + date + ".log";
    }

    /**
     * 获取Crash文件保存位置根路径
     *
     * @param context
     * @return
     */
    private static File getCrashRootPath(Context context) {
        File file = new File(getSaveRootPath(context, true) + File.separator + "crash");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File[] getCrashFiles(Context context) {
        return getCrashRootPath(context).listFiles();
    }

    public static void clearCrashFiles(Context context) {
        File[] cacheFiles = getCrashFiles(context);
        if (cacheFiles != null) {
            for (File file : cacheFiles) {
                file.delete();
            }
        }
    }

    /**
     * 获取全局缓存目录路径
     *
     * @param context
     * @param isOut 是否保存在app沙盒外，网络缓存请使用false
     * @return
     */
    public static String getCacheFilePath(Context context, boolean isOut) {
        File f = getSaveRootPath(context, isOut);

        f = new File(f.getAbsolutePath() + CACHE_DIR_NAME);
        if (!f.exists()) {
            f.mkdirs();
        }

        return f.getAbsolutePath();
    }

    /**
     * 判断是否有sd卡
     *
     * @return
     */
    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取sd卡路径
     *
     * @return
     */
    private static String getSDCardPath() {
        File path = Environment.getExternalStorageDirectory();
        return path.getAbsolutePath();
    }

    /**
     * 获取uri的真实url
     *
     * @param context
     * @param contentURI
     * @return
     */
    public static String getRealPathFromURI(Context context, Uri contentURI) {
        String result;
        String[] proj = {MediaStore.Images.ImageColumns.DATA};
        Cursor cursor = context.getContentResolver().query(contentURI, proj, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }

        return String.format(result, "utf-8");
    }

    /***
     * clear the app cache create when photo handling,not app all cache
     *
     * @param context
     */
    public static void clearLocalCache(Context context) {
        File fileRoot = new File(getCacheFilePath(context,true));
        if (fileRoot != null) {
            File[] cacheFiles = fileRoot.listFiles();
            for (File file : cacheFiles) {
                file.delete();
            }
        }
    }

    /**
     * get all files path of assets by parent path
     *
     * @param context
     * @param path    parent path
     * @return
     */
    public static List<String> getAllFilePathsFromAssets(Context context, final String path) {
        List<String> filePaths = new ArrayList<>();
        String[] fileNames = null;
        try {
            fileNames = context.getAssets().list(path);
            if (fileNames != null) {
                for (String fileName : fileNames) {
                    filePaths.add(fileName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePaths;
    }

    /**
     * 获取文件夹下所有文件路径
     *
     * @param file
     * @return
     */
    public static List<String> getAllFilePath(File file) {
        List<String> list = null;
        if(file.exists() && file.isDirectory()) {
            list =  new ArrayList<String>();
            File[] files = file.listFiles();
            for(File f : files) {
                list.add(f.getAbsolutePath());
            }
            Collections.sort(list, new Comparator<String>() {
                @Override
                public int compare(String str1, String str2) {
                    return str1.compareTo(str2);
                }
            });
        }
        return list;
    }

    /**
     * 删除文件或者文件夹下所有文件
     * @param file
     */
    public static void deleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                return;
            }
            for (File f : childFile) {
                deleteFile(f);
            }
        }
    }

    /**
     * 保存图片到SD卡上
     *
     * @param bm
     * @param fileName
     *
     */
    static File file;
    static String IMGURL = "/image/";

    public static boolean saveFile(Bitmap bmp,String type, String url) {
        try {
            Bitmap bm = bmp;
            boolean sdCardExist = Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
            // 获得文件名字
            final String fileNa = url;
            // 创建图片缓存文件夹
            if (sdCardExist) {
                file = new File(Environment.getExternalStorageDirectory().getPath() + IMGURL +type+ fileNa);
                String path = Environment.getExternalStorageDirectory().getPath() + IMGURL;
                File ad_ = new File(path+type);
                if (!ad_.exists()){
                    ad_.mkdirs();
                }
                // 检查图片是否存在
                if (!file.exists()) {
                    file.createNewFile();
                }
            }
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(file));
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            return true;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;

    }
}
