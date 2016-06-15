package com.techpalle.poc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by skillgun on 12/31/2015.
 */
public class PalleDatabase {
    private Context con;
    private  MyHelper mh;
    private SQLiteDatabase sdb;

    public PalleDatabase(Context con){
        mh = new MyHelper(con, "palleuniversity.db", null, 1);
        this.con = con;
    }
    public void open(){
        sdb = mh.getWritableDatabase();
    }
    public void close(){
        sdb.close();
    }

    public long insertPaypal(PaypalData p){
        ContentValues cv = new ContentValues();
        cv.put("amount", p.getAmount());
        cv.put("currency_code", p.getCurrency_code());
        cv.put("shipping", p.getShipping());
        cv.put("subtotal", p.getSubtotal());
        cv.put("tax", p.getTax());
        cv.put("short_description", p.getShort_description());
        cv.put("bn_code", p.getBn_code());
        cv.put("item_name", p.getItem_name());

        cv.put("item_price", p.getItem_price());
        cv.put("currency", p.getCurrency());
        cv.put("sku", p.getSku());
        cv.put("state", p.getState());
        cv.put("paymentid", p.getPaymentId());
        cv.put("createtime", p.getCreateTime());
        cv.put("transactionid", p.getTransactionID());

        //palle specific values
        cv.put("palle_course_name", p.getCourse());
        cv.put("palle_course_id", p.getCourse_id());
        cv.put("palle_email", p.getEmail());//email with which user pruchased course

        return sdb.insert("PaypalPayments", null, cv);
    }

    public long insertCCAvenue(CCAvenueData ccAvenueData) {
        ContentValues cv = new ContentValues();

        cv.put("order_id", ccAvenueData.getOrder_id());//8511072
        cv.put("tracking_id", ccAvenueData.getTracking_id());//104026219461
        cv.put("bank_ref_no", ccAvenueData.getBank_ref_no());//153656611871
        cv.put("order_status", ccAvenueData.getOrder_status());//Success
        cv.put("failure_message", ccAvenueData.getFailure_message());
        cv.put("payment_mode", ccAvenueData.getPayment_mode());//Net Banking
        cv.put("card_name", ccAvenueData.getCard_name());//HDFC Bank
        cv.put("status_code", ccAvenueData.getStatus_code());//null
        cv.put("status_message", ccAvenueData.getStatus_message());//null
        cv.put("currency", ccAvenueData.getCurrency());//INR
        cv.put("amount", ccAvenueData.getAmount());//2.0
        cv.put("billing_name", ccAvenueData.getBilling_name());//Palle reddy akshith
        cv.put("billing_address", ccAvenueData.getBilling_address());//S.Uppalapadu, jammalamadugu, kadapa
        cv.put("billing_city", ccAvenueData.getBilling_city());//Uppalapadu
        cv.put("billing_state", ccAvenueData.getBilling_state());//Andrha pradesh
        cv.put("billing_zip", ccAvenueData.getBilling_zip());//514434
        cv.put("billing_country", ccAvenueData.getBilling_country());//India
        cv.put("billing_tel", ccAvenueData.getBilling_tel());//9740588499
        cv.put("billing_email", ccAvenueData.getBilling_email());//varalakshmirajulapalle@gmail.com
        cv.put("delivery_name", ccAvenueData.getDelivery_name());//Palle reddy akshith
        cv.put("delivery_address", ccAvenueData.getDelivery_address());//S.Uppalapadu, jammalamadugu, kadapa
        cv.put("delivery_city", ccAvenueData.getDelivery_city());//Uppalapadu
        cv.put("delivery_state", ccAvenueData.getDelivery_state());//Andrha pradesh
        cv.put("delivery_zip", ccAvenueData.getDelivery_zip());//514434
        cv.put("delivery_country", ccAvenueData.getDelivery_country());//India
        cv.put("delivery_tel", ccAvenueData.getDelivery_tel());//9740588499
        cv.put("merchant_param1", ccAvenueData.getMerchant_param1());
        cv.put("merchant_param2", ccAvenueData.getMerchant_param2());
        cv.put("merchant_param3", ccAvenueData.getMerchant_param3());
        cv.put("merchant_param4", ccAvenueData.getMerchant_param4());
        cv.put("merchant_param5", ccAvenueData.getMerchant_param5());
        cv.put("vault", ccAvenueData.getVault());//N
        cv.put("offer_type", ccAvenueData.getOffer_type());//null
        cv.put("offer_code", ccAvenueData.getOffer_code());//null
        cv.put("discount_value", ccAvenueData.getDiscount_value());//0.0
        cv.put("mer_amount", ccAvenueData.getMer_amount());//2.0
        cv.put("eci_value", ccAvenueData.getEci_value());//null

        //palle specific values
        cv.put("palle_course_name", ccAvenueData.getPalle_course_name());
        cv.put("palle_course_id", ccAvenueData.getPalle_course_id());
        cv.put("palle_email", ccAvenueData.getPalle_email());//email with which user pruchased course

        return sdb.insert("CCAvenuePayments", null, cv);
    }

    public Cursor getCCAvenueData(String order_id){
        Cursor c = null;
        c = sdb.query("CCAvenuePayments",null,"order_id = ?",new String[]{order_id},null,null,null);
        return c;
    }
    public Cursor getCCAvenueData(){
        Cursor c = null;
        c = sdb.query("CCAvenuePayments",null,null,null,null,null,null);
        return c;
    }
    public Cursor getPaypalData(){
        Cursor c = null;
        c = sdb.query("PaypalPayments",null,null,null,null,null,null);
        return c;
    }
    public Cursor getPaypalData(String transactionID){
        Cursor c = null;
        c = sdb.query("PaypalPayments",null,"transactionid = ?",new String[]{transactionID},null,null,null);
        return c;
    }

    public void insertCourseDetailsFromServer(CourseDetailsFromServer c){
        ContentValues cv = new ContentValues();

        cv.put("assignment_hours",c.getAssignment_hours());
        cv.put("avg_rating",c.getAvg_rating());
        cv.put("course_id",c.getCourse_id());
        cv.put("course_fee_inr_after_discount",c.getCourse_fee_inr_after_discount());
        cv.put("course_fee_usd_after_discount",c.getCourse_fee_usd_after_discount());
        cv.put("course_display_name",c.getCourse_display_name());
        cv.put("course_fee_inr",c.getCourse_fee_inr());
        cv.put("course_fee_usd",c.getCourse_fee_usd());
        cv.put("course_hours",c.getCourse_hours());
        cv.put("total_tests",c.getTotal_tests());
        cv.put("total_views",c.getTotal_views());

        sdb.insert("CourseDetailsFromServer", null, cv);
    }
    public Cursor getCourseDetailsFromServer(){
        return sdb.query("CourseDetailsFromServer",null,null,null,null,null,null);
    }
    public Cursor getCourseDetailsFromServer(String course_id){
        return sdb.query("CourseDetailsFromServer",null,"course_id = ?",
                new String[]{course_id},
                null,null,null);
    }
    public void deleteCourseDetailsFromServer(){
        sdb.delete("CourseDetailsFromServer",null,null);
    }
    private class MyHelper extends SQLiteOpenHelper{
        public MyHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table CCAvenuePayments(_id integer primary key, order_id text, " +
                    "tracking_id text, bank_ref_no text, order_status text, failure_message text, " +
                    "payment_mode text, card_name text, status_code text, status_message text, " +
                    "currency text, amount text, billing_name text, billing_address text, " +
                    "billing_city text, billing_state text, billing_zip text, " +
                    "billing_country text, billing_tel text, billing_email text, " +
                    "delivery_name text, delivery_address text, delivery_city text, " +
                    "delivery_state text, delivery_zip text, delivery_country text, " +
                    "delivery_tel text, merchant_param1 text, merchant_param2 text, " +
                    "merchant_param3 text, merchant_param4 text, merchant_param5 text, " +
                    "vault text, offer_type text, offer_code text, discount_value text, " +
                    "mer_amount text, eci_value text, palle_course_name text, palle_course_id text, " +
                    "palle_email text);");

            db.execSQL("create table PaypalPayments(_id integer primary key, amount text, currency_code text, " +
                    "shipping text, subtotal text, tax text, short_description text, bn_code text, " +
                    "item_name text, item_price text, currency text, sku text, state text, " +
                    "paymentid text, createtime text, transactionid text, palle_course_name text, palle_course_id text, " +
                    "palle_email text, palle_order_id text);");

            db.execSQL("create table CourseDetailsFromServer(_id integer primary key, assignment_hours text, " +
                    "avg_rating text, course_id text, course_fee_inr_after_discount text, " +
                    "course_fee_usd_after_discount text, course_display_name text, course_fee_inr text, " +
                    "course_fee_usd text, course_hours text, total_tests text, total_views text);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
