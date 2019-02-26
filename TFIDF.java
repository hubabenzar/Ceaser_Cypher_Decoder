//Imports
import java.io.*;
import java.util.*;

//Start of class
public class TFIDF {
	//Attributes
	protected String docName;
	private HashMap<String,Double> wordCount;  //  list of (word, count) pairs

//Method or calculating TFIDF
//COMMENT IS VERY SIMILAR TO THE SECOND HASH MAP!
	private static ArrayList<HashMap<String, Double>> getHashMaps(String[] fileNames) {
		ArrayList<HashMap<String, Double>> arrayList = new ArrayList<>(0);
		for (String docName1 : fileNames) {
			Scanner infile;
			String line;
			File in = new File(docName1);
			try {
				HashMap<String, Double> wordCount = new HashMap<>();
				infile = new Scanner(in);  //  file may or may not exist
				while (infile.hasNextLine()) {
					line = infile.nextLine();
					line = line.toLowerCase();  // convert to lower case
					//  Split the string into component words using
					//  a regular expression.
					String[] termFreq = line.split("[^\\p{L}]+");
					for (String term : termFreq) {
						if (term != null && !term.isEmpty()) {
							wordCount.put(term, wordCount.getOrDefault(term, 0D) + 1D);
						}
					}
				}
				arrayList.add(wordCount);
			}
			
			//  If the file doesn't exist, print out the error message.
			catch (FileNotFoundException fnfe) {
				System.out.println(fnfe);
			}
			
		}
		return arrayList;
	}
	
//Construtor method
   	//  The constructor actually does most all of the job, i.e.
	//  it loads the file and constructs the HashMap of the
	//  word counts. 
	public TFIDF(String docName)  {
		this.docName = docName;																		//name of the document
		this.wordCount = new HashMap<String,Double>();  											//empty HashMap
		Scanner infile;																				//used to read the contents of the file
		String line;																				//represent the newest line of the file
		File in = new File(docName);																//Set in to an actual file
		try {																						//trying to read the file
			infile = new Scanner(in);  																//used to read the contents of the file
			while(infile.hasNextLine()){															//it reads thought the file, it keeps looking for a line while the file has a line
				line = infile.nextLine();															//reads the next line
				line = line.toLowerCase();  														//convert to lower case
				String[] termFreq = line.split("[^\\p{L}]+");  									//Split the string into component words using a regular expression
				/* Refering to the exoression from lecutre notes from Week 7
				This is an example where I am using Unicode, in particular, I am looking for a non-(Unicode)"letter" or number (or a sequence of them, such as a period followed by spaces, etc) on which to perform
				a split.  So an apostrophe will count, and will trigger a split.  (Unicode letters include "normal" English letters, as well as other letters in European languages that have
				accents, umlauts, etc, and other letters in other languages such as Japanese, Chinese, etc.)
				*/
				for (String term : termFreq)  {
				    if ( term != null && !term.isEmpty() ){
						wordCount.put(term, wordCount.getOrDefault(term, 0D) + 1D);					//goes through the array of the split //goes through the array and increments the value for the word if the word isn't in the hasmap then it puts 1
					}
				}
			}

			double termTotal = 0;																	//Counter of total terms
			//it replaces the current highest with the current one if it is bigger
			HashMap.Entry<String, Double> highest = wordCount.entrySet().stream().findFirst().get();
			for(HashMap.Entry<String, Double> entry : wordCount.entrySet()) {
				termTotal += entry.getValue();
				if (entry.getValue() > highest.getValue()) {
					highest = entry;
				}
			}
			//goes through the filenames and prints them out along with the word with the highest TFIDF as well as the TFIDF
			System.out.println("==========\n" + docName + "\n==========");
			System.out.println(highest.getKey() + " " + (highest.getValue() / termTotal));
			//System.out.println("Total number of terms is " + termTotal + ".\n");
			/*
			TESTING CASES
			System.out.println("termTotal "+termTotal);
			System.out.println("termTotal "+termTotal);
			*/

		
		}
      
	  //  If the file doesn't exist, print out the error message.  
		catch (FileNotFoundException fnfe) {
			System.out.println(fnfe);
		}
	}//  end of constructor

//Start of Main method
	public static void main (String args[]) {
//Error State
		//If there is no entry it comes here
		if(args.length == 0){																							//When there is no string inserted it displays this message
			System.out.println("Oops! Please enter a document name to the program!");									//It's more efficient to have the error state at the top of the code as the computer won'the
			return;																										//I also used my not true bracket as I thaught it would make it more unique
		}
//Working State
		//Checks if the number of inputs is equal to one
		//This is where it executes the TF calculations.
		if (args.length == 1) {
			System.out.println("Max TF value for this file.\n");
			new TFIDF(args[0]);
		} 
		//If there are multiple entrys it comes here
		else {
			System.out.println("Max TFIDF values for each file.\n");													//Message
			ArrayList<HashMap<String, Double>> list = getHashMaps(args);
			HashMap<String, Double> invDocFreq = new HashMap<>();														//procceses the files and returns an arraylist of hashmaps for each file
			String[] max = new String[args.length];																		//storing the words with the highest tfidf values in each file
			double[] sums = new double[args.length];																	//the number of words in each file, we need that for the TF's of every file
			for (int index = 0; index < list.size(); index++) {															//loops for each array entry
				for (String word : list.get(index).keySet()) {															//the inner part calculates the number of documents each word is in
					invDocFreq.put(word, invDocFreq.getOrDefault(word, 0D) + 1D);										//adds everytime it sees a word is in a document
					sums[index] += list.get(index).get(word);															//adds to the number of words in each document
				}
			}
			invDocFreq.replaceAll((t, aDouble) -> Math.log10(((double) args.length) / aDouble));						//calculates the idf for each word and replaces the value that was in there already, s represents the word and the a Double is the number of documents that word is in
			for (int index = 0; index < list.size(); index++) {															//loops for each array entry
				final int n = index;																					//n gets assigned to index if this doesnt exist it throws this error "variable used in lambda expression should be final or effectively final
				list.get(index).replaceAll((word, termFrequency) -> (termFrequency / sums[n]) * invDocFreq.get(word));	//calculates the TFIDF values for every word and replaces the old numbers
				//lines 145 to 151 are used to calculate the the word with the highest TFIDF in each document
				max[index] =  "";																						//needed so we don't get a NullPointerException																		
				//goes through the words of each document and then replaces the max if it sees a word that has a higher TDIDF value
				for (String word : list.get(index).keySet()) {
					if (list.get(index).get(word) > list.get(index).getOrDefault(max[index], 0D)) {
						max[index] = word;
					}
				}
				//Calculation ends
			}
			//goes through the filenames and prints them out along with the word with the highest TFIDF as well as the TFIDF
			for (int index = 0; index < args.length; index++) {
				System.out.println("==========");
				System.out.println(args[index]);
				System.out.println("==========");
				System.out.println(max[index] + " " + list.get(index).get(max[index]) + "\n");
				/*
				TESTING CASE - PRINTS OUT ALL IDFs
				System.out.println("All the IDFs" + invDocFreq);
				/**/
			}
		}
	}
}