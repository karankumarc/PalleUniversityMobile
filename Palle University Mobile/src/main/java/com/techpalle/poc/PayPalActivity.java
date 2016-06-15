package com.techpalle.poc;

import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalOAuthScopes;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.paypal.android.sdk.payments.ProofOfPayment;
import com.paypal.android.sdk.payments.ShippingAddress;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Basic sample using the SDK to make a payment or consent to future payments.
 * 
 * For sample mobile backend interactions, see
 * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
 */
public class PayPalActivity extends Activity {
    private static final String TAG = "paymentExample";
    //satish start
    private String course, course_id, email;
    private int purchaseMode;//0-sdcard, 1-pendrive, 2-both.
    private String priceUSD, palle_order_id;
    //satish end
    /**
     * - Set to PayPalConfiguration.ENVIRONMENT_PRODUCTION to move real money.
     * 
     * - Set to PayPalConfiguration.ENVIRONMENT_SANDBOX to use your test credentials
     * from https://developer.paypal.com
     * 
     * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
     * without communicating to PayPal's servers.
     */
    private static final String CONFIG_ENVIRONMENT =PayPalConfiguration.ENVIRONMENT_NO_NETWORK;// PayPalConfiguration.ENVIRONMENT_PRODUCTION;//ENVIRONMENT_SANDBOX;//ENVIRONMENT_NO_NETWORK;

    // note that these credentials will differ between live & sandbox environments.
    
    // Below is palle technologies paypal android app client id for sandbox testing.
     private static final String CONFIG_CLIENT_ID = "ARPv4uJLcOhxIYoUrgQWZehajZv8846wY9xonQdSvk5kX848ErGiQJK2omWdq_zrSfSbtYfMow0K_zVC";//"ARPv4uJLcOhxIYoUrgQWZehajZv8846wY9xonQdSvk5kX848ErGiQJK2omWdq_zrSfSbtYfMow0K_zVC";
    // Below is palle technologies paypal android app client id for live.
    //private static final String CONFIG_CLIENT_ID = "AZz3ZS_Qc_51wt3n5cKYdC01aynA7A2yHnizrd5Rd--kH350UdPssC1WShbr1wu6bPrnBpmlDPJ2Nh58";
    
    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            .acceptCreditCards(false) //to disable direct card payments - added by satish - since indian merchants are not acceptable to take card payments
            // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Palle University")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paypal_activity);

        //satish start
        Intent in = getIntent();
        Bundle bnd = in.getExtras();
        if(bnd != null){
            course = bnd.getString("course");
            course_id = bnd.getString("course_id");
            email = bnd.getString("email");
            priceUSD = bnd.getString("priceUSD");
        }
        palle_order_id = "";//generate unique id
        //satish end
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        //satish start
        onBuy();
        //satish end
    }

    //satish start

    public void onBuy() {
        /*
         * PAYMENT_INTENT_SALE will cause the payment to complete immediately.
         * Change PAYMENT_INTENT_SALE to
         *   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
         *   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
         *     later via calls from your server.
         *
         * Also, to include additional payment details and an item list, see getStuffToBuy() below.
         */
        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);

        /*
         * See getStuffToBuy(..) for examples of some available payment options.
         */

        //satish adding start
        //enableShippingAddressRetrieval(thingToBuy, true);
        //addAppProvidedShippingAddress(thingToBuy);
        //satish adding end

        Intent intent = new Intent(PayPalActivity.this, PaymentActivity.class);


        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }
    //satish end
    public void onBuyPressed(View pressed) {
        /* 
         * PAYMENT_INTENT_SALE will cause the payment to complete immediately.
         * Change PAYMENT_INTENT_SALE to 
         *   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
         *   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
         *     later via calls from your server.
         * 
         * Also, to include additional payment details and an item list, see getStuffToBuy() below.
         */
        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);

        /*
         * See getStuffToBuy(..) for examples of some available payment options.
         */

        //satish adding start
        enableShippingAddressRetrieval(thingToBuy, true);
        //addAppProvidedShippingAddress(thingToBuy);
        //satish adding end
        
        Intent intent = new Intent(PayPalActivity.this, PaymentActivity.class);
        

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }
    
    private PayPalPayment getThingToBuy(String paymentIntent) {
        return new PayPalPayment(new BigDecimal("1"), "USD", course/*"dotnet course"*/,
                paymentIntent);
    }
    
    /* 
     * This method shows use of optional payment details and item list.
     */
    private PayPalPayment getStuffToBuy(String paymentIntent) {
        //--- include an item list, payment amount details
        PayPalItem[] items =
            {
                    new PayPalItem("sample item #1", 2, new BigDecimal("87.50"), "USD",
                            "sku-12345678"),
                    new PayPalItem("free sample item #2", 1, new BigDecimal("0.00"),
                            "USD", "sku-zero-price"),
                    new PayPalItem("sample item #3 with a longer name", 6, new BigDecimal("37.99"),
                            "USD", "sku-33333") 
            };
        BigDecimal subtotal = PayPalItem.getItemTotal(items);
        BigDecimal shipping = new BigDecimal("7.21");
        BigDecimal tax = new BigDecimal("4.67");
        PayPalPaymentDetails paymentDetails = new PayPalPaymentDetails(shipping, subtotal, tax);
        BigDecimal amount = subtotal.add(shipping).add(tax);
        PayPalPayment payment = new PayPalPayment(amount, "USD", "sample item", paymentIntent);
        payment.items(items).paymentDetails(paymentDetails);

        //--- set other optional fields like invoice_number, custom field, and soft_descriptor
        payment.custom("This is text that will be associated with the payment that the app can use.");

        return payment;
    }
    
    /*
     * Add app-provided shipping address to payment
     */
    private void addAppProvidedShippingAddress(PayPalPayment paypalPayment) {
        ShippingAddress shippingAddress =
                new ShippingAddress().recipientName("Akshith reddy").line1("52 North Main St.")
                        .city("Austin").state("TX").postalCode("78729").countryCode("US");
        paypalPayment.providedShippingAddress(shippingAddress);
    }
    
    /*
     * Enable retrieval of shipping addresses from buyer's PayPal account
     */
    private void enableShippingAddressRetrieval(PayPalPayment paypalPayment, boolean enable) {
        paypalPayment.enablePayPalShippingAddressesRetrieval(enable);
    }

    public void onFuturePaymentPressed(View pressed) {
        Intent intent = new Intent(PayPalActivity.this, PayPalFuturePaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startActivityForResult(intent, REQUEST_CODE_FUTURE_PAYMENT);
    }

    public void onProfileSharingPressed(View pressed) {
        Intent intent = new Intent(PayPalActivity.this, PayPalProfileSharingActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PayPalProfileSharingActivity.EXTRA_REQUESTED_SCOPES, getOauthScopes());

        startActivityForResult(intent, REQUEST_CODE_PROFILE_SHARING);
    }

    private PayPalOAuthScopes getOauthScopes() {
        /* create the set of required scopes
         * Note: see https://developer.paypal.com/docs/integration/direct/identity/attributes/ for mapping between the
         * attributes you select for this app in the PayPal developer portal and the scopes required here.
         */
        Set<String> scopes = new HashSet<String>(
                Arrays.asList(PayPalOAuthScopes.PAYPAL_SCOPE_EMAIL, PayPalOAuthScopes.PAYPAL_SCOPE_ADDRESS) );
        return new PayPalOAuthScopes(scopes);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i(TAG, confirm.toJSONObject().toString(4));
                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));

                        /**
                         *  TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
                         * or consent completion.
                         * See https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                         * for more details.
                         *
                         * For sample mobile backend interactions, see
                         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
                         */
                        Toast.makeText(
                                getApplicationContext(),
                                "PaymentConfirmation info received from PayPal", Toast.LENGTH_LONG)
                                .show();

                        //satish start
                        String root_sd = Environment.getExternalStorageDirectory().toString();
                        File file = new File(root_sd+"/paypal_response1.txt");
                        FileWriter fw;
                        try {
                            fw = new FileWriter(file);
                            fw.write("CONFIRM : \n"+confirm.toJSONObject().toString()+"\n------");
                            fw.write("CONFIRM PAYMENT : \n"+confirm.getPayment().toJSONObject().toString());
                            fw.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        //1. READ PAYPAL PAYMENT DETAILS - from json
                        JSONObject paypalpayment = confirm.getPayment().toJSONObject();
                        String amount = paypalpayment.getString("amount");
                        String currency_code = paypalpayment.getString("currency_code");

                        //JSONObject details = paypalpayment.getJSONObject("details");
                        String shipping = null;//details.getString("shipping");
                        String subtotal = null;//details.getString("subtotal");
                        String tax = null;//details.getString("tax");

                        String short_description = paypalpayment.getString("short_description");
                        String bn_code = null;//paypalpayment.getString("bn_code");

                        //JSONObject item_list = paypalpayment.getJSONObject("item_list");
                        //JSONArray items = item_list.getJSONArray("items");
                        String item_name = null;
                        String item_price = null;
                        String currency = null;
                        String sku = null;
                       /* if(items.length() >= 1){
                            //atleast one item is there, read first item details only as of now
                            JSONObject item1 = items.getJSONObject(0);
                            item_name = item1.getString("name");
                            item_price = item1.getString("price");
                            currency = item1.getString("currency");
                            sku = item1.getString("sku");
                        }*/

                        //2. READ PROOF OF PAYMENT (with out json)
                        ProofOfPayment proofOfPayment = confirm.getProofOfPayment();
                        String state = proofOfPayment.getState();
                        String paymentId = proofOfPayment.getPaymentId();
                        String createTime = proofOfPayment.getCreateTime();
                        String transactionID = proofOfPayment.getTransactionId();

                        //write them into some file
                        File file2 = new File(root_sd+"/paypal_response2.txt");
                        FileWriter fw2;
                        try {
                            fw2 = new FileWriter(file2);
                            fw2.write("amount : "+amount+"\n");
                            fw2.write("currency_code : "+currency_code+"\n");
                            fw2.write("shipping : "+shipping+"\n");
                            fw2.write("subtotal : "+subtotal+"\n");
                            fw2.write("tax : "+tax+"\n");
                            fw2.write("short_description : "+short_description+"\n");
                            fw2.write("bn_code : "+bn_code+"\n");
                            fw2.write("item_name : "+item_name+"\n");
                            fw2.write("item_price : "+item_price+"\n");
                            fw2.write("currency : "+currency+"\n");
                            fw2.write("sku : "+sku+"\n");

                            fw2.write("state : "+state+"\n");
                            fw2.write("paymentId : "+paymentId+"\n");
                            fw2.write("createTime : "+createTime+"\n");
                            fw2.write("transactionID : "+transactionID);

                            fw2.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        //INSERT PAYPAL TRANSACTION DETAILS INTO DATABASE IN THE PHONE
                        PaypalData pdata = new PaypalData(amount, currency_code, shipping, subtotal, tax, short_description, bn_code,
                                item_name, item_price, currency, sku, state, paymentId, createTime, transactionID, course, course_id, email,palle_order_id);
                        PalleDatabase pdb = new PalleDatabase(this);
                        pdb.open();
                        long id = pdb.insertPaypal(pdata);
                        Log.d("PAYPAL","ROW INSERTED.."+id);
                        pdb.close();


                        //SEND PAYPAL PAYMENT STATUS TO PURCHASE ACTIVITY OF PALLE UNIVERSITY
                        Intent in = new Intent();
                        in.setAction("PAYPAL_TRANSACTION_STATUS");
                        in.putExtra("status","success");
                        in.putExtra("transactionID", transactionID);
                        sendBroadcast(in);
                        finish();
                        //satish end


                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                        //SEND PAYPAL PAYMENT STATUS TO PURCHASE ACTIVITY OF PALLE UNIVERSITY
                        Intent in = new Intent();
                        in.setAction("PAYPAL_TRANSACTION_STATUS");
                        in.putExtra("status", "success - jsonexception");
                        in.putExtra("reason","Paypal confirmation data corrupted. \" +\n" +
                                "                            \"Please contact our team immeidately on 080-41645630");
                        sendBroadcast(in);
                        finish();

                    }
                }else{
                    //SEND PAYPAL PAYMENT STATUS TO PURCHASE ACTIVITY OF PALLE UNIVERSITY
                    Intent in = new Intent();
                    in.setAction("PAYPAL_TRANSACTION_STATUS");
                    in.putExtra("status","success - confirm failed");
                    in.putExtra("reason","Paypal confirmation failed. " +
                            "Please contact our team immeidately on 080-41645630");
                    sendBroadcast(in);
                    finish();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.");
                Intent in = new Intent();
                in.setAction("PAYPAL_TRANSACTION_STATUS");
                in.putExtra("status","failure");
                in.putExtra("reason","TRANSACTION CANCELLED");
                sendBroadcast(in);
                finish();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        TAG,
                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
                Intent in = new Intent();
                in.setAction("PAYPAL_TRANSACTION_STATUS");
                in.putExtra("status","failure");
                in.putExtra("reason","Wrong paypal configuration, please try again. Or contact our team at 080-4164530");
                sendBroadcast(in);
                finish();

            }
        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth =
                        data.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("FuturePaymentExample", auth.toJSONObject().toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("FuturePaymentExample", authorization_code);

                        sendAuthorizationToServer(auth);
                        Toast.makeText(
                                getApplicationContext(),
                                "Future Payment code received from PayPal", Toast.LENGTH_LONG)
                                .show();

                    } catch (JSONException e) {
                        Log.e("FuturePaymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("FuturePaymentExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        "FuturePaymentExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            } 
        } else if (requestCode == REQUEST_CODE_PROFILE_SHARING) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth =
                        data.getParcelableExtra(PayPalProfileSharingActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("ProfileSharingExample", auth.toJSONObject().toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("ProfileSharingExample", authorization_code);

                        sendAuthorizationToServer(auth);
                        Toast.makeText(
                                getApplicationContext(),
                                "Profile Sharing code received from PayPal", Toast.LENGTH_LONG)
                                .show();

                    } catch (JSONException e) {
                        Log.e("ProfileSharingExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("ProfileSharingExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        "ProfileSharingExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        }
    }

    private void sendAuthorizationToServer(PayPalAuthorization authorization) {

        /**
         * TODO: Send the authorization response to your server, where it can
         * exchange the authorization code for OAuth access and refresh tokens.
         * 
         * Your server must then store these tokens, so that your server code
         * can execute payments for this user in the future.
         * 
         * A more complete example that includes the required app-server to
         * PayPal-server integration is available from
         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
         */

    }

    public void onFuturePaymentPurchasePressed(View pressed) {
        // Get the Client Metadata ID from the SDK
        String metadataId = PayPalConfiguration.getClientMetadataId(this);

        Log.i("FuturePaymentExample", "Client Metadata ID: " + metadataId);

        // TODO: Send metadataId and transaction details to your server for processing with
        // PayPal...
        Toast.makeText(
                getApplicationContext(), "Client Metadata Id received from SDK", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onDestroy() {
        // Stop service when done
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
}
