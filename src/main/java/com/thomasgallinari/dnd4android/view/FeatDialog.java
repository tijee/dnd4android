package com.thomasgallinari.dnd4android.view;

import java.sql.SQLException;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.thomasgallinari.dnd4android.R;
import com.thomasgallinari.dnd4android.model.Feat;
import com.thomasgallinari.dnd4android.view.util.ErrorHandler;
import com.thomasgallinari.dnd4android.view.util.TextViewBinder;

/**
 * A dialog to create or edit a feat.
 */
public class FeatDialog extends Dialog implements
	android.view.View.OnClickListener {

    private EditText mDescriptionEditText;
    private Feat mFeat;
    private EditText mNameEditText;
    private Button mOkButton;
    private TextView mTitleTextView;

    /**
     * Creates a new {@link FeatDialog}.
     * 
     * @param pContext
     *            {@inheritDoc}
     */
    public FeatDialog(Context pContext) {
	super(pContext, R.style.Theme_Dialog_DnD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
	FeatsActivity ownerActivity = (FeatsActivity) getOwnerActivity();

	switch (v.getId()) {
	case R.id.feat_cancel_button:
	    if (mFeat.getId() > 0) {
		try {
		    ownerActivity.getDatabaseHelper().getFeatDao().refresh(
			    mFeat);
		} catch (SQLException e) {
		    ErrorHandler.log(getClass(), "Failed to refresh the feat",
			    e, getOwnerActivity().getString(
				    R.string.error_feat_refresh),
			    getOwnerActivity());
		}
	    }
	    cancel();
	    break;
	case R.id.feat_ok_button:
	    mFeat.setDescription(mDescriptionEditText.getText().toString());
	    mFeat.setName(mNameEditText.getText().toString());

	    if (mFeat.getId() > 0) {
		try {
		    ownerActivity.getDatabaseHelper().getFeatDao()
			    .update(mFeat);
		} catch (SQLException e) {
		    ErrorHandler.log(getClass(), "Failed to update the feat",
			    e, getOwnerActivity().getString(
				    R.string.error_feat_update),
			    getOwnerActivity());
		}
	    } else {
		try {
		    ownerActivity.getDatabaseHelper().getFeatDao()
			    .create(mFeat);
		} catch (SQLException e) {
		    ErrorHandler.log(getClass(), "Failed to create the feat",
			    e, getOwnerActivity().getString(
				    R.string.error_feat_create),
			    getOwnerActivity());
		}
	    }

	    dismiss();
	    break;
	}
    }

    /**
     * Sets the feat to be edited.
     * 
     * @param feat
     *            the concerned {@link Feat}
     */
    public void setFeat(Feat feat) {
	mFeat = feat;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.feat);

	mOkButton = (Button) findViewById(R.id.feat_ok_button);
	mOkButton.setOnClickListener(this);
	((Button) findViewById(R.id.feat_cancel_button))
		.setOnClickListener(this);

	mDescriptionEditText = (EditText) findViewById(R.id.feat_description_edit_text);
	mNameEditText = (EditText) findViewById(R.id.feat_name_edit_text);
	mTitleTextView = (TextView) findViewById(R.id.feat_title_text_view);

	TextViewBinder binder = new TextViewBinder(mOkButton, new TextView[] {
		mNameEditText, mDescriptionEditText });
	mNameEditText.addTextChangedListener(binder);
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

	mDescriptionEditText.setText("");
	mNameEditText.setText("");
    }

    private void fillData() {
	if (mFeat.getId() == 0) {
	    mTitleTextView.setText(R.string.feat_title);
	} else {
	    mTitleTextView.setText(mFeat.getName());
	    mNameEditText.setText(mFeat.getName());
	    mDescriptionEditText.setText(mFeat.getDescription());
	}
    }
}
