package com.nazarenko.barcode;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    private ListView listView;
    private Intent barcode;
    private BarcodeInfoAdapter adapter;
    private List<BarcodeInfo> barcodeInfoList = new ArrayList<>();;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        }
       barcode = new Intent(this, BarcodeActivity.class);
        final FloatingActionButton fab = findViewById(R.id.fab);
        listView = findViewById(R.id.listView);
        final Intent viewIntent = new Intent(this, BarcodView.class);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                final CharSequence[] options = { getResources().getString(R.string.delete),
                        getResources().getString(R.string.Cancel) };
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.delete_message);
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (options[item].equals(getResources().getString(R.string.delete))) {
                            db.delete(DatabaseHelper.TABLE, DatabaseHelper.COLUMN_BC + " = "
                                    + "\'" + adapter.getItem(position).getImage() +"\'" , null);
                            setInitialData();
                            adapter = new BarcodeInfoAdapter(getApplicationContext(), R.layout.list_item, barcodeInfoList);
                            listView.setAdapter(adapter);
                        } else if (options[item].equals(getResources().getString(R.string.Cancel))) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                viewIntent.putExtra("IMAGE", adapter.getItem(position).getImage());
                viewIntent.putExtra("TYPE", adapter.getItem(position).getType());
                startActivity(viewIntent);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle(R.string.nameTittle);
                alertDialog.setMessage(R.string.nameMessage);
                final EditText input = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                alertDialog.setPositiveButton(getResources().getString(R.string.enter),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                barcode.putExtra("BACODENAME", input.getText().toString());
                                startActivity(barcode);
                            }
                        });
                alertDialog.setNegativeButton(getResources().getString(R.string.Cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();

        setInitialData();
        if(barcodeInfoList != null){
            adapter = new BarcodeInfoAdapter(this, R.layout.list_item, barcodeInfoList);
            listView.setAdapter(adapter);
            barcodeInfoList = new ArrayList<>();
        }
    }

    private void setInitialData() {
        db = databaseHelper.getReadableDatabase();
        Cursor userCursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE + ";", null);
        if (userCursor.moveToFirst()) {
            for (int i = 0; i < userCursor.getCount(); i++) {
                barcodeInfoList.add(new BarcodeInfo (userCursor.getString(1),userCursor.getString(2),
                        userCursor.getInt(3)));
                userCursor.moveToNext();
            }
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {} else {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.camera_error, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();
            }

        }
    }

    public void onBackPressed() {
        exitDialog();
    }

    private void exitDialog(){

        AlertDialog.Builder exitDialog = new AlertDialog.Builder(
                MainActivity.this);
        exitDialog.setTitle(R.string.exitDialog_tittle);
        exitDialog.setPositiveButton(R.string.exitDialog_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        exitDialog.setNegativeButton(R.string.exitDialog_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        exitDialog.show();
    }

}
