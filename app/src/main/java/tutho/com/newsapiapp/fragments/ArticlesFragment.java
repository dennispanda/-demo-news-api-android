package tutho.com.newsapiapp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tutho.com.newsapiapp.R;
import tutho.com.newsapiapp.adapters.ArticlesAdapter;
import tutho.com.newsapiapp.http.NetworkConnectionStatus;
import tutho.com.newsapiapp.http.RestClient;
import tutho.com.newsapiapp.interfaces.ItemSelectedListener;
import tutho.com.newsapiapp.models.ArticleItem;
import tutho.com.newsapiapp.utils.AsyncLoader;
import tutho.com.newsapiapp.utils.CommonTasks;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticlesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        LoaderManager.LoaderCallbacks<Object>,ItemSelectedListener {
    public static final String TAG = "ArticlesFragment";
    private AppCompatActivity mActivity;
    private RecyclerView articleRecycler;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar progressBar;
    private String source;
    private String sortBy;
    private boolean categoryFlag;
    private static int LOADER = 1002;
    private ArrayList<ArticleItem> articleItemArrayList = new ArrayList<ArticleItem>();
    private ArticlesAdapter articlesAdapter;
    private ArticleDetailsFragment articleDetailsFragment;


    public ArticlesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mActivity = (AppCompatActivity) activity;
        }catch(ClassCastException ce){
            Log.e(TAG, " Cannot Cast to Acticity" + ce);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null){
            source = args.getString(CommonTasks.SOURCE_NAME);
            sortBy = args.getString(CommonTasks.SOURCE_SORTBY);
            categoryFlag = args.getBoolean(CommonTasks.HAS_CATEGORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_articles, container, false);
        articleRecycler = (RecyclerView) rootView.findViewById(R.id.lst_articles);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager llm = new LinearLayoutManager(mActivity);
        articleRecycler.setLayoutManager(llm);
        progressBar = (ProgressBar) rootView.findViewById(R.id.list_progress);

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        runLoader();
    }

    private void runLoader(){
        if(NetworkConnectionStatus.isOnline(mActivity)) {
            mActivity.getSupportLoaderManager().restartLoader(LOADER, null, this);
        }else{
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.toast_no_connection), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRefresh() {
        runLoader();

    }

    @Override
    public void onItemSelected(int position, Object object, int view) {
        if(NetworkConnectionStatus.isOnline(mActivity)) {
            ArticleItem articleItem = (ArticleItem) object;
            articleDetailsFragment = new ArticleDetailsFragment();
            Bundle args = new Bundle();
            args.putString(CommonTasks.JSON_URL, articleItem.getArticleUrl());
            articleDetailsFragment.setArguments(args);
            CommonTasks.addFragment(articleDetailsFragment, true, FragmentTransaction.TRANSIT_NONE, R.id.lyt_content, mActivity.getSupportFragmentManager(), ArticleDetailsFragment.class.getName());
        }else{
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.toast_no_connection), Toast.LENGTH_LONG).show();
        }

    }

    public static class GetArticles extends AsyncLoader<Object> {
        private ArrayList<ArticleItem> articleItemArrayList = new ArrayList<ArticleItem>();
        private String source;
        private String sortBy;
        private boolean categoryFlag;

        public GetArticles(Context ctx,String source,String sortBy,boolean categoryFlag){
            super(ctx);
            this.source = source;
            this.sortBy = sortBy;
            this.categoryFlag = categoryFlag;
        }

        @Override
        public Object loadInBackground() {
            String dataSent = "";
            String url = CommonTasks.NEWS_ARTICLE_URL;
            String params = CommonTasks.NEWS_SOURCE_ENDPOINT+"="+source+"&"+CommonTasks.NEWS_SORTBY_ENDPOINT+"="+sortBy+
                    "&"+CommonTasks.NEWS_LANGUAGE_ENDPOINT+"="+CommonTasks.NEWS_LANGUAGE+"&"+CommonTasks.API_KEY_ENDPOINT+"="+
                    CommonTasks.API_KEY;
            if(categoryFlag){
                params = params+"&"+CommonTasks.NEWS_CATEGORY_ENDPOINT+"="+CommonTasks.NEWS_CATEGORY;
            }
            try {
                dataSent = RestClient.makeRestRequest(CommonTasks.GET, url, params);
            }catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(TAG, dataSent);
            articleItemArrayList = parseArticleData(dataSent);
            return articleItemArrayList;
        }

        private ArrayList<ArticleItem> parseArticleData(String dataSent){
            ArrayList<ArticleItem> articleItemArrayList = new ArrayList<ArticleItem>();
            if(dataSent != null){
                try {
                    JSONObject jObj = new JSONObject(dataSent);
                    if(jObj.has(CommonTasks.JSON_STATUS) & !jObj.isNull(CommonTasks.JSON_STATUS)){
                        String status = jObj.getString(CommonTasks.JSON_STATUS);
                        if(status.equals(CommonTasks.JSON_OK)){
                            if(jObj.has(CommonTasks.JSON_ARTICLES) & !jObj.isNull(CommonTasks.JSON_ARTICLES)){
                                JSONArray jarrSources = jObj.getJSONArray(CommonTasks.JSON_ARTICLES);
                                for(int i = 0;i < jarrSources.length();i++){
                                    JSONObject jObjSources = jarrSources.getJSONObject(i);
                                    ArticleItem articleItem = new ArticleItem();
                                    if(jObjSources.has(CommonTasks.JSON_AUTHOR) & !jObjSources.isNull(CommonTasks.JSON_AUTHOR)){
                                        articleItem.setArticleAuthor(jObjSources.getString(CommonTasks.JSON_AUTHOR));
                                    }
                                    if(jObjSources.has(CommonTasks.JSON_TITLE) & !jObjSources.isNull(CommonTasks.JSON_TITLE)){
                                        articleItem.setArticleTitle(jObjSources.getString(CommonTasks.JSON_TITLE));
                                    }
                                    if(jObjSources.has(CommonTasks.JSON_DESC) & !jObjSources.isNull(CommonTasks.JSON_DESC)){
                                        articleItem.setArticleDescription(jObjSources.getString(CommonTasks.JSON_DESC));
                                    }
                                    if(jObjSources.has(CommonTasks.JSON_URL) & !jObjSources.isNull(CommonTasks.JSON_URL)){
                                        articleItem.setArticleUrl(jObjSources.getString(CommonTasks.JSON_URL));
                                    }
                                    if(jObjSources.has(CommonTasks.JSON_URL_IMAGE) & !jObjSources.isNull(CommonTasks.JSON_URL_IMAGE)){
                                        articleItem.setArticleImgUrl(jObjSources.getString(CommonTasks.JSON_URL_IMAGE));
                                    }
                                    if(jObjSources.has(CommonTasks.JSON_PUB_DATE) & !jObjSources.isNull(CommonTasks.JSON_PUB_DATE)){
                                        articleItem.setArticleDate(jObjSources.getString(CommonTasks.JSON_PUB_DATE));
                                    }
                                    articleItemArrayList.add(articleItem);
                                }
                            }
                        }else if(status.equals(CommonTasks.JSON_ERROR)){
                            articleItemArrayList = null;
                        }else{
                            articleItemArrayList = null;
                        }
                    }
                } catch (JSONException e) {
                    Log.e(TAG,"JSON excpetion encountered "+e);
                }
            }

            return articleItemArrayList;
        }
    }

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        progressBar.setVisibility(View.VISIBLE);
        return new GetArticles(mActivity,source,sortBy,categoryFlag);
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        progressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        if(data != null){
            articleItemArrayList = (ArrayList<ArticleItem>) data;
            articlesAdapter = new ArticlesAdapter(mActivity,articleItemArrayList,this);
            articleRecycler.setAdapter(articlesAdapter);
        }else{
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.toast_no_articles), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }
}
