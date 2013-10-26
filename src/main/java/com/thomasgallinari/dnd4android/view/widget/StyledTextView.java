/**
 * Copyright (C) 2011 Thomas Gallinari
 */
package com.thomasgallinari.dnd4android.view.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * A {@link TextView} with a particular font.
 */
public class StyledTextView extends TextView {

    /**
     * The font used for this component.
     */
    public static final String FONT = "fonts/Primitive.ttf";

    /**
     * Creates a new {@link StyledTextView}.
     * 
     * @param context
     *            {@inheritDoc}
     */
    public StyledTextView(Context context) {
	super(context);
	init();
    }

    /**
     * Creates a new {@link StyledTextView}.
     * 
     * @param context
     *            {@inheritDoc}
     * @param attrs
     *            {@inheritDoc}
     */
    public StyledTextView(Context context, AttributeSet attrs) {
	super(context, attrs);
	init();
    }

    /**
     * Creates a new {@link StyledTextView}.
     * 
     * @param context
     *            {@inheritDoc}
     * @param attrs
     *            {@inheritDoc}
     * @param defStyle
     *            {@inheritDoc}
     */
    public StyledTextView(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
	init();
    }

    private void init() {
	setTypeface(Typeface.createFromAsset(getContext().getAssets(), FONT));
    }
}
