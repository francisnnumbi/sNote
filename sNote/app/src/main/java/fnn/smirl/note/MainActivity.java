package fnn.smirl.note;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import fnn.smirl.note.util.NoteAdapter;
import fnn.smirl.note.util.Memo;

public class MainActivity extends Activity {
 static JSONObject jsonObj;
 static JSONParser parser;
 static NoteAdapter adapter ;
 public static String filename;


 private ListView note_list = null;
 EditText heading = null;

 @Override
 protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	 ActionBar actionBar = getActionBar();
	 try {
		PackageInfo info = getPackageManager()
		 .getPackageInfo(getPackageName(), 0);
		actionBar.setSubtitle(getResources().getString(R.string.par) + " " + getResources().getString(R.string.author));
		actionBar.setTitle(getResources()
											 .getString(R.string.app_name) + " " + info.versionName + info.versionCode);
	 }
	 catch (PackageManager.NameNotFoundException e) {}
	}
	filename = getFilesDir() + "/snote.json";
	jsonObj = new JSONObject();
	parser = new JSONParser();
	heading = (EditText)findViewById(R.id.heading);
	heading.setOnEditorActionListener(new OnEditorActionListener(){

		@Override
		public boolean onEditorAction(TextView p1, int p2, KeyEvent p3) {
		 fill_list(heading.getText().toString().trim());
		 return true;
		} 
	 });

	note_list = (ListView)findViewById(R.id.note_list);

	LayoutInflater inflater = getLayoutInflater();
	ViewGroup heady = (ViewGroup)inflater.inflate(R.layout.list_header,
																								note_list, false);
	note_list.addHeaderView(heady, null, false);

	fill_list("");
 }

 private void fill_list(String criteria) {
	ArrayList<Memo> list = retrieveRecord();
	filterList(list, criteria);
	if (list.isEmpty()) {
	 list.add(new Memo(getResources().getString(R.string.empty_list), ""));
	}
	Collections.sort(list);

	adapter = new NoteAdapter(this, list);
	note_list.setAdapter(adapter);
 }

 @Override
 protected void onResume() {
	super.onResume();
	fill_list(heading.getText().toString().trim());
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
	startActivity(intent);
 }

 public void newNote(View v) {
	newMemo();
 }

 /*** treating in json files */
 static void store() {

	FileWriter w;

	try {
	 w = new FileWriter(filename);
	 w.write(jsonObj.toJSONString());
	 w.flush();
	}
	catch (IOException e) {}
 }

 private ArrayList<Memo> retrieveRecord() {
	try {
	 Object obj = parser.parse(new FileReader(filename));
	 jsonObj.clear();
	 jsonObj = (JSONObject)obj;
	 ArrayList<Memo> list = new ArrayList<Memo>();
	 Set set = jsonObj.keySet();
	 Iterator iter = set.iterator();
	 while (iter.hasNext()) {
		String k = iter.next().toString();
		String v = (String) jsonObj.get(k);
		Memo r = new Memo(k, v);
		list.add(r);
	 }
	 Collections.sort(list);
	 return list;
	}
	catch (Exception e) {}
	return new ArrayList<Memo>();
 }


 public static void addRecord(Memo memo) {
	jsonObj.put(memo.getHeader(), memo.getBody());
	store();
 }


 public static void deleteRecord(String key) {
	jsonObj.remove(key);
	store();
	adapter.notifyDataSetChanged();
 }


 private void filterList(ArrayList<Memo> l, String criteria) {
	ArrayList<Memo> tl = new ArrayList<Memo>();
	for (int i = 0; i < l.size(); i++) {
	 Memo m = l.get(i);
	 if (m.contains(criteria))
		tl.add(m);
	}
	l.clear();
	l.addAll(tl);
 }
}
