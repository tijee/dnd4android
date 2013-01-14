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
 * A dialog that shows the detail of the character HP.
 */
public class HpDetailDialog extends Dialog implements OnClickListener {

    class HpTextWatcher implements TextWatcher {

	private EditText mEditText;

	public HpTextWatcher(EditText editText) {
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

    private EditText mBaseEditText;
    private Character mCharacter;
    private EditText mClassEditText;
    private TextView mConTextView;
    private EditText mFeatEditText;
    private TextView mLevelTextView;
    private EditText mMiscEditText;
    private EditText mPerLevelGainedEditText;
    private TextView mTitleTextView;
    private TextView mTotalScoreTextView;
    private TextView mTotalTextView;

    /**
     * Creates a new {@link HpDetailDialog}.
     * 
     * @param context
     *            {@inheritDoc}
     * @param character
     *            the concerned character
     */
    public HpDetailDialog(Context context, Character character) {
	super(context, R.style.Theme_Dialog_DnD);
	this.mCharacter = character;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.hp_detail_cancel_button:
	    cancel();
	    break;
	case R.id.hp_detail_ok_button:
	    validateData(mBaseEditText);
	    validateData(mClassEditText);
	    validateData(mFeatEditText);
	    validateData(mMiscEditText);
	    validateData(mPerLevelGainedEditText);

	    mCharacter.setHpBase(Integer.parseInt(mBaseEditText.getText()
		    .toString()));
	    mCharacter.setHpClass(Integer.parseInt(mClassEditText.getText()
		    .toString()));
	    mCharacter.setHpFeat(Integer.parseInt(mFeatEditText.getText()
		    .toString()));
	    mCharacter.setHpMisc(Integer.parseInt(mMiscEditText.getText()
		    .toString()));
	    mCharacter.setHpPerLevel(Integer.parseInt(mPerLevelGainedEditText
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
	setContentView(R.layout.hp_detail);

	((Button) findViewById(R.id.hp_detail_cancel_button))
		.setOnClickListener(this);
	((Button) findViewById(R.id.hp_detail_ok_button))
		.setOnClickListener(this);

	mBaseEditText = (EditText) findViewById(R.id.hp_detail_base_edit_text);
	mClassEditText = (EditText) findViewById(R.id.hp_detail_class_edit_text);
	mConTextView = (TextView) findViewById(R.id.hp_detail_con_text_view);
	mFeatEditText = (EditText) findViewById(R.id.hp_detail_feat_edit_text);
	mLevelTextView = (TextView) findViewById(R.id.hp_detail_level_text_view);
	mMiscEditText = (EditText) findViewById(R.id.hp_detail_misc_edit_text);
	mPerLevelGainedEditText = (EditText) findViewById(R.id.hp_detail_per_level_gained_edit_text);
	mTitleTextView = (TextView) findViewById(R.id.hp_detail_title_text_view);
	mTotalScoreTextView = (TextView) findViewById(R.id.hp_detail_total_score_text_view);
	mTotalTextView = (TextView) findViewById(R.id.hp_detail_total_text_view);

	mBaseEditText.addTextChangedListener(new HpTextWatcher(mBaseEditText));
	mClassEditText
		.addTextChangedListener(new HpTextWatcher(mClassEditText));
	mFeatEditText.addTextChangedListener(new HpTextWatcher(mFeatEditText));
	mMiscEditText.addTextChangedListener(new HpTextWatcher(mMiscEditText));
	mPerLevelGainedEditText.addTextChangedListener(new HpTextWatcher(
		mPerLevelGainedEditText));
    }

    @Override
    protected void onStart() {
	super.onStart();
	fillData();
	mBaseEditText.requestFocus();
    }

    private void bindField(EditText editText) {
	switch (editText.getId()) {
	case R.id.hp_detail_base_edit_text:
	    mCharacter.setHpBase(Integer
		    .parseInt(editText.getText().toString()));
	    break;
	case R.id.hp_detail_class_edit_text:
	    mCharacter.setHpClass(Integer.parseInt(editText.getText()
		    .toString()));
	    break;
	case R.id.hp_detail_feat_edit_text:
	    mCharacter.setHpFeat(Integer
		    .parseInt(editText.getText().toString()));
	    break;
	case R.id.hp_detail_misc_edit_text:
	    mCharacter.setHpMisc(Integer
		    .parseInt(editText.getText().toString()));
	    break;
	case R.id.hp_detail_per_level_gained_edit_text:
	    mCharacter.setHpPerLevel(Integer.parseInt(editText.getText()
		    .toString()));
	    break;
	}
    }

    private void fillCalculatedData() {
	mTotalScoreTextView.setText(String.valueOf(mCharacter.getHpMax()));
    }

    private void fillData() {
	mConTextView.setText(String.valueOf(mCharacter.getCon()));
	mLevelTextView.setText(String.valueOf(mCharacter.getLevel() - 1));
	mBaseEditText.setText(String.valueOf(mCharacter.getHpBase()));
	mClassEditText.setText(String.valueOf(mCharacter.getHpClass()));
	mFeatEditText.setText(String.valueOf(mCharacter.getHpFeat()));
	mMiscEditText.setText(String.valueOf(mCharacter.getHpMisc()));
	mPerLevelGainedEditText.setText(String.valueOf(mCharacter
		.getHpPerLevel()));

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
