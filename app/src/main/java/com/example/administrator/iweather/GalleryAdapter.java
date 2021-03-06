package com.example.administrator.iweather;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/6/20.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    List<String> listitem1=null;
    List<String> listitem2=null;
    LayoutInflater mlayoutinflater=null;
    int id[]=null;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mlayoutinflater.inflate(R.layout.horizontal_list,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.mtime=view.findViewById(R.id.textView7);
        viewHolder.mImg=view.findViewById(R.id.imageView3);
        viewHolder.mtmp=view.findViewById(R.id.textView8);

        return viewHolder;
    }


public GalleryAdapter(Context context, List<String> listitem1,List<String> listitem2,int id[])
{
mlayoutinflater=LayoutInflater.from(context);
this.listitem1=listitem1;
    this.listitem2=listitem2;
this.id=id;

}

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        holder.mtime.setText(listitem1.get(i));
        holder.mtmp.setText(listitem2.get(i));
        holder.mImg.setImageResource(id[i]);

    }

    @Override
    public int getItemCount() {
        return listitem1.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View arg0)
        {
            super(arg0);
        }

        TextView mtime;
        ImageView mImg;
        TextView mtmp;
    }
}
