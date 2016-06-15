package com.techpalle.poc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactUsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContactUsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactUsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView tv1, tv2, tv3;
    private EditText et1, et2, et3, et4;
    Button b;
    private SharedPreferences sp;
    private SharedPreferences.Editor et;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ContactUsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactUsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactUsFragment newInstance(String param1, String param2) {
        ContactUsFragment fragment = new ContactUsFragment();
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
    public void onResume() {
        super.onResume();
        if(mParam2!=null)
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(mParam2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_contact_us, container, false);

        sp = getActivity().getSharedPreferences("profile",0);
        String name = sp.getString("name",null);
        String mobile = sp.getString("mobile",null);
        String email = sp.getString("email",null);


        tv1 = (TextView) v.findViewById(R.id.textView1);
        tv2 = (TextView) v.findViewById(R.id.textView2);
        tv3 = (TextView) v.findViewById(R.id.textView3);

        et1 = (EditText) v.findViewById(R.id.editText1);
        et2 = (EditText) v.findViewById(R.id.editText2);
        et3 = (EditText) v.findViewById(R.id.editText3);
        et4 = (EditText) v.findViewById(R.id.editText4);

        if(name != null){
            et1.setText(name);
        }
        if(mobile != null){
            et2.setText(mobile);
        }
        if(email != null){
            et3.setText(email);
        }

        b = (Button) v.findViewById(R.id.button1);


        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.setAction(Intent.ACTION_CALL);
                in.setData(Uri.parse("tel:+918041645630"));
                startActivity(in);
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.setAction(Intent.ACTION_CALL);
                in.setData(Uri.parse("tel:+918041645630"));
                startActivity(in);
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.setAction(Intent.ACTION_CALL);
                in.setData(Uri.parse("tel:+919741377117"));
                startActivity(in);
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et1.getText().toString();
                String mobile = et2.getText().toString();
                String email = et3.getText().toString();
                String query = et4.getText().toString();

                name.trim();
                mobile.trim();
                email.trim();
                query.trim();

                if(name.equals("")){
                    et1.setHint("Please enter your name");return;
                }
                if(mobile.equals("")){
                    et2.setHint("Enter mobile num");return;
                }
                if(email.equals("")){
                    et3.setHint("Enter your email");return;
                }
                if(query.equals("")){
                    et4.setHint("Please enter your query");return;
                }

                //save user details
                et = sp.edit();
                et.putString("name", name);
                et.putString("mobile", mobile);
                et.putString("email", email);
                et.commit();


                String mailBody = "Name : "+name+"\n"+"Mobile : "+mobile+"\n"+"Email : "+email+"\n"+"Query : "+query;
                Intent intent = new Intent(Intent.ACTION_SENDTO,
                        Uri.fromParts("mailto", "info@techpalle.com", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Course Enquiry");
                intent.putExtra(Intent.EXTRA_TEXT, mailBody);
                startActivity(Intent.createChooser(intent, "Send Mail"));
            }
        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteractionContactUs(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteractionContactUs(Uri uri);
    }
}
