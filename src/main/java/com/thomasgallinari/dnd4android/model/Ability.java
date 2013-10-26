/**
 * Copyright (C) 2010 Thomas Gallinari
 */
package com.thomasgallinari.dnd4android.model;

import android.content.res.Resources;

import com.thomasgallinari.dnd4android.R;

/**
 * An interface that contains all the abilities.
 */
public class Ability {

    /**
     * The charisma ability.
     */
    public static final String CHA = "CHA";

    /**
     * The constitution ability.
     */
    public static final String CON = "CON";

    /**
     * The dexterity ability.
     */
    public static final String DEX = "DEX";

    /**
     * The intelligence ability.
     */
    public static final String INT = "INT";

    /**
     * The strength ability.
     */
    public static final String STR = "STR";

    /**
     * The wisdom ability.
     */
    public static final String WIS = "WIS";

    /**
     * Gets the ability name from its key.
     * 
     * @param pAbilKey
     *            the key of the ability
     * @param pRes
     *            the resources to get the string
     * @return the name of the ability
     */
    public static String getName(String pAbilKey, Resources pRes) {
	String name = "";
	if (pAbilKey.equals(CHA)) {
	    name = pRes.getString(R.string.scores_ability_cha);
	} else if (pAbilKey.equals(CON)) {
	    name = pRes.getString(R.string.scores_ability_con);
	} else if (pAbilKey.equals(DEX)) {
	    name = pRes.getString(R.string.scores_ability_dex);
	} else if (pAbilKey.equals(INT)) {
	    name = pRes.getString(R.string.scores_ability_int);
	} else if (pAbilKey.equals(STR)) {
	    name = pRes.getString(R.string.scores_ability_str);
	} else if (pAbilKey.equals(WIS)) {
	    name = pRes.getString(R.string.scores_ability_wis);
	}
	return name;
    }
}
