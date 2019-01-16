package com.demoapp.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demoapp.Model.Venue;
import com.demoapp.R;

import java.util.ArrayList;

public class AdapterVenueListActivity extends RecyclerView.Adapter<AdapterVenueListActivity.MyViewHolder> {

    private ArrayList<Venue> venueArrayList;
    public AdapterVenueListActivity(ArrayList<Venue> venueArrayList) {
        this.venueArrayList=venueArrayList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.single_view_row, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        //holder.tv_id.setText(venueArrayList.get(i).getId());
        holder.tv_name.setText(venueArrayList.get(i).getName());

        if (!venueArrayList.get(i).getLocation().getFormattedAddress().isEmpty()) {
            for (int k = 0; k < venueArrayList.get(i).getLocation().getFormattedAddress().size(); k++) {
                holder.tv_address.setText(venueArrayList.get(i).getLocation().getFormattedAddress().get(0));
            }
        }

        holder.tv_distance.setText(venueArrayList.get(i).getLocation().getDistance());

    }

    @Override
    public int getItemCount() {
        return venueArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_id,tv_name,tv_distance,tv_address;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //tv_id=(TextView)itemView.findViewById(R.id.txt_id);
            tv_name=(TextView)itemView.findViewById(R.id.tv_name);
            tv_distance=(TextView)itemView.findViewById(R.id.tv_distance);
            tv_address=(TextView)itemView.findViewById(R.id.tv_address);
        }
    }
}
