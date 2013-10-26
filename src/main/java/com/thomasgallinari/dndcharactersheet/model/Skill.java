/**
 * Copyright (C) 2011 Thomas Gallinari
 */
package com.thomasgallinari.dndcharactersheet.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * A skill of a character.
 */
@DatabaseTable(tableName = "skill")
public class Skill {
    
    private static final String ACROBATICS = "skill_acrobatics";
    private static final String ARCANA = "skill_arcana";
    private static final String ATHLETICS = "skill_athletics";
    private static final String BLUFF = "skill_bluff";
    private static final String DIPLOMACY = "skill_diplomacy";
    private static final String DUNGEONEERING = "skill_dungeoneering";
    private static final String ENDURANCE = "skill_endurance";
    private static final String HEAL = "skill_heal";
    private static final String HISTORY = "skill_history";
    private static final String INSIGHT = "skill_insight";
    private static final String INTIMIDATE = "skill_intimidate";
    private static final String NATURE = "skill_nature";
    private static final String PERCEPTION = "skill_perception";
    private static final String RELIGION = "skill_religion";
    private static final String STEALTH = "skill_stealth";
    private static final String STREETWISE = "skill_streetwise";
    private static final String THIEVERY = "skill_thievery";

    public static final String[][] DEFAULT_SKILLS = {
	    { ACROBATICS, Ability.DEX }, { ARCANA, Ability.INT },
	    { ATHLETICS, Ability.STR }, { BLUFF, Ability.CHA },
	    { DIPLOMACY, Ability.CHA }, { DUNGEONEERING, Ability.WIS },
	    { ENDURANCE, Ability.CON }, { HEAL, Ability.WIS },
	    { HISTORY, Ability.INT }, { INSIGHT, Ability.WIS },
	    { INTIMIDATE, Ability.CHA }, { NATURE, Ability.WIS },
	    { PERCEPTION, Ability.WIS }, { RELIGION, Ability.INT },
	    { STEALTH, Ability.DEX }, { STREETWISE, Ability.CHA },
	    { THIEVERY, Ability.DEX } };

    @DatabaseField(columnName = "ability", canBeNull = false)
    private String mAbility;
    @DatabaseField(columnName = "armor", defaultValue = "0")
    private int mArmor;
    @DatabaseField(columnName = "character_id", canBeNull = false, foreign = true)
    private Character mCharacter;
    @DatabaseField(columnName = "id", generatedId = true)
    private int mId;
    @DatabaseField(columnName = "misc", defaultValue = "0")
    private int mMisc;
    @DatabaseField(columnName = "name", canBeNull = false)
    private String mName;
    @DatabaseField(columnName = "trained", defaultValue = "false")
    private boolean mTrained;

    /**
     * Creates a new {@link Skill}.
     */
    public Skill() {

    }

    /**
     * @return the ability
     */
    public String getAbility() {
	return mAbility;
    }

    /**
     * @return the armor
     */
    public int getArmor() {
	return mArmor;
    }

    /**
     * @return the character
     */
    public Character getCharacter() {
	return mCharacter;
    }

    /**
     * @return the id
     */
    public int getId() {
	return mId;
    }

    /**
     * @return the misc
     */
    public int getMisc() {
	return mMisc;
    }

    /**
     * @return the name
     */
    public String getName() {
	return mName;
    }

    /**
     * @return the skill score
     */
    public int getScore() {
	int abilMod = 0;
	if (mAbility.equals(Ability.CHA)) {
	    abilMod = mCharacter.getChaMod();
	} else if (mAbility.equals(Ability.CON)) {
	    abilMod = mCharacter.getConMod();
	} else if (mAbility.equals(Ability.DEX)) {
	    abilMod = mCharacter.getDexMod();
	} else if (mAbility.equals(Ability.INT)) {
	    abilMod = mCharacter.getIntelMod();
	} else if (mAbility.equals(Ability.STR)) {
	    abilMod = mCharacter.getStrMod();
	} else if (mAbility.equals(Ability.WIS)) {
	    abilMod = mCharacter.getWisMod();
	}
	return abilMod + mCharacter.getLevel() / 2 + mArmor + mMisc
		+ (mTrained ? 5 : 0);
    }

    /**
     * @return the trained
     */
    public boolean isTrained() {
	return mTrained;
    }

    /**
     * @param ability
     *            the ability to set
     */
    public void setAbility(String ability) {
	this.mAbility = ability;
    }

    /**
     * @param armor
     *            the armor to set
     */
    public void setArmor(int armor) {
	this.mArmor = armor;
    }

    /**
     * @param character
     *            the character to set
     */
    public void setCharacter(Character character) {
	this.mCharacter = character;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(int id) {
	this.mId = id;
    }

    /**
     * @param misc
     *            the misc to set
     */
    public void setMisc(int misc) {
	this.mMisc = misc;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	this.mName = name;
    }

    /**
     * @param trained
     *            the trained to set
     */
    public void setTrained(boolean trained) {
	this.mTrained = trained;
    }
}
