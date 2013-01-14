/**
 * Copyright (C) 2011 Thomas Gallinari
 */
package com.thomasgallinari.dndcharactersheet.view;

import java.sql.SQLException;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.thomasgallinari.dndcharactersheet.R;
import com.thomasgallinari.dndcharactersheet.model.Character;
import com.thomasgallinari.dndcharactersheet.model.Skill;
import com.thomasgallinari.dndcharactersheet.view.util.ErrorHandler;
import com.thomasgallinari.dndcharactersheet.view.util.TextViewBinder;

/**
 * A dialog to create a new character.
 */
public class NewCharacterDialog extends Dialog implements
	android.view.View.OnClickListener {

    private EditText mClassEditText;
    private EditText mLevelEditText;
    private EditText mNameEditText;
    private Button mOkButton;
    private EditText mRaceEditText;

    /**
     * Creates a new {@link NewCharacterDialog}.
     * 
     * @param pContext
     *            {@inheritDoc}
     */
    public NewCharacterDialog(Context pContext) {
	super(pContext, R.style.Theme_Dialog_DnD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.new_character_cancel_button:
	    cancel();
	    break;
	case R.id.new_character_ok_button:
	    createCharacter();
	    dismiss();
	    break;
	}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.new_character);

	mOkButton = (Button) findViewById(R.id.new_character_ok_button);
	mOkButton.setOnClickListener(this);
	((Button) findViewById(R.id.new_character_cancel_button))
		.setOnClickListener(this);

	mClassEditText = (EditText) findViewById(R.id.new_character_class_edit_text);
	mLevelEditText = (EditText) findViewById(R.id.new_character_level_edit_text);
	mNameEditText = (EditText) findViewById(R.id.new_character_name_edit_text);
	mRaceEditText = (EditText) findViewById(R.id.new_character_race_edit_text);

	TextViewBinder binder = new TextViewBinder(mOkButton, new TextView[] {
		mNameEditText, mLevelEditText });
	mNameEditText.addTextChangedListener(binder);
	mLevelEditText.addTextChangedListener(binder);
    }

    @Override
    protected void onStart() {
	super.onStart();
	mNameEditText.requestFocus();
    }

    @Override
    protected void onStop() {
	super.onStop();
	mClassEditText.setText("");
	mLevelEditText.setText("");
	mNameEditText.setText("");
	mRaceEditText.setText("");
    }

    private void createCharacter() {
	Character character = new Character();
	character.setClazz(mClassEditText.getText().toString());
	character.setLevel(Integer
		.parseInt(mLevelEditText.getText().toString()));
	character.setName(mNameEditText.getText().toString());
	character.setRace(mRaceEditText.getText().toString());

	try {
	    ((CharactersActivity) getOwnerActivity()).getDatabaseHelper()
		    .getCharacterDao().create(character);
	} catch (SQLException e) {
	    ErrorHandler.log(getClass(), "Failed to create the character", e,
		    getOwnerActivity().getString(
			    R.string.error_character_create),
		    getOwnerActivity());
	}

	for (String[] defaultSkill : Skill.DEFAULT_SKILLS) {
	    Skill skill = new Skill();
	    skill.setCharacter(character);
	    skill.setName(defaultSkill[0]);
	    skill.setAbility(defaultSkill[1]);
	    try {
		((CharactersActivity) getOwnerActivity()).getDatabaseHelper()
			.getSkillDao().create(skill);
	    } catch (SQLException e) {
		ErrorHandler.log(getClass(), "Failed to create the skill", e,
			getOwnerActivity().getString(
				R.string.error_skill_create),
			getOwnerActivity());
	    }
	}
    }
}
