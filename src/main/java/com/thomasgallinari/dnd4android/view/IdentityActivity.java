/**
 * Copyright (C) 2011 Thomas Gallinari
 */
package com.thomasgallinari.dnd4android.view;

import java.sql.SQLException;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.thomasgallinari.dnd4android.R;
import com.thomasgallinari.dnd4android.db.DatabaseHelper;
import com.thomasgallinari.dnd4android.model.Character;
import com.thomasgallinari.dnd4android.view.util.ErrorHandler;

/**
 * An activity that shows character information.
 */
public class IdentityActivity extends OrmLiteBaseActivity<DatabaseHelper>
	implements OnFocusChangeListener {

    public static final String CHARACTER = "com.thomasgallinari.dndcharactersheet.CHARACTER";

    private Character mCharacter;
    private EditText mClassEditText;
    private EditText mEpicDestinyEditText;
    private EditText mLevelEditText;
    private EditText mMiscEditText;
    private EditText mNameEditText;
    private EditText mParagonPathEditText;
    private EditText mRaceEditText;
    private EditText mXpEditText;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
	validateData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.identity);

	mCharacter = (Character) getIntent().getSerializableExtra(CHARACTER);

	mClassEditText = (EditText) findViewById(R.id.identity_class_edit_text);
	mEpicDestinyEditText = (EditText) findViewById(R.id.identity_epic_destiny_edit_text);
	mLevelEditText = (EditText) findViewById(R.id.identity_level_edit_text);
	mMiscEditText = (EditText) findViewById(R.id.identity_misc_edit_text);
	mNameEditText = (EditText) findViewById(R.id.identity_name_edit_text);
	mParagonPathEditText = (EditText) findViewById(R.id.identity_paragon_path_edit_text);
	mRaceEditText = (EditText) findViewById(R.id.identity_race_edit_text);
	mXpEditText = (EditText) findViewById(R.id.identity_xp_edit_text);

	mXpEditText.setOnFocusChangeListener(this);

	fillData();
    }

    @Override
    protected void onPause() {
	super.onPause();
	validateData();

	mCharacter.setClazz(mClassEditText.getText().toString());
	mCharacter.setEpicDestiny(mEpicDestinyEditText.getText().toString());
	mCharacter.setLevel(Integer.parseInt(mLevelEditText.getText()
		.toString()));
	mCharacter.setMisc(mMiscEditText.getText().toString());
	mCharacter.setName(mNameEditText.getText().toString());
	mCharacter.setParagonPath(mParagonPathEditText.getText().toString());
	mCharacter.setRace(mRaceEditText.getText().toString());
	mCharacter.setXp(Integer.parseInt(mXpEditText.getText().toString()));

	try {
	    getHelper().getCharacterDao().update(mCharacter);
	} catch (SQLException e) {
	    ErrorHandler.log(getClass(), "Failed to update the character", e,
		    getString(R.string.error_character_update), this);
	}
    }

    private void fillData() {
	mClassEditText.setText(mCharacter.getClazz());
	mEpicDestinyEditText.setText(mCharacter.getEpicDestiny());
	mLevelEditText.setText(String.valueOf(mCharacter.getLevel()));
	mMiscEditText.setText(mCharacter.getMisc());
	mNameEditText.setText(mCharacter.getName());
	mParagonPathEditText.setText(mCharacter.getParagonPath());
	mRaceEditText.setText(mCharacter.getRace());
	mXpEditText.setText(String.valueOf(mCharacter.getXp()));
    }

    private void validateData() {
	if (mLevelEditText.getText().length() == 0
		|| Integer.parseInt(mLevelEditText.getText().toString()) < 0) {
	    mLevelEditText.setText("0");
	}
	if (mXpEditText.getText().length() == 0) {
	    mXpEditText.setText("0");
	} else {
	    int xp = 0;
	    String[] numbers = mXpEditText.getText().toString().split("\\+");
	    for (String number : numbers) {
		if (number.length() > 0) {
		    xp += Integer.parseInt(number);
		}
	    }
	    mXpEditText.setText(String.valueOf(xp));
	}
    }
}
