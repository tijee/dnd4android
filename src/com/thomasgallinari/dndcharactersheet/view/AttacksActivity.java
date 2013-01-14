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
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.j256.ormlite.android.apptools.OrmLiteBaseListActivity;
import com.j256.ormlite.stmt.QueryBuilder;
import com.thomasgallinari.dndcharactersheet.R;
import com.thomasgallinari.dndcharactersheet.db.DatabaseHelper;
import com.thomasgallinari.dndcharactersheet.model.Attack;
import com.thomasgallinari.dndcharactersheet.model.Character;
import com.thomasgallinari.dndcharactersheet.view.util.ErrorHandler;

/**
 * An activity that shows the attack workspace.
 */
public class AttacksActivity extends OrmLiteBaseListActivity<DatabaseHelper>
	implements OnClickListener, OnDismissListener {

    class AttackListAdapter extends ArrayAdapter<Attack> {

	class ViewHolder {
	    Button mAttackButton;
	    Button mDamageButton;
	    TextView mNameTextView;
	}

	private Comparator<Attack> mAttackComparator;

	public AttackListAdapter(Context context, int textViewResourceId,
		List<Attack> objects) {
	    super(context, textViewResourceId, objects);
	    mAttackComparator = new Comparator<Attack>() {
		public int compare(Attack attack1, Attack attack2) {
		    return attack1.compareTo(attack2);
		};
	    };
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    final Attack attack = getItem(position);
	    attack.setCharacter(mCharacter);

	    ViewHolder holder;

	    if (convertView == null) {
		convertView = LayoutInflater.from(getContext()).inflate(
			R.layout.attack_list_item, null);
		holder = new ViewHolder();
		holder.mAttackButton = (Button) convertView
			.findViewById(R.id.attack_list_item_attack_button);
		holder.mDamageButton = (Button) convertView
			.findViewById(R.id.attack_list_item_damage_button);
		holder.mDamageButton.setOnClickListener(AttacksActivity.this);
		holder.mNameTextView = (TextView) convertView
			.findViewById(R.id.attack_list_item_name_text_view);
		convertView.setTag(holder);
	    } else {
		holder = (ViewHolder) convertView.getTag();
	    }

	    holder.mAttackButton.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
		    mSelectedAttack = attack;
		    showDialog(DIALOG_ATTACK_ATTACK);
		}
	    });
	    holder.mDamageButton.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
		    mSelectedAttack = attack;
		    showDialog(DIALOG_ATTACK_DAMAGE);
		}
	    });

	    holder.mAttackButton.setText(String
		    .valueOf(attack.getAttackScore()));
	    holder.mDamageButton.setText(String
		    .valueOf(attack.getDamageScore()));
	    holder.mNameTextView.setText(attack.getName());

	    return convertView;
	}

	public void sort() {
	    sort(mAttackComparator);
	}
    }

    class QueryAttacksTask extends AsyncTask<Void, Void, List<Attack>> {

	@Override
	protected List<Attack> doInBackground(Void... params) {
	    List<Attack> attacks = new ArrayList<Attack>();
	    try {
		QueryBuilder<Attack, Integer> queryBuilder = getHelper()
			.getAttackDao().queryBuilder();
		queryBuilder.setWhere(getHelper().getAttackDao().queryBuilder()
			.where().eq("character_id", mCharacter.getId()));
		attacks = getHelper().getAttackDao().query(
			queryBuilder.prepare());
	    } catch (SQLException e) {
		ErrorHandler.log(AttacksActivity.this.getClass(),
			"Failed to load the attacks", e,
			getString(R.string.error_attack_load),
			AttacksActivity.this);
	    }
	    return attacks;
	}

	@Override
	protected void onPostExecute(List<Attack> attacks) {
	    AttackListAdapter adapter = new AttackListAdapter(
		    AttacksActivity.this, R.layout.attack_list_item, attacks);
	    adapter.sort();
	    setListAdapter(adapter);
	    mProgressBar.setVisibility(View.INVISIBLE);
	    mEmptyTextView.setText(getText(R.string.attacks_empty));
	}

	@Override
	protected void onPreExecute() {
	    mProgressBar.setVisibility(View.VISIBLE);
	    mEmptyTextView.setText(getText(R.string.attacks_loading));
	}
    }

    public static final String CHARACTER = "com.thomasgallinari.dndcharactersheet.CHARACTER";

    private static final int DIALOG_ATTACK_ATTACK = 1;
    private static final int DIALOG_ATTACK_CREATE = 2;
    private static final int DIALOG_ATTACK_DAMAGE = 3;
    private static final int DIALOG_ATTACK_DELETE = 4;
    private static final int DIALOG_ATTACK_EDIT = 5;

    private AttackAttackDialog mAttackAttackDialog;
    private AttackDamageDialog mAttackDamageDialog;
    private AttackDialog mAttackDialog;
    private Character mCharacter;
    private AlertDialog mDeleteAttackDialog;
    private TextView mEmptyTextView;
    private ProgressBar mProgressBar;
    private Attack mSelectedAttack;

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
	case R.id.attack_list_item_damage_button:
	    showDialog(DIALOG_ATTACK_DAMAGE);
	    break;
	case R.id.attacks_new_attack_button:
	    showDialog(DIALOG_ATTACK_CREATE);
	    break;
	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.attacks_context_menu_delete_item:
	    showDialog(DIALOG_ATTACK_DELETE);
	    return true;
	case R.id.attacks_context_menu_open_item:
	    showDialog(DIALOG_ATTACK_EDIT);
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

	mSelectedAttack = (Attack) getListAdapter().getItem(
		((AdapterContextMenuInfo) menuInfo).position);
	menu.setHeaderTitle(mSelectedAttack.getName());
	getMenuInflater().inflate(R.menu.attacks_context_menu, menu);
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

	setContentView(R.layout.attacks);

	mCharacter = (Character) getIntent().getSerializableExtra(CHARACTER);

	mEmptyTextView = (TextView) findViewById(android.R.id.empty);
	mProgressBar = (ProgressBar) findViewById(R.id.attacks_progress_bar);

	registerForContextMenu(getListView());

	fillData();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
	Dialog dialog = null;
	switch (id) {
	case DIALOG_ATTACK_ATTACK:
	    dialog = getAttackAttackDialog();
	    break;
	case DIALOG_ATTACK_DAMAGE:
	    dialog = getAttackDamageDialog();
	    break;
	case DIALOG_ATTACK_DELETE:
	    dialog = getAttackDeleteAlertDialog();
	    break;
	case DIALOG_ATTACK_CREATE:
	case DIALOG_ATTACK_EDIT:
	    dialog = getAttackDialog();
	    break;
	}
	return dialog;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
	super.onListItemClick(l, v, position, id);
	mSelectedAttack = (Attack) getListAdapter().getItem(position);
	showDialog(DIALOG_ATTACK_EDIT);
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
	super.onPrepareDialog(id, dialog, args);

	switch (id) {
	case DIALOG_ATTACK_ATTACK:
	    ((AttackAttackDialog) dialog).setAttack(mSelectedAttack);
	    break;
	case DIALOG_ATTACK_CREATE:
	    Attack attack = new Attack();
	    attack.setCharacter(mCharacter);
	    ((AttackDialog) dialog).setAttack(attack);
	    break;
	case DIALOG_ATTACK_DAMAGE:
	    ((AttackDamageDialog) dialog).setAttack(mSelectedAttack);
	    break;
	case DIALOG_ATTACK_EDIT:
	    ((AttackDialog) dialog).setAttack(mSelectedAttack);
	    break;
	}
    }

    @Override
    protected void onResume() {
	super.onResume();
	fillData();
    }

    private void fillData() {
	new QueryAttacksTask().execute();
    }

    private Dialog getAttackAttackDialog() {
	if (mAttackAttackDialog == null) {
	    mAttackAttackDialog = new AttackAttackDialog(this);
	    mAttackAttackDialog.setOwnerActivity(this);
	    mAttackAttackDialog.setOnDismissListener(this);
	}
	return mAttackAttackDialog;
    }

    private Dialog getAttackDamageDialog() {
	if (mAttackDamageDialog == null) {
	    mAttackDamageDialog = new AttackDamageDialog(this);
	    mAttackDamageDialog.setOwnerActivity(this);
	    mAttackDamageDialog.setOnDismissListener(this);
	}
	return mAttackDamageDialog;
    }

    private AlertDialog getAttackDeleteAlertDialog() {
	if (mDeleteAttackDialog == null) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage(R.string.attacks_delete_alert_dialog_message)
		    .setCancelable(true).setPositiveButton(R.string.global_yes,
			    new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
					int id) {
				    try {
					getHelper().getAttackDao().delete(
						mSelectedAttack);
					new QueryAttacksTask().execute();
				    } catch (SQLException e) {
					ErrorHandler
						.log(
							AttacksActivity.this
								.getClass(),
							"Failed to delete the attack",
							e,
							getString(R.string.error_attack_delete),
							AttacksActivity.this);
				    }
				}
			    }).setNegativeButton(R.string.global_no,
			    new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
					int id) {
				    dialog.cancel();
				}
			    });
	    mDeleteAttackDialog = builder.create();
	}
	return mDeleteAttackDialog;
    }

    private Dialog getAttackDialog() {
	if (mAttackDialog == null) {
	    mAttackDialog = new AttackDialog(this);
	    mAttackDialog.setOwnerActivity(this);
	    mAttackDialog.setOnDismissListener(this);
	}
	return mAttackDialog;
    }
}
