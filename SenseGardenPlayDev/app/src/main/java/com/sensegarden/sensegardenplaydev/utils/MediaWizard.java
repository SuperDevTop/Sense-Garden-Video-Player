package com.sensegarden.sensegardenplaydev.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import com.sensegarden.sensegardenplaydev.SensePlayApplication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MediaWizard {

    public MediaWizard() {
    }

    public void loadFromGallery(Activity activity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                Constants.RequestCodes.IMAGE_UPLOAD);
    }

    public byte[] baosFromUri(Uri filePath) {
        Bitmap image = null;
        try {
            image = MediaStore.Images.Media.getBitmap(SensePlayApplication.instance.getContentResolver(), filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int imageRotation = getImageRotation(filePath.getPath());
        if (imageRotation != 0) image = getBitmapRotatedByDegree(image, imageRotation);

        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        return baos.toByteArray();
    }

    private int getImageRotation(String path) {
        ExifInterface exif = null;
        int exifRotation = 0;

        try {
            exif = new ExifInterface(path);
            exifRotation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (exif == null) return 0;

        return exifToDegrees(exifRotation);
    }

    private int exifToDegrees(int rotation) {
        if (rotation == ExifInterface.ORIENTATION_ROTATE_90) return 90;
        else if (rotation == ExifInterface.ORIENTATION_ROTATE_180) return 180;
        else if (rotation == ExifInterface.ORIENTATION_ROTATE_270) return 270;
        return 0;
    }

    private Bitmap getBitmapRotatedByDegree(Bitmap bitmap, float rotationDegree) {
        Matrix matrix = new Matrix();
        matrix.preRotate(rotationDegree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
