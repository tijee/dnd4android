/**
 * Copyright (C) 2011 Thomas Gallinari
 */
package com.thomasgallinari.dnd4android.view.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * A {@link Button} with a particular font.
 */
public class StyledButton extends Button {

    private static final String FONT = "fonts/Primitive.ttf";

    /**
     * Creates a new {@link StyledButton}.
     * 
     * @param context
     *            {@inheritDoc}
     */
    public StyledButton(Context context) {
	super(context);
	init();
    }

    /**
     * Creates a new {@link StyledButton}.
     * 
     * @param context
     *            {@inheritDoc}
     * @param attrs
     *            {@inheritDoc}
     */
    public StyledButton(Context context, AttributeSet attrs) {
	super(context, attrs);
	init();
    }

    /**
     * Creates a new {@link StyledButton}.
     * 
     * @param context
     *            {@inheritDoc}
     * @param attrs
     *            {@inheritDoc}
     * @param defStyle
     *            {@inheritDoc}
     */
    public StyledButton(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
	init();
    }

    private void init() {
	setTypeface(Typeface.createFromAsset(getContext().getAssets(), FONT));
    }
}
