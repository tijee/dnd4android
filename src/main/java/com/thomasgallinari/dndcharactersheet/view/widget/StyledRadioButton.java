/**
 * Copyright (C) 2011 Thomas Gallinari
 */
package com.thomasgallinari.dndcharactersheet.view.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * A {@link RadioButton} with a particular font.
 */
public class StyledRadioButton extends RadioButton {

    /**
     * The font used for this component.
     */
    public static final String FONT = "fonts/Primitive.ttf";

    /**
     * Creates a new {@link StyledRadioButton}.
     * 
     * @param context
     *            {@inheritDoc}
     */
    public StyledRadioButton(Context context) {
	super(context);
	init();
    }

    /**
     * Creates a new {@link StyledRadioButton}.
     * 
     * @param context
     *            {@inheritDoc}
     * @param attrs
     *            {@inheritDoc}
     */
    public StyledRadioButton(Context context, AttributeSet attrs) {
	super(context, attrs);
	init();
    }

    /**
     * Creates a new {@link StyledRadioButton}.
     * 
     * @param context
     *            {@inheritDoc}
     * @param attrs
     *            {@inheritDoc}
     * @param defStyle
     *            {@inheritDoc}
     */
    public StyledRadioButton(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
	init();
    }

    private void init() {
	setTypeface(Typeface.createFromAsset(getContext().getAssets(), FONT));
    }
}
