/**
 * Copyright (C) 2011 Thomas Gallinari
 */
package com.thomasgallinari.dndcharactersheet.view;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OrmLiteBaseListActivity;
import com.j256.ormlite.stmt.QueryBuilder;
import com.thomasgallinari.dndcharactersheet.R;
import com.thomasgallinari.dndcharactersheet.db.DatabaseHelper;
import com.thomasgallinari.dndcharactersheet.model.Ability;
import com.thomasgallinari.dndcharactersheet.model.Character;
import com.thomasgallinari.dndcharactersheet.model.Skill;
import com.thomasgallinari.dndcharactersheet.view.util.ErrorHandler;

/**
 * An activity that shows all the character skills.
 */
public class SkillsActivity extends OrmLiteBaseListActivity<DatabaseHelper>
	implements OnDismissListener {

    class QuerySkillsTask extends AsyncTask<Void, Void, List<Skill>> {

	@Override
	protected List<Skill> doInBackground(Void... params) {
	    List<Skill> skills = new ArrayList<Skill>();
	    try {
		QueryBuilder<Skill, Integer> queryBuilder = getHelper()
			.getSkillDao().queryBuilder();
		queryBuilder.setWhere(getHelper().getSkillDao().queryBuilder()
			.where().eq("character_id", mCharacter.getId()));
		skills = getHelper().getSkillDao()
			.query(queryBuilder.prepare());
	    } catch (SQLException e) {
		ErrorHandler.log(SkillsActivity.this.getClass(),
			"Failed to load the skills", e,
			getString(R.string.error_skill_load),
			SkillsActivity.this);
	    }
	    return skills;
	}

	@Override
	protected void onPostExecute(List<Skill> skills) {
	    SkillListAdapter adapter = new SkillListAdapter(
		    SkillsActivity.this, R.layout.skill_list_item, skills);
	    adapter.sort();
	    setListAdapter(adapter);
	    mProgressBar.setVisibility(View.INVISIBLE);
	    mEmptyTextView.setText(getText(R.string.skills_empty));
	}

	@Override
	protected void onPreExecute() {
	    mProgressBar.setVisibility(View.VISIBLE);
	    mEmptyTextView.setText(getText(R.string.skills_loading));
	}
    }

    class SkillListAdapter extends ArrayAdapter<Skill> {

	class ViewHolder {
	    StringBuilder mNameStringBuilder;
	    TextView mNameTextView;
	    TextView mScoreTextView;
	}

	public SkillListAdapter(Context context, int textViewResourceId,
		List<Skill> objects) {
	    super(context, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    Skill skill = getItem(position);
	    skill.setCharacter(mCharacter);

	    ViewHolder holder;

	    if (convertView == null) {
		convertView = LayoutInflater.from(getContext()).inflate(
			R.layout.skill_list_item, null);
		holder = new ViewHolder();
		holder.mNameStringBuilder = new StringBuilder();
		holder.mNameTextView = (TextView) convertView
			.findViewById(R.id.skill_list_item_name_text_view);
		holder.mScoreTextView = (TextView) convertView
			.findViewById(R.id.skill_list_item_score_text_view);
		convertView.setTag(holder);
	    } else {
		holder = (ViewHolder) convertView.getTag();
	    }

	    holder.mNameStringBuilder.setLength(0);
	    holder.mNameStringBuilder
		    .append(getSkillName(skill))
		    .append(" (")
		    .append(Ability.getName(skill.getAbility(), getResources()))
		    .append(")");
	    holder.mNameTextView.setText(holder.mNameStringBuilder);
	    holder.mScoreTextView.setText(String.valueOf(skill.getScore()));

	    return convertView;
	}

	public void sort() {
	    sort(new Comparator<Skill>() {

		@Override
		public int compare(Skill skill1, Skill skill2) {
		    return getSkillName(skill1).compareTo(getSkillName(skill2));
		}
	    });
	}
    }

    public static final String CHARACTER = "com.thomasgallinari.dndcharactersheet.CHARACTER";

    private static final int DIALOG_SKILL_EDIT = 2;

    private Character mCharacter;
    private TextView mEmptyTextView;
    private ProgressBar mProgressBar;
    private Skill mSelectedSkill;
    private SkillDetailDialog mSkillDialog;

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
    public void onDismiss(DialogInterface dialog) {
	new QuerySkillsTask().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.skills);

	mCharacter = (Character) getIntent().getSerializableExtra(CHARACTER);

	mEmptyTextView = (TextView) findViewById(android.R.id.empty);
	mProgressBar = (ProgressBar) findViewById(R.id.skills_progress_bar);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
	Dialog dialog = null;
	switch (id) {
	case DIALOG_SKILL_EDIT:
	    if (mSkillDialog == null) {
		mSkillDialog = new SkillDetailDialog(this);
	    }
	    dialog = mSkillDialog;
	    break;
	}
	if (dialog != null) {
	    dialog.setOwnerActivity(this);
	    dialog.setOnDismissListener(this);
	}
	return dialog;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
	super.onListItemClick(l, v, position, id);
	mSelectedSkill = (Skill) getListAdapter().getItem(position);
	showDialog(DIALOG_SKILL_EDIT);
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
	super.onPrepareDialog(id, dialog, args);

	SkillDetailDialog skillDialog = (SkillDetailDialog) dialog;
	skillDialog.setSkill(mSelectedSkill);
	skillDialog.setTitle(getSkillName(mSelectedSkill));
    }

    @Override
    protected void onResume() {
	super.onResume();
	fillData();
    }

    private void fillData() {
	new QuerySkillsTask().execute();
    }

    private String getSkillName(Skill skill) {
	String skillName = "";
	try {
	    skillName = getString(R.string.class.getField(skill.getName())
		    .getInt(R.string.class));
	} catch (IllegalArgumentException e) {
	    ErrorHandler.log(getClass(), "Could not find skill name", e);
	} catch (SecurityException e) {
	    ErrorHandler.log(getClass(), "Could not find skill name", e);
	} catch (IllegalAccessException e) {
	    ErrorHandler.log(getClass(), "Could not find skill name", e);
	} catch (NoSuchFieldException e) {
	    ErrorHandler.log(getClass(), "Could not find skill name", e);
	}
	return skillName;
    }
}
