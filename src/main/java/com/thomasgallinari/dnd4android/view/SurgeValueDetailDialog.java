/**
 * Copyright (C) 2011 Thomas Gallinari
 */
package com.thomasgallinari.dnd4android.view;

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

import com.thomasgallinari.dnd4android.R;
import com.thomasgallinari.dnd4android.model.Character;

/**
 * A dialog that shows the detail of the character surge value.
 */
public class SurgeValueDetailDialog extends Dialog implements OnClickListener {

    class SurgeTextWatcher implements TextWatcher {

	private EditText mEditText;

	public SurgeTextWatcher(EditText editText) {
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

    private TextView mBaseTextView;
    private Character mCharacter;
    private EditText mClassEditText;
    private EditText mFeatEditText;
    private EditText mMiscEditText;
    private TextView mTitleTextView;
    private TextView mTotalScoreTextView;
    private TextView mTotalTextView;

    /**
     * Creates a new {@link SurgeValueDetailDialog}.
     * 
     * @param context
     *            {@inheritDoc}
     * @param character
     *            the concerned character
     */
    public SurgeValueDetailDialog(Context context, Character character) {
	super(context, R.style.Theme_Dialog_DnD);
	this.mCharacter = character;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.surge_value_detail_cancel_button:
	    cancel();
	    break;
	case R.id.surge_value_detail_ok_button:
	    validateData(mClassEditText);
	    validateData(mFeatEditText);
	    validateData(mMiscEditText);

	    mCharacter.setSurgeClass(Integer.parseInt(mClassEditText.getText()
		    .toString()));
	    mCharacter.setSurgeFeat(Integer.parseInt(mFeatEditText.getText()
		    .toString()));
	    mCharacter.setSurgeMisc(Integer.parseInt(mMiscEditText.getText()
		    .toString()));

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
	setContentView(R.layout.surge_value_detail);

	((Button) findViewById(R.id.surge_value_detail_cancel_button))
		.setOnClickListener(this);
	((Button) findViewById(R.id.surge_value_detail_ok_button))
		.setOnClickListener(this);

	mBaseTextView = (TextView) findViewById(R.id.surge_value_detail_base_text_view);
	mClassEditText = (EditText) findViewById(R.id.surge_value_detail_class_edit_text);
	mFeatEditText = (EditText) findViewById(R.id.surge_value_detail_feat_edit_text);
	mMiscEditText = (EditText) findViewById(R.id.surge_value_detail_misc_edit_text);
	mTitleTextView = (TextView) findViewById(R.id.surge_value_detail_title_text_view);
	mTotalTextView = (TextView) findViewById(R.id.surge_value_detail_total_text_view);
	mTotalScoreTextView = (TextView) findViewById(R.id.surge_value_detail_total_score_text_view);

	mClassEditText.addTextChangedListener(new SurgeTextWatcher(
		mClassEditText));
	mFeatEditText
		.addTextChangedListener(new SurgeTextWatcher(mFeatEditText));
	mMiscEditText
		.addTextChangedListener(new SurgeTextWatcher(mMiscEditText));
    }

    @Override
    protected void onStart() {
	super.onStart();
	fillData();
	mClassEditText.requestFocus();
    }

    private void bindField(EditText editText) {
	switch (editText.getId()) {
	case R.id.surge_value_detail_class_edit_text:
	    mCharacter.setSurgeClass(Integer.parseInt(editText.getText()
		    .toString()));
	    break;
	case R.id.surge_value_detail_feat_edit_text:
	    mCharacter.setSurgeFeat(Integer.parseInt(editText.getText()
		    .toString()));
	    break;
	case R.id.surge_value_detail_misc_edit_text:
	    mCharacter.setSurgeMisc(Integer.parseInt(editText.getText()
		    .toString()));
	    break;
	}
    }

    private void fillCalculatedData() {
	mTotalScoreTextView.setText(String.valueOf(mCharacter.getSurgeValue()));
    }

    private void fillData() {
	mBaseTextView.setText(String.valueOf(mCharacter.getHpMax() / 4));
	mClassEditText.setText(String.valueOf(mCharacter.getSurgeClass()));
	mFeatEditText.setText(String.valueOf(mCharacter.getSurgeFeat()));
	mMiscEditText.setText(String.valueOf(mCharacter.getSurgeMisc()));

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
