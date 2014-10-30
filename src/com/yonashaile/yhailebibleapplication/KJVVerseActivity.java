package com.yonashaile.yhailebibleapplication;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class KJVVerseActivity extends Activity {

	int bookID;
    int chapterID;
    String bookName;
    private static final String DB_NAME = "KJV.sqlite";
	    
    // Set database field names as constants
	private static final String TABLE_NAME = "verse";
	private static final String ID = "id";
	private static final String BOOK_ID = "book_id";
	private static final String BOOK_CHAPTER = "chapter";
	private static final String BOOK_VERSE = "verse";
	private static final String BOOK_TEXT = "text";
	ActionMode.Callback modeCallBack;
	int position;
	private SQLiteDatabase database;
	private ArrayList<String> verses;
	private ListView kjvListView ;  
	ArrayList<Integer> chaptersHash;
	String aAdapt;
	 
	// Database related variables
	BookmarkDBHelper helper;
	SQLiteDatabase db;
	String itemValue;
		
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kjv_verse_layout);
				
		helper = new BookmarkDBHelper(this.getApplicationContext());
		db = helper.getWritableDatabase();

		// Find the ListView resource.   
	    kjvListView = (ListView) findViewById( R.id.kjv_verse_list ); 

	    // Open the database
	    MyDatabaseOpener dbOpener = new MyDatabaseOpener(getBaseContext(), DB_NAME);
	    database = dbOpener.openDataBase();
	    
	    loadDatabase();
	    
	    // Set title with appropriate Chapter
	    setTitle("Ch. " + chapterID);
	    
	    // ArrayAdapter for the listview of this activity
	    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	              android.R.layout.simple_list_item_1, android.R.id.text1, verses);
	    
	    // Assign adapter to ListView
	    kjvListView.setAdapter(adapter); 
	    kjvListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
	    	public boolean onItemLongClick (AdapterView parent, View view, int position, long id) {
	    		System.out.println("Long click");
	    		startActionMode(modeCallBack);
	    		itemValue = (String) kjvListView.getItemAtPosition(position);
	    		view.setSelected(true);
	    		return true;
	    	};
	    });
        
	    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
	    	// Use floating context menus on Froyo and Gingerbread
	    	registerForContextMenu(kjvListView);
	    } else {
	    	getActionBar().setDisplayHomeAsUpEnabled(true);
       
	    	modeCallBack = new ActionMode.Callback() {
        	   
	    	public boolean onPrepareActionMode(ActionMode mode, Menu menu){   
	    		return false;
	    	}
        	  
	    	public void onDestroyActionMode(ActionMode mode) {
        	    mode = null;  
	    	}
        	   
	    	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
	    		mode.getMenuInflater().inflate(R.menu.bookmark_context_menu, menu);
	    		return true;
	    	}
        	   
	    	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
	    		switch (item.getItemId()) {
   	            
	    		// Selecting verse to bookmark with a longpress
	    		case R.id.menu_item_bookmark_contact:
   	            	itemValue =  "(KJV)" + bookName + " " + String.valueOf(chapterID) + ":" + itemValue;
   	            	// Add a record.
   	            	ContentValues cv = new ContentValues();
   	             		
   	            	// Adds a value to the set: column, word to add
   	            	cv.put("Verse", itemValue); 
   	            	db.insert("Bookmarks", null, cv);
   	            	mode.finish();
   	            	adapter.notifyDataSetChanged();
   	            	return true;
        	   
	    		default:
	    			return false;
	    		}
	    	};
	    	};
	    }
     
	}
	
	// load database method
	private void loadDatabase() {
		chaptersHash = new ArrayList<Integer>();
		String totalVerse;
		
		// Receive values from previous activity and set to variable for use in this activity
		Intent mIntent = getIntent();
		bookID = (int)mIntent.getIntExtra("book", 0);
		chapterID = (int)mIntent.getIntExtra("chapter", 0);
		bookName = (String)mIntent.getStringExtra("bookName");
		chaptersHash = (ArrayList<Integer>)mIntent.getIntegerArrayListExtra("chapters");
            
		verses = new ArrayList<String>();
		Cursor testamentCursor = database.query(TABLE_NAME, new String[] {ID, BOOK_ID,  BOOK_CHAPTER, BOOK_VERSE, BOOK_TEXT}, null, null, null, null, BOOK_ID);
		testamentCursor.moveToFirst();
		if(!testamentCursor.isAfterLast()) {
			do {
				Integer bookInt = testamentCursor.getInt(1);
		    	int chapterNum = testamentCursor.getInt(2);
	    		
	    		if(bookID == bookInt && chapterNum == chapterID){
	    			int verseNum = testamentCursor.getInt(3);
	    			String verseText = testamentCursor.getString(4);
	    			
	    			if(verseNum < 10){
	    				totalVerse = "0" + String.valueOf(verseNum) + "  " + verseText;
	    		    	verses.add(totalVerse);
	    			}else {
	    			
	    			totalVerse = String.valueOf(verseNum) + "  " + verseText;
	    			verses.add(totalVerse);
	    			}
	    		}
			} while (testamentCursor.moveToNext());
		}
		testamentCursor.close();
	}

	// Inflate context menu
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		  
			getMenuInflater().inflate(R.menu.bookmark_context_menu, menu);
	}
		
	// Set Context menu item
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		int position = info.position;
			
		switch (item.getItemId()) {
		
			// Bookmark menu option
			case R.id.menu_item_bookmark_contact:
	            	
				// ListView Clicked item value
				String  itemValue    = (String) kjvListView.getItemAtPosition(position);
	                
				itemValue =  "(KJV)" + bookName + " " + String.valueOf(chapterID) + ":" + itemValue;
	                
				// Add a record.
				ContentValues cv = new ContentValues();
				
				// Adds a value to the set: column, word to add
				cv.put("Verse", itemValue); 
				db.insert("Bookmarks", null, cv);
				return true;
	          
		}
	        return super.onContextItemSelected(item);
	}
	    
	//  Perform any final cleanup before an activity is destroyed
	public void onDestroy()	{
		android.util.Log.v("YONAS","OnDestro CALLED");
		helper.close();  // Close any open database object
		super.onDestroy();
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
         
            // ActionBar up button
		 	case android.R.id.home:
		 		this.finish();
		 		return true;
         
		 	// Help menu item to open dialog box of apps instruction
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
        	        	
        	        	// Close dialog box
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