package com.mrleo.satimer;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionViewHolder>{
    private ArrayList<Period> periods;
    private int selectedItem = -1;
    private SectionViewHolder sectionViewHolder;
    boolean enabled;

    public SectionAdapter(ArrayList<Period> periods) {
        this.periods = periods;
    }

    @Override
    public SectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section, parent, false);
        sectionViewHolder = new SectionViewHolder(view);
        return sectionViewHolder;
    }

    @Override
    public void onBindViewHolder(SectionViewHolder holder, int position) {
        holder.sectionName.setText(periods.get(position).getSession());
        holder.sectionMinutes.setText(periods.get(position).getMinutes() + " minutes");

        if(selectedItem == position){
            holder.cardView.setBackgroundColor(Color.parseColor("#888888"));
        }
        else{
            holder.cardView.setBackgroundColor(Color.parseColor("#EEEEEE"));
        }
    }

    @Override
    public int getItemCount() {
        return periods.size();
    }

    public int getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(int selectedItem) {

        notifyItemChanged(selectedItem);
        this.selectedItem = selectedItem;
        notifyItemChanged(selectedItem);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public class SectionViewHolder  extends ViewHolder{
        CardView cardView;
        TextView sectionName;
        TextView sectionMinutes;

        public SectionViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_view);
            sectionName = itemView.findViewById(R.id.section_name);
            sectionMinutes = itemView.findViewById(R.id.section_minutes);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(enabled) {
                        notifyItemChanged(selectedItem);
                        selectedItem = getLayoutPosition();
                        notifyItemChanged(selectedItem);
                    }
                }
            });
        }
    }
}
