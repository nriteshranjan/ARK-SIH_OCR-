package com.example.ml_vision;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
    private Context mContext;
    List<Pair<String, String>> list;

    RecyclerViewAdapter(Context context,List<Pair<String, String>> list)
    {
        mContext = context;
        this.list =list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Pair<String, String> temp= list.get(position);
        ImageView item_image = holder.item_image;
        TextView attributes,values;
        attributes = holder.attributes;
        values = holder.values;
        attributes.setText(temp.first);
        values.setText(temp.second);
        if(position == 0)
        {
            attributes.setTextColor(Color.WHITE);
            values.setTextColor(Color.WHITE);
            attributes.setTextSize(16f);
            values.setTextSize(16f);
            item_image.setColorFilter(Color.argb(255, 68, 138, 255));
        }
        if(position == 0)
            item_image.setImageResource(R.drawable.play);
        else item_image.setImageResource(R.drawable.next);
    }

    @Override
    public int getItemCount()
    {
        return Activity_2.parameter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView item_image;
        TextView attributes,values;

        public ViewHolder(View itemView)
        {
            super(itemView);
            item_image = itemView.findViewById(R.id.item_image);
            attributes = itemView.findViewById(R.id.attr);
            values = itemView.findViewById(R.id.val);
        }
    }
}
