package anamapp.pro.belajar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import anamapp.pro.belajar.R;
import anamapp.pro.belajar.helpers.Helper;
import anamapp.pro.belajar.models.MahasiwaModel;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.MyViewHolder> implements Filterable {

    private ArrayList<MahasiwaModel> mahasiwaModelArrayList, semuaData;
    private Context context;

    public MahasiswaAdapter(Context context, ArrayList<MahasiwaModel> mahasiwaModelArrayList) {
        this.context = context;
        this.mahasiwaModelArrayList = mahasiwaModelArrayList;
        semuaData = (ArrayList<MahasiwaModel>) mahasiwaModelArrayList.clone();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_mahasiswa, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(position == 0){
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.cardView.getLayoutParams();
            int margin = Helper.dpToPixel(context, 10);
            layoutParams.setMargins(margin, margin, margin, margin);
            holder.cardView.setLayoutParams(layoutParams);
        }
        MahasiwaModel mahasiwaModel = mahasiwaModelArrayList.get(position);
        holder.nimTextView.setText(mahasiwaModel.getNim());
        holder.namaTextView.setText(mahasiwaModel.getNama());
    }

    @Override
    public int getItemCount() {
        return mahasiwaModelArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                ArrayList<MahasiwaModel> arrayList = new ArrayList<>();
                if(charSequence.length() == 0 || charSequence == null){
                    arrayList = semuaData;
                }else {
                    for (int i = 0; i < semuaData.size(); i++) {
                        MahasiwaModel mahasiwaModel = semuaData.get(i);
                        if (mahasiwaModel.contains(charSequence.toString())) {
                            arrayList.add(mahasiwaModel);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = arrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mahasiwaModelArrayList = (ArrayList<MahasiwaModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView nimTextView, namaTextView;
        private View parentView;
        private CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parentView = itemView;
            cardView = itemView.findViewById(R.id.card_view);
            nimTextView = itemView.findViewById(R.id.nim_text);
            namaTextView = itemView.findViewById(R.id.nama_text);
        }
    }
}
