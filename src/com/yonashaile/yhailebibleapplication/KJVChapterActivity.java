package com.yonashaile.yhailebibleapplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

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

public class KJVChapterActivity extends Activity {


    
	 private static final String DB_NAME = "KJV.sqlite";
	 private static final String TABLE_NAME = "verse";
	 private static final String ID = "id";
	 private static final String BOOK_ID = "book_id";
	 private static final String BOOK_CHAPTER = "chapter";
	 private static final String BOOK_VERSE = "verse";
	 private static final String BOOK_TEXT = "text";
	
	 private SQLiteDatabase database;
	 private ArrayList<Integer> chapters;
	 private ArrayList<Integer> chaptersHash;
	 private ListView kjvListView ;  
	 int passedBookID;
	 String bookName;
	 int bookID;
	 Intent mIntent2;
	
	 @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kjv_activity_layout);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	           getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
	    mIntent2 = getIntent();
		
        // Find the ListView resource.   
	    kjvListView = (ListView) findViewById( R.id.kjv_list ); 
	    
	    // Open the database    
	    MyDatabaseOpener dbOpener = new MyDatabaseOpener(getBaseContext(), DB_NAME);
	    database = dbOpener.openDataBase();
	    
	    // Call load database method and set return value as the passedBookID
	    passedBookID = loadDatabase();
		
	    // Use HashSet to eliminate duplicates
	    HashSet<Integer> hs = new HashSet<Integer>();
	    chaptersHash = new ArrayList<Integer>();
	    for(int i = 0; i < chapters.size(); i++) {
	    	
	    	hs.add(chapters.get(i));
	    	
	    	
	    }
	    
	    for (int s : hs) {
	        chaptersHash.add(s);
	    }
	    
	    
	    setTitle(bookName);
	    
	    // Sort ArrayList
	    Collections.sort(chaptersHash);
	    
	    // ArrayAdapter for listview
	    ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,
	              android.R.layout.simple_list_item_1, android.R.id.text1, chaptersHash);
	    
	    // Assign adapter to ListView
     kjvListView.setAdapter(adapter); 
     
     // ListView Item Click Listener
     kjvListView.setOnItemClickListener(new OnItemClickListener() {

           public void onItemClick(AdapterView<?> parent, View view,
              int position, long id) {
             
            // ListView Clicked item index
            int itemPosition     = position;
            
            itemPosition     = itemPosition + 1;
            
            // Pass variables to next activity          
            new Bundle();
            Intent intent = new Intent(view.getContext(), KJVVerseActivity.class);
            
            intent.putExtra("chapter", itemPosition);
            intent.putExtra("book", passedBookID);
            intent.putExtra("bookName", bookName);
            intent.putExtra("chapters", chaptersHash);

            // Start next activity
			startActivity(intent);
           
           }

     }); 
           
	 }
		
	 @SuppressWarnings("unchecked")
	 private int loadDatabase() {
		 
		 Intent mIntent = getIntent();
		 bookID = (int)mIntent.getIntExtra("book", 0);
		 bookName = (String)mIntent.getStringExtra("bookText");
		 bookName = (String)mIntent.getStringExtra("bookText");
		 
		 bookID = (int)mIntent2.getIntExtra("book", 0);

		 chapters = new ArrayList<Integer>();
		 Cursor testamentCursor = database.query(TABLE_NAME, new String[] {ID, BOOK_ID,  BOOK_CHAPTER, BOOK_VERSE, BOOK_TEXT}, null, null, null, null, BOOK_ID);
		 testamentCursor.moveToFirst();
		 
		 if(!testamentCursor.isAfterLast()) {
	    	do {
	    		Integer bookInt = testamentCursor.getInt(1);
	    		
	    		if(bookID == bookInt){
	    			int chapter = testamentCursor.getInt(2);
	    			chapters.add(chapter);}
	    	} while (testamentCursor.moveToNext());
		 }
		 testamentCursor.close();
		 return bookID;
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
         
         // ActionBar up button
         case android.R.id.home:
        	this.finish();
        	return true;
        	
        // Help menu option to open up instruction dialog box	
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