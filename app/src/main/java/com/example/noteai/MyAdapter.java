package com.example.noteai;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    ArrayList<String> text;
    Context context;

    public MyAdapter(Context ct,ArrayList<String> t){
        text = t;
        context = ct;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.text.setText(text.get(position));

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NoteEditor.class);
                intent.putExtra("Text",text.get(position));
                intent.putExtra("Pos",position);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return text.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        ConstraintLayout mainLayout;


        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            text = itemView.findViewById(R.id.textId);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }



    }
}
