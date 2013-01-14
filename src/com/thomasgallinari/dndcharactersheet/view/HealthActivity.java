/**
 * Copyright (C) 2011 Thomas Gallinari
 */
package com.thomasgallinari.dndcharactersheet.view;

import java.sql.SQLException;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.thomasgallinari.dndcharactersheet.R;
import com.thomasgallinari.dndcharactersheet.db.DatabaseHelper;
import com.thomasgallinari.dndcharactersheet.model.Character;
import com.thomasgallinari.dndcharactersheet.view.util.ErrorHandler;

/**
 * An activity that shows character health (hit points and action points).
 */
public class HealthActivity extends OrmLiteBaseActivity<DatabaseHelper>
	implements OnCheckedChangeListener, OnClickListener, OnDismissListener,
	OnCancelListener {

    class HealthTextWatcher implements TextWatcher {

	private EditText mEditText;

	public HealthTextWatcher(EditText editText) {
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

    public static final String CHARACTER = "com.thomasgallinari.dndcharactersheet.CHARACTER";

    private static final int DIALOG_HP_DETAIL = 1;
    private static final int DIALOG_SURGE_VALUE_DETAIL = 2;
    private static final int DIALOG_SURGES_PER_DAY_DETAIL = 3;

    private Character mCharacter;
    private int mClickedButton;
    private EditText mCurrentActionPointsEditText;
    private EditText mCurrentHpEditText;
    private EditText mCurrentSurgesEditText;
    private CheckBox mDeathSavingFailCheckBox1;
    private CheckBox mDeathSavingFailCheckBox2;
    private CheckBox mDeathSavingFailCheckBox3;
    private HpDetailDialog mHpDialog;
    private Button mHpMaxButton;
    private EditText mMaxActionPointsEditText;
    private EditText mModifiersEditText;
    private CheckBox mSecondWindCheckBox;
    private SurgesPerDayDetailDialog mSurgePerDayDialog;
    private Button mSurgesPerDayButton;
    private Button mSurgeValueButton;
    private SurgeValueDetailDialog mSurgeValueDialog;
    private EditText mTempHpEditText;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCancel(DialogInterface dialog) {
	try {
	    getHelper().getCharacterDao().refresh(mCharacter);
	} catch (SQLException e) {
	    ErrorHandler.log(getClass(), "Failed to refresh the character", e,
		    getString(R.string.error_character_refresh), this);
	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	mDeathSavingFailCheckBox1.setClickable(false);
	mDeathSavingFailCheckBox2.setClickable(false);
	mDeathSavingFailCheckBox3.setClickable(false);
	buttonView.setClickable(true);
	switch (buttonView.getId()) {
	case R.id.health_death_saving_1_check_box:
	    if (isChecked) {
		mDeathSavingFailCheckBox2.setClickable(true);
	    }
	    break;
	case R.id.health_death_saving_2_check_box:
	    if (isChecked) {
		mDeathSavingFailCheckBox3.setClickable(true);
	    } else {
		mDeathSavingFailCheckBox1.setClickable(true);
	    }
	    break;
	case R.id.health_death_saving_3_check_box:
	    if (!isChecked) {
		mDeathSavingFailCheckBox2.setClickable(true);
	    }
	    break;
	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
	mClickedButton = v.getId();
	switch (mClickedButton) {
	case R.id.health_hp_max_button:
	    showDialog(DIALOG_HP_DETAIL);
	    break;
	case R.id.health_surge_value_button:
	    showDialog(DIALOG_SURGE_VALUE_DETAIL);
	    break;
	case R.id.health_surges_max_button:
	    showDialog(DIALOG_SURGES_PER_DAY_DETAIL);
	    break;
	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDismiss(DialogInterface dialog) {
	try {
	    getHelper().getCharacterDao().update(mCharacter);
	} catch (SQLException e) {
	    ErrorHandler.log(getClass(), "Failed to update the character", e,
		    getString(R.string.error_character_update), this);
	}

	switch (mClickedButton) {
	case R.id.health_hp_max_button:
	    fillHpMax();
	    updateHpColor();
	case R.id.health_surge_value_button:
	    fillSurgeValue();
	    break;
	case R.id.health_surges_max_button:
	    fillSurgesPerDay();
	    break;
	}
    }

    /**
     * Refreshes the data after an external event (damage, surge, rest).
     */
    public void refresh() {
	fillData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	mCharacter = (Character) getIntent().getSerializableExtra(CHARACTER);

	setContentView(R.layout.health);

	mCurrentActionPointsEditText = (EditText) findViewById(R.id.health_action_points_current_edit_text);
	mCurrentHpEditText = (EditText) findViewById(R.id.health_hp_current_edit_text);
	mCurrentSurgesEditText = (EditText) findViewById(R.id.health_surges_current_edit_text);
	mDeathSavingFailCheckBox1 = (CheckBox) findViewById(R.id.health_death_saving_1_check_box);
	mDeathSavingFailCheckBox2 = (CheckBox) findViewById(R.id.health_death_saving_2_check_box);
	mDeathSavingFailCheckBox3 = (CheckBox) findViewById(R.id.health_death_saving_3_check_box);
	mHpMaxButton = (Button) findViewById(R.id.health_hp_max_button);
	mMaxActionPointsEditText = (EditText) findViewById(R.id.health_action_points_max_edit_text);
	mModifiersEditText = (EditText) findViewById(R.id.health_modifiers_edit_text);
	mSecondWindCheckBox = (CheckBox) findViewById(R.id.health_second_wind_check_box);
	mSurgesPerDayButton = (Button) findViewById(R.id.health_surges_max_button);
	mSurgeValueButton = (Button) findViewById(R.id.health_surge_value_button);
	mTempHpEditText = (EditText) findViewById(R.id.health_hp_temp_edit_text);

	mDeathSavingFailCheckBox1.setOnCheckedChangeListener(this);
	mDeathSavingFailCheckBox2.setOnCheckedChangeListener(this);
	mDeathSavingFailCheckBox3.setOnCheckedChangeListener(this);

	mCurrentActionPointsEditText
		.addTextChangedListener(new HealthTextWatcher(
			mCurrentActionPointsEditText));
	mCurrentHpEditText.addTextChangedListener(new HealthTextWatcher(
		mCurrentHpEditText));
	mCurrentSurgesEditText.addTextChangedListener(new HealthTextWatcher(
		mCurrentSurgesEditText));
	mMaxActionPointsEditText.addTextChangedListener(new HealthTextWatcher(
		mMaxActionPointsEditText));
	mTempHpEditText.addTextChangedListener(new HealthTextWatcher(
		mTempHpEditText));

	fillData();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
	Dialog dialog = null;
	switch (id) {
	case DIALOG_HP_DETAIL:
	    if (mHpDialog == null) {
		mHpDialog = new HpDetailDialog(this, mCharacter);
	    }
	    dialog = mHpDialog;
	    break;
	case DIALOG_SURGE_VALUE_DETAIL:
	    if (mSurgeValueDialog == null) {
		mSurgeValueDialog = new SurgeValueDetailDialog(this, mCharacter);
	    }
	    dialog = mSurgeValueDialog;
	    break;
	case DIALOG_SURGES_PER_DAY_DETAIL:
	    if (mSurgePerDayDialog == null) {
		mSurgePerDayDialog = new SurgesPerDayDetailDialog(this,
			mCharacter);
	    }
	    dialog = mSurgePerDayDialog;
	    break;
	default:
	    dialog = null;
	}
	if (dialog != null) {
	    dialog.setOwnerActivity(this);
	    dialog.setOnDismissListener(this);
	    dialog.setOnCancelListener(this);
	}
	return dialog;
    }

    @Override
    protected void onPause() {
	super.onPause();

	validateData(mCurrentActionPointsEditText);
	validateData(mCurrentHpEditText);
	validateData(mCurrentSurgesEditText);
	validateData(mMaxActionPointsEditText);
	validateData(mTempHpEditText);

	int deathSavingThrowFailures = 0;
	if (mDeathSavingFailCheckBox3.isChecked()) {
	    deathSavingThrowFailures = 3;
	} else if (mDeathSavingFailCheckBox2.isChecked()) {
	    deathSavingThrowFailures = 2;
	} else if (mDeathSavingFailCheckBox1.isChecked()) {
	    deathSavingThrowFailures = 1;
	}

	mCharacter.setActionPointsCurrent(Integer
		.parseInt(mCurrentActionPointsEditText.getText().toString()));
	mCharacter.setActionPointsMax(Integer.parseInt(mMaxActionPointsEditText
		.getText().toString()));
	mCharacter.setDeathSavingFailures(deathSavingThrowFailures);
	mCharacter.setHealthNotes(mModifiersEditText.getText().toString());
	mCharacter.setHpCurrent(Integer.parseInt(mCurrentHpEditText.getText()
		.toString()));
	mCharacter.setHpTemp(Integer.parseInt(mTempHpEditText.getText()
		.toString()));
	mCharacter.setSecondWind(mSecondWindCheckBox.isChecked());
	mCharacter.setSurgesCurrent(Integer.parseInt(mCurrentSurgesEditText
		.getText().toString()));

	try {
	    getHelper().getCharacterDao().update(mCharacter);
	} catch (SQLException e) {
	    ErrorHandler.log(getClass(), "Failed to update the character", e,
		    getString(R.string.error_character_update), this);
	}
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
	super.onPrepareDialog(id, dialog, args);
	dialog.setTitle(getDialogName());
    }

    @Override
    protected void onResume() {
	super.onResume();
	fillData();
    }

    private void bindField(EditText editText) {
	switch (editText.getId()) {
	case R.id.health_action_points_current_edit_text:
	    mCharacter.setActionPointsCurrent(Integer.parseInt(editText
		    .getText().toString()));
	    break;
	case R.id.health_action_points_max_edit_text:
	    mCharacter.setActionPointsMax(Integer.parseInt(editText.getText()
		    .toString()));
	    break;
	case R.id.health_hp_current_edit_text:
	    mCharacter.setHpCurrent(Integer.parseInt(editText.getText()
		    .toString()));
	    updateHpColor();
	    break;
	case R.id.health_hp_temp_edit_text:
	    mCharacter.setHpTemp(Integer
		    .parseInt(editText.getText().toString()));
	    break;
	case R.id.health_surges_current_edit_text:
	    mCharacter.setSurgesCurrent(Integer.parseInt(editText.getText()
		    .toString()));
	    break;
	}
    }

    private void fillCalculatedData() {
	fillHpMax();
	fillSurgesPerDay();
	fillSurgeValue();
    }

    private void fillData() {
	fillCalculatedData();

	mCurrentActionPointsEditText.setText(String.valueOf(mCharacter
		.getActionPointsCurrent()));
	mCurrentHpEditText.setText(String.valueOf(mCharacter.getHpCurrent()));
	mCurrentSurgesEditText.setText(String.valueOf(mCharacter
		.getSurgesCurrent()));
	mMaxActionPointsEditText.setText(String.valueOf(mCharacter
		.getActionPointsMax()));
	mModifiersEditText.setText(mCharacter.getHealthNotes());
	mSecondWindCheckBox.setChecked(mCharacter.isSecondWind());
	mTempHpEditText.setText(String.valueOf(mCharacter.getHpTemp()));

	mDeathSavingFailCheckBox1.setChecked(false);
	mDeathSavingFailCheckBox2.setChecked(false);
	mDeathSavingFailCheckBox3.setChecked(false);
	if (mCharacter.getDeathSavingFailures() == 0) {
	    mDeathSavingFailCheckBox2.setClickable(false);
	    mDeathSavingFailCheckBox3.setClickable(false);
	}
	if (mCharacter.getDeathSavingFailures() > 0) {
	    mDeathSavingFailCheckBox1.setChecked(true);
	}
	if (mCharacter.getDeathSavingFailures() > 1) {
	    mDeathSavingFailCheckBox2.setChecked(true);
	}
	if (mCharacter.getDeathSavingFailures() > 2) {
	    mDeathSavingFailCheckBox3.setChecked(true);
	}
    }

    private void fillHpMax() {
	mHpMaxButton.setText(String.valueOf(mCharacter.getHpMax()));
    }

    private void fillSurgesPerDay() {
	mSurgesPerDayButton.setText(String
		.valueOf(mCharacter.getSurgesPerDay()));
    }

    private void fillSurgeValue() {
	mSurgeValueButton.setText(String.valueOf(mCharacter.getSurgeValue()));
    }

    private String getDialogName() {
	String score = "";
	switch (mClickedButton) {
	case R.id.health_hp_max_button:
	    score = getString(R.string.health_hp_max);
	    break;
	case R.id.health_surge_value_button:
	    score = getString(R.string.health_surge_value);
	    break;
	case R.id.health_surges_max_button:
	    score = getString(R.string.health_surges_max);
	    break;
	}
	return score;
    }

    private void updateHpColor() {
	if (mCharacter.isDying()) {
	    mCurrentHpEditText.setTextColor(0xffff0000);
	} else if (mCharacter.isBloodied()) {
	    mCurrentHpEditText.setTextColor(0xfff88040);
	} else {
	    mCurrentHpEditText.setTextColor(0xff000000);
	}
    }

    private void validateData(EditText editText) {
	if (editText.getText().length() == 0) {
	    editText.setText("0");
	} else if (editText.getText().toString().equals("-")) {
	    editText.setText("-0");
	} else {
	    if (editText == mCurrentHpEditText
		    && Integer.parseInt(editText.getText().toString()) > mCharacter
			    .getHpMax()) {
		editText.setText(String.valueOf(mCharacter.getHpMax()));
	    } else if (editText == mCurrentSurgesEditText
		    && Integer.parseInt(editText.getText().toString()) > mCharacter
			    .getSurgesPerDay()) {
		editText.setText(String.valueOf(mCharacter.getSurgesPerDay()));
	    } else if (editText == mCurrentActionPointsEditText
		    && Integer.parseInt(editText.getText().toString()) > mCharacter
			    .getActionPointsMax()) {
		editText.setText(String
			.valueOf(mCharacter.getActionPointsMax()));
	    } else if (editText == mMaxActionPointsEditText
		    && Integer.parseInt(editText.getText().toString()) < mCharacter
			    .getActionPointsCurrent()) {
		editText.setText(String.valueOf(mCharacter
			.getActionPointsCurrent()));
	    }
	}
    }
}
