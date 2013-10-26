package com.thomasgallinari.dnd4android.view;

import java.sql.SQLException;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.thomasgallinari.dnd4android.R;
import com.thomasgallinari.dnd4android.model.Power;
import com.thomasgallinari.dnd4android.view.util.ErrorHandler;
import com.thomasgallinari.dnd4android.view.util.TextViewBinder;
import com.thomasgallinari.dnd4android.view.widget.StyledCheckedTextView;
import com.thomasgallinari.dnd4android.view.widget.StyledTextView;

/**
 * A dialog to create or edit a power.
 */
public class PowerDialog extends Dialog implements
	android.view.View.OnClickListener {

    class PowerTypeAdapter extends ArrayAdapter<String> {

	public PowerTypeAdapter(Context context, int textViewResourceId,
		String[] objects) {
	    super(context, textViewResourceId, objects);
	    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}

	@Override
	public View getDropDownView(int position, View convertView,
		ViewGroup parent) {
	    CheckedTextView view = (CheckedTextView) super.getDropDownView(
		    position, convertView, parent);
	    view.setTypeface(Typeface.createFromAsset(getContext().getAssets(),
		    StyledCheckedTextView.FONT));
	    view.setText(getPowerTypeName(getItem(position)));
	    return view;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    TextView view = (TextView) super.getView(position, convertView,
		    parent);
	    view.setTypeface(Typeface.createFromAsset(getContext().getAssets(),
		    StyledTextView.FONT));
	    view.setText(getPowerTypeName(getItem(position)));
	    view.setGravity(Gravity.CENTER);
	    return view;
	}
    }

    private static final String POWER_TYPE_PREFIX = "power_type_";

    private EditText mDescriptionEditText;
    private EditText mLevelEditText;
    private EditText mNameEditText;
    private Button mOkButton;
    private Power mPower;
    private TextView mTitleTextView;
    private Spinner mTypeSpinner;
    private CheckBox mUsedCheckBox;
    private TextView mUsedTextView;

    /**
     * Creates a new {@link PowerDialog}.
     * 
     * @param pContext
     *            {@inheritDoc}
     */
    public PowerDialog(Context pContext) {
	super(pContext, R.style.Theme_Dialog_DnD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
	PowersActivity ownerActivity = (PowersActivity) getOwnerActivity();

	switch (v.getId()) {
	case R.id.power_cancel_button:
	    if (mPower.getId() > 0) {
		try {
		    ownerActivity.getDatabaseHelper().getPowerDao().refresh(
			    mPower);
		} catch (SQLException e) {
		    ErrorHandler.log(getClass(), "Failed to refresh the power",
			    e, getOwnerActivity().getString(
				    R.string.error_power_refresh),
			    getOwnerActivity());
		}
	    }
	    cancel();
	    break;
	case R.id.power_ok_button:
	    mPower.setDescription(mDescriptionEditText.getText().toString());
	    mPower.setLevel(Integer.parseInt(mLevelEditText.getText()
		    .toString()));
	    mPower.setName(mNameEditText.getText().toString());
	    mPower.setType(mTypeSpinner.getSelectedItem().toString());

	    if (mPower.getId() > 0) {
		mPower.setUsed(mUsedCheckBox.isChecked());
		try {
		    ownerActivity.getDatabaseHelper().getPowerDao().update(
			    mPower);
		} catch (SQLException e) {
		    ErrorHandler.log(getClass(), "Failed to update the power",
			    e, getOwnerActivity().getString(
				    R.string.error_power_update),
			    getOwnerActivity());
		}
	    } else {
		try {
		    ownerActivity.getDatabaseHelper().getPowerDao().create(
			    mPower);
		} catch (SQLException e) {
		    ErrorHandler.log(getClass(), "Failed to create the power",
			    e, getOwnerActivity().getString(
				    R.string.error_power_create),
			    getOwnerActivity());
		}
	    }

	    dismiss();
	    break;
	}
    }

    /**
     * Sets the power to be edited.
     * 
     * @param power
     *            the concerned {@link Power}
     */
    public void setPower(Power power) {
	mPower = power;
	if (power.getId() > 0) {
	    if (power.getType().equals(Power.AT_WILL)) {
		mUsedCheckBox.setVisibility(View.GONE);
		mUsedTextView.setVisibility(View.GONE);
	    } else {
		mUsedCheckBox.setVisibility(View.VISIBLE);
		mUsedTextView.setVisibility(View.VISIBLE);
	    }
	} else {
	    mUsedCheckBox.setVisibility(View.GONE);
	    mUsedTextView.setVisibility(View.GONE);
	}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.power);

	mOkButton = (Button) findViewById(R.id.power_ok_button);
	mOkButton.setOnClickListener(this);
	((Button) findViewById(R.id.power_cancel_button))
		.setOnClickListener(this);

	mDescriptionEditText = (EditText) findViewById(R.id.power_description_edit_text);
	mLevelEditText = (EditText) findViewById(R.id.power_level_edit_text);
	mNameEditText = (EditText) findViewById(R.id.power_name_edit_text);
	mTitleTextView = (TextView) findViewById(R.id.power_title_text_view);
	mUsedCheckBox = (CheckBox) findViewById(R.id.power_used_check_box);
	mUsedTextView = (TextView) findViewById(R.id.power_used_text_view);
	mTypeSpinner = (Spinner) findViewById(R.id.power_type_spinner);
	mTypeSpinner.setAdapter(new PowerTypeAdapter(getOwnerActivity(),
		android.R.layout.simple_spinner_item, new String[] {
			Power.AT_WILL, Power.ENCOUNTER, Power.DAILY }));

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

	mTypeSpinner.setSelection(0);
	mDescriptionEditText.setText("");
	mLevelEditText.setText("");
	mUsedCheckBox.setChecked(false);
	mNameEditText.setText("");
    }

    private void fillData() {
	if (mPower.getId() == 0) {
	    mTitleTextView.setText(R.string.power_title);
	} else {
	    mTitleTextView.setText(mPower.getName());

	    for (int i = 0; i < mTypeSpinner.getCount(); i++) {
		if (mTypeSpinner.getItemAtPosition(i).equals(mPower.getType())) {
		    mTypeSpinner.setSelection(i);
		    break;
		}
	    }
	    mNameEditText.setText(mPower.getName());
	    mLevelEditText.setText(String.valueOf(mPower.getLevel()));
	    mUsedCheckBox.setChecked(mPower.isUsed());
	    mDescriptionEditText.setText(mPower.getDescription());
	}
    }

    private String getPowerTypeName(String type) {
	String powerTypeName = "";
	try {
	    powerTypeName = getOwnerActivity().getString(
		    R.string.class.getField(
			    new StringBuilder().append(POWER_TYPE_PREFIX)
				    .append(type).toString()).getInt(
			    R.string.class));
	} catch (IllegalArgumentException e) {
	    ErrorHandler.log(getClass(), "Could not find power type name", e);
	} catch (SecurityException e) {
	    ErrorHandler.log(getClass(), "Could not find power type name", e);
	} catch (IllegalAccessException e) {
	    ErrorHandler.log(getClass(), "Could not find power type name", e);
	} catch (NoSuchFieldException e) {
	    ErrorHandler.log(getClass(), "Could not find power type name", e);
	}
	return powerTypeName;
    }
}
