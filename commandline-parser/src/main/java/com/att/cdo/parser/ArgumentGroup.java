package com.att.cdo.parser;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ArgumentGroups - is a class that handles the grouping and nested
 * resolution of arguments.
 * 
 * @author Lance Jensen email:lj556b@att.com phone:214-882-3888
 *
 */
public class ArgumentGroup {

	private static int NOT_A_FLAG = 0, ROOT_FLAG_DEPTH = 1;
	
	private String flag;
	private String[] flagValues;
	private List<ArgumentGroup> subFlags = new LinkedList<ArgumentGroup>();
	
	/**
	 * ArgumentGroup - is a grouping of flag and its values or subflags created as
	 * part of lexing the argument flags.
	 * @param flag - is a string indicating the field/argument of interest.
	 * @param flagValues - is one or more values that could be subflags.
	 * @throws IlleagalArgumentException is thrown as a function of groupArguments.
	 * 	 @see ArgumentGroup.groupArguments
	 */
	private ArgumentGroup(String flag, String[] flagValues) throws IlleagalArgumentException {
		this.flag = flag;
		if(containsSubFlags(flagValues)) {
			this.subFlags = groupArguments(getFlagDepthFor(flag) +1,flagValues);
		} else {
			this.flagValues = flagValues;
		}	
	}

	/**
	 * groupArguments - recursively processes the flags and values to provide a List of nested 
	 * argument groups used to marshel data into pojos.
	 * @param args - is a list of string arguments consisting of flags and one or more corrisponding values.
	 * @return a list of argument groups.
	 * @throws IlleagalArgumentException is thrown when input does not start with a flag or if no value
	 * is provided after a flag.
	 */
	public static List<ArgumentGroup> groupArguments(String[] args) throws IlleagalArgumentException {
		return groupArguments(ROOT_FLAG_DEPTH, args);
	}
	
	/**
	 * groupArguments - recursively processes the flags and values to provide a List of nested 
	 * argument groups used to marshel data into pojos.
	 * @param args - is a list of string arguments consisting of flags and one or more corrisponding values.
	 * @return a list of argument groups.
	 * @throws IlleagalArgumentException is thrown when input does not start with a flag or if no value
	 * is provided after a flag.
	 */
	private static List<ArgumentGroup> groupArguments(int flagDepth, String[] args) throws IlleagalArgumentException {
		List<ArgumentGroup> groups = new LinkedList<ArgumentGroup>();
		
		for (int startIndex = 0; startIndex < args.length; ) {
			int flagStart = findNextFlag(startIndex, flagDepth, args);
			int flagEnd = findNextFlag(startIndex+1, flagDepth, args);
			
			if(startIndex != flagStart) {
				throw new IlleagalArgumentException("Argument input must begin with one '-' for each argument level.\nThe argument " + args[startIndex] + " is invalid." );
			}
			if(flagStart +1 == flagEnd ) {
				throw new IlleagalArgumentException("The flag \'" + args[startIndex] + "\' must have a value after the flag.");
			}
			if(flagEnd == -1) {
				flagEnd = args.length;
			}
			groups.add(new ArgumentGroup(args[flagStart], Arrays.copyOfRange(args, flagStart+1, flagEnd)));
			startIndex = flagEnd;
		}
		return groups;
	}

	/**
	 * findNextFlag - scans the arguments for a flag with the corrisponding number of '-' to its
	 * depth.
	 * @param startIndex - is the index to start searching for the next flag.
	 * @param depth - is the expected number of '-'
	 * @param args is an array of arguments to be searched for a flag.
	 * @return the index of the next flag in the args array or -1 otherwise.
	 */
	private static int findNextFlag(int startIndex, int depth, String[] args) {
		String pattern = "^[\\-]{" + depth + "}[A-Za-z].*$";
		for(int dashesAdded = 0; dashesAdded < depth; dashesAdded++) {
			
		}
		
		for(int index = startIndex; index < args.length; index++) {
			if(Pattern.matches(pattern, args[index])) {
				return index;
			}
		}
		return -1;
	}

	/**
	 * getFlagDepthFor - is a method that counts up the leading '-' values which are used to indicate
	 * flag depth for complex objects.
	 * @param flag is a string containing a flag level
	 * @return the flagDepth by counting '-' characters.  If no leading '-' exists then the string is
	 * not a flag.
	 */
	public static int getFlagDepthFor(String flag) {
		int flagDepth = 0;
		for(char currentChar : flag.toCharArray()) {
			if(currentChar == '-') {
				flagDepth++;
			} else {
				break;
			}
		}
		return flagDepth;
	}
	
	/**
	 * valueIsAFlag - is a method that classifies a string as a flag or not.
	 * @param value - is a string that is examined to indicate if it is a flag.
	 * @return true if the value has one more leading '-' and false otherwise.
	 */
	private boolean valueIsAFlag(String value) {
		int flagDepth = getFlagDepthFor(value);
		if(flagDepth == this.NOT_A_FLAG) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * containsSubFlags - is a convenince method to detect subflags.
	 * @param flagValues is an array of string tokens which may have a flag.
	 * @return true if a token starts with one or more '-' and false otherwise.
	 */
	private boolean containsSubFlags(String[] flagValues) {
		for(String flagValue : flagValues) {
			if(valueIsAFlag(flagValue)) {
				return true;
			}
		}
		return false;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String[] getFlagValues() {
		return flagValues;
	}

	public void setFlagValues(String[] flagValues) {
		this.flagValues = flagValues;
	}

	public List<ArgumentGroup> getSubFlags() {
		return subFlags;
	}

	public void setSubFlags(List<ArgumentGroup> subFlags) {
		this.subFlags = subFlags;
	}

	@Override
	public String toString() {
		return "ArgumentGroup [flag=" + flag + ", flagValues=" + Arrays.toString(flagValues) + ", subFlags=" + subFlags
				+ "]";
	}
	
	
}
