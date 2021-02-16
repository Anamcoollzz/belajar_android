package anamapp.pro.belajar;

import anamapp.pro.belajar.helpers.MyActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FormActivity extends AppCompatActivity implements MyActivity {

    private TextView textView;
    private EditText editText;
    private AutoCompleteTextView autoCompleteTextView;

    private ArrayList<Country> countryArrayList;

    private ArrayAdapter<Country> autocompleteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        initVar();
        initView();
        initAction();
    }

    @Override
    public void initView() {
        textView = findViewById(R.id.textView);
        editText = findViewById(R.id.editText);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
    }

    @Override
    public void initAction() {
        editText.setError("Error");
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textView.setText(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        autoCompleteTextView.setAdapter(autocompleteAdapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                textView.setText(countryArrayList.get(i).getName());
            }
        });
    }

    @Override
    public void initVar() {
        countryArrayList = new ArrayList<>();
        countryArrayList.add(new Country(1, "Indonesia"));
        countryArrayList.add(new Country(2, "Malaysia"));
        autocompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, countryArrayList);
    }

    private class Country {
        private int id;
        private String name;

        public Country(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
