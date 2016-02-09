package com.svs.myprojects.bookmyauto.mapspage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.svs.myprojects.bookmyauto.R;

public class FragmentHistory extends Fragment {
	
	public FragmentHistory(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
         
        return rootView;
    }
}
