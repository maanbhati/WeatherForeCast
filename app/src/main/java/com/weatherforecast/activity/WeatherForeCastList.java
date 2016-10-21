package com.weatherforecast.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;

import com.weatherforecast.R;
import com.weatherforecast.adapter.CustomExpandableListAdapter;
import com.weatherforecast.model.DayDataModel;
import com.weatherforecast.model.DayDetailDataModel;
import com.weatherforecast.model.ExpandableDetails;
import com.weatherforecast.model.ExpandableHeader;
import com.weatherforecast.model.ListDataModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Mansingh.Bhati on 10/20/2016.
 */
public class WeatherForeCastList extends AppCompatActivity {
    List<ExpandableHeader> expandableListTitle;
    List<ExpandableDetails> expandableDetailsList;
    LinkedHashMap<ExpandableHeader, List<ExpandableDetails>> expandableListDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weatherforecast_list);
        ListDataModel listDataModel = (ListDataModel) getIntent().getSerializableExtra("ListDataModel");

        // binding filtered/formatted data to custom list adaptor
        bindExpandableListData(listDataModel);

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListTitle = new ArrayList<ExpandableHeader>(expandableListDetail.keySet());
        CustomExpandableListAdapter expandableListAdapter = new CustomExpandableListAdapter(WeatherForeCastList.this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });
    }

    /**
     * Method to bind ListDataModel data to expandable list adaptor.
     */
    private void bindExpandableListData(ListDataModel listDataModel) {
        ExpandableHeader expandableHeader = null;
        ExpandableDetails expandableDetails = null;
        List<Double> minTempList = new ArrayList<Double>();
        List<Double> maxTempList = new ArrayList<Double>();
        List<DayDataModel> dayDataModels = listDataModel.getDayDataModels();
        expandableListDetail = new LinkedHashMap<>();
        for (DayDataModel dayDataModel : dayDataModels) {
            expandableHeader = new ExpandableHeader();
            expandableDetailsList = new ArrayList<>();
            expandableHeader.setDay(dayDataModel.getCurrentDate().toString().substring(0, 10));
            List<DayDetailDataModel> dayDetailDataModels = dayDataModel.getDayDetailDataModels();
            for (DayDetailDataModel dayDetailDataModel : dayDetailDataModels) {
                expandableDetails = new ExpandableDetails();
                double minTemp = Double.parseDouble(dayDetailDataModel.getMinTemperature());
                double maxTemp = Double.parseDouble(dayDetailDataModel.getMaxTemperature());
                minTemp = minTemp - 273.15F;
                maxTemp = maxTemp - 273.15F;
                minTempList.add(minTemp);
                maxTempList.add(maxTemp);
                expandableHeader.setWeather(dayDetailDataModel.getWeather());
                expandableDetails.setWeather(dayDetailDataModel.getWeather());
                expandableDetails.setDateAndTime(dayDetailDataModel.getTime());
                double temperature = Double.parseDouble(dayDetailDataModel.getTemperature());
                temperature = temperature - 273.15F;
                expandableDetails.setMinmaxTemp((int) temperature + "" + (char) 0x00B0);
                expandableDetails.setTemperature((int) temperature + "" + (char) 0x00B0);
                expandableDetails.setWind(dayDetailDataModel.getWindSpeed() + " Miles/hour");
                expandableDetails.setCityName(listDataModel.getCityName());
                expandableDetailsList.add(expandableDetails);
            }
            expandableHeader.setMinMaxTemp(minMaxTemp(minTempList, maxTempList));
            minTempList.clear();
            maxTempList.clear();
            expandableListDetail.put(expandableHeader, expandableDetailsList);
        }
    }

    /**
     * Method to filter min and max temperature of the whole day.
     */
    private String minMaxTemp(List<Double> minTempList, List<Double> maxTempList) {
        String minMaxResulted = "";
        double maxValue = Collections.max(maxTempList);
        double minValue = Collections.min(minTempList);
        minMaxResulted = (int) minValue + "" + (char) 0x00B0 + " - " + (int) maxValue + "" + (char) 0x00B0;
        return minMaxResulted;
    }

}
