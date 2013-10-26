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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.thomasgallinari.dnd4android.R;
import com.thomasgallinari.dnd4android.model.Character;

/**
 * A dialog that shows the detail of the character AC.
 */
public class AcDetailDialog extends Dialog implements
	android.view.View.OnClickListener {

    class AcTextWatcher implements TextWatcher {

	private EditText mEditText;

	public AcTextWatcher(EditText editText) {
	    mEditText = editText;
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

    private EditText mArmorEditText;
    private TextView mBaseTextView;
    private Character mCharacter;
    private EditText mClassEditText;
    private EditText mEnhEditText;
    private EditText mFeatEditText;
    private EditText mMiscEditText;
    private TextView mTitleTextView;
    private TextView mTotalScoreTextView;
    private TextView mTotalTextView;

    /**
     * Creates a new {@link AcDetailDialog}.
     * 
     * @param context
     *            {@inheritDoc}
     * @param character
     *            the concerned character
     */
    public AcDetailDialog(Context context, Character character) {
	super(context, R.style.Theme_Dialog_DnD);
	mCharacter = character;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.ac_detail_cancel_button:
	    cancel();
	    break;
	case R.id.ac_detail_ok_button:
	    validateData(mArmorEditText);
	    validateData(mClassEditText);
	    validateData(mEnhEditText);
	    validateData(mFeatEditText);
	    validateData(mMiscEditText);

	    mCharacter.setAcArmor(Integer.parseInt(mArmorEditText.getText()
		    .toString()));
	    mCharacter.setAcClass(Integer.parseInt(mClassEditText.getText()
		    .toString()));
	    mCharacter.setAcEnh(Integer.parseInt(mEnhEditText.getText()
		    .toString()));
	    mCharacter.setAcFeat(Integer.parseInt(mFeatEditText.getText()
		    .toString()));
	    mCharacter.setAcMisc(Integer.parseInt(mMiscEditText.getText()
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
	setContentView(R.layout.ac_detail);

	((Button) findViewById(R.id.ac_detail_cancel_button))
		.setOnClickListener(this);
	((Button) findViewById(R.id.ac_detail_ok_button))
		.setOnClickListener(this);

	mArmorEditText = (EditText) findViewById(R.id.ac_detail_armor_edit_text);
	mClassEditText = (EditText) findViewById(R.id.ac_detail_class_edit_text);
	mEnhEditText = (EditText) findViewById(R.id.ac_detail_enh_edit_text);
	mFeatEditText = (EditText) findViewById(R.id.ac_detail_feat_edit_text);
	mMiscEditText = (EditText) findViewById(R.id.ac_detail_misc_edit_text);
	mBaseTextView = (TextView) findViewById(R.id.ac_detail_base_text_view);
	mTitleTextView = (TextView) findViewById(R.id.ac_detail_title_text_view);
	mTotalTextView = (TextView) findViewById(R.id.ac_detail_total_text_view);
	mTotalScoreTextView = (TextView) findViewById(R.id.ac_detail_total_score_text_view);

	mArmorEditText
		.addTextChangedListener(new AcTextWatcher(mArmorEditText));
	mClassEditText
		.addTextChangedListener(new AcTextWatcher(mClassEditText));
	mEnhEditText.addTextChangedListener(new AcTextWatcher(mEnhEditText));
	mFeatEditText.addTextChangedListener(new AcTextWatcher(mFeatEditText));
	mMiscEditText.addTextChangedListener(new AcTextWatcher(mMiscEditText));
    }

    @Override
    protected void onStart() {
	super.onStart();
	fillData();
	mArmorEditText.requestFocus();
    }

    private void bindField(EditText editText) {
	switch (editText.getId()) {
	case R.id.ac_detail_armor_edit_text:
	    mCharacter.setAcArmor(Integer.parseInt(editText.getText()
		    .toString()));
	    break;
	case R.id.ac_detail_class_edit_text:
	    mCharacter.setAcClass(Integer.parseInt(editText.getText()
		    .toString()));
	    break;
	case R.id.ac_detail_enh_edit_text:
	    mCharacter
		    .setAcEnh(Integer.parseInt(editText.getText().toString()));
	    break;
	case R.id.ac_detail_feat_edit_text:
	    mCharacter.setAcFeat(Integer
		    .parseInt(editText.getText().toString()));
	    break;
	case R.id.ac_detail_misc_edit_text:
	    mCharacter.setAcMisc(Integer
		    .parseInt(editText.getText().toString()));
	    break;
	}
    }

    private void fillCalculatedData() {
	mTotalScoreTextView.setText(String.valueOf(mCharacter.getAc()));
    }

    private void fillData() {
	mArmorEditText.setText(String.valueOf(mCharacter.getAcArmor()));
	mClassEditText.setText(String.valueOf(mCharacter.getAcClass()));
	mEnhEditText.setText(String.valueOf(mCharacter.getAcEnh()));
	mFeatEditText.setText(String.valueOf(mCharacter.getAcFeat()));
	mMiscEditText.setText(String.valueOf(mCharacter.getAcMisc()));
	mBaseTextView.setText(String.valueOf(10 + mCharacter.getLevel() / 2));

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
