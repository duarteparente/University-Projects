package com.example.falldetective;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ContactListViewAdapter extends RecyclerView.Adapter<ContactListViewAdapter.ViewHolder>{

    private final ContactListViewInterface RecyclerInterface;
    Context context;
    List<ContactInfoModel> contacts;

    public ContactListViewAdapter(ContactListViewInterface recyclerInterface, Context context, List<ContactInfoModel> contacts) {
        RecyclerInterface = recyclerInterface;
        this.context = context;
        this.contacts = contacts;
    }


    @NonNull
    @Override
    public ContactListViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.contacts_view_row, parent, false);

        return new ViewHolder(view, this.RecyclerInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactListViewAdapter.ViewHolder holder, int position) {
        holder.name.setText(contacts.get(position).name);
        holder.contact.setText(contacts.get(position).number);

    }

    public void setItems(List<ContactInfoModel> contacts) {
        this.contacts = contacts;
    }


    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView contact;

        public ViewHolder(@NonNull View itemView, ContactListViewInterface RecyclerInterface) {
            super(itemView);

            name = itemView.findViewById(R.id.textInfoRow);
            contact = itemView.findViewById(R.id.textContactRow);

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
