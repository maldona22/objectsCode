import java.util.*;
import tester.*;

/**
 * A class that defines a new permutation code, as well as methods for encoding
 * and decoding of the messages that use this code.
 */

public class PermutationCode {
    // The original list of characters to be encoded
    ArrayList<Character> alphabet = 
        new ArrayList<Character>(Arrays.asList(
                    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 
                    'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 
                    't', 'u', 'v', 'w', 'x', 'y', 'z'));

    ArrayList<Character> code = new ArrayList<Character>(26);

    // A random number generator
    Random rand = new Random();

    // Create a new instance of the encoder/decoder with a new permutation code 
    PermutationCode() {
        this.code = this.initEncoder();
    }

    // Create a new instance of the encoder/decoder with the given code 
    PermutationCode(ArrayList<Character> code) {
        this.code = code;
    }

    void swap(ArrayList<Character> list, int firstIndex, int secondIndex) {
        Character temp = list.get(firstIndex);
        list.set(firstIndex, list.get(secondIndex));
        list.set(secondIndex, temp);
    }

    // Initialize the encoding permutation of the characters
    ArrayList<Character> initEncoder() {
        // Decided to take the approach of shuffling the alphabet
        // As our way of making a random permutation
        Random rand = new Random();
        code = new ArrayList<Character>(Arrays.asList(
                    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 
                    'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 
                    't', 'u', 'v', 'w', 'x', 'y', 'z'));
        for (int i = code.size(); i >= 0; i--) {
            swap(code, i, rand.nextInt(i));
        }
        return this.code;
    }

    String encodeHelper(String source, String accum) {
        if (source.isEmpty()) {
            return accum;
        } else {
            char firstChar = source.charAt(0);
            // TODO figure out what functions jason is ok with us using
            return accum + code.get(firstChar) + encodeHelper(source.replace(String.valueOf(firstChar), ""), accum);
        }
    }

    // produce an encoded String from the given String
    String encode(String source) {
        return encodeHelper(source, "");
    }

    String decodeHelper(String code, String accum) {
        if (code.isEmpty()) {
            return accum;
        } else {
            char firstChar = code.charAt(0);
            return accum + alphabet.get(firstChar) + decodeHelper(code.replace(String.valueOf(firstChar), ""), accum);
        }
    }

    // produce a decoded String from the given String
    String decode(String code) {
        return decodeHelper(code, "");
    }
}