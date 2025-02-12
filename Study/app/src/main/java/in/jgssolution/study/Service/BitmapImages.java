package in.jgssolution.study.Service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class BitmapImages {
    public static Bitmap decodeBase64(String encodedImage) {
        if (encodedImage == null || encodedImage.isEmpty()) {
            return null;
        }
        byte[] decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

}
