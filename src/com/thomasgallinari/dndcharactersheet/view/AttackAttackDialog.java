/**
 * Copyright (C) 2011 Thomas Gallinari
 */
package com.thomasgallinari.dndcharactersheet.view;

import java.sql.SQLException;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.thomasgallinari.dndcharactersheet.R;
import com.thomasgallinari.dndcharactersheet.model.Attack;
import com.thomasgallinari.dndcharactersheet.view.util.ErrorHandler;

/**
 * A dialog that shows the detail of an attack.
 */
public class AttackAttackDialog extends Dialog implements
	android.view.View.OnClickListener {

    class AttackTextWatcher implements TextWatcher {

	private EditText mEditText;

	public AttackTextWatcher(EditText editText) {
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

    private EditText mAbilEditText;
    private Attack mAttack;
    private TextView mBaseTextView;
    private EditText mClassEditText;
    private EditText mEnhEditText;
    private EditText mFeatEditText;
    private EditText mMiscEditText;
    private EditText mProfEditText;
    private TextView mTotalScoreTextView;

    /**
     * Creates a new {@link AttackAttackDialog}.
     * 
     * @param context
     *            {@inheritDoc}
     */
    public AttackAttackDialog(Context context) {
	super(context, R.style.Theme_Dialog_DnD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.attack_attack_cancel_button:
	    cancel();
	    break;
	case R.id.attack_attack_ok_button:
	    validateData(mAbilEditText);
	    validateData(mClassEditText);
	    validateData(mEnhEditText);
	    validateData(mFeatEditText);
	    validateData(mMiscEditText);
	    validateData(mProfEditText);

	    mAttack.setAttackAbil(Integer.parseInt(mAbilEditText.getText()
		    .toString()));
	    mAttack.setAttackClass(Integer.parseInt(mClassEditText.getText()
		    .toString()));
	    mAttack.setAttackEnh(Integer.parseInt(mEnhEditText.getText()
		    .toString()));
	    mAttack.setAttackFeat(Integer.parseInt(mFeatEditText.getText()
		    .toString()));
	    mAttack.setAttackMisc(Integer.parseInt(mMiscEditText.getText()
		    .toString()));
	    mAttack.setAttackProf(Integer.parseInt(mProfEditText.getText()
		    .toString()));

	    try {
		((AttacksActivity) getOwnerActivity()).getDatabaseHelper()
			.getAttackDao().update(mAttack);
	    } catch (SQLException e) {
		ErrorHandler.log(getClass(), "Failed to update the attack", e,
			getOwnerActivity().getString(
				R.string.error_attack_update),
			getOwnerActivity());
	    }

	    dismiss();
	    break;
	}
    }

    /**
     * Sets the attack to be edited.
     * 
     * @param attack
     *            the concerned {@link Attack}
     */
    public void setAttack(Attack attack) {
	mAttack = attack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.attack_attack);

	((Button) findViewById(R.id.attack_attack_cancel_button))
		.setOnClickListener(this);
	((Button) findViewById(R.id.attack_attack_ok_button))
		.setOnClickListener(this);

	mAbilEditText = (EditText) findViewById(R.id.attack_attack_abil_edit_text);
	mBaseTextView = (TextView) findViewById(R.id.attack_attack_base_text_view);
	mClassEditText = (EditText) findViewById(R.id.attack_attack_class_edit_text);
	mEnhEditText = (EditText) findViewById(R.id.attack_attack_enh_edit_text);
	mFeatEditText = (EditText) findViewById(R.id.attack_attack_feat_edit_text);
	mMiscEditText = (EditText) findViewById(R.id.attack_attack_misc_edit_text);
	mProfEditText = (EditText) findViewById(R.id.attack_attack_prof_edit_text);
	mTotalScoreTextView = (TextView) findViewById(R.id.attack_attack_total_score_text_view);

	mAbilEditText.addTextChangedListener(new AttackTextWatcher(
		mAbilEditText));
	mClassEditText.addTextChangedListener(new AttackTextWatcher(
		mClassEditText));
	mEnhEditText
		.addTextChangedListener(new AttackTextWatcher(mEnhEditText));
	mFeatEditText.addTextChangedListener(new AttackTextWatcher(
		mFeatEditText));
	mMiscEditText.addTextChangedListener(new AttackTextWatcher(
		mMiscEditText));
	mProfEditText.addTextChangedListener(new AttackTextWatcher(
		mProfEditText));
    }

    @Override
    protected void onStart() {
	super.onStart();
	fillData();
	mAbilEditText.requestFocus();
    }

    private void bindField(EditText editText) {
	switch (editText.getId()) {
	case R.id.attack_attack_abil_edit_text:
	    mAttack.setAttackAbil(Integer.parseInt(editText.getText()
		    .toString()));
	    break;
	case R.id.attack_attack_class_edit_text:
	    mAttack.setAttackClass(Integer.parseInt(editText.getText()
		    .toString()));
	    break;
	case R.id.attack_attack_enh_edit_text:
	    mAttack.setAttackEnh(Integer
		    .parseInt(editText.getText().toString()));
	    break;
	case R.id.attack_attack_feat_edit_text:
	    mAttack.setAttackFeat(Integer.parseInt(editText.getText()
		    .toString()));
	    break;
	case R.id.attack_attack_misc_edit_text:
	    mAttack.setAttackMisc(Integer.parseInt(editText.getText()
		    .toString()));
	    break;
	case R.id.attack_attack_prof_edit_text:
	    mAttack.setAttackProf(Integer.parseInt(editText.getText()
		    .toString()));
	    break;
	}
    }

    private void fillCalculatedData() {
	mTotalScoreTextView.setText(String.valueOf(mAttack.getAttackScore()));
    }

    private void fillData() {
	mBaseTextView.setText(String
		.valueOf(mAttack.getCharacter().getLevel() / 2));
	mAbilEditText.setText(String.valueOf(mAttack.getAttackAbil()));
	mClassEditText.setText(String.valueOf(mAttack.getAttackClass()));
	mEnhEditText.setText(String.valueOf(mAttack.getAttackEnh()));
	mFeatEditText.setText(String.valueOf(mAttack.getAttackFeat()));
	mMiscEditText.setText(String.valueOf(mAttack.getAttackMisc()));
	mProfEditText.setText(String.valueOf(mAttack.getAttackProf()));

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
