package app.ij.mlwithtensorflowlite.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.ij.mlwithtensorflowlite.MainActivity;
import app.ij.mlwithtensorflowlite.R;
import app.ij.mlwithtensorflowlite.model.ParseItemModel;

public class ParseItemAdapter extends RecyclerView.Adapter<ParseItemAdapter.ViewHolder> {

    private ArrayList<ParseItemModel> parseItems;
    private Context context;

    public ParseItemAdapter(ArrayList<ParseItemModel> parseItems, Context context) {
        this.parseItems = parseItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ParseItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parse_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParseItemAdapter.ViewHolder holder, int position) {
        ParseItemModel parseItem = parseItems.get(position);
        holder.textView.setText(parseItem.getTitle());
        ///Picasso.get().load(parseItem.getImgUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return parseItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
            textView = view.findViewById(R.id.textView);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            ParseItemModel parseItem = parseItems.get(position);

            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("title", parseItem.getTitle());
            intent.putExtra("image", parseItem.getImgUrl());
            intent.putExtra("detailUrl", parseItem.getDetailUrl());
            context.startActivity(intent);
        }
    }

    public void setFilter (ArrayList<ParseItemModel> newList) {
        parseItems = new ArrayList<>();
        parseItems.addAll(newList);
        notifyDataSetChanged();
    }
}
