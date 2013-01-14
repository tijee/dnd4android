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
import com.thomasgallinari.dndcharactersheet.model.Attack;
import com.thomasgallinari.dndcharactersheet.view.util.ErrorHandler;
import com.thomasgallinari.dndcharactersheet.view.util.TextViewBinder;

/**
 * A dialog to create or edit an attack.
 */
public class AttackDialog extends Dialog implements
	android.view.View.OnClickListener {

    private Attack mAttack;
    private EditText mNameEditText;
    private Button mOkButton;
    private TextView mTitleTextView;

    /**
     * Creates a new {@link AttackDialog}.
     * 
     * @param pContext
     *            {@inheritDoc}
     */
    public AttackDialog(Context pContext) {
	super(pContext, R.style.Theme_Dialog_DnD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
	AttacksActivity ownerActivity = (AttacksActivity) getOwnerActivity();

	switch (v.getId()) {
	case R.id.attack_cancel_button:
	    if (mAttack.getId() > 0) {
		try {
		    ownerActivity.getDatabaseHelper().getAttackDao().refresh(
			    mAttack);
		} catch (SQLException e) {
		    ErrorHandler.log(getClass(),
			    "Failed to refresh the attack", e,
			    getOwnerActivity().getString(
				    R.string.error_attack_refresh),
			    getOwnerActivity());
		}
	    }
	    cancel();
	    break;
	case R.id.attack_ok_button:
	    mAttack.setName(mNameEditText.getText().toString());

	    if (mAttack.getId() > 0) {
		try {
		    ownerActivity.getDatabaseHelper().getAttackDao().update(
			    mAttack);
		} catch (SQLException e) {
		    ErrorHandler.log(getClass(), "Failed to update the attack",
			    e, getOwnerActivity().getString(
				    R.string.error_attack_update),
			    getOwnerActivity());
		}
	    } else {
		try {
		    ownerActivity.getDatabaseHelper().getAttackDao().create(
			    mAttack);
		} catch (SQLException e) {
		    ErrorHandler.log(getClass(), "Failed to create the attack",
			    e, getOwnerActivity().getString(
				    R.string.error_attack_create),
			    getOwnerActivity());
		}
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

	setContentView(R.layout.attack);

	mOkButton = (Button) findViewById(R.id.attack_ok_button);
	mOkButton.setOnClickListener(this);
	((Button) findViewById(R.id.attack_cancel_button))
		.setOnClickListener(this);

	mNameEditText = (EditText) findViewById(R.id.attack_name_edit_text);
	mTitleTextView = (TextView) findViewById(R.id.attack_title_text_view);

	TextViewBinder binder = new TextViewBinder(mOkButton,
		new TextView[] { mNameEditText });
	mNameEditText.addTextChangedListener(binder);
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

	mNameEditText.setText("");
    }

    private void fillData() {
	if (mAttack.getId() == 0) {
	    mTitleTextView.setText(R.string.attack_title);
	} else {
	    mTitleTextView.setText(mAttack.getName());
	    mNameEditText.setText(mAttack.getName());
	}
    }
}
