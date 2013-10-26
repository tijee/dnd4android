/**
 * Copyright (C) 2011 Thomas Gallinari
 */
package com.thomasgallinari.dnd4android.view;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.thomasgallinari.dnd4android.R;
import com.thomasgallinari.dnd4android.model.Power;
import com.thomasgallinari.dnd4android.view.util.ErrorHandler;
import com.thomasgallinari.dnd4android.view.widget.StyledCheckedTextView;
import com.thomasgallinari.dnd4android.view.widget.StyledTextView;

/**
 * A dialog that enables to sort the powers of the character.
 */
public class PowerSortDialog extends Dialog implements OnClickListener {

    class PowerSortAttrAdapter extends ArrayAdapter<String> {

	public PowerSortAttrAdapter(Context context, int textViewResourceId,
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
	    view.setText(getPowerSortLabel(getItem(position)));
	    return view;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    TextView view = (TextView) super.getView(position, convertView,
		    parent);
	    view.setTypeface(Typeface.createFromAsset(getContext().getAssets(),
		    StyledTextView.FONT));
	    view.setText(getPowerSortLabel(getItem(position)));
	    view.setGravity(Gravity.CENTER);
	    return view;
	}
    }

    private static final String POWER_SORT_PREFIX = "power_sort_";

    private Spinner mAttrSpinner1;
    private Spinner mAttrSpinner2;
    private Spinner mOrderSpinner1;
    private Spinner mOrderSpinner2;

    /**
     * Creates a new {@link PowerSortDialog}.
     * 
     * @param context
     *            {@inheritDoc}
     */
    public PowerSortDialog(Context context) {
	super(context, R.style.Theme_Dialog_DnD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.power_sort_cancel_button:
	    cancel();
	    break;
	case R.id.power_sort_ok_button:
	    SharedPreferences prefs = getOwnerActivity().getPreferences(
		    PowersActivity.MODE_PRIVATE);
	    Editor prefsEditor = prefs.edit();
	    prefsEditor.putString(PowersActivity.PREFS_SORT_ATTR_1,
		    (String) mAttrSpinner1.getSelectedItem());
	    prefsEditor.putString(PowersActivity.PREFS_SORT_ATTR_2,
		    (String) mAttrSpinner2.getSelectedItem());
	    prefsEditor.putString(PowersActivity.PREFS_SORT_ORDER_1,
		    (String) mOrderSpinner1.getSelectedItem());
	    prefsEditor.putString(PowersActivity.PREFS_SORT_ORDER_2,
		    (String) mOrderSpinner2.getSelectedItem());
	    prefsEditor.commit();

	    dismiss();
	    break;
	}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.power_sort);

	((Button) findViewById(R.id.power_sort_cancel_button))
		.setOnClickListener(this);
	((Button) findViewById(R.id.power_sort_ok_button))
		.setOnClickListener(this);

	String[] attrs = new String[] { Power.SORT_ATTR_LEVEL,
		Power.SORT_ATTR_NAME, Power.SORT_ATTR_TYPE };
	String[] orders = new String[] { Power.SORT_ORDER_ASC,
		Power.SORT_ORDER_DESC };

	mAttrSpinner1 = (Spinner) findViewById(R.id.power_sort_by_spinner_1);
	mAttrSpinner2 = (Spinner) findViewById(R.id.power_sort_by_spinner_2);
	mOrderSpinner1 = (Spinner) findViewById(R.id.power_sort_order_spinner_1);
	mOrderSpinner2 = (Spinner) findViewById(R.id.power_sort_order_spinner_2);

	mAttrSpinner1.setAdapter(new PowerSortAttrAdapter(getOwnerActivity(),
		android.R.layout.simple_spinner_item, attrs));
	mAttrSpinner2.setAdapter(new PowerSortAttrAdapter(getOwnerActivity(),
		android.R.layout.simple_spinner_item, attrs));
	mOrderSpinner1.setAdapter(new PowerSortAttrAdapter(getOwnerActivity(),
		android.R.layout.simple_spinner_item, orders));
	mOrderSpinner2.setAdapter(new PowerSortAttrAdapter(getOwnerActivity(),
		android.R.layout.simple_spinner_item, orders));
    }

    @Override
    protected void onStart() {
	super.onStart();
	fillData();
    }

    private void fillData() {
	SharedPreferences prefs = getOwnerActivity().getPreferences(
		PowersActivity.MODE_PRIVATE);
	String sortAttr1 = prefs.getString(PowersActivity.PREFS_SORT_ATTR_1,
		Power.SORT_ATTR_LEVEL);
	String sortAttr2 = prefs.getString(PowersActivity.PREFS_SORT_ATTR_2,
		Power.SORT_ATTR_TYPE);
	String sortOrder1 = prefs.getString(PowersActivity.PREFS_SORT_ORDER_1,
		Power.SORT_ORDER_ASC);
	String sortOrder2 = prefs.getString(PowersActivity.PREFS_SORT_ORDER_2,
		Power.SORT_ORDER_ASC);

	for (int i = 0; i < mAttrSpinner1.getCount(); i++) {
	    if (mAttrSpinner1.getItemAtPosition(i).equals(sortAttr1)) {
		mAttrSpinner1.setSelection(i);
		break;
	    }
	}
	for (int i = 0; i < mAttrSpinner2.getCount(); i++) {
	    if (mAttrSpinner2.getItemAtPosition(i).equals(sortAttr2)) {
		mAttrSpinner2.setSelection(i);
		break;
	    }
	}
	for (int i = 0; i < mOrderSpinner1.getCount(); i++) {
	    if (mOrderSpinner1.getItemAtPosition(i).equals(sortOrder1)) {
		mOrderSpinner1.setSelection(i);
		break;
	    }
	}
	for (int i = 0; i < mOrderSpinner2.getCount(); i++) {
	    if (mOrderSpinner2.getItemAtPosition(i).equals(sortOrder2)) {
		mOrderSpinner2.setSelection(i);
		break;
	    }
	}
    }

    private String getPowerSortLabel(String type) {
	String label = "";
	try {
	    label = getOwnerActivity().getString(
		    R.string.class.getField(
			    new StringBuilder().append(POWER_SORT_PREFIX)
				    .append(type).toString()).getInt(
			    R.string.class));
	} catch (IllegalArgumentException e) {
	    ErrorHandler.log(getClass(), "Could not find power sort label", e);
	} catch (SecurityException e) {
	    ErrorHandler.log(getClass(), "Could not find power sort label", e);
	} catch (IllegalAccessException e) {
	    ErrorHandler.log(getClass(), "Could not find power sort label", e);
	} catch (NoSuchFieldException e) {
	    ErrorHandler.log(getClass(), "Could not find power sort label", e);
	}
	return label;
    }
}
