package com.techpalle.poc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.avenues.lib.utility.AvenuesParams;
import com.avenues.lib.utility.Constants;
import com.avenues.lib.utility.RSAUtility;
import com.avenues.lib.utility.ServiceHandler;
import com.avenues.lib.utility.ServiceUtility;

public class WebViewActivity extends Activity {
	private ProgressDialog dialog;
	Intent mainIntent;
	String html, encVal;
	private String course, course_id, email;
	private int purchaseMode;//0-sdcard, 1-pendrive, 2-both

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_webview);
		mainIntent = getIntent();
		if(mainIntent != null){
			course = mainIntent.getStringExtra("course");
			course_id = mainIntent.getStringExtra("course_id");
			email = mainIntent.getStringExtra("email");
			purchaseMode = mainIntent.getIntExtra("purchaseMode",-1);
		}
		// Calling async task to get display content
		new RenderView().execute();
	}
	
	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class RenderView extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			dialog = new ProgressDialog(WebViewActivity.this);
			dialog.setMessage("Please wait...");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();
	
			// Making a request to url and getting response
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(AvenuesParams.ACCESS_CODE, mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE)));
			params.add(new BasicNameValuePair(AvenuesParams.ORDER_ID, mainIntent.getStringExtra(AvenuesParams.ORDER_ID)));
	
			String vResponse = sh.makeServiceCall(mainIntent.getStringExtra(AvenuesParams.RSA_KEY_URL), ServiceHandler.POST, params);
            if(vResponse != null)
			    Log.d("CC-AVENUE", vResponse);
			if (vResponse != null)
			    System.out.println(vResponse);
			//satish start
			String root_sd = Environment.getExternalStorageDirectory().toString();
			File file = new File(root_sd+"/response.txt");
			FileWriter fw;
			try {
				fw = new FileWriter(file);
                if(vResponse != null)
				    fw.write(vResponse);
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			//remove html code from response and take out only encrypted key.
			if(vResponse != null){
				int pos = vResponse.indexOf("<");
				if(pos != -1){
					vResponse = vResponse.substring(0, pos);
					vResponse.trim();
				}
			}
			//satish end
			
			if(!ServiceUtility.chkNull(vResponse).equals("") 
					&& ServiceUtility.chkNull(vResponse).toString().indexOf("ERROR")==-1){
				StringBuffer vEncVal = new StringBuffer("");
				vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT, mainIntent.getStringExtra(AvenuesParams.AMOUNT)));
				vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CURRENCY, mainIntent.getStringExtra(AvenuesParams.CURRENCY)));
				encVal = RSAUtility.encrypt(vEncVal.substring(0,vEncVal.length()-1), vResponse);
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (dialog.isShowing())
				dialog.dismiss();
			
			@SuppressWarnings("unused")
			class MyJavaScriptInterface
			{
				@JavascriptInterface
			    public void processHTML(String html)
			    {
					//satish start
                    Log.d("SATISH-process",html);
                    //store transaction response in user mobile in response2.txt
					String root_sd = Environment.getExternalStorageDirectory().toString();
					File file = new File(root_sd+"/response2.txt");
					FileWriter fw;
					try {
						fw = new FileWriter(file);
						fw.write(html);
						fw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

                    //store transaction details in database. Below is sample transaction response html data
                    /*
                    <head><head></head><body>order_id = 4061974<br>tracking_id = 105026500004<br>bank_ref_no = 160017411827<br>
                    order_status = Success<br>failure_message = <br>payment_mode = Net Banking<br>card_name = HDFC Bank<br>
                    status_code = null<br>status_message = null<br>currency = INR<br>amount = 2.0<br>billing_name = VARALAKSHMI satish<br>
                    billing_address = S. Uppalapadu, jammala madugu, kadapa<br>billing_city = Bommanahalli<br>billing_state = Kannada<br>
                    billing_zip = 560068<br>billing_country = India<br>billing_tel = 9740588499<br>billing_email = satish.ice@gmail.com<br>
                    delivery_name = VARALAKSHMI satish<br>delivery_address = S. Uppalapadu, jammala madugu, kadapa<br>
                    delivery_city = Bommanahalli<br>delivery_state = Kannada<br>delivery_zip = 560068<br>delivery_country = India<br>
                    delivery_tel = 9740588499<br>merchant_param1 = <br>merchant_param2 = <br>merchant_param3 = <br>merchant_param4 = <br>
                    merchant_param5 = <br>vault = N<br>offer_type = null<br>offer_code = null<br>discount_value = 0.0<br>mer_amount = 2.0<br>
                    eci_value = null<br>
                    */

                    StringBuilder sb = new StringBuilder(html);
                    CCAvenueData c = new CCAvenueData();
                    String[] search = {"order_id",
                        "tracking_id",
                        "bank_ref_no", "order_status" , "failure_message" ,
                        "payment_mode" , "card_name" , "status_code" , "status_message" ,
                        "currency" , "amount" , "billing_name" , "billing_address" ,
                        "billing_city" , "billing_state" , "billing_zip" ,
                        "billing_country" , "billing_tel" , "billing_email" ,
                        "delivery_name" , "delivery_address" , "delivery_city" ,
                        "delivery_state" , "delivery_zip" , "delivery_country" ,
                        "delivery_tel" , "merchant_param1" , "merchant_param2" ,
                        "merchant_param3" , "merchant_param4" , "merchant_param5" ,
                        "vault" , "offer_type" , "offer_code" , "discount_value" ,
                        "mer_amount" , "eci_value"};


                    Intent in = new Intent();
                    in.setAction("CCAVENUE_TRANSACTION_STATUS");

                    for(int i=0; i<search.length; i++){
                        switch(search[i]){
                            case "order_id":
								String order_id = getValue(sb, search[i]);
								c.setOrder_id(order_id);
                                in.putExtra("order_id",order_id);//for tracking in purchase screen
                                break;
                            case "tracking_id":
								String tracking_id = getValue(sb, search[i]);
								c.setTracking_id(tracking_id);
								break;
                            case "bank_ref_no":
								String bank_ref_no = getValue(sb, search[i]);
								c.setBank_ref_no(bank_ref_no);
								break;
                            case "order_status":
								String order_status = getValue(sb, search[i]);
								c.setOrder_status(order_status);
								break;
                            case "failure_message":
								String failure_message = getValue(sb, search[i]);
								c.setFailure_message(failure_message);
								break;
                            case "payment_mode":
								String payment_mode = getValue(sb, search[i]);
								c.setPayment_mode(payment_mode);
								break;
                            case "card_name":
								String card_name = getValue(sb, search[i]);
								c.setCard_name(card_name);
								break;
                            case "status_code":
								String status_code = getValue(sb, search[i]);
								c.setStatus_code(status_code);
								break;
                            case "status_message":
								String status_message = getValue(sb, search[i]);
								c.setStatus_message(status_message);
								break;
                            case "currency":
								String currency = getValue(sb, search[i]);
								c.setCurrency(currency);
								break;
                            case "amount":
								String amount = getValue(sb, search[i]);
								c.setAmount(amount);
								break;
                            case "billing_name":
								String billing_name = getValue(sb, search[i]);
								c.setBilling_name(billing_name);
								break;
                            case "billing_address":
								String billing_address = getValue(sb, search[i]);
								c.setBilling_address(billing_address);
								break;
                            case "billing_city":
								String billing_city = getValue(sb, search[i]);
								c.setBilling_city(billing_city);
								break;
                            case "billing_state":
								String billing_state = getValue(sb, search[i]);
								c.setBilling_state(billing_state);
								break;
                            case "billing_zip":
								String billing_zip = getValue(sb, search[i]);
								c.setBilling_zip(billing_zip);
								break;
                            case "billing_country":
								String billing_country = getValue(sb, search[i]);
								c.setBilling_country(billing_country);
								break;
                            case "billing_tel":
								String billing_tel = getValue(sb, search[i]);
								c.setBilling_tel(billing_tel);
								break;
                            case "billing_email":
								String billing_email = getValue(sb, search[i]);
								c.setBilling_email(billing_email);
								break;
                            case "delivery_name":
								String delivery_name = getValue(sb, search[i]);
								c.setDelivery_name(delivery_name);
								break;
                            case "delivery_address":
								String delivery_address = getValue(sb, search[i]);
								c.setDelivery_address(delivery_address);
								break;
                            case "delivery_city":
								String delivery_city = getValue(sb, search[i]);
								c.setDelivery_city(delivery_city);
								break;
                            case "delivery_state":
								String delivery_state = getValue(sb, search[i]);
								c.setDelivery_state(delivery_state);
								break;
                            case "delivery_zip":
								String delivery_zip = getValue(sb, search[i]);
								c.setDelivery_zip(delivery_zip);
								break;
                            case "delivery_country":
								String delivery_country = getValue(sb, search[i]);
								c.setDelivery_country(delivery_country);
								break;
                            case "delivery_tel":
								String delivery_tel = getValue(sb, search[i]);
								c.setDelivery_tel(delivery_tel);
								break;
                            case "merchant_param1":
								String merchant_param1 = getValue(sb, search[i]);
								c.setMerchant_param1(merchant_param1);
								break;
                            case "merchant_param2":
								String merchant_param2 = getValue(sb, search[i]);
								c.setMerchant_param2(merchant_param2);
								break;
                            case "merchant_param3":
								String merchant_param3 = getValue(sb, search[i]);
								c.setMerchant_param3(merchant_param3);
								break;
                            case "merchant_param4":
								String merchant_param4 = getValue(sb, search[i]);
								c.setMerchant_param4(merchant_param4);
								break;
                            case "merchant_param5":
								String merchant_param5 = getValue(sb, search[i]);
								c.setMerchant_param5(merchant_param5);
								break;
                            case "vault":
								String vault = getValue(sb, search[i]);
								c.setVault(vault);
								break;
                            case "offer_type":
								String offer_type = getValue(sb, search[i]);
								c.setOffer_type(offer_type);
								break;
                            case "offer_code":
								String offer_code = getValue(sb, search[i]);
								c.setOffer_code(offer_code);
								break;
                            case "discount_value":
								String discount_value = getValue(sb, search[i]);
								c.setDiscount_value(discount_value);
								break;
                            case "mer_amount":
								String mer_amount = getValue(sb, search[i]);
								c.setMer_amount(mer_amount);
								break;
                            case "eci_value":
								String eci_value = getValue(sb, search[i]);
								c.setEci_value(eci_value);
								break;
                        }
                    }

                    /*if(sb.indexOf("order_id") != -1){
                        int pos = sb.indexOf("order_id");
                        int equalspos = sb.indexOf("=", pos);
                        if(equalspos != -1){
                            int brpos = sb.indexOf("<br>", equalspos);
                            if(brpos != -1){
                                String order_id = sb.substring(equalspos+1, brpos);
                            }
                        }
                    }*/

					c.setPalle_course_name(course);
					c.setPalle_course_id(course_id);
					c.setPalle_email(email);

                    PalleDatabase pdb = new PalleDatabase(WebViewActivity.this);
                    pdb.open();
                    long id = pdb.insertCCAvenue(c);
					Log.d("CCAVENUE","ROW INSERTED.."+id);
                    pdb.close();

					//satish end

					// process the html as needed by the app
			    	String status = null;
			    	if(html.indexOf("Failure")!=-1){
			    		status = "Transaction Declined!";
			    	}else if(html.indexOf("Success")!=-1){
			    		status = "Transaction Successful!";
			    	}else if(html.indexOf("Aborted")!=-1){
			    		status = "Transaction Cancelled!";
			    	}else{
			    		status = "Status Not Known!";
			    	}
			    	//Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
			    	//Intent intent = new Intent(getApplicationContext(),StatusActivity.class);//satish commented
					//intent.putExtra("transStatus", status); //satish commented
					//startActivity(intent);//satish commented

                    in.putExtra("status",status);
                    sendBroadcast(in);

                    finish();//close current web activity
			    }
			}
			
			final WebView webview = (WebView) findViewById(R.id.webview);
			webview.getSettings().setJavaScriptEnabled(true);
			webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
			webview.setWebViewClient(new WebViewClient(){
				@Override  
	    	    public void onPageFinished(WebView view, String url) {
	    	        super.onPageFinished(webview, url);
					//satish start
					/*
					if url is palleuniversity ccavenue response aspx, capture data and
					store it in database
					 */
					if(url.equals("http://palleuniversity.com/merchant/ccavResponseHandler.aspx")){
                        Log.d("SATISH-PAGEDONE","palle came.."+url);
					}
					//satish end
	    	        if(url.indexOf("/ccavResponseHandler.aspx")!=-1){
	    	        	webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
	    	        }
	    	    }  

	    	    @Override
	    	    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
	    	        Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
	    	    }
			});
			
			/* An instance of this class will be registered as a JavaScript interface */
			StringBuffer params = new StringBuffer();
			params.append(ServiceUtility.addToPostParams(AvenuesParams.ACCESS_CODE,mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE)));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_ID,mainIntent.getStringExtra(AvenuesParams.MERCHANT_ID)));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.ORDER_ID,mainIntent.getStringExtra(AvenuesParams.ORDER_ID)));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.REDIRECT_URL,mainIntent.getStringExtra(AvenuesParams.REDIRECT_URL)));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.CANCEL_URL,mainIntent.getStringExtra(AvenuesParams.CANCEL_URL)));
            if(encVal != null)//satish adding check for no internet
			    params.append(ServiceUtility.addToPostParams(AvenuesParams.ENC_VAL,URLEncoder.encode(encVal)));
			
			//showToast("some thing went wrong");
			
			String vPostParams = params.substring(0,params.length()-1);
			try {
				webview.postUrl(Constants.TRANS_URL, EncodingUtils.getBytes(vPostParams, "UTF-8"));
			} catch (Exception e) {
				showToast("Exception occured while opening webview.");
			}
		}
	}
	
	public void showToast(String msg) {
		Toast.makeText(this, "Toast: " + msg, Toast.LENGTH_LONG).show();
	}

    //satish start
    public String getValue(StringBuilder sb, String tag){
        if(sb.indexOf(tag) != -1){
            int pos = sb.indexOf(tag);
            int equalspos = sb.indexOf("=", pos);
            if(equalspos != -1){
                int brpos = sb.indexOf("<br>", equalspos);
                if(brpos != -1){
                    String value = sb.substring(equalspos+1, brpos);
                    return value;
                }
            }
        }
        return null;
    }
    //satish end
} 