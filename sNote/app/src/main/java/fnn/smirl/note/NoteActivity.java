package fnn.smirl.note;
import android.content.*;
import android.view.*;
import android.widget.*;

import android.app.Activity;
import android.os.Bundle;
import android.text.Selection;

public class NoteActivity extends Activity
{

 SharedPreferences memo;
 SharedPreferences.Editor memo_updator;
 String tempHeader = "";

 EditText note_title = null;
 EditText note_body = null;

 ClipboardManager mClipboard;
 ClipData mClip;
 @Override
 protected void onCreate(Bundle savedInstanceState)
 {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.note_view);
	Bundle bundle = getIntent().getExtras();
	String header = bundle.getString(getResources().getString(R.string.header));
	tempHeader = header;
	String body = bundle.getString(getResources().getString(R.string.body));
	memo = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
	memo_updator = memo.edit();
	note_title = (EditText)findViewById(R.id.note_title);
	note_title.setText(header);
	note_body = (EditText)findViewById(R.id.note_body);
	note_body.setText(body);

	mClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

 }

 @Override
 public boolean onCreateOptionsMenu(Menu menu)
 {
	// TODO: Implement this method
	getMenuInflater().inflate(R.menu.n_a_menu, menu);
	return true;
 }

 @Override
 public boolean onOptionsItemSelected(MenuItem item)
 {
	// TODO: Implement this method
	switch (item.getItemId()){
	 case R.id.item_select_all:
		note_body.selectAll();
		break;
	 case R.id.item_copy:
		mClip = ClipData.newPlainText("text", 
																	note_body.getEditableText()
																	.subSequence(note_body.getSelectionStart(), note_body.getSelectionEnd()));
		mClipboard.setPrimaryClip(mClip);
		break;
	 case R.id.item_cut:

		break;
	 case R.id.item_paste:
		mClip = mClipboard.getPrimaryClip();
		ClipData.Item ite = mClip.getItemAt(0);
		note_body.getEditableText()
		 .insert(note_body.getSelectionStart(), ite.getText());
		break;
	}
	return true;
 }

 public void save_changes(View v){
	if (tempHeader != null){
	 memo_updator.remove(tempHeader);
	}
	String df = note_title.getText().toString();
	if (df == "" || df == " ") df = tempHeader;
	if (df != ""){
	 memo_updator.putString(df,
													note_body.getText().toString());
	 memo_updator.commit();
	 finish();
	}else{
	 Toast.makeText(this, getResources().getString(R.string.notif_memo_not_saved),
									Toast.LENGTH_LONG)
		.show();
	}
 }

 public void delete_memo(View v){
	memo_updator.remove(note_title.getText().toString());
	memo_updator.commit();
	finish();
 }

 public void cancel_changes(View v){
	finish();
 }
}
