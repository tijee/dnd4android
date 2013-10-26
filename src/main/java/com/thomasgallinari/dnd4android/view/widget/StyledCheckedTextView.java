/**
 * Copyright (C) 2011 Thomas Gallinari
 */
package com.thomasgallinari.dnd4android.view.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckedTextView;

/**
 * A {@link CheckedTextView} with a particular font.
 */
public class StyledCheckedTextView extends CheckedTextView {

    /**
     * The font used for this component.
     */
    public static final String FONT = "fonts/Primitive.ttf";

    /**
     * Creates a new {@link StyledCheckedTextView}.
     * 
     * @param context
     *            {@inheritDoc}
     */
    public StyledCheckedTextView(Context context) {
	super(context);
	init();
    }

    /**
     * Creates a new {@link StyledCheckedTextView}.
     * 
     * @param context
     *            {@inheritDoc}
     * @param attrs
     *            {@inheritDoc}
     */
    public StyledCheckedTextView(Context context, AttributeSet attrs) {
	super(context, attrs);
	init();
    }

    /**
     * Creates a new {@link StyledCheckedTextView}.
     * 
     * @param context
     *            {@inheritDoc}
     * @param attrs
     *            {@inheritDoc}
     * @param defStyle
     *            {@inheritDoc}
     */
    public StyledCheckedTextView(Context context, AttributeSet attrs,
	    int defStyle) {
	super(context, attrs, defStyle);
	init();
    }

    private void init() {
	setTypeface(Typeface.createFromAsset(getContext().getAssets(), FONT));
    }
}
