/**
 * Author: Nikko Campbell
 * StudentID: 0505046
 * File: Vigenere.java
 * 
 * A static utility class to supply functionality for operations involving the
 * Vigenere cipher. Includes encryption/decyption, frequency analysis, index of
 * coincidence calculation, and key calculation functionality.
 */
package com.nikkocampbell.vigenere;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Vigenere {

	/**
	 * Encrypts a given plaintext message with a given keyword using the
	 * Vigenere cipher
	 * 
	 * @param plaintext
	 *            a plaintext string to be encoded
	 * @param keyword
	 *            a string to use in the encryption of plaintext
	 * @return the encrypted version of plaintext
	 */
	public static String encrypt(String plaintext, String keyword) {
		plaintext = format(plaintext);
		keyword = format(keyword);
		String ciphertext = "";

		for (int i = 0; i < plaintext.length(); i++) {
			int plainChar = plaintext.charAt(i) - 'A';
			int keyChar = keyword.charAt(i % keyword.length()) - 'A';
			int cipherChar = ((plainChar + keyChar) % 26) + 'A';
			ciphertext += (char) cipherChar;
		}

		return ciphertext;
	}

	/**
	 * Decrypts a given ciphertext message encoded with a Vigenere cipher using
	 * the given keyword
	 * 
	 * @param ciphertext
	 *            a ciphertext string to be decoded
	 * @param keyword
	 *            a string to use in the decryption of ciphertext
	 * @return the decrypted version of ciphertext
	 */
	public static String decrypt(String ciphertext, String keyword) {
		ciphertext = format(ciphertext);
		keyword = format(keyword);
		String plaintext = "";

		for (int i = 0; i < ciphertext.length(); i++) {
			int cipherChar = ciphertext.charAt(i) - 'A';
			int keyChar = keyword.charAt(i % keyword.length()) - 'A';
			int plainChar = ((cipherChar - keyChar) % 26);
			if (plainChar < 0) {
				plainChar += 26;
			}
			plainChar += 'A';
			plaintext += (char) plainChar;
		}

		return plaintext;
	}

	/**
	 * Calculates the number of occurrences of each letter in a given string
	 * 
	 * @param text
	 *            a string to calculate the letter frequencies on
	 * @return an integer array containing the number of occurrences of each
	 *         letter within text
	 */
	public static int[] letterFrequency(String text) {
		int[] frequencies = new int[26];

		text = format(text);

		for (int i = 0; i < text.length(); i++) {
			frequencies[text.charAt(i) - 'A']++;
		}

		return frequencies;
	}

	/**
	 * Wrapper function to calculate the Index of Coincidence from a given text
	 * 
	 * @param text
	 *            a string to calculate the Index of Coincidence of
	 * @return the Index of Coincidence
	 * @see Vigenere#calcIC(int[])
	 */
	public static double calcIC(String text) {
		return calcIC(letterFrequency(text));
	}

	/**
	 * Calculates the Index of Coincidence based on the supplied letter
	 * frequency array
	 * 
	 * @param frequencies
	 *            an array containing the letter frequencies of a text
	 * @return the Index of Coincidence
	 * @see Vigenere#calcIC(String)
	 */
	public static double calcIC(int[] frequencies) {
		double ic = 0;
		int sum = 0;
		for (int i = 0; i < frequencies.length; i++) {
			sum += frequencies[i];
		}

		for (int i = 0; i < frequencies.length; i++) {
			double top = frequencies[i] * (frequencies[i] - 1);
			double bottom = sum * (sum - 1);
			ic += top / bottom;
		}
		return ic;
	}

	/**
	 * Estimates the keylength of a give string encrypted with a Vigenere cipher
	 * 
	 * @param text
	 *            a string encrypted with a Vigenere cipher
	 * @return the approximate key length of the key used to encrypt the text
	 */
	public static double estimateKeyLength(String text) {
		double ic = calcIC(text);
		double top = 0.027 * text.length();
		double bottom = (text.length() - 1) * ic - 0.038 * text.length()
				+ 0.065;
		return top / bottom;
	}

	/**
	 * Performs the kasiski test in order to calculate the length of the key
	 * word used to encrypt a given ciphertext. Divides the text into its
	 * 3-character {@link Substring}s, and counts the number of occurances of
	 * each, storing each unique substring in a {@link HashMap}. After this, the
	 * distances between substrings with multiple occurences are calculated, and
	 * their factors are used to estimate the length of the keyword.
	 * 
	 * @param text
	 *            a string encrypted with a Vigenere cipher
	 * @param minKeyLength
	 *            the minimum length that may be returned for the key length
	 * @param maxKeyLength
	 *            the maximum length that may be returned for the key length
	 * @return the estimated length of the key used to encrypt the text
	 * @see Vigenere#estimateKeyLength(HashMap, int, int)
	 */
	public static int kasiski(String text, int minKeyLength, int maxKeyLength) {
		HashMap<String, Substring> substringMap = new HashMap<String, Substring>();
		text = format(text);
		String sub;

		// Fill HashMap with all substrings
		for (int i = 0; i < text.length() - 2; i++) {
			sub = text.substring(i, i + 3);
			if (substringMap.containsKey(sub)) {
				substringMap.get(sub).addOccurance(i);
			} else {
				substringMap.put(sub, new Substring(sub, i));
			}
		}

		// Convert HashMap to ArrayList for working with the values
		ArrayList<Substring> substrings = new ArrayList<Substring>(
				substringMap.values());

		// Remove all substrings with only single occurrence
		substrings = Substring.removeSingleOccurrenceSubstrings(substrings);

		/*
		 * Find the differences between positions of multiple occurrences ofthe
		 * same substring and calculate the factors of each
		 */
		HashMap<Integer, Integer> factorOccurances = new HashMap<Integer, Integer>();
		for (Substring substr : substrings) {
			ArrayList<Integer> differences = substr.getDifferences(true);
			for (Integer diff : differences) {
				ArrayList<Integer> factors = calculateFactors(diff);
				for (Integer fact : factors) {
					if (factorOccurances.containsKey(fact)) {
						Integer temp = factorOccurances.get(fact);
						factorOccurances.put(fact, ++temp);
					} else {
						factorOccurances.put(fact, 1);
					}
				}
			}
		}

		// Analzye the frequency of all of the factors
		return estimateKeyLength(factorOccurances, minKeyLength, maxKeyLength);
	}

	/**
	 * Uses a list of {@link Substring} occurences found through the Kasiski
	 * test in order to estimate the length of a keyword. The most common
	 * occuring factor of the distances between reoccuring substrings is most
	 * likely the length of the keyword.
	 * 
	 * @param occurances
	 * @param minKeyLength
	 * @param maxKeyLength
	 * @return the estimated length of the keyword for the ciphertext associated
	 *         with occurrences
	 * @see Vigenere#kasiski(String, int, int)
	 */
	public static int estimateKeyLength(HashMap<Integer, Integer> occurances,
			int minKeyLength, int maxKeyLength) {
		Set<Integer> keys = occurances.keySet();
		Integer maxKey = 0;
		Integer maxFreq = 0;
		for (Integer key : keys) {
			if (key < minKeyLength)
				continue;
			if (key > maxKeyLength)
				continue;
			Integer freq = occurances.get(key);
			if (freq >= maxFreq && key >= minKeyLength && key <= maxKeyLength) {
				maxFreq = freq;
				maxKey = key;
			}
		}
		if (maxKey < minKeyLength) {
			return minKeyLength;
		} else if (maxKey > maxKeyLength) {
			return maxKeyLength;
		} else {
			return maxKey;
		}
	}

	/**
	 * Calculates the prime factors of a given number
	 * 
	 * @param num
	 *            number to calculate the prime factors of
	 * @return an {@link ArrayList} of Integers containing the prime factors of
	 *         num
	 */
	public static ArrayList<Integer> calculatePrimeFactors(int num) {
		ArrayList<Integer> factors = new ArrayList<Integer>();
		int n = num;
		for (int i = 2; i <= n / i; i++) {
			while (n % i == 0) {
				factors.add(i);
				n /= i;
			}
		}
		if (n > 1) {
			factors.add(n);
		}
		return factors;
	}

	/**
	 * Calculates the factors of a give number
	 * 
	 * @param num
	 *            number to calculate the factors of
	 * @return an {@link ArrayList} of Integers containing the factors of num
	 */
	public static ArrayList<Integer> calculateFactors(int num) {
		ArrayList<Integer> factors = new ArrayList<Integer>();
		int n = num;
		for (int i = 1; i <= (int) Math.sqrt(n); i++) {
			if (n % i == 0) {
				factors.add(i);
			}
		}
		int size = factors.size();
		for (int i = size - 1; i >= 0; i--) {
			factors.add(num / factors.get(i));
		}

		return factors;
	}

	/**
	 * Estimates the key used to encrypt ciphertext by splitting ciphertext into
	 * a number of strings split based on keyLength and using a basic frequency
	 * analysis to estimate the key used to generate them and combining to
	 * estimate the key
	 * 
	 * @param ciphertext
	 *            a string encrypted with a key of length keyLength
	 * @param keyLength
	 *            length of the key used to encrypt ciphertext
	 * @return an estimate of the key used to encrypt ciphertext
	 */
	public static String estimateKey(String ciphertext, int keyLength) {
		String separatedCipher[] = new String[keyLength];
		String key = "";

		for (int i = 0; i < keyLength; i++) {
			separatedCipher[i] = "";
		}

		for (int i = 0; i < ciphertext.length(); i++) {
			separatedCipher[i % keyLength] += ciphertext.charAt(i);
		}

		for (int i = 0; i < keyLength; i++) {
			int[] freq = letterFrequency(separatedCipher[i]);
			key += (char) ((arrayMaxPos(freq) - 4) + 'A');
		}

		return key;
	}

	/**
	 * Returns the position in theArray with the highest value
	 * 
	 * @param theArray
	 *            an int array
	 * @return the position in theArray with the highest value
	 */
	public static int arrayMaxPos(int[] theArray) {
		int maxPos = 0;
		for (int i = 1; i < theArray.length; i++) {
			if (theArray[i] > theArray[maxPos]) {
				maxPos = i;
			}
		}
		return maxPos;
	}

	/**
	 * Formats a given string for use with encryption and decryption methods.
	 * Removes all non-alphabetic characters, and capitalizes all remaining
	 * characters.
	 * 
	 * @param text
	 *            a string to format
	 * @return a string with proper formatting for encryption and decryption
	 *         methods
	 */
	public static String format(String text) {
		return text.toUpperCase().replaceAll("[^\\p{L}]", "");
	}
}
