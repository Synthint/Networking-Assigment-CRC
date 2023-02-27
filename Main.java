import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        String key = "1000100000010001"; // x^16 + x^12 + x^5 + 1

        try {
			Scanner scanner = new Scanner(new File("inputFile.txt"));
            // read in each line, encode it with the CRC, then decode it back
            // to the ascii values
			while (scanner.hasNextLine()) { 
				String currLine = scanner.nextLine(); 
                String codeWord = encodeString(currLine, key); 
                String dataWord = decodeString(codeWord, key);
                System.out.println("Input -> "+currLine);
                System.out.println("Encoded Input -> "+codeWord);
                System.out.println("Decoded Input -> "+dataWord);
                System.out.println("");
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }


    /*///////////////////////////////////////////////
        decode a string by checking if it is valid
        then chop off the CRC portion and convert
        back to ascii
     *////////////////////////////////////////////////
    public static String decodeString(String code, String key){
        if(!validCodeWord(code, key)){return "";}
        return bitStringToString(code.substring(0,code.length()-(key.length()-1)));
    }

    /*///////////////////////////////////////////////
        encode a string by converting to bits and
        appending the CRC code at the end
     *////////////////////////////////////////////////
    public static String encodeString(String data,String key){
        String dataBits = stringToBitString(data);
        return dataToCodeWord(dataBits, key);
    }

    /*///////////////////////////////////////////////
        convert a string of 1s and 0s
        to ascii characters
     *////////////////////////////////////////////////
    public static String bitStringToString(String input){
        String output = "";
        if(input.length() % 7 != 0){ // ascii characters are 7 bits long, 
            //if the input is not divisible by 7, it is not valid
            return "";
        }
        while( input.length()>0 ){
            // get first 7 "bits" as a string, parse to an int, convert to character
            int currBlock = Integer.parseInt(input.substring(0,7), 2);
            output += (char)currBlock;
            // remove first 7 bits and repeat until input is empty
            input = input.substring(7);
        }

        return output;
    }

    /*///////////////////////////////////////////////
        convert a string of ascii characters 
        to a string of 1s and 0s
     *////////////////////////////////////////////////
    public static String stringToBitString(String input){
        String output = "";
        for(int x=0;x<input.length();x++){
            int currCharCode = input.charAt(x); // convert character at an index to ascii number
            String nextBlock = Integer.toString(currCharCode,2); // get the value of the next block as a string

            // ascii goes up to 2^7, so any string less than 7 bits long must be extended
            while(nextBlock.length() < 7){
                nextBlock = "0" + nextBlock;
            }
            // append to output
            output += nextBlock;
        }
        return output;
    }


    /*///////////////////////////////////////////////
        return true if the codeword is correct
        return false if an error is detected
     *////////////////////////////////////////////////
    public static boolean validCodeWord(String codeWord, String key){
        return Integer.parseInt(CRCRemainderCalc(codeWord, key), 2) == 0;
    }
    
    /*///////////////////////////////////////////////
        calculate the crc remainder of
        a dataword and append it to
        dataword to get codeword
     *////////////////////////////////////////////////
    public static String dataToCodeWord(String dataWord, String key){
        return dataWord + CRCRemainderCalc(dataWord, key); 
    }


    /*///////////////////////////////////////////////
        calculate the CRC remainder for a given
        dataword and string
     *////////////////////////////////////////////////
    public static String CRCRemainderCalc(String dataWord, String key){
        for(int x=0;x<key.length()-1;x++){
            dataWord += "0"; // extend dividend by length of key-1
        }
        int keyNum = Integer.parseInt(key, 2); // get key as a number to be used later for xor calculation
        String remainder = dataWord.substring(0,key.length()); // set remainder as the first key-length block of bits
        int dataWordIndex = key.length(); // index used to get next bit of dataWord to append to remainder after xor for subtraction
        while(true){
            if(remainder.charAt(0) == '0'){ // subtract 0 if first bit of remainder is 0
                if(dataWordIndex < dataWord.length()){// if not the final remainder calculation
                    // remove first digit, append next dataWord bit and increase dataWordIndex
                    remainder = remainder.substring(1,remainder.length()) + dataWord.charAt(dataWordIndex);
                    dataWordIndex++;
                    continue;
                }else{// if it is the final remainder calc, chop off first digit
                    remainder = remainder.substring(1,remainder.length());
                    break;
                }
            }
            int remNum = Integer.parseInt(remainder, 2); // remainder as an int for xor
            int xorVal = remNum ^ keyNum; // use xor as bit subtraction
            String xorStr = Integer.toString(xorVal,2); // return xor to a string to do bit ops
            while(xorStr.length() < key.length()-1){
                xorStr = "0" + xorStr; // ensure the xorStr is the correct size of the remainder
            }

            if(dataWordIndex < dataWord.length()){ // if this is not the final remainder calculation
                // then add the next dataWord bit, increase dataWordIndex, and reset the remainder
                xorStr += dataWord.charAt(dataWordIndex);
                dataWordIndex++;
                remainder = xorStr;
            }else{
                // reset the remainder so it can be returned
                remainder = xorStr;
                break;
            }
        }

        return remainder;
    }



}


