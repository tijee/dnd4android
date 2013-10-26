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
import com.thomasgallinari.dnd4android.model.Ritual;
import com.thomasgallinari.dnd4android.view.util.ErrorHandler;

/**
 * An activity that shows all the character rituals.
 */
public class RitualsActivity extends OrmLiteBaseListActivity<DatabaseHelper>
	implements OnClickListener, OnDismissListener {

    class QueryRitualsTask extends AsyncTask<Void, Void, List<Ritual>> {

	@Override
	protected List<Ritual> doInBackground(Void... params) {
	    List<Ritual> rituals = new ArrayList<Ritual>();
	    try {
		QueryBuilder<Ritual, Integer> queryBuilder = getHelper()
			.getRitualDao().queryBuilder();
		queryBuilder.setWhere(getHelper().getRitualDao().queryBuilder()
			.where().eq("character_id", mCharacter.getId()));
		rituals = getHelper().getRitualDao().query(
			queryBuilder.prepare());
	    } catch (SQLException e) {
		ErrorHandler.log(RitualsActivity.this.getClass(),
			"Failed to load the rituals", e,
			getString(R.string.error_ritual_load),
			RitualsActivity.this);
	    }
	    return rituals;
	}

	@Override
	protected void onPostExecute(List<Ritual> rituals) {
	    RitualListAdapter adapter = new RitualListAdapter(
		    RitualsActivity.this, R.layout.ritual_list_item, rituals);
	    adapter.sort();
	    setListAdapter(adapter);
	    mProgressBar.setVisibility(View.INVISIBLE);
	    mEmptyTextView.setText(getText(R.string.rituals_empty));
	}

	@Override
	protected void onPreExecute() {
	    mProgressBar.setVisibility(View.VISIBLE);
	    mEmptyTextView.setText(getText(R.string.rituals_loading));
	}
    }

    class RitualListAdapter extends ArrayAdapter<Ritual> {

	class ViewHolder {
	    TextView mCostTextView;
	    TextView mDescriptionTextView;
	    TextView mNameTextView;
	    TextView mTimeTextView;
	}

	private Comparator<Ritual> mRitualComparator;

	public RitualListAdapter(Context context, int textViewResourceId,
		List<Ritual> objects) {
	    super(context, textViewResourceId, objects);
	    mRitualComparator = new Comparator<Ritual>() {
		public int compare(Ritual ritual1, Ritual ritual2) {
		    return ritual1.compareTo(ritual2);
		};
	    };
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    Ritual ritual = getItem(position);
	    ritual.setCharacter(mCharacter);

	    ViewHolder holder;

	    if (convertView == null) {
		convertView = LayoutInflater.from(getContext()).inflate(
			R.layout.ritual_list_item, null);
		holder = new ViewHolder();
		holder.mCostTextView = (TextView) convertView
			.findViewById(R.id.ritual_list_item_cost_text_view);
		holder.mDescriptionTextView = (TextView) convertView
			.findViewById(R.id.ritual_list_item_description_text_view);
		holder.mNameTextView = (TextView) convertView
			.findViewById(R.id.ritual_list_item_name_text_view);
		holder.mTimeTextView = (TextView) convertView
			.findViewById(R.id.ritual_list_item_time_text_view);
		convertView.setTag(holder);
	    } else {
		holder = (ViewHolder) convertView.getTag();
	    }

	    holder.mCostTextView.setText(ritual.getCost());
	    holder.mDescriptionTextView.setText(ritual.getDescription());
	    holder.mNameTextView.setText(ritual.getName());
	    holder.mTimeTextView.setText(ritual.getTime());

	    return convertView;
	}

	public void sort() {
	    sort(mRitualComparator);
	}
    }

    public static final String CHARACTER = "com.thomasgallinari.dndcharactersheet.CHARACTER";

    private static final int DIALOG_RITUAL_CREATE = 1;
    private static final int DIALOG_RITUAL_DELETE = 2;
    private static final int DIALOG_RITUAL_EDIT = 3;

    private Character mCharacter;
    private AlertDialog mDeleteRitualDialog;
    private TextView mEmptyTextView;
    private ProgressBar mProgressBar;
    private RitualDialog mRitualDialog;
    private Ritual mSelectedRitual;

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
	case R.id.rituals_new_ritual_button:
	    showDialog(DIALOG_RITUAL_CREATE);
	    break;
	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.rituals_context_menu_delete_item:
	    showDialog(DIALOG_RITUAL_DELETE);
	    return true;
	case R.id.rituals_context_menu_open_item:
	    showDialog(DIALOG_RITUAL_EDIT);
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

	mSelectedRitual = (Ritual) getListAdapter().getItem(
		((AdapterContextMenuInfo) menuInfo).position);
	menu.setHeaderTitle(mSelectedRitual.getName());
	getMenuInflater().inflate(R.menu.rituals_context_menu, menu);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDismiss(DialogInterface dialog) {
	fillData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.rituals);

	mCharacter = (Character) getIntent().getSerializableExtra(CHARACTER);

	mEmptyTextView = (TextView) findViewById(android.R.id.empty);
	mProgressBar = (ProgressBar) findViewById(R.id.rituals_progress_bar);

	registerForContextMenu(getListView());

	fillData();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
	Dialog dialog = null;
	switch (id) {
	case DIALOG_RITUAL_CREATE:
	case DIALOG_RITUAL_EDIT:
	    dialog = getRitualDialog();
	    break;
	case DIALOG_RITUAL_DELETE:
	    dialog = getRitualDeleteAlertDialog();
	    break;
	}
	return dialog;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
	super.onListItemClick(l, v, position, id);
	mSelectedRitual = (Ritual) getListAdapter().getItem(position);
	showDialog(DIALOG_RITUAL_EDIT);
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
	super.onPrepareDialog(id, dialog, args);

	switch (id) {
	case DIALOG_RITUAL_CREATE:
	    Ritual ritual = new Ritual();
	    ritual.setCharacter(mCharacter);
	    ((RitualDialog) dialog).setRitual(ritual);
	    break;
	case DIALOG_RITUAL_EDIT:
	    ((RitualDialog) dialog).setRitual(mSelectedRitual);
	    break;
	}
    }

    private void fillData() {
	new QueryRitualsTask().execute();
    }

    private AlertDialog getRitualDeleteAlertDialog() {
	if (mDeleteRitualDialog == null) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage(R.string.rituals_delete_alert_dialog_message)
		    .setCancelable(true).setPositiveButton(R.string.global_yes,
			    new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
					int id) {
				    try {
					getHelper().getRitualDao().delete(
						mSelectedRitual);
					new QueryRitualsTask().execute();
				    } catch (SQLException e) {
					ErrorHandler
						.log(
							RitualsActivity.this
								.getClass(),
							"Failed to delete the ritual",
							e,
							getString(R.string.error_ritual_delete),
							RitualsActivity.this);
				    }
				}
			    }).setNegativeButton(R.string.global_no,
			    new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
					int id) {
				    dialog.cancel();
				}
			    });
	    mDeleteRitualDialog = builder.create();
	}
	return mDeleteRitualDialog;
    }

    private Dialog getRitualDialog() {
	if (mRitualDialog == null) {
	    mRitualDialog = new RitualDialog(this);
	    mRitualDialog.setOwnerActivity(this);
	    mRitualDialog.setOnDismissListener(this);
	}
	return mRitualDialog;
    }
}
