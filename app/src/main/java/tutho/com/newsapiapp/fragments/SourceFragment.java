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
import tutho.com.newsapiapp.adapters.SourceAdapter;
import tutho.com.newsapiapp.http.NetworkConnectionStatus;
import tutho.com.newsapiapp.http.RestClient;
import tutho.com.newsapiapp.interfaces.ItemSelectedListener;
import tutho.com.newsapiapp.models.SourceItem;
import tutho.com.newsapiapp.utils.AsyncLoader;
import tutho.com.newsapiapp.utils.CommonTasks;

/**
 * Created by karuri on 4/24/17.
 */
public class SourceFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        LoaderManager.LoaderCallbacks<Object>,ItemSelectedListener {
    public static final String TAG = "SourceFragment";
    private AppCompatActivity mActivity;
    private RecyclerView sourceRecycler;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar progressBar;
    private boolean categoryFlag;
    private static int LOADER = 1001;
    private ArrayList<SourceItem> sourceItemArrayList = new ArrayList<SourceItem>();
    private SourceAdapter sourceAdapter;
    private ArticlesFragment articlesFragment;

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
            categoryFlag = args.getBoolean(CommonTasks.HAS_CATEGORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_articles, container, false);
        sourceRecycler = (RecyclerView) rootView.findViewById(R.id.lst_articles);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager llm = new LinearLayoutManager(mActivity);
        sourceRecycler.setLayoutManager(llm);
        progressBar = (ProgressBar) rootView.findViewById(R.id.list_progress);

        return rootView;
    }

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
        SourceItem sourceItem = (SourceItem) object;
        articlesFragment = new ArticlesFragment();
        Bundle args = new Bundle();
        args.putString(CommonTasks.SOURCE_NAME,sourceItem.getSourceID());
        args.putString(CommonTasks.SOURCE_SORTBY,sourceItem.getSortBy());
        args.putBoolean(CommonTasks.HAS_CATEGORY,categoryFlag);
        articlesFragment.setArguments(args);
        CommonTasks.addFragment(articlesFragment,true, FragmentTransaction.TRANSIT_NONE,R.id.lyt_content,mActivity.getSupportFragmentManager(),ArticlesFragment.class.getName());
    }

    public static class GetSources extends AsyncLoader<Object> {
        private ArrayList<SourceItem> sourceItemArrayList = new ArrayList<SourceItem>();
        private boolean categoryFlag;


        public GetSources(Context ctx,boolean categoryFlag){
            super(ctx);
            this.categoryFlag = categoryFlag;
        }

        @Override
        public Object loadInBackground() {
            String dataSent = null;
            String url = CommonTasks.NEWS_SOURCE_URL;
            String params = CommonTasks.NEWS_LANGUAGE_ENDPOINT+"="+CommonTasks.NEWS_LANGUAGE+"&"+CommonTasks.API_KEY_ENDPOINT+"="+
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
            sourceItemArrayList = parseSourceData(dataSent);
            return sourceItemArrayList;
        }

        private ArrayList<SourceItem> parseSourceData(String dataSent){
            ArrayList<SourceItem> sourceItemArrayList = new ArrayList<SourceItem>();
            if(dataSent != null){
                try {
                    JSONObject jObj = new JSONObject(dataSent);
                    if(jObj.has(CommonTasks.JSON_STATUS) & !jObj.isNull(CommonTasks.JSON_STATUS)){
                        String status = jObj.getString(CommonTasks.JSON_STATUS);
                        if(status.equals(CommonTasks.JSON_OK)){
                            if(jObj.has(CommonTasks.JSON_SOURCES) & !jObj.isNull(CommonTasks.JSON_SOURCES)){
                                JSONArray jarrSources = jObj.getJSONArray(CommonTasks.JSON_SOURCES);
                                for(int i = 0;i < jarrSources.length();i++){
                                    JSONObject jObjSources = jarrSources.getJSONObject(i);
                                    SourceItem sourceItem = new SourceItem();
                                    if(jObjSources.has(CommonTasks.JSON_ID) & !jObjSources.isNull(CommonTasks.JSON_ID)){
                                        sourceItem.setSourceID(jObjSources.getString(CommonTasks.JSON_ID));
                                    }
                                    if(jObjSources.has(CommonTasks.JSON_NAME) & !jObjSources.isNull(CommonTasks.JSON_NAME)){
                                        sourceItem.setSourceName(jObjSources.getString(CommonTasks.JSON_NAME));
                                    }
                                    if(jObjSources.has(CommonTasks.JSON_DESC) & !jObjSources.isNull(CommonTasks.JSON_DESC)){
                                        sourceItem.setSourceDescription(jObjSources.getString(CommonTasks.JSON_DESC));
                                    }
                                    if(jObjSources.has(CommonTasks.JSON_URL) & !jObjSources.isNull(CommonTasks.JSON_URL)){
                                        sourceItem.setSourceUrl(jObjSources.getString(CommonTasks.JSON_URL));
                                    }
                                    if(jObjSources.has(CommonTasks.JSON_SORTS_BY) & !jObjSources.isNull(CommonTasks.JSON_SORTS_BY)){
                                        JSONArray jarrSorts = jObjSources.getJSONArray(CommonTasks.JSON_SORTS_BY);
                                        sourceItem.setSortBy(jarrSorts.getString(0));

                                    }
                                    sourceItemArrayList.add(sourceItem);
                                }
                            }
                        }else if(status.equals(CommonTasks.JSON_ERROR)){
                            sourceItemArrayList = null;
                        }else{
                            sourceItemArrayList = null;
                        }
                    }
                } catch (JSONException e) {
                    Log.e(TAG,"JSON excpetion encountered "+e);
                }
            }

            return sourceItemArrayList;
        }
    }

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        progressBar.setVisibility(View.VISIBLE);
        return new GetSources(mActivity,categoryFlag);
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        progressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        if(data != null){
            sourceItemArrayList = (ArrayList<SourceItem>) data;
            sourceAdapter = new SourceAdapter(mActivity,sourceItemArrayList,this);
            sourceRecycler.setAdapter(sourceAdapter);
        }else{
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.toast_no_articles), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }
}
