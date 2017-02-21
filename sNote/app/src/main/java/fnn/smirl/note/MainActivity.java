package fnn.smirl.note;

import android.view.*;
import android.widget.*;
import java.io.*;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView.OnEditorActionListener;
import fnn.smirl.note.util.Memo;
import fnn.smirl.note.util.NoteAdapter;
import java.util.ArrayList;
import java.util.Collections;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class MainActivity extends Activity {
 static JSONArray jsonArray;
 static JSONParser parser;
 static NoteAdapter adapter ;
 public static Context ctx;
 public static String filename;
 private static String criteria = "";
 private static Resources res;
 public static Activity ACTIVITY;
 
 private static ListView note_list = null;
 EditText heading = null;

 @Override
 protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	 ActionBar actionBar = getActionBar();
	 ctx = getApplicationContext();
	 res = getResources();
	 ACTIVITY = this;
	 try {
		PackageInfo info = getPackageManager()
		 .getPackageInfo(getPackageName(), 0);
		actionBar.setSubtitle(res.getString(R.string.par) + " " + getResources().getString(R.string.author));
		actionBar.setTitle(res
											 .getString(R.string.app_name) + " " + info.versionName + info.versionCode);
	 }
	 catch (PackageManager.NameNotFoundException e) {}
	}
	filename = getFilesDir() + "/snote.json";
	jsonArray = new JSONArray();
	parser = new JSONParser();
	heading = (EditText)findViewById(R.id.heading);
	heading.setOnEditorActionListener(new OnEditorActionListener(){

		@Override
		public boolean onEditorAction(TextView p1, int p2, KeyEvent p3) {
		 criteria = heading.getText().toString().trim();
		 fill_list(criteria);
		 return true;
		} 
	 });

	note_list = (ListView)findViewById(R.id.note_list);

	LayoutInflater inflater = getLayoutInflater();
	ViewGroup heady = (ViewGroup)inflater.inflate(R.layout.list_header,
																								note_list, false);
	note_list.addHeaderView(heady, null, false);

	retrieveRecord();
	fill_list(criteria);
 }

 private static void fill_list(String criteria) {

	ArrayList<Memo> list = new ArrayList<Memo>();
	list.clear();
	filterList(list, criteria);
	if (list.isEmpty()) {
	 list.add(new Memo(0, res.getString(R.string.empty_list), "", false));
	}
	Collections.sort(list);
	if (adapter!=null)adapter.clear();
	adapter = new NoteAdapter(ctx, list);
	note_list.setAdapter(adapter);
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
		Toast.makeText(this, getResources().getString(R.string.in_construction),
									 Toast.LENGTH_LONG).show();
		break;
	 case R.id.m_m_exit:
		finish();
		break;	
	}
	return true;
 }

 private void newMemo() {
	Intent intent = new Intent(this, NoteActivity.class);
	intent.putExtra(getResources().getString(R.string.header), "");
	intent.putExtra(getResources().getString(R.string.body), "");
	intent.putExtra("done", false);
	intent.putExtra("date_id", 0);
	startActivity(intent);
 }
 
 /*** treating in json files */
 static void store() {

	FileWriter w;
	try {
	 w = new FileWriter(filename);
	 w.write(jsonArray.toJSONString());
	 w.flush();
	}
	catch (IOException e) {}
 }

 private void retrieveRecord() {
	try {
	 Object obj = parser.parse(new FileReader(filename));
	 jsonArray.clear();
	 jsonArray = (JSONArray)obj;
	}
	catch (Exception e) {}
 }


 public static void addRecord(Memo memo) {
	if(jsonArray.contains(toJSONObject(memo))){
	 adapter.remove(memo);
	 }
	jsonArray.add(toJSONObject(memo));
	store();
	adapter.notifyDataSetChanged();
 }

 public static JSONObject toJSONObject(Memo memo) {
	JSONObject jOb = new JSONObject();
	jOb.put("id", memo.dateId);
	jOb.put("header", memo.header);
	jOb.put("body", memo.body);
	jOb.put("done", memo.done);
	return jOb;
 }

 public static Memo toMemo(JSONObject obj) {
	return new Memo((long)obj.get("id"), (String)obj.get("header"),
									(String)obj.get("body"), (boolean)obj.get("done"));
 }

 public static void deleteRecord(Memo memo) {
	if(jsonArray.contains(toJSONObject(memo))){
	adapter.remove(memo);
	jsonArray.remove(toJSONObject(memo));
	store();
	adapter.notifyDataSetChanged();
}
 }
 
 public static void replaceRecord(Memo newMemo, Memo oldMemo){
	deleteRecord(oldMemo);
	addRecord(newMemo);
	adapter.notifyDataSetChanged();
	fill_list(criteria);
 }


 private static void filterList(ArrayList<Memo> l, String criteria) {
	ArrayList<Memo> tl = new ArrayList<Memo>();
	tl.clear();
	for (int i = 0; i < jsonArray.size(); i++) {
	 JSONObject mm = (JSONObject)jsonArray.get(i);
	 if (((String)mm.get("header")).contains(criteria)) {
		Memo r = toMemo(mm);
		tl.add(r);
	 }
	}
	l.clear();
	l.addAll(tl);
 }
}
