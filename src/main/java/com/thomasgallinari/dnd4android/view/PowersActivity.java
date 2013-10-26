/**
 * Copyright (C) 2011 Thomas Gallinari
 */
package com.thomasgallinari.dnd4android.view;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.j256.ormlite.android.apptools.OrmLiteBaseListActivity;
import com.j256.ormlite.stmt.QueryBuilder;
import com.thomasgallinari.dnd4android.R;
import com.thomasgallinari.dnd4android.db.DatabaseHelper;
import com.thomasgallinari.dnd4android.model.Character;
import com.thomasgallinari.dnd4android.model.Power;
import com.thomasgallinari.dnd4android.view.util.ErrorHandler;

/**
 * An activity that shows all the character powers.
 */
public class PowersActivity extends OrmLiteBaseListActivity<DatabaseHelper>
	implements OnClickListener, OnDismissListener {

    class PowerListAdapter extends ArrayAdapter<Power> {

	class ViewHolder {
	    View mColorView;
	    TextView mDescriptionTextView;
	    TextView mNameTextView;
	}

	public PowerListAdapter(Context context, int textViewResourceId,
		List<Power> objects) {
	    super(context, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    Power power = getItem(position);
	    power.setCharacter(mCharacter);

	    ViewHolder holder;

	    if (convertView == null) {
		convertView = LayoutInflater.from(getContext()).inflate(
			R.layout.power_list_item, null);
		holder = new ViewHolder();
		holder.mColorView = convertView
			.findViewById(R.id.power_list_item_type_view);
		holder.mDescriptionTextView = (TextView) convertView
			.findViewById(R.id.power_list_item_description_text_view);
		holder.mNameTextView = (TextView) convertView
			.findViewById(R.id.power_list_item_name_text_view);
		convertView.setTag(holder);
	    } else {
		holder = (ViewHolder) convertView.getTag();
	    }

	    holder.mColorView.setBackgroundResource(power.getColorId());
	    holder.mNameTextView.setText(power.getName());
	    holder.mNameTextView.setEnabled(!power.isUsed());
	    holder.mDescriptionTextView.setText(power.getDescription());
	    holder.mDescriptionTextView.setEnabled(!power.isUsed());

	    return convertView;
	}

	public void sort() {
	    sort(mPowerComparator);
	}
    }

    class QueryPowersTask extends AsyncTask<Void, Void, List<Power>> {

	@Override
	protected List<Power> doInBackground(Void... params) {
	    List<Power> powers = new ArrayList<Power>();
	    try {
		QueryBuilder<Power, Integer> queryBuilder = getHelper()
			.getPowerDao().queryBuilder();
		queryBuilder.setWhere(getHelper().getPowerDao().queryBuilder()
			.where().eq("character_id", mCharacter.getId()));
		powers = getHelper().getPowerDao()
			.query(queryBuilder.prepare());
	    } catch (SQLException e) {
		ErrorHandler.log(PowersActivity.this.getClass(),
			"Failed to load the powers", e,
			getString(R.string.error_power_load),
			PowersActivity.this);
	    }
	    return powers;
	}

	@Override
	protected void onPostExecute(List<Power> powers) {
	    PowerListAdapter adapter = new PowerListAdapter(
		    PowersActivity.this, R.layout.power_list_item, powers);
	    adapter.sort();
	    setListAdapter(adapter);
	    mProgressBar.setVisibility(View.INVISIBLE);
	    mEmptyTextView.setText(getText(R.string.powers_empty));
	}

	@Override
	protected void onPreExecute() {
	    mProgressBar.setVisibility(View.VISIBLE);
	    mEmptyTextView.setText(getText(R.string.powers_loading));
	}
    }

    public static final String CHARACTER = "com.thomasgallinari.dndcharactersheet.CHARACTER";

    public static final String PREFS_SORT_ATTR_1 = "sort.attr.1";
    public static final String PREFS_SORT_ATTR_2 = "sort.attr.2";
    public static final String PREFS_SORT_ORDER_1 = "sort.order.1";
    public static final String PREFS_SORT_ORDER_2 = "sort.order.2";

    private static final int DIALOG_POWER_CREATE = 1;
    private static final int DIALOG_POWER_DELETE = 2;
    private static final int DIALOG_POWER_EDIT = 3;
    private static final int DIALOG_POWER_SORT = 4;

    private Character mCharacter;
    private AlertDialog mDeletePowerDialog;
    private TextView mEmptyTextView;
    private Comparator<Power> mPowerComparator;
    private PowerDialog mPowerDialog;
    private PowerSortDialog mPowerSortDialog;
    private ProgressBar mProgressBar;
    private Power mSelectedPower;

    /**
     * @return the {@link DatabaseHelper}
     */
    public DatabaseHelper getDatabaseHelper() {
	return getHelper();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.powers_new_power_button:
	    showDialog(DIALOG_POWER_CREATE);
	    break;
	case R.id.powers_sort_button:
	    showDialog(DIALOG_POWER_SORT);
	    break;
	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.powers_context_menu_delete_item:
	    showDialog(DIALOG_POWER_DELETE);
	    return true;
	case R.id.powers_context_menu_open_item:
	    showDialog(DIALOG_POWER_EDIT);
	    return true;
	case R.id.powers_context_menu_use_item:
	    mSelectedPower.setUsed(!mSelectedPower.isUsed());
	    try {
		getHelper().getPowerDao().update(mSelectedPower);
		new QueryPowersTask().execute();
	    } catch (SQLException e) {
		ErrorHandler.log(getClass(), "Failed to update the power", e,
			getString(R.string.error_power_update), this);
	    }
	    return true;
	default:
	    return super.onContextItemSelected(item);
	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
	    ContextMenuInfo menuInfo) {
	super.onCreateContextMenu(menu, v, menuInfo);

	mSelectedPower = (Power) getListAdapter().getItem(
		((AdapterContextMenuInfo) menuInfo).position);
	menu.setHeaderTitle(mSelectedPower.getName());
	getMenuInflater().inflate(R.menu.powers_context_menu, menu);
	if (mSelectedPower.getType().equals(Power.ENCOUNTER)
		|| mSelectedPower.getType().equals(Power.DAILY)) {
	    MenuItem usedItem = menu
		    .findItem(R.id.powers_context_menu_use_item);
	    usedItem.setVisible(true);
	    usedItem
		    .setTitle(mSelectedPower.isUsed() ? R.string.powers_context_menu_recover
			    : R.string.powers_context_menu_use);
	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDismiss(DialogInterface dialog) {
	if (dialog == mPowerSortDialog) {
	    mPowerComparator = getPowerComparator();
	    ((PowerListAdapter) getListAdapter()).sort();
	} else {
	    new QueryPowersTask().execute();
	}
    }

    /**
     * Refreshes the data after a rest.
     */
    public void refresh() {
	new QueryPowersTask().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.powers);

	mPowerComparator = getPowerComparator();
	mCharacter = (Character) getIntent().getSerializableExtra(CHARACTER);

	mEmptyTextView = (TextView) findViewById(android.R.id.empty);
	mProgressBar = (ProgressBar) findViewById(R.id.powers_progress_bar);

	registerForContextMenu(getListView());

	fillData();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
	Dialog dialog = null;
	switch (id) {
	case DIALOG_POWER_CREATE:
	case DIALOG_POWER_EDIT:
	    dialog = getPowerCreateDialog();
	    break;
	case DIALOG_POWER_DELETE:
	    dialog = getPowerDeleteAlertDialog();
	    break;
	case DIALOG_POWER_SORT:
	    dialog = getPowerSortDialog();
	    break;
	}
	return dialog;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
	super.onListItemClick(l, v, position, id);
	mSelectedPower = (Power) getListAdapter().getItem(position);
	showDialog(DIALOG_POWER_EDIT);
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
	super.onPrepareDialog(id, dialog, args);

	switch (id) {
	case DIALOG_POWER_CREATE:
	    Power power = new Power();
	    power.setCharacter(mCharacter);
	    ((PowerDialog) dialog).setPower(power);
	    break;
	case DIALOG_POWER_EDIT:
	    ((PowerDialog) dialog).setPower(mSelectedPower);
	    break;
	}
    }

    @Override
    protected void onResume() {
	super.onResume();
	fillData();
    }

    private void fillData() {
	new QueryPowersTask().execute();
    }

    private Comparator<Power> getPowerComparator() {
	return new Comparator<Power>() {

	    @Override
	    public int compare(Power power1, Power power2) {
		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		String sortAttr1 = prefs.getString(PREFS_SORT_ATTR_1,
			Power.SORT_ATTR_LEVEL);
		String sortAttr2 = prefs.getString(PREFS_SORT_ATTR_2,
			Power.SORT_ATTR_TYPE);
		String sortOrder1 = prefs.getString(PREFS_SORT_ORDER_1,
			Power.SORT_ORDER_ASC);
		String sortOrder2 = prefs.getString(PREFS_SORT_ORDER_2,
			Power.SORT_ORDER_ASC);

		int result = power1.compareTo(power2, sortAttr1, sortOrder1);
		if (result == 0) {
		    result = power1.compareTo(power2, sortAttr2, sortOrder2);
		}

		return result;
	    }
	};
    }

    private Dialog getPowerCreateDialog() {
	if (mPowerDialog == null) {
	    mPowerDialog = new PowerDialog(this);
	    mPowerDialog.setOwnerActivity(this);
	    mPowerDialog.setOnDismissListener(this);
	}
	return mPowerDialog;
    }

    private AlertDialog getPowerDeleteAlertDialog() {
	if (mDeletePowerDialog == null) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage(R.string.powers_delete_alert_dialog_message)
		    .setCancelable(true).setPositiveButton(R.string.global_yes,
			    new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
					int id) {
				    try {
					getHelper().getPowerDao().delete(
						mSelectedPower);
					new QueryPowersTask().execute();
				    } catch (SQLException e) {
					ErrorHandler
						.log(
							PowersActivity.this
								.getClass(),
							"Failed to delete the power",
							e,
							getString(R.string.error_power_delete),
							PowersActivity.this);
				    }
				}
			    }).setNegativeButton(R.string.global_no,
			    new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
					int id) {
				    dialog.cancel();
				}
			    });
	    mDeletePowerDialog = builder.create();
	}
	return mDeletePowerDialog;
    }

    private Dialog getPowerSortDialog() {
	if (mPowerSortDialog == null) {
	    mPowerSortDialog = new PowerSortDialog(this);
	    mPowerSortDialog.setOwnerActivity(this);
	    mPowerSortDialog.setOnDismissListener(this);
	}
	return mPowerSortDialog;
    }
}
