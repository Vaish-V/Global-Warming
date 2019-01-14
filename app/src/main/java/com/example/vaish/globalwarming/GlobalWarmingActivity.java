package com.example.vaish.globalwarming;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GlobalWarmingActivity extends AppCompatActivity implements LoaderCallbacks<List<GlobalWarming>> {

    private static final int GW_ID = 1;
    private static final String REQUEST_URL =
            "http://content.guardianapis.com/search?q=%22global%20warming%22&api-key=test";
    private GlobalWarmingAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.global_list);
        Log.i(":    ","Layout set !!!");
        TextView emp = (TextView)findViewById(R.id.empty);
        ListView listView = (ListView)findViewById(R.id.list);
        mAdapter = new GlobalWarmingAdapter(this,new ArrayList<GlobalWarming>());
        listView.setAdapter(mAdapter);
        listView.setEmptyView(emp);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GlobalWarming globalWarming = mAdapter.getItem(i);
                Uri newsUri = Uri.parse(globalWarming.getMurl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(websiteIntent);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            android.app.LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(GW_ID, null, this);
            Log.i("init", "executed");
        }
        else
        {
            emp.setText("No internet connection !");
        }
    }


    @Override
    public Loader<List<GlobalWarming>> onCreateLoader(int i, Bundle bundle) {
        Log.i("onCreate"," Loader");
        return new GlobalLoader(this,REQUEST_URL);
    }


    @Override
    public void onLoadFinished(Loader<List<GlobalWarming>> loader, List<GlobalWarming> globalWarmings) {
        mAdapter.clear();
        Log.i("onLoad","Finished");
        if (globalWarmings != null && !globalWarmings.isEmpty()) {
            mAdapter.addAll(globalWarmings);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<GlobalWarming>> loader) {

        mAdapter.clear();
        Log.i("onLoad","Finished");
    }


    private static class GlobalLoader extends AsyncTaskLoader<List<GlobalWarming>> {
        private final String LOG_TAG = GlobalLoader.class.getName();
        private String mUrl;

        public GlobalLoader(Context context, String url) {
            super(context);
            mUrl = url;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public List<GlobalWarming> loadInBackground() {
            if(mUrl==null)
            {
                return null;
            }
            List<GlobalWarming> globalWarmings = QueryUtils.fetchData(mUrl);
            Log.i("Data fetched","");
            return globalWarmings;
        }
    }
}
