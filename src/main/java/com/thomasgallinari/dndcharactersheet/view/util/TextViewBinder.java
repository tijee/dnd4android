/**
 * Copyright (C) 2011 Thomas Gallinari
 */
package com.thomasgallinari.dndcharactersheet.view.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

/**
 * A text watcher that enables / disables the given component(s) considering the
 * text length.
 */
public class TextViewBinder implements TextWatcher {

    private View mTarget;
    private TextView[] mTextViews;

    /**
     * Creates a new {@link TextViewBinder}.
     * 
     * @param pTarget
     *            the bound view
     * @param pTextViews
     *            the text views that may change the state of the target view
     */
    public TextViewBinder(View pTarget, TextView[] pTextViews) {
	mTarget = pTarget;
	mTextViews = pTextViews;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterTextChanged(Editable s) {
	boolean enabled = true;
	for (TextView textView : mTextViews) {
	    if (textView.getText().length() == 0) {
		enabled = false;
		break;
	    }
	}
	mTarget.setEnabled(enabled);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
	    int after) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }
}
