/*
================================================================================================================
Project - Spell Check and AutoSuggest using Trie

Trie also known as prefix tree is used to store a dynamic set or associative array,
where the keys are usually strings

Key Features - Insert, Delete, Search, Spell Check, Auto Suggest
================================================================================================================
*/

package app;

import java.util.ArrayList;

public class TrieUtil {

	int CHARACTER_MAX = 26; // Each node can have 26 children. A to Z.
	int SUGGESTION_COUNT = 5;
	int maxSuggestions = SUGGESTION_COUNT; // To restrict the maximum number of results returned. A - A, An, Apple, Able, America.
	
	/*
	 * This class is used to build object for each instance.
	 * It consists of pointers to 26 children and isEndOfWord boolean flag, 
	 * denoting the end of a word.
	 */
	class TrieNode {
		
		TrieNode children[] = new TrieNode[CHARACTER_MAX];
		boolean isEndOfWord; // flag used to maintain if the current node is a end node for any word
		
		public TrieNode() {
			for(int i=0; i<CHARACTER_MAX; i++) {
				children[i] = null;
			}
			isEndOfWord = false;
		}
	}
	
	TrieNode root = new TrieNode(); // Root with null/empty values, used as point of reference for future calls
	
	/*
	 * This function is used to insert words into the trie. 
	 */
	public boolean insert(String word) {
		boolean notExist = false;
		TrieNode currentNode = root;
		word = word.toLowerCase(); // converted to lowercase for consistency
		int length = word.length();
		char insert;
		
		for(int i=0; i<length; i++) {
			insert = word.charAt(i);
			TrieNode childNode = currentNode.children[insert-'a']; // insert-'a' -> this gives the index for the children array, Eg - a-0, b-1, c-2, z-26 etc.
			if(childNode == null) {
				currentNode.children[insert-'a'] = new TrieNode(); // If the child node is not present, we create a new child node. 
																// Suppose we insert apple, after checking for 'p' if its child 'l' is not present, then we create one.
				notExist = true;
			}
			currentNode = currentNode.children[insert-'a']; // move the current node to the child node
		}
		if(currentNode.isEndOfWord == false) {
			currentNode.isEndOfWord = true; // The last character of the word is marked as the end of the word
			notExist = true;
		}
		
		return notExist;
	}
	
	/*
	 * This function is used to search words from the trie. 
	 */
	public boolean search(String word) {
		TrieNode currentNode = root;
		word = word.toLowerCase();
		int length = word.length();
		char search;
		
		for(int i=0; i<length; i++) {
			search = word.charAt(i);
			if(currentNode.children[search-'a'] != null) { // Checking if the character is already present in the trie at the desired level.
				currentNode = currentNode.children[search-'a']; // if it is present, move to that node and check for its children further.
			} else {
				return false;
			}
		}
		if(currentNode.isEndOfWord) { // If the last character searched is an end of word, then the word is present in the trie.
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * This function is used to delete words from the trie.
	 * This deletes the word from the last character, ensuring no other words are deleted.
	 */
	public void delete(String word) {
		TrieNode currentNode = root;
		word = word.toLowerCase();
		int length = word.length();
		char delete;
		
		for(int i=0; i<length; i++) {
			delete = word.charAt(i);
			TrieNode childNode = currentNode.children[delete-'a']; // delete-'a' -> index of a character in the array
			if(childNode != null) {
				currentNode = currentNode.children[delete-'a'];
			}
			if(i==length-1) { // need to check only at the last character of the word
				if(currentNode.isEndOfWord) { // If there are 2 words, "an" & "ant" and we are to delete "an", we just have to delete isEndOfWord flag for 'n' node
					currentNode.isEndOfWord = false;
				}
				
				if(isFree(currentNode)) { // we delete a node only if it doesn't have any other children
					currentNode = null;
					delete(word.substring(0, length-1)); // while deleting "ant", if there are no child nodes for 't', we delete 't' node and pass "an" again.
				}
			}
		}
	}
	
	/*
	 * This function is used to check if the current node is free or have any pointers to any other child nodes.
	 */
	public boolean isFree(TrieNode node) {
		for(int i=0; i<CHARACTER_MAX; i++) {
			if(node.children[i] != null)
				return false;
		}
		return true;
	}
	
	/*
	 * This function is used to autosuggest words based on the input string. This is a recursive function.
	 * Eg - For "an" -> "an", "and", "ant", "antenna" are some suggestions.
	 */
	public ArrayList<String> autoSuggest(TrieNode currentNode, String word){
		
		ArrayList<String> idxList = new ArrayList<String>();
		String toSearch = "";
		TrieNode activeNode = new TrieNode();
		
		if(maxSuggestions>0) {
			for(int i=0; i<CHARACTER_MAX; i++) {
				activeNode = currentNode;
				if(maxSuggestions>0 && activeNode.children[i] != null) {
					activeNode = activeNode.children[i]; // from "an", traverse and explore further nodes, thus forming "and"
					char letter = (char) ('a'+i); // will contain 'd', as in the above "and" case
					toSearch = word;
					toSearch = toSearch + letter;
					if(activeNode.isEndOfWord) { // When the current node is a end of word node, then it means we have traversed a proper word and so we are adding it to the list
						idxList.add(toSearch);
						--maxSuggestions;
					}
					idxList.addAll(autoSuggest(activeNode, toSearch)); // recursive call to the same function
				} else if(maxSuggestions<=0) {
					break;
				}
			}
		}
		
		return idxList;
	}
	
	/*
	 * This function is used to autosuggest words based on the input string.
	 * Eg - For "an" -> "an", "and", "ant", "antenna" are some suggestions.
	 */
	public ArrayList<String> autoSuggest(String word) {
		
		ArrayList<String> idxList = new ArrayList<String>(); // ArrayList used to collect all the suggestions
		TrieNode currentNode = root;
		word = word.toLowerCase();
		int length = word.length();
		char search;
		
		for(int i=0; i<length; i++) { // For loop traverses the given word. When "an" is typed,  we traverse to 'a' and then to 'n' and check for further suggestions
			search = word.charAt(i);
			if(currentNode.children[search-'a'] != null) {
				currentNode = currentNode.children[search-'a']; // search-'a' -> gives the index of a character in the array
			} else {
				return null;
			}
		}
		
		if(currentNode.isEndOfWord) { // When the current node is a end of word node, then it means we have traversed a proper word and so we are adding it to the list
			idxList.add(word);
			--maxSuggestions; // Global variable to limit the number of suggestions. After adding the word to the list, we decrement it.
		}
		
		idxList.addAll(autoSuggest(currentNode,word)); // After traversing "an" we pass the current node and the word traversed so far to a recursive call.
		maxSuggestions = SUGGESTION_COUNT;
		return idxList;
	}
}