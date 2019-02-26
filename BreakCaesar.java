
import java.util.*;
public class BreakCaesar {
//Character Frequency Method  													//Contains all of the character frequencies given to us in the assignment.
	static double charFreq(int index){
		return new double[] {0.0855, 0.0160, 0.0316, 0.0387, 0.1210, 0.0218,
							 0.0209, 0.0496, 0.0733, 0.0022, 0.0081, 0.0421, 
							 0.0253, 0.0717, 0.0747, 0.0207, 0.0010, 0.0633,
							 0.0673, 0.0894, 0.0268, 0.0106, 0.0183, 0.0019, 
							 0.0172, 0.0011}[index];
	}
	public static void main(String[] args) {
//Error State
		if(!(args.length != 0)){												//When there is no string inserted it displays this message
			System.out.println("Oops, you haven't given enough parameters!");	//It's more efficient to have the error state at the top of the code as the computer won'the
			System.out.println("	Usage: java BreakCaesar \"string\"");		//have to use all the resources going to the merge of the code to find the error.
			return;																//I also used my not true bracket as I thaught it would make it more unique
		}

//Working State
		//Letter Counter
		double[] freq = new double[26];											//Initialise the Array freq to 26 rows
		double charTrack = args[0]												//String gets assigned to the array
			  .toLowerCase()													//Sets all of the characters to lower case, as later we need to count all the characters the same way
			  .chars()															//Gets everything that is a character in the string
			  .map(value -> value - 'a')										//Maps all of the values that are character
			  .filter(value -> value > -1 && value < 26)						//Since the characters have a default value that translates to numbers. It filters the character between 0 to 26.
			  .peek(value -> freq[value]++)										//Peek puts the value into the frequency array and increments it.
			  .summaryStatistics()												//Calculates the total number of elements.
			  .getCount();														//Initializes the array of tool parameters with the default values
			  
		//Freq of Letter For Loop		
		for (int i = 0; i < 26; i++) {											//Set i to zero while i is lower than the frequency length it increments.
			freq[i] = freq[i] / charTrack;										//The incrementing i is the position in the array.
		}																		//Divides all the frequencies and gets a value for each letter present.

		//Chi Value For Loops
		double[] chiValues = new double[26];									//Initialises the array 26 index long
		for (int i = 0; i < 26; i++){											//Set i to zero while i is lower than the frequency length it increments.
			double currChar = 0;												//double Current set to 0
			for(int j = 0; j < 26; j++){										//Loop through the frequency array
				double calc = freq[j];											//calc is assigned as the array freq with the j index. 
				calc = (calc*calc) + (calc / (charFreq((j+i)%26)));				//calc is multiplied by calc then calc is calc divided by character frequency j+i mod 26 take away character frequency j+i mod 26
				currChar = currChar + calc;										//current is current and calc.
			}
			chiValues[i] = currChar;											//chiValues with the index i get set the current character
		}
		
		//Loop to find the character that is the Highest using Math
		int charHighest = 0;													//charHighest gets assigned as 0
		for (int i = 0; i < 26; i++){											//Set i to zero while i is lower than the frequency length it increments.
			if(Math.abs(chiValues[i]) < Math.abs(chiValues[charHighest])){		//If math is the absolute value of chiValues array index i is bigger than chiValues index charHighest
				charHighest = i;												//charHighest gets assigned as i
			}
		}
		
		//Mergeing
		char[] merge = args[0].toCharArray();									//final array is initialised with the extension of .toCharArray.
		for(int i = 0; i < merge.length; i++){									//int i is 0 and while i is lower than the length of the merge array then increments until it's true.
			//If statemtent for lower case characters
			if(merge[i] >= 'a' && merge[i] <= 'z'){								//if merge index i is higher or equal to a and is smaller or equal to z then.
				merge[i] = (char)('a' + ((merge[i] - 'a' + charHighest) %26));	//merge i is char a plus merge index minus a plus highest character count mod 26.
			}
			//If statemtent for upper case characters
			else if (merge[i] >= 'A' && merge[i] <= 'Z'){						//if merge index i is higher or equal to A and is smaller or equal to Z then.
				merge[i] = (char)('A' + ((merge[i] - 'A' + charHighest) %26));	//merge i is char a plus merge index minus a plus highest character count mod 26.
			}
		}
		//Output
		System.out.println("\n==============================");					//Long Line of =
		System.out.println(args[0]);											//Prints inserted string
		System.out.println("==============================");					//Long Line of =
		System.out.println(new String(merge));									//Prints solved string
/*Test Case		System.out.println("The highest char value is: "+charHighest);*/
	}
}

//Questions to consider
/*
Q1.
If the language was other than english then we would have to write an analyser to see what language it would be.
Then based on this we would use a character frequency like we did for English. After this we would proceed the same way.

Q2.
We could use a frequency of characters used for lower case characters and upper case characters.
This would give us a visual into how the letters are used normally.
Then we would seperate the string into upper case and lower case letters.
After this we could start to solve it. Then we would be able to stich it back together and the outcome should be correct.
I don't think this method would have an effect on our calculation.
*/