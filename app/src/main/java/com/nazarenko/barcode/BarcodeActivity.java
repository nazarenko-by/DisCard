package com.nazarenko.barcode;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;


public class BarcodeActivity extends Activity {

        private Camera mCamera;
        private CameraPreview mPreview;
        private Handler autoFocusHandler;

        ImageScanner scanner;

        private boolean barcodeScanned = false;
        private boolean previewing = true;
        private String name;

        public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.barcode_activity);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        Toast.makeText(getApplicationContext(), getResources().getString(R.string.camera_massage), Toast.LENGTH_LONG).show();

        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();
        /* Instance barcode scanner */
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
        FrameLayout preview = (FrameLayout)findViewById(R.id.cameraPreview);
        preview.addView(mPreview);
        ImageView v = new ImageView(this);
        v.setScaleType(ImageView.ScaleType.CENTER);
        v.setImageResource(R.drawable.ic_focus);
        preview.addView(v);
        Intent intent = getIntent();
        name = intent.getStringExtra("BACODENAME");

    }

        public void onPause() {
        super.onPause();
        releaseCamera();
    }

        /** A safe way to get an instance of the Camera object. */
        public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e){
        }
        return c;
    }

        private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

        private Runnable doAutoFocus = new Runnable() {
            public void run() {
                if (previewing)
                    mCamera.autoFocus(autoFocusCB);
            }
        };

        PreviewCallback previewCb = new PreviewCallback() {
            public void onPreviewFrame(byte[] data, Camera camera) {
                Camera.Parameters parameters = camera.getParameters();
                Size size = parameters.getPreviewSize();

                Image barcode = new Image(size.width, size.height, "Y800");
                barcode.setData(data);

                int result = scanner.scanImage(barcode);


                if (result != 0) {
                    DatabaseHelper databaseHelper;
                    SQLiteDatabase db;
                    databaseHelper = new DatabaseHelper(getApplicationContext());
                    db = databaseHelper.getReadableDatabase();
                    ContentValues cv = new ContentValues();
                    previewing = false;
                    mCamera.setPreviewCallback(null);
                    mCamera.stopPreview();
                    SymbolSet syms = scanner.getResults();
                    for (Symbol sym : syms) {
                        cv.put(DatabaseHelper.COLUMN_BC, sym.getData());
                        cv.put(DatabaseHelper.COLUMN_NM, name);
                        cv.put(DatabaseHelper.COLUMN_TP, sym.getType());
                        barcodeScanned = true;
                    }
                    db.insert(DatabaseHelper.TABLE, null, cv);
                    db.close();
                    finish();
                }
            }
        };

        AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
            public void onAutoFocus(boolean success, Camera camera) {
                autoFocusHandler.postDelayed(doAutoFocus, 500);
            }
        };
}
