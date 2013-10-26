/**
 * Copyright (C) 2010 Thomas Gallinari
 */
package com.thomasgallinari.dndcharactersheet.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * A attack of a character.
 */
@DatabaseTable(tableName = "attack")
public class Attack implements Comparable<Attack> {

    @DatabaseField(columnName = "attack_abil", defaultValue = "0")
    private int mAttackAbil;
    @DatabaseField(columnName = "attack_class", defaultValue = "0")
    private int mAttackClass;
    @DatabaseField(columnName = "attack_enh", defaultValue = "0")
    private int mAttackEnh;
    @DatabaseField(columnName = "attack_feat", defaultValue = "0")
    private int mAttackFeat;
    @DatabaseField(columnName = "attack_misc", defaultValue = "0")
    private int mAttackMisc;
    @DatabaseField(columnName = "attack_prof", defaultValue = "0")
    private int mAttackProf;
    @DatabaseField(columnName = "character_id", canBeNull = false, foreign = true)
    private Character mCharacter;
    @DatabaseField(columnName = "damage_abil", defaultValue = "0")
    private int mDamageAbil;
    @DatabaseField(columnName = "damage_enh", defaultValue = "0")
    private int mDamageEnh;
    @DatabaseField(columnName = "damage_feat", defaultValue = "0")
    private int mDamageFeat;
    @DatabaseField(columnName = "damage_misc", defaultValue = "0")
    private int mDamageMisc;
    @DatabaseField(columnName = "id", generatedId = true)
    private int mId;
    @DatabaseField(columnName = "name", canBeNull = false)
    private String mName;

    /**
     * Creates a new {@link Attack}.
     */
    public Attack() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Attack another) {
	return mName.compareTo(another.mName);
    }

    /**
     * @return the attack abil
     */
    public int getAttackAbil() {
	return mAttackAbil;
    }

    /**
     * @return the attack score
     */
    public int getAttackScore() {
	return mCharacter.getLevel() / 2 + mAttackAbil + mAttackClass
		+ mAttackProf + mAttackFeat + mAttackEnh + mAttackMisc;
    }

    /**
     * @return the attack class
     */
    public int getAttackClass() {
	return mAttackClass;
    }

    /**
     * @return the attack enh
     */
    public int getAttackEnh() {
	return mAttackEnh;
    }

    /**
     * @return the attack feat
     */
    public int getAttackFeat() {
	return mAttackFeat;
    }

    /**
     * @return the attack misc
     */
    public int getAttackMisc() {
	return mAttackMisc;
    }

    /**
     * @return the attack prof
     */
    public int getAttackProf() {
	return mAttackProf;
    }

    /**
     * @return the character
     */
    public Character getCharacter() {
	return mCharacter;
    }

    /**
     * @return the damage abil
     */
    public int getDamageAbil() {
	return mDamageAbil;
    }

    /**
     * @return the damage enh
     */
    public int getDamageEnh() {
	return mDamageEnh;
    }

    /**
     * @return the damage feat
     */
    public int getDamageFeat() {
	return mDamageFeat;
    }

    /**
     * @return the damage misc
     */
    public int getDamageMisc() {
	return mDamageMisc;
    }

    /**
     * @return the damage score
     */
    public int getDamageScore() {
	return mDamageAbil + mDamageFeat + mDamageEnh + mDamageMisc;
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
     * @param attackAbil
     *            the attackAbil to set
     */
    public void setAttackAbil(int attackAbil) {
	mAttackAbil = attackAbil;
    }

    /**
     * @param attackClass
     *            the attackClass to set
     */
    public void setAttackClass(int attackClass) {
	mAttackClass = attackClass;
    }

    /**
     * @param attackEnh
     *            the attackEnh to set
     */
    public void setAttackEnh(int attackEnh) {
	mAttackEnh = attackEnh;
    }

    /**
     * @param attackFeat
     *            the attackFeat to set
     */
    public void setAttackFeat(int attackFeat) {
	mAttackFeat = attackFeat;
    }

    /**
     * @param attackMisc
     *            the attackMisc to set
     */
    public void setAttackMisc(int attackMisc) {
	mAttackMisc = attackMisc;
    }

    /**
     * @param attackProf
     *            the attackProf to set
     */
    public void setAttackProf(int attackProf) {
	mAttackProf = attackProf;
    }

    /**
     * @param character
     *            the character to set
     */
    public void setCharacter(Character character) {
	mCharacter = character;
    }

    /**
     * @param damageAbil
     *            the damageAbil to set
     */
    public void setDamageAbil(int damageAbil) {
	mDamageAbil = damageAbil;
    }

    /**
     * @param damageEnh
     *            the damageEnh to set
     */
    public void setDamageEnh(int damageEnh) {
	mDamageEnh = damageEnh;
    }

    /**
     * @param damageFeat
     *            the damageFeat to set
     */
    public void setDamageFeat(int damageFeat) {
	mDamageFeat = damageFeat;
    }

    /**
     * @param damageMisc
     *            the damageMisc to set
     */
    public void setDamageMisc(int damageMisc) {
	mDamageMisc = damageMisc;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(int id) {
	mId = id;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	mName = name;
    }
}
