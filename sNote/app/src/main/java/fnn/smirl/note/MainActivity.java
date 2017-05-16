package fnn.smirl.note;

import android.view.*;
import fnn.smirl.note.util.*;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import fnn.smirl.simple.Serializer;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import android.support.v7.widget.*;

public class MainActivity extends AppCompatActivity implements OnClickListener {
 static NoteRVAdapter adapter ;
 public static Context ctx;
 public static String filename;
 private static String criteria = "";
 private static Resources res;
 public static Activity ACTIVITY;
 private FloatingActionButton fab;
 public static Memos memos = new Memos();
 public static Serializer serializer =
 new Serializer();

 private static RecyclerView note_rv = null;
 AppCompatEditText heading = null;

 @Override
 protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);
	ctx = getApplicationContext();
	res = getResources();
	ACTIVITY = this;
	setupToolbar();
  setupFab();

	filename = getFilesDir() + "/snote.json";
	
	heading = (AppCompatEditText)findViewById(R.id.heading);
	heading.setOnEditorActionListener(new OnEditorActionListener(){

		@Override
		public boolean onEditorAction(TextView p1, int p2, KeyEvent p3) {
		 criteria = heading.getText().toString().trim();
		 fill_list(criteria);
		 return true;
		} 
	 });

	note_rv = (RecyclerView)findViewById(R.id.note_rv);
	 note_rv.setHasFixedSize(true);

	 LinearLayoutManager llm = new LinearLayoutManager(ctx);
	 llm.setOrientation(LinearLayoutManager.VERTICAL);
	 note_rv.setLayoutManager(llm);
	 

//	LayoutInflater inflater = getLayoutInflater();
//	ViewGroup heady = (ViewGroup)inflater.inflate(R.layout.list_header,
//																								note_rv, false);
//	note_rv.addHeaderView(heady, null, false);
//
	retrieveRecord();
	fill_list(criteria);
	adapter = new NoteRVAdapter(ctx);
	
	note_rv.setAdapter(adapter);
 }

 private static void fill_list(String criteria) {
	memos.subList(criteria);
	Collections.sort(memos.subList);
 }


 private void setupToolbar() {
	Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
	setSupportActionBar(toolbar);
	final ActionBar ab = getSupportActionBar();
	ab.setHomeAsUpIndicator(R.drawable.notepad);
	ab.setDisplayHomeAsUpEnabled(true);
	try {
	 PackageInfo info = getPackageManager()
		.getPackageInfo(getPackageName(), 0);
	 ab.setSubtitle(res.getString(R.string.par) + " " + getResources().getString(R.string.author));
	 ab.setTitle(res
							 .getString(R.string.app_name) + " " + info.versionName + info.versionCode);
	}
	catch (PackageManager.NameNotFoundException e) {}

 }

 @Override
 public void onClick(View p1) {
	// TODO: Implement this method
	if (p1.getId() == R.id.fab) {
	 newMemo();
	}
 }

 private void setupFab() {
	fab = (FloatingActionButton)findViewById(R.id.fab);
	fab.setOnClickListener(this);
 }


 @Override
 public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.main_menu, menu);
	return true;
 }

 @Override
 public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	 case R.id.m_m_new:
		newMemo();
		break;
	 case R.id.m_m_about:
		displayInfo(this, getResources().getString(R.string.in_construction));
		break;
	 case R.id.m_m_exit:
		finish();
		break;	
	}
	return true;
 }

 private void newMemo() {
	Intent intent = new Intent(this, NoteActivity.class);
	intent.putExtra("date_id", 0);
	intent.putExtra("new", true);
	startActivity(intent);
 }

 /*** treating in json files */
 public static void store() {
	serializer.serialize(filename, memos, Memos.class);
	adapter.notifyDataSetChanged();
	displayInfo(ACTIVITY, "Mise à jour complète");
 }

 private void retrieveRecord() {
	File f = new File(filename);
	if(!f.exists()){
	 memos = new Memos();
	 memos.add(new Memo(0, "Sample", "Sample", false));
	 store();
	}
	try{
	memos = serializer.deserialize(filename, Memos.class);
	}
catch (Exception e) {
 
 retrieveRecord();
}
 }


 public static void addRecord(Memo memo) {
	memos.add(memo);
	store();

	adapter.notifyDataSetChanged();
 }



 public static void deleteRecord(Memo memo) {
	memos.remove(memo);
	store();
	adapter.notifyDataSetChanged();
 }

 public static void replaceRecord(Memo oldMemo, Memo newMemo) {
	memos.replace(oldMemo, newMemo);
	store();
	adapter.notifyDataSetChanged();
	fill_list(criteria);
 }

 public static void displayInfo(Activity activ, String info) {
	Snackbar
	 .make(activ.findViewById(R.id.root_layout),
				 info,
				 Snackbar.LENGTH_SHORT)
	 .setAction("Action", new OnClickListener(){

		 @Override
		 public void onClick(View p1) {
			 // TODO: Implement this method
		 }

		 
	 })
	 .show();
 }
}
