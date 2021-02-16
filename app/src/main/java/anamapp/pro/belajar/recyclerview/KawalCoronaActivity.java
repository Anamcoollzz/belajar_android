package anamapp.pro.belajar.recyclerview;

import anamapp.pro.belajar.R;
import anamapp.pro.belajar.adapter.CoronaProvinsiAdapter;
import anamapp.pro.belajar.helpers.MyActivity;
import anamapp.pro.belajar.helpers.api.ANetworking;
import anamapp.pro.belajar.helpers.api.EndPoint;
import anamapp.pro.belajar.models.CoronaProvinsiModel;
import anamapp.pro.belajar.models.MahasiwaModel;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class KawalCoronaActivity extends AppCompatActivity implements MyActivity {

    private RecyclerView recyclerView;
    private ArrayList<CoronaProvinsiModel> coronaProvinsiModelArrayList;
    private TextInputEditText filterTextInputEditText;
    private SwipeRefreshLayout pullToRefresh;
    private ProgressBar progressBar;
    private CoronaProvinsiAdapter coronaProvinsiAdapter;
    private String TAG = "KawalCoronaActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kawal_corona);
        initVar();
        initView();
        initAction();
    }

    @Override
    public void initView() {
        recyclerView = findViewById(R.id.recycler_view);
        filterTextInputEditText = findViewById(R.id.filter_text);
        progressBar = findViewById(R.id.progressBar);
        pullToRefresh = findViewById(R.id.pullToRefresh);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Kawal Corona Provinsi");
    }

    @Override
    public void initAction() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        coronaProvinsiAdapter = new CoronaProvinsiAdapter(KawalCoronaActivity.this, coronaProvinsiModelArrayList);
        recyclerView.setAdapter(coronaProvinsiAdapter);
        filterTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                coronaProvinsiAdapter.getFilter().filter(text);
            }
        });
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchDataCoronaProvinsi();
            }
        });
    }

    @Override
    public void initVar() {
        coronaProvinsiModelArrayList = new ArrayList<>();
        fetchDataCoronaProvinsi();
    }

    private void fetchDataCoronaProvinsi() {
        ANetworking.get(EndPoint.PROVINSI).getAsJSONArray(new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    coronaProvinsiModelArrayList = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i).getJSONObject("attributes");
                        Log.d(TAG, jsonObject.toString());
                        CoronaProvinsiModel coronaProvinsiModel = new Gson().fromJson(jsonObject.toString(), CoronaProvinsiModel.class);
                        Log.d(TAG, coronaProvinsiModel.getProvinsi());
                        coronaProvinsiModelArrayList.add(coronaProvinsiModel);
                    }
                    coronaProvinsiAdapter.setData(coronaProvinsiModelArrayList);
                    coronaProvinsiAdapter.notifyDataSetChanged();
                    hideLoading();
                    pullToRefresh.setRefreshing(false);
                    Toast.makeText(getApplicationContext(), coronaProvinsiModelArrayList.size()+" data found", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {

            }
        });
    }

    private void hideLoading(){
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

}
