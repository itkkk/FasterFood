package it.uniba.di.ivu.sms16.gruppo3.fasterfood.search_screen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import java.io.File;
import java.util.List;
import it.uniba.di.ivu.sms16.gruppo3.fasterfood.R;

public class AdapterLogosSpinner extends BaseAdapter {
    Context context;
    List<File> logoList;
    LayoutInflater inflter;

    AdapterLogosSpinner(Context context, List<File> logoList){
        this.context = context;
        this.logoList = logoList;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return logoList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.spinner_element_search, null);
        ImageView icon = (ImageView) convertView.findViewById(R.id.imgLogo);
        File logo = logoList.get(position);
        Bitmap logoBitmap = BitmapFactory.decodeFile(logo.getAbsolutePath());
        icon.setImageBitmap(logoBitmap);

        return convertView;
    }
}
