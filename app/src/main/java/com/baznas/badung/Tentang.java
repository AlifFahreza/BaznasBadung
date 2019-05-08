package com.baznas.badung;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import pl.droidsonroids.gif.GifImageView;

public class Tentang extends AppCompatActivity {

    private TextView nama, bank, norek, deskripsi;
    private String tag_json_obj = "json_obj_req";
    private ImageView back;
    private LinearLayout linearLayout;
    private String deskrip, nama_bank, atas_nama, no_rekening, JSON_STRING;
    private GifImageView progressbar;

    @Override
    public void onBackPressed() {
        final Intent intent = new Intent(Tentang.this, Home.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang);

        nama = (TextView) findViewById(R.id.nama);
        linearLayout = (LinearLayout) findViewById(R.id.linear);
        bank = (TextView) findViewById(R.id.bank);
        progressbar = findViewById(R.id.progressBar);
        norek = (TextView) findViewById(R.id.norek);
        deskripsi = (TextView) findViewById(R.id.deskripsi);
        back = (ImageView) findViewById(R.id.backInfaq);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(Tentang.this, Home.class);
                startActivity(intent);
                finish();
            }
        });


        progressbar.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.INVISIBLE);
        getJSON();
    }

    private void showData() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                deskrip = jo.getString(konfigurasi.TAG_DESKRIPSI);
                nama_bank = jo.getString(konfigurasi.TAG_BANK);
                no_rekening = jo.getString(konfigurasi.TAG_NOREK);
                atas_nama = jo.getString(konfigurasi.TAG_NAMA);

                HashMap<String, String> data = new HashMap<>();
                data.put(konfigurasi.TAG_DESKRIPSI, deskrip);
                data.put(konfigurasi.TAG_BANK, nama_bank);
                data.put(konfigurasi.TAG_NOREK, no_rekening);
                data.put(konfigurasi.TAG_NAMA, atas_nama);

                list.add(data);

                nama.setText(""+atas_nama);
                bank.setText(""+nama_bank);
                norek.setText(""+no_rekening);
                deskripsi.setText(""+deskrip);

                progressbar.setVisibility(View.INVISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                showData();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(konfigurasi.URL_GET_ABOUT);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
}
