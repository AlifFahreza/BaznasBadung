package com.baznas.badung;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Adapter extends BaseAdapter {
        private Activity activity;
        private LayoutInflater inflater;
        private List<Data> item;
        public static String id;
        public static String nama;

        public Adapter(Activity activity, List<Data> item) {
            this.activity = activity;
            this.item = item;
        }

        @Override
        public int getCount() {
            return item.size();
        }

        @Override
        public Object getItem(int location) {
            return item.get(location);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (inflater == null)
                inflater = (LayoutInflater) activity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null)
                convertView = inflater.inflate(R.layout.list_spinner, null);

            TextView spinnerItem = (TextView) convertView.findViewById(R.id.Lspinner);

            Data data;
            data = item.get(position);


            spinnerItem.setText(data.getNama_zis());
            id = data.getId_zis();
            nama = data.getNama_zis();
            return convertView;
        }
    }
