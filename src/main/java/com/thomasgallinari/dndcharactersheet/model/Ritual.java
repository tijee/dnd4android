/**
 * Copyright (C) 2010 Thomas Gallinari
 */
package com.thomasgallinari.dndcharactersheet.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * A ritual of a character.
 */
@DatabaseTable(tableName = "ritual")
public class Ritual implements Comparable<Ritual> {

    @DatabaseField(columnName = "character_id", canBeNull = false, foreign = true)
    private Character mCharacter;
    @DatabaseField(columnName = "cost")
    private String mCost;
    @DatabaseField(columnName = "description", canBeNull = false)
    private String mDescription;
    @DatabaseField(columnName = "duration")
    private String mDuration;
    @DatabaseField(columnName = "id", generatedId = true)
    private int mId;
    @DatabaseField(columnName = "level", defaultValue = "0")
    private int mLevel;
    @DatabaseField(columnName = "name", canBeNull = false)
    private String mName;
    @DatabaseField(columnName = "skill")
    private String mSkill;
    @DatabaseField(columnName = "time")
    private String mTime;

    /**
     * Creates a new {@link Ritual}.
     */
    public Ritual() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Ritual another) {
	int result = Integer.valueOf(mLevel).compareTo(another.mLevel);
	if (result == 0) {
	    result = mName.compareTo(another.mName);
	}
	return result;
    }

    /**
     * @return the character
     */
    public Character getCharacter() {
	return mCharacter;
    }

    /**
     * @return the cost
     */
    public String getCost() {
	return mCost;
    }

    /**
     * @return the description
     */
    public String getDescription() {
	return mDescription;
    }

    /**
     * @return the duration
     */
    public String getDuration() {
	return mDuration;
    }

    /**
     * @return the id
     */
    public int getId() {
	return mId;
    }

    /**
     * @return the level
     */
    public int getLevel() {
	return mLevel;
    }

    /**
     * @return the name
     */
    public String getName() {
	return mName;
    }

    /**
     * @return the skill
     */
    public String getSkill() {
	return mSkill;
    }

    /**
     * @return the time
     */
    public String getTime() {
	return mTime;
    }

    /**
     * @param character
     *            the character to set
     */
    public void setCharacter(Character character) {
	mCharacter = character;
    }

    /**
     * @param cost
     *            the cost to set
     */
    public void setCost(String cost) {
	mCost = cost;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
	mDescription = description;
    }

    /**
     * @param duration
     *            the duration to set
     */
    public void setDuration(String duration) {
	mDuration = duration;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(int id) {
	mId = id;
    }

    /**
     * @param level
     *            the level to set
     */
    public void setLevel(int level) {
	mLevel = level;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	mName = name;
    }

    /**
     * @param skill
     *            the skill to set
     */
    public void setSkill(String skill) {
	mSkill = skill;
    }

    /**
     * @param time
     *            the time to set
     */
    public void setTime(String time) {
	mTime = time;
    }
}
