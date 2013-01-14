/**
 * Copyright (C) 2011 Thomas Gallinari
 */
package com.thomasgallinari.dndcharactersheet.view;

import java.sql.SQLException;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.thomasgallinari.dndcharactersheet.R;
import com.thomasgallinari.dndcharactersheet.db.DatabaseHelper;
import com.thomasgallinari.dndcharactersheet.model.Character;
import com.thomasgallinari.dndcharactersheet.view.util.ErrorHandler;

/**
 * An activity that shows character scores.
 */
public class ScoresActivity extends OrmLiteBaseActivity<DatabaseHelper>
	implements OnClickListener, OnDismissListener, OnCancelListener {

    class ScoreTextWatcher implements TextWatcher {

	private EditText mEditText;

	public ScoreTextWatcher(EditText editText) {
	    mEditText = editText;
	}

	@Override
	public void afterTextChanged(Editable s) {
	    validateData(mEditText);
	    bindField(mEditText);
	    fillCalculatedData();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
		int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before,
		int count) {
	}
    }

    public static final String CHARACTER = "com.thomasgallinari.dndcharactersheet.CHARACTER";

    private static final int DIALOG_SHOW_AC_DETAIL = 1;
    private static final int DIALOG_SHOW_INIT_DETAIL = 2;
    private static final int DIALOG_SHOW_MOV_DETAIL = 3;
    private static final int DIALOG_SHOW_SCORE_DETAIL = 4;

    private Button mAcButton;
    private AcDetailDialog mAcDialog;
    private EditText mChaEditText;
    private Character mCharacter;
    private TextView mChaTextView;
    private int mClickedButton;
    private EditText mConEditText;
    private TextView mConTextView;
    private EditText mDexEditText;
    private TextView mDexTextView;
    private Button mFortButton;
    private Button mInitButton;
    private InitDetailDialog mInitDialog;
    private EditText mIntEditText;
    private TextView mIntTextView;
    private Button mMovButton;
    private MovDetailDialog mMovDialog;
    private Button mRefButton;
    private ScoreDetailDialog mScoreDialog;
    private EditText mStrEditText;
    private TextView mStrTextView;
    private Button mWillButton;
    private EditText mWisEditText;
    private TextView mWisTextView;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCancel(DialogInterface dialog) {
	try {
	    getHelper().getCharacterDao().refresh(mCharacter);
	} catch (SQLException e) {
	    ErrorHandler.log(getClass(), "Failed to refresh the character", e,
		    getString(R.string.error_character_refresh), this);
	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
	mClickedButton = v.getId();
	switch (mClickedButton) {
	case R.id.scores_ac_button:
	    showDialog(DIALOG_SHOW_AC_DETAIL);
	    break;
	case R.id.scores_init_button:
	    showDialog(DIALOG_SHOW_INIT_DETAIL);
	    break;
	case R.id.scores_mov_button:
	    showDialog(DIALOG_SHOW_MOV_DETAIL);
	    break;
	case R.id.scores_fort_button:
	case R.id.scores_ref_button:
	case R.id.scores_will_button:
	    showDialog(DIALOG_SHOW_SCORE_DETAIL);
	    break;
	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDismiss(DialogInterface dialog) {
	try {
	    getHelper().getCharacterDao().update(mCharacter);
	} catch (SQLException e) {
	    ErrorHandler.log(getClass(), "Failed to update the character", e,
		    getString(R.string.error_character_update), this);
	}

	switch (mClickedButton) {
	case R.id.scores_ac_button:
	    fillAc();
	    break;
	case R.id.scores_fort_button:
	    fillFort();
	    break;
	case R.id.scores_init_button:
	    fillInit();
	    break;
	case R.id.scores_mov_button:
	    fillMov();
	    break;
	case R.id.scores_ref_button:
	    fillRef();
	    break;
	case R.id.scores_will_button:
	    fillWill();
	    break;
	}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	mCharacter = (Character) getIntent().getSerializableExtra(CHARACTER);

	setContentView(R.layout.scores);

	mAcButton = (Button) findViewById(R.id.scores_ac_button);
	mFortButton = (Button) findViewById(R.id.scores_fort_button);
	mRefButton = (Button) findViewById(R.id.scores_ref_button);
	mWillButton = (Button) findViewById(R.id.scores_will_button);
	mInitButton = (Button) findViewById(R.id.scores_init_button);
	mMovButton = (Button) findViewById(R.id.scores_mov_button);
	mChaEditText = (EditText) findViewById(R.id.scores_cha_edit_text);
	mConEditText = (EditText) findViewById(R.id.scores_con_edit_text);
	mDexEditText = (EditText) findViewById(R.id.scores_dex_edit_text);
	mIntEditText = (EditText) findViewById(R.id.scores_int_edit_text);
	mStrEditText = (EditText) findViewById(R.id.scores_str_edit_text);
	mWisEditText = (EditText) findViewById(R.id.scores_wis_edit_text);
	mChaTextView = (TextView) findViewById(R.id.scores_cha_text_view);
	mConTextView = (TextView) findViewById(R.id.scores_con_text_view);
	mDexTextView = (TextView) findViewById(R.id.scores_dex_text_view);
	mIntTextView = (TextView) findViewById(R.id.scores_int_text_view);
	mStrTextView = (TextView) findViewById(R.id.scores_str_text_view);
	mWisTextView = (TextView) findViewById(R.id.scores_wis_text_view);

	mChaEditText.addTextChangedListener(new ScoreTextWatcher(mChaEditText));
	mConEditText.addTextChangedListener(new ScoreTextWatcher(mConEditText));
	mDexEditText.addTextChangedListener(new ScoreTextWatcher(mDexEditText));
	mIntEditText.addTextChangedListener(new ScoreTextWatcher(mIntEditText));
	mStrEditText.addTextChangedListener(new ScoreTextWatcher(mStrEditText));
	mWisEditText.addTextChangedListener(new ScoreTextWatcher(mWisEditText));
    }

    @Override
    protected Dialog onCreateDialog(int id) {
	Dialog dialog = null;
	switch (id) {
	case DIALOG_SHOW_AC_DETAIL:
	    if (mAcDialog == null) {
		mAcDialog = new AcDetailDialog(this, mCharacter);
	    }
	    dialog = mAcDialog;
	    break;
	case DIALOG_SHOW_INIT_DETAIL:
	    if (mInitDialog == null) {
		mInitDialog = new InitDetailDialog(this, mCharacter);
	    }
	    dialog = mInitDialog;
	    break;
	case DIALOG_SHOW_MOV_DETAIL:
	    if (mMovDialog == null) {
		mMovDialog = new MovDetailDialog(this, mCharacter);
	    }
	    dialog = mMovDialog;
	    break;
	case DIALOG_SHOW_SCORE_DETAIL:
	    if (mScoreDialog == null) {
		mScoreDialog = new ScoreDetailDialog(this, mCharacter);
	    }
	    dialog = mScoreDialog;
	    break;
	}
	if (dialog != null) {
	    dialog.setOwnerActivity(this);
	    dialog.setOnDismissListener(this);
	    dialog.setOnCancelListener(this);
	}
	return dialog;
    }

    @Override
    protected void onPause() {
	super.onPause();

	validateData(mChaEditText);
	validateData(mConEditText);
	validateData(mDexEditText);
	validateData(mIntEditText);
	validateData(mStrEditText);
	validateData(mWisEditText);

	mCharacter.setCha(Integer.parseInt(mChaEditText.getText().toString()));
	mCharacter.setCon(Integer.parseInt(mConEditText.getText().toString()));
	mCharacter.setDex(Integer.parseInt(mDexEditText.getText().toString()));
	mCharacter
		.setIntel(Integer.parseInt(mIntEditText.getText().toString()));
	mCharacter.setStr(Integer.parseInt(mStrEditText.getText().toString()));
	mCharacter.setWis(Integer.parseInt(mWisEditText.getText().toString()));

	try {
	    getHelper().getCharacterDao().update(mCharacter);
	} catch (SQLException e) {
	    ErrorHandler.log(getClass(), "Failed to update the character", e,
		    getString(R.string.error_character_update), this);
	}
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
	super.onPrepareDialog(id, dialog, args);

	dialog.setTitle(getDialogScoreName());

	if (mClickedButton == R.id.scores_init_button) {
	    ((InitDetailDialog) dialog).setAbilMod(mCharacter.getDexMod());
	}
	if (mClickedButton == R.id.scores_fort_button) {
	    ((ScoreDetailDialog) dialog).setAbilMod(Character.FORT, Math.max(
		    mCharacter.getStrMod(), mCharacter.getConMod()));
	}
	if (mClickedButton == R.id.scores_ref_button) {
	    ((ScoreDetailDialog) dialog).setAbilMod(Character.REF, Math.max(
		    mCharacter.getDexMod(), mCharacter.getIntelMod()));
	}
	if (mClickedButton == R.id.scores_will_button) {
	    ((ScoreDetailDialog) dialog).setAbilMod(Character.WILL, Math.max(
		    mCharacter.getWisMod(), mCharacter.getChaMod()));
	}
    }

    @Override
    protected void onResume() {
	super.onResume();
	fillData();
    }

    private void bindField(EditText editText) {
	switch (editText.getId()) {
	case R.id.scores_cha_edit_text:
	    mCharacter.setCha(Integer.parseInt(editText.getText().toString()));
	    break;
	case R.id.scores_con_edit_text:
	    mCharacter.setCon(Integer.parseInt(editText.getText().toString()));
	    break;
	case R.id.scores_dex_edit_text:
	    mCharacter.setDex(Integer.parseInt(editText.getText().toString()));
	    break;
	case R.id.scores_int_edit_text:
	    mCharacter
		    .setIntel(Integer.parseInt(editText.getText().toString()));
	    break;
	case R.id.scores_str_edit_text:
	    mCharacter.setStr(Integer.parseInt(editText.getText().toString()));
	    break;
	case R.id.scores_wis_edit_text:
	    mCharacter.setWis(Integer.parseInt(editText.getText().toString()));
	    break;
	}
    }

    private void fillAc() {
	mAcButton.setText(String.valueOf(mCharacter.getAc()));
    }

    private void fillCalculatedData() {
	mChaTextView.setText(String.valueOf(mCharacter.getChaMod()));
	mConTextView.setText(String.valueOf(mCharacter.getConMod()));
	mDexTextView.setText(String.valueOf(mCharacter.getDexMod()));
	mIntTextView.setText(String.valueOf(mCharacter.getIntelMod()));
	mStrTextView.setText(String.valueOf(mCharacter.getStrMod()));
	mWisTextView.setText(String.valueOf(mCharacter.getWisMod()));

	fillAc();
	fillInit();
	fillMov();
	fillFort();
	fillRef();
	fillWill();
    }

    private void fillData() {
	mChaEditText.setText(String.valueOf(mCharacter.getCha()));
	mConEditText.setText(String.valueOf(mCharacter.getCon()));
	mDexEditText.setText(String.valueOf(mCharacter.getDex()));
	mIntEditText.setText(String.valueOf(mCharacter.getIntel()));
	mStrEditText.setText(String.valueOf(mCharacter.getStr()));
	mWisEditText.setText(String.valueOf(mCharacter.getWis()));

	fillCalculatedData();
    }

    private void fillFort() {
	mFortButton.setText(String.valueOf(mCharacter.getFort()));
    }

    private void fillInit() {
	mInitButton.setText(String.valueOf(mCharacter.getInit()));
    }

    private void fillMov() {
	mMovButton.setText(String.valueOf(mCharacter.getMov()));
    }

    private void fillRef() {
	mRefButton.setText(String.valueOf(mCharacter.getRef()));
    }

    private void fillWill() {
	mWillButton.setText(String.valueOf(mCharacter.getWill()));
    }

    private String getDialogScoreName() {
	String score = "";
	switch (mClickedButton) {
	case R.id.scores_ac_button:
	    score = getString(R.string.scores_defense_ac);
	    break;
	case R.id.scores_fort_button:
	    score = getString(R.string.scores_defense_fort);
	    break;
	case R.id.scores_ref_button:
	    score = getString(R.string.scores_defense_ref);
	    break;
	case R.id.scores_will_button:
	    score = getString(R.string.scores_defense_will);
	    break;
	case R.id.scores_init_button:
	    score = getString(R.string.scores_initiative);
	    break;
	case R.id.scores_mov_button:
	    score = getString(R.string.scores_movement);
	    break;
	}
	return score;
    }

    private void validateData(EditText editText) {
	if (editText.getText().length() == 0
		|| Integer.parseInt(editText.getText().toString()) < 0) {
	    editText.setText("0");
	}
    }
}
