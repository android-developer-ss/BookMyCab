package com.svs.myprojects.bookmyauto.mapspage;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.svs.myprojects.bookmyauto.R;

import java.util.ArrayList;

/**
 * Created by snehalsutar on 2/4/16.
 */
public class FragmentDriversList extends Fragment implements AdapterView.OnItemClickListener {

    public ArrayList<DriverListItems> listData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                inflater.getContext(), android.R.layout.simple_list_item_1,
//                getResources().getStringArray(R.array.nav_drawer_items));
//        setListAdapter(adapter);
        View rootView = inflater.inflate(R.layout.fragment_drivers_list,container,false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        getListView().setOnItemClickListener(this);

        final RecyclerView recyclerView = (RecyclerView)getActivity().findViewById(R.id.recyclerView);

        // init layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        listData = new ArrayList<>();
        setListData();
        final MyAdapter adapter = new MyAdapter(listData,getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
    }


    public static String[] logoImageUrlList = {
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
    public void setListData() {
        for (int i = 0; i < logoImageUrlList.length; i++) {
            final DriverListItems listModel = new DriverListItems();
            listModel.setImgURL(logoImageUrlList[i]);
            listModel.setDriverName(driverNameList[i]);
            listModel.setDriverRating(driverRatingList[i]);
            listModel.setVehicleNumber(vehicleNumberList[i]);
            listData.add(listModel);
        }
    }
}

/***************************************************************************************************
 * Driver List Item Class. To match the items to be displayed in the CardView
 **************************************************************************************************/
class DriverListItems {
    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(String driverRating) {
        this.driverRating = driverRating;
    }

    String imgURL;
    String driverName;
    String vehicleNumber;
    String driverRating;
}

/***************************************************************************************************
 * Adapter to display items in the CardView
 **************************************************************************************************/
 class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    ArrayList<DriverListItems> dataset_;
    DriverListItems dataValues = null;
    Context context = null;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView vehicleImg;
        public TextView vehicleNumber;
        public TextView driverName;
        public TextView driverRating;
        public RatingBar ratingBar;

        public MyViewHolder(View v) {
            super(v);
            vehicleImg = (ImageView) itemView.findViewById(R.id.driver_list_vehicle_img);
            vehicleNumber = (TextView) itemView.findViewById(R.id.driver_list_vehicle_number);
            driverName = (TextView) itemView.findViewById(R.id.driver_list_driver_name);
            driverRating = (TextView) itemView.findViewById(R.id.driver_list_driver_rating);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);

        }
    }


    public MyAdapter(ArrayList<DriverListItems> dataset, Context paramContext) {
        dataset_ = dataset;
        context = paramContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_drivers_list_item, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        dataValues = dataset_.get(position);
        viewHolder.vehicleNumber.setText(dataValues.getVehicleNumber());
        viewHolder.driverName.setText(dataValues.getDriverName());
        viewHolder.driverRating.setText(dataValues.getDriverRating());
        viewHolder.ratingBar.setRating(Float.parseFloat(dataValues.getDriverRating()));
        Picasso.with(context)
                .load(dataValues.getImgURL())
                .placeholder(R.mipmap.ic_launcher)
                .into(viewHolder.vehicleImg);
    }

    @Override
    public int getItemCount() {
        return dataset_.size();
    }
}