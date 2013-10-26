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
 * A dialog that shows the detail of the character movement.
 */
public class MovDetailDialog extends Dialog implements OnClickListener {

    class MovTextWatcher implements TextWatcher {

	private EditText mEditText;

	public MovTextWatcher(EditText editText) {
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
    private EditText mBaseEditText;
    private Character mCharacter;
    private EditText mItemEditText;
    private EditText mMiscEditText;
    private TextView mTitleTextView;
    private TextView mTotalScoreTextView;
    private TextView mTotalTextView;

    /**
     * Creates a new {@link MovDetailDialog}.
     * 
     * @param context
     *            {@inheritDoc}
     * @param character
     *            the concerned character
     */

    public MovDetailDialog(Context context, Character character) {
	super(context, R.style.Theme_Dialog_DnD);
	mCharacter = character;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.mov_detail_cancel_button:
	    cancel();
	    break;
	case R.id.mov_detail_ok_button:
	    validateData(mArmorEditText);
	    validateData(mBaseEditText);
	    validateData(mItemEditText);
	    validateData(mMiscEditText);

	    mCharacter.setMovArmor(Integer.parseInt(mArmorEditText.getText()
		    .toString()));
	    mCharacter.setMovBase(Integer.parseInt(mBaseEditText.getText()
		    .toString()));
	    mCharacter.setMovItem(Integer.parseInt(mItemEditText.getText()
		    .toString()));
	    mCharacter.setMovMisc(Integer.parseInt(mMiscEditText.getText()
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
	setContentView(R.layout.mov_detail);

	((Button) findViewById(R.id.mov_detail_cancel_button))
		.setOnClickListener(this);
	((Button) findViewById(R.id.mov_detail_ok_button))
		.setOnClickListener(this);

	mArmorEditText = (EditText) findViewById(R.id.mov_detail_armor_edit_text);
	mBaseEditText = (EditText) findViewById(R.id.mov_detail_base_edit_text);
	mItemEditText = (EditText) findViewById(R.id.mov_detail_item_edit_text);
	mMiscEditText = (EditText) findViewById(R.id.mov_detail_misc_edit_text);
	mTitleTextView = (TextView) findViewById(R.id.mov_detail_title_text_view);
	mTotalTextView = (TextView) findViewById(R.id.mov_detail_total_text_view);
	mTotalScoreTextView = (TextView) findViewById(R.id.mov_detail_total_score_text_view);

	mArmorEditText
		.addTextChangedListener(new MovTextWatcher(mArmorEditText));
	mBaseEditText.addTextChangedListener(new MovTextWatcher(mBaseEditText));
	mItemEditText.addTextChangedListener(new MovTextWatcher(mItemEditText));
	mMiscEditText.addTextChangedListener(new MovTextWatcher(mMiscEditText));
    }

    @Override
    protected void onStart() {
	super.onStart();
	fillData();
	mBaseEditText.requestFocus();
    }

    private void bindField(EditText editText) {
	switch (editText.getId()) {
	case R.id.mov_detail_armor_edit_text:
	    mCharacter.setMovArmor(Integer.parseInt(editText.getText()
		    .toString()));
	    break;
	case R.id.mov_detail_base_edit_text:
	    mCharacter.setMovBase(Integer.parseInt(editText.getText()
		    .toString()));
	    break;
	case R.id.mov_detail_item_edit_text:
	    mCharacter.setMovItem(Integer.parseInt(editText.getText()
		    .toString()));
	    break;
	case R.id.mov_detail_misc_edit_text:
	    mCharacter.setMovMisc(Integer.parseInt(editText.getText()
		    .toString()));
	    break;
	}
    }

    private void fillCalculatedData() {
	mTotalScoreTextView.setText(String.valueOf(mCharacter.getMov()));
    }

    private void fillData() {
	mArmorEditText.setText(String.valueOf(mCharacter.getMovArmor()));
	mBaseEditText.setText(String.valueOf(mCharacter.getMovBase()));
	mItemEditText.setText(String.valueOf(mCharacter.getMovItem()));
	mMiscEditText.setText(String.valueOf(mCharacter.getMovMisc()));

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
