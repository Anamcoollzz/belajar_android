package anamapp.pro.belajar.crud;

import anamapp.pro.belajar.R;
import anamapp.pro.belajar.helpers.DaoHandler;
import anamapp.pro.belajar.models.DaoSession;
import anamapp.pro.belajar.models.TblPengeluaran;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class CreateActivity extends AppCompatActivity {

    @BindView(R.id.etPembelian)
    EditText etPembelian;
    @BindView(R.id.etNominal)
    EditText etNominal;
    @BindView(R.id.btnSimpan)
    Button btnSimpan;

    private Unbinder unbinder;
    private DaoSession daoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Buat pengeluaran");

        unbinder = ButterKnife.bind(this);
        daoSession = DaoHandler.getInstance(this);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pembelian = etPembelian.getText().toString();
                String nominal = etNominal.getText().toString();

                if (pembelian.isEmpty() || nominal.isEmpty()) {
                    Toast.makeText(CreateActivity.this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    TblPengeluaran tblPengeluaran = new TblPengeluaran();
                    tblPengeluaran.setPengeluaran(pembelian);
                    tblPengeluaran.setNominal(Integer.parseInt(nominal));
                    daoSession.getTblPengeluaranDao().insert(tblPengeluaran);

                    Toast.makeText(CreateActivity.this, "Berhasil menginput data",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreateActivity.this, DataActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}