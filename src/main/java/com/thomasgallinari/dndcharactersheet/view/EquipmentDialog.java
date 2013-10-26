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
import com.thomasgallinari.dndcharactersheet.model.Equipment;
import com.thomasgallinari.dndcharactersheet.view.util.ErrorHandler;
import com.thomasgallinari.dndcharactersheet.view.util.TextViewBinder;

/**
 * A dialog to create or edit an equipment item.
 */
public class EquipmentDialog extends Dialog implements
	android.view.View.OnClickListener {

    private EditText mDescriptionEditText;
    private Equipment mEquipment;
    private EditText mNameEditText;
    private Button mOkButton;
    private TextView mTitleTextView;

    /**
     * Creates a new {@link EquipmentDialog}.
     * 
     * @param pContext
     *            {@inheritDoc}
     */
    public EquipmentDialog(Context pContext) {
	super(pContext, R.style.Theme_Dialog_DnD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
	EquipmentActivity ownerActivity = (EquipmentActivity) getOwnerActivity();

	switch (v.getId()) {
	case R.id.equipment_item_cancel_button:
	    if (mEquipment.getId() > 0) {
		try {
		    ownerActivity.getDatabaseHelper().getEquipmentDao()
			    .refresh(mEquipment);
		} catch (SQLException e) {
		    ErrorHandler.log(getClass(),
			    "Failed to refresh the equipment item", e,
			    getOwnerActivity().getString(
				    R.string.error_equipment_item_refresh),
			    getOwnerActivity());
		}
	    }
	    cancel();
	    break;
	case R.id.equipment_item_ok_button:
	    mEquipment
		    .setDescription(mDescriptionEditText.getText().toString());
	    mEquipment.setName(mNameEditText.getText().toString());

	    if (mEquipment.getId() > 0) {
		try {
		    ownerActivity.getDatabaseHelper().getEquipmentDao().update(
			    mEquipment);
		} catch (SQLException e) {
		    ErrorHandler.log(getClass(),
			    "Failed to update the equipment item", e,
			    getOwnerActivity().getString(
				    R.string.error_equipment_item_update),
			    getOwnerActivity());
		}
	    } else {
		try {
		    ownerActivity.getDatabaseHelper().getEquipmentDao().create(
			    mEquipment);
		} catch (SQLException e) {
		    ErrorHandler.log(getClass(),
			    "Failed to create the equipment", e,
			    getOwnerActivity().getString(
				    R.string.error_equipment_item_create),
			    getOwnerActivity());
		}
	    }

	    dismiss();
	    break;
	}
    }

    /**
     * Sets the equipment item to be edited.
     * 
     * @param equipment
     *            the concerned {@link Equipment}
     */
    public void setEquipment(Equipment equipment) {
	mEquipment = equipment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.equipment_item);

	mOkButton = (Button) findViewById(R.id.equipment_item_ok_button);
	mOkButton.setOnClickListener(this);
	((Button) findViewById(R.id.equipment_item_cancel_button))
		.setOnClickListener(this);

	mDescriptionEditText = (EditText) findViewById(R.id.equipment_item_description_edit_text);
	mNameEditText = (EditText) findViewById(R.id.equipment_item_name_edit_text);
	mTitleTextView = (TextView) findViewById(R.id.equipment_item_title_text_view);

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
	if (mEquipment.getId() == 0) {
	    mTitleTextView.setText(R.string.equipment_item_title);
	} else {
	    mTitleTextView.setText(mEquipment.getName());
	    mNameEditText.setText(mEquipment.getName());
	    mDescriptionEditText.setText(mEquipment.getDescription());
	}
    }
}
