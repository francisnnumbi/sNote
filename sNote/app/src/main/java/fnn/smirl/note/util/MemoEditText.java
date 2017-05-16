package fnn.smirl.note.util;
import android.widget.*;
import android.util.*;
import android.content.*;
import android.graphics.*;

public class MemoEditText extends EditText {
	private boolean lineNumberVisible = false;
	private Rect _rect;
	private Paint lpaint;
	private int lineNumberMarginGap = 2;
	private final int LINE_NUMBER_TEXTSIZE_GAP = 2;

	public MemoEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		_rect = new Rect();
		lpaint = new Paint();
		lpaint.setAntiAlias(true);
		lpaint.setStyle(Paint.Style.FILL);
		lpaint.setColor(Color.BLACK);
		lpaint.setTextSize(getTextSize() - LINE_NUMBER_TEXTSIZE_GAP);
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
				canvas.drawText(t, _rect.left, baseLine, lpaint);
				baseLine += getLineHeight();
			}
			
			setPadding((int)lpaint.measureText(t) + lineNumberMarginGap, getPaddingTop(),
			getPaddingRight(), getPaddingBottom());
		}
		super.onDraw(canvas);
	}

}
