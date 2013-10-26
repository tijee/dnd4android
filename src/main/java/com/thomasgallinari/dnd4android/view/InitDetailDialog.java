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
 * A dialog that shows the detail of the character initiative.
 */
public class InitDetailDialog extends Dialog implements OnClickListener {

    class InitTextWatcher implements TextWatcher {

	@Override
	public void afterTextChanged(Editable s) {
	    validateData();
	    bindField();
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

    private int mAbilMod;
    private TextView mAbilTextView;
    private TextView mBaseTextView;
    private Character mCharacter;
    private EditText mMiscEditText;
    private TextView mTitleTextView;
    private TextView mTotalScoreTextView;
    private TextView mTotalTextView;

    /**
     * Creates a new {@link InitDetailDialog}.
     * 
     * @param context
     *            {@inheritDoc}
     * @param character
     *            the concerned character
     */
    public InitDetailDialog(Context context, Character character) {
	super(context, R.style.Theme_Dialog_DnD);
	mCharacter = character;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.init_detail_cancel_button:
	    cancel();
	    break;
	case R.id.init_detail_ok_button:
	    validateData();

	    mCharacter.setInitMisc(Integer.parseInt(mMiscEditText.getText()
		    .toString()));

	    dismiss();
	    break;
	}
    }

    /**
     * Sets the ability modifier to calculate the initiative.
     * 
     * @param pAbilMod
     *            the ability modifier
     */
    public void setAbilMod(int pAbilMod) {
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
	setContentView(R.layout.init_detail);

	((Button) findViewById(R.id.init_detail_cancel_button))
		.setOnClickListener(this);
	((Button) findViewById(R.id.init_detail_ok_button))
		.setOnClickListener(this);

	mMiscEditText = (EditText) findViewById(R.id.init_detail_misc_edit_text);
	mAbilTextView = (TextView) findViewById(R.id.init_detail_abil_text_view);
	mBaseTextView = (TextView) findViewById(R.id.init_detail_base_text_view);
	mTitleTextView = (TextView) findViewById(R.id.init_detail_title_text_view);
	mTotalTextView = (TextView) findViewById(R.id.init_detail_total_text_view);
	mTotalScoreTextView = (TextView) findViewById(R.id.init_detail_total_score_text_view);

	mMiscEditText.addTextChangedListener(new InitTextWatcher());
    }

    @Override
    protected void onStart() {
	super.onStart();
	fillData();
	mMiscEditText.requestFocus();
    }

    private void bindField() {
	mCharacter.setInitMisc(Integer.parseInt(mMiscEditText.getText()
		.toString()));
    }

    private void fillCalculatedData() {
	mTotalScoreTextView.setText(String.valueOf(mCharacter.getInit()));
    }

    private void fillData() {
	mMiscEditText.setText(String.valueOf(mCharacter.getInitMisc()));
	mAbilTextView.setText(String.valueOf(mAbilMod));
	mBaseTextView.setText(String.valueOf(mCharacter.getLevel() / 2));

	fillCalculatedData();
    }

    private void validateData() {
	if (mMiscEditText.getText().length() == 0) {
	    mMiscEditText.setText("0");
	}
	if (mMiscEditText.getText().toString().equals("-")) {
	    mMiscEditText.setText("-0");
	}
    }
}
