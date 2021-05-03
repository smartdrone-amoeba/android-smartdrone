package com.dgs.smartdrone.activity.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dgs.smartdrone.R;
import com.dgs.smartdrone.activity.SettingActivity;
import com.dgs.smartdrone.activity.WayPoint2Activity;
import com.dgs.smartdrone.activity.WayPointActivity;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView txtOne = root.findViewById(R.id.txtOne);
        final TextView txtTwo = root.findViewById(R.id.txtTwo);
        final TextView txtThree = root.findViewById(R.id.txtThree);
        txtOne.setOnClickListener(this);
        txtTwo.setOnClickListener(this);;
        txtThree.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtOne: {

                Intent intent = new Intent(getContext(), WayPointActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.txtTwo: {

                Intent intent = new Intent(getContext(), WayPoint2Activity.class);
                startActivity(intent);
                break;
            }
            case R.id.txtThree: {

                Intent intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
                break;
            }
            default:
                break;
        }
    }
}