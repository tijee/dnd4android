/**
 * Copyright (C) 2011 Thomas Gallinari
 */
package com.thomasgallinari.dndcharactersheet.view;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
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
import com.thomasgallinari.dndcharactersheet.R;
import com.thomasgallinari.dndcharactersheet.db.DatabaseHelper;
import com.thomasgallinari.dndcharactersheet.model.Character;
import com.thomasgallinari.dndcharactersheet.model.Equipment;
import com.thomasgallinari.dndcharactersheet.view.util.ErrorHandler;

/**
 * An activity that shows the character equipment.
 */
public class EquipmentActivity extends OrmLiteBaseListActivity<DatabaseHelper>
	implements OnClickListener, OnDismissListener, OnCancelListener {

    class EquipmentListAdapter extends ArrayAdapter<Equipment> {

	class ViewHolder {
	    TextView mDescriptionTextView;
	    TextView mNameTextView;
	}

	private Comparator<Equipment> mEquipmentComparator;

	public EquipmentListAdapter(Context context, int textViewResourceId,
		List<Equipment> objects) {
	    super(context, textViewResourceId, objects);
	    mEquipmentComparator = new Comparator<Equipment>() {
		public int compare(Equipment equipment1, Equipment equipment2) {
		    return equipment1.compareTo(equipment2);
		};
	    };
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    Equipment equipment = getItem(position);
	    equipment.setCharacter(mCharacter);

	    ViewHolder holder;

	    if (convertView == null) {
		convertView = LayoutInflater.from(getContext()).inflate(
			R.layout.equipment_list_item, null);
		holder = new ViewHolder();
		holder.mDescriptionTextView = (TextView) convertView
			.findViewById(R.id.equipment_list_item_description_text_view);
		holder.mNameTextView = (TextView) convertView
			.findViewById(R.id.equipment_list_item_name_text_view);
		convertView.setTag(holder);
	    } else {
		holder = (ViewHolder) convertView.getTag();
	    }

	    holder.mNameTextView.setText(equipment.getName());
	    holder.mDescriptionTextView.setText(equipment.getDescription());

	    return convertView;
	}

	public void sort() {
	    sort(mEquipmentComparator);
	}
    }

    class QueryEquipmentTask extends AsyncTask<Void, Void, List<Equipment>> {

	@Override
	protected List<Equipment> doInBackground(Void... params) {
	    List<Equipment> equipments = new ArrayList<Equipment>();
	    try {
		QueryBuilder<Equipment, Integer> queryBuilder = getHelper()
			.getEquipmentDao().queryBuilder();
		queryBuilder.setWhere(getHelper().getEquipmentDao()
			.queryBuilder().where().eq("character_id",
				mCharacter.getId()));
		equipments = getHelper().getEquipmentDao().query(
			queryBuilder.prepare());
	    } catch (SQLException e) {
		ErrorHandler.log(EquipmentActivity.this.getClass(),
			"Failed to load the equipment items", e,
			getString(R.string.error_equipment_load),
			EquipmentActivity.this);
	    }
	    return equipments;
	}

	@Override
	protected void onPostExecute(List<Equipment> equipments) {
	    EquipmentListAdapter adapter = new EquipmentListAdapter(
		    EquipmentActivity.this, R.layout.equipment_list_item,
		    equipments);
	    adapter.sort();
	    setListAdapter(adapter);
	    mProgressBar.setVisibility(View.INVISIBLE);
	    mEmptyTextView.setText(getText(R.string.equipment_empty));
	}

	@Override
	protected void onPreExecute() {
	    mProgressBar.setVisibility(View.VISIBLE);
	    mEmptyTextView.setText(getText(R.string.equipment_loading));
	}
    }

    public static final String CHARACTER = "com.thomasgallinari.dndcharactersheet.CHARACTER";

    private static final int DIALOG_EQUIPMENT_CREATE = 1;
    private static final int DIALOG_EQUIPMENT_DELETE = 2;
    private static final int DIALOG_EQUIPMENT_EDIT = 3;
    private static final int DIALOG_MONEY = 4;

    private Character mCharacter;
    private AlertDialog mDeleteEquipmentDialog;
    private TextView mEmptyTextView;
    private EquipmentDialog mEquipmentDialog;
    private MoneyDialog mMoneyDialog;
    private ProgressBar mProgressBar;
    private Equipment mSelectedEquipment;

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
    public void onCancel(DialogInterface dialog) {
	if (dialog == mMoneyDialog) {
	    try {
		getHelper().getCharacterDao().refresh(mCharacter);
	    } catch (SQLException e) {
		ErrorHandler.log(getClass(), "Failed to refresh the character",
			e, getString(R.string.error_character_refresh), this);
	    }
	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.equipment_new_equipment_button:
	    showDialog(DIALOG_EQUIPMENT_CREATE);
	    break;
	case R.id.equipment_money_button:
	    showDialog(DIALOG_MONEY);
	    break;
	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.equipment_context_menu_delete_item:
	    showDialog(DIALOG_EQUIPMENT_DELETE);
	    return true;
	case R.id.equipment_context_menu_open_item:
	    showDialog(DIALOG_EQUIPMENT_EDIT);
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

	mSelectedEquipment = (Equipment) getListAdapter().getItem(
		((AdapterContextMenuInfo) menuInfo).position);
	menu.setHeaderTitle(mSelectedEquipment.getName());
	getMenuInflater().inflate(R.menu.equipment_context_menu, menu);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDismiss(DialogInterface dialog) {
	if (dialog == mMoneyDialog) {
	    try {
		getHelper().getCharacterDao().update(mCharacter);
	    } catch (SQLException e) {
		ErrorHandler.log(getClass(), "Failed to update the character",
			e, getString(R.string.error_character_update), this);
	    }
	} else if (dialog == mEquipmentDialog) {
	    fillData();
	}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.equipment);

	mCharacter = (Character) getIntent().getSerializableExtra(CHARACTER);

	mEmptyTextView = (TextView) findViewById(android.R.id.empty);
	mProgressBar = (ProgressBar) findViewById(R.id.equipment_progress_bar);

	registerForContextMenu(getListView());

	fillData();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
	Dialog dialog = null;
	switch (id) {
	case DIALOG_EQUIPMENT_CREATE:
	case DIALOG_EQUIPMENT_EDIT:
	    dialog = getEquipmentDialog();
	    break;
	case DIALOG_EQUIPMENT_DELETE:
	    dialog = getEquipmentDeleteAlertDialog();
	    break;
	case DIALOG_MONEY:
	    dialog = getMoneyDialog();
	    break;
	}
	return dialog;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
	super.onListItemClick(l, v, position, id);
	mSelectedEquipment = (Equipment) getListAdapter().getItem(position);
	showDialog(DIALOG_EQUIPMENT_EDIT);
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
	super.onPrepareDialog(id, dialog, args);

	switch (id) {
	case DIALOG_EQUIPMENT_CREATE:
	    Equipment equipment = new Equipment();
	    equipment.setCharacter(mCharacter);
	    ((EquipmentDialog) dialog).setEquipment(equipment);
	    break;
	case DIALOG_EQUIPMENT_EDIT:
	    ((EquipmentDialog) dialog).setEquipment(mSelectedEquipment);
	    break;
	}
    }

    private void fillData() {
	new QueryEquipmentTask().execute();
    }

    private AlertDialog getEquipmentDeleteAlertDialog() {
	if (mDeleteEquipmentDialog == null) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage(R.string.equipment_delete_alert_dialog_message)
		    .setCancelable(true).setPositiveButton(R.string.global_yes,
			    new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
					int id) {
				    try {
					getHelper().getEquipmentDao().delete(
						mSelectedEquipment);
					new QueryEquipmentTask().execute();
				    } catch (SQLException e) {
					ErrorHandler
						.log(
							EquipmentActivity.this
								.getClass(),
							"Failed to delete the equipment item",
							e,
							getString(R.string.error_equipment_item_delete),
							EquipmentActivity.this);
				    }
				}
			    }).setNegativeButton(R.string.global_no,
			    new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
					int id) {
				    dialog.cancel();
				}
			    });
	    mDeleteEquipmentDialog = builder.create();
	}
	return mDeleteEquipmentDialog;
    }

    private Dialog getEquipmentDialog() {
	if (mEquipmentDialog == null) {
	    mEquipmentDialog = new EquipmentDialog(this);
	    mEquipmentDialog.setOwnerActivity(this);
	    mEquipmentDialog.setOnDismissListener(this);
	}
	return mEquipmentDialog;
    }

    private Dialog getMoneyDialog() {
	if (mMoneyDialog == null) {
	    mMoneyDialog = new MoneyDialog(this, mCharacter);
	    mMoneyDialog.setOwnerActivity(this);
	    mMoneyDialog.setOnCancelListener(this);
	    mMoneyDialog.setOnDismissListener(this);
	}
	return mMoneyDialog;
    }
}
