package com.techpalle.poc;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.techpalle.poc.MyOrdersFragment.OnFragmentInteractionListenerOders} interface
 * to handle interaction events.
 * Use the {@link MyOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

class Orders{
    private String item0, item1, item2, item3, from;
    private String billing_name,billing_address,billing_city,billing_state,billing_country;
    private String delivery_name,delivery_address, delivery_city, delivery_state, delivery_country;

    public String getDelivery_name() {
        return delivery_name;
    }

    public void setDelivery_name(String delivery_name) {
        this.delivery_name = delivery_name;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public String getDelivery_city() {
        return delivery_city;
    }

    public void setDelivery_city(String delivery_city) {
        this.delivery_city = delivery_city;
    }

    public String getDelivery_state() {
        return delivery_state;
    }

    public void setDelivery_state(String delivery_state) {
        this.delivery_state = delivery_state;
    }

    public String getDelivery_country() {
        return delivery_country;
    }

    public void setDelivery_country(String delivery_country) {
        this.delivery_country = delivery_country;
    }

    public String getBilling_name() {
        return billing_name;
    }

    public void setBilling_name(String billing_name) {
        this.billing_name = billing_name;
    }

    public String getBilling_address() {
        return billing_address;
    }

    public void setBilling_address(String billing_address) {
        this.billing_address = billing_address;
    }

    public String getBilling_city() {
        return billing_city;
    }

    public void setBilling_city(String billing_city) {
        this.billing_city = billing_city;
    }

    public String getBilling_state() {
        return billing_state;
    }

    public void setBilling_state(String billing_state) {
        this.billing_state = billing_state;
    }

    public String getBilling_country() {
        return billing_country;
    }

    public void setBilling_country(String billing_country) {
        this.billing_country = billing_country;
    }

    public String getItem0() {
        return item0;
    }

    public void setItem0(String item0) {
        this.item0 = item0;
    }

    public String getItem1() {
        return item1;
    }

    public void setItem1(String item1) {
        this.item1 = item1;
    }

    public String getItem2() {
        return item2;
    }

    public void setItem2(String item2) {
        this.item2 = item2;
    }

    public String getItem3() {
        return item3;
    }

    public void setItem3(String item3) {
        this.item3 = item3;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
public class MyOrdersFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ListView lv;
    MyAdapter m;
    PalleDatabase pdb;
    Cursor c;
    ArrayList<Orders> al;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListenerOders mListener;

    public MyOrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyOrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyOrdersFragment newInstance(String param1, String param2) {
        MyOrdersFragment fragment = new MyOrdersFragment();
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


    private class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return al.size();
        }
        @Override
        public Object getItem(int position) {
            return al.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = getActivity().getLayoutInflater().inflate(R.layout.order_row, null);

            TextView tv1 = (TextView) v.findViewById(R.id.textView1);
            TextView tv2 = (TextView) v.findViewById(R.id.textView2);
            TextView tv3 = (TextView) v.findViewById(R.id.textView3);
            TextView tv4 = (TextView) v.findViewById(R.id.textView4);
            TextView tv5 = (TextView) v.findViewById(R.id.textView5);

            TextView tv6 = (TextView) v.findViewById(R.id.textView6);
            TextView tv7 = (TextView) v.findViewById(R.id.textView7);
            TextView tv8 = (TextView) v.findViewById(R.id.textView8);
            TextView tv9 = (TextView) v.findViewById(R.id.textView9);
            TextView tv10 = (TextView) v.findViewById(R.id.textView10);

            TextView tv11 = (TextView) v.findViewById(R.id.textView11);
            TextView tv12 = (TextView) v.findViewById(R.id.textView12);
            TextView tv13 = (TextView) v.findViewById(R.id.textView13);
            TextView tv14 = (TextView) v.findViewById(R.id.textView14);
            TextView tv15 = (TextView) v.findViewById(R.id.textView15);

            Orders o = al.get(position);
            tv1.setText(o.getItem0());
            tv2.setText(o.getItem1());
            tv3.setText(o.getItem2());
            tv4.setText(o.getItem3());
            tv5.setText(o.getFrom());

            tv6.setText(o.getBilling_name());
            tv7.setText(o.getBilling_address());
            tv8.setText(o.getBilling_city());
            tv9.setText(o.getBilling_state());
            tv10.setText(o.getBilling_country());

            tv11.setText(o.getDelivery_name());
            tv12.setText(o.getDelivery_address());
            tv13.setText(o.getDelivery_city());
            tv14.setText(o.getDelivery_state());
            tv15.setText(o.getDelivery_country());

            return v;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_orders, container, false);
        lv = (ListView) v.findViewById(R.id.listView1);
        al = new ArrayList<Orders>();
        pdb = new PalleDatabase(getActivity());
        pdb.open();
        c = pdb.getCCAvenueData();
        if(c != null){
            while(c.moveToNext()){
                String palle_course_name = c.getString(c.getColumnIndex("palle_course_name"));
                String order_id = c.getString(c.getColumnIndex("order_id"));
                String order_status = c.getString(c.getColumnIndex("order_status"));
                String currency = c.getString(c.getColumnIndex("currency"));
                String amount = c.getString(c.getColumnIndex("amount"));

                String billing_name = c.getString(c.getColumnIndex("billing_name"));
                String billing_address = c.getString(c.getColumnIndex("billing_address"));
                String billing_city = c.getString(c.getColumnIndex("billing_city"));
                String billing_state = c.getString(c.getColumnIndex("billing_state"));
                String billing_country = c.getString(c.getColumnIndex("billing_country"));

                String delivery_name = c.getString(c.getColumnIndex("delivery_name"));
                String delivery_address = c.getString(c.getColumnIndex("delivery_address"));
                String delivery_city = c.getString(c.getColumnIndex("delivery_city"));
                String delivery_state = c.getString(c.getColumnIndex("delivery_state"));
                String delivery_country = c.getString(c.getColumnIndex("delivery_country"));

                Orders o = new Orders();
                o.setFrom("Payment mode : CCAvenue");
                o.setItem0("Course : " + palle_course_name);
                o.setItem1("Order id : " + order_id);
                o.setItem2("Order status : " + order_status);
                o.setItem3("Amount : " + currency + " " + amount);

                o.setBilling_name(billing_name);
                o.setBilling_country(billing_country);
                o.setBilling_address(billing_address);
                o.setBilling_city(billing_city);
                o.setBilling_state(billing_state);

                o.setDelivery_name(delivery_name);
                o.setDelivery_address(delivery_address);
                o.setDelivery_city(delivery_city);
                o.setDelivery_state(delivery_state);
                o.setDelivery_country(delivery_country);

                al.add(o);
            }
        }

        c = pdb.getPaypalData();
        if(c != null){
            while(c.moveToNext()){
                String order_id = c.getString(c.getColumnIndex("transactionid"));
                String item_name = c.getString(c.getColumnIndex("item_name"));
                String amount = c.getString(c.getColumnIndex("amount"));

                Orders o = new Orders();
                o.setFrom("Payment mode : PayPal");
                o.setItem0("Course : "+item_name);
                o.setItem1("Order id : " + order_id);
                o.setItem2("Order status : ");
                o.setItem3("Amount : " + amount);

                al.add(o);
            }
        }

        m = new MyAdapter();
        lv.setAdapter(m);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteractionOrders(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListenerOders) {
            mListener = (OnFragmentInteractionListenerOders) context;
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
    public interface OnFragmentInteractionListenerOders {
        // TODO: Update argument type and name
        void onFragmentInteractionOrders(Uri uri);
    }

    
}
