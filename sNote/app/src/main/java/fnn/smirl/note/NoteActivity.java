package fnn.smirl.note;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.design.widget.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.text.*;
import android.view.*;
import android.view.View.*;
import android.widget.TextView.*;
import fnn.smirl.note.util.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;

import android.content.ClipboardManager;
import android.support.v7.app.ActionBar;
import android.view.View.OnClickListener;
import android.text.style.*;
import android.widget.*;


public class NoteActivity extends AppCompatActivity
implements Tokenize, OnClickListener {

	String tempHeader = "";
	private long tempDateId;
	Pattern pattern = Pattern.compile("");
	AppCompatEditText note_title = null;
	MemoEditText note_body = null;
	private AppCompatTextView note_date = null;
	private AppCompatCheckBox note_done = null;
	Editable spannable;
	private static Activity _activity;
	ClipboardManager mClipboard;
	ClipData mClip;
	private boolean newNote = true;
	Memo memo;


	private FloatingActionButton fab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_view);
		_activity = this;

		Bundle bundle = getIntent().getExtras();
		newNote = bundle.getBoolean("new");
		note_date = (AppCompatTextView)findViewById(R.id.note_date);
		tempDateId = bundle.getLong("date_id");
		memo = MainActivity.memos.get(tempDateId);

		if (memo == null) {
			Calendar cal = Calendar.getInstance();
			tempDateId = cal.getTimeInMillis();
			memo = new Memo(tempDateId, "New Memo", "New Idea", false);
		}

		note_title = (AppCompatEditText)findViewById(R.id.note_title);
		note_title.setText(memo.header);
		note_body = (MemoEditText)findViewById(R.id.note_body);
		note_body.setLineNumberVisible(true);
		note_body.setLineNumberTextColor(Color.CYAN);
		note_body.setLineNumberMarginGap(10);
		note_body.setText(memo.body);

		mClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

		String s_date = formatDateToStringFrom(tempDateId);
		note_date.setText(s_date);


		note_done = (AppCompatCheckBox)findViewById(R.id.note_done);
		note_done.setChecked(memo.done);

		((Button)findViewById(R.id.noteview_multi_comment)).setOnClickListener(this);
		((Button)findViewById(R.id.noteview_single_comment)).setOnClickListener(this);
		((Button)findViewById(R.id.noteview_parenth)).setOnClickListener(this);
		
		
		
		setupToolbar();
		setupFab();
		refreshFormat();
	}

	private static void displayInfo(String info) {
		Snackbar
		.make(_activity.findViewById(R.id.note_root_layout),
		info,
		Snackbar.LENGTH_SHORT)
		.setAction("Ok", new OnClickListener(){

			@Override
			public void onClick(View p1) {
				// TODO: Implement this method
			}
		})
		.show();
	}

	@Override
	public void onClick(View p1) {
		// TODO: Implement this method
		int ii, uu;
		CharSequence st;
		String tt;
		switch(p1.getId()){
			case R.id.noteview_single_comment:
//				ii = note_body.getSelectionStart();
//				uu = note_body.getSelectionEnd();
//				st = note_body.getText().subSequence(ii, uu);
//				tt = ((Button) p1).getText().toString();
//				note_body.getText().replace(ii, uu, tt);
//				ii = note_body.getSelectionStart();
//				uu = note_body.getSelectionEnd();
//				note_body.getText().insert(ii-2, st);
//				note_body.setSelection(ii-2, uu-2);
//				break;
			case R.id.noteview_multi_comment:
				ii = note_body.getSelectionStart();
				uu = note_body.getSelectionEnd();
				st = note_body.getText().subSequence(ii, uu);
				tt = ((Button) p1).getText().toString();
				note_body.getText().replace(ii, uu, tt);
				ii = note_body.getSelectionStart();
				uu = note_body.getSelectionEnd();
				note_body.getText().insert(ii-2, st);
				note_body.setSelection(ii-2, uu-2);
				break;
			case R.id.noteview_parenth:
				ii = note_body.getSelectionStart();
				uu = note_body.getSelectionEnd();
				st = note_body.getText().subSequence(ii, uu);
				tt = ((Button) p1).getText().toString();
				note_body.getText().replace(ii, uu, tt);
				ii = note_body.getSelectionStart();
				uu = note_body.getSelectionEnd();
				note_body.getText().insert(ii-1, st);
				note_body.setSelection(ii-1, uu-1);
				break;
				
		}
	}


	

	private void setupToolbar() {
		android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.note_toolbar);
		setSupportActionBar(toolbar);
		final ActionBar ab = getSupportActionBar();
		ab.setHomeAsUpIndicator(R.drawable.notepad);
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setTitle("Memo");
		ab.setSubtitle("ID:" + memo.dateId);
		ab.setHomeButtonEnabled(true);
	}

	private void setupFab() {
		fab = (FloatingActionButton)findViewById(R.id.nv_fab);
		fab.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View p1) {
				// TODO: Implement this method

//				int pos1 = note_body.getSelectionStart();
//				int pos2 = note_body.getSelectionEnd();
//				refreshFormat();
//				note_body.setSelection(pos1, pos2);
			}

		});
	}

	private String formatDateToStringFrom(long longDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		return formatter.format(new Date(longDate));
	}

	@Override
	public void onBackPressed() {
		cancel_changes();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.n_a_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				displayInfo("Return to Main App");
				finish();
				break;
			case R.id.item_select_all:
				note_body.selectAll();
				break;
			case R.id.item_copy:
				mClip = ClipData.newPlainText("text", 
				note_body.getEditableText()
				.subSequence(note_body.getSelectionStart(), note_body.getSelectionEnd()));
				mClipboard.setPrimaryClip(mClip);
				break;
			case R.id.item_share:
				spannable = note_body.getText();
				Sharing.share(this, note_title.getText().toString(), Html.toHtml(spannable));
				break;
			case R.id.item_save:
				save_changes();
				break;
			case R.id.item_delete:
				delete_memo();
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

	public void save_changes() {
		displayInfo("Mise à jour de la base des données...");
		String df = note_title.getText().toString();
		memo.header = df;
		memo.body = note_body.getText().toString();
		memo.done = note_done.isChecked();
		if (newNote) {
			MainActivity.memos.add(memo);
		}
		newNote = false;
		MainActivity.store();
		displayInfo("Mise à jour complète.");
	}

	public void delete_memo() {
		MainActivity.deleteRecord(memo);	
		finish();
	}

	public void cancel_changes() {
		finish();
	}

	private void refreshFormat() {
		int pos1 = note_body.getSelectionStart();
		int pos2 = note_body.getSelectionEnd();
		spannable = note_body.getText();
		EditorUtils.formatEditor(spannable);
		note_body.setText(spannable, BufferType.SPANNABLE);
		note_body.setSelection(pos1, pos2);
	}
}
