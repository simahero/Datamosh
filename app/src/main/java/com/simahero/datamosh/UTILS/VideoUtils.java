package com.simahero.datamosh.UTILS;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class VideoUtils {

    private static final String TAG = "VideoUtils";

    //SIMPLE URI TO STRING PATH, COPY TO APP DIR ON ANDROID Q
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        if (Build.VERSION.SDK_INT >= 29) {
            return copyFileToCache(context, contentUri);
        } else {
            String[] proj = {MediaStore.Video.Media.DATA};
            Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
    }


    //GETTING THUMBNAIL FROM VIDEO
    public static void getBitmapFromUri(Context context, Uri uri, MutableLiveData<Bitmap> data) throws Throwable {
        Bitmap bitmap;
        bitmap = retriveVideoFrameFromVideo(context, uri, 5);
        if (bitmap != null) {
            bitmap = Bitmap.createBitmap(bitmap);
            //bitmap = Bitmap.createScaledBitmap(bitmap, 240, 240, false);
            data.setValue(bitmap);
        }
    }

    public static Bitmap retriveVideoFrameFromVideo(Context context, Uri uri, int time) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(context, uri);
            bitmap = mediaMetadataRetriever.getFrameAtTime(time, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    //ADDING RESULTS TO MEDIASTORE
    public static void insertToMediaStore(Activity activity, String path) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Video.Media.TITLE, "moshed");
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/avi");
        values.put(MediaStore.Video.Media.DATA, path);
        ContentResolver resolver = activity.getBaseContext().getContentResolver();
        resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
    }

    @SuppressLint("NewApi")
    public static String getPath(Context context, Uri uri) {
        if (Build.VERSION.SDK_INT >= 29){
            String wholeID = DocumentsContract.getDocumentId(uri);
            String id = wholeID.split(":")[1];
            String[] col = {MediaStore.Video.Media.DATA};
            String sel = MediaStore.Video.Media._ID + "=?";
            Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, col, sel, new String[]{id}, null);
            int colindex = 0;
            if (cursor != null){
                colindex = cursor.getColumnIndex(col[0]);
                if (cursor.moveToFirst()){
                    return cursor.getString(colindex);
                }
                cursor.close();
            }
        }
            final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
            String selection = null;
            String[] selectionArgs = null;
            if (needToCheckUri && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                } else if (isDownloadsDocument(uri)) {
                    final String id = DocumentsContract.getDocumentId(uri);
                    uri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                } else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("image".equals(type)) {
                        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    selection = "_id=?";
                    selectionArgs = new String[]{split[1]};
                }
            }

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static String copyFileToCache(Context context, Uri uri) {
        File f = new File(context.getFilesDir() + "moshed.mp4");
        try {
            FileInputStream fileInputStream = (FileInputStream) context.getContentResolver().openInputStream(uri);
            FileOutputStream fileOutputStream = new FileOutputStream(f);
            byte[] buff = new byte[1024];
            int data;
            while ((data = fileInputStream.read(buff)) >0){
                fileOutputStream.write(buff, 0, data);
            }
        } catch (FileNotFoundException e) {
            Log.i(TAG, "copyFileToCache: " + e.getMessage());
        } catch (IOException e) {
            Log.i(TAG, "copyFileToCache: " + e.getMessage());
        }
        return f.getAbsolutePath();
    }
}

