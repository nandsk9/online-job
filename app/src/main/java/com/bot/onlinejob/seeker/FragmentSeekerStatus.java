package com.bot.onlinejob.seeker;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bot.onlinejob.R;

public class FragmentSeekerStatus extends Fragment {
    Activity referenceActivity;
    View parentHolder;



    //required empty constructor
    public FragmentSeekerStatus(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        referenceActivity=getActivity();
        parentHolder = inflater.inflate(R.layout.seeker_status_fragment, container,
                false);
        Log.e("Frag : ","Status");
        //return parentHolder;
        return parentHolder;
    }
}
