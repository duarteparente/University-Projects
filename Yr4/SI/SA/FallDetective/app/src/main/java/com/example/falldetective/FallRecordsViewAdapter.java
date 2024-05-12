package com.example.falldetective;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FallRecordsViewAdapter extends RecyclerView.Adapter<FallRecordsViewAdapter.ViewHolder>{

    private final ContactListViewInterface RecyclerInterface;
    Context context;
    List<FallRecordModel> falls;

    public FallRecordsViewAdapter(ContactListViewInterface recyclerInterface, Context context, List<FallRecordModel> falls) {
        RecyclerInterface = recyclerInterface;
        this.context = context;
        this.falls = falls;
    }

    @NonNull
    @Override
    public FallRecordsViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fall_records_view_row, parent, false);

        return new FallRecordsViewAdapter.ViewHolder(view, this.RecyclerInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull FallRecordsViewAdapter.ViewHolder holder, int position) {
        holder.date.setText(falls.get(position).date);
        String location = falls.get(position).latitude + ", " + falls.get(position).longitude;
        if (falls.get(position).latitude == -1){
            location = "---";
        }
        holder.location.setText(location);
        if (falls.get(position).positive){
            holder.confirm.setImageResource(R.drawable.baseline_check_24);
        }
        else{
            holder.confirm.setImageResource(R.drawable.baseline_close_24);
        }

    }

    public void setItems(List<FallRecordModel> contacts) {
        this.falls = contacts;
    }


    @Override
    public int getItemCount() {
        return falls.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView date, location;
        ImageView confirm;

        public ViewHolder(@NonNull View itemView, ContactListViewInterface RecyclerInterface) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            location = itemView.findViewById(R.id.location);
            confirm = itemView.findViewById(R.id.confirm);

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
