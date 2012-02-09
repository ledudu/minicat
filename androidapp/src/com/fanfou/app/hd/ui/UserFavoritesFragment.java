package com.fanfou.app.hd.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Messenger;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.fanfou.app.hd.App;
import com.fanfou.app.hd.db.Contents.BasicColumns;
import com.fanfou.app.hd.db.Contents.StatusInfo;
import com.fanfou.app.hd.service.Constants;
import com.fanfou.app.hd.service.FanFouService;
import com.fanfou.app.hd.util.StringHelper;

/**
 * @author mcxiaoke
 * @version 1.0 2012.02.07
 * @version 1.1 2012.02.09
 * 
 */
public class UserFavoritesFragment extends BaseTimlineFragment {
	private static final String TAG = UserFavoritesFragment.class
			.getSimpleName();
	private String userId;
	private int page=1;

	public static UserFavoritesFragment newInstance(String userId) {
		Bundle args = new Bundle();
		args.putString(Constants.EXTRA_ID, userId);
		UserFavoritesFragment fragment = new UserFavoritesFragment();
		fragment.setArguments(args);
		if (App.DEBUG) {
			Log.d(TAG, "newInstance() "+fragment);
		}
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle data=getArguments();
		if(data!=null){
			userId=data.getString(Constants.EXTRA_ID);
		}
		if(StringHelper.isEmpty(userId)){
			userId=App.getUserId();
		}
		
		if (App.DEBUG) {
			Log.d(TAG, "onCreate() userId="+userId);
		}
	}

	@Override
	protected int getType() {
		return Constants.TYPE_FAVORITES_LIST;
	}

	@Override
	protected void doFetch(boolean doGetMore) {
		if (App.DEBUG) {
			Log.d(TAG, "doFetch() doGetMore=" + doGetMore);
		}
		if (doGetMore) {
			page++;
		} else {
			page = 1;
		}
		final ResultHandler handler = new ResultHandler(this);
		FanFouService.doFetchFavorites(getActivity(), new Messenger(handler), page, userId);
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Uri uri = StatusInfo.CONTENT_URI;
		String selection = BasicColumns.TYPE + " =? AND " + BasicColumns.OWNER_ID
				+ " =? ";
		String[] selectionArgs = new String[] { String.valueOf(getType()), userId};
		CursorLoader loader=new CursorLoader(getActivity(), uri, null, selection, selectionArgs, null);
		return loader;
	}

}