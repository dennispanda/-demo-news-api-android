package tutho.com.newsapiapp.adapters;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import tutho.com.newsapiapp.R;
import tutho.com.newsapiapp.interfaces.ItemSelectedListener;
import tutho.com.newsapiapp.models.ArticleItem;
import tutho.com.newsapiapp.models.SourceItem;
import tutho.com.newsapiapp.utils.CommonTasks;

/**
 * Created by karuri on 4/24/17.
 */
public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticlesHolder>{
    public static final String TAG = "ArticlesAdapter";
    private Context ctx;
    private Fragment fragment;
    private ItemSelectedListener mCallBack;
    private ArrayList<ArticleItem> articleItemArrayList = new ArrayList<ArticleItem>();

    public ArticlesAdapter(Context ctx, ArrayList<ArticleItem> articleItemArrayList, Fragment fragment){
        this.ctx = ctx;
        this.fragment = fragment;
        this.articleItemArrayList = articleItemArrayList;
        try {
            mCallBack = (ItemSelectedListener) fragment;
        } catch (ClassCastException e) {
            throw new ClassCastException(fragment.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public ArticlesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View articlesView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_arcticles, parent, false);
        ArticlesHolder articlesHolder = new ArticlesHolder(articlesView,fragment);
        return articlesHolder;
    }

    @Override
    public void onBindViewHolder(ArticlesHolder holder, final int position) {
        final ArticleItem articleItem = (ArticleItem) articleItemArrayList.get(position);
        final String author = articleItem.getArticleAuthor();
        final String title = articleItem.getArticleTitle();
        final String pubDate = articleItem.getArticleDate();
        final String imgUrl = articleItem.getArticleImgUrl();
        final String desc = articleItem.getArticleDescription();
        holder.txtAuthor.setText(author);
        holder.txtTitle.setText(title);
        holder.txtDescription.setText(desc);
        holder.txtDate.setText(pubDate);
        Glide.with(ctx)
                .load(imgUrl)
                .centerCrop()
                .thumbnail(0.1f)
                .placeholder(R.drawable.globe_news)
                .error(R.drawable.globe_news)
                .into(holder.imgArticle);
        holder.lytContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.onItemSelected(position,articleItem,R.id.lyt_content);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleItemArrayList.size();
    }

    public static class ArticlesHolder extends RecyclerView.ViewHolder {
        private TextView txtAuthor;
        private TextView txtTitle;
        private TextView txtDate;
        private TextView txtDescription;
        private ImageView imgArticle;
        private LinearLayout lytContent;


        public ArticlesHolder(View itemView,Fragment fragment){
            super(itemView);
            txtAuthor = (TextView) itemView.findViewById(R.id.txt_author);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
            txtDate = (TextView) itemView.findViewById(R.id.txt_publish_date);
            txtDescription = (TextView) itemView.findViewById(R.id.txt_description);
            imgArticle = (ImageView) itemView.findViewById(R.id.img_article);
            lytContent = (LinearLayout) itemView.findViewById(R.id.lyt_content);
        }
    }
}
