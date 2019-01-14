package com.example.vaish.globalwarming;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Vaish on 23-09-2016.
 */
public class GlobalWarmingAdapter extends ArrayAdapter<GlobalWarming> {

    public GlobalWarmingAdapter(Context context, List<GlobalWarming> globalWarmings) {
        super(context, 0, globalWarmings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.global_activity, parent, false);
        }

        GlobalWarming nowGlobalWarming = getItem(position);
        TextView textView = (TextView) listItemView.findViewById(R.id.t);
        textView.setText(nowGlobalWarming.getMtitle());

        return listItemView;
    }

}
