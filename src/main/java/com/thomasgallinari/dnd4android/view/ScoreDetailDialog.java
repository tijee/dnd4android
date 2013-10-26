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
 * A dialog that shows the detail of the character FORT, REF, or WILL.
 */
public class ScoreDetailDialog extends Dialog implements OnClickListener {

    class ScoreTextWatcher implements TextWatcher {

	private EditText mEditText;

	public ScoreTextWatcher(EditText editText) {
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

    private int mAbilKey;
    private int mAbilMod;
    private TextView mAbilTextView;
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
     * Creates a new {@link ScoreDetailDialog}.
     * 
     * @param context
     *            {@inheritDoc}
     * @param character
     *            the concerned character
     */
    public ScoreDetailDialog(Context context, Character character) {
	super(context, R.style.Theme_Dialog_DnD);
	this.mCharacter = character;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.score_detail_cancel_button:
	    cancel();
	    break;
	case R.id.score_detail_ok_button:
	    validateData(mClassEditText);
	    validateData(mEnhEditText);
	    validateData(mFeatEditText);
	    validateData(mMiscEditText);

	    switch (mAbilKey) {
	    case Character.FORT:
		mCharacter.setFortClass(Integer.parseInt(mClassEditText
			.getText().toString()));
		mCharacter.setFortEnh(Integer.parseInt(mEnhEditText.getText()
			.toString()));
		mCharacter.setFortFeat(Integer.parseInt(mFeatEditText.getText()
			.toString()));
		mCharacter.setFortMisc(Integer.parseInt(mMiscEditText.getText()
			.toString()));
		break;
	    case Character.REF:
		mCharacter.setRefClass(Integer.parseInt(mClassEditText
			.getText().toString()));
		mCharacter.setRefEnh(Integer.parseInt(mEnhEditText.getText()
			.toString()));
		mCharacter.setRefFeat(Integer.parseInt(mFeatEditText.getText()
			.toString()));
		mCharacter.setRefMisc(Integer.parseInt(mMiscEditText.getText()
			.toString()));
		break;
	    case Character.WILL:
		mCharacter.setWillClass(Integer.parseInt(mClassEditText
			.getText().toString()));
		mCharacter.setWillEnh(Integer.parseInt(mEnhEditText.getText()
			.toString()));
		mCharacter.setWillFeat(Integer.parseInt(mFeatEditText.getText()
			.toString()));
		mCharacter.setWillMisc(Integer.parseInt(mMiscEditText.getText()
			.toString()));
		break;
	    }

	    dismiss();
	    break;
	}
    }

    /**
     * Sets the ability modifier to calculate the score.
     * 
     * @param pAbilKey
     *            the ability key to save it later
     * @param pAbilMod
     *            the ability modifier
     */
    public void setAbilMod(int pAbilKey, int pAbilMod) {
	mAbilKey = pAbilKey;
	mAbilMod = pAbilMod;
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
	setContentView(R.layout.score_detail);

	((Button) findViewById(R.id.score_detail_cancel_button))
		.setOnClickListener(this);
	((Button) findViewById(R.id.score_detail_ok_button))
		.setOnClickListener(this);

	mClassEditText = (EditText) findViewById(R.id.score_detail_class_edit_text);
	mEnhEditText = (EditText) findViewById(R.id.score_detail_enh_edit_text);
	mFeatEditText = (EditText) findViewById(R.id.score_detail_feat_edit_text);
	mMiscEditText = (EditText) findViewById(R.id.score_detail_misc_edit_text);
	mAbilTextView = (TextView) findViewById(R.id.score_detail_abil_text_view);
	mBaseTextView = (TextView) findViewById(R.id.score_detail_base_text_view);
	mTitleTextView = (TextView) findViewById(R.id.score_detail_title_text_view);
	mTotalTextView = (TextView) findViewById(R.id.score_detail_total_text_view);
	mTotalScoreTextView = (TextView) findViewById(R.id.score_detail_total_score_text_view);

	mClassEditText.addTextChangedListener(new ScoreTextWatcher(
		mClassEditText));
	mEnhEditText.addTextChangedListener(new ScoreTextWatcher(mEnhEditText));
	mFeatEditText
		.addTextChangedListener(new ScoreTextWatcher(mFeatEditText));
	mMiscEditText
		.addTextChangedListener(new ScoreTextWatcher(mMiscEditText));
    }

    @Override
    protected void onStart() {
	super.onStart();
	fillData();
	mClassEditText.requestFocus();
    }

    private void bindField(EditText editText) {
	switch (editText.getId()) {
	case R.id.score_detail_class_edit_text:
	    switch (mAbilKey) {
	    case Character.FORT:
		mCharacter.setFortClass(Integer.parseInt(editText.getText()
			.toString()));
		break;
	    case Character.REF:
		mCharacter.setRefClass(Integer.parseInt(editText.getText()
			.toString()));
		break;
	    case Character.WILL:
		mCharacter.setWillClass(Integer.parseInt(editText.getText()
			.toString()));
		break;
	    }
	    break;
	case R.id.score_detail_enh_edit_text:
	    switch (mAbilKey) {
	    case Character.FORT:
		mCharacter.setFortEnh(Integer.parseInt(editText.getText()
			.toString()));
		break;
	    case Character.REF:
		mCharacter.setRefEnh(Integer.parseInt(editText.getText()
			.toString()));
		break;
	    case Character.WILL:
		mCharacter.setWillEnh(Integer.parseInt(editText.getText()
			.toString()));
		break;
	    }
	    break;
	case R.id.score_detail_feat_edit_text:
	    switch (mAbilKey) {
	    case Character.FORT:
		mCharacter.setFortFeat(Integer.parseInt(editText.getText()
			.toString()));
		break;
	    case Character.REF:
		mCharacter.setRefFeat(Integer.parseInt(editText.getText()
			.toString()));
		break;
	    case Character.WILL:
		mCharacter.setWillFeat(Integer.parseInt(editText.getText()
			.toString()));
		break;
	    }
	    break;
	case R.id.score_detail_misc_edit_text:
	    switch (mAbilKey) {
	    case Character.FORT:
		mCharacter.setFortMisc(Integer.parseInt(editText.getText()
			.toString()));
		break;
	    case Character.REF:
		mCharacter.setRefMisc(Integer.parseInt(editText.getText()
			.toString()));
		break;
	    case Character.WILL:
		mCharacter.setWillMisc(Integer.parseInt(editText.getText()
			.toString()));
		break;
	    }
	    break;
	}
    }

    private void fillCalculatedData() {
	switch (mAbilKey) {
	case Character.FORT:
	    mTotalScoreTextView.setText(String.valueOf(mCharacter.getFort()));
	    break;
	case Character.REF:
	    mTotalScoreTextView.setText(String.valueOf(mCharacter.getRef()));
	    break;
	case Character.WILL:
	    mTotalScoreTextView.setText(String.valueOf(mCharacter.getWill()));
	    break;
	}
    }

    private void fillData() {
	int clas = 0;
	int enh = 0;
	int feat = 0;
	int misc = 0;

	switch (mAbilKey) {
	case Character.FORT:
	    clas = mCharacter.getFortClass();
	    enh = mCharacter.getFortEnh();
	    feat = mCharacter.getFortFeat();
	    misc = mCharacter.getFortMisc();
	    break;
	case Character.REF:
	    clas = mCharacter.getRefClass();
	    enh = mCharacter.getRefEnh();
	    feat = mCharacter.getRefFeat();
	    misc = mCharacter.getRefMisc();
	    break;
	case Character.WILL:
	    clas = mCharacter.getWillClass();
	    enh = mCharacter.getWillEnh();
	    feat = mCharacter.getWillFeat();
	    misc = mCharacter.getWillMisc();
	    break;
	}

	mClassEditText.setText(String.valueOf(clas));
	mEnhEditText.setText(String.valueOf(enh));
	mFeatEditText.setText(String.valueOf(feat));
	mMiscEditText.setText(String.valueOf(misc));
	mAbilTextView.setText(String.valueOf(mAbilMod));
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
