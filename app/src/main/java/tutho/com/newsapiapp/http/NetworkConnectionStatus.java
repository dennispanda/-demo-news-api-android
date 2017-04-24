package tutho.com.newsapiapp.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkConnectionStatus {

	public static boolean isOnline(Context context) {
		ConnectivityManager conMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

		if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
			//Toast.makeText(context, "No Internet connection. Try again later!",
				//	Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

}
