package com.baznas.badung;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ZakatMal extends AppCompatActivity {

    private TextView idScan, teksBukti;
    private ImageView backZakatMal, buktiImage;
    private EditText namaPengirim, notelepon, bankpengirim, norekening, pemilikrekening, jumlah;
    private Button btn, kirimZakatMaal;
    private String tag_json_obj = "json_obj_req";
    private IntentIntegrator btnScan;
    private String id_zis, nama_zis, id, ConvertImage;
    private static final int PICK_IMAGE = 100;
    public String IDSCANNER = "0";
    private File f;
    private Data item;
    private Bitmap imageUri;
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] byteArray;
    Spinner spinner_zis;
    ProgressDialog pDialog;
    Adapter adapter;
    List<Data> listSpinner = new ArrayList<Data>();
    private static final String TAG = ZakatMal.class.getSimpleName();

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ZakatMal.this, Home.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zakat_mal);

        kirimZakatMaal = findViewById(R.id.kirimzakatmaal);
        namaPengirim = findViewById(R.id.namaPengirim);
        notelepon = findViewById(R.id.notelepon);
        bankpengirim = findViewById(R.id.bankpengirim);
        norekening = findViewById(R.id.norekening);
        teksBukti = findViewById(R.id.teksbukti);
        pemilikrekening = findViewById(R.id.pemilikrekening);
        jumlah = findViewById(R.id.jumlah);
        backZakatMal = findViewById(R.id.backInfaq);
        buktiImage = findViewById(R.id.buktiImage);
        byteArrayOutputStream = new ByteArrayOutputStream();
        idScan = findViewById(R.id.idScan);
        btnScan = new IntentIntegrator(this);
        spinner_zis = findViewById(R.id.spinner_zis);
        adapter = new Adapter(ZakatMal.this, listSpinner);
        spinner_zis.setAdapter(adapter);

        callData();

        backZakatMal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ZakatMal.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

        buktiImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        if (imageUri != null){
            imageUri.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
            byteArray = byteArrayOutputStream.toByteArray();
            ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }else {
            teksBukti.setText("Mohon Lampirkan Bukti Transfer");
            teksBukti.setTextColor(Color.parseColor("#FFFF0004"));
        }

        kirimZakatMaal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String nama_pengirim = namaPengirim.getText().toString().trim();
                final String no_telepon = notelepon.getText().toString().trim();
                final String bank_pengirim = bankpengirim.getText().toString().trim();
                final String no_rekening = norekening.getText().toString().trim();
                final String pemilik_rekening = pemilikrekening.getText().toString().trim();
                final String Jumlah = jumlah.getText().toString().trim();

                final AlertDialog.Builder loginDialog = new AlertDialog.Builder(new ContextThemeWrapper(ZakatMal.this, android.R.style.Theme_DeviceDefault_Light_Dialog));
                LayoutInflater factory = LayoutInflater.from(ZakatMal.this);
                final View view = factory.inflate(R.layout.activity_infaq, null);

                if (nama_pengirim.isEmpty()){
                    namaPengirim.setError("Nama Tidak Boleh Kosong");
                }else if (no_telepon.isEmpty()){
                    notelepon.setError("No Telepon Tidak Boleh Kosong");
                }else if (bank_pengirim.isEmpty()){
                    bankpengirim.setError("Bank Tidak Boleh Kosong");
                }else if (no_rekening.isEmpty()){
                    norekening.setError("No Rekening Tidak Boleh Kosong");
                }else if (pemilik_rekening.isEmpty()){
                    pemilikrekening.setError("Pemilik Rekening Tidak Boleh Kosong");
                }else if (Jumlah.isEmpty()){
                    jumlah.setError("Jumlah Tidak Boleh Kosong");
                }else {

                    if (imageUri != null){
                        imageUri.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
                        byteArray = byteArrayOutputStream.toByteArray();
                        ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

                        class KirimZakatMaal extends AsyncTask<Void,Void,String> {

                            ProgressDialog loading;

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                loading = ProgressDialog.show(ZakatMal.this,"Sedang Diproses...","Tunggu...",false,false);
                            }

                            @Override
                            protected void onPostExecute(String s) {
                                super.onPostExecute(s);
                                loading.dismiss();
                                Toast.makeText(ZakatMal.this,s,Toast.LENGTH_LONG).show();
                            }

                            @Override
                            protected String doInBackground(Void... v) {
                                HashMap<String,String> params = new HashMap<>();

                                params.put(konfigurasi.ADD_IDSCAN,IDSCANNER);
                                params.put(konfigurasi.ADD_IDSCAN, item.getId_zis());
                                params.put(konfigurasi.ADD_NAMA_GAMBAR,f.getName());
                                params.put(konfigurasi.ADD_NAMA_PENGIRIM,nama_pengirim);
                                params.put(konfigurasi.ADD_NO_TELEPON,no_telepon);
                                params.put(konfigurasi.ADD_BANK_PENGIRIM,bank_pengirim);
                                params.put(konfigurasi.ADD_PEMILIK_REKENING,pemilik_rekening);
                                params.put(konfigurasi.ADD_NO_REKENING,no_rekening);
                                params.put(konfigurasi.ADD_JUMLAH_MAL,Jumlah);
                                params.put(konfigurasi.ADD_BUKTI_MAL,ConvertImage);

                                RequestHandler rh = new RequestHandler();
                                String res = rh.sendPostRequest(konfigurasi.URL_POST_ZAKAT_MAAL, params);
                                return res;
                            }
                        }

                        KirimZakatMaal ae = new KirimZakatMaal();
                        ae.execute();
                    }else {
                        teksBukti.setText("Mohon Lampirkan Bukti Transfer");
                        teksBukti.setTextColor(Color.parseColor("#FFFF0004"));
                    }
                }

            }
        });
    }

    private void callData() {
        listSpinner.clear();

        pDialog = new ProgressDialog(ZakatMal.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        showDialog();

        // Creating volley request obj
        JsonArrayRequest jArr = new JsonArrayRequest(konfigurasi.URL_GET_SPINNER,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, response.toString());

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);

                                item = new Data();

                                item.setId_zis(obj.getString(konfigurasi.TAG_ID));
                                item.setNama_zis(obj.getString(konfigurasi.TAG_NAMAA));

                                listSpinner.add(item);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();

                        hideDialog();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(ZakatMal.this, error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jArr);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    public void scan(View view) {
        btnScan.initiateScan();
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if (result.getContents() == null){
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_SHORT).show();
            }else {
                try {
                    JSONObject obj = new JSONObject(result.getContents());
                } catch (JSONException e) {
                    e.printStackTrace();
                    id = result.getContents();

                    if (id.contains("_")){
                        String[] items = id.split("_");
                        items[0] = items[0].trim();
                        items[1] = items[1].trim();
                        IDSCANNER = items[0].trim();
                        idScan.setText(items[1].toString());
                    }else {
                        Toast.makeText(this, "Result Not Found", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){

                if (data != null){
                    Uri contentUri = data.getData();

                    try {
                        imageUri = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentUri);
                        String selectedPath = getPath(contentUri);
                        buktiImage.setImageBitmap(imageUri);
                        teksBukti.setVisibility(View.INVISIBLE);
                        f = new File(selectedPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }
    }
}