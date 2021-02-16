package anamapp.pro.belajar.helpers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

public class PhotoHelper{


    public static Bitmap rotateImage(String path) throws IOException {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        int rotate = 0;
        ExifInterface exif;
        exif = new ExifInterface(path);
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotate = 270;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotate = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotate = 90;
                break;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
    }

    public static Bitmap resizeImage(Bitmap bitmap, int destinationWidth) {
        float scale = (float) destinationWidth / bitmap.getWidth();
        int destinationHeight = Math.round(scale * bitmap.getHeight());
        return Bitmap.createScaledBitmap(bitmap, destinationWidth, destinationHeight, false);
    }

    public static void saveImageToDir(Bitmap bitmap, File dir, String filename){
        File file = new File(dir, filename);
        FileOutputStream fout;
        try {
            fout = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
            fout.flush();
            fout.close();
            bitmap.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getFilename(String suffix){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return "JPEG_" + timeStamp + '_' + suffix + ".jpg";
    }

    public static String getFilename(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return "JPEG_" + timeStamp + ".jpg";
    }

}
