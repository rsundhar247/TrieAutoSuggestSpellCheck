/*
================================================================================================================
Project - Spell Check and AutoSuggest using Trie

Trie also known as prefix tree is used to store a dynamic set or associative array,
where the keys are usually strings

Key Features - Insert, Delete, Search, Spell Check, Auto Suggest
================================================================================================================
*/

package app;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class SpellCheck  extends HttpServlet{
	private static final long serialVersionUID = 1L;
	static TrieUtil trieUtil = new TrieUtil();

	public SpellCheck() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
        // Writing the message on the web page      
        PrintWriter out = response.getWriter();      
        out.println("<p>" + "Hello Friends!" + "</p>");  
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = response.getWriter();
		String json = null;
		
		int option = Integer.parseInt(request.getParameter("option"));
		
		//Loading the list of words from dictionary		
		Dictionary dic = new Dictionary();
		trieUtil = dic.ReadDictionary(trieUtil);
		
		
		if(option == 1) {
			HashMap<String,ArrayList<String>> respMap = new HashMap<String,ArrayList<String>>();
			String sentence = request.getParameter("sentence");
			ArrayList<String> inValidWords = spellCheck(sentence);
			if(inValidWords.size() > 0)
				respMap.put("invalid",inValidWords);
			EditDist editDist = new EditDist();
			for(int i = 0; i<inValidWords.size(); i++) {
				respMap.put(inValidWords.get(i), editDist.Suggestion(inValidWords.get(i)));
			}
			json = new Gson().toJson(respMap);
			
		} else if(option == 2) {
			String suggest = request.getParameter("suggestion");
			ArrayList<String> suggestion = trieUtil.autoSuggest(suggest);
			json = new Gson().toJson(suggestion);
			
		} else if(option == 3) {
			String msg = "";
			boolean fullAccess = (request.getParameter("user").equals("rajsu") || request.getParameter("pass").equals("pass123")) ? true : false;
			if(! fullAccess) {
				msg = "No Access";
			} else {
				String insert = request.getParameter("word");
				insert = insert.trim().toLowerCase().replaceAll("[^a-z ]", "");
				
				try{
					boolean notExist = trieUtil.insert(insert);
					if(notExist) {
						boolean flag = dic.WriteDictionary(insert);
						if(!flag) {
							msg = "Error";
							System.out.println("Error in inserting the word. Please make sure its a valid alphabet only word.");
						} else {
							msg = "Success";
							System.out.println(insert+" is inserted into the dictionary.");
						}
					} else {
						msg = "Exists";
						System.out.println(insert+" already exists in the dictionary.");
					}
				} catch(Exception e) {
					msg = "Error";
					System.out.println("Error in inserting the word. Please make sure its a valid alphabet only word.");
					e.printStackTrace();
				}
			}
			json = new Gson().toJson(msg);
			
		} else if(option == 4) {
			String msg = "";
			boolean fullAccess = (request.getParameter("user").equals("rajsu") || request.getParameter("pass").equals("pass123")) ? true : false;
			if(! fullAccess) {
				msg = "No Access";
				System.out.println("You don't have permission to delete a word. Get a Teacher permission.");
			} else {
				String delete = request.getParameter("word");
				delete = delete.trim().toLowerCase().replaceAll("[^a-z ]", "");
				
				try{
					boolean exists = trieUtil.search(delete);
					if(exists) {
						trieUtil.delete(delete);
						boolean flag = dic.DeleteFromDictionary(delete);
						if(!flag) {
							msg = "Error";
							/*System.out.println("Error in deleting the word. Please make sure its a valid alphabet only word.");*/
						} else {
							msg = "Success";
							/*System.out.println(delete+" is deleted from the dictionary.");*/
						}
					} else {
						msg = "NotExist";
						System.out.println(delete+" doesn't exist in the dictionary.");
					}
				} catch(Exception e) {
					msg = "Error";
					System.out.println("Exception in deleting the word. Please make sure its a valid alphabet only word.");
					e.printStackTrace();
				}
			}
			json = new Gson().toJson(msg);
		}
		
		pw.write(json);
	}
	
	/*
	 * This function is used to spellCheck a given sentence.
	 */
	public static ArrayList<String> spellCheck(String input) {
		
		boolean isValid = false;
		ArrayList<String> notValid = new ArrayList<String>(); // Used to store the invalid words (or) misspelled words
		String[] wordList = input.trim().toLowerCase().replaceAll("[^a-z ]", "").split(" "); // replaceAll function is used to ignore all the special characters (user's will be taken as users)
																							// We then split the sentence into an array of words, seperated by space
		for(int i=0; i<wordList.length; i++) {
			if(! wordList[i].isEmpty()) {
				isValid = trieUtil.search(wordList[i]); // For each word, we call search function in trie. It returns true if the word is present in the trie, false otherwise.
				if(! isValid) {
					notValid.add(wordList[i]);
				}
			}
		}
		
		return notValid;
	}
}