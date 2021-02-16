package anamapp.pro.belajar.recyclerview;

import anamapp.pro.belajar.R;
import anamapp.pro.belajar.adapter.MahasiswaAdapter;
import anamapp.pro.belajar.helpers.MyActivity;
import anamapp.pro.belajar.models.MahasiwaModel;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class StaticRecyclerViewActivity extends AppCompatActivity implements MyActivity {

    private RecyclerView recyclerView;
    private ArrayList<MahasiwaModel> mahasiwaModelArrayList;
    private Button changeAdapterButton;
    private TextInputEditText filterTextInputEditText;
    private MahasiswaAdapter mahasiswaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_recycler_view);
        initVar();
        initView();
        initAction();
    }

    @Override
    public void initView() {
        recyclerView = findViewById(R.id.recycler_view);
        changeAdapterButton = findViewById(R.id.change_adapter_button);
        filterTextInputEditText = findViewById(R.id.filter_text);
    }

    @Override
    public void initAction() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mahasiswaAdapter = new MahasiswaAdapter(StaticRecyclerViewActivity.this, mahasiwaModelArrayList);
        recyclerView.setAdapter(mahasiswaAdapter);
        changeAdapterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mahasiwaModelArrayList.clear();
                for (int i = 0; i < 20; i++) {
                    mahasiwaModelArrayList.add(new MahasiwaModel("16241010112" + i, "Nama " + i));
                }
                mahasiswaAdapter = new MahasiswaAdapter(StaticRecyclerViewActivity.this, mahasiwaModelArrayList);
                recyclerView.setAdapter(mahasiswaAdapter);
            }
        });
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
                mahasiswaAdapter.getFilter().filter(text);
            }
        });
    }

    @Override
    public void initVar() {
        mahasiwaModelArrayList = new ArrayList<>();
        mahasiwaModelArrayList.add(new MahasiwaModel("162410101128", "Hairul Anam"));
        mahasiwaModelArrayList.add(new MahasiwaModel("162410101138", "Muhammad Yusuf Auliya"));
        mahasiwaModelArrayList.add(new MahasiwaModel("162410101148", "Riski Dae"));
        mahasiwaModelArrayList.add(new MahasiwaModel("162410101158", "Intan Wahyu"));
    }
}
