package com.techpalle.poc;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Frag3 extends Fragment {
	   EditText et1,et2,et3,et4,et5;
	     TextView tv1,tv2,tv3,tv4,tv0;
	     Button b;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.frag3, null);
		et1=(EditText) v.findViewById(R.id.editText1);
		et2=(EditText) v.findViewById(R.id.editText2);
		et3=(EditText) v.findViewById(R.id.editText3);
		et4=(EditText) v.findViewById(R.id.editText4);
		et5=(EditText) v.findViewById(R.id.editText5);
		tv0=(TextView) v.findViewById(R.id.textView1);
		tv1=(TextView) v.findViewById(R.id.textView0);
		tv2=(TextView) v.findViewById(R.id.textView3);
		tv3=(TextView) v.findViewById(R.id.textView4);
		tv4=(TextView) v.findViewById(R.id.textView5);
		b=(Button) v.findViewById(R.id.button1);
		Drawable originalDrawable = et1.getBackground();
		
		
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		/*		
			Editable abc=et1.getText();
				if(abc.equals("hai"))
				{
					 et1.setBackgroundResource(R.drawable.shape_edittext);
				}
				else
				{
                   et1.setBackgroundResource(R.drawable.edittext_red);
					
					Toast.makeText(getActivity(), "hai", 1).show();
				}
				
				*/
				
				
				
	
				
				if((et1.getText().toString()).equals("")){
					//Toast.makeText(getActivity(), "hai", 1).show();
					//et1.setError("Please Provide a number");
					 et1.setBackgroundResource(R.drawable.edittext_red);
					
				}
				
				
				 if((et2.getText().toString()).equals("")){
						
						//et2.setError("Please Provide a name");
					 et2.setBackgroundResource(R.drawable.edittext_red);
						
					}
		             if((et3.getText().toString()).equals("")){
						
						//et3.setError("Please Provide a email");
		        		 et3.setBackgroundResource(R.drawable.edittext_red);
						
					}
		             if((et4.getText().toString()).equals("")){
		 				
		 				//et4.setError("Please Provide a password");
		        		 et4.setBackgroundResource(R.drawable.edittext_red);
		 				
		 			}
		             if((et5.getText().toString()).equals("")){
		 				
		 				//et5.setError("Please Confirm password");
		        		 et5.setBackgroundResource(R.drawable.edittext_red);
		 				
		 			}
		             
		 			
		             if((et2.getText().toString()).matches("[A-Za-z]+")){
		  				
		  				
		            	 et2.setBackgroundResource(R.drawable.shape_edittext);
		  				
		  			}else{
		  				//et1.setError("Please Provide a valid number");
		  				et2.setText("");
		  				et2.setBackgroundResource(R.drawable.edittext_red);
		  				et2.setHint("please enter a valid last name");
		  				
		  			}
		              if((et1.getText().toString()).matches("[A-Za-z]+")){
		   				
		   				
		            	  et1.setBackgroundResource(R.drawable.shape_edittext);
		   				
		   			}else{
		   				//et2.setError("Name should not contain symbols or numbers ");
		   				et1.setText("");
		   				et1.setHint("please enter a valid  name");
		   				
		   				et1.setBackgroundResource(R.drawable.edittext_red);
		   			
		   			}
		              
		              
		              if((et3.getText().toString()).matches("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b")){
		     				
		     				
		            	  et3.setBackgroundResource(R.drawable.shape_edittext);
		     				
		    			}else{
		    				//et4.setError("password should not contain symbols");
		    				et3.setText("");
		    				et3.setBackgroundResource(R.drawable.edittext_red);
		    				et3.setHint("please enter a valid mail id");
		    			}    
				
		              
		              if((et4.getText().toString()).matches("[0-9]+")){
		  				
		            	  et4.setBackgroundResource(R.drawable.shape_edittext);
		                  
		  				
		              }
		   			
		   			
		              if((et5.getText().toString()).matches("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b")){
		     				
		     				
		            	  et5.setBackgroundResource(R.drawable.shape_edittext);
		     				
		    			}else{
		    				//et4.setError("password should not contain symbols");
		    				et5.setText("");
		    				et5.setBackgroundResource(R.drawable.edittext_red);
		    				et5.setHint("please enter a valid email id");
		    			}    
				
		              
		              
		              if((et2.getText().toString()).matches("[A-Za-z]+")&&(et1.getText().toString()).matches("[A-Za-z]+")
		            		  &&(et3.getText().toString()).matches("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b")
		            		  &&(et4.getText().toString()).matches("[0-9]+")
		            		  &&(et5.getText().toString()).matches("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b")
		            		  )
		              {
		            	  
		            	  
		            	 // Toast.makeText(getActivity(), "hai", 1).show();
		            	  
		            	  
		            	  tv0.setText("registering.....please wait");
		            	  tv0.setVisibility(View.VISIBLE);

						  CoursePurchaseActivity m=(CoursePurchaseActivity) getActivity();
						  m.signUpPressed(et1.getText().toString(), et2.getText().toString(), et3.getText().toString(),
								  et4.getText().toString(),  et5.getText().toString());

					  }
		              
		              
		              
		              
		              
	               /*
				if(et1.getError()==null&&et2.getError()==null&&et3.getError()==null&&et4.getError()==null&&et5.getError()==null&&et6.getError()==null){
					//Toast.makeText(RegisterActivity.this, "noerror", 1).show();
					
					int enumb=Integer.parseInt(enumbr);
					mdb.insert(enumb,ename,email, password, password2, pwhint);
									
									Intent in=new Intent(Register.this,Home.class);
									startActivity(in);
									Toast.makeText(Register.this, "registered", Toast.LENGTH_LONG).show();
								
				   
								    et1.setText("");
								    et2.setText("");
								    et3.setText("");
								    et4.setText("");
								    et5.setText("");
								    et6.setText("");
								    et1.requestFocus();
					
									
					
							}
			
				
				
				
				
				
				*/
				
				
				
		             }
			
		});
		
		
		
		tv2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				CoursePurchaseActivity m=(CoursePurchaseActivity) getActivity();
				m.pressed();
				
				
			}
		});
		
tv3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				CoursePurchaseActivity m=(CoursePurchaseActivity) getActivity();
				m.pressedprivacy();
				
				
			}
		});

		tv4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				CoursePurchaseActivity m=(CoursePurchaseActivity) getActivity();
				m.pressedprivacylogin();


			}
		});

		return v;
	}
	
}
