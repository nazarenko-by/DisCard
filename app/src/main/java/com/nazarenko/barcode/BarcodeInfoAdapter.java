package com.nazarenko.barcode;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class BarcodeInfoAdapter extends ArrayAdapter<BarcodeInfo> {
    private LayoutInflater inflater;
    private int layout;
    private List<BarcodeInfo> barcodeInfos;

    public BarcodeInfoAdapter(Context context, int resource, List<BarcodeInfo> barcodeInfos) {
        super(context, resource, barcodeInfos);
        this.barcodeInfos = barcodeInfos;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(this.layout, parent, false);

        TextView barcodeName = view.findViewById(R.id.barcodeName);
        ImageView barcodeView = view.findViewById(R.id.barcodeView);

        BarcodeInfo barcodeInfo = barcodeInfos.get(position);
        barcodeName.setText(barcodeInfo.getNameCard());
        Bitmap bitmap;
        AsyncBarcodeGenerator asyncQRGenerator = new AsyncBarcodeGenerator();
        asyncQRGenerator.setType(barcodeInfo.getType());
        bitmap = asyncQRGenerator.createBarcode(barcodeInfo.getImage());
        barcodeView.setImageBitmap(bitmap);

        return view;
    }
}
