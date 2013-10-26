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
import com.thomasgallinari.dndcharactersheet.model.Ritual;
import com.thomasgallinari.dndcharactersheet.view.util.ErrorHandler;
import com.thomasgallinari.dndcharactersheet.view.util.TextViewBinder;

/**
 * A dialog to create or edit a ritual.
 */
public class RitualDialog extends Dialog implements
	android.view.View.OnClickListener {

    private EditText mCostEditText;
    private EditText mDescriptionEditText;
    private EditText mDurationEditText;
    private EditText mLevelEditText;
    private EditText mNameEditText;
    private Button mOkButton;
    private Ritual mRitual;
    private EditText mSkillEditText;
    private EditText mTimeEditText;
    private TextView mTitleTextView;

    /**
     * Creates a new {@link RitualDialog}.
     * 
     * @param pContext
     *            {@inheritDoc}
     */
    public RitualDialog(Context pContext) {
	super(pContext, R.style.Theme_Dialog_DnD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
	RitualsActivity ownerActivity = (RitualsActivity) getOwnerActivity();

	switch (v.getId()) {
	case R.id.ritual_cancel_button:
	    if (mRitual.getId() > 0) {
		try {
		    ownerActivity.getDatabaseHelper().getRitualDao().refresh(
			    mRitual);
		} catch (SQLException e) {
		    ErrorHandler.log(getClass(),
			    "Failed to refresh the ritual", e,
			    getOwnerActivity().getString(
				    R.string.error_ritual_refresh),
			    getOwnerActivity());
		}
	    }
	    cancel();
	    break;
	case R.id.ritual_ok_button:
	    mRitual.setCost(mCostEditText.getText().toString());
	    mRitual.setDescription(mDescriptionEditText.getText().toString());
	    mRitual.setDuration(mDurationEditText.getText().toString());
	    mRitual.setLevel(Integer.parseInt(mLevelEditText.getText()
		    .toString()));
	    mRitual.setName(mNameEditText.getText().toString());
	    mRitual.setSkill(mSkillEditText.getText().toString());
	    mRitual.setTime(mTimeEditText.getText().toString());

	    if (mRitual.getId() > 0) {
		try {
		    ownerActivity.getDatabaseHelper().getRitualDao().update(
			    mRitual);
		} catch (SQLException e) {
		    ErrorHandler.log(getClass(), "Failed to update the ritual",
			    e, getOwnerActivity().getString(
				    R.string.error_ritual_update),
			    getOwnerActivity());
		}
	    } else {
		try {
		    ownerActivity.getDatabaseHelper().getRitualDao().create(
			    mRitual);
		} catch (SQLException e) {
		    ErrorHandler.log(getClass(), "Failed to create the ritual",
			    e, getOwnerActivity().getString(
				    R.string.error_ritual_create),
			    getOwnerActivity());
		}
	    }

	    dismiss();
	    break;
	}
    }

    /**
     * Sets the ritual to be edited.
     * 
     * @param ritual
     *            the concerned {@link Ritual}
     */
    public void setRitual(Ritual ritual) {
	mRitual = ritual;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.ritual);

	mOkButton = (Button) findViewById(R.id.ritual_ok_button);
	mOkButton.setOnClickListener(this);
	((Button) findViewById(R.id.ritual_cancel_button))
		.setOnClickListener(this);

	mCostEditText = (EditText) findViewById(R.id.ritual_cost_edit_text);
	mDescriptionEditText = (EditText) findViewById(R.id.ritual_description_edit_text);
	mDurationEditText = (EditText) findViewById(R.id.ritual_duration_edit_text);
	mLevelEditText = (EditText) findViewById(R.id.ritual_level_edit_text);
	mNameEditText = (EditText) findViewById(R.id.ritual_name_edit_text);
	mSkillEditText = (EditText) findViewById(R.id.ritual_skill_edit_text);
	mTimeEditText = (EditText) findViewById(R.id.ritual_time_edit_text);
	mTitleTextView = (TextView) findViewById(R.id.ritual_title_text_view);

	TextViewBinder binder = new TextViewBinder(mOkButton, new TextView[] {
		mNameEditText, mLevelEditText, mDescriptionEditText });
	mNameEditText.addTextChangedListener(binder);
	mLevelEditText.addTextChangedListener(binder);
	mDescriptionEditText.addTextChangedListener(binder);
    }

    @Override
    protected void onStart() {
	super.onStart();

	fillData();
	mNameEditText.requestFocus();
    }

    @Override
    protected void onStop() {
	super.onStop();

	mCostEditText.setText("");
	mDescriptionEditText.setText("");
	mDurationEditText.setText("");
	mLevelEditText.setText("");
	mNameEditText.setText("");
	mSkillEditText.setText("");
	mTimeEditText.setText("");
    }

    private void fillData() {
	if (mRitual.getId() == 0) {
	    mTitleTextView.setText(R.string.ritual_title);
	} else {
	    mTitleTextView.setText(mRitual.getName());

	    mCostEditText.setText(mRitual.getCost());
	    mDescriptionEditText.setText(mRitual.getDescription());
	    mDurationEditText.setText(mRitual.getDuration());
	    mLevelEditText.setText(String.valueOf(mRitual.getLevel()));
	    mNameEditText.setText(mRitual.getName());
	    mSkillEditText.setText(mRitual.getSkill());
	    mTimeEditText.setText(mRitual.getTime());
	}
    }
}
