/**
 * Copyright (C) 2011 Thomas Gallinari
 */
package com.thomasgallinari.dnd4android.view;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteBaseTabActivity;
import com.j256.ormlite.stmt.QueryBuilder;
import com.thomasgallinari.dnd4android.R;
import com.thomasgallinari.dnd4android.db.DatabaseHelper;
import com.thomasgallinari.dnd4android.model.Character;
import com.thomasgallinari.dnd4android.model.Power;
import com.thomasgallinari.dnd4android.view.util.ErrorHandler;

/**
 * An activity that displays the character sheet.
 */
public class SheetActivity extends OrmLiteBaseTabActivity<DatabaseHelper>
	implements OnItemClickListener {

    class QueryRecoverAllPowersTask extends AsyncTask<Void, Void, List<Power>> {

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
		ErrorHandler.log(SheetActivity.this.getClass(),
			"Failed to load the powers", e,
			getString(R.string.error_power_load),
			SheetActivity.this);
	    }
	    return powers;
	}

	@Override
	protected void onPostExecute(List<Power> powers) {
	    for (Power power : powers) {
		power.setUsed(false);
		try {
		    getHelper().getPowerDao().update(power);
		} catch (SQLException e) {
		    ErrorHandler.log(SheetActivity.this.getClass(),
			    "Failed to update the power", e,
			    getString(R.string.error_power_update),
			    SheetActivity.this);
		}
	    }

	    if (getCurrentActivity() instanceof PowersActivity) {
		((PowersActivity) getCurrentActivity()).refresh();
	    }

	    getWaitDialog().dismiss();
	}

	@Override
	protected void onPreExecute() {
	    showDialog(DIALOG_WAIT);
	}
    }

    class QueryRecoverEncounterPowersTask extends
	    AsyncTask<Void, Void, List<Power>> {

	@Override
	protected List<Power> doInBackground(Void... params) {
	    List<Power> powers = new ArrayList<Power>();
	    try {
		QueryBuilder<Power, Integer> queryBuilder = getHelper()
			.getPowerDao().queryBuilder();
		queryBuilder.setWhere(getHelper().getPowerDao().queryBuilder()
			.where().eq("character_id", mCharacter.getId()).and()
			.eq("type", Power.ENCOUNTER));
		powers = getHelper().getPowerDao()
			.query(queryBuilder.prepare());
	    } catch (SQLException e) {
		ErrorHandler.log(SheetActivity.this.getClass(),
			"Failed to load the powers", e,
			getString(R.string.error_power_load),
			SheetActivity.this);
	    }
	    return powers;
	}

	@Override
	protected void onPostExecute(List<Power> powers) {
	    for (Power power : powers) {
		power.setUsed(false);
		try {
		    getHelper().getPowerDao().update(power);
		} catch (SQLException e) {
		    ErrorHandler.log(SheetActivity.this.getClass(),
			    "Failed to update the power", e,
			    getString(R.string.error_power_update),
			    SheetActivity.this);
		}
	    }

	    if (getCurrentActivity() instanceof PowersActivity) {
		((PowersActivity) getCurrentActivity()).refresh();
	    }

	    getWaitDialog().dismiss();
	}

	@Override
	protected void onPreExecute() {
	    showDialog(DIALOG_WAIT);
	}
    }

    class TabAdapter extends BaseAdapter {

	@Override
	public int getCount() {
	    return TABS.length;
	}

	@Override
	public Object getItem(int position) {
	    return position;
	}

	@Override
	public long getItemId(int position) {
	    return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    if (convertView == null) {
		convertView = new ImageView(SheetActivity.this);
		((ImageView) convertView)
			.setScaleType(ImageView.ScaleType.CENTER);
	    }
	    if (position == getTabHost().getCurrentTab()) {
		((ImageView) convertView)
			.setImageResource(TABS_SELECTED[position]);
	    } else {
		((ImageView) convertView).setImageResource(TABS[position]);
	    }
	    return convertView;
	}
    }

    public static final String CHARACTER = "com.thomasgallinari.dndcharactersheet.CHARACTER";

    private static final int DIALOG_DAMAGE = 1;
    private static final int DIALOG_EXTENDED_REST = 2;
    private static final int DIALOG_SHORT_REST = 3;
    private static final int DIALOG_SURGE = 4;
    private static final int DIALOG_WAIT = 5;

    private static final String TAB_ATTACK = "tab_attack";
    private static final String TAB_EQUIPMENT = "tab_equipment";
    private static final String TAB_FEATS = "tab_feats";
    private static final String TAB_HEALTH = "tab_health";
    private static final String TAB_IDENTITY = "tab_identity";
    private static final String TAB_NOTES = "tab_notes";
    private static final String TAB_POWERS = "tab_powers";
    private static final String TAB_RITUALS = "tab_rituals";
    private static final String TAB_SCORES = "tab_scores";
    private static final String TAB_SKILLS = "tab_skills";

    private static final Integer[] TABS = { R.drawable.tab_identity_unselected,
	    R.drawable.tab_scores_unselected, R.drawable.tab_attack_unselected,
	    R.drawable.tab_health_unselected, R.drawable.tab_powers_unselected,
	    R.drawable.tab_skills_unselected, R.drawable.tab_feats_unselected,
	    R.drawable.tab_equipment_unselected,
	    R.drawable.tab_rituals_unselected, R.drawable.tab_notes_unselected };
    private static final Integer[] TABS_SELECTED = {
	    R.drawable.tab_identity_selected, R.drawable.tab_scores_selected,
	    R.drawable.tab_attack_selected, R.drawable.tab_health_selected,
	    R.drawable.tab_powers_selected, R.drawable.tab_skills_selected,
	    R.drawable.tab_feats_selected, R.drawable.tab_equipment_selected,
	    R.drawable.tab_rituals_selected, R.drawable.tab_notes_selected };

    private Character mCharacter;
    private AlertDialog mDamageDialog;
    private AlertDialog mExtendedRestDialog;
    private AlertDialog mShortRestDialog;
    private AlertDialog mSurgeDialog;
    private Gallery mTabGallery;
    private AlertDialog mWaitDialog;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.sheet_menu, menu);
	return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
	    long id) {
	getTabHost().setCurrentTab(position);
	((BaseAdapter) mTabGallery.getAdapter()).notifyDataSetChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	boolean consume = false;
	switch (item.getItemId()) {
	case R.id.sheet_menu_damage_item:
	    showDialog(DIALOG_DAMAGE);
	    consume = true;
	    break;
	case R.id.sheet_menu_extended_rest_item:
	    showDialog(DIALOG_EXTENDED_REST);
	    consume = true;
	    break;
	case R.id.sheet_menu_short_rest:
	    showDialog(DIALOG_SHORT_REST);
	    consume = true;
	    break;
	case R.id.sheet_menu_surge_item:
	    // spendSurge();
	    showDialog(DIALOG_SURGE);
	    consume = true;
	    break;
	default:
	    consume = super.onOptionsItemSelected(item);
	}
	return consume;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.sheet);

	mCharacter = (Character) getIntent().getSerializableExtra(CHARACTER);

	TabHost tabHost = getTabHost();
	TabHost.TabSpec spec;
	Intent intent;

	intent = new Intent().setClass(this, IdentityActivity.class).putExtra(
		IdentityActivity.CHARACTER, mCharacter);
	spec = tabHost.newTabSpec(TAB_IDENTITY).setIndicator("")
		.setContent(intent);
	tabHost.addTab(spec);

	intent = new Intent().setClass(this, ScoresActivity.class).putExtra(
		ScoresActivity.CHARACTER, mCharacter);
	spec = tabHost.newTabSpec(TAB_SCORES).setIndicator("")
		.setContent(intent);
	tabHost.addTab(spec);

	intent = new Intent().setClass(this, AttacksActivity.class).putExtra(
		AttacksActivity.CHARACTER, mCharacter);
	spec = tabHost.newTabSpec(TAB_ATTACK).setIndicator("")
		.setContent(intent);
	tabHost.addTab(spec);

	intent = new Intent().setClass(this, HealthActivity.class).putExtra(
		HealthActivity.CHARACTER, mCharacter);
	spec = tabHost.newTabSpec(TAB_HEALTH).setIndicator("")
		.setContent(intent);
	tabHost.addTab(spec);

	intent = new Intent().setClass(this, PowersActivity.class).putExtra(
		PowersActivity.CHARACTER, mCharacter);
	spec = tabHost.newTabSpec(TAB_POWERS).setIndicator("")
		.setContent(intent);
	tabHost.addTab(spec);

	intent = new Intent().setClass(this, SkillsActivity.class).putExtra(
		SkillsActivity.CHARACTER, mCharacter);
	spec = tabHost.newTabSpec(TAB_SKILLS).setIndicator("")
		.setContent(intent);
	tabHost.addTab(spec);

	intent = new Intent().setClass(this, FeatsActivity.class).putExtra(
		FeatsActivity.CHARACTER, mCharacter);
	spec = tabHost.newTabSpec(TAB_FEATS).setIndicator("")
		.setContent(intent);
	tabHost.addTab(spec);

	intent = new Intent().setClass(this, EquipmentActivity.class).putExtra(
		EquipmentActivity.CHARACTER, mCharacter);
	spec = tabHost.newTabSpec(TAB_EQUIPMENT).setIndicator("")
		.setContent(intent);
	tabHost.addTab(spec);

	intent = new Intent().setClass(this, RitualsActivity.class).putExtra(
		EquipmentActivity.CHARACTER, mCharacter);
	spec = tabHost.newTabSpec(TAB_RITUALS).setIndicator("")
		.setContent(intent);
	tabHost.addTab(spec);

	intent = new Intent().setClass(this, NotesActivity.class).putExtra(
		NotesActivity.CHARACTER, mCharacter);
	spec = tabHost.newTabSpec(TAB_NOTES).setIndicator("")
		.setContent(intent);
	tabHost.addTab(spec);

	TabWidget tabWidget = getTabWidget();
	for (int i = 0; i < tabWidget.getChildCount(); i++) {
	    View view = tabWidget.getChildAt(i);
	    view.setBackgroundDrawable(null);
	}

	mTabGallery = (Gallery) findViewById(R.id.sheet_tabs_gallery);
	mTabGallery.setAdapter(new TabAdapter());
	mTabGallery.setOnItemClickListener(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Dialog onCreateDialog(int id) {
	Dialog d = null;

	switch (id) {
	case DIALOG_DAMAGE:
	    if (mDamageDialog == null) {
		Builder builder = new Builder(this);
		View damageDialogView = getLayoutInflater().inflate(
			R.layout.damage_dialog, null);
		final EditText damageEditText = (EditText) damageDialogView
			.findViewById(R.id.damage_dialog_edit_text);
		builder.setView(damageDialogView)
			.setPositiveButton(R.string.global_ok,
				new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog,
					    int id) {
					if (damageEditText.getText().length() > 0) {
					    damage(Integer
						    .parseInt(damageEditText
							    .getText()
							    .toString()));
					} else {
					    dialog.cancel();
					}
					damageEditText.setText("");
				    }
				})
			.setNegativeButton(R.string.global_cancel,
				new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog,
					    int id) {
					dialog.cancel();
					damageEditText.setText("");
				    }
				});
		mDamageDialog = builder.create();
	    }
	    d = mDamageDialog;
	    break;
	case DIALOG_EXTENDED_REST:
	    if (mExtendedRestDialog == null) {
		Builder builder = new Builder(this);
		builder.setMessage(R.string.sheet_rest_alert)
			.setCancelable(false)
			.setPositiveButton(R.string.global_yes,
				new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog,
					    int id) {
					extendedRest();
				    }
				})
			.setNegativeButton(R.string.global_no,
				new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog,
					    int id) {
					dialog.cancel();
				    }
				});
		mExtendedRestDialog = builder.create();
	    }
	    d = mExtendedRestDialog;
	    break;
	case DIALOG_SHORT_REST:
	    if (mShortRestDialog == null) {
		Builder builder = new Builder(this);
		builder.setMessage(R.string.sheet_rest_alert)
			.setCancelable(false)
			.setPositiveButton(R.string.global_yes,
				new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog,
					    int id) {
					shortRest();
				    }
				})
			.setNegativeButton(R.string.global_no,
				new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog,
					    int id) {
					dialog.cancel();
				    }
				});
		mShortRestDialog = builder.create();
	    }
	    d = mShortRestDialog;
	    break;
	case DIALOG_SURGE:
	    if (mSurgeDialog == null) {
		Builder builder = new Builder(this);
		View surgeDialogView = getLayoutInflater().inflate(
			R.layout.surge_dialog, null);
		final EditText surgeEditText = (EditText) surgeDialogView
			.findViewById(R.id.surge_dialog_edit_text);
		final RadioGroup radioGroup = (RadioGroup) surgeDialogView
			.findViewById(R.id.surge_dialog_radio_group);
		builder.setView(surgeDialogView)
			.setPositiveButton(R.string.global_ok,
				new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog,
					    int id) {
					if (surgeEditText.getText().length() > 0) {
					    int n = Integer
						    .parseInt(surgeEditText
							    .getText()
							    .toString());
					    switch (radioGroup
						    .getCheckedRadioButtonId()) {
					    case R.id.surge_dialog_surge_radio_button:
						spendSurges(n);
						break;
					    case R.id.surge_dialog_hp_radio_button:
						gainHp(n);
						break;
					    case R.id.surge_dialog_hp_temp_radio_button:
						gainTempHp(n);
						break;
					    }
					} else {
					    dialog.cancel();
					}
					surgeEditText.setText("");
				    }
				})
			.setNegativeButton(R.string.global_cancel,
				new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog,
					    int id) {
					dialog.cancel();
					surgeEditText.setText("");
				    }
				});
		mSurgeDialog = builder.create();
	    }
	    d = mSurgeDialog;
	    break;
	case DIALOG_WAIT:
	    d = getWaitDialog();
	}
	return d;
    }

    private void damage(int damage) {
	if (damage != 0) {
	    int tempHpBeforeDamage = mCharacter.getHpTemp();
	    int realHpBeforeDamage = mCharacter.getHpCurrent();

	    int tempHpAfterDamage = tempHpBeforeDamage - damage;
	    if (tempHpAfterDamage >= 0) {
		mCharacter.setHpTemp(tempHpAfterDamage);
	    } else {
		int damageOnRealHp = -tempHpAfterDamage;
		int realHpAfterDamage = realHpBeforeDamage - damageOnRealHp;
		mCharacter.setHpTemp(0);
		mCharacter.setHpCurrent(realHpAfterDamage);
	    }

	    try {
		getHelper().getCharacterDao().update(mCharacter);

		Toast toast = Toast.makeText(
			this,
			getString(R.string.sheet_remaining_hp,
				mCharacter.getHpCurrent(),
				mCharacter.getHpTemp()), Toast.LENGTH_SHORT);
		toast.show();

		if (mCharacter.isDead()) {
		    toast = Toast.makeText(this, R.string.sheet_dead,
			    Toast.LENGTH_SHORT);
		    toast.show();
		} else if (mCharacter.isDying()) {
		    toast = Toast.makeText(this, R.string.sheet_dying,
			    Toast.LENGTH_SHORT);
		    toast.show();
		} else if (mCharacter.isBloodied()) {
		    toast = Toast.makeText(this, R.string.sheet_bloodied,
			    Toast.LENGTH_SHORT);
		    toast.show();
		}

		if (getCurrentActivity() instanceof HealthActivity) {
		    ((HealthActivity) getCurrentActivity()).refresh();
		}
	    } catch (SQLException e) {
		ErrorHandler.log(getClass(), "Failed to update the character",
			e, getString(R.string.error_character_update), this);
	    }
	}
    }

    private void extendedRest() {
	mCharacter.setActionPointsCurrent(mCharacter.getActionPointsMax());
	mCharacter.setDeathSavingFailures(0);
	mCharacter.setHpCurrent(mCharacter.getHpMax());
	mCharacter.setHpTemp(0);
	mCharacter.setSecondWind(false);
	mCharacter.setSurgesCurrent(mCharacter.getSurgesPerDay());

	try {
	    getHelper().getCharacterDao().update(mCharacter);

	    if (getCurrentActivity() instanceof HealthActivity) {
		((HealthActivity) getCurrentActivity()).refresh();
	    }
	} catch (SQLException e) {
	    ErrorHandler.log(getClass(), "Failed to update the character", e,
		    getString(R.string.error_character_update), this);
	}

	new QueryRecoverAllPowersTask().execute();
    }

    private void gainHp(int n) {
	mCharacter.setHpCurrent(Math.min(mCharacter.getHpMax(),
		mCharacter.getHpCurrent() + n));

	try {
	    getHelper().getCharacterDao().update(mCharacter);

	    Toast toast = Toast.makeText(
		    this,
		    getString(R.string.sheet_remaining_hp,
			    mCharacter.getHpCurrent(), mCharacter.getHpTemp()),
		    Toast.LENGTH_SHORT);
	    toast.show();

	    if (getCurrentActivity() instanceof HealthActivity) {
		((HealthActivity) getCurrentActivity()).refresh();
	    }
	} catch (SQLException e) {
	    ErrorHandler.log(getClass(), "Failed to update the character", e,
		    getString(R.string.error_character_update), this);
	}
    }

    private void gainTempHp(int n) {
	mCharacter.setHpTemp(mCharacter.getHpTemp() + n);

	Toast toast = Toast.makeText(
		this,
		getString(R.string.sheet_remaining_hp,
			mCharacter.getHpCurrent(), mCharacter.getHpTemp()),
		Toast.LENGTH_SHORT);
	toast.show();

	try {
	    getHelper().getCharacterDao().update(mCharacter);

	    if (getCurrentActivity() instanceof HealthActivity) {
		((HealthActivity) getCurrentActivity()).refresh();
	    }
	} catch (SQLException e) {
	    ErrorHandler.log(getClass(), "Failed to update the character", e,
		    getString(R.string.error_character_update), this);
	}
    }

    private AlertDialog getWaitDialog() {
	if (mWaitDialog == null) {
	    Builder builder = new Builder(this);
	    builder.setView(getLayoutInflater().inflate(R.layout.wait_dialog,
		    null));
	    mWaitDialog = builder.create();
	}
	return mWaitDialog;
    }

    private void shortRest() {
	mCharacter.setHpTemp(0);
	mCharacter.setSecondWind(false);

	try {
	    getHelper().getCharacterDao().update(mCharacter);

	    if (getCurrentActivity() instanceof HealthActivity) {
		((HealthActivity) getCurrentActivity()).refresh();
	    }
	} catch (SQLException e) {
	    ErrorHandler.log(getClass(), "Failed to update the character", e,
		    getString(R.string.error_character_update), this);
	}

	new QueryRecoverEncounterPowersTask().execute();
    }

    private void spendSurges(int n) {
	if (mCharacter.getSurgesCurrent() >= n) {
	    mCharacter.setSurgesCurrent(mCharacter.getSurgesCurrent() - n);
	    mCharacter
		    .setHpCurrent(Math.min(
			    mCharacter.getHpMax(),
			    mCharacter.getHpCurrent() + n
				    * mCharacter.getSurgeValue()));

	    try {
		getHelper().getCharacterDao().update(mCharacter);

		Toast toast = Toast.makeText(
			this,
			getResources().getQuantityString(
				R.plurals.sheet_remaining_health,
				mCharacter.getSurgesCurrent(),
				mCharacter.getHpCurrent(),
				mCharacter.getSurgesCurrent()),
			Toast.LENGTH_SHORT);
		toast.show();

		if (getCurrentActivity() instanceof HealthActivity) {
		    ((HealthActivity) getCurrentActivity()).refresh();
		}
	    } catch (SQLException e) {
		ErrorHandler.log(getClass(), "Failed to update the character",
			e, getString(R.string.error_character_update), this);
	    }
	} else {
	    Toast toast = Toast.makeText(this,
		    R.string.sheet_not_enough_surges, Toast.LENGTH_SHORT);
	    toast.show();
	}
    }
}
