/**
 * Author: Nikko Campbell
 * StudentID: 0505046
 * File: Substring.java
 * 
 * A representation of a substring within a larger text. Stores the value of
 * the substring as well as the position of all occurrences of the substring
 * within the larger text.
 */
package com.nikkocampbell.vigenere;

import java.util.ArrayList;
import java.util.Iterator;

public class Substring {

	private String message;
	private ArrayList<Integer> positions;
	private ArrayList<Integer> differences;

	/**
	 * Constructs a new substring instance for a given string message and adds
	 * the position as the first occurrence
	 * 
	 * @param msg
	 *            the string representation of the substring
	 * @param pos
	 *            the position of the occurrence of the substring
	 */
	public Substring(String msg, int pos) {
		message = msg;
		positions = new ArrayList<Integer>();
		differences = new ArrayList<Integer>();
		positions.add(pos);
	}

	/**
	 * Adds a new occurrence of the substring
	 * 
	 * @param pos
	 *            the position of the occurrence of the substring
	 */
	public void addOccurance(int pos) {
		positions.add(pos);
	}

	/**
	 * Computes and stores the distances between the positions of subsequent
	 * substrings
	 */
	public void calculateDifferences() {
		if (positions.size() > 1) {
			for (int i = 1; i < positions.size(); i++) {
				differences.add(positions.get(i) - positions.get(i - 1));
			}
		} else {
			differences.add(0);
		}
	}

	/**
	 * Returns an ArrayList of all the positions of the substring
	 * 
	 * @return the positions of each occurrence of this substring
	 */
	public ArrayList<Integer> getPositions() {
		ArrayList<Integer> pos = new ArrayList<Integer>();
		for (Integer n : positions) {
			pos.add(n);
		}
		return pos;
	}

	/**
	 * Returns an ArrayList of all the differences between subsequent
	 * occurrences of substrings computed by calculateDifferences. Can also
	 * specify whether or not to recompute the differences.
	 * 
	 * @param recalc
	 *            specifies if the differences should be recalculated
	 * @return the differences between each subsequent occurrence of the
	 *         substring
	 */
	public ArrayList<Integer> getDifferences(boolean recalc) {
		if (recalc) {
			calculateDifferences();
		}
		ArrayList<Integer> diff = new ArrayList<Integer>();
		for (Integer n : differences) {
			diff.add(n);
		}
		return diff;
	}

	/**
	 * Retuns the number of occurrences of this substring
	 * 
	 * @return number of occurrences of the substring
	 */
	public int getOccuranceCount() {
		return positions.size();
	}

	/**
	 * Determines whether or not there is more than one occurrence of this
	 * substring
	 * 
	 * @return true if only one occurrence. False otherwise
	 */
	public boolean isSingleOccurance() {
		return positions.size() <= 1;
	}

	/**
	 * Returns the message of the substring
	 * 
	 * @return the message representation of the substring
	 */
	public String getValue() {
		return message;
	}

	/**
	 * Loops through an ArrayList of Substrings, removing any instances with
	 * only a single occurrence
	 * 
	 * @param substrings
	 *            ArrayList to remove single occurrences from
	 * @return an ArrayList of Substrings all occurring more than once
	 */
	public static ArrayList<Substring> removeSingleOccurrenceSubstrings(
			ArrayList<Substring> substrings) {
		Iterator<Substring> it = substrings.iterator();
		while (it.hasNext()) {
			Substring substr = it.next();
			if (substr.isSingleOccurance()) {
				it.remove();
			}
		}
		return substrings;
	}

	/**
	 * Returns the message of the substring followed by the position of each of
	 * the occurrences of the Substring
	 */
	public String toString() {
		String out = message + ": ";
		for (int i = 0; i < positions.size(); i++) {
			out += positions.get(i) + ", ";
		}
		return out;
	}
}
