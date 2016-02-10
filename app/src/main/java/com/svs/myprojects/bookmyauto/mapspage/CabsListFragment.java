package com.svs.myprojects.bookmyauto.mapspage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.svs.myprojects.bookmyauto.Constants;
import com.svs.myprojects.bookmyauto.R;

import java.util.ArrayList;

/**
 * Created by snehalsutar on 2/4/16.
 */
public class CabsListFragment extends ListFragment implements AdapterView.OnItemClickListener {

    CabsListAdapter mCabsListAdapter;
    ArrayList<CabListItems> mArrayListCabList;
    Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cabs_list, container, false);
        return view; //super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                getActivity(), android.R.layout.simple_list_item_1,
//                getResources().getStringArray(R.array.nav_drawer_items));
        fillArrayList();
        mCabsListAdapter = new CabsListAdapter(mArrayListCabList, mContext);
        setListAdapter(mCabsListAdapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Constants.SELECTED_CAR_INITIAL_COORDINATES);
        intent.putExtra("message","My coord");
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
//        mContext.sendBroadcast(intent);

        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
//            super.onBackPressed();
        }

        //Send an sms..
        sendSMS();
    }

    private void sendSMS() {

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("+15073416266",
                    null,
                    "3755 Main St 110, St Charles, IL, 60174",
                    null,
                    null);
            Toast.makeText(getActivity(), "message Sent", Toast.LENGTH_SHORT).show();

    }

    public void fillArrayList() {
        mArrayListCabList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CabListItems cabListItems = new CabListItems();
            cabListItems.cabNumber = "Cab: " + i;
            cabListItems.cabDriverEmailId = "Email: " + i;
            cabListItems.cabDriverLocation = "Location: " + i;
            cabListItems.cabMinsAway = "Mins Away: " + i;
            mArrayListCabList.add(cabListItems);
        }
    }

}

/***************************************************************************************************
 * Cab List View Items.
 **************************************************************************************************/
class CabListItems {
    String cabNumber;
    String cabDriverEmailId;
    String cabMinsAway;
    String cabDriverLocation;

    public String getCabMinsAway() {
        return cabMinsAway;
    }

    public void setCabMinsAway(String cabMinsAway) {
        this.cabMinsAway = cabMinsAway;
    }

    public String getCabDriverLocation() {
        return cabDriverLocation;
    }

    public void setCabDriverLocation(String cabDriverLocation) {
        this.cabDriverLocation = cabDriverLocation;
    }

    public String getCabDriverEmailId() {
        return cabDriverEmailId;
    }

    public void setCabDriverEmailId(String cabDriverEmailId) {
        this.cabDriverEmailId = cabDriverEmailId;
    }

    public String getCabNumber() {
        return cabNumber;
    }

    public void setCabNumber(String cabNumber) {
        this.cabNumber = cabNumber;
    }
}

/***************************************************************************************************
 * Cab List View Adapter
 **************************************************************************************************/

class CabsListAdapter extends BaseAdapter {
    ArrayList<CabListItems> mCabListItemsArr;
    Context mContext;
    CabListItems cablistItem = null;

    private static LayoutInflater inflater = null;


    public CabsListAdapter(ArrayList<CabListItems> cabListItems, Context context) {
        mCabListItemsArr = cabListItems;
        mContext = context;
        //Layout inflator to call external xml layout ()
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        if (mCabListItemsArr.size() <= 0)
            return 1;
        else
            return mCabListItemsArr.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView cabNumber;
        public TextView cabEmailId;
        public TextView cabLocation;
        public TextView cabMinsAway;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;// = inflater.inflate(R.layout.cab_list_item, null);
        ViewHolder viewHolder = new ViewHolder();

        if (convertView == null) {
            view = inflater.inflate(R.layout.cab_list_item, null);
            viewHolder.cabNumber = (TextView) view.findViewById(R.id.cab_number);
            viewHolder.cabEmailId = (TextView) view.findViewById(R.id.cab_email_id);
            viewHolder.cabLocation = (TextView) view.findViewById(R.id.cab_location);
            viewHolder.cabMinsAway = (TextView) view.findViewById(R.id.cab_mins_away);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


        if (mCabListItemsArr.size() <= 0) {
            viewHolder.cabNumber.setText("No Data");

        } else {
            /***** Get each Model object from Arraylist ********/
            cablistItem = null;
            cablistItem = (CabListItems) mCabListItemsArr.get(position);
            /************  Set Model values in Holder elements ***********/
            viewHolder.cabNumber.setText(cablistItem.getCabNumber());
            viewHolder.cabEmailId.setText(cablistItem.getCabDriverEmailId());
            viewHolder.cabLocation.setText(cablistItem.getCabDriverLocation());
            viewHolder.cabMinsAway.setText(cablistItem.getCabMinsAway());
        }

        return view;
    }
}


