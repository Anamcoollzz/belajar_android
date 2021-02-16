package anamapp.pro.belajar;

import anamapp.pro.belajar.helpers.MyActivity;
import anamapp.pro.belajar.recyclerview.KawalCoronaActivity;
import anamapp.pro.belajar.recyclerview.StaticRecyclerViewActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RecyclerViewActivity extends AppCompatActivity implements MyActivity {

    private Button staticButton, kawalCoronaButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view);
        initVar();
        initView();
        initAction();
    }

    @Override
    public void initView() {
        staticButton = findViewById(R.id.static_button);
        kawalCoronaButton = findViewById(R.id.kawal_corona_api_button);
    }

    @Override
    public void initAction() {
        staticButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecyclerViewActivity.this, StaticRecyclerViewActivity.class);
                startActivity(intent);
            }
        });
        kawalCoronaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecyclerViewActivity.this, KawalCoronaActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initVar() {

    }
}
