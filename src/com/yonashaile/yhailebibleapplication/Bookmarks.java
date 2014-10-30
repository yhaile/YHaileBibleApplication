package com.yonashaile.yhailebibleapplication;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class Bookmarks extends Activity {

	ActionMode.Callback modeCallBack;
	String aAdapt;
String itemValue;
    
	//Database related variables
		BookmarkDBHelper helper;
private SQLiteDatabase rawVersesDB;
private ArrayList<String> bookmarkVerses;
 private ListView bookmarksList ; 
 
 BookmarkDBHelper helper2;
 BookmarkDBHelper helper3;
 private SQLiteDatabase rawVersesDB2;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.bookmarks_layout);
	
	bookmarkVerses = new ArrayList<String>();
	helper = new BookmarkDBHelper(this.getApplicationContext());
	rawVersesDB = helper.getWritableDatabase();
	
	Cursor result = rawVersesDB.rawQuery("Select * from Bookmarks", null);  
	result.moveToFirst();
	
	//Loop thru and get all of the records
	Cursor cursor = getWordsCursor();

	while (cursor.moveToNext()){
		
		 
		bookmarkVerses.add(cursor.getString(1));	
 	}
	
	
	
	// Find the ListView resource.   
    bookmarksList = (ListView) findViewById( R.id.bookmarks_list ); 
  	      
    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
              android.R.layout.simple_list_item_1, android.R.id.text1, bookmarkVerses);
    
    // Assign adapter to ListView
    bookmarksList.setAdapter(adapter); 


    // Select bookmark listview item with a longpress
    bookmarksList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
    	public boolean onItemLongClick (AdapterView parent, View view, int position, long id) {
    		
    		startActionMode(modeCallBack);
    		itemValue = (String) bookmarksList.getItemAtPosition(position);
    		view.setSelected(true);
    		return true;
    	};
    });
  

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
    	
    	// Floating Context menu for lower than Honeycomb
    	registerForContextMenu(bookmarksList);
    } else {
    	
    	// ActionBar Context menu
    	modeCallBack = new ActionMode.Callback() {
    	   
    	public boolean onPrepareActionMode(ActionMode mode, Menu menu){   
    	    return false;
    	}
    	  
    	public void onDestroyActionMode(ActionMode mode) {
    	    mode = null;  
    	}
    	   
    	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
    		mode.getMenuInflater().inflate(R.menu.delete_bookmarks_context_menu, menu);
    		return true;
    	}
    	   
    	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
    		switch (item.getItemId()) {
    		
    		// Delete individual bookmark verse selected with a long press
    		case R.id.menu_item_delete_bookmark_contact:
	                	
    			helper2 = new BookmarkDBHelper(getApplicationContext());
    			rawVersesDB2 = helper2.getWritableDatabase();
    			
    			// Identify which verse to delete from database of bookmarks by matching it to listview item value
    			rawVersesDB2.delete("Bookmarks", "Verse='" + itemValue + "'", null);  
    			onCreate(null);
    			mode.finish();
    			adapter.notifyDataSetChanged();
    			return true;
	         	   
     		   	default:
     		   		return false;
    		}};
    	};
    }

}
	
 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.clear_bookmarks, menu);
		return true;
	
	}

	@Override  
	public boolean onOptionsItemSelected(MenuItem item) {  
	
	 switch (item.getItemId()) {  
	
	 	// Clear all bookmarks menu item
	 	case R.id.clear:
    	 
	 		helper2 = new BookmarkDBHelper(this.getApplicationContext());
	 		rawVersesDB2 = helper2.getWritableDatabase();
	 		
	 		// Delete all bookmarks from database
	 		rawVersesDB2.delete("Bookmarks", null, null);  
	 		onCreate(null);
	 		return true;
     
	 	// Back to main activity menu option 
	 	case R.id.back_to_main:
    	
	 		Intent intent = new Intent(getBaseContext(), MainActivity.class);
	 		// Start next activity
	 		startActivity(intent);
	 		return true;
    
	 	// Help menu option to open instruction dialog box
	 	case R.id.help:
    	 
	 		final Dialog dialog = new Dialog(this);
    	    dialog.setContentView(R.layout.instruction_dialog);
    	    dialog.setTitle("Instructions:");
    	    TextView instructions = (TextView) dialog.findViewById(R.id.instructionsTextView);
    	    instructions.setText("Welcome to YHaileBibleApp!\n You can select from two translations on this page: The American Standard and King James Versions. From there you select from a list of the two Testemants: Old and New. From there you select a book, then a chapter. That chapters verses are displayed.  You can then select verses to add to a favorites/bookmarks list by pressing and holding on the particular verse. At any point in the Bible you can back up to the last section by pressing the back button in the top left corner of the app. You can also at any point go to the Bookmarks to look over your selections. It can be a useful tool to compare verses from different areas or translations in the Bible.  Once on the Boorkmarks Page you can delete individual bookmarks by pressing and holding on a specific verse or clear the list by pressing the Garbage Can or 'Clear all Bookmarks' menu option in the upper right corner of the screen. I hope you enjoy!");

    	    Button button = (Button) dialog.findViewById(R.id.closeButton);
    	    button.setOnClickListener(new OnClickListener() {  
    	        
    	    	// Close dialog box
    	    	@Override  
    	        public void onClick(View view) {  
    	            dialog.dismiss();            
    	        }  
    	    });

    	    dialog.show();
    	    return true;
    	    
    	 // ActionBar up button
    	 	 	case android.R.id.home:
    	 	 		this.finish();
    	 	 		return true;
    	
	 	default:
	 		return super.onOptionsItemSelected(item); 
    	    
	 }

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
  
		getMenuInflater().inflate(R.menu.delete_bookmarks_context_menu, menu);
	}

	// Set Context menu item
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		int position = info.position;
    
		switch (item.getItemId()) {
		
			// Delete individual bookmarks menu option
        	case R.id.menu_item_delete_bookmark_contact:
        		String  itemValue    = (String) bookmarksList.getItemAtPosition(position);
        		helper2 = new BookmarkDBHelper(this.getApplicationContext());
        		rawVersesDB2 = helper2.getWritableDatabase();
        		rawVersesDB2.delete("Bookmarks", "Verse='" + itemValue + "'", null);  
        	    onCreate(null);
        	    return true;
        	          
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * @return
	 */
	@SuppressWarnings("deprecation")
	 private Cursor getWordsCursor(){
	 	Cursor cursor = rawVersesDB.query("Bookmarks", null, null, null, null, null, null);
	 	startManagingCursor(cursor); 
	 	return cursor;
	 }
 	
	//  Perform any final cleanup before an activity is destroyed
	public void onDestroy()	{
		android.util.Log.v("YONAS","OnDestro CALLED");
		helper.close();
		super.onDestroy();
	}
}