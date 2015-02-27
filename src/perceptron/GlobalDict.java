package perceptron;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

public class GlobalDict 
{
	public static void parse_files( File directory, Set<String> GLOBO_DICT) throws IOException 
	{
	    for (File file : directory.listFiles()) 
	    {
	    	String line; 
	    	BufferedReader br = new BufferedReader(new FileReader( file ));
	    		
	    	while((line = br.readLine()) != null) 
	    	{
	    		String[] words = line.split(" ");//those are your words

	    		populateDict( words, GLOBO_DICT );	
	    	}
	    }  
	}
	
	public static void populateDict( String[] words, Set<String> GLOBO_DICT ) throws IOException
	{
		String word;
		
		for (int i = 0; i < words.length; i++) 
		{
			word = words[i];
			if (!GLOBO_DICT.contains(word))
			{
				GLOBO_DICT.add(word);
			}
		}	
	}
}
