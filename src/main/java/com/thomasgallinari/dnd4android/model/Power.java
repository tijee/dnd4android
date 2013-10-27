/**
 * Copyright (C) 2010 Thomas Gallinari
 */
package com.thomasgallinari.dnd4android.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.thomasgallinari.dnd4android.R;

import java.io.Serializable;

/**
 * A power of a character.
 */
@DatabaseTable(tableName = "power")
public class Power implements Serializable {

    /**
     * The at-will power type.
     */
    public static final String AT_WILL = "at_will";

    /**
     * The daily power type.
     */
    public static final String DAILY = "daily";

    /**
     * The encounter power type.
     */
    public static final String ENCOUNTER = "encounter";

    /**
     * The key to sort by level.
     */
    public static final String SORT_ATTR_LEVEL = "level";

    /**
     * The key to sort by name.
     */
    public static final String SORT_ATTR_NAME = "name";

    /**
     * The key to sort by type.
     */
    public static final String SORT_ATTR_TYPE = "type";

    /**
     * The key to sort ascending.
     */
    public static final String SORT_ORDER_ASC = "asc";

    /**
     * The key to sort descending.
     */
    public static final String SORT_ORDER_DESC = "desc";

    @DatabaseField(columnName = "character_id", canBeNull = false, foreign = true)
    private Character mCharacter;
    @DatabaseField(columnName = "description", canBeNull = false)
    private String mDescription;
    @DatabaseField(columnName = "id", generatedId = true)
    private int mId;
    @DatabaseField(columnName = "level", defaultValue = "0")
    private int mLevel;
    @DatabaseField(columnName = "name", canBeNull = false)
    private String mName;
    @DatabaseField(columnName = "type", canBeNull = false, defaultValue = AT_WILL)
    private String mType;
    @DatabaseField(columnName = "used", defaultValue = "false")
    private boolean mUsed;

    /**
     * Creates a new {@link Power}.
     */
    public Power() {

    }

    /**
     * Compares this power with another.
     *
     * @param another the other power
     * @param attr    the attribute to be compared
     * @param order   ascending or descending
     * @return -1 if this power is before the other, 1 if it is after, 0 if they
     * are equal
     */
    public int compareTo(Power another, String attr, String order) {
        int result = 0;

        Power first = null;
        Power second = null;
        if (order.equals(SORT_ORDER_ASC)) {
            first = this;
            second = another;
        } else {
            first = another;
            second = this;
        }

        if (attr.equals(SORT_ATTR_LEVEL)) {
            result = Integer.valueOf(first.mLevel).compareTo(second.mLevel);
        } else if (attr.equals(SORT_ATTR_NAME)) {
            result = first.mName.compareTo(second.mName);
        } else if (attr.equals(SORT_ATTR_TYPE)) {
            if (second.mType.equals(DAILY)) {
                if (first.mType.equals(AT_WILL)
                        || first.mType.equals(ENCOUNTER)) {
                    result = -1;
                }
            } else if (second.mType.equals(ENCOUNTER)) {
                if (first.mType.equals(AT_WILL)) {
                    result = -1;
                } else if (first.mType.equals(DAILY)) {
                    result = 1;
                }
            } else if (second.mType.equals(AT_WILL)) {
                if (first.mType.equals(ENCOUNTER) || first.mType.equals(DAILY)) {
                    result = 1;
                }
            }
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
     * @return the power color corresponding to its type: at-will -> green,
     * encounter -> red, daily -> gray
     */
    public int getColorId() {
        int color = 0;
        if (mType.equals(AT_WILL)) {
            color = R.color.dnd_green;
        } else if (mType.equals(ENCOUNTER)) {
            color = mUsed ? R.color.dnd_red_disabled : R.color.dnd_red;
        } else if (mType.equals(DAILY)) {
            color = mUsed ? R.color.dnd_gray_disabled : R.color.dnd_gray;
        }
        return color;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return mDescription;
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
     * @return the type
     */
    public String getType() {
        return mType;
    }

    /**
     * @return the used
     */
    public boolean isUsed() {
        return mUsed;
    }

    /**
     * @param character the character to set
     */
    public void setCharacter(Character character) {
        this.mCharacter = character;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.mDescription = description;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.mId = id;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(int level) {
        this.mLevel = level;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.mName = name;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.mType = type;
    }

    /**
     * @param used the used to set
     */
    public void setUsed(boolean used) {
        this.mUsed = used;
    }
}
