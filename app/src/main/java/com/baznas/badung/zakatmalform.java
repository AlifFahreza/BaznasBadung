package com.baznas.badung;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class zakatmalform extends Fragment {

    private TextView tv, qrscan;
    private Button btn;


    public zakatmalform() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_zakatmalform, container, false);

        tv = view.findViewById(R.id.tv);
        btn = view.findViewById(R.id.btn);
        qrscan = view.findViewById(R.id.qrSCAN);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });

        return view;
    }

    protected void displayReceivedData(String message)
    {
        qrscan.setText(""+message);
    }

}
