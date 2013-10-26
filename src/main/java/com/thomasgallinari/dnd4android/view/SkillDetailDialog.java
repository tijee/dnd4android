/**
 * Copyright (C) 2011 Thomas Gallinari
 */
package com.thomasgallinari.dnd4android.view;

import java.sql.SQLException;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.thomasgallinari.dnd4android.R;
import com.thomasgallinari.dnd4android.model.Ability;
import com.thomasgallinari.dnd4android.model.Skill;
import com.thomasgallinari.dnd4android.view.util.ErrorHandler;

/**
 * A dialog that shows the detail of a character skill.
 */
public class SkillDetailDialog extends Dialog implements
	OnCheckedChangeListener, OnClickListener {

    class SkillTextWatcher implements TextWatcher {

	private EditText mEditText;

	public SkillTextWatcher(EditText editText) {
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

    private TextView mAbilScoreTextView;
    private EditText mArmorEditText;
    private EditText mMiscEditText;
    private Skill mSkill;
    private TextView mSkillTextView;
    private TextView mTitleTextView;
    private TextView mTotalTextView;
    private CheckBox mTrainedCheckBox;

    /**
     * Creates a new {@link SkillDetailDialog}.
     * 
     * @param context
     *            {@inheritDoc}
     */
    public SkillDetailDialog(Context context) {
	super(context, R.style.Theme_Dialog_DnD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	mSkill.setTrained(isChecked);
	fillCalculatedData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.skill_detail_cancel_button:
	    try {
		((SkillsActivity) getOwnerActivity()).getHelper().getSkillDao()
			.refresh(mSkill);
	    } catch (SQLException e) {
		ErrorHandler.log(getClass(), "Failed to refresh the skill", e,
			getOwnerActivity().getString(
				R.string.error_skill_refresh),
			getOwnerActivity());
	    }
	    cancel();
	    break;
	case R.id.skill_detail_ok_button:
	    validateData(mArmorEditText);
	    validateData(mMiscEditText);

	    mSkill.setArmor(Integer.parseInt(mArmorEditText.getText()
		    .toString()));
	    mSkill
		    .setMisc(Integer.parseInt(mMiscEditText.getText()
			    .toString()));
	    mSkill.setTrained(mTrainedCheckBox.isChecked());

	    try {
		((SkillsActivity) getOwnerActivity()).getDatabaseHelper()
			.getSkillDao().update(mSkill);
	    } catch (SQLException e) {
		ErrorHandler.log(getClass(), "Failed to update the skill", e,
			getOwnerActivity().getString(
				R.string.error_skill_update),
			getOwnerActivity());
	    }

	    dismiss();
	    break;
	}
    }

    /**
     * Sets the given skill.
     * 
     * @param skill
     *            the concerned skill
     */
    public void setSkill(Skill skill) {
	mSkill = skill;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTitle(CharSequence title) {
	super.setTitle(title);
	mTitleTextView.setText(title);
	mSkillTextView.setText(title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTitle(int titleId) {
	super.setTitle(titleId);
	mTitleTextView.setText(titleId);
	mSkillTextView.setText(titleId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.skill_detail);

	((Button) findViewById(R.id.skill_detail_cancel_button))
		.setOnClickListener(this);
	((Button) findViewById(R.id.skill_detail_ok_button))
		.setOnClickListener(this);

	mTrainedCheckBox = (CheckBox) findViewById(R.id.skill_detail_trained_check_box);
	mArmorEditText = (EditText) findViewById(R.id.skill_detail_armor_penalty_edit_text);
	mMiscEditText = (EditText) findViewById(R.id.skill_detail_misc_edit_text);
	mAbilScoreTextView = (TextView) findViewById(R.id.skill_detail_abil_score_text_view);
	mSkillTextView = (TextView) findViewById(R.id.skill_detail_skill_text_view);
	mTotalTextView = (TextView) findViewById(R.id.skill_detail_total_text_view);
	mTitleTextView = (TextView) findViewById(R.id.skill_detail_title_text_view);

	mTrainedCheckBox.setOnCheckedChangeListener(this);

	mArmorEditText.addTextChangedListener(new SkillTextWatcher(
		mArmorEditText));
	mMiscEditText
		.addTextChangedListener(new SkillTextWatcher(mMiscEditText));
    }

    @Override
    protected void onStart() {
	super.onStart();
	fillData();
	mArmorEditText.requestFocus();
    }

    private void bindField(EditText editText) {
	switch (editText.getId()) {
	case R.id.skill_detail_armor_penalty_edit_text:
	    mSkill.setArmor(Integer.parseInt(editText.getText().toString()));
	    break;
	case R.id.skill_detail_misc_edit_text:
	    mSkill.setMisc(Integer.parseInt(editText.getText().toString()));
	    break;
	}
    }

    private void fillCalculatedData() {
	mTotalTextView.setText(String.valueOf(mSkill.getScore()));
    }

    private void fillData() {
	int abilMod = 0;
	if (mSkill.getAbility().equals(Ability.CHA)) {
	    abilMod = mSkill.getCharacter().getChaMod();
	} else if (mSkill.getAbility().equals(Ability.CON)) {
	    abilMod = mSkill.getCharacter().getConMod();
	} else if (mSkill.getAbility().equals(Ability.DEX)) {
	    abilMod = mSkill.getCharacter().getDexMod();
	} else if (mSkill.getAbility().equals(Ability.INT)) {
	    abilMod = mSkill.getCharacter().getIntelMod();
	} else if (mSkill.getAbility().equals(Ability.STR)) {
	    abilMod = mSkill.getCharacter().getStrMod();
	} else if (mSkill.getAbility().equals(Ability.WIS)) {
	    abilMod = mSkill.getCharacter().getWisMod();
	}

	mTrainedCheckBox.setChecked(mSkill.isTrained());
	mArmorEditText.setText(String.valueOf(mSkill.getArmor()));
	mMiscEditText.setText(String.valueOf(mSkill.getMisc()));
	mAbilScoreTextView.setText(String.valueOf(abilMod
		+ mSkill.getCharacter().getLevel() / 2));

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
