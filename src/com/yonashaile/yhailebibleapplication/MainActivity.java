package com.yonashaile.yhailebibleapplication;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity {

	
	private static final String PACKAGE_NAME = "com.yonashaile.yhailebibleapplication";
	ImageButton button1;
	ImageButton button2;
	Dialog dialog;
	boolean start = true;
	private String versionName = "1.0";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		PackageInfo info = null;
		try {
			info = getPackageManager().getPackageInfo(PACKAGE_NAME, 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	      int currentVersion = info.versionCode;
	      this.versionName  = info.versionName;
	      SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	      int lastVersion = prefs.getInt("version_code", 0);
	      if (currentVersion > lastVersion) {
	        prefs.edit().putInt("version_code", currentVersion).commit();
	       

		
		
		
		dialog = new Dialog(this);
    	dialog.setContentView(R.layout.instruction_dialog);
    	dialog.setTitle("Instructions:");
    	TextView instructions = (TextView) dialog.findViewById(R.id.instructionsTextView);
    	instructions.setText("Welcome to YHaileBibleApp!\n You can select from two translations on this page: The American Standard and King James Versions. From there you select from a list of the two Testemants: Old and New. From there you select a book, then a chapter. That chapters verses are displayed.  You can then select verses to add to a favorites/bookmarks list by pressing and holding on the particular verse. At any point in the Bible you can back up to the last section by pressing the back button in the top left corner of the app. You can also at any point go to the Bookmarks to look over your selections. It can be a useful tool to compare verses from different areas or translations in the Bible.  Once on the Boorkmarks Page you can delete individual bookmarks by pressing and holding on a specific verse or clear the list by pressing the Garbage Can or 'Clear all Bookmarks' menu option in the upper right corner of the screen. I hope you enjoy!");

    	Button button = (Button) dialog.findViewById(R.id.closeButton);
    	button.setOnClickListener(new OnClickListener() {  
    		@Override  
    		public void onClick(View view) {  
    			dialog.dismiss();            
    		}  
    	});

    	dialog.show();
	      }
		
		button1 = (ImageButton) findViewById(R.id.button1);
		button2 = (ImageButton) findViewById(R.id.button2);
		
		/*
         * @param View.OnClickListener
         */
        button1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

            	Intent intent = new Intent(v.getContext(), ASVersionActivity.class);
		    				
				// Start next activity
				startActivity(intent);
            
            }
		
        });
        
        /*
         * @param View.OnClickListener
         */
        button2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
            	
            	Intent intent = new Intent(v.getContext(), KJVersionActivity.class);
		    					
				// Start next activity
				startActivity(intent);
            	
            }
		
        });
	}
		
	@Override
	protected void onStart() {
	    super.onStart();
	    

	    	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override  
    public boolean onOptionsItemSelected(MenuItem item) {  
		
		 switch (item.getItemId()) {  
    
		 	case R.id.bookmark:
        	 
		 		Intent intent = new Intent(getBaseContext(), Bookmarks.class);
		 		// Start next activity
				startActivity(intent);
				return true;
                 	    
            // Help menu option that opens instruction dialog box 
		 	case R.id.help:
		 		final Dialog dialog = new Dialog(this);     	 
        	    dialog.setContentView(R.layout.instruction_dialog);
        	    dialog.setTitle("Instructions:");
        	    TextView instructions = (TextView) dialog.findViewById(R.id.instructionsTextView);
        	    instructions.setText("Welcome to YHaileBibleApp!\n You can select from two translations on this page: The American Standard and King James Versions. From there you select from a list of the two Testemants: Old and New. From there you select a book, then a chapter. That chapters verses are displayed.  You can then select verses to add to a favorites/bookmarks list by pressing and holding on the particular verse. At any point in the Bible you can back up to the last section by pressing the back button in the top left corner of the app. You can also at any point go to the Bookmarks to look over your selections. It can be a useful tool to compare verses from different areas or translations in the Bible.  Once on the Boorkmarks Page you can delete individual bookmarks by pressing and holding on a specific verse or clear the list by pressing the Garbage Can or 'Clear all Bookmarks' menu option in the upper right corner of the screen. I hope you enjoy!");

        	    Button button = (Button) dialog.findViewById(R.id.closeButton);
        	    button.setOnClickListener(new OnClickListener() {  
        	        @Override  
        	        public void onClick(View view) {  
        	            dialog.dismiss();            
        	        }  
        	    });

        	    dialog.show();
        		
        		return true;
        	default:
        	return super.onOptionsItemSelected(item); }
	}
}