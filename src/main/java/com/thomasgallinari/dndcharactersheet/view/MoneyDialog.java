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

import com.thomasgallinari.dndcharactersheet.R;
import com.thomasgallinari.dndcharactersheet.model.Character;

/**
 * A dialog that shows the money of the character.
 */
public class MoneyDialog extends Dialog implements OnClickListener {

    class MoneyTextWatcher implements TextWatcher {

	private EditText mEditText;

	public MoneyTextWatcher(EditText editText) {
	    mEditText = editText;
	}

	@Override
	public void afterTextChanged(Editable s) {
	    validateData(mEditText);
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

    private EditText mAstralDiamondsEditText;
    private Character mCharacter;
    private EditText mCopperPiecesEditText;
    private EditText mGoldPiecesEditText;
    private EditText mPlatinumPiecesEditText;
    private EditText mSilverPiecesEditText;

    /**
     * Creates a new {@link MoneyDialog}.
     * 
     * @param context
     *            {@inheritDoc}
     * @param character
     *            the concerned character
     */
    public MoneyDialog(Context context, Character character) {
	super(context, R.style.Theme_Dialog_DnD);
	mCharacter = character;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.money_cancel_button:
	    cancel();
	    break;
	case R.id.money_ok_button:
	    validateData(mCopperPiecesEditText);
	    validateData(mSilverPiecesEditText);
	    validateData(mGoldPiecesEditText);
	    validateData(mPlatinumPiecesEditText);
	    validateData(mAstralDiamondsEditText);

	    mCharacter.setCopperPieces(Integer.parseInt(mCopperPiecesEditText
		    .getText().toString()));
	    mCharacter.setSilverPieces(Integer.parseInt(mSilverPiecesEditText
		    .getText().toString()));
	    mCharacter.setGoldPieces(Integer.parseInt(mGoldPiecesEditText
		    .getText().toString()));
	    mCharacter.setPlatinumPieces(Integer
		    .parseInt(mPlatinumPiecesEditText.getText().toString()));
	    mCharacter.setAstralDiamonds(Integer
		    .parseInt(mAstralDiamondsEditText.getText().toString()));

	    dismiss();
	    break;
	}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.money);

	((Button) findViewById(R.id.money_cancel_button))
		.setOnClickListener(this);
	((Button) findViewById(R.id.money_ok_button)).setOnClickListener(this);

	mCopperPiecesEditText = (EditText) findViewById(R.id.money_copper_pieces_edit_text);
	mSilverPiecesEditText = (EditText) findViewById(R.id.money_silver_pieces_edit_text);
	mGoldPiecesEditText = (EditText) findViewById(R.id.money_gold_pieces_edit_text);
	mPlatinumPiecesEditText = (EditText) findViewById(R.id.money_platinum_pieces_edit_text);
	mAstralDiamondsEditText = (EditText) findViewById(R.id.money_astral_diamonds_edit_text);

	mCopperPiecesEditText.addTextChangedListener(new MoneyTextWatcher(
		mCopperPiecesEditText));
	mSilverPiecesEditText.addTextChangedListener(new MoneyTextWatcher(
		mSilverPiecesEditText));
	mGoldPiecesEditText.addTextChangedListener(new MoneyTextWatcher(
		mGoldPiecesEditText));
	mPlatinumPiecesEditText.addTextChangedListener(new MoneyTextWatcher(
		mPlatinumPiecesEditText));
	mAstralDiamondsEditText.addTextChangedListener(new MoneyTextWatcher(
		mAstralDiamondsEditText));
    }

    @Override
    protected void onStart() {
	super.onStart();
	fillData();
	mCopperPiecesEditText.requestFocus();
    }

    private void fillData() {
	mCopperPiecesEditText.setText(String.valueOf(mCharacter
		.getCopperPieces()));
	mSilverPiecesEditText.setText(String.valueOf(mCharacter
		.getSilverPieces()));
	mGoldPiecesEditText.setText(String.valueOf(mCharacter.getGoldPieces()));
	mPlatinumPiecesEditText.setText(String.valueOf(mCharacter
		.getPlatinumPieces()));
	mAstralDiamondsEditText.setText(String.valueOf(mCharacter
		.getAstralDiamonds()));
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
