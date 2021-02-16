package anamapp.pro.belajar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import anamapp.pro.belajar.R;
import anamapp.pro.belajar.helpers.Helper;
import anamapp.pro.belajar.models.CoronaProvinsiModel;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CoronaProvinsiAdapter extends RecyclerView.Adapter<CoronaProvinsiAdapter.MyViewHolder> implements Filterable {

    private ArrayList<CoronaProvinsiModel> provinsiModelArrayList, semuaData;
    private Context context;

    public CoronaProvinsiAdapter(Context context, ArrayList<CoronaProvinsiModel> provinsiModelArrayList) {
        this.context = context;
        this.provinsiModelArrayList = provinsiModelArrayList;
        semuaData = (ArrayList<CoronaProvinsiModel>) provinsiModelArrayList.clone();
    }

    public void setData(ArrayList<CoronaProvinsiModel> coronaProvinsiModelArrayList){
        this.provinsiModelArrayList = coronaProvinsiModelArrayList;
        this.semuaData = (ArrayList<CoronaProvinsiModel>) coronaProvinsiModelArrayList.clone();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_korona_provinsi, parent, false);
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
        CoronaProvinsiModel provinsiModel = provinsiModelArrayList.get(position);
        holder.provinsiTextView.setText(provinsiModel.getProvinsi());
        holder.positifTextView.setText(provinsiModel.getKasusPosi()+"");
        holder.sembuhTextView.setText(provinsiModel.getKasusSemb()+"");
        holder.meninggalTextView.setText(provinsiModel.getKasusMeni()+"");
    }

    @Override
    public int getItemCount() {
        return provinsiModelArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                ArrayList<CoronaProvinsiModel> arrayList = new ArrayList<>();
                if(charSequence.length() == 0 || charSequence == null){
                    arrayList = semuaData;
                }else {
                    for (int i = 0; i < semuaData.size(); i++) {
                        CoronaProvinsiModel provinsiModel = semuaData.get(i);
                        if (provinsiModel.contains(charSequence.toString())) {
                            arrayList.add(provinsiModel);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = arrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                provinsiModelArrayList = (ArrayList<CoronaProvinsiModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView provinsiTextView, positifTextView, sembuhTextView, meninggalTextView;
        private View parentView;
        private CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parentView = itemView;
            cardView = itemView.findViewById(R.id.card_view);
            provinsiTextView = itemView.findViewById(R.id.provinsi_text);
            positifTextView = itemView.findViewById(R.id.positif_text);
            sembuhTextView = itemView.findViewById(R.id.sembuh_text);
            meninggalTextView = itemView.findViewById(R.id.meninggal_text);
        }
    }
}
