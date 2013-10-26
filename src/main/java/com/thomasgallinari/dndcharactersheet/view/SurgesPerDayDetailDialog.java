/**
 * Copyright (C) 2011 Thomas Gallinari
 */
package com.thomasgallinari.dndcharactersheet.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.thomasgallinari.dndcharactersheet.R;
import com.thomasgallinari.dndcharactersheet.model.Character;

/**
 * A dialog that shows the detail of the character surges per day.
 */
public class SurgesPerDayDetailDialog extends Dialog implements OnClickListener {

    class SurgesPerDayTextWatcher implements TextWatcher {

	private EditText mEditText;

	public SurgesPerDayTextWatcher(EditText editText) {
	    this.mEditText = editText;
	}

	@Override
	public void afterTextChanged(Editable s) {
	    validateData(mEditText);
	    bindField(mEditText);
	    fillCalculatedData();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
		int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before,
		int count) {
	}
    }

    private Character mCharacter;
    private EditText mClassEditText;
    private TextView mConTextView;
    private EditText mFeatEditText;
    private EditText mMiscEditText;
    private TextView mTitleTextView;
    private TextView mTotalScoreTextView;
    private TextView mTotalTextView;

    /**
     * Creates a new {@link SurgesPerDayDetailDialog}.
     * 
     * @param context
     *            {@inheritDoc}
     * @param character
     *            the concerned character
     */
    public SurgesPerDayDetailDialog(Context context, Character character) {
	super(context, R.style.Theme_Dialog_DnD);
	this.mCharacter = character;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.surges_per_day_detail_cancel_button:
	    cancel();
	    break;
	case R.id.surges_per_day_detail_ok_button:
	    validateData(mClassEditText);
	    validateData(mFeatEditText);
	    validateData(mMiscEditText);

	    mCharacter.setSurgesPerDayClass(Integer.parseInt(mClassEditText
		    .getText().toString()));
	    mCharacter.setSurgesPerDayFeat(Integer.parseInt(mFeatEditText
		    .getText().toString()));
	    mCharacter.setSurgesPerDayMisc(Integer.parseInt(mMiscEditText
		    .getText().toString()));

	    dismiss();
	    break;
	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTitle(CharSequence title) {
	super.setTitle(title);
	mTitleTextView.setText(title);
	mTotalTextView.setText(title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTitle(int titleId) {
	super.setTitle(titleId);
	mTitleTextView.setText(titleId);
	mTotalTextView.setText(titleId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.surges_per_day_detail);

	((Button) findViewById(R.id.surges_per_day_detail_cancel_button))
		.setOnClickListener(this);
	((Button) findViewById(R.id.surges_per_day_detail_ok_button))
		.setOnClickListener(this);

	mClassEditText = (EditText) findViewById(R.id.surges_per_day_detail_class_edit_text);
	mConTextView = (TextView) findViewById(R.id.surges_per_day_detail_con_text_view);
	mFeatEditText = (EditText) findViewById(R.id.surges_per_day_detail_feat_edit_text);
	mMiscEditText = (EditText) findViewById(R.id.surges_per_day_detail_misc_edit_text);
	mTitleTextView = (TextView) findViewById(R.id.surges_per_day_detail_title_text_view);
	mTotalTextView = (TextView) findViewById(R.id.surges_per_day_detail_total_text_view);
	mTotalScoreTextView = (TextView) findViewById(R.id.surges_per_day_detail_total_score_text_view);

	mClassEditText.addTextChangedListener(new SurgesPerDayTextWatcher(
		mClassEditText));
	mFeatEditText.addTextChangedListener(new SurgesPerDayTextWatcher(
		mFeatEditText));
	mMiscEditText.addTextChangedListener(new SurgesPerDayTextWatcher(
		mMiscEditText));
    }

    @Override
    protected void onStart() {
	super.onStart();
	fillData();
	mClassEditText.requestFocus();
    }

    private void bindField(EditText editText) {
	switch (editText.getId()) {
	case R.id.surges_per_day_detail_class_edit_text:
	    mCharacter.setSurgesPerDayClass(Integer.parseInt(editText.getText()
		    .toString()));
	    break;
	case R.id.surges_per_day_detail_feat_edit_text:
	    mCharacter.setSurgesPerDayFeat(Integer.parseInt(editText.getText()
		    .toString()));
	    break;
	case R.id.surges_per_day_detail_misc_edit_text:
	    mCharacter.setSurgesPerDayMisc(Integer.parseInt(editText.getText()
		    .toString()));
	    break;
	}
    }

    private void fillCalculatedData() {
	mTotalScoreTextView.setText(String
		.valueOf(mCharacter.getSurgesPerDay()));
    }

    private void fillData() {
	mConTextView.setText(String.valueOf(mCharacter.getConMod()));
	mClassEditText.setText(String
		.valueOf(mCharacter.getSurgesPerDayClass()));
	mFeatEditText.setText(String.valueOf(mCharacter.getSurgesPerDayFeat()));
	mMiscEditText.setText(String.valueOf(mCharacter.getSurgesPerDayMisc()));

	fillCalculatedData();
    }

    private void validateData(EditText editText) {
	if (editText.getText().length() == 0) {
	    editText.setText("0");
	}
	if (editText.getText().toString().equals("-")) {
	    editText.setText("-0");
	}
    }
}
