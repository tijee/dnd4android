/**
 * Copyright (C) 2011 Thomas Gallinari
 */
package com.thomasgallinari.dnd4android.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.thomasgallinari.dnd4android.model.Attack;
import com.thomasgallinari.dnd4android.model.Character;
import com.thomasgallinari.dnd4android.model.Equipment;
import com.thomasgallinari.dnd4android.model.Feat;
import com.thomasgallinari.dnd4android.model.Power;
import com.thomasgallinari.dnd4android.model.Ritual;
import com.thomasgallinari.dnd4android.model.Skill;
import com.thomasgallinari.dnd4android.view.util.ErrorHandler;

/**
 * Database helper class used to manage the creation and upgrading of your
 * database. This class also usually provides the DAOs used by the other
 * classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "dndcharactersheet.db";
    private static final int DATABASE_VERSION = 2;

    private Dao<Attack, Integer> mAttackDao;
    private Dao<Character, Integer> mCharacterDao;
    private Dao<Equipment, Integer> mEquipmentDao;
    private Dao<Feat, Integer> mFeatDao;
    private Dao<Power, Integer> mPowerDao;
    private Dao<Ritual, Integer> mRitualDao;
    private Dao<Skill, Integer> mSkillDao;

    /**
     * Creates a new {@link DatabaseHelper}.
     * 
     * @param context
     *            {@inheritDoc}
     */
    public DatabaseHelper(Context context) {
	super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Closes the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
	super.close();
	mAttackDao = null;
	mCharacterDao = null;
	mEquipmentDao = null;
	mFeatDao = null;
	mPowerDao = null;
	mRitualDao = null;
	mSkillDao = null;
    }

    /**
     * Returns the Database Access Object (DAO) for the {@link Attack} class. It
     * will create it or just give the cached value.
     * 
     * @return a DAO for the {@link Attack} class
     */
    public Dao<Attack, Integer> getAttackDao() throws SQLException {
	if (mAttackDao == null) {
	    mAttackDao = BaseDaoImpl.createDao(getConnectionSource(),
		    Attack.class);
	}
	return mAttackDao;
    }

    /**
     * Returns the Database Access Object (DAO) for the {@link Character} class.
     * It will create it or just give the cached value.
     * 
     * @return a DAO for the {@link Character} class
     */
    public Dao<Character, Integer> getCharacterDao() throws SQLException {
	if (mCharacterDao == null) {
	    mCharacterDao = BaseDaoImpl.createDao(getConnectionSource(),
		    Character.class);
	}
	return mCharacterDao;
    }

    /**
     * Returns the Database Access Object (DAO) for the {@link Equipment} class.
     * It will create it or just give the cached value.
     * 
     * @return a DAO for the {@link Equipment} class
     */
    public Dao<Equipment, Integer> getEquipmentDao() throws SQLException {
	if (mEquipmentDao == null) {
	    mEquipmentDao = BaseDaoImpl.createDao(getConnectionSource(),
		    Equipment.class);
	}
	return mEquipmentDao;
    }

    /**
     * Returns the Database Access Object (DAO) for the {@link Feat} class. It
     * will create it or just give the cached value.
     * 
     * @return a DAO for the {@link Feat} class
     */
    public Dao<Feat, Integer> getFeatDao() throws SQLException {
	if (mFeatDao == null) {
	    mFeatDao = BaseDaoImpl.createDao(getConnectionSource(), Feat.class);
	}
	return mFeatDao;
    }

    /**
     * Returns the Database Access Object (DAO) for the {@link Power} class. It
     * will create it or just give the cached value.
     * 
     * @return a DAO for the {@link Power} class
     */
    public Dao<Power, Integer> getPowerDao() throws SQLException {
	if (mPowerDao == null) {
	    mPowerDao = BaseDaoImpl.createDao(getConnectionSource(),
		    Power.class);
	}
	return mPowerDao;
    }

    /**
     * Returns the Database Access Object (DAO) for the {@link Ritual} class. It
     * will create it or just give the cached value.
     * 
     * @return a DAO for the {@link Ritual} class
     */
    public Dao<Ritual, Integer> getRitualDao() throws SQLException {
	if (mRitualDao == null) {
	    mRitualDao = BaseDaoImpl.createDao(getConnectionSource(),
		    Ritual.class);
	}
	return mRitualDao;
    }

    /**
     * Returns the Database Access Object (DAO) for the {@link Skill} class. It
     * will create it or just give the cached value.
     * 
     * @return a DAO for the {@link Skill} class
     */
    public Dao<Skill, Integer> getSkillDao() throws SQLException {
	if (mSkillDao == null) {
	    mSkillDao = BaseDaoImpl.createDao(getConnectionSource(),
		    Skill.class);
	}
	return mSkillDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
	try {
	    // Log.i(getClass().getSimpleName(), "Creating database");
	    TableUtils.createTable(connectionSource, Attack.class);
	    TableUtils.createTable(connectionSource, Character.class);
	    TableUtils.createTable(connectionSource, Equipment.class);
	    TableUtils.createTable(connectionSource, Feat.class);
	    TableUtils.createTable(connectionSource, Power.class);
	    TableUtils.createTable(connectionSource, Ritual.class);
	    TableUtils.createTable(connectionSource, Skill.class);
	} catch (SQLException e) {
	    ErrorHandler.log(getClass(), "Failed to create the database", e);
	    throw new RuntimeException(e);
	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
	    int oldVersion, int newVersion) {
	if (oldVersion < 2 && newVersion >= 2) {
	    try {
		TableUtils.createTable(connectionSource, Attack.class);
	    } catch (SQLException e) {
		ErrorHandler
			.log(getClass(), "Failed to update the database", e);
		throw new RuntimeException(e);
	    }
	}
    }
}
