package tutho.com.newsapiapp.adapters;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import tutho.com.newsapiapp.R;
import tutho.com.newsapiapp.interfaces.ItemSelectedListener;
import tutho.com.newsapiapp.models.ArticleItem;
import tutho.com.newsapiapp.models.SourceItem;
import tutho.com.newsapiapp.utils.CommonTasks;

/**
 * Created by karuri on 4/24/17.
 */
public class SourceAdapter extends RecyclerView.Adapter<SourceAdapter.SourceHolder>{
    public static final String TAG = "SourceAdapter";
    private Context ctx;
    private Fragment fragment;
    private ItemSelectedListener mCallBack;
    private ArrayList<SourceItem> sourceItemArrayList = new ArrayList<SourceItem>();

    public SourceAdapter(Context ctx, ArrayList<SourceItem> sourceItemArrayList, Fragment fragment){
        this.ctx = ctx;
        this.fragment = fragment;
        this.sourceItemArrayList = sourceItemArrayList;
        try {
            mCallBack = (ItemSelectedListener) fragment;
        } catch (ClassCastException e) {
            throw new ClassCastException(fragment.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public SourceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View articlesView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_arcticles, parent, false);
        SourceHolder articlesHolder = new SourceHolder(articlesView,fragment);
        return articlesHolder;
    }

    @Override
    public void onBindViewHolder(SourceHolder holder, final int position) {
        final SourceItem sourceItem = (SourceItem) sourceItemArrayList.get(position);
        final String sourceName = sourceItem.getSourceName();
        final String sourceUrl = sourceItem.getSourceUrl();
        final String sourceDescription = sourceItem.getSourceDescription();
        holder.txtName.setText(sourceName);
        Log.e(TAG,"Source name "+sourceItem.getSourceName());
        holder.txtUrl.setText(sourceUrl);
        holder.txtDescription.setText(sourceDescription);

        holder.lytContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Source name "+ sourceName);
                mCallBack.onItemSelected(position,sourceItem,R.id.lyt_content);
            }
        });

    }

    @Override
    public int getItemCount() {
        return sourceItemArrayList.size();
    }

    public static class SourceHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private TextView txtUrl;
        private TextView txtDescription;
        private ImageView imgArticle;
        private LinearLayout lytContent;


        public SourceHolder(View itemView,Fragment fragment){
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txt_author);
            txtUrl = (TextView) itemView.findViewById(R.id.txt_title);
            txtDescription = (TextView) itemView.findViewById(R.id.txt_description);
            imgArticle = (ImageView) itemView.findViewById(R.id.img_article);
            lytContent = (LinearLayout) itemView.findViewById(R.id.lyt_content);
        }
    }
}
