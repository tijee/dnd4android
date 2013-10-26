/**
 * Copyright (C) 2011 Thomas Gallinari
 */
package com.thomasgallinari.dnd4android.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * A character.
 */
@DatabaseTable(tableName = "character")
public class Character implements Serializable {

    /**
     * The fort key.
     */
    public static final int FORT = 1;

    /**
     * The ref key.
     */
    public static final int REF = 2;

    /**
     * The will key.
     */
    public static final int WILL = 3;

    private static final long serialVersionUID = 5180681000456053646L;

    @DatabaseField(columnName = "ac_armor", defaultValue = "0")
    private int mAcArmor;
    @DatabaseField(columnName = "ac_class", defaultValue = "0")
    private int mAcClass;
    @DatabaseField(columnName = "ac_enh", defaultValue = "0")
    private int mAcEnh;
    @DatabaseField(columnName = "ac_feat", defaultValue = "0")
    private int mAcFeat;
    @DatabaseField(columnName = "ac_misc", defaultValue = "0")
    private int mAcMisc;
    @DatabaseField(columnName = "action_points_current", defaultValue = "0")
    private int mActionPointsCurrent;
    @DatabaseField(columnName = "action_points_max", defaultValue = "0")
    private int mActionPointsMax;
    @DatabaseField(columnName = "astral_diamonds", defaultValue = "0")
    private int mAstralDiamonds;
    @DatabaseField(columnName = "cha", defaultValue = "0")
    private int mCha;
    @DatabaseField(columnName = "class")
    private String mClass;
    @DatabaseField(columnName = "con", defaultValue = "0")
    private int mCon;
    @DatabaseField(columnName = "copper_pieces", defaultValue = "0")
    private int mCopperPieces;
    @DatabaseField(columnName = "death_saving_failures", defaultValue = "0")
    private int mDeathSavingFailures;
    @DatabaseField(columnName = "dex", defaultValue = "0")
    private int mDex;
    @DatabaseField(columnName = "epic_destiny")
    private String mEpicDestiny;
    @DatabaseField(columnName = "fort_class", defaultValue = "0")
    private int mFortClass;
    @DatabaseField(columnName = "fort_enh", defaultValue = "0")
    private int mFortEnh;
    @DatabaseField(columnName = "fort_feat", defaultValue = "0")
    private int mFortFeat;
    @DatabaseField(columnName = "fort_misc", defaultValue = "0")
    private int mFortMisc;
    @DatabaseField(columnName = "gold_pieces", defaultValue = "0")
    private int mGoldPieces;
    @DatabaseField(columnName = "health_notes", width = 4095)
    private String mHealthNotes;
    @DatabaseField(columnName = "hp_base", defaultValue = "0")
    private int mHpBase;
    @DatabaseField(columnName = "hp_class", defaultValue = "0")
    private int mHpClass;
    @DatabaseField(columnName = "hp_current", defaultValue = "0")
    private int mHpCurrent;
    @DatabaseField(columnName = "hp_feat", defaultValue = "0")
    private int mHpFeat;
    @DatabaseField(columnName = "hp_misc", defaultValue = "0")
    private int mHpMisc;
    @DatabaseField(columnName = "hp_per_level", defaultValue = "0")
    private int mHpPerLevel;
    @DatabaseField(columnName = "hp_temp", defaultValue = "0")
    private int mHpTemp;
    @DatabaseField(columnName = "id", generatedId = true)
    private int mId;
    @DatabaseField(columnName = "init_misc", defaultValue = "0")
    private int mInitMisc;
    @DatabaseField(columnName = "int", defaultValue = "0")
    private int mInt;
    @DatabaseField(columnName = "level", defaultValue = "0")
    private int mLevel;
    @DatabaseField(columnName = "misc", width = 4095)
    private String mMisc;
    @DatabaseField(columnName = "mov_armor", defaultValue = "0")
    private int mMovArmor;
    @DatabaseField(columnName = "mov_base", defaultValue = "0")
    private int mMovBase;
    @DatabaseField(columnName = "mov_item", defaultValue = "0")
    private int mMovItem;
    @DatabaseField(columnName = "mov_misc", defaultValue = "0")
    private int mMovMisc;
    @DatabaseField(columnName = "name", canBeNull = false)
    private String mName;
    @DatabaseField(columnName = "notes", width = 4095)
    private String mNotes;
    @DatabaseField(columnName = "paragon_path")
    private String mParagonPath;
    @DatabaseField(columnName = "platinum_pieces", defaultValue = "0")
    private int mPlatinumPieces;
    @DatabaseField(columnName = "race")
    private String mRace;
    @DatabaseField(columnName = "ref_abil", defaultValue = "0")
    private int mRefAbil;
    @DatabaseField(columnName = "ref_class", defaultValue = "0")
    private int mRefClass;
    @DatabaseField(columnName = "ref_enh", defaultValue = "0")
    private int mRefEnh;
    @DatabaseField(columnName = "ref_feat", defaultValue = "0")
    private int mRefFeat;
    @DatabaseField(columnName = "ref_misc", defaultValue = "0")
    private int mRefMisc;
    @DatabaseField(columnName = "second_wind", defaultValue = "false")
    private boolean mSecondWind;
    @DatabaseField(columnName = "silver_pieces", defaultValue = "0")
    private int mSilverPieces;
    @DatabaseField(columnName = "str", defaultValue = "0")
    private int mStr;
    @DatabaseField(columnName = "surge_class", defaultValue = "0")
    private int mSurgeClass;
    @DatabaseField(columnName = "surge_feat", defaultValue = "0")
    private int mSurgeFeat;
    @DatabaseField(columnName = "surge_misc", defaultValue = "0")
    private int mSurgeMisc;
    @DatabaseField(columnName = "surges_current", defaultValue = "0")
    private int mSurgesCurrent;
    @DatabaseField(columnName = "surges_per_day_class", defaultValue = "0")
    private int mSurgesPerDayClass;
    @DatabaseField(columnName = "surges_per_day_feat", defaultValue = "0")
    private int mSurgesPerDayFeat;
    @DatabaseField(columnName = "surges_per_day_misc", defaultValue = "0")
    private int mSurgesPerDayMisc;
    @DatabaseField(columnName = "will_abil", defaultValue = "0")
    private int mWillAbil;
    @DatabaseField(columnName = "will_class", defaultValue = "0")
    private int mWillClass;
    @DatabaseField(columnName = "will_enh", defaultValue = "0")
    private int mWillEnh;
    @DatabaseField(columnName = "will_feat", defaultValue = "0")
    private int mWillFeat;
    @DatabaseField(columnName = "will_misc", defaultValue = "0")
    private int mWillMisc;
    @DatabaseField(columnName = "wis", defaultValue = "0")
    private int mWis;
    @DatabaseField(columnName = "xp", defaultValue = "0")
    private int mXp;

    /**
     * Creates a new {@link Character}.
     */
    public Character() {

    }

    /**
     * @return the ac
     */
    public int getAc() {
	return 10 + mLevel / 2 + mAcArmor + mAcClass + mAcEnh + mAcFeat
		+ mAcMisc;
    }

    /**
     * @return the acArmor
     */
    public int getAcArmor() {
	return mAcArmor;
    }

    /**
     * @return the acClass
     */
    public int getAcClass() {
	return mAcClass;
    }

    /**
     * @return the acEnh
     */
    public int getAcEnh() {
	return mAcEnh;
    }

    /**
     * @return the acFeat
     */
    public int getAcFeat() {
	return mAcFeat;
    }

    /**
     * @return the acMisc
     */
    public int getAcMisc() {
	return mAcMisc;
    }

    /**
     * @return the actionPointsCurrent
     */
    public int getActionPointsCurrent() {
	return mActionPointsCurrent;
    }

    /**
     * @return the actionPointsMax
     */
    public int getActionPointsMax() {
	return mActionPointsMax;
    }

    /**
     * @return the astralDiamonds
     */
    public int getAstralDiamonds() {
	return mAstralDiamonds;
    }

    /**
     * @return the cha
     */
    public int getCha() {
	return mCha;
    }

    /**
     * @return the cha mod
     */
    public int getChaMod() {
	return getMod(mCha);
    }

    /**
     * @return the clazz
     */
    public String getClazz() {
	return mClass;
    }

    /**
     * @return the con
     */
    public int getCon() {
	return mCon;
    }

    /**
     * @return the con mod
     */
    public int getConMod() {
	return getMod(mCon);
    }

    /**
     * @return the copperPieces
     */
    public int getCopperPieces() {
	return mCopperPieces;
    }

    /**
     * @return the deathSavingFailures
     */
    public int getDeathSavingFailures() {
	return mDeathSavingFailures;
    }

    /**
     * @return the dex
     */
    public int getDex() {
	return mDex;
    }

    /**
     * @return the dex mod
     */
    public int getDexMod() {
	return getMod(mDex);
    }

    /**
     * @return the epicDestiny
     */
    public String getEpicDestiny() {
	return mEpicDestiny;
    }

    /**
     * @return the fort
     */
    public int getFort() {
	return 10 + mLevel / 2 + Math.max(getStrMod(), getConMod())
		+ mFortClass + mFortEnh + mFortFeat + mFortMisc;
    }

    /**
     * @return the fortClass
     */
    public int getFortClass() {
	return mFortClass;
    }

    /**
     * @return the fortEnh
     */
    public int getFortEnh() {
	return mFortEnh;
    }

    /**
     * @return the fortFeat
     */
    public int getFortFeat() {
	return mFortFeat;
    }

    /**
     * @return the fortMisc
     */
    public int getFortMisc() {
	return mFortMisc;
    }

    /**
     * @return the goldPieces
     */
    public int getGoldPieces() {
	return mGoldPieces;
    }

    /**
     * @return the healthNotes
     */
    public String getHealthNotes() {
	return mHealthNotes;
    }

    /**
     * @return the hpBase
     */
    public int getHpBase() {
	return mHpBase;
    }

    /**
     * @return the hpClass
     */
    public int getHpClass() {
	return mHpClass;
    }

    /**
     * @return the hpCurrent
     */
    public int getHpCurrent() {
	return mHpCurrent;
    }

    /**
     * @return the hpFeat
     */
    public int getHpFeat() {
	return mHpFeat;
    }

    /**
     * @return the max hp
     */
    public int getHpMax() {
	return mHpBase + mCon + (mLevel - 1) * mHpPerLevel + mHpClass + mHpFeat
		+ mHpMisc;
    }

    /**
     * @return the hpMisc
     */
    public int getHpMisc() {
	return mHpMisc;
    }

    /**
     * @return the hpPerLevel
     */
    public int getHpPerLevel() {
	return mHpPerLevel;
    }

    /**
     * @return the hpTemp
     */
    public int getHpTemp() {
	return mHpTemp;
    }

    /**
     * @return the id
     */
    public int getId() {
	return mId;
    }

    /**
     * @return the init
     */
    public int getInit() {
	return mLevel / 2 + getDexMod() + mInitMisc;
    }

    /**
     * @return the initMisc
     */
    public int getInitMisc() {
	return mInitMisc;
    }

    /**
     * @return the intel
     */
    public int getIntel() {
	return mInt;
    }

    /**
     * @return the int mod
     */
    public int getIntelMod() {
	return getMod(mInt);
    }

    /**
     * @return the level
     */
    public int getLevel() {
	return mLevel;
    }

    /**
     * @return the misc
     */
    public String getMisc() {
	return mMisc;
    }

    /**
     * @return the mov
     */
    public int getMov() {
	return mMovArmor + mMovBase + mMovItem + mMovMisc;
    }

    /**
     * @return the movArmor
     */
    public int getMovArmor() {
	return mMovArmor;
    }

    /**
     * @return the movBase
     */
    public int getMovBase() {
	return mMovBase;
    }

    /**
     * @return the movItem
     */
    public int getMovItem() {
	return mMovItem;
    }

    /**
     * @return the movMisc
     */
    public int getMovMisc() {
	return mMovMisc;
    }

    /**
     * @return the name
     */
    public String getName() {
	return mName;
    }

    /**
     * @return the notes
     */
    public String getNotes() {
	return mNotes;
    }

    /**
     * @return the paragonPath
     */
    public String getParagonPath() {
	return mParagonPath;
    }

    /**
     * @return the platinumPieces
     */
    public int getPlatinumPieces() {
	return mPlatinumPieces;
    }

    /**
     * @return the race
     */
    public String getRace() {
	return mRace;
    }

    /**
     * @return the ref
     */
    public int getRef() {
	return 10 + mLevel / 2 + Math.max(getDexMod(), getIntelMod())
		+ mRefClass + mRefEnh + mRefFeat + mRefMisc;
    }

    /**
     * @return the refAbil
     */
    public int getRefAbil() {
	return mRefAbil;
    }

    /**
     * @return the refClass
     */
    public int getRefClass() {
	return mRefClass;
    }

    /**
     * @return the refEnh
     */
    public int getRefEnh() {
	return mRefEnh;
    }

    /**
     * @return the refFeat
     */
    public int getRefFeat() {
	return mRefFeat;
    }

    /**
     * @return the refMisc
     */
    public int getRefMisc() {
	return mRefMisc;
    }

    /**
     * @return the silverPieces
     */
    public int getSilverPieces() {
	return mSilverPieces;
    }

    /**
     * @return the str
     */
    public int getStr() {
	return mStr;
    }

    /**
     * @return the str mod
     */
    public int getStrMod() {
	return getMod(mStr);
    }

    /**
     * @return the surgeClass
     */
    public int getSurgeClass() {
	return mSurgeClass;
    }

    /**
     * @return the surgeFeat
     */
    public int getSurgeFeat() {
	return mSurgeFeat;
    }

    /**
     * @return the surgeMisc
     */
    public int getSurgeMisc() {
	return mSurgeMisc;
    }

    /**
     * @return the surgesCurrent
     */
    public int getSurgesCurrent() {
	return mSurgesCurrent;
    }

    /**
     * @return the surges per day
     */
    public int getSurgesPerDay() {
	return getMod(mCon) + mSurgesPerDayClass + mSurgesPerDayFeat
		+ mSurgesPerDayMisc;
    }

    /**
     * @return the surgesPerDayClass
     */
    public int getSurgesPerDayClass() {
	return mSurgesPerDayClass;
    }

    /**
     * @return the surgesPerDayFeat
     */
    public int getSurgesPerDayFeat() {
	return mSurgesPerDayFeat;
    }

    /**
     * @return the surgesPerDayMisc
     */
    public int getSurgesPerDayMisc() {
	return mSurgesPerDayMisc;
    }

    /**
     * @return the surge value
     */
    public int getSurgeValue() {
	return getHpMax() / 4 + mSurgeClass + mSurgeFeat + mSurgeMisc;
    }

    /**
     * @return the will
     */
    public int getWill() {
	return 10 + mLevel / 2 + Math.max(getWisMod(), getChaMod())
		+ mWillClass + mWillEnh + mWillFeat + mWillMisc;
    }

    /**
     * @return the willAbil
     */
    public int getWillAbil() {
	return mWillAbil;
    }

    /**
     * @return the willClass
     */
    public int getWillClass() {
	return mWillClass;
    }

    /**
     * @return the willEnh
     */
    public int getWillEnh() {
	return mWillEnh;
    }

    /**
     * @return the willFeat
     */
    public int getWillFeat() {
	return mWillFeat;
    }

    /**
     * @return the willMisc
     */
    public int getWillMisc() {
	return mWillMisc;
    }

    /**
     * @return the wis
     */
    public int getWis() {
	return mWis;
    }

    /**
     * @return the wis mod
     */
    public int getWisMod() {
	return getMod(mWis);
    }

    /**
     * @return the xp
     */
    public int getXp() {
	return mXp;
    }

    /**
     * @return true if the character is bloodied
     */
    public boolean isBloodied() {
	return mHpCurrent <= getHpMax() / 2;
    }

    /**
     * @return true if the character is dead
     */
    public boolean isDead() {
	return mHpCurrent <= -(getHpMax() / 2);
    }

    /**
     * @return true if the character is dying
     */
    public boolean isDying() {
	return mHpCurrent <= 0;
    }

    /**
     * @return the secondWind
     */
    public boolean isSecondWind() {
	return mSecondWind;
    }

    /**
     * @param acArmor
     *            the acArmor to set
     */
    public void setAcArmor(int acArmor) {
	mAcArmor = acArmor;
    }

    /**
     * @param acClass
     *            the acClass to set
     */
    public void setAcClass(int acClass) {
	mAcClass = acClass;
    }

    /**
     * @param acEnh
     *            the acEnh to set
     */
    public void setAcEnh(int acEnh) {
	mAcEnh = acEnh;
    }

    /**
     * @param acFeat
     *            the acFeat to set
     */
    public void setAcFeat(int acFeat) {
	mAcFeat = acFeat;
    }

    /**
     * @param acMisc
     *            the acMisc to set
     */
    public void setAcMisc(int acMisc) {
	mAcMisc = acMisc;
    }

    /**
     * @param actionPointsCurrent
     *            the actionPointsCurrent to set
     */
    public void setActionPointsCurrent(int actionPointsCurrent) {
	mActionPointsCurrent = actionPointsCurrent;
    }

    /**
     * @param actionPointsMax
     *            the actionPointsMax to set
     */
    public void setActionPointsMax(int actionPointsMax) {
	mActionPointsMax = actionPointsMax;
    }

    /**
     * @param astralDiamonds
     *            the astralDiamonds to set
     */
    public void setAstralDiamonds(int astralDiamonds) {
	mAstralDiamonds = astralDiamonds;
    }

    /**
     * @param cha
     *            the cha to set
     */
    public void setCha(int cha) {
	mCha = cha;
    }

    /**
     * @param clazz
     *            the clazz to set
     */
    public void setClazz(String clazz) {
	mClass = clazz;
    }

    /**
     * @param con
     *            the con to set
     */
    public void setCon(int con) {
	mCon = con;
    }

    /**
     * @param copperPieces
     *            the copperPieces to set
     */
    public void setCopperPieces(int copperPieces) {
	mCopperPieces = copperPieces;
    }

    /**
     * @param deathSavingFailures
     *            the deathSavingFailures to set
     */
    public void setDeathSavingFailures(int deathSavingFailures) {
	mDeathSavingFailures = deathSavingFailures;
    }

    /**
     * @param dex
     *            the dex to set
     */
    public void setDex(int dex) {
	mDex = dex;
    }

    /**
     * @param epicDestiny
     *            the epicDestiny to set
     */
    public void setEpicDestiny(String epicDestiny) {
	mEpicDestiny = epicDestiny;
    }

    /**
     * @param fortClass
     *            the fortClass to set
     */
    public void setFortClass(int fortClass) {
	mFortClass = fortClass;
    }

    /**
     * @param fortEnh
     *            the fortEnh to set
     */
    public void setFortEnh(int fortEnh) {
	mFortEnh = fortEnh;
    }

    /**
     * @param fortFeat
     *            the fortFeat to set
     */
    public void setFortFeat(int fortFeat) {
	mFortFeat = fortFeat;
    }

    /**
     * @param fortMisc
     *            the fortMisc to set
     */
    public void setFortMisc(int fortMisc) {
	mFortMisc = fortMisc;
    }

    /**
     * @param goldPieces
     *            the goldPieces to set
     */
    public void setGoldPieces(int goldPieces) {
	mGoldPieces = goldPieces;
    }

    /**
     * @param healthNotes
     *            the healthNotes to set
     */
    public void setHealthNotes(String healthNotes) {
	mHealthNotes = healthNotes;
    }

    /**
     * @param hpBase
     *            the hpBase to set
     */
    public void setHpBase(int hpBase) {
	mHpBase = hpBase;
    }

    /**
     * @param hpClass
     *            the hpClass to set
     */
    public void setHpClass(int hpClass) {
	mHpClass = hpClass;
    }

    /**
     * @param hpCurrent
     *            the hpCurrent to set
     */
    public void setHpCurrent(int hpCurrent) {
	mHpCurrent = hpCurrent;
    }

    /**
     * @param hpFeat
     *            the hpFeat to set
     */
    public void setHpFeat(int hpFeat) {
	mHpFeat = hpFeat;
    }

    /**
     * @param hpMisc
     *            the hpMisc to set
     */
    public void setHpMisc(int hpMisc) {
	mHpMisc = hpMisc;
    }

    /**
     * @param hpPerLevel
     *            the hpPerLevel to set
     */
    public void setHpPerLevel(int hpPerLevel) {
	mHpPerLevel = hpPerLevel;
    }

    /**
     * @param hpTemp
     *            the hpTemp to set
     */
    public void setHpTemp(int hpTemp) {
	mHpTemp = hpTemp;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(int id) {
	mId = id;
    }

    /**
     * @param initMisc
     *            the initMisc to set
     */
    public void setInitMisc(int initMisc) {
	mInitMisc = initMisc;
    }

    /**
     * @param intel
     *            the intel to set
     */
    public void setIntel(int intel) {
	mInt = intel;
    }

    /**
     * @param level
     *            the level to set
     */
    public void setLevel(int level) {
	mLevel = level;
    }

    /**
     * @param misc
     *            the misc to set
     */
    public void setMisc(String misc) {
	mMisc = misc;
    }

    /**
     * @param movArmor
     *            the movArmor to set
     */
    public void setMovArmor(int movArmor) {
	mMovArmor = movArmor;
    }

    /**
     * @param movBase
     *            the movBase to set
     */
    public void setMovBase(int movBase) {
	mMovBase = movBase;
    }

    /**
     * @param movItem
     *            the movItem to set
     */
    public void setMovItem(int movItem) {
	mMovItem = movItem;
    }

    /**
     * @param movMisc
     *            the movMisc to set
     */
    public void setMovMisc(int movMisc) {
	mMovMisc = movMisc;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	mName = name;
    }

    /**
     * @param notes
     *            the notes to set
     */
    public void setNotes(String notes) {
	mNotes = notes;
    }

    /**
     * @param paragonPath
     *            the paragonPath to set
     */
    public void setParagonPath(String paragonPath) {
	mParagonPath = paragonPath;
    }

    /**
     * @param platinumPieces
     *            the platinumPieces to set
     */
    public void setPlatinumPieces(int platinumPieces) {
	mPlatinumPieces = platinumPieces;
    }

    /**
     * @param race
     *            the race to set
     */
    public void setRace(String race) {
	mRace = race;
    }

    /**
     * @param refAbil
     *            the refAbil to set
     */
    public void setRefAbil(int refAbil) {
	mRefAbil = refAbil;
    }

    /**
     * @param refClass
     *            the refClass to set
     */
    public void setRefClass(int refClass) {
	mRefClass = refClass;
    }

    /**
     * @param refEnh
     *            the refEnh to set
     */
    public void setRefEnh(int refEnh) {
	mRefEnh = refEnh;
    }

    /**
     * @param refFeat
     *            the refFeat to set
     */
    public void setRefFeat(int refFeat) {
	mRefFeat = refFeat;
    }

    /**
     * @param refMisc
     *            the refMisc to set
     */
    public void setRefMisc(int refMisc) {
	mRefMisc = refMisc;
    }

    /**
     * @param secondWind
     *            the secondWind to set
     */
    public void setSecondWind(boolean secondWind) {
	mSecondWind = secondWind;
    }

    /**
     * @param silverPieces
     *            the silverPieces to set
     */
    public void setSilverPieces(int silverPieces) {
	mSilverPieces = silverPieces;
    }

    /**
     * @param str
     *            the str to set
     */
    public void setStr(int str) {
	mStr = str;
    }

    /**
     * @param surgeClass
     *            the surgeClass to set
     */
    public void setSurgeClass(int surgeClass) {
	mSurgeClass = surgeClass;
    }

    /**
     * @param surgeFeat
     *            the surgeFeat to set
     */
    public void setSurgeFeat(int surgeFeat) {
	mSurgeFeat = surgeFeat;
    }

    /**
     * @param surgeMisc
     *            the surgeMisc to set
     */
    public void setSurgeMisc(int surgeMisc) {
	mSurgeMisc = surgeMisc;
    }

    /**
     * @param surgesCurrent
     *            the surgesCurrent to set
     */
    public void setSurgesCurrent(int surgesCurrent) {
	mSurgesCurrent = surgesCurrent;
    }

    /**
     * @param surgesPerDayClass
     *            the surgesPerDayClass to set
     */
    public void setSurgesPerDayClass(int surgesPerDayClass) {
	mSurgesPerDayClass = surgesPerDayClass;
    }

    /**
     * @param surgesPerDayFeat
     *            the surgesPerDayFeat to set
     */
    public void setSurgesPerDayFeat(int surgesPerDayFeat) {
	mSurgesPerDayFeat = surgesPerDayFeat;
    }

    /**
     * @param surgesPerDayMisc
     *            the surgesPerDayMisc to set
     */
    public void setSurgesPerDayMisc(int surgesPerDayMisc) {
	mSurgesPerDayMisc = surgesPerDayMisc;
    }

    /**
     * @param willAbil
     *            the willAbil to set
     */
    public void setWillAbil(int willAbil) {
	mWillAbil = willAbil;
    }

    /**
     * @param willClass
     *            the willClass to set
     */
    public void setWillClass(int willClass) {
	mWillClass = willClass;
    }

    /**
     * @param willEnh
     *            the willEnh to set
     */
    public void setWillEnh(int willEnh) {
	mWillEnh = willEnh;
    }

    /**
     * @param willFeat
     *            the willFeat to set
     */
    public void setWillFeat(int willFeat) {
	mWillFeat = willFeat;
    }

    /**
     * @param willMisc
     *            the willMisc to set
     */
    public void setWillMisc(int willMisc) {
	mWillMisc = willMisc;
    }

    /**
     * @param wis
     *            the wis to set
     */
    public void setWis(int wis) {
	mWis = wis;
    }

    /**
     * @param xp
     *            the xp to set
     */
    public void setXp(int xp) {
	mXp = xp;
    }

    private int getMod(int value) {
	int mod = 0;
	if (value >= 10) {
	    mod = (value - 10) / 2;
	} else {
	    mod = (value - 11) / 2;
	}
	return mod;
    }
}
