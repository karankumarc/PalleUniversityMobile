package com.techpalle.poc;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

class DataToPalleServer{
    private String address,city,state,postalcode,country,email,app_specific_trans_id,course_id,
    paid_date,expire_date,payment_type_id,payment_status,discount_id,amount_paid,actual_fee,
    currency_id,payment_gateway_trans_id,payment_mode;

    public DataToPalleServer(String address, String city, String state, String postalcode, String country, String email, String app_specific_trans_id, String course_id, String paid_date, String expire_date, String payment_type_id, String payment_status, String discount_id, String amount_paid, String actual_fee, String currency_id, String payment_gateway_trans_id, String payment_mode) {
        this.address = address;
        this.city = city;
        this.state = state;
        this.postalcode = postalcode;
        this.country = country;
        this.email = email;
        this.app_specific_trans_id = app_specific_trans_id;
        this.course_id = course_id;
        this.paid_date = paid_date;
        this.expire_date = expire_date;
        this.payment_type_id = payment_type_id;
        this.payment_status = payment_status;
        this.discount_id = discount_id;
        this.amount_paid = amount_paid;
        this.actual_fee = actual_fee;
        this.currency_id = currency_id;
        this.payment_gateway_trans_id = payment_gateway_trans_id;
        this.payment_mode = payment_mode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getApp_specific_trans_id() {
        return app_specific_trans_id;
    }

    public void setApp_specific_trans_id(String app_specific_trans_id) {
        this.app_specific_trans_id = app_specific_trans_id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getPaid_date() {
        return paid_date;
    }

    public void setPaid_date(String paid_date) {
        this.paid_date = paid_date;
    }

    public String getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(String expire_date) {
        this.expire_date = expire_date;
    }

    public String getPayment_type_id() {
        return payment_type_id;
    }

    public void setPayment_type_id(String payment_type_id) {
        this.payment_type_id = payment_type_id;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getDiscount_id() {
        return discount_id;
    }

    public void setDiscount_id(String discount_id) {
        this.discount_id = discount_id;
    }

    public String getAmount_paid() {
        return amount_paid;
    }

    public void setAmount_paid(String amount_paid) {
        this.amount_paid = amount_paid;
    }

    public String getActual_fee() {
        return actual_fee;
    }

    public void setActual_fee(String actual_fee) {
        this.actual_fee = actual_fee;
    }

    public String getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(String currency_id) {
        this.currency_id = currency_id;
    }

    public String getPayment_gateway_trans_id() {
        return payment_gateway_trans_id;
    }

    public void setPayment_gateway_trans_id(String payment_gateway_trans_id) {
        this.payment_gateway_trans_id = payment_gateway_trans_id;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }
}

public class CoursePurchaseActivity extends AppCompatActivity implements CCAvenueResultFragment.OnFragmentInteractionListener {
    ActionBar ab;
    String course, course_id;
    String price_inr_before_discount, price_usd_before_discount;
    String priceINR, priceUSD;
    String action;
    String payWith;
    int purchaseMode; //0-sdcard, 1-pendrive, 2-both

    private Button ccavenue, paypal;
    private TextView tv1, tv3, tv6, tv16, tv7, tv17, courseTitle;
    private IntentFilter ccAvenueIntentFilter;
    private CCAvenueTransactionReceiver ccAvenueTransactionReceiver;
    private IntentFilter paypalIntentFilter;
    private PayPalTransactionReceiver payPalTransactionReceiver;

    private boolean ccavenueReceived;
    private Intent ccavenueIntent;
    private boolean paypalReceived;
    private Intent paypalIntent;
    private boolean statusEnabled;
    private MyLoginDialog mld;
    private String email, password;
    private String reg_name, reg_last_name, reg_mail, reg_mobile, reg_ref_mail;
    private String forgot_email;

    private String encodeData(String input) throws UnsupportedEncodingException {
        byte[] encoded = org.apache.commons.codec.binary.Base64.encodeBase64(input.getBytes());
        return new String(encoded);
    }

    public void signUpPressed(String name, String last_name, String email,
                              String mobile, String ref_email) {
        this.reg_name = name;
        this.reg_last_name = last_name;
        this.reg_mail = email;
        this.reg_mobile = mobile;
        this.reg_ref_mail = ref_email;

        new SignUpTask().execute();
    }

    public void retrievePassWord(String email) {
        forgot_email = email;
        new ForgotPasswordTask().execute();
    }

    private class MyLoginDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog d = null;
            ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setTitle("Login Check in progress");
            pd.setMessage("Please wait while we validate with server..");
            d = pd;
            return d;
        }
    }


    private static class ServerPaymentInsert extends DialogFragment{
        public ServerPaymentInsert() {
        }
        public static ServerPaymentInsert newInstance(String param1) {
            Bundle args = new Bundle();
            args.putString("param1",param1);
            ServerPaymentInsert fragment = new ServerPaymentInsert();
            fragment.setArguments(args);
            return fragment;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog d = null;
            AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
            ab.setTitle("Server pay result");
            ab.setMessage(getArguments().getString("param1"));
            d = ab.create();
            return d;
        }
    }
    private class InsertMobilePaymentsTask extends AsyncTask<DataToPalleServer, Void, String> {

        @Override
        protected String doInBackground(DataToPalleServer... params) {
            // TODO Auto-generated method stub
            DataToPalleServer dtp = params[0];

            if(dtp == null){
                return null;
            }

            HttpURLConnection con = null;
            try {
                URL url  = new URL("http://palleuniversity.com/PalleUniversity_App.svc/Mobile/Insert_Mobile_Payments/app");
                con = (HttpURLConnection) url.openConnection();

                String luser = encodeData("Palle_Tech_Palle_University");//Base64.encodeToString("Palle_Tech_Palle_University".getBytes(), 0);
                String lpass = encodeData("University_Palle_Tech_Palle");//Base64.encodeToString("University_Palle_Tech_Palle".getBytes(), 0);


                con.setRequestProperty("address", encodeData(dtp.getAddress()));
                con.setRequestProperty("city", encodeData(dtp.getCity()));
                con.setRequestProperty("state", encodeData(dtp.getState()));
                con.setRequestProperty("postalcode", encodeData(dtp.getPostalcode()));
                con.setRequestProperty("country", encodeData(dtp.getCountry()));
                con.setRequestProperty("email", encodeData(dtp.getEmail()));
                con.setRequestProperty("app_specific_trans_id", encodeData(dtp.getApp_specific_trans_id()));
                con.setRequestProperty("course_id", encodeData(dtp.getCourse_id()));
                con.setRequestProperty("paid_date", encodeData(dtp.getPaid_date()));
                con.setRequestProperty("expire_date", encodeData(dtp.getExpire_date()));
                con.setRequestProperty("payment_type_id", encodeData(dtp.getPayment_type_id()));
                con.setRequestProperty("payment_status", encodeData(dtp.getPayment_status()));
                con.setRequestProperty("discount_id", encodeData(dtp.getDiscount_id()));
                con.setRequestProperty("amount_paid", encodeData(dtp.getAmount_paid()));
                con.setRequestProperty("actual_fee", encodeData(dtp.getActual_fee()));
                con.setRequestProperty("currency_id", encodeData(dtp.getCurrency_id()));
                con.setRequestProperty("payment_gateway_trans_id", encodeData(dtp.getPayment_gateway_trans_id()));
                con.setRequestProperty("payment_mode", encodeData(dtp.getPayment_mode()));

                String course_purchase_mode = "";
                if(purchaseMode == 0){
                    course_purchase_mode = "sdcard";
                }else if(purchaseMode == 1){
                    course_purchase_mode = "pendrive";
                }
                con.setRequestProperty("course_purchase_mode", encodeData(course_purchase_mode));//added newly

                con.setRequestProperty("uname", luser);
                con.setRequestProperty("pwd", lpass);

                con.connect();

                InputStream i = con.getInputStream();
                InputStreamReader ir = new InputStreamReader(i);
                BufferedReader br = new BufferedReader(ir);

                StringBuilder s = new StringBuilder();
                String str = br.readLine();
                while(str != null){
                    s.append(str);
                    str = br.readLine();
                }
                return s.toString();
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                ServerPaymentInsert s = ServerPaymentInsert.newInstance(e.getMessage());
                s.show(getSupportFragmentManager(),"server");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                ServerPaymentInsert s = ServerPaymentInsert.newInstance(e.getMessage());
                s.show(getSupportFragmentManager(), "server");
            }

            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(CoursePurchaseActivity.this, "SIGNUP PROCESS OVER", 1).show();
            mld.dismiss();
            if(result == null){
                Toast.makeText(CoursePurchaseActivity.this, "SERVER NULL RESPONSE", 1).show();
                ServerPaymentInsert s = ServerPaymentInsert.newInstance("SERVER-NULL-RESPONSE");
                s.show(getSupportFragmentManager(), "server");
                return;
            }
            try {
                //RESULT CAME FROM SERVER
                JSONObject j = new JSONObject(result);
                //JSONObject jo = j.getJSONObject("GetMobileDetailsResult");
                String ack = j.getString("Add_User_User_PaymentsResult");

                if(ack.equals("+ack")){
                    Toast.makeText(CoursePurchaseActivity.this, "CHECK YOUR EMAIL..PASS WORD SENT", 1).show();
                    ServerPaymentInsert s = ServerPaymentInsert.newInstance("SERVER-payment-inserted");
                    s.show(getSupportFragmentManager(), "server");
                }else if(ack.equals("-ack")){
                    Toast.makeText(CoursePurchaseActivity.this, "WRONG EMAIL..TRY AGAIN", 1).show();
                    ServerPaymentInsert s = ServerPaymentInsert.newInstance("SERVER-payment-NOT-inserted");
                    s.show(getSupportFragmentManager(), "server");
                }

            } catch (JSONException e) {
                ServerPaymentInsert s = ServerPaymentInsert.newInstance("json exception - "+result);
                s.show(getSupportFragmentManager(),"server");
                Toast.makeText(CoursePurchaseActivity.this, "Some thing went wrong, try later", 1).show();
            }

            super.onPostExecute(result);
        }
    }

    private class ForgotPasswordTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub

            HttpURLConnection con = null;
            try {
                URL url  = new URL("http://palleuniversity.com/PalleUniversity_App.svc/Mobile/ForgotPassword/app");
                con = (HttpURLConnection) url.openConnection();

                String luser = encodeData("Palle_Tech_Palle_University");//Base64.encodeToString("Palle_Tech_Palle_University".getBytes(), 0);
                String lpass = encodeData("University_Palle_Tech_Palle");//Base64.encodeToString("University_Palle_Tech_Palle".getBytes(), 0);


                con.setRequestProperty("email", encodeData(forgot_email));

                con.setRequestProperty("uname", luser);
                con.setRequestProperty("pwd", lpass);

                con.connect();

                InputStream i = con.getInputStream();
                InputStreamReader ir = new InputStreamReader(i);
                BufferedReader br = new BufferedReader(ir);

                StringBuilder s = new StringBuilder();
                String str = br.readLine();
                while(str != null){
                    s.append(str);
                    str = br.readLine();
                }
                return s.toString();
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(CoursePurchaseActivity.this, "SIGNUP PROCESS OVER", 1).show();
            if(mld != null) {
                mld.dismiss();
            }
            if(result == null){
                Toast.makeText(CoursePurchaseActivity.this, "SERVER NULL RESPONSE", 1).show();
                return;
            }
            try {
                //RESULT CAME FROM SERVER
                JSONObject j = new JSONObject(result);
                String ack = j.getString("SendPassWordToEmailResult");

                if(ack.equals("+ack")){
                    Toast.makeText(CoursePurchaseActivity.this, "CHECK YOUR EMAIL..PASS WORD SENT", 1).show();
                    Frag1 m1=new Frag1();
                    FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container, m1);
                    ft.commit();
                }else if(ack.equals("-ack")){
                    Toast.makeText(CoursePurchaseActivity.this, "WRONG EMAIL..TRY AGAIN", 1).show();
                }
            } catch (JSONException e) {
                Toast.makeText(CoursePurchaseActivity.this, "Some thing went wrong, try later", 1).show();
            }

            super.onPostExecute(result);
        }

    }

    private class SignUpTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            //SHOW A DIALOG PROGRESS BOX - VALIDATING USER AUTHENTICATION
            /*
            mld = new MyLoginDialog();
            mld.show(getSupportFragmentManager(), "progress");

            Toast.makeText(CoursePurchaseActivity.this, "VALIDATION IN PROGRESS WITH SERVER...PLZ WAIT", 1).show();
            */
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub

            HttpURLConnection con = null;
            try {
                URL url  = new URL("http://palleuniversity.com/PalleUniversity_App.svc/Mobile/MobileSignUp/app");
                con = (HttpURLConnection) url.openConnection();

                String luser = encodeData("Palle_Tech_Palle_University");//Base64.encodeToString("Palle_Tech_Palle_University".getBytes(), 0);
                String lpass = encodeData("University_Palle_Tech_Palle");//Base64.encodeToString("University_Palle_Tech_Palle".getBytes(), 0);


                con.setRequestProperty("firstname", encodeData(reg_name));
                con.setRequestProperty("lastname", encodeData(reg_last_name));
                con.setRequestProperty("address", encodeData(""));
                con.setRequestProperty("cityname", encodeData(""));
                con.setRequestProperty("state", encodeData(""));
                con.setRequestProperty("zip", encodeData(""));
                con.setRequestProperty("countryname", encodeData(""));
                con.setRequestProperty("email", encodeData(reg_mail));
                con.setRequestProperty("mobile", encodeData(reg_mobile));
                con.setRequestProperty("password", encodeData(""));
                con.setRequestProperty("referralemail", encodeData(reg_ref_mail));

                con.setRequestProperty("uname", luser);
                con.setRequestProperty("pwd", lpass);

                con.connect();

                InputStream i = con.getInputStream();
                InputStreamReader ir = new InputStreamReader(i);
                BufferedReader br = new BufferedReader(ir);

                StringBuilder s = new StringBuilder();
                String str = br.readLine();
                while(str != null){
                    s.append(str);
                    str = br.readLine();
                }
                return s.toString();
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(CoursePurchaseActivity.this, "SIGNUP PROCESS OVER", 1).show();
            //mld.dismiss();
            if(result == null){
                Toast.makeText(CoursePurchaseActivity.this, "SERVER NULL RESPONSE", 1).show();
                return;
            }
            try {
                //RESULT CAME FROM SERVER
                JSONObject j = new JSONObject(result);
                JSONObject jo = j.getJSONObject("GetMobileDetailsResult");
                String ack = jo.getString("ack");

                if(ack.equals("+ve")){
                    Toast.makeText(CoursePurchaseActivity.this, "SIGN UP SUCCESS", 1).show();
                    Frag1 m1=new Frag1();
                    FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container, m1);
                    ft.commit();
                }else if(ack.equals("-ve")){
                    Toast.makeText(CoursePurchaseActivity.this, "USER REGISTRATION FAILED..TRY AGAIN", 1).show();
                }

            } catch (JSONException e) {
                Toast.makeText(CoursePurchaseActivity.this, "Some thing went wrong, try later", 1).show();
            }

            super.onPostExecute(result);
        }

    }

    private class LoginTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            //SHOW A DIALOG PROGRESS BOX - VALIDATING USER AUTHENTICATION
            mld = new MyLoginDialog();
            mld.show(getSupportFragmentManager(), "progress");

            Toast.makeText(CoursePurchaseActivity.this, "VALIDATION IN PROGRESS WITH SERVER...PLZ WAIT", 1).show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub

            HttpURLConnection con = null;
            try {
                URL url  = new URL("http://palleuniversity.com/PalleUniversity_App.svc/Mobile/LoginDetails/app");
                con = (HttpURLConnection) url.openConnection();

                String luser = encodeData("Palle_Tech_Palle_University");//Base64.encodeToString("Palle_Tech_Palle_University".getBytes(), 0);
                String lpass = encodeData("University_Palle_Tech_Palle");//Base64.encodeToString("University_Palle_Tech_Palle".getBytes(), 0);

                String lemail = encodeData(email);//Base64.encodeToString(po_order_no.getBytes(), 0);
                String lpassword =  encodeData(password);//Base64.encodeToString(imei_meid_esn.getBytes(), 0);

                con.setRequestProperty("email", lemail);//po_order_no);
                con.setRequestProperty("password", lpassword);//imei_meid_esn);
                con.setRequestProperty("uname", luser);
                con.setRequestProperty("pwd", lpass);

                con.connect();

                InputStream i = con.getInputStream();
                InputStreamReader ir = new InputStreamReader(i);
                BufferedReader br = new BufferedReader(ir);

                StringBuilder s = new StringBuilder();
                String str = br.readLine();
                while(str != null){
                    s.append(str);
                    str = br.readLine();
                }
                return s.toString();
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(CoursePurchaseActivity.this, "VALIDATION OVER", 1).show();
            mld.dismiss();
            if(result == null){
                Toast.makeText(CoursePurchaseActivity.this, "SERVER NULL RESPONSE", 1).show();
                return;
            }
            try {
                //RESULT CAME FROM SERVER
                JSONObject j = new JSONObject(result);
                //JSONObject jo = j.getJSONObject("GetLoginDetailsResult");
                String ack = j.getString("GetLoginDetailsResult");//jo.getString("ack");

                if(ack.equals("+ack")){
                    Toast.makeText(CoursePurchaseActivity.this, "USER TEST PASS", 1).show();
                    if(payWith.equals("ccavenue")){
                        //launch ccavenue page
                        Intent in = new Intent(CoursePurchaseActivity.this, InitialScreenActivity.class);
                        in.putExtra("email",email);//user email who is purchasing this
                        in.putExtra("course",course);
                        in.putExtra("course_id",course_id);
                        in.putExtra("priceINR",priceINR);
                        in.putExtra("purchaseMode",purchaseMode);
                        startActivity(in);
                    }else if(payWith.equals("paypal")){
                        //launch paypal page
                        Intent in = new Intent(CoursePurchaseActivity.this, PayPalActivity.class);
                        in.putExtra("email",email);//user email who is purchasing this
                        in.putExtra("course",course);
                        in.putExtra("course_id",course_id);
                        in.putExtra("priceUSD",priceUSD);
                        in.putExtra("purchaseMode",purchaseMode);
                        startActivity(in);
                    }
                }else if(ack.equals("-ack")){
                    Toast.makeText(CoursePurchaseActivity.this, "USER TEST FAIL..check your email and pw", 1).show();
                }

            } catch (JSONException e) {
                Toast.makeText(CoursePurchaseActivity.this, "Some thing went wrong, try later", 1).show();
            }

            super.onPostExecute(result);
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    private void paypalReceiverFromOnPostResume(Intent intent){

        tv1.setBackgroundColor(Color.parseColor("#cccccc"));
        tv3.setBackgroundColor(Color.parseColor("#337ab7"));
        statusEnabled = true;

        PalleDatabase pdb = new PalleDatabase(CoursePurchaseActivity.this);
        pdb.open();
        Bundle bnd = intent.getExtras();
        if(bnd != null){
            String status = bnd.getString("status");
            if(status.equals("success")) {
                String transactionID = bnd.getString("transactionID");//transaction id - varchar
                Cursor c = pdb.getPaypalData(transactionID);
                if (c != null) {
                    if (c.moveToFirst() == true) {
                        String palle_course_id = c.getString(c.getColumnIndex("palle_course_id"));//new
                        String palle_email = c.getString(c.getColumnIndex("palle_email"));//new
                        String palle_course_name = c.getString(c.getColumnIndex("palle_course_name"));//new
                        String palle_order_id = c.getString(c.getColumnIndex("palle_order_id"));//new

                        String amount = c.getString(c.getColumnIndex("amount"));
                        String currency_code = c.getString(c.getColumnIndex("currency_code"));
                        String shipping = c.getString(c.getColumnIndex("shipping"));
                        String subtotal = c.getString(c.getColumnIndex("subtotal"));
                        String tax = c.getString(c.getColumnIndex("tax"));
                        String short_description = c.getString(c.getColumnIndex("short_description"));

                        String bn_code = c.getString(c.getColumnIndex("bn_code"));
                        String item_name = c.getString(c.getColumnIndex("item_name"));
                        String item_price = c.getString(c.getColumnIndex("item_price"));
                        String currency = c.getString(c.getColumnIndex("currency"));
                        String sku = c.getString(c.getColumnIndex("sku"));
                        String state = c.getString(c.getColumnIndex("state"));
                        String paymentid = c.getString(c.getColumnIndex("paymentid"));
                        String createtime = c.getString(c.getColumnIndex("createtime"));
                        String transactionid = c.getString(c.getColumnIndex("transactionid"));

                        int cur_id = 1;//course id - int (1 USD, 2 INR) - Currency id
                        //Date paid_date = Calendar.getInstance().getTime(); //paid date - datetime

                        int year = Calendar.getInstance().get(Calendar.YEAR)+1900;//FROM 1900
                        int month = Calendar.getInstance().get(Calendar.MONTH)+1;//0-11
                        int date = Calendar.getInstance().get(Calendar.DATE);//1-31
                        String paid_date = ""+month+"-"+date+"-"+year;
                        //expire date - datetime (6 months from date of paid, can be changed later)
                        String expire_date;
                        if(month+6 > 12){
                            int expmonth = (month+6)-12;
                            int expyear = year+1;
                            expire_date = ""+expmonth+"-"+date+"-"+expyear;
                        }else{
                            expire_date = ""+(month+6)+"-"+date+"-"+year;
                        }

                        int pt_id = 2;//pt_id - int (1credit_debitcard, 2paypal, 3ccavenue, 4net_transfer, 5cash) - payment type
                        int disc_id = 1;//disc id - int (1 no_discount) - discount id

                        //SHOW RESULT TRANSACTION STATUS TO USER
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        CCAvenueResultFragment resultFragment =
                                CCAvenueResultFragment.newInstance("PAYPAL PAYMENT STATUS:\n" +
                                                "item_name : " + item_name,
                                        "item price : " + item_price + "\n" +
                                                "sub total : " + subtotal + "\n" +
                                                "tax : " + tax + "\n" +
                                                "currency : " + currency + "\n" +
                                                "amount : " + amount + "\n" + "shipping : " + shipping + "\n" +
                                                "transactionID : " + transactionid + "\n" +
                                                "paymentid : " + paymentid + "\n" +
                                                "For more details check your registered Email \n" +
                                                "We will keep you updated with your order status..");
                        ft.replace(R.id.container, resultFragment);
                        ft.commit();

                        Cursor c2 = pdb.getCourseDetailsFromServer(palle_course_id);
                        String course_fee_inr = null, course_fee_inr_after_discount = null;
                        if(c2 != null){
                            boolean moved = c2.moveToFirst();
                            if(moved){
                                course_fee_inr = c2.getString(c2.getColumnIndex("course_fee_inr"));
                                course_fee_inr_after_discount = c2.getString(c2.getColumnIndex("course_fee_inr_after_discount"));
                            }
                        }
                        if(course_fee_inr == null){
                            course_fee_inr = amount;
                        }

                        DataToPalleServer dtp = new DataToPalleServer(shipping,"no_del_city_provided","no_del_state_provided",
                                "no_del_zip_provided","no_del_country_provided",palle_email,palle_order_id/*app spcfc trnscn id*/,palle_course_id,
                                paid_date,expire_date,"3"/*ccavenue=3*/,"true"/*paymnt status*/,"1"/*dicsount*/,
                                course_fee_inr_after_discount,course_fee_inr,""+cur_id,transactionid,"paid from paypal"/*payment mode*/);
                        new InsertMobilePaymentsTask().execute(dtp);

                    }
                }
            }else if(status.equals("success - jsonexception") || status.equals("success - confirm failed") ||
                    status.equals("failure")){
                String reason = bnd.getString("reason");
                //SHOW RESULT TRANSACTION STATUS TO USER
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                CCAvenueResultFragment resultFragment =
                        CCAvenueResultFragment.newInstance("PAYPAL PAYMENT STATUS:\n",
                                reason);
                ft.replace(R.id.container, resultFragment);
                ft.commit();
            }
        }
        paypalIntent = null;
    }
    private void ccavenueReceiverFromOnPostResume(Intent intent){
        statusEnabled = true;
        tv1.setBackgroundColor(Color.parseColor("#cccccc"));
        tv3.setBackgroundColor(Color.parseColor("#337ab7"));

        PalleDatabase pdb = new PalleDatabase(CoursePurchaseActivity.this);
        pdb.open();
        Bundle bnd = intent.getExtras();
        if(bnd != null) {
            String order_id = bnd.getString("order_id");//transaction id - varchar
            String transaction_status = bnd.getString("status");//status given by webview activity
            Cursor c = pdb.getCCAvenueData(order_id);
            if(c != null){
                if(c.moveToFirst() == true){
                    String palle_course_id = c.getString(c.getColumnIndex("palle_course_id"));//new
                    String palle_email = c.getString(c.getColumnIndex("palle_email"));//new
                    String palle_course_name = c.getString(c.getColumnIndex("palle_course_name"));//new

                    String tracking_id = c.getString(c.getColumnIndex("tracking_id"));//transaction id - varchar
                    String bank_ref_no = c.getString(c.getColumnIndex("bank_ref_no"));//transaction id - varchar
                    String delivery_address = c.getString(c.getColumnIndex("delivery_address"));//address - varchar
                    String delivery_city = c.getString(c.getColumnIndex("delivery_city"));//city - varchar
                    String delivery_state = c.getString(c.getColumnIndex("delivery_state"));//state - varchar
                    String delivery_zip = c.getString(c.getColumnIndex("delivery_zip"));//zip - varchar
                    String delivery_country = c.getString(c.getColumnIndex("delivery_country"));//country - varchar
                    String email = c.getString(c.getColumnIndex("billing_email"));//email - varchar
                    String discount_value = c.getString(c.getColumnIndex("discount_value"));//unused
                    String mer_amount = c.getString(c.getColumnIndex("mer_amount"));//unused
                    int cur_id = 2;//course id - int (1 USD, 2 INR) - Currency id
                    //Date paid_date = Calendar.getInstance().getTime(); //paid date - datetime
                    int year = Calendar.getInstance().get(Calendar.YEAR);//+1900;//FROM 1900
                    int month = Calendar.getInstance().get(Calendar.MONTH)+1;//0-11
                    int date = Calendar.getInstance().get(Calendar.DATE);//1-31
                    String paid_date = ""+month+"-"+date+"-"+year;
                    //expire date - datetime (6 months from date of paid, can be changed later)
                    String expire_date;
                    if(month+6 > 12){
                        int expmonth = (month+6)-12;
                        int expyear = year+1;
                        expire_date = ""+expmonth+"-"+date+"-"+expyear;
                    }else{
                        expire_date = ""+(month+6)+"-"+date+"-"+year;
                    }

                    int pt_id = 3;//pt_id - int (1credit_debitcard, 2paypal, 3ccavenue, 4net_transfer, 5cash) - payment type
                    String order_status = c.getString(c.getColumnIndex("order_status"));//payment status - bit
                    int disc_id = 1;//disc id - int (1 no_discount) - discount id
                    String amount = c.getString(c.getColumnIndex("amount"));//amount paid - int
                    //actual fee - int
                    String currency = c.getString(c.getColumnIndex("currency"));//currency id - int
                    String failure_message = c.getString(c.getColumnIndex("failure_message"));//unused
                    String payment_mode = c.getString(c.getColumnIndex("payment_mode"));//unused
                    String card_name = c.getString(c.getColumnIndex("card_name"));//unused
                    String status_code = c.getString(c.getColumnIndex("status_code"));//unused
                    String status_message = c.getString(c.getColumnIndex("status_message"));//unused
                    String delivery_name = c.getString(c.getColumnIndex("delivery_name"));//unused


                    //SHOW RESULT TRANSACTION STATUS TO USER
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    if(transaction_status.equals("Transaction Successful!")) {
                        CCAvenueResultFragment resultFragment =
                                CCAvenueResultFragment.newInstance("CCAVENUE PAYMENT STATUS : \n" +
                                        "order_id : " + order_id, "\n" + transaction_status +
                                        "\n" + "Delivered to : " + delivery_address + "\n" + "Amount : " + amount + "\n" +
                                        "For more details check your registered Email \n" +
                                        "We will keep you updated with your order status..");
                        ft.replace(R.id.container, resultFragment);
                        //insert transaction to palle university web server
                        /*
                        private String address,city,state,postalcode,country,email,app_specific_trans_id,course_id,
                        paid_date,expire_date,payment_type_id,payment_status,discount_id,amount_paid,actual_fee,
                        currency_id,payment_gateway_trans_id,payment_mode;
                        */
                        Cursor c2 = pdb.getCourseDetailsFromServer(palle_course_id);
                        String course_fee_inr = null, course_fee_inr_after_discount = null;
                        if(c2 != null){
                            boolean moved = c2.moveToFirst();
                            if(moved){
                                course_fee_inr = c2.getString(c2.getColumnIndex("course_fee_inr"));
                                course_fee_inr_after_discount = c2.getString(c2.getColumnIndex("course_fee_inr_after_discount"));
                            }
                        }
                        if(course_fee_inr == null){
                            course_fee_inr = amount;
                        }

                        DataToPalleServer dtp = new DataToPalleServer(delivery_address,delivery_city,delivery_state,
                                delivery_zip,delivery_country,palle_email,order_id/*app spcfc trnscn id*/,palle_course_id,
                                paid_date,expire_date,"3"/*ccavenue=3*/,"true"/*paymnt status*/,"1"/*dicsount*/,
                                course_fee_inr_after_discount,course_fee_inr,""+cur_id,tracking_id,payment_mode);

                        new InsertMobilePaymentsTask().execute(dtp);

                    }else if(transaction_status.equals("Transaction Declined!")){
                        CCAvenueResultFragment resultFragment =
                                CCAvenueResultFragment.newInstance("CCAVENUE PAYMENT STATUS : \n" +
                                        "order_id : " + order_id, "\n" + transaction_status +
                                        "\n" + "order status : " + order_status + "\n" + "payment mode : " + payment_mode + "\n" +
                                        "status message : "+status_message+"\n"+"Please retry with other mode.");
                        ft.replace(R.id.container, resultFragment);
                    }else if(transaction_status.equals("Transaction Cancelled!")){
                        CCAvenueResultFragment resultFragment =
                                CCAvenueResultFragment.newInstance("CCAVENUE PAYMENT STATUS : \n" +
                                        "order_id : " + order_id, "\n" + transaction_status);
                        ft.replace(R.id.container, resultFragment);
                    }else if(transaction_status.equals("Status Not Known!")){
                        CCAvenueResultFragment resultFragment =
                                CCAvenueResultFragment.newInstance("CCAVENUE PAYMENT STATUS : \n" +
                                        "order_id : " + order_id, "\n" + transaction_status +
                                        "\n"+"Please retry with other mode.");
                        ft.replace(R.id.container, resultFragment);
                    }else{
                        CCAvenueResultFragment resultFragment =
                                CCAvenueResultFragment.newInstance("CCAVENUE PAYMENT STATUS : \n",
                                        "Invalid transaction, try again");
                        ft.replace(R.id.container, resultFragment);
                    }
                    ft.commit();



                }
            }
        }
        ccavenueIntent = null;
    }



    private class PayPalTransactionReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            paypalReceived = true;
            paypalIntent = intent;
        }
    }

    private class CCAvenueTransactionReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            ccavenueReceived = true;
            ccavenueIntent = intent;
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(ccavenueReceived == true){
            ccavenueReceived = false;
            ccavenueReceiverFromOnPostResume(ccavenueIntent);
        }else if(paypalReceived == true){
            paypalReceived = false;
            paypalReceiverFromOnPostResume(paypalIntent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(ccAvenueTransactionReceiver);
        unregisterReceiver(payPalTransactionReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_purchase);
        paypal = (Button) findViewById(R.id.paypal);
        ccavenue = (Button) findViewById(R.id.ccavenue);
        tv1 = (TextView) findViewById(R.id.textView1);
        tv3 = (TextView) findViewById(R.id.textView3);

        tv6 = (TextView) findViewById(R.id.textView6);
        tv16 = (TextView) findViewById(R.id.textView16);
        tv7 = (TextView) findViewById(R.id.textView7);
        tv17 = (TextView) findViewById(R.id.textView17);
        courseTitle = (TextView) findViewById(R.id.courseTitle);

        tv6.setPaintFlags(tv6.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tv7.setPaintFlags(tv7.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        ccAvenueIntentFilter = new IntentFilter();
        ccAvenueIntentFilter.addAction("CCAVENUE_TRANSACTION_STATUS");
        ccAvenueTransactionReceiver = new CCAvenueTransactionReceiver();
        registerReceiver(ccAvenueTransactionReceiver, ccAvenueIntentFilter);

        paypalIntentFilter = new IntentFilter();
        paypalIntentFilter.addAction("PAYPAL_TRANSACTION_STATUS");
        payPalTransactionReceiver = new PayPalTransactionReceiver();
        registerReceiver(payPalTransactionReceiver, paypalIntentFilter);

        Intent in = getIntent();
        if(in != null){
            Bundle bnd = in.getExtras();
            if(bnd != null){
                course = bnd.getString("course");
                course_id = bnd.getString("course_id");
                price_inr_before_discount = bnd.getString("price_inr_before_discount");
                price_usd_before_discount = bnd.getString("price_usd_before_discount");
                priceINR = bnd.getString("priceINR");
                priceUSD = bnd.getString("priceUSD");
                purchaseMode = bnd.getInt("purchaseMode");
                action = bnd.getString("action");
                payWith = bnd.getString("pay");
            }
        }

        tv6.setText("\u20B9 "+((Integer.parseInt(price_inr_before_discount.trim()))));
        tv16.setText("\u20B9 "+((Integer.parseInt(priceINR.trim()))));
        tv7.setText("$ "+((Integer.parseInt(price_usd_before_discount.trim()))));
        tv17.setText("$ "+((Integer.parseInt(priceUSD.trim()))));
        courseTitle.setText(course);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ab = getSupportActionBar();

        Drawable d = getResources().getDrawable(R.drawable.actionbar_bg_shape);
        ab.setBackgroundDrawable(d);
        ab.setDisplayShowTitleEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        //if(course != null)
        ab.setTitle(course);

        if(action.equals("signup")) {
            Frag3 m1 = new Frag3();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.container, m1);
            ft.commit();
        }else if(action.equals("login")){
            Frag1 m1=new Frag1();
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.add(R.id.container, m1);
            ft.commit();
        }

        if(payWith.equals("ccavenue")){
            //ccavenue.setBackgroundColor(Color.parseColor("#0000ff"));
            ccavenue.setBackground(getResources().getDrawable(R.drawable.button_shape_blue));
        }else if(payWith.equals("paypal")){
            //paypal.setBackgroundColor(Color.parseColor("#0000ff"));
            paypal.setBackground(getResources().getDrawable(R.drawable.button_shape_blue));
        }

        ccavenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ccavenue.setBackgroundColor(Color.parseColor("#0000ff"));
                //paypal.setBackgroundColor(Color.parseColor("#528fc2"));
                ccavenue.setBackground(getResources().getDrawable(R.drawable.button_shape_blue));
                paypal.setBackground(getResources().getDrawable(R.drawable.button_shape));
                Toast.makeText(CoursePurchaseActivity.this, "SELECTED PAY WITH CC-AVENUE", Toast.LENGTH_SHORT).show();
                payWith = "ccavenue";
                if(statusEnabled){
                    statusEnabled = false;
                    Frag1 m1=new Frag1();
                    FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container, m1).addToBackStack("tag");
                    ft.commit();
                    tv1.setBackgroundColor(Color.parseColor("#337ab7"));
                    tv3.setBackgroundColor(Color.parseColor("#cccccc"));
                }
            }
        });
        paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //paypal.setBackgroundColor(Color.parseColor("#0000ff"));
                //ccavenue.setBackgroundColor(Color.parseColor("#528fc2"));
                paypal.setBackground(getResources().getDrawable(R.drawable.button_shape_blue));
                ccavenue.setBackground(getResources().getDrawable(R.drawable.button_shape));
                Toast.makeText(CoursePurchaseActivity.this, "SELECTED PAY WITH PAYPAL", Toast.LENGTH_SHORT).show();
                payWith = "paypal";
                if(statusEnabled){
                    statusEnabled = false;
                    Frag1 m1=new Frag1();
                    FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container, m1).addToBackStack("tag");
                    ft.commit();
                    tv1.setBackgroundColor(Color.parseColor("#337ab7"));
                    tv3.setBackgroundColor(Color.parseColor("#cccccc"));
                }
            }
        });

    }

    public void pressed()
    {
        if(action.equals("signup")) {
            Terms t = new Terms();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, t).addToBackStack("tag");
            ft.commit();
        }else if(action.equals("login")){
            Terms t=new Terms();
            FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container,t).addToBackStack("tag");
            ft.commit();
        }
    }

    public void pressedprivacy()
    {
        if(action.equals("signup")) {
            Privacy p = new Privacy();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, p);
            ft.commit();
        }else if(action.equals("login")){
            Frag2 p=new Frag2();
            FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, p);
            ft.commit();
        }
    }

    public void pressedprivacylogin()
    {

        Frag1 p=new Frag1();
        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container,p);
        ft.commit();

    }

    public void loginButtonClicked(String email, String password){
        this.email = email;
        this.password = password;
        new LoginTask().execute();
        //removed and moved to logintask
    }

    public void pressedsignup(){
        //user want to signup, open sign up fragment
        Frag3 p=new Frag3();
        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, p);
        ft.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Toast.makeText(CoursePurchaseActivity.this, "hello", Toast.LENGTH_SHORT).show();
        int id = item.getItemId();
        /*if (id == R.id.action_settings) {
            return true;
        }
        else if(id==android.R.id.home)
        {
            Frag1 mmm=new Frag1();
            FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container,mmm);
            ft.commit();
        }*/

        switch(id){
            case android.R.id.home:
                finish();
                break;
            case R.id.contact:
                Intent in = new Intent(this, ContactUsActivity.class);
                startActivity(in);
                break;
            case R.id.mail:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.coursepurchasemenu, menu);
        menu.getItem(1).setVisible(false);
        return true;
    }

}
