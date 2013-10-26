/**
 * Copyright (C) 2011 Thomas Gallinari
 */
package com.thomasgallinari.dnd4android.view.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * An {@link EditText} with a particular font.
 */
public class StyledEditText extends EditText {

    private static final String FONT = "fonts/Primitive.ttf";

    /**
     * Creates a new {@link StyledEditText}.
     * 
     * @param context
     *            {@inheritDoc}
     */
    public StyledEditText(Context context) {
	super(context);
	init();
    }

    /**
     * Creates a new {@link StyledEditText}.
     * 
     * @param context
     *            {@inheritDoc}
     * @param attrs
     *            {@inheritDoc}
     */
    public StyledEditText(Context context, AttributeSet attrs) {
	super(context, attrs);
	init();
    }

    /**
     * Creates a new {@link StyledEditText}.
     * 
     * @param context
     *            {@inheritDoc}
     * @param attrs
     *            {@inheritDoc}
     * @param defStyle
     *            {@inheritDoc}
     */
    public StyledEditText(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
	init();
    }

    private void init() {
	setTypeface(Typeface.createFromAsset(getContext().getAssets(), FONT));
    }
}
