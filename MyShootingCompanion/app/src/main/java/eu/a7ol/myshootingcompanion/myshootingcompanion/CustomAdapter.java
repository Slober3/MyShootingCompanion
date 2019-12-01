package eu.a7ol.myshootingcompanion.myshootingcompanion;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList<DataModel> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewtime;
        TextView textViewVersion;
        ImageView imageViewIcon;
        CardView cardViewColor;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.textViewVersion = (TextView) itemView.findViewById(R.id.textViewVersion);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView);
            this.cardViewColor = (CardView) itemView.findViewById(R.id.card_view);
        this.textViewtime = (TextView) itemView.findViewById(R.id.textView);

        }
    }

    public CustomAdapter(ArrayList<DataModel> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);

        view.setOnClickListener(MainActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewVersion = holder.textViewVersion;
        ImageView imageView = holder.imageViewIcon;
        CardView cardViewcolor = holder.cardViewColor;
        TextView txtTotTime = holder.textViewtime;


        textViewName.setText(dataSet.get(listPosition).getName());
        textViewVersion.setText(dataSet.get(listPosition).getTime());
        imageView.setImageResource(dataSet.get(listPosition).getImage());
        cardViewcolor.setCardBackgroundColor(dataSet.get(listPosition).getColor());
        txtTotTime.setText(dataSet.get(listPosition).getTimedif());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}