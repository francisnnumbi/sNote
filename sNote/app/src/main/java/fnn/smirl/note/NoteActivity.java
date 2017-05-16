package fnn.smirl.note;
import android.os.*;
import android.support.v7.widget.*;
import android.text.*;
import android.view.*;
import fnn.smirl.note.util.*;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View.OnClickListener;
import android.widget.TextView.BufferType;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NoteActivity extends AppCompatActivity
implements OnClickListener, Tokenize {

	String tempHeader = "";
	private long tempDateId;
	Pattern pattern = Pattern.compile("");
	Handler handler;
	AppCompatEditText note_title = null;
	MemoEditText note_body = null;
	private AppCompatTextView note_date = null;
	private AppCompatCheckBox note_done = null;
	Spannable spannable;
	private boolean runFormat = true;
	private static Activity _activity;
	ClipboardManager mClipboard;
	ClipData mClip;
	private boolean newNote = true;
	Memo memo;

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
		note_body.setLineNumberMarginGap(5);
		note_body.setText(memo.body);

		mClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

		note_body.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
				// TODO: Implement this method
				// runFormat = true;

			}

			@Override
			public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
				// TODO: Implement this method

			}

			@Override
			public void afterTextChanged(Editable p1) {
				// TODO: Implement this method

				runFormat = true;
			}

		});


		String s_date = formatDateToStringFrom(tempDateId);
		note_date.setText(s_date);


		note_done = (AppCompatCheckBox)findViewById(R.id.note_done);
		note_done.setChecked(memo.done);
		if (note_done.isChecked()) {
			note_done.setText("fait");
		} else {
			note_done.setText("en cours");
		}

		note_done.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(android.widget.CompoundButton p1, boolean p2) {
				try {	 
					if (note_done.isChecked()) {
						note_done.setText("fait");
					} else {
						note_done.setText("en cours");
					}
				} catch (Exception e) {}
				// MainActivity.replaceRecord(mm, memo);
			}
		});


		setupToolbar();
	}

	@Override
	public void onClick(View p1) {
		// TODO: Implement this method
		switch (p1.getId()) {

		}
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
	

	private void setupToolbar() {
		Toolbar toolbar = (Toolbar)findViewById(R.id.note_toolbar);
		setSupportActionBar(toolbar);
		final ActionBar ab = getSupportActionBar();
		ab.setHomeAsUpIndicator(R.drawable.notepad);
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setTitle("Memo");
		ab.setSubtitle("ID:" + memo.dateId);
		ab.setHomeButtonEnabled(true);
	}

	private String formatDateToStringFrom(long longDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		return formatter.format(new Date(longDate));
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO: Implement this method
		super.onPostCreate(savedInstanceState);

		handler = new Handler(Looper.getMainLooper());
		handler.postDelayed(new Runnable(){

			@Override
			public void run() {
				// TODO: Implement this method
				synchronized (note_body) {
					if (runFormat) {
						int pos1 = note_body.getSelectionStart();
						int pos2 = note_body.getSelectionEnd();
						refreshFormat();
						note_body.setSelection(pos1, pos2);
						runFormat = false;
					}
					handler.postDelayed(this, 1000);
				} 
			}
		}, 100);

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
				// here
				spannable = new SpannableString(note_body.getText().toString());
				Sharing.share(this, note_title.getText().toString(), Html.toHtml(spannable));
				// end here
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
//	SpannableStringBuilder ssb = new SpannableStringBuilder();
//	ssb.append(note_body.getText().toString());
		spannable = new SpannableString(note_body.getText().toString());
		Matcher matcher = END.matcher(spannable);
		formatEditor(matcher, spannable);

	}

	private void formatEditor(Matcher mat, Spannable ssb) {

		while (mat.find()) {
			mat.usePattern(SPACE);
			mat.find();

			mat.usePattern(NUMBER);
			while (mat.find()) {
				ssb.setSpan(new StyleSpan(Typeface.BOLD),
				mat.start(), mat.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			}

			mat.usePattern(BLOCK_NAME);
			while (mat.find()) {
				ssb.setSpan(new StyleSpan(Typeface.BOLD),
				mat.start(), mat.end() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				ssb.setSpan(new ForegroundColorSpan(Color.parseColor(BLOCK_NAME_COLOR)),
				mat.start(), mat.end() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);								

			}

			mat.usePattern(RESERVED_KEYS);
			while (mat.find()) {
				ssb.setSpan(new StyleSpan(Typeface.BOLD),
				mat.start(), mat.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				ssb.setSpan(new ForegroundColorSpan(Color.parseColor(RESERVED_KEYS_COLOR)),
				mat.start(), mat.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);								

			}
			mat.usePattern(PRIMITIVE_TYPE);
			while (mat.find()) {
				ssb.setSpan(new StyleSpan(Typeface.BOLD_ITALIC),
				mat.start(), mat.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				ssb.setSpan(new ForegroundColorSpan(Color.parseColor(PRIMITIVE_TYPE_COLOR)),
				mat.start(), mat.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);								

			}


			mat.usePattern(BLOCK_SIGN);
			while (mat.find()) {
				ssb.setSpan(new StyleSpan(Typeface.BOLD),
				mat.start(), mat.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				ssb.setSpan(new ForegroundColorSpan(Color.parseColor(BLOCK_SIGN_COLOR)),
				mat.start(), mat.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);						

			}

			mat.usePattern(INV_COMMA);
			while (mat.find()) {
				ssb.setSpan(new StyleSpan(Typeface.ITALIC),
				mat.start() + 1, mat.end() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				ssb.setSpan(new ForegroundColorSpan(Color.parseColor(INV_COMMA_COLOR)),
				mat.start() + 1, mat.end() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);						


			}
			mat.usePattern(COMMENT_BLOCK);
			while (mat.find()) {
				ssb.setSpan(new StyleSpan(Typeface.ITALIC),
				mat.start(), mat.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				ssb.setSpan(new ForegroundColorSpan(Color.parseColor(COMMENT_BLOCK_COLOR)),
				mat.start(), mat.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);						

			}

			mat.usePattern(END);
			if (mat.find()) {

				note_body.setText(ssb, BufferType.SPANNABLE);
				break;
			}
		}
	}
}
