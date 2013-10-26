/**
 * Copyright (C) 2011 Thomas Gallinari
 */
package com.thomasgallinari.dndcharactersheet.view;

import java.sql.SQLException;

import android.os.Bundle;
import android.widget.EditText;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.thomasgallinari.dndcharactersheet.R;
import com.thomasgallinari.dndcharactersheet.db.DatabaseHelper;
import com.thomasgallinari.dndcharactersheet.model.Character;
import com.thomasgallinari.dndcharactersheet.view.util.ErrorHandler;

/**
 * An activity that shows miscellaneous notes about the game.
 */
public class NotesActivity extends OrmLiteBaseActivity<DatabaseHelper> {

    public static final String CHARACTER = "com.thomasgallinari.dndcharactersheet.CHARACTER";

    private Character mCharacter;
    private EditText mNotesEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.notes);

	mCharacter = (Character) getIntent().getSerializableExtra(CHARACTER);

	mNotesEditText = (EditText) findViewById(R.id.notes_edit_text);

	fillData();
    }

    @Override
    protected void onPause() {
	super.onPause();

	mCharacter.setNotes(mNotesEditText.getText().toString());
	try {
	    getHelper().getCharacterDao().update(mCharacter);
	} catch (SQLException e) {
	    ErrorHandler.log(getClass(), "Failed to update the character", e,
		    getString(R.string.error_character_update), this);
	}
    }

    private void fillData() {
	mNotesEditText.setText(mCharacter.getNotes());
    }
}
