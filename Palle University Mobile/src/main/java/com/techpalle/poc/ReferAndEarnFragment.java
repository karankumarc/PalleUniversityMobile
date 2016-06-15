package com.techpalle.poc;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReferAndEarnFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReferAndEarnFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ReferAndEarnFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReferAndEarnFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReferAndEarnFragment newInstance(String param1, String param2) {
        ReferAndEarnFragment fragment = new ReferAndEarnFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_refer_and_earn, container, false);
        TextView tv = (TextView) v.findViewById(R.id.textView1);
        Button b = (Button) v.findViewById(R.id.button1);

        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = getActivity().getAssets().open("refer_and_earn.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String str = null;
            do {
                str = br.readLine();
                sb.append(str);
            } while (str != null);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        tv.setText(Html.fromHtml(sb.toString()));

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), PalleWebActivity.class);
                startActivity(in);
            }
        });

        return v;
    }

}
