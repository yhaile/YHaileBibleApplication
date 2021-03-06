package com.yonashaile.yhailebibleapplication;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class KJVersionActivity extends Activity {

	// Database field names set as constant variables
	 private static final String DB_NAME = "KJV.sqlite";
	 private static final String TABLE_NAME = "testament";
	 private static final String TESTAMENT_ID = "id";
	 private static final String TESTAMENT_TESTAMENT = "name";
	 private SQLiteDatabase database;
	 
	 // ArrayList for listview
	 private ArrayList<String> testaments;
	 private ListView kjvListView ;  
	 
	 @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kjv_activity_layout);
	
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			
			// ActionBar up button
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
		
		// Find the ListView resource.   
	    kjvListView = (ListView) findViewById( R.id.kjv_list ); 
	  	      
	    MyDatabaseOpener dbOpener = new MyDatabaseOpener(getBaseContext(), DB_NAME);
	    database = dbOpener.openDataBase();
	    //That�s it, the database is open!
	    loadDatabase();
	    setTitle("KJV");
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, testaments);
	    
	    // Assign adapter to ListView
	    kjvListView.setAdapter(adapter); 
       
       // ListView Item Click Listener
       kjvListView.setOnItemClickListener(new OnItemClickListener() {

             public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
               
              // ListView Clicked item index
              int itemPosition     = position;
              
              kjvListView.getItemAtPosition(position);
              
              // Pass necessary variables to next activity
              Bundle bundle = new Bundle();
              Intent intent = new Intent(view.getContext(), KJVBookActivity.class);
              
              intent.putExtras(bundle);
              intent.putExtra("testament", itemPosition);
		    					
              // Start next activity
              startActivity(intent);
           
             }

       }); 
	 }
	
	 // Method loads database
	 @SuppressWarnings("unchecked")
	 private void loadDatabase() {
		 testaments = new ArrayList<String>();
		 Cursor testamentCursor = database.query(TABLE_NAME, new String[] {TESTAMENT_ID, TESTAMENT_TESTAMENT}, null, null, null, null, TESTAMENT_TESTAMENT);
	    	testamentCursor.moveToFirst();
	    	if(!testamentCursor.isAfterLast()) {
	    		do {
	    			String name = testamentCursor.getString(1);
	    			testaments.add(name);
	    		} while (testamentCursor.moveToNext());
	    	}
	    	testamentCursor.close();
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
	 
			    // Bookmark menu option
			 	case R.id.bookmark:
	        	 
			 		Intent intent = new Intent(getBaseContext(), Bookmarks.class);
			 		// Start next activity
					startActivity(intent);
					return true;
	         
			 	// Action bar up button
			 	case android.R.id.home:
	        	 
			 		this.finish();
			 		return true;
	        	
			 	// Help menu option that opens instructions dialog box
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
	        	        	
	        	        	// close dialog box
	        	            dialog.dismiss();            
	        	        }  
	        	    });

	        	    dialog.show();
	        		
	        		return true;
			 	case R.id.back_to_main:
			    	
			 		Intent intentBack = new Intent(getBaseContext(), MainActivity.class);
			 		// Start next activity
			 		startActivity(intentBack);
			 		return true;
	        	default:
	        	return super.onOptionsItemSelected(item); 
	        	    
			 }
			 
	 }

}