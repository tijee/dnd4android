/**
 * Copyright (C) 2011 Thomas Gallinari
 */
package com.thomasgallinari.dnd4android.view;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OpenHelperManager.SqliteOpenHelperFactory;
import com.j256.ormlite.android.apptools.OrmLiteBaseListActivity;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.thomasgallinari.dnd4android.R;
import com.thomasgallinari.dnd4android.db.DatabaseHelper;
import com.thomasgallinari.dnd4android.model.Character;
import com.thomasgallinari.dnd4android.view.util.ErrorHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.util.List;

/**
 * A {@link ListActivity} containing the existing characters.
 */
public class CharactersActivity extends OrmLiteBaseListActivity<DatabaseHelper>
        implements OnClickListener, OnDismissListener {

    private static final String BACKUP_DIR = "/Android/data/com.thomasgallinari.dndcharactersheet/databases";
    private static final String DATA_PATH = "/data/com.thomasgallinari.dndcharactersheet/databases/dndcharactersheet.db";

    class BackupTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            File dbFile = new File(Environment.getDataDirectory() + DATA_PATH);

            File backupDir = new File(Environment.getExternalStorageDirectory()
                    + BACKUP_DIR);
            if (!backupDir.exists()) {
                if (!backupDir.mkdirs()) {
                    return false;
                }
            }
            File backupFile = new File(backupDir, dbFile.getName());

            try {
                backupFile.createNewFile();
                copyFile(dbFile, backupFile);
                return true;
            } catch (IOException e) {
                Log.e(CharactersActivity.class.getSimpleName(),
                        "Error on data backup", e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            getWaitDialog().dismiss();
            if (success) {
                Toast.makeText(CharactersActivity.this,
                        R.string.backup_success, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CharactersActivity.this, R.string.error_backup,
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            showDialog(DIALOG_WAIT);
        }

        private void copyFile(File src, File dst) throws IOException {
            FileChannel inChannel = new FileInputStream(src).getChannel();
            FileChannel outChannel = new FileOutputStream(dst).getChannel();
            try {
                inChannel.transferTo(0, inChannel.size(), outChannel);
            } finally {
                if (inChannel != null) {
                    inChannel.close();
                }
                if (outChannel != null) {
                    outChannel.close();
                }
            }
        }
    }

    class CharacterListAdapter extends ArrayAdapter<Character> {

        class ViewHolder {
            TextView mClassTextView;
            TextView mLevelTextView;
            TextView mNameTextView;
            TextView mRaceTextView;
        }

        public CharacterListAdapter(Context context, int textViewResourceId,
                                    List<Character> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.character_list_item, parent, false);
                holder = new ViewHolder();
                holder.mClassTextView = (TextView) convertView
                        .findViewById(R.id.character_list_item_class_text_view);
                holder.mLevelTextView = (TextView) convertView
                        .findViewById(R.id.character_list_item_level_text_view);
                holder.mNameTextView = (TextView) convertView
                        .findViewById(R.id.character_list_item_name_text_view);
                holder.mRaceTextView = (TextView) convertView
                        .findViewById(R.id.character_list_item_race_text_view);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.mClassTextView.setText(getItem(position).getClazz());
            holder.mLevelTextView.setText(String.valueOf(getItem(position)
                    .getLevel()));
            holder.mNameTextView.setText(getItem(position).getName());
            holder.mRaceTextView.setText(getItem(position).getRace());

            return convertView;
        }
    }

    class QueryCharactersTask extends AsyncTask<Void, Void, List<Character>> {

        @Override
        protected List<Character> doInBackground(Void... params) {
            List<Character> characters = null;
            try {
                characters = getHelper().getCharacterDao().queryForAll();
            } catch (SQLException e) {
                ErrorHandler.log(CharactersActivity.this.getClass(),
                        "Failed to load the characters", e,
                        getString(R.string.error_character_load),
                        CharactersActivity.this);
            }
            return characters;
        }

        @Override
        protected void onPostExecute(List<Character> characters) {
            setListAdapter(new CharacterListAdapter(CharactersActivity.this,
                    R.layout.character_list_item, characters));
            mProgressBar.setVisibility(View.INVISIBLE);
            mEmptyTextView.setText(getText(R.string.characters_empty));
        }

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            mEmptyTextView.setText(getText(R.string.characters_loading));
        }
    }

    class RestoreTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            File dbFile = new File(Environment.getDataDirectory() + DATA_PATH);
            if (dbFile.exists()) {
                dbFile.delete();
            }

            File backupFile = new File(
                    Environment.getExternalStorageDirectory() + BACKUP_DIR,
                    dbFile.getName());
            if (!backupFile.exists()) {
                return false;
            }

            try {
                dbFile.createNewFile();
                copyFile(backupFile, dbFile);
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            getWaitDialog().dismiss();
            if (success) {
                Toast.makeText(CharactersActivity.this,
                        R.string.restore_success, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(CharactersActivity.this, R.string.error_restore,
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            showDialog(DIALOG_WAIT);
        }

        private void copyFile(File src, File dst) throws IOException {
            FileChannel inChannel = new FileInputStream(src).getChannel();
            FileChannel outChannel = new FileOutputStream(dst).getChannel();
            try {
                inChannel.transferTo(0, inChannel.size(), outChannel);
            } finally {
                if (inChannel != null) {
                    inChannel.close();
                }
                if (outChannel != null) {
                    outChannel.close();
                }
            }
        }
    }

    private static final String BASE64_PUBLIC_KEY = "YOUR_OWN_KEY_HERE";
    private static final int DIALOG_CHARACTER_CREATE = 1;
    private static final int DIALOG_CHARACTER_DELETE = 2;
    private static final int DIALOG_PURCHASE = 3;
    private static final int DIALOG_WAIT = 4;
    private static final byte[] SALT = new byte[]{-104, -103, -67, 112, 41,
            -43, 90, -10, 36, -107, 62, -36, -99, 16, -32, -120, 117, 77, 25,
            -22};

    static {
        OpenHelperManager.setOpenHelperFactory(new SqliteOpenHelperFactory() {

            @Override
            public OrmLiteSqliteOpenHelper getHelper(Context context) {
                return new DatabaseHelper(context);
            }
        });
    }

    private AlertDialog mDeleteCharacterDialog;
    private TextView mEmptyTextView;
    private Intent mMarketIntent;
    private NewCharacterDialog mNewCharacterDialog;
    private ProgressBar mProgressBar;
    private AlertDialog mPurchaseDialog;
    private Character mSelectedCharacter;
    private AlertDialog mWaitDialog;

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
            case R.id.characters_new_character_button:
                showDialog(DIALOG_CHARACTER_CREATE);
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.characters_context_menu_delete_item:
                showDialog(DIALOG_CHARACTER_DELETE);
                return true;
            case R.id.characters_context_menu_open_item:
                startActivity(new Intent().setClass(this, SheetActivity.class)
                        .putExtra(SheetActivity.CHARACTER, mSelectedCharacter));
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
        mSelectedCharacter = (Character) getListAdapter().getItem(
                ((AdapterContextMenuInfo) menuInfo).position);
        menu.setHeaderTitle(mSelectedCharacter.getName());
        getMenuInflater().inflate(R.menu.characters_context_menu, menu);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global_menu, menu);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDismiss(DialogInterface dialog) {
        fillData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean consume = false;
        switch (item.getItemId()) {
            case R.id.global_menu_backup_item:
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    new BackupTask().execute();
                } else {
                    Toast.makeText(this, R.string.error_backup_no_external_storage,
                            Toast.LENGTH_SHORT).show();
                }
                consume = true;
                break;
            case R.id.global_menu_restore_item:
                new RestoreTask().execute();
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

        setContentView(R.layout.characters);

        mEmptyTextView = (TextView) findViewById(android.R.id.empty);
        mProgressBar = (ProgressBar) findViewById(R.id.characters_progress_bar);

        registerForContextMenu(getListView());
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog;
        switch (id) {
            case DIALOG_CHARACTER_CREATE:
                dialog = getCharacterCreateDialog();
                break;
            case DIALOG_CHARACTER_DELETE:
                dialog = getCharacterDeleteAlertDialog();
                break;
            case DIALOG_PURCHASE:
                dialog = getPurchaseDialog();
                break;
            case DIALOG_WAIT:
                dialog = getWaitDialog();
                break;
            default:
                dialog = null;
        }
        return dialog;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mSelectedCharacter = (Character) getListAdapter().getItem(position);
        startActivity(new Intent().setClass(this, SheetActivity.class)
                .putExtra(SheetActivity.CHARACTER, mSelectedCharacter));
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillData();
    }

    private void fillData() {
        new QueryCharactersTask().execute();
    }

    private Dialog getCharacterCreateDialog() {
        if (mNewCharacterDialog == null) {
            mNewCharacterDialog = new NewCharacterDialog(this);
            mNewCharacterDialog.setOwnerActivity(this);
            mNewCharacterDialog.setOnDismissListener(this);
        }
        return mNewCharacterDialog;
    }

    private AlertDialog getCharacterDeleteAlertDialog() {
        if (mDeleteCharacterDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.characters_delete_alert_dialog_message)
                    .setCancelable(true)
                    .setPositiveButton(R.string.global_yes,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    try {
                                        getHelper().getCharacterDao().delete(
                                                mSelectedCharacter);
                                        fillData();
                                    } catch (SQLException e) {
                                        ErrorHandler.log(
                                                CharactersActivity.this
                                                        .getClass(),
                                                "Failed to delete the character",
                                                e,
                                                getString(R.string.error_character_delete),
                                                CharactersActivity.this);
                                    }
                                }
                            })
                    .setNegativeButton(R.string.global_no,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });
            mDeleteCharacterDialog = builder.create();
        }
        return mDeleteCharacterDialog;
    }

    private AlertDialog getPurchaseDialog() {
        if (mPurchaseDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.purchase_dialog_message)
                    .setCancelable(false)
                    .setPositiveButton(R.string.global_yes,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    if (mMarketIntent == null) {
                                        mMarketIntent = new Intent(
                                                Intent.ACTION_VIEW);
                                        mMarketIntent.setData(Uri
                                                .parse("market://details?id=com.thomasgallinari.dndcharactersheet"));
                                    }
                                    startActivity(mMarketIntent);
                                    finish();
                                }
                            })
                    .setNegativeButton(R.string.global_no,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                    finish();
                                }
                            });
            mPurchaseDialog = builder.create();
        }
        return mPurchaseDialog;
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
}
