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

import net.sourceforge.zbar.Symbol;

import java.util.Hashtable;

public class AsyncBarcodeGenerator {

    private static final int WIDTH = 400;
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
        if (type == Symbol.EAN8){
            try {
                matrix = new EAN8Writer().encode(
                        params,
                        BarcodeFormat.EAN_8,
                        WIDTH, HEIGHT, hints);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }else if(type == Symbol.EAN13) {
            try {
                matrix = new EAN13Writer().encode(
                        params,
                        BarcodeFormat.EAN_13,
                        WIDTH, HEIGHT, hints);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        } else if(type == Symbol.CODE128){
            try {
                matrix = new Code128Writer().encode(
                        params,
                        BarcodeFormat.CODE_128,
                        WIDTH, HEIGHT, hints);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }else if(type == Symbol.CODE39){
            try {
                matrix = new Code39Writer().encode(
                        params,
                        BarcodeFormat.CODE_39,
                        WIDTH, HEIGHT, hints);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        } else if(type == Symbol.CODE93){
            try {
                matrix = new Code39Writer().encode(
                        params,
                        BarcodeFormat.CODE_93,
                        WIDTH, HEIGHT, hints);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }else if(type == Symbol.CODABAR){
            try {
                matrix = new Code39Writer().encode(
                        params,
                        BarcodeFormat.CODABAR,
                        WIDTH, HEIGHT, hints);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }else if(type == Symbol.PDF417){
            try {
                matrix = new Code39Writer().encode(
                        params,
                        BarcodeFormat.PDF_417,
                        WIDTH, HEIGHT, hints);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }else if(type == Symbol.UPCA){
            try {
                matrix = new Code39Writer().encode(
                        params,
                        BarcodeFormat.UPC_A,
                        WIDTH, HEIGHT, hints);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }else if(type == Symbol.UPCE){
            try {
                matrix = new Code39Writer().encode(
                        params,
                        BarcodeFormat.UPC_E,
                        WIDTH, HEIGHT, hints);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }else {
            bitmap = null;
            return bitmap;
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
