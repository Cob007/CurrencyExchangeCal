package android.michealcob.calculator.ui.adapter;


import android.content.Context;
import android.michealcob.calculator.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder> {


    ItemClickListener itemClickListener;
    List<String> currencyName;
    public CurrencyAdapter(){}

    public CurrencyAdapter(List<String> _lst, ItemClickListener _item){
        this.currencyName = _lst;
        this.itemClickListener = _item;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.currency, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        ViewHolder viewHolder = holder;
        viewHolder.mTitle.setText(currencyName.get(i));
        final int position = i;
        viewHolder.mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "make a long press!", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.mTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemClickListener.onItemClicked(position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        if(null == currencyName) return 0;
        return this.currencyName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.country_name);

        }
    }

    public interface ItemClickListener{
        void onItemClicked(int position);
    }

}

