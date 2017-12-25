package app;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class EditDist {
	
	public ArrayList<String> Suggestion(String word)
	{
		String sugg = null;
		int min = Integer.MAX_VALUE;
		InputStream is = null;
		BufferedReader breader = null;
		PriorityQueue<PQueue> pQueue = new PriorityQueue<PQueue>();
		try {		
			//is = this.getClass().getResourceAsStream("wordlist.txt");
			is = new FileInputStream("F:/Local Workspace/TrieAutoSuggestSpellCheck/WebContent/WEB-INF/data/wordlist.txt");
			
			breader = new BufferedReader(new InputStreamReader(is));
			String sCurrentLine;
			while ((sCurrentLine = breader.readLine()) != null) {
				sCurrentLine = sCurrentLine.toLowerCase().replaceAll("[^a-z]", "");
				int distance = minDistance(word, sCurrentLine);
				min = distance;
				sugg = sCurrentLine;	
				pQueue.add(new PQueue(min, sugg)); 
				if(sCurrentLine.equals("sentence")) {
					System.out.println(distance);
				}
				if(pQueue.size() > 3) {
					pQueue.poll();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (breader != null)
					breader.close();
				if (is != null)
					is.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}	
		
		ArrayList<String> suggestion = new ArrayList<String>();
		int size = pQueue.size();
		for(int i = 0; i < size; i++) {
			suggestion.add(pQueue.remove().getWord());
		}
		return suggestion;
	}
	
	public int minDistance(String word1, String word2) {
        if (word1 == null && word2 == null)
            return 0;
        if (word1 == null || word1.length() == 0)
            return word2.length();
        if (word2 == null || word2.length() == 0)
            return word1.length();
            
        int[][] d = new int[word1.length() + 1][word2.length() + 1];
        for (int i = 0; i <= word1.length(); i ++) {
            d[i][0] = i;
        }
        for (int j = 0; j <= word2.length(); j ++) {
        	d[0][j] = j;
        }            
                
        for (int i = 1; i <= word1.length(); i ++) {
            for (int j = 1; j <= word2.length(); j ++) {
                int count = Integer.MAX_VALUE;
                if (word1.charAt(i - 1) == word2.charAt(j - 1))
                    count = d[i - 1][j - 1];
                else
                    count = d[i - 1][j - 1] + 1;
                
                d[i][j] = 
                    Math.min(Math.min(d[i - 1][j] + 1, d[i][j - 1] + 1), count);                
            }               
        }
        return d[word1.length()][word2.length()];
    }

  
}