package app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.URL;

public class Dictionary {

	public BufferedReader breader = null;
	public FileReader freader = null;
	public String readPath = "", writePath = "";
	//construct default file path
	public Dictionary()
	{
		readPath = "F:/Local Workspace/TrieAutoSuggestSpellCheck/WebContent/WEB-INF/data/wordlist.txt";
		writePath = "F:/Local Workspace/TrieAutoSuggestSpellCheck/WebContent/WEB-INF/data/wordlist2.txt";
	}
	
	public Dictionary(String filepath)
	{
		readPath = filepath;
	}
	/**
	 * read all words from dictionary and print
	 */
	public TrieUtil ReadDictionary(TrieUtil trieUtil)
	{
		try {
			freader = new FileReader(readPath);
			breader = new BufferedReader(freader);
			String sCurrentLine;

			
			while ((sCurrentLine = breader.readLine()) != null) {
				sCurrentLine = sCurrentLine.toLowerCase().replaceAll("[^a-z ]", "");
				if(sCurrentLine.matches("^[a-zA-Z]*$")) {
					trieUtil.insert(sCurrentLine);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (breader != null)
					breader.close();
				if (freader != null)
					freader.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}	
		return trieUtil;
	}
	
	/**
	 * write a word into the dictionary
	 * @param word
	 */
	public boolean WriteDictionary(String word)
	{
		 try {  
			 RandomAccessFile randomFile = new RandomAccessFile(readPath, "rw");   
			 long fileLength = randomFile.length();
			 randomFile.seek(fileLength);
			 randomFile.writeBytes("\n"+word);
			 randomFile.close();
			 return true;
		 } catch (Exception e) {  
			 e.printStackTrace();  
			 System.out.println("writting failure");  
			 return false;
		 }         
	}
	
	/**
	 * delete a word from the dictionary
	 * @param word
	 */
	public boolean DeleteFromDictionary(String word)
	{
		boolean successful  = false;
		File inputFile = new File(readPath);
		File tempFile = new File(writePath);

		 try {  
			 	
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

			String lineToRemove = word;
			String currentLine;
			boolean first = true;
			
			while((currentLine = reader.readLine()) != null) {
			    String trimmedLine = currentLine.trim();
			    if(trimmedLine.equals(lineToRemove)) continue;
			    if(first) {
			    	writer.write(currentLine);
			    	first = false;
			    } else
			    	writer.write(System.getProperty("line.separator") + currentLine);
			}
			writer.close(); 
			reader.close(); 
			inputFile.delete();
			successful = tempFile.renameTo(inputFile);
        } catch (Exception e) {  
            e.printStackTrace();  
            System.out.println("Deleting failure");  
            successful = false;
        } finally {
        	tempFile.renameTo(inputFile);
        }
		 
		 return successful;
	}

	
	
//	long begintime = System.currentTimeMillis();	
//	 long begintime2 = System.currentTimeMillis();
//	 System.out.println(begintime2 - begintime);
}
