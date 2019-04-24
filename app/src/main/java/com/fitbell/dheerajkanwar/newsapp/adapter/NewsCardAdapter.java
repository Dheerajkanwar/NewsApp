package com.fitbell.dheerajkanwar.newsapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.fitbell.dheerajkanwar.newsapp.databinding.NewsFeedCardBinding;
import com.fitbell.dheerajkanwar.newsapp.model.Article;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class NewsCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Article> arrayList ;
    private LayoutInflater inflater ;

    public NewsCardAdapter(ArrayList<Article> arrayList) {
        this.arrayList = arrayList ;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(inflater == null){
            inflater = LayoutInflater.from(parent.getContext()) ;
        }

        NewsFeedCardBinding newsFeedCardBinding = NewsFeedCardBinding.inflate(inflater, parent, false);
        return new NewsItemViewHolder(newsFeedCardBinding) ;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        NewsItemViewHolder holder1 = (NewsItemViewHolder) holder ;
        Article datum = arrayList.get(position) ;
        holder1.bind(datum);

    }

    @Override
    public int getItemCount() {
        return arrayList == null ? 0 : arrayList.size();
    }



    class NewsItemViewHolder extends RecyclerView.ViewHolder {

        private NewsFeedCardBinding newsFeedCardBinding ;

        NewsItemViewHolder(NewsFeedCardBinding newsFeedCardBinding) {
            super(newsFeedCardBinding.getRoot());
            this.newsFeedCardBinding = newsFeedCardBinding ;
        }

        void bind(Article article) {
            newsFeedCardBinding.setArticle(article);
        }

    }

    public void addDatatoAdapter() {
        notifyDataSetChanged();
    }


}
