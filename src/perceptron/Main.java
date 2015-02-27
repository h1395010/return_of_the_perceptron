package perceptron;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;

public class Main 
{
	static String PATH = "/home/matthias/Workbench/SUTD/ISTD_50.570/assignments/data/train";
	//static String PATH = "/home/matthias/Workbench/SUTD/ISTD_50.570/assignments/practice_data/data/train";
	
	//all the words in each file as index, the label as value
	//static Map< ArrayList<String> , String > train__list_of_file_words = new HashMap<>();
	//static Map< ArrayList<String> , String > test__list_of_file_words = new HashMap<>();
	
	//frequency count against global dictionary
	//static Map< int[] , String > train_freq_count_against_globo_dict = new HashMap<>();
	//static Map< int[] , String > test_freq_count_against_globo_dict = new HashMap<>();
	final static Table< int[] , String , Integer > train_freq_count_against_globo_dict = HashBasedTable.create();
	final static Table< int[] , String , Integer > test_freq_count_against_globo_dict = HashBasedTable.create();
	
	
	//try guava jellie
	final static Table< ArrayList<String>, String, Integer > train__list_of_file_words = HashBasedTable.create();
	final static Table< ArrayList<String>, String, Integer > test__list_of_file_words = HashBasedTable.create();
	
	
	//all of the words in all of the files
	static Set<String> GLOBO_DICT = new HashSet<String>();
	
	public static void main(String[] args) throws IOException 
	{
		//each of the diferent categories
		//String[] categories = { "/atheism", "/politics", "/science", "/sports"};
		String[] categories = { "/atheism", "/sports"};
		//String[] categories = { "/politics", "/science"};
		
		//cycle through all categories once to populate the global dict
		for(int cycle = 0; cycle <= 1; cycle++)
		{
			String general_data_partition = PATH + categories[cycle];
			
			File directory = new File( general_data_partition );
			GlobalDict.parse_files( directory , GLOBO_DICT  );
		}
		
		
		for(int cycle = 0; cycle <= 1; cycle++)
		{
			String general_data_partition = PATH + categories[cycle];
					
			File directory = new File( general_data_partition );

			EvaluateFiles.store_file_words_with_label( directory , GLOBO_DICT , train__list_of_file_words );	
		}
		
		
//		for ( Cell< ArrayList<String>, String, Integer > cell: train__list_of_file_words.cellSet() )
//		{
//		    System.out.println(cell.getRowKey()+" "+cell.getColumnKey()+" "+cell.getValue());
//		}
		
		
		EvaluateFiles.generate_word_frequency_count_struc( GLOBO_DICT, train__list_of_file_words, train_freq_count_against_globo_dict );
		
		
//		for ( Cell< int[] , String , Integer > cell: train_freq_count_against_globo_dict.cellSet() )
//		{
//		    System.out.println(Arrays.toString( cell.getRowKey() ) +" "+cell.getColumnKey()+" "+cell.getValue());
//		}
		
		Perceptron.perceptron(train_freq_count_against_globo_dict, GLOBO_DICT);
		
	}

}
