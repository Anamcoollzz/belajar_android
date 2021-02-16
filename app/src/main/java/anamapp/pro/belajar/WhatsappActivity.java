package anamapp.pro.belajar;

import anamapp.pro.belajar.services.ApiCall;
import anamapp.pro.belajar.services.SMSService;
import anamapp.pro.belajar.services.WhatAppAccessibilityService;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WhatsappActivity extends AppCompatActivity {

    private EditText inputPesan;
    private EditText inputDelay;
    private Button btnKirim;
    private Button btnKirim2;
    private Button btnTesting;
    private Button btnAksesibilitas;
    private Button btnPanduan;
    private TextView inputJumlahPesan;
    private TextView txtPastikanAksesibilitas;

    private JSONArray whatsappDatas;

    Thread threadWhatsappApi;

    private int jumlahPesan = 1;
    private int delay = 7000;
    private boolean IS_WA_ORIGINAL = false;
    //    private boolean IS_WA_ORIGINAL = true;
//    private String waEndpoint = "https://aksiberbagi.com/testing/whatsapp";
    private String waEndpoint = "https://aksiberbagi.com/whatsapp/antrian/2";
    private String waEndpointTesting = "https://aksiberbagi.com/whatsapp/antrian/testing";
    private String waEndpointDefault = "https://aksiberbagi.com/whatsapp/antrian/2";

    private boolean stoped = true;
    private boolean isTesting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Log.d("ANDROIDVERSION", String.valueOf(Build.VERSION.SDK_INT));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsapp);
//        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);
        inputPesan = findViewById(R.id.inputPesan);
        inputDelay = findViewById(R.id.inputDelay);
        inputJumlahPesan = findViewById(R.id.inputJumlahPesan);
        btnKirim = findViewById(R.id.btnKirim);
        btnKirim2 = findViewById(R.id.btnKirim2);
        btnAksesibilitas = findViewById(R.id.btnAksesibilitas);
        btnPanduan = findViewById(R.id.btnPanduan);
        txtPastikanAksesibilitas = findViewById(R.id.pastikanAksesibilitas);
        btnTesting = findViewById(R.id.btnTesting);
        inputDelay.setText("7000");
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();

//        if (!isAccessibilityOn(getApplicationContext())) {
//bukaMenuAksesibilitas();
//        }
        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waEndpoint = waEndpointDefault;
                isTesting = false;
                if (stoped) {
                    startThreadWhatsappApi();
                } else {
                    stopThreadWhatsappApi();
                }
            }
        });

        btnTesting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waEndpoint = waEndpointTesting;
                isTesting = true;
                if (stoped) {
                    startThreadWhatsappApi();
                } else {
                    stopThreadWhatsappApi();
                }
            }
        });

        btnKirim2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waEndpoint = waEndpointDefault;
                isTesting = false;
                if (stoped) {
                    startThreadWhatsappApi();
                } else {
                    stopThreadWhatsappApi();
                }
            }
        });
        btnKirim2.setVisibility(View.GONE);

//        if (IS_WA_ORIGINAL) {
//            btnKirim.setVisibility(View.VISIBLE);
//            btnKirim2.setVisibility(View.GONE);
//        } else {
//            btnKirim.setVisibility(View.GONE);
//            btnKirim2.setVisibility(View.VISIBLE);
//        }

        btnAksesibilitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bukaMenuAksesibilitas();
            }
        });
        btnPanduan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://bit.ly/2ZTQhkK"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
//        this.getWhatsapp();
//        this.startThreadWhatsappApi();
    }

    private void bukaMenuAksesibilitas() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private boolean isAccessibilityOn(Context context) {
        int accessibilityEnabled = 0;
        final String service = context.getPackageName() + "/" + WhatAppAccessibilityService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getApplicationContext().getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException ignored) {
        }

        TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                colonSplitter.setString(settingValue);
                while (colonSplitter.hasNext()) {
                    String accessibilityService = colonSplitter.next();

                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private void getWhatsapp() {
        Log.i("whatsapp api", "request to server");
        ApiCall.getWhatsapp(waEndpoint).getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    whatsappDatas = response.getJSONArray("data");
                    List<String> numbers = new ArrayList<>();
//                    jumlahPesan = whatsappDatas.length();

                    for (int i = 0; i < whatsappDatas.length(); i++) {
                        numbers.add(whatsappDatas.getJSONObject(i).getString("noWa"));
                        String[] noWa = {whatsappDatas.getJSONObject(i).getString("noWa")};
                        String pesan = whatsappDatas.getJSONObject(i).getString("pesan");
                        SMSService.startActionWHATSAPP(getApplicationContext(), pesan, "1", noWa, false, IS_WA_ORIGINAL);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            inputJumlahPesan.setVisibility(View.VISIBLE);
                            inputJumlahPesan.setText("Jumlah Pesan Yang Diambil: " + whatsappDatas.length());

//                            wa business
//                            Intent sendIntent = new Intent("android.intent.action.MAIN");
//                            sendIntent.putExtra("jid", "6285322778935" + "@s.whatsapp.net");
//                            sendIntent.putExtra(Intent.EXTRA_TEXT, "ANAM");
//                            sendIntent.setAction(Intent.ACTION_SEND);
//                            sendIntent.setPackage("com.whatsapp.w4b");
//                            sendIntent.setType("text/plain");
//                            startActivity(sendIntent);
                        }
                    });

                    Log.i("whatsapp api", Objects.requireNonNull(response.toString()));
                } catch (JSONException e) {
                    Log.e("whatsapp api", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onError(ANError anError) {

                Log.e("whatsapp api", Objects.requireNonNull(anError.getErrorBody()));
            }
        });
    }

    private void startedEvent() {
        stoped = false;
        if (isTesting) {
            btnTesting.setText("Berhenti");
            btnTesting.setBackgroundResource(R.color.colorDanger);
            btnKirim.setVisibility(View.GONE);
        } else {
            btnKirim.setText("Berhenti");
            btnKirim.setBackgroundResource(R.color.colorDanger);
            btnTesting.setVisibility(View.GONE);
        }

        btnAksesibilitas.setVisibility(View.GONE);
        btnPanduan.setVisibility(View.GONE);
        txtPastikanAksesibilitas.setVisibility(View.GONE);
    }

    private void stopedEvent() {
        stoped = true;
        if (isTesting) {
            btnTesting.setText("Testing");
            btnTesting.setBackgroundResource(R.color.colorGrey);
            btnKirim.setVisibility(View.VISIBLE);
        } else {
            btnKirim.setText("Mulai");
            btnKirim.setBackgroundResource(R.color.colorGrey);
            btnTesting.setVisibility(View.VISIBLE);
        }

        btnAksesibilitas.setVisibility(View.VISIBLE);
        btnPanduan.setVisibility(View.VISIBLE);
        txtPastikanAksesibilitas.setVisibility(View.VISIBLE);
    }

    private void startThreadWhatsappApi() {
        String delayString = inputDelay.getText().toString();
        if (delayString.trim().isEmpty()) {
            Toast.makeText(this, "Masukkan delay dulu :)", Toast.LENGTH_SHORT).show();
            return;
        }
        int localdelay;
        try {
            localdelay = Integer.parseInt(delayString);
        } catch (Exception e) {
            Toast.makeText(this, "Masukkan delay dalam bentuk angka :)", Toast.LENGTH_SHORT).show();
            return;
        }
        if (localdelay < 7000) {
            Toast.makeText(this, "Delay minimal 7000ms :)", Toast.LENGTH_SHORT).show();
            return;
        }
        this.delay = localdelay;

        this.startedEvent();
//        if (this.threadWhatsappApi == null)
        this.threadWhatsappApi = new Thread() {
            @Override
            public void run() {
                try {
                    while (!this.isInterrupted()) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                            }
//                        });
                        getWhatsapp();
                        Log.i("jumlahPesan", String.valueOf(jumlahPesan));
                        sleep(delay);
//                        sleep(jumlahPesan * 8000);
//                        sleep(3000);
//                        stopThreadWhatsappApi();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Toast.makeText(WhatsappActivity.this, "Thread WA API dihidupkan ", Toast.LENGTH_SHORT).show();
        Log.e("kunnam", "anam");
        this.threadWhatsappApi.start();
    }

    private void stopThreadWhatsappApi() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(WhatsappActivity.this, "Thread WA API dimatikan ", Toast.LENGTH_SHORT).show();
                inputJumlahPesan.setVisibility(View.GONE);
            }
        });
        this.threadWhatsappApi.interrupt();
        this.threadWhatsappApi = null;
        this.stopedEvent();
    }


}
