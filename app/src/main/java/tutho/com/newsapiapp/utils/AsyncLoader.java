package tutho.com.newsapiapp.utils;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Loader which extends AsyncTaskLoaders and handles caveats as pointed out in
 * 
 * @param <D> data type
 */
public abstract class AsyncLoader<D> extends AsyncTaskLoader<D> {
	private D data;
	
	public AsyncLoader(Context context) {
		super(context);
	}

	@Override
	public void deliverResult(D data) {
		if (isReset()) {
			// An async query came in while the loader is stopped
			return;
		}

		this.data = data;

		super.deliverResult(data);
	}

	@Override
	protected void onStartLoading() {
		if (data != null) {
			deliverResult(data);
		}

		if (takeContentChanged() || data == null) {
			forceLoad();
		}
	}

	@Override
	protected void onStopLoading() {
		// Attempt to cancel the current load task if possible.
		cancelLoad();
	}

	@Override
	protected void onReset() {
		super.onReset();

		// Ensure the loader is stopped
		onStopLoading();

		data = null;
	}

	public D getData() {
		return data;
	}

    public void setData(D data){
        this.data = data;
    }
}
