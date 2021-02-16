package anamapp.pro.belajar.maps;

import anamapp.pro.belajar.R;
import anamapp.pro.belajar.helpers.MyActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MapMenuActivity extends AppCompatActivity implements MyActivity {


    //    init view
    Activity activity;
    Button placeAutoCompleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_menu);
        initVar();
        initView();
        initAction();
    }

    public void initView() {
        activity = this;
        placeAutoCompleteButton = findViewById(R.id.place_autocomplete_custom_button);
    }

    public void initAction() {
        placeAutoCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, PlaceAutoCompleteCustomActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initVar() {
    }
}
