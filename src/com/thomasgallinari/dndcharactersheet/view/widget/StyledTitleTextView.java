package com.thomasgallinari.dndcharactersheet.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

/**
 * A {@link StyledTextView} with a white outline.
 */
public class StyledTitleTextView extends StyledTextView {

    private int mAscent;
    private Paint mTextPaint;
    private Paint mTextPaintOutline;

    /**
     * Creates a new {@link StyledTitleTextView}.
     * 
     * @param context
     *            {@inheritDoc}
     */
    public StyledTitleTextView(Context context) {
	super(context);
	init();
    }

    /**
     * Creates a new {@link StyledTitleTextView}.
     * 
     * @param context
     *            {@inheritDoc}
     * @param attrs
     *            {@inheritDoc}
     */
    public StyledTitleTextView(Context context, AttributeSet attrs) {
	super(context, attrs);
	init();
    }

    /**
     * Creates a new {@link StyledTitleTextView}.
     * 
     * @param context
     *            {@inheritDoc}
     * @param attrs
     *            {@inheritDoc}
     * @param defStyle
     *            {@inheritDoc}
     */
    public StyledTitleTextView(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
	init();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(Canvas canvas) {
	canvas.drawText(getText().toString(), getPaddingLeft(), getPaddingTop()
		- mAscent, mTextPaintOutline);
	canvas.drawText(getText().toString(), getPaddingLeft(), getPaddingTop()
		- mAscent, mTextPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	setMeasuredDimension(measureWidth(widthMeasureSpec),
		measureHeight(heightMeasureSpec));
    }

    private void init() {
	mTextPaint = new Paint();
	mTextPaint.setAntiAlias(true);
	mTextPaint.setTextSize(getTextSize());
	mTextPaint.setColor(0xff811C32);
	mTextPaint.setStyle(Paint.Style.FILL);
	mTextPaint.setFakeBoldText(true);
	mTextPaint.setTypeface(getTypeface());

	mTextPaintOutline = new Paint();
	mTextPaintOutline.setAntiAlias(true);
	mTextPaintOutline.setTextSize(getTextSize());
	mTextPaintOutline.setColor(0xffffffff);
	mTextPaintOutline.setStyle(Paint.Style.STROKE);
	mTextPaintOutline.setStrokeWidth(2);
	mTextPaintOutline.setFakeBoldText(true);
	mTextPaintOutline.setTypeface(getTypeface());

	setPadding(3, 3, 3, 3);
    }

    private int measureHeight(int measureSpec) {
	int result = 0;
	int specMode = MeasureSpec.getMode(measureSpec);
	int specSize = MeasureSpec.getSize(measureSpec);

	mAscent = (int) mTextPaint.ascent();
	if (specMode == MeasureSpec.EXACTLY) {
	    // We were told how big to be
	    result = specSize;
	} else {
	    // Measure the text (beware: ascent is a negative number)
	    result = (int) (-mAscent + mTextPaint.descent()) + getPaddingTop()
		    + getPaddingBottom();
	    if (specMode == MeasureSpec.AT_MOST) {
		// Respect AT_MOST value if that was what is called for by
		// measureSpec
		result = Math.min(result, specSize);
	    }
	}
	return result;
    }

    private int measureWidth(int measureSpec) {
	int result = 0;
	int specMode = MeasureSpec.getMode(measureSpec);
	int specSize = MeasureSpec.getSize(measureSpec);

	if (specMode == MeasureSpec.EXACTLY) {
	    // We were told how big to be
	    result = specSize;
	} else {
	    // Measure the text
	    result = (int) mTextPaint.measureText(getText().toString())
		    + getPaddingLeft() + getPaddingRight();
	    if (specMode == MeasureSpec.AT_MOST) {
		// Respect AT_MOST value if that was what is called for by
		// measureSpec
		result = Math.min(result, specSize);
	    }
	}

	return result;
    }
}
