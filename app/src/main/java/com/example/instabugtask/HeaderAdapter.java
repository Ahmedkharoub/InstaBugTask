package com.example.instabugtask;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.ViewHolder> {
    @NonNull


    private ArrayList<headerKey> header;
    public HeaderAdapter (ArrayList<headerKey>header){
        this.header = header;
    }


    public ArrayList<headerKey> getList(){
        return header;
    }

    public HeaderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.headers,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.key.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                    header.get(viewType).setKey(s.toString());
            }
        });
        viewHolder.value.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                    header.get(viewType).setValue(s.toString());
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HeaderAdapter.ViewHolder holder, int position) {
       holder.key.setText(header.get(position).getKey());
       holder.value.setText(header.get(position).getValue());
       holder.removeRow.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               header.remove(holder.getAdapterPosition());
               notifyDataSetChanged();
           }
       });
    }

    @Override
    public int getItemCount() {
        return header.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText key;
        EditText value;
        ImageButton removeRow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            key = itemView.findViewById(R.id.edit_key);
            value = itemView.findViewById(R.id.edit_value);
            removeRow = itemView.findViewById(R.id.remove_row);
        }
    }
}
