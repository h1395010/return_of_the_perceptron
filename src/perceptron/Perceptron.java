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
	  
      static final String LABEL = "atheism";
      //static final String LABEL = "sports";
      //static final String LABEL = "science";
	  
	  public static void perceptron( Table< int[] , String , Integer > train_freq_count_against_globo_dict,
			  					     Table< int[] , String , Integer > test_freq_count_against_globo_dict,
			  						 Set<String> GLOBO_DICT )
	  {
		  int globo_dict_size = GLOBO_DICT.size();
		  int number_of_files__train = train_freq_count_against_globo_dict.size();
		  
		  double[] weights = new double[ globo_dict_size + 1 ];//one for bias
		  for (int i = 0; i < weights.length; i++) 
		  {
            weights[i] = randomNumber(0,1);
		  }
		    
		  		    

		   double[][] feature_matrix__train = new double[ number_of_files__train ][ globo_dict_size ];
		   int[] outputs__train = new int [ number_of_files__train ];
		    
		   int z = 0;
		   for ( Cell< int[] , String , Integer > cell: train_freq_count_against_globo_dict.cellSet() )
		   {			   
			   int[] container_of_feature_vector = cell.getRowKey();
			   //System.out.println( Arrays.toString( container_of_feature_vector ) );
			   
			   for (int q = 0; q < globo_dict_size; q++) 
	           {
				   feature_matrix__train[z][q] = container_of_feature_vector[q];
	           }
			   outputs__train[z] = String.valueOf( cell.getColumnKey() ).equals(LABEL) ? 1 : 0;
	           
	           z++;
		   }
		   //System.out.println( Arrays.toString( outputs ) );
		    
		   
		   
		  
		  //LEARNING WEIGHTS
		  double localError, globalError;
		  int p, iteration, output;
		
		  iteration = 0;
		  do 
		  {
			  iteration++;
			  globalError = 0;
			  //loop through all instances (complete one epoch)
			  for (p = 0; p < number_of_files__train; p++) 
			  {
				  // calculate predicted class
				  output = calculateOutput( theta, weights, feature_matrix__train, p, globo_dict_size );
				  // difference between predicted and actual class values
				  localError = outputs__train[p] - output;
				  //update weights and bias
				  for (int i = 0; i < globo_dict_size; i++) 
				  {
					  weights[i] += ( LEARNING_RATE * localError * feature_matrix__train[p][i] );
				  }
				  weights[ globo_dict_size ] += ( LEARNING_RATE * localError );
				  
				  //summation of squared error (error value for all instances)
				  globalError += (localError*localError);
			  }

			  /* Root Mean Squared Error */
			  if (iteration < 10) 
				  System.out.println("Iteration 0" + iteration + " : RMSE = " + Math.sqrt( globalError/number_of_files__train ) );
			  else
				  System.out.println("Iteration " + iteration + " : RMSE = " + Math.sqrt( globalError/number_of_files__train ) );
			  //System.out.println( Arrays.toString( weights ) );
		  } 
		  while(globalError != 0 && iteration<=MAX_ITER);
		  
		  
		  /*
		    for(int j = 0; j < 10; j++){
	        double x1 = randomNumber(-10 , 10);
	        double y1 = randomNumber(-10 , 10);   
	        double z1 = randomNumber(-10 , 10); 
	      
	        output = calculateOutput(theta,weights, x1, y1, z1);
	        System.out.println("\n=======\nNew Random Point:");
	        //System.out.println("x = "+x1+",y = "+y1+ ",z = "+z1);
	        System.out.println("class = "+output);
	        */
	        
	        
	       int number_of_files__test = test_freq_count_against_globo_dict.size();
	       double[][] feature_matrix__test = new double[ number_of_files__test ][ globo_dict_size ];
	        
		   //i don't actually need this info, but something to clarify the output would be great
		   String[] test_file_true_label = new String [ number_of_files__test ];
		    
		   int x = 0;
		   for ( Cell< int[] , String , Integer > cell: test_freq_count_against_globo_dict.cellSet() )
		   {			   
			   int[] container_of_feature_vector__test = cell.getRowKey();
			   //System.out.println( Arrays.toString( container_of_feature_vector ) );
			   
			   for (int q = 0; q < globo_dict_size; q++) 
	           {
				   feature_matrix__test[x][q] = container_of_feature_vector__test[q];
	           }
			   test_file_true_label[x] = (String)( cell.getColumnKey() );
	           
	           x++;
		   }
		   //System.out.println( Arrays.toString( outputs ) );
		   System.out.println();
		   
		   
		   double tp = 0.0;
		   double fp = 0.0; 
		   double tn = 0.0;
		   double fn = 0.0;
		   
		  for (p = 0; p < number_of_files__test; p++) 
		  {
			  int predicted_class = calculateOutput( theta, weights, feature_matrix__test, p, globo_dict_size );
		      //System.out.println("predicted class = " + predicted_class );
		      
		      int actual_class = ( test_file_true_label[p] ).equals(LABEL) ? 1 : 0;
		      
		      //System.out.println( "actual class = " + actual_class );
		      
		      //System.out.println( "actual class = " + test_file_true_label[p] );
		      
		      //System.out.println();
		      
		      if( actual_class == 1 && predicted_class == 1 )
		    	  tp++;
		      if( actual_class == 1 && predicted_class == 0 )
		    	  fn++;
		      if( actual_class == 0 && predicted_class == 1 )
		    	  fp++;
		      if( actual_class == 0 && predicted_class == 0 )
		    	  tn++;   
		  }
		  
		  System.out.println( "tp: " + tp );
		  System.out.println( "fp: " + fp );
		  System.out.println( "tn: " + tn );
		  System.out.println( "fn: " + fn );
		  System.out.println();
		  
		  double precision = tp / (tp + fp);
		  System.out.println( "precision = " + precision );
		  
		  double recall = tp / (tp + fn);
		  System.out.println( "recall = " + recall );
		  
		  double f_measure = ( 2 * ( precision * recall ) ) / ( precision + recall );
		  System.out.println( "f_measure = " + f_measure );
		  
		  System.out.println();
		  System.out.println();
		  //put in some shit about F-measure and whatnot
		  
		  
		  
		  
		  
		  
		  
		  
		  

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
