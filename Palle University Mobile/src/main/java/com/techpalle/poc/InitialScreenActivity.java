package com.techpalle.poc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.avenues.lib.utility.AvenuesParams;
import com.avenues.lib.utility.ServiceUtility;

import java.math.BigInteger;
import java.security.SecureRandom;

public class InitialScreenActivity extends Activity {
	
	private EditText accessCode, merchantId, currency, amount, orderId, rsaKeyUrl, redirectUrl, cancelUrl;
	//satish start
	private String email, course, course_id, priceINR;
	private int purchaseMode;//0-sdcard, 1-pendrive, 2-both

	//satish end
	private void init(){
		accessCode = (EditText) findViewById(R.id.accessCode);
		merchantId = (EditText) findViewById(R.id.merchantId);
		orderId  = (EditText) findViewById(R.id.orderId);
		currency = (EditText) findViewById(R.id.currency);
		amount = (EditText) findViewById(R.id.amount);
		rsaKeyUrl = (EditText) findViewById(R.id.rsaUrl);
		redirectUrl = (EditText) findViewById(R.id.redirectUrl);
		cancelUrl = (EditText) findViewById(R.id.cancelUrl);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_initial_screen);
		//for testing start
        /*
		Intent in2 = new Intent();
		in2.setAction("CCAVENUE_TRANSACTION_STATUS");
		sendBroadcast(in2);
		finish();
        */
		//testing end

		init();
		
		//generating order number
		//Integer randomNum = ServiceUtility.randInt(0, 9999999);
		//Generate unique palle order id
		String r1 = "PU_"+System.currentTimeMillis();//gets current time in milliseconds starting from jan 1 1970
		String r2 = "_"+ServiceUtility.randInt(0, 9999999);//generates a random number till 9999999
		//SecureRandom random = new SecureRandom();
		//String r3 = "_"+new BigInteger(130, random).toString(32);



		orderId.setText(r1+r2);//randomNum.toString());

        //satish start
        Intent in = getIntent();
        Bundle bnd = in.getExtras();
        if(bnd != null){
			email = bnd.getString("email");
            course = bnd.getString("course");
			course_id = bnd.getString("course_id");
            priceINR = bnd.getString("priceINR");
            purchaseMode = bnd.getInt("purchaseMode");
        }
        Toast.makeText(this, "PRICE : "+priceINR, Toast.LENGTH_LONG).show();
        //amount.setText("2"); //only for testing purpose we are paying 2 RUPEES, change it
        amount.setText(priceINR);
        startPurchase();
        finish();
        //satish end

    }

	//satish start
	public void startPurchase(){
		String vAccessCode = ServiceUtility.chkNull(accessCode.getText()).toString().trim();
		String vMerchantId = ServiceUtility.chkNull(merchantId.getText()).toString().trim();
		String vCurrency = ServiceUtility.chkNull(currency.getText()).toString().trim();
		String vAmount = ServiceUtility.chkNull(amount.getText()).toString().trim();
		if(!vAccessCode.equals("") && !vMerchantId.equals("") && !vCurrency.equals("") && !vAmount.equals("")){
			Intent intent = new Intent(this,WebViewActivity.class);
			intent.putExtra(AvenuesParams.ACCESS_CODE, ServiceUtility.chkNull(accessCode.getText()).toString().trim());
			intent.putExtra(AvenuesParams.MERCHANT_ID, ServiceUtility.chkNull(merchantId.getText()).toString().trim());
			intent.putExtra(AvenuesParams.ORDER_ID, ServiceUtility.chkNull(orderId.getText()).toString().trim());
			intent.putExtra(AvenuesParams.CURRENCY, ServiceUtility.chkNull(currency.getText()).toString().trim());
			intent.putExtra(AvenuesParams.AMOUNT, ServiceUtility.chkNull(amount.getText()).toString().trim());

			intent.putExtra(AvenuesParams.REDIRECT_URL, ServiceUtility.chkNull(redirectUrl.getText()).toString().trim());
			intent.putExtra(AvenuesParams.CANCEL_URL, ServiceUtility.chkNull(cancelUrl.getText()).toString().trim());
			intent.putExtra(AvenuesParams.RSA_KEY_URL, ServiceUtility.chkNull(rsaKeyUrl.getText()).toString().trim());
			//satish start
			intent.putExtra("email",email);
			intent.putExtra("course",course);
			intent.putExtra("course_id",course_id);
			intent.putExtra("priceINR",priceINR);
            intent.putExtra("purchaseMode",purchaseMode);
			//satish end
			startActivity(intent);
		}else{
			showToast("All parameters are mandatory.");
		}
	}
	//satish end

	public void onClick(View view) {
		//Mandatory parameters. Other parameters can be added if required.
		String vAccessCode = ServiceUtility.chkNull(accessCode.getText()).toString().trim();
		String vMerchantId = ServiceUtility.chkNull(merchantId.getText()).toString().trim();
		String vCurrency = ServiceUtility.chkNull(currency.getText()).toString().trim();
		String vAmount = ServiceUtility.chkNull(amount.getText()).toString().trim();
		if(!vAccessCode.equals("") && !vMerchantId.equals("") && !vCurrency.equals("") && !vAmount.equals("")){
			Intent intent = new Intent(this,WebViewActivity.class);
			intent.putExtra(AvenuesParams.ACCESS_CODE, ServiceUtility.chkNull(accessCode.getText()).toString().trim());
			intent.putExtra(AvenuesParams.MERCHANT_ID, ServiceUtility.chkNull(merchantId.getText()).toString().trim());
			intent.putExtra(AvenuesParams.ORDER_ID, ServiceUtility.chkNull(orderId.getText()).toString().trim());
			intent.putExtra(AvenuesParams.CURRENCY, ServiceUtility.chkNull(currency.getText()).toString().trim());
			intent.putExtra(AvenuesParams.AMOUNT, ServiceUtility.chkNull(amount.getText()).toString().trim());
			
			intent.putExtra(AvenuesParams.REDIRECT_URL, ServiceUtility.chkNull(redirectUrl.getText()).toString().trim());
			intent.putExtra(AvenuesParams.CANCEL_URL, ServiceUtility.chkNull(cancelUrl.getText()).toString().trim());
			intent.putExtra(AvenuesParams.RSA_KEY_URL, ServiceUtility.chkNull(rsaKeyUrl.getText()).toString().trim());
			
			startActivity(intent);
		}else{
			showToast("All parameters are mandatory.");
		}
	}
	
	public void showToast(String msg) {
		Toast.makeText(this, "Toast: " + msg, Toast.LENGTH_LONG).show();
	}
} 