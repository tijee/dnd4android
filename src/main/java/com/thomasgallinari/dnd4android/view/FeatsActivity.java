/**
 * Copyright (C) 2011 Thomas Gallinari
 */
package com.thomasgallinari.dnd4android.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OrmLiteBaseListActivity;
import com.j256.ormlite.stmt.QueryBuilder;
import com.thomasgallinari.dnd4android.R;
import com.thomasgallinari.dnd4android.db.DatabaseHelper;
import com.thomasgallinari.dnd4android.model.Character;
import com.thomasgallinari.dnd4android.model.Feat;
import com.thomasgallinari.dnd4android.view.util.ErrorHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * An activity that shows all the character feats.
 */
public class FeatsActivity extends OrmLiteBaseListActivity<DatabaseHelper>
        implements OnClickListener, OnDismissListener {

    class FeatListAdapter extends ArrayAdapter<Feat> {

        class ViewHolder {
            TextView mDescriptionTextView;
            TextView mNameTextView;
        }

        private Comparator<Feat> mFeatComparator;

        public FeatListAdapter(Context context, int textViewResourceId,
                               List<Feat> objects) {
            super(context, textViewResourceId, objects);
            mFeatComparator = new Comparator<Feat>() {
                public int compare(Feat feat1, Feat feat2) {
                    return feat1.compareTo(feat2);
                }

                ;
            };
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Feat feat = getItem(position);
            feat.setCharacter(mCharacter);

            ViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.feat_list_item, null);
                holder = new ViewHolder();
                holder.mDescriptionTextView = (TextView) convertView
                        .findViewById(R.id.feat_list_item_description_text_view);
                holder.mNameTextView = (TextView) convertView
                        .findViewById(R.id.feat_list_item_name_text_view);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.mNameTextView.setText(feat.getName());
            holder.mDescriptionTextView.setText(feat.getDescription());

            return convertView;
        }

        public void sort() {
            sort(mFeatComparator);
        }
    }

    class QueryFeatsTask extends AsyncTask<Void, Void, List<Feat>> {

        @Override
        protected List<Feat> doInBackground(Void... params) {
            List<Feat> feats = new ArrayList<Feat>();
            try {
                QueryBuilder<Feat, Integer> queryBuilder = getHelper()
                        .getFeatDao().queryBuilder();
                queryBuilder.setWhere(getHelper().getFeatDao().queryBuilder()
                        .where().eq("character_id", mCharacter.getId()));
                feats = getHelper().getFeatDao().query(queryBuilder.prepare());
            } catch (SQLException e) {
                ErrorHandler
                        .log(FeatsActivity.this.getClass(),
                                "Failed to load the feats", e,
                                getString(R.string.error_feat_load),
                                FeatsActivity.this);
            }
            return feats;
        }

        @Override
        protected void onPostExecute(List<Feat> feats) {
            FeatListAdapter adapter = new FeatListAdapter(FeatsActivity.this,
                    R.layout.feat_list_item, feats);
            adapter.sort();
            setListAdapter(adapter);
            mProgressBar.setVisibility(View.INVISIBLE);
            mEmptyTextView.setText(getText(R.string.feats_empty));
        }

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            mEmptyTextView.setText(getText(R.string.feats_loading));
        }
    }

    public static final String CHARACTER = "com.thomasgallinari.dndcharactersheet.CHARACTER";

    private static final int DIALOG_FEAT_CREATE = 1;
    private static final int DIALOG_FEAT_DELETE = 2;
    private static final int DIALOG_FEAT_EDIT = 3;

    private static final String BUNDLE_SELECTED_FEAT = "selected_feat";

    private Character mCharacter;
    private AlertDialog mDeleteFeatDialog;
    private TextView mEmptyTextView;
    private FeatDialog mFeatDialog;
    private ProgressBar mProgressBar;
    private Feat mSelectedFeat;

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
            case R.id.feats_new_feat_button:
                showDialog(DIALOG_FEAT_CREATE);
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.feats_context_menu_delete_item:
                showDialog(DIALOG_FEAT_DELETE);
                return true;
            case R.id.feats_context_menu_open_item:
                showDialog(DIALOG_FEAT_EDIT);
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

        mSelectedFeat = (Feat) getListAdapter().getItem(
                ((AdapterContextMenuInfo) menuInfo).position);
        menu.setHeaderTitle(mSelectedFeat.getName());
        getMenuInflater().inflate(R.menu.feats_context_menu, menu);
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

        setContentView(R.layout.feats);

        if (savedInstanceState != null) {
            mSelectedFeat = (Feat) savedInstanceState.get(BUNDLE_SELECTED_FEAT);
        }

        mCharacter = (Character) getIntent().getSerializableExtra(CHARACTER);

        mEmptyTextView = (TextView) findViewById(android.R.id.empty);
        mProgressBar = (ProgressBar) findViewById(R.id.feats_progress_bar);

        registerForContextMenu(getListView());

        fillData();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch (id) {
            case DIALOG_FEAT_CREATE:
            case DIALOG_FEAT_EDIT:
                dialog = getFeatDialog();
                break;
            case DIALOG_FEAT_DELETE:
                dialog = getFeatDeleteAlertDialog();
                break;
        }
        return dialog;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mSelectedFeat = (Feat) getListAdapter().getItem(position);
        showDialog(DIALOG_FEAT_EDIT);
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
        super.onPrepareDialog(id, dialog, args);

        switch (id) {
            case DIALOG_FEAT_CREATE:
                Feat feat = new Feat();
                feat.setCharacter(mCharacter);
                ((FeatDialog) dialog).setFeat(feat);
                break;
            case DIALOG_FEAT_EDIT:
                ((FeatDialog) dialog).setFeat(mSelectedFeat);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(BUNDLE_SELECTED_FEAT, mSelectedFeat);
    }

    private void fillData() {
        new QueryFeatsTask().execute();
    }

    private Dialog getFeatDialog() {
        if (mFeatDialog == null) {
            mFeatDialog = new FeatDialog(this);
            mFeatDialog.setOwnerActivity(this);
            mFeatDialog.setOnDismissListener(this);
        }
        return mFeatDialog;
    }

    private AlertDialog getFeatDeleteAlertDialog() {
        if (mDeleteFeatDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.feats_delete_alert_dialog_message)
                    .setCancelable(true).setPositiveButton(R.string.global_yes,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int id) {
                            try {
                                getHelper().getFeatDao().delete(
                                        mSelectedFeat);
                                new QueryFeatsTask().execute();
                            } catch (SQLException e) {
                                ErrorHandler
                                        .log(
                                                FeatsActivity.this
                                                        .getClass(),
                                                "Failed to delete the feat",
                                                e,
                                                getString(R.string.error_feat_delete),
                                                FeatsActivity.this);
                            }
                        }
                    }).setNegativeButton(R.string.global_no,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int id) {
                            dialog.cancel();
                        }
                    });
            mDeleteFeatDialog = builder.create();
        }
        return mDeleteFeatDialog;
    }
}
