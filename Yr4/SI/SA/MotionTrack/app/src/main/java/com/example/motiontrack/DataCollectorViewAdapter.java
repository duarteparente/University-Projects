package com.example.motiontrack;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DataCollectorViewAdapter extends RecyclerView.Adapter<DataCollectorViewAdapter.ViewHolder> {

    private final DataCollectorViewInterface RecyclerInterface;
    Context context;
    ArrayList<ActivityTypeModel> activities;

    public DataCollectorViewAdapter(Context context, ArrayList<ActivityTypeModel> activities, DataCollectorViewInterface RecyclerInterface){
        this.context = context;
        this.activities = activities;
        this.RecyclerInterface = RecyclerInterface;
    }

    @NonNull
    @Override
    public DataCollectorViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activities_recycler_row, parent, false);

        return new ViewHolder(view, this.RecyclerInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull DataCollectorViewAdapter.ViewHolder holder, int position) {
        holder.description.setText(this.activities.get(position).description);
        holder.image.setImageResource(this.activities.get(position).image);

    }

    @Override
    public int getItemCount() {
        return this.activities.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView description;

        public ViewHolder(@NonNull View itemView, DataCollectorViewInterface RecyclerInterface) {
            super(itemView);

            image = itemView.findViewById(R.id.imageViewRow);
            description = itemView.findViewById(R.id.textViewRow);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (RecyclerInterface != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            RecyclerInterface.onItemClick(pos);
                        }
                    }

                }
            });
        }
    }
}
