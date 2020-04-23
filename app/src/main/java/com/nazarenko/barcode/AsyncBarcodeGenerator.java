package com.nazarenko.barcode;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.oned.Code39Writer;
import com.google.zxing.oned.EAN13Writer;
import com.google.zxing.oned.EAN8Writer;

import java.util.Hashtable;

public class AsyncBarcodeGenerator {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 100;
    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFF;

    private int type;

    public void setType(int type){
        this.type = type;
    }

    public Bitmap createBarcode(String params) {
        Bitmap bitmap;
        BitMatrix matrix = null;
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        if (type == 8){
            try {
                matrix = new EAN8Writer().encode(
                        params,
                        BarcodeFormat.EAN_8,
                        WIDTH, HEIGHT, hints);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }else if(type == 13) {
            try {
                matrix = new EAN13Writer().encode(
                        params,
                        BarcodeFormat.EAN_13,
                        WIDTH, HEIGHT, hints);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        } else if(type == 128){
            try {
                matrix = new Code128Writer().encode(
                        params,
                        BarcodeFormat.CODE_128,
                        WIDTH, HEIGHT, hints);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }else if(type == 39){
            try {
                matrix = new Code39Writer().encode(
                        params,
                        BarcodeFormat.CODE_39,
                        WIDTH, HEIGHT, hints);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
            bitmap = matrixToBitmap(matrix);



        return bitmap;
    }

    private Bitmap matrixToBitmap(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setPixel(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }
}
