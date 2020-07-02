package com.example.noteai;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    ArrayList<RowData> text;
    Context context;
    ArrayList<Long> selectedValues;
    ViewGroup vg;
    MenuItem mainDeleteButton;
    Boolean delMode = false;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            text = itemView.findViewById(R.id.textId);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            selectedValues = new ArrayList<Long>();

        }

    }

    public MyAdapter(Context ct, ArrayList<RowData> t){
        text = t;
        context = ct;

    }

    public void setMenuItem(MenuItem menuItem){
        mainDeleteButton = menuItem;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row,parent, false);
        vg = parent;
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.text.setText(text.get(position).getBody());

        Log.i("notedev","adapter "+text.get(position));

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(delMode){
                    ConstraintLayout cl = (ConstraintLayout) view;
                    updateSelectedItems(position,cl);
                }else{
                    Intent intent = new Intent(context, NoteEditor.class);
                    intent.putExtra("Text",text.get(position).getBody());
                    intent.putExtra("RowId",text.get(position).getRowId());
                    context.startActivity(intent);
                }
            }
        });

        holder.mainLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.i("notedev","on long click");
                try{
                    ConstraintLayout cl = (ConstraintLayout) view;
                    updateSelectedItems(position,cl);
                }catch (Exception e){
                    Log.i("notedev","visibility exception "+e.getMessage());
                }
                return true;
            }
        });


    }

    public void updateSelectedItems(int position,ConstraintLayout cl){
        try{
            long id = text.get(position).getRowId();
            if(selectedValues.contains(id)){
//              TODO Need to set the background color properly
                cl.getViewById(R.id.rowCard).setBackgroundColor(0x00000000);
                selectedValues.remove(id);
            }else{
                cl.getViewById(R.id.rowCard).setBackgroundColor(Color.rgb(190,241,222));
                selectedValues.add(id);
            }

            if(selectedValues.isEmpty()){
                delMode = false;
            }else {
                delMode = true;
            }
            mainDeleteButton.setVisible(delMode);
        }catch (Exception e){
            Log.i("notedev","visibility exception "+e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return text.size();
    }

    public ArrayList<Long> getSelectedValues(){
        return selectedValues;
    }


}
