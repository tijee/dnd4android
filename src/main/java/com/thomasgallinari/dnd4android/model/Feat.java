/**
 * Copyright (C) 2010 Thomas Gallinari
 */
package com.thomasgallinari.dnd4android.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * A feat of a character.
 */
@DatabaseTable(tableName = "feat")
public class Feat implements Serializable, Comparable<Feat> {

    @DatabaseField(columnName = "character_id", canBeNull = false, foreign = true)
    private Character mCharacter;
    @DatabaseField(columnName = "description", canBeNull = false)
    private String mDescription;
    @DatabaseField(columnName = "id", generatedId = true)
    private int mId;
    @DatabaseField(columnName = "name", canBeNull = false)
    private String mName;

    /**
     * Creates a new {@link Feat}.
     */
    public Feat() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Feat another) {
        return mName.compareTo(another.mName);
    }

    /**
     * @return the character
     */
    public Character getCharacter() {
        return mCharacter;
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
     * @return the name
     */
    public String getName() {
        return mName;
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
     * @param name the name to set
     */
    public void setName(String name) {
        this.mName = name;
    }
}
