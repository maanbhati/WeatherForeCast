package com.weatherforecast.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.weatherforecast.R;
import com.weatherforecast.model.ExpandableDetails;
import com.weatherforecast.model.ExpandableHeader;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * CustomExpandableListAdapter for expandable list view
 */
public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<ExpandableHeader> expandableListTitle;
    private LinkedHashMap<ExpandableHeader, List<ExpandableDetails>> expandableListDetail;

    public CustomExpandableListAdapter(Context context, List<ExpandableHeader> expandableListTitle,
                                       LinkedHashMap<ExpandableHeader, List<ExpandableDetails>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        ExpandableDetails expandableDetails = (ExpandableDetails) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
        TextView cityName = (TextView) convertView
                .findViewById(R.id.cityName);
        TextView dateAndTime = (TextView) convertView
                .findViewById(R.id.dateAndTime);
        TextView temperature = (TextView) convertView
                .findViewById(R.id.temperature);
        TextView weather = (TextView) convertView
                .findViewById(R.id.weather);
        TextView wind = (TextView) convertView
                .findViewById(R.id.wind);
        TextView minmaxTemp = (TextView) convertView
                .findViewById(R.id.minmaxTemp);

        cityName.setText(expandableDetails.getCityName());
        dateAndTime.setText(expandableDetails.getDateAndTime().substring(0, 5) + " Hrs");
        temperature.setText(expandableDetails.getTemperature());
        weather.setText(expandableDetails.getWeather());
        wind.setText(expandableDetails.getWind());
        minmaxTemp.setText(expandableDetails.getMinmaxTemp() + "C");

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        ExpandableHeader expandableHeader = (ExpandableHeader) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView day = (TextView) convertView
                .findViewById(R.id.day);
        TextView weather = (TextView) convertView
                .findViewById(R.id.weather);
        TextView minmaxTemp = (TextView) convertView
                .findViewById(R.id.minmaxTemp);

        ImageView groupHolder = (ImageView) convertView
                .findViewById(R.id.arrow_right);

        day.setText(expandableHeader.getDay());
        weather.setText(expandableHeader.getWeather());
        minmaxTemp.setText(expandableHeader.getMinMaxTemp());

        if (isExpanded) {
            groupHolder.setImageResource(R.drawable.arrow_down);
        } else {
            groupHolder.setImageResource(R.drawable.arrow_up);
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}