package com.svs.myprojects.bookmyauto.mapspage;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.svs.myprojects.bookmyauto.R;

import java.util.ArrayList;

public class FragmentHistory extends Fragment {

    public static String[] carImageUrlList = {
            "http://www.fordesigner.com/imguploads/Image/cjbc/zcool/png20080526/1211808122.png",
            "http://files.softicons.com/download/web-icons/urban-stories-icons-by-artdesigner.lv/png/256x256/Car_by_Artdesigner.lv.png",
            "http://iconizer.net/files/Transportation_Brilliant/orig/car.png",
            "http://files.softicons.com/download/transport-icons/car-icon-set-by-bevelandemboss.net/png/325x325/car3.png",
            "http://iconizer.net/files/Brilliant/orig/army_hammer.png",

            "http://icons.iconarchive.com/icons/iconshock/real-vista-transportation/256/vintage-car-icon.png",
            "http://files.softicons.com/download/transport-icons/old-car-icon-by-dunedhel/png/256x256/grey_car.png",
            "http://www.veryicon.com/icon/ico/Transport/Classic%20Car/Classic%20car%20yellow.ico",
            "http://demo.sc.chinaz.com/Files/pic/icons/5918/c4.png",
            "http://www.free-icons-download.net/images/cars-icons-49879.png",

            "http://vector-magz.com/wp-content/uploads/2013/10/ice-cream-truck-clip-art.png",
            "http://2.bp.blogspot.com/_dKt1N49kLCY/TNyxCBcT5XI/AAAAAAAAAQ4/XNsqpOR4a2w/s1600/CarBack_JeroenVanHoorebeke.png",
            "http://iconbug.com/download/size/256/icon/7096/angry-car/",

    };
    public static String[] driverNameList = {
            "Jamie Lannister",
            "Ned Stark",
            "Arya Stark",
            "Jeoffrey Lannister",
            "Sam Winchester",

            "Dean Winchester",
            "Hermoine Granger",
            "Emma Watson",
            "Harry Potter",
            "Ron Weasley",

            "Ellen Degeneres",
            "Ellen Pompeo",
            "Meredith Grey"

    };
    public static String[] date_and_time = {
            "1st June 2015 (3.45 PM)",
            "21st June 2015 (6.35 PM)",
            "3rd July 2015 (11.35 AM)",
            "13th August 2015 (6.35 PM)",
            "30th Sept 2015 (6.35 PM)",

            "1st June 2015 (3.45 PM)",
            "21st June 2015 (6.35 PM)",
            "3rd July 2015 (11.35 AM)",
            "13th August 2015 (6.35 PM)",
            "30th Sept 2015 (6.35 PM)",

            "1st June 2015 (3.45 PM)",
            "21st June 2015 (6.35 PM)",
            "3rd July 2015 (11.35 AM)"

    };
    public static String[] vehicleNumberList = {
            "12345",
            "DFDGERDSF",
            "DALLAS TW",
            "CHICAGO MW",
            "ILLINOS QW",

            "TEXAS DW",
            "DFDGERDSF",
            "DALLAS TW",
            "CHICAGO MW",
            "ILLINOS QW",

            "JAMIE",
            "JAMIE",
            "JAMIE"
    };
    public static String[] driverRatingList = {
            "4.5",
            "3",
            "2",
            "5",
            "4",

            "1.6",
            "2.5",
            "3.8",
            "4.7",
            "5",

            "5",
            "3.5",
            "4.2",

    };

    public ArrayList<HistoryListItems> listData;

    public FragmentHistory() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        getListView().setOnItemClickListener(this);

        final RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerViewInHistory);

        // init layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        listData = new ArrayList<>();
        setListData();
        final MyHistoryAdapter adapter = new MyHistoryAdapter(listData, getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void setListData() {
        for (int i = 0; i < carImageUrlList.length; i++) {
            final HistoryListItems listModel = new HistoryListItems();
            listModel.setTravel_history_driver_name(driverNameList[i]);
            listModel.setTravel_history_vehicle_number(vehicleNumberList[i]);
            listModel.setTravel_history_from_addr("3755 E Main St # 110, St. Charles, IL 60174");
            listModel.setTravel_history_to_addr("1520 E. Main, St. Charles, IL 60174");
            listModel.setTravel_history_charge_amount("25$");
            listModel.setTravel_history_car_type("Santro");
            listModel.setTravel_history_date_and_time(date_and_time[i]);
            listModel.setTravel_history_car_image(carImageUrlList[i]);
            listData.add(listModel);
        }
    }
}

class HistoryListItems {
    String travel_history_from_addr;
    String travel_history_to_addr;
    String travel_history_driver_name;
    String travel_history_vehicle_number;
    String travel_history_car_type;
    String travel_history_charge_amount;
    String travel_history_date_and_time;
    String travel_history_car_image;


    public String getTravel_history_car_image() {
        return travel_history_car_image;
    }

    public void setTravel_history_car_image(String travel_history_car_image) {
        this.travel_history_car_image = travel_history_car_image;
    }


    public String getTravel_history_date_and_time() {
        return travel_history_date_and_time;
    }

    public void setTravel_history_date_and_time(String travel_history_date_and_time) {
        this.travel_history_date_and_time = travel_history_date_and_time;
    }



    public String getTravel_history_charge_amount() {
        return travel_history_charge_amount;
    }

    public void setTravel_history_charge_amount(String travel_history_charge_amount) {
        this.travel_history_charge_amount = travel_history_charge_amount;
    }

    public String getTravel_history_car_type() {
        return travel_history_car_type;
    }

    public void setTravel_history_car_type(String travel_history_car_type) {
        this.travel_history_car_type = travel_history_car_type;
    }

    public String getTravel_history_vehicle_number() {
        return travel_history_vehicle_number;
    }

    public void setTravel_history_vehicle_number(String travel_history_vehicle_number) {
        this.travel_history_vehicle_number = travel_history_vehicle_number;
    }

    public String getTravel_history_driver_name() {
        return travel_history_driver_name;
    }

    public void setTravel_history_driver_name(String travel_history_driver_name) {
        this.travel_history_driver_name = travel_history_driver_name;
    }

    public String getTravel_history_to_addr() {
        return travel_history_to_addr;
    }

    public void setTravel_history_to_addr(String travel_history_to_addr) {
        this.travel_history_to_addr = travel_history_to_addr;
    }

    public String getTravel_history_from_addr() {
        return travel_history_from_addr;
    }

    public void setTravel_history_from_addr(String travel_history_from_addr) {
        this.travel_history_from_addr = travel_history_from_addr;
    }



}


/***************************************************************************************************
 * Adapter to display items in the CardView
 **************************************************************************************************/
class MyHistoryAdapter extends RecyclerView.Adapter<MyHistoryAdapter.MyViewHolder> {

    ArrayList<HistoryListItems> dataset_;
    HistoryListItems dataValues = null;
    Context context = null;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView dateAndTime;
        public TextView fromAddress;
        public TextView toAddress;
        public TextView driverName;
        public TextView vehicleNumber;
        public TextView carType;
        public TextView cost;
        public ImageView vehicleImg;

        public MyViewHolder(View v) {
            super(v);
            vehicleImg = (ImageView) itemView.findViewById(R.id.travel_history_car_image);

            dateAndTime = (TextView) itemView.findViewById(R.id.travel_history_trip_number);
            fromAddress = (TextView) itemView.findViewById(R.id.travel_history_from_addr);
            toAddress = (TextView) itemView.findViewById(R.id.travel_history_to_addr);
            vehicleNumber = (TextView) itemView.findViewById(R.id.travel_history_vehicle_number);
            driverName = (TextView) itemView.findViewById(R.id.travel_history_driver_name);
            carType = (TextView) itemView.findViewById(R.id.travel_history_car_type);
            cost = (TextView) itemView.findViewById(R.id.travel_history_charge_amount);


        }
    }


    public MyHistoryAdapter(ArrayList<HistoryListItems> dataset, Context paramContext) {
        dataset_ = dataset;
        context = paramContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_history_list_item, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        dataValues = dataset_.get(position);
        viewHolder.dateAndTime.setText(dataValues.getTravel_history_date_and_time());
        viewHolder.vehicleNumber.setText(dataValues.getTravel_history_vehicle_number());
        viewHolder.driverName.setText(dataValues.getTravel_history_driver_name());
        viewHolder.fromAddress.setText(dataValues.getTravel_history_from_addr());
        viewHolder.toAddress.setText(dataValues.getTravel_history_to_addr());
        viewHolder.carType.setText(dataValues.getTravel_history_car_type());
        viewHolder.cost.setText(dataValues.getTravel_history_charge_amount());

//        viewHolder.driverRating.setText(dataValues.getDriverRating());
//        viewHolder.ratingBar.setRating(Float.parseFloat(dataValues.getDriverRating()));
        Picasso.with(context)
                .load(dataValues.getTravel_history_car_image())
                .placeholder(R.mipmap.ic_launcher)
                .into(viewHolder.vehicleImg);
    }

    @Override
    public int getItemCount() {
        return dataset_.size();
    }
}