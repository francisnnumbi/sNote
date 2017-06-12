package fnn.smirl.note.util;
import android.widget.*;
import android.util.*;
import android.content.*;
import android.graphics.*;

public class MemoEditText extends EditText {
	private boolean lineNumberVisible = false;
	private Rect _rect;
	private Paint lpaint;
	private MemoWatcher mWatcher;
	private int lineNumberMarginGap = 2;
	private final int LINE_NUMBER_TEXTSIZE_GAP = 2;

	public MemoEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		_rect = new Rect();
		lpaint = new Paint();
		lpaint.setAntiAlias(true);
		lpaint.setStyle(Paint.Style.FILL);
		lpaint.setColor(Color.WHITE);
		lpaint.setTextSize(getTextSize() - LINE_NUMBER_TEXTSIZE_GAP);
		mWatcher = new MemoWatcher();
		addTextChangedListener(mWatcher);
	}

	public void setLineNumberMarginGap(int lineNumberMarginGap) {
		this.lineNumberMarginGap = lineNumberMarginGap;
	}

	public int getLineNumberMarginGap() {
		return lineNumberMarginGap;
	}

	public void setLineNumberVisible(boolean lineNumberVisible) {
		this.lineNumberVisible = lineNumberVisible;
	}

	public boolean isLineNumberVisible() {
		return lineNumberVisible;
	}

	public void setLineNumberTextColor(int textColor) {
		lpaint.setColor(textColor);
	}

	public int getLineNumberTextColor() {
		return	lpaint.getColor();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO: Implement this method
		if (lineNumberVisible) {
				int baseLine = getBaseline();
				String t = "";
				for (int i = 0; i < getLineCount(); i++) {
					t = "" + (i + 1);
					lpaint.setAlpha(255);
					canvas.drawText(t, _rect.left, baseLine, lpaint);
					lpaint.setAlpha(25);
					canvas.drawLine(_rect.left, baseLine, getWidth(), baseLine, lpaint);
					baseLine += getLineHeight();
				}
				
				lpaint.setAlpha(25);
				int marg = (int)lpaint.measureText(t) + lineNumberMarginGap;
				canvas.drawLine(marg - (lineNumberMarginGap/2), 0, marg - (lineNumberMarginGap/2), baseLine - getLineHeight(), lpaint);
				setPadding(marg, getPaddingTop(),
				getPaddingRight(), getPaddingBottom());
			}
		super.onDraw(canvas);
	}

}
