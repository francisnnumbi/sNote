package fnn.smirl.note;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener
{

 @Override
 public void onSharedPreferenceChanged(SharedPreferences p1, String p2)
 {
	// TODO: Implement this method
	fill_list("");
 }


 private ListView note_list = null;
 EditText heading = null;
 SharedPreferences memo;
 SharedPreferences.Editor memo_updator;


 @Override
 protected void onCreate(Bundle savedInstanceState)
 {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
	{
	 ActionBar actionBar = getActionBar();
	 try
	 {
		PackageInfo info = getPackageManager()
		 .getPackageInfo(getPackageName(), 0);
		actionBar.setSubtitle(getResources().getString(R.string.par) + " " + getResources().getString(R.string.author));
		actionBar.setTitle(getResources()
											 .getString(R.string.app_name) + " " + info.versionName + info.versionCode);
	 }
	 catch (PackageManager.NameNotFoundException e) {}


	}

	memo = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
	memo_updator = memo.edit();
	memo.registerOnSharedPreferenceChangeListener(this);

	heading = (EditText)findViewById(R.id.heading);
	heading.setOnEditorActionListener(new OnEditorActionListener(){

		@Override
		public boolean onEditorAction(TextView p1, int p2, KeyEvent p3)
		{
		 // TODO: Implement this method
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

 private void fill_list(String criteria){
	Map map = memo.getAll();
	ArrayList<Memo> list = new ArrayList<Memo>();
	Iterator iter = map.keySet().iterator();
	while (iter.hasNext()){
	 String d = (String)iter.next();
	 if (d.contains(criteria)){
		list.add(new Memo(d, memo.getString(d, "")));
	 }
	}
	if (list.isEmpty()){
	 list.add(new Memo(getResources().getString(R.string.empty_list), ""));
	}
	Collections.sort(list);
	
	NoteAdapter adapter = new NoteAdapter(this, list);
	note_list.setAdapter(adapter);
 }

 @Override
 protected void onResume()
 {
	// TODO: Implement this method
	super.onResume();
	fill_list(heading.getText().toString().trim());
 }



 @Override
 public boolean onCreateOptionsMenu(Menu menu)
 {
	// TODO: Implement this method
	getMenuInflater().inflate(R.menu.main_menu, menu);
	return true;
 }

 @Override
 public boolean onOptionsItemSelected(MenuItem item)
 {
	// TODO: Implement this method
	switch (item.getItemId())
	{
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

 private void newMemo(){
	Intent intent = new Intent(this, NoteActivity.class);
	intent.putExtra(getResources().getString(R.string.header), "");
	intent.putExtra(getResources().getString(R.string.body), "");
	startActivity(intent);
 }

 public void newNote(View v){
	newMemo();
 }
}
