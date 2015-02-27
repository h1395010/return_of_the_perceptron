package perceptron;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;

public class Perceptron 
{
	  static int MAX_ITER = 100;
	  static double LEARNING_RATE = 0.1;           
	  static int theta = 0; 
	  
      //static final String LABEL = "atheism";
      //static final String LABEL = "sports";
      static final String LABEL = "science";
	  
	  public static void perceptron( Table< int[] , String , Integer > train_freq_count_against_globo_dict,
			  						 Set<String> GLOBO_DICT )
	  {
		  int globo_dict_size = GLOBO_DICT.size();
		  int number_of_files = train_freq_count_against_globo_dict.size();
		  
		  double[] weights = new double[ globo_dict_size + 1 ];//one for bias
		  for (int i = 0; i < weights.length; i++) 
		  {
            weights[i] = randomNumber(0,1);
		  }
		    
		  		    
		   double[][] feature_matrix = new double[ number_of_files ][ globo_dict_size ];
		   int[] outputs = new int [ number_of_files ];
		    
		   int z = 0;
		   for ( Cell< int[] , String , Integer > cell: train_freq_count_against_globo_dict.cellSet() )
		   {			   
			   int[] container_of_feature_vector = cell.getRowKey();
			   //System.out.println( Arrays.toString( container_of_feature_vector ) );
			   
			   for (int q = 0; q < globo_dict_size; q++) 
	           {
				   feature_matrix[z][q] = container_of_feature_vector[q];
	           }
	           outputs[z] = String.valueOf( cell.getColumnKey() ).equals(LABEL) ? 1 : 0;
	           
	           z++;
		   }
		   //System.out.println( Arrays.toString( outputs ) );
		    
		  
		  
		  double localError, globalError;
		  int p, iteration, output;
		
		  iteration = 0;
		  do 
		  {
			  iteration++;
			  globalError = 0;
			  //loop through all instances (complete one epoch)
			  for (p = 0; p < number_of_files; p++) 
			  {
				  // calculate predicted class
				  output = calculateOutput( theta, weights, feature_matrix, p, globo_dict_size );
				  // difference between predicted and actual class values
				  localError = outputs[p] - output;
				  //update weights and bias
				  for (int i = 0; i < globo_dict_size; i++) 
				  {
					  weights[i] += ( LEARNING_RATE * localError * feature_matrix[p][i] );
				  }
				  weights[ globo_dict_size ] += ( LEARNING_RATE * localError );
				  
				  //summation of squared error (error value for all instances)
				  globalError += (localError*localError);
			  }

			  /* Root Mean Squared Error */
			  System.out.println("Iteration "+iteration+" : RMSE = "+Math.sqrt(globalError/number_of_files));
			  //System.out.println( Arrays.toString( weights ) );
		  } 
		  while(globalError != 0 && iteration<=MAX_ITER);

	  }
	  
	  static int calculateOutput( int theta, double weights[], double[][] feature_matrix, int file_index, int globo_dict_size )
	  {
	     //double sum = x * weights[0] + y * weights[1] + z * weights[2] + weights[3];
		 double sum = 0;
		 
		 for (int i = 0; i < globo_dict_size; i++) 
		 {
			 sum += ( weights[i] * feature_matrix[file_index][i] );
		 }
		 //bias
		 sum += weights[ globo_dict_size ];
		 
	     return (sum >= theta) ? 1 : 0;
	  }
	  
	  public static double randomNumber(int min , int max) 
	  {
		  DecimalFormat df = new DecimalFormat("#.####");
		  double d = min + Math.random() * (max - min);
		  String s = df.format(d);
		  double x = Double.parseDouble(s);
		  return x;
	  }
}
