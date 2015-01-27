package ru.ifmo.cs.bcomp.android;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.ViewHolder> {
    private String[] data;

    public MemoryAdapter(String[] data) {
        this.data = data;
    }


    @Override
    public MemoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.memory_view, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(data[position]);
    }


    @Override
    public int getItemCount() {
        return data.length;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        private static Typeface typeface = Typeface.create("Courier New", Typeface.NORMAL);

        public ViewHolder(TextView v) {
            super(v);
            textView = v;
            textView.setTypeface(typeface);
        }
    }
}
