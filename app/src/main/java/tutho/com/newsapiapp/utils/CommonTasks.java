package tutho.com.newsapiapp.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by karuri on 4/24/17.
 */
public class CommonTasks {
    public static final int GET = 0;
    public static final String API_KEY_ENDPOINT = "apiKey";
    public static final String API_KEY = "4e92ffccebd74a08a1eed3bf3c3e7a99";
    public static final String NEWS_ARTICLE_URL = "https://newsapi.org/v1/articles";
    public static final String NEWS_SOURCE_URL = "https://newsapi.org/v1/sources";
    public static final String NEWS_CATEGORY_ENDPOINT = "category";
    public static final String NEWS_CATEGORY = "technology";
    public static final String NEWS_LANGUAGE_ENDPOINT = "language";
    public static final String NEWS_LANGUAGE = "en";
    public static final String NEWS_SOURCE_ENDPOINT = "source";
    public static final String NEWS_SORTBY = "latest";
    public static final String NEWS_SORTBY_ENDPOINT = "sortBy";
    public static final String HAS_CATEGORY = "has_category";
    public static final String JSON_STATUS = "status";
    public static final String JSON_OK = "ok";
    public static final String JSON_ERROR = "error";
    public static final String JSON_SOURCES = "sources";
    public static final String JSON_SORTS_BY = "sortBysAvailable";
    public static final String JSON_ARTICLES = "articles";
    public static final String JSON_ID = "id";
    public static final String JSON_NAME = "name";
    public static final String JSON_DESC = "description";
    public static final String JSON_URL = "url";
    public static final String JSON_AUTHOR = "author";
    public static final String JSON_TITLE = "title";
    public static final String JSON_URL_IMAGE = "urlToImage";
    public static final String JSON_PUB_DATE = "publishedAt";

    public static final String SOURCE_SORTBY = "source_sortby";
    public static final String SOURCE_NAME = "source_name";



    public static void addFragment(Fragment fragment, boolean addToBackStack, int transition, int layoutResourceID, FragmentManager fm, String tag){
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(layoutResourceID, fragment, tag);
        ft.setTransition(transition);
        if (addToBackStack)
            ft.addToBackStack(tag);
        ft.commit();
    }
}
