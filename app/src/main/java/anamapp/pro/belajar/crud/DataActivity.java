package anamapp.pro.belajar.crud;

import anamapp.pro.belajar.R;
import anamapp.pro.belajar.helpers.DaoHandler;
import anamapp.pro.belajar.helpers.Helper;
import anamapp.pro.belajar.models.DaoSession;
import anamapp.pro.belajar.models.TblPengeluaran;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import anamapp.pro.belajar.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
public class DataActivity extends AppCompatActivity
        implements PengeluaranAdapter.PengeluaranAdapterCallback,
        EditDialogFragment.EditDialogListener {

    @BindView(R.id.rvNote)
    RecyclerView rvNote;
    @BindView(R.id.fabAdd)
    FloatingActionButton fabAdd;
    @BindView(R.id.tvTotal)
    TextView tvTotal;

    private DaoSession daoSession;
    private PengeluaranAdapter pengeluaranAdapter;

    private List<TblPengeluaran> tblPengeluaranList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        ButterKnife.bind(this);
        daoSession = DaoHandler.getInstance(this);

        /*
        Fungi untuk READ data dari database. Contoh disini memanggil data yang berada dalam
        tabel TblPengeluaran.
         */
        tblPengeluaranList = daoSession.getTblPengeluaranDao().queryBuilder().list();
        pengeluaranAdapter = new PengeluaranAdapter(tblPengeluaranList, this);
        rvNote.setLayoutManager(new LinearLayoutManager(this));
        rvNote.setItemAnimator(new DefaultItemAnimator());
        rvNote.setAdapter(pengeluaranAdapter);
        pengeluaranAdapter.notifyDataSetChanged();

        tvTotal.setText(Helper.convertRupiah(getTotal()));

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DataActivity.this, CreateActivity.class));
            }
        });
    }

    /*
    Fungsi untuk mengirim data dari adapter ke edit dialog.
    Disini memanggil EditDialogFragment dengan parameter id, pembelian, dan nominal.
     */
    @Override
    public void onLongClick(int position) {
        long id = tblPengeluaranList.get(position).getIdTblPengeluaran();
        String pembelian = tblPengeluaranList.get(position).getPengeluaran();
        int nominal = tblPengeluaranList.get(position).getNominal();

        FragmentManager fm = getSupportFragmentManager();
        EditDialogFragment editDialogFragment = EditDialogFragment.newInstance(id, pembelian, nominal);
        editDialogFragment.show(fm, "dialog_edit");
    }

    /*
    Fungsi delete data. Sebelum menghapus data ada semacam popup terlebih dahulu agar meyakinkan user.
     */
    @Override
    public void onDelete(int position) {
        String name = tblPengeluaranList.get(position).getPengeluaran();
        showDialogDelete(position, name);
    }

    /*
    Fungsi untuk men-totalkan semua nominal yang ada didalam tabel TblPengeluaran.
     */
    private int getTotal(){
        int total = 0;
        for (int i = 0; i < tblPengeluaranList.size(); i++){
            int nominal = tblPengeluaranList.get(i).getNominal();
            total = total + nominal;
        }
        return total;
    }

    /*
    Fungsi untuk memanggil Alert Dialog. Alert dialog ini berfungsi untuk meyakinkan user kembali
    apakah datanya ingin dihapus atau tidak.
     */
    private void showDialogDelete(final int position, String name){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(DataActivity.this);
        builder1.setMessage("Yakin untuk menghapus item "+ name + " ?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ya",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        /*
                        Fungsi delete suatu data bedasarkan idnya.
                         */
                        long idTbl = tblPengeluaranList.get(position).getIdTblPengeluaran();
                        daoSession.getTblPengeluaranDao().deleteByKey(idTbl);

                        tblPengeluaranList.remove(position);
                        pengeluaranAdapter.notifyItemRemoved(position);
                        pengeluaranAdapter.notifyItemRangeChanged(position, tblPengeluaranList.size());

                        tvTotal.setText(Helper.convertRupiah(getTotal()));

                        dialog.dismiss();
                    }
                });

        builder1.setNegativeButton(
                "Tidak",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    /*
    Fungsi ini untuk menerima data yang dikirimkan dari EditDialogFragment ke HomeActivity.
    Data yang dikirimkan dari EditDialogFragment ini ada id, pembelian, dan nominal. Lalu
    setelah mendapatkan datanya panggil fungsi update dari Greendao.
     */
    @Override
    public void requestUpdate(long id, String pembelian, int nominal) {
        TblPengeluaran tblPengeluaran = daoSession.getTblPengeluaranDao().load(id);
        tblPengeluaran.setPengeluaran(pembelian);
        tblPengeluaran.setNominal(nominal);
        daoSession.getTblPengeluaranDao().update(tblPengeluaran);

        pengeluaranAdapter.notifyDataSetChanged();
        tvTotal.setText(Helper.convertRupiah(getTotal()));
    }
}