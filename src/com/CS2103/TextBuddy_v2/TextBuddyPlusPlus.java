package com.CS2103.TextBuddy_v2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
 * National University of Singapore
 * The MIT License (MIT)
 *
 * Copyright (c) 2014. Kenneth Ham Gao Jie (A0111875E)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * CE2: TextBuddyPlusPlus
 * F10-2J
 * Name: Kenneth Ham Gao Jie
 * Matriculation Number: A0111875E
 *
 * This program reads in an existing file name, and updates the file according to commands specified by the user.
 * The 'TextBuddy' command initiates the running of the program.
 * The 'display' command shows all strings stored in the text file.
 * The 'add' command adds a new specified string into the text file.
 * The 'delete' command deletes a particular string from the file, specified by the user as the number position of the string in the text file.
 * The 'clear' command deletes all strings from the text file.
 * The 'exit' command exits user from the program.
 *
 * This program will save all updated content back into the user's original file after each command user chooses to execute.
 *
 * [Program Assumptions]
 * 1. File name specified by the user is an existing file
 * 2. Attempt to delete an element of ID smaller than 0 or
 *    greater than the list size will result in an "Invalid Element ID"
 */

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TextBuddyPlusPlus {
	private static final String APP_COMMAND = "TextBuddy";
	private static final String MSG_WELCOME = "Welcome to TextBuddy. %s is ready for use\n";
	private static final String COMMAND = "command: ";
	private static final String MSG_LIST_EMPTY = "%s is empty\n";
	private static final String MSG_PRINT_LIST = "%d. %s\n";
	private static final String MSG_ADDED_ELEMENT = "added to %s: \"%s\"\n";
	private static final String MSG_DELETE_ELEMENT = "delete from %s: \"%s\"\n";
	private static final String MSG_CLEAR_LIST = "all content deleted from %s\n";
	private static final String MSG_ERROR_SAVING = "Error encountered when saving %s\n";
	private static final String MSG_ERROR_READING = "Error encountered when reading %s\n";
	private static final String MSG_INVALID_COMMAND = "Invalid command!\n";
	private static final String MSG_INVALID_ELEID = "Invalid element ID\n";

	// Points to current directory
	private static final String CURRENT_DIRECTORY = System.getProperty("user.dir") + "/";
	private static final String TXT_EXTENSION = ".txt";

	// Booleans used when saving file
	private static final boolean FILE_SAVE_SUCCESSFUL = true;
	private static final boolean FILE_SAVE_UNSUCCESSFUL = false;

	// Enumerated types of command
	enum COMMAND_TYPE {
		DISPLAY, ADD, DELETE, CLEAR, INVALID, EXIT
	};

	private static final ArrayList<String> list = new ArrayList<String>();
	private static final BufferedReader buffered_reader = new BufferedReader(new InputStreamReader(System.in));
	private static String file_name;

	/**
	 * Initialize TextBuddy
	 * Usage: java TextBuddy <file_name>
	 * @param args        Input parameters to execute TextBuddy.
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		TextBuddy(args);
		processUserCommands();
	}

	private static void TextBuddy(String[] args) {
		/* Check for filename in program input parameter
		 * If COMMAND equals to "TextBuddy", program is valid.
		 * Else, program will exit because of invalid command.
		 */
		if (args.length > 0) {
			if(!args[0].equals(APP_COMMAND)){
				print(MSG_INVALID_COMMAND);
				System.exit(0);
			} else if (args.length < 2) {
				print(MSG_INVALID_COMMAND);
				System.exit(0);
			} else {
				file_name = args[1];
				checkIfFileExists();
				print(String.format(MSG_WELCOME, CURRENT_DIRECTORY + file_name));
			}
		} else {
			file_name = getDateTime().concat(TXT_EXTENSION);
		}
	}

	/**
	 * Gets the current date and time.
	 *
	 * @return The current date and time in "dd-MMM-HH-mm" format.
	 */
	private static String getDateTime() {
		SimpleDateFormat dt_format = new SimpleDateFormat("dd-MMM-HH-mm");

		return (dt_format.format(new Date()));
	}

	/**
	 * Check if file exists
	 * If file exists, read the file contents.
	 * todo: Else create a new file
	 */
	private static void checkIfFileExists() {
		try {
			File file = new File(file_name);

			if (!file.exists()) {
				createFileIfNotExist(file);
			} else {
				/* Read in the file contents
				 * add it to the list
				*/
				String _line;
				BufferedReader br = new BufferedReader(new FileReader(file_name));

				while ((_line = br.readLine()) != null) {
					list.add(_line);
				}

				br.close();
			}
		} catch (Exception ex) {
			list.clear(); // Clear all data
			print(String.format(MSG_ERROR_READING, file_name));
		}
	}

	/**
	 * createFileIfNotExist:
	 *
	 * @param n_file        Receive File to be processed
	 * @throws IOException
	 */
	private static void createFileIfNotExist(File n_file) throws IOException {
		n_file.createNewFile();
	}

	/**
	 * processUserCommands:
	 * Read commands from each line and execute the commands accordingly
	 * DISPLAY: displays all lines in the file
	 * ADD: adds a line to the file
	 * DELETE: delete a line from the file
	 * CLEAR: clear the entire file
	 * EXIT: exit the program
	 * INVALID COMMAND: anything that doesn't resemble the commands above
	 *
	 * @throws IOException
	 */
	private static void processUserCommands() throws IOException {
		String print_out = "";

		while(true) {
			print(COMMAND);

			try {
				/* Split the commands into two arguments.
				 * cmd[0] represents COMMAND argument
				 * cmd[1] represents the argument to be parsed into
				*/
				String[] cmd = buffered_reader.readLine().trim().split(" ", 2);
				COMMAND_TYPE cmd_type = determineCommandType(cmd[0]);

				switch (cmd_type) {
					case DISPLAY :
						print_out = displayFile();
						break;

					case ADD :
						print_out = addElement(cmd[1]);
						break;

					case DELETE :
						print_out = deleteElement(cmd[1]);
						break;

					case CLEAR :
						print_out = clearList();
						break;

					case EXIT :
						System.exit(0);
						break;

					default :
						print(MSG_INVALID_COMMAND);
						break;
				}
			} catch (Exception ex) {
				print_out = MSG_INVALID_COMMAND;
			}

			print(print_out);
		}
	}

	/**
	 * This operation determines which of the supported command types the user
	 * wants to perform
	 *
	 * @param command_type_str is the first word of the user command
	 */
	private static COMMAND_TYPE determineCommandType(String command_type_str) {
		if (command_type_str == null) {
			throw new Error("Command type string cannot be null!");
		}

		if (command_type_str.equalsIgnoreCase("display") || command_type_str.equalsIgnoreCase("ls")) {
			return COMMAND_TYPE.DISPLAY;
		} else if (command_type_str.equalsIgnoreCase("add") || command_type_str.equalsIgnoreCase("+")) {
			return COMMAND_TYPE.ADD;
		} else if (command_type_str.equalsIgnoreCase("delete") || command_type_str.equalsIgnoreCase("rm") || command_type_str.equalsIgnoreCase("-")) {
			return COMMAND_TYPE.DELETE;
		} else if (command_type_str.equalsIgnoreCase("clear")) {
			return COMMAND_TYPE.CLEAR;
		} else if (command_type_str.equalsIgnoreCase("exit") || command_type_str.equalsIgnoreCase("quit") || command_type_str.equalsIgnoreCase("q")) {
			return COMMAND_TYPE.EXIT;
		} else {
			return COMMAND_TYPE.INVALID;
		}
	}

	/**
	 * addElement:
	 * add line to an ArrayList<String>
	 *
	 * @param _element      Input element to be added
	 * @return              Success message when element added or error while saving
	 */
	private static String addElement(String _element) {
		list.add(_element);

		boolean isFileSaveSuccessful = saveFile();

		if (isFileSaveSuccessful) {
			return String.format(MSG_ADDED_ELEMENT, file_name, _element);
		} else {
			return String.format(MSG_ERROR_SAVING, file_name);
		}
	}

	/**
	 * saveFile:
	 * Saves ArrayList<String> elements to file
	 *
	 * @return      True or false while saving file -> Success or failure in saving
	 */
	private static boolean saveFile() {
		try {
			FileWriter file = new FileWriter(file_name);
			String output = "";

			for (String _line : list) {
				output = output + _line + "\n";
			}

			file.write(output);
			file.flush();
			file.close();
		} catch (IOException e) {
			return FILE_SAVE_UNSUCCESSFUL;
		}

		return FILE_SAVE_SUCCESSFUL;
	}

	/**
	 * displayFile:
	 * Displays all lines found in the text file.
	 * If list is empty, return list empty message, otherwise, print out the entire list of text
	 * @return      Entire data entries list
	 * @throws IOException
	 */
	private static String displayFile() throws IOException {
		StringBuffer strbuffer = new StringBuffer();

		if (list.isEmpty()) {
			System.out.printf(MSG_LIST_EMPTY, file_name);
		} else {
			for (int i = 0; i < list.size(); i++) {
				strbuffer.append(String.format(MSG_PRINT_LIST, (i + 1), list.get(i)));
			}
		}

		return strbuffer.toString();
	}

	/**
	 * deleteElement:
	 * deletes a single index line from the file
	 * @param param     Input element to be deleted
	 * @return          Delete successful and saved or error deleting and saving
	 */
	private static String deleteElement(String param) {
		int id;
		int _index;

		// Parse the element ID
		try {
			id = parseID(param);
		} catch (Exception e) {
			return String.format(MSG_INVALID_COMMAND);
		}

		// Check if ID is valid
		if (id > 0 && list.size() >= id) {
			_index = id - 1; // 0 based indexing list
			String element = list.get(_index);
			list.remove(_index);

			boolean isFileSaveSuccessful = saveFile();

			if (isFileSaveSuccessful) {
				return String.format(MSG_DELETE_ELEMENT, file_name, element);
			} else {
				return String.format(MSG_ERROR_SAVING, file_name);
			}
		} else if (list.isEmpty()) {
			return String.format(MSG_LIST_EMPTY, file_name);
		} else {
			return String.format(MSG_INVALID_ELEID);
		}
	}

	/**
	 * clearList:
	 * clears the data entries of the entire file
	 * @return        File cleared successfully or error saving
	 * @throws IOException
	 */
	private static String clearList() throws IOException {
		list.clear();

		boolean isFileSaveSuccessful = saveFile();

		if (isFileSaveSuccessful) {
			return String.format(MSG_CLEAR_LIST, file_name);
		} else {
			return String.format(MSG_ERROR_SAVING, file_name);
		}
	}

	/**
	 * Parse the parameter string into integer preparing for element deletion.
	 *
	 * @param param 	User entered parameter
	 * @return 			Element ID as entered by user if valid
	 * @throws 			Exception When invalid element ID entered
	 */
	private static int parseID(String param) throws Exception {
		try {
			return Integer.parseInt(param.split(" ", 2)[0]);
		} catch (Exception e) {
			throw e;
		}
	}

	private static void print(String output) {
		System.out.print(output);
	}
}
