package com.example.clpagaduan.animomol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class arrayAdapter extends ArrayAdapter<Card> {
    Context context;
    public arrayAdapter(Context context, int resourceId, List<Card> items){
        super(context, resourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        Card card_item = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView fname = convertView.findViewById(R.id.cardView_name);
        TextView dls_id = convertView.findViewById(R.id.cardView_idcourse);
        TextView desc = convertView.findViewById(R.id.cardView_desc);
        ImageView image = convertView.findViewById(R.id.image);

        fname.setText(card_item.getFname());
        dls_id.setText(card_item.getDls_id());
        desc.setText(card_item.getDesc());
        switch (card_item.getProfileImageUrl()){
            case "default":
                Glide.with(getContext()).load(R.mipmap.ic_launcher).into(image);
                break;
            default:
                Glide.clear(image);
                Glide.with(getContext()).load(card_item.getProfileImageUrl()).into(image);
                break;
        }


        return convertView;

    }
}
