package tutho.com.newsapiapp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import tutho.com.newsapiapp.R;
import tutho.com.newsapiapp.utils.CommonTasks;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleDetailsFragment extends Fragment {
    public static final String TAG = "ArticleDetailsFragment";
    private String webUrl;
    private WebView articleWebView;


    public ArticleDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null){
            webUrl = args.getString(CommonTasks.JSON_URL);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_article_details, container, false);
        articleWebView = (WebView) rootView.findViewById(R.id.article_webview);
        WebSettings webSettings = articleWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        articleWebView.setWebViewClient(new WebViewClient());
        articleWebView.loadUrl(webUrl);

        return rootView;
    }

}
