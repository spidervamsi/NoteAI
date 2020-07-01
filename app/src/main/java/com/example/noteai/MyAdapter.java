package com.example.noteai;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    ArrayList<RowData> text;
    Context context;
    ArrayList<Long> selectedValues;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        ConstraintLayout mainLayout;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            text = itemView.findViewById(R.id.textId);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            checkBox  = itemView.findViewById(R.id.check);
            selectedValues = new ArrayList<Long>();
        }



    }

    public MyAdapter(Context ct,ArrayList<RowData> t){
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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.text.setText(text.get(position).getBody());

        Log.i("notedev","adapter "+text.get(position));

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NoteEditor.class);
                intent.putExtra("Text",text.get(position).getBody());
                intent.putExtra("RowId",text.get(position).getRowId());
                context.startActivity(intent);
            }
        });


            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Log.i("notedev", "checkbox " + Integer.toString(position));
                        CheckBox c = (CheckBox) view;
                        if(c.isChecked()){
                            selectedValues.add(text.get(position).getRowId());
                        }else {
                            selectedValues.remove(text.get(position).getRowId());
                        }

                    }catch (Exception e){
                        Log.i("notedev","check exception "+e.getMessage());
                    }

                }
            });

    }

    @Override
    public int getItemCount() {
        return text.size();
    }

    public ArrayList<Long> getSelectedValues(){
        return selectedValues;
    }


}
