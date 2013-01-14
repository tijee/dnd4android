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
 * A dialog that shows the detail of an attack's damage.
 */
public class AttackDamageDialog extends Dialog implements
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
    private EditText mEnhEditText;
    private EditText mFeatEditText;
    private EditText mMiscEditText;
    private TextView mTotalScoreTextView;

    /**
     * Creates a new {@link AttackDamageDialog}.
     * 
     * @param context
     *            {@inheritDoc}
     */
    public AttackDamageDialog(Context context) {
	super(context, R.style.Theme_Dialog_DnD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.attack_damage_cancel_button:
	    cancel();
	    break;
	case R.id.attack_damage_ok_button:
	    validateData(mAbilEditText);
	    validateData(mEnhEditText);
	    validateData(mFeatEditText);
	    validateData(mMiscEditText);

	    mAttack.setDamageAbil(Integer.parseInt(mAbilEditText.getText()
		    .toString()));
	    mAttack.setDamageEnh(Integer.parseInt(mEnhEditText.getText()
		    .toString()));
	    mAttack.setDamageFeat(Integer.parseInt(mFeatEditText.getText()
		    .toString()));
	    mAttack.setDamageMisc(Integer.parseInt(mMiscEditText.getText()
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
	setContentView(R.layout.attack_damage);

	((Button) findViewById(R.id.attack_damage_cancel_button))
		.setOnClickListener(this);
	((Button) findViewById(R.id.attack_damage_ok_button))
		.setOnClickListener(this);

	mAbilEditText = (EditText) findViewById(R.id.attack_damage_abil_edit_text);
	mEnhEditText = (EditText) findViewById(R.id.attack_damage_enh_edit_text);
	mFeatEditText = (EditText) findViewById(R.id.attack_damage_feat_edit_text);
	mMiscEditText = (EditText) findViewById(R.id.attack_damage_misc_edit_text);
	mTotalScoreTextView = (TextView) findViewById(R.id.attack_damage_total_score_text_view);

	mAbilEditText.addTextChangedListener(new AttackTextWatcher(
		mAbilEditText));
	mEnhEditText
		.addTextChangedListener(new AttackTextWatcher(mEnhEditText));
	mFeatEditText.addTextChangedListener(new AttackTextWatcher(
		mFeatEditText));
	mMiscEditText.addTextChangedListener(new AttackTextWatcher(
		mMiscEditText));
    }

    @Override
    protected void onStart() {
	super.onStart();
	fillData();
	mAbilEditText.requestFocus();
    }

    private void bindField(EditText editText) {
	switch (editText.getId()) {
	case R.id.attack_damage_abil_edit_text:
	    mAttack.setDamageAbil(Integer.parseInt(editText.getText()
		    .toString()));
	    break;
	case R.id.attack_damage_enh_edit_text:
	    mAttack.setDamageEnh(Integer
		    .parseInt(editText.getText().toString()));
	    break;
	case R.id.attack_damage_feat_edit_text:
	    mAttack.setDamageFeat(Integer.parseInt(editText.getText()
		    .toString()));
	    break;
	case R.id.attack_damage_misc_edit_text:
	    mAttack.setDamageMisc(Integer.parseInt(editText.getText()
		    .toString()));
	    break;
	}
    }

    private void fillCalculatedData() {
	mTotalScoreTextView.setText(String.valueOf(mAttack.getDamageScore()));
    }

    private void fillData() {
	mAbilEditText.setText(String.valueOf(mAttack.getDamageAbil()));
	mEnhEditText.setText(String.valueOf(mAttack.getDamageEnh()));
	mFeatEditText.setText(String.valueOf(mAttack.getDamageFeat()));
	mMiscEditText.setText(String.valueOf(mAttack.getDamageMisc()));

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
