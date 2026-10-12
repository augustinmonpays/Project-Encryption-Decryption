package ch.epfl.cs107.utils;

import ch.epfl.cs107.Helper;

import java.sql.SQLOutput;

import static ch.epfl.cs107.utils.Text.*;
import static ch.epfl.cs107.utils.Image.*;
import static ch.epfl.cs107.utils.Bit.*;
import static ch.epfl.cs107.stegano.ImageSteganography.*;
import static ch.epfl.cs107.stegano.TextSteganography.*;
import static ch.epfl.cs107.crypto.Encrypt.*;
import static ch.epfl.cs107.crypto.Decrypt.*;
import static ch.epfl.cs107.Main.*;

/**
 * <b>Task 1.1: </b>Utility class to manipulate bits
 *
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.0.0
 * @since 1.0.0
 */
public final class Bit {

    // DO NOT CHANGE THIS, MORE ON THAT ON WEEK 7
    private Bit(){}

    // ============================================================================================
    // ==================================== BIT MANIPULATION ======================================
    // ============================================================================================

    /**
     * Embed a bit in a given integer bits
     * <p>
     * @param value value to embed in
     * @param m <code>true</code> to embed 1, <code>false</code> to embed 0
     * @param pos position of the bit to change
     * @return embedded value
     */
    public static int embedInXthBit(int value, boolean m, int pos) { //modifie la valeur du bit à la position "pos"
        int newValue = value >>> pos; //si V = 01101000, pos = 4, m = true -> nV = 00000110
        assert pos < 8 && pos >= 0;
        if (m){ //si boolean m = true donc on veut que le bit à pos soit modifié en "1" au lieu de "0"
            newValue = (newValue | 1) << pos; //nV = 00000111 -> 01110000
            return (newValue | value); //01110000 | 01101000 = 011110000
        }
        else{ //si m = false donc on veut que le bit devienne 0 au lieu de 1
            newValue =  ~(1 << pos);//par exemple : V = 01110111, pos = 2, nV = 00011101
            //On prend le bit 1 = 0000001 << pos = 00000100 -> ~00000100 = 11111011
            return (value &  newValue);//01110111 & 11111011 = 01110011
        }
    }

    /**
     * Embed a bit in the "least significant bit" (LSB)
     * <p>
     * @param value value to embed in
     * @param m <code>true</code> to embed 1, <code>false</code> to embed 0
     * @return embedded value
     */
    public static int embedInLSB(int value, boolean m){ //change la valeur du dernier bit
        if (m){
            return value | 1; //la si c'est true on compare avec le bit 1 (=00000001) et ducoup ça va juste modifier
            //le dernier bit qui deviendra 1 genre si tu fais 11101110 | 00000001 = 11101111
        }
        else{ //La un peu pareil mais ça fonctionne différemment je te laisse comprendre
            return value & ~1; //tu verras pour les autres méthodes c'est grobalement le même fonctionnement
            //que pour cette méthode et la première
        }
    }
    /**
     * Extract a bit in a given position from a given value
     * <p>
     * @param value value to extract from
     * @param pos position of the bit to extract
     * @return <code>true</code> if the bit is '1' and <code>false</code> otherwise
     */
    public static boolean getXthBit(int value, int pos) { //donne la valeur du bit à la positon "pos"
        assert pos < 32;
        assert pos >= 0;
        value = value >>> pos; //je te laisse comprendre c'est pas trop dur
        if ((value & 1) == 1) {
            return true; //donc le bit à pos vaut 1
        }
        return false; //le bit à pos vaut 0
    }

    /**
     * Extract the 'least significant bit' from a given value
     * <p>
     * @param value value to extract from
     * @return <code>true</code> if the bit is '1' and <code>false</code> otherwise
     */
    public static boolean getLSB(int value) { //donne la valeur du dernier bit
        if ((value & 1) == 1) { //on compare le dernier bit à 1 genre c'est ez
            return true;
        }
        return false;
    }

    // ============================================================================================
    // ==================================== BYTE MANIPULATION =====================================
    // ============================================================================================

    /**
     * Convert a byte value to the bit array representation.
     * <p>
     * Suppose we have the following input <b><code>0b00_11_01_10</code></b>. We can expect this function
     * to return the following array :
     * <b>[<code>false</code>,
     *     <code>false</code>,
     *     <code>true</code>,
     *     <code>true</code>,
     *     <code>false</code>,
     *     <code>true</code>,
     *     <code>true</code>,
     *     <code>false</code>]</b>
     * @param value byte representation of the value
     * @return bit array representation of the value
     */
    public static boolean[] toBitArray(byte value){ //transformer des bits en tableau de boolean
        boolean[] tab = new boolean[8];//j'ai donc mis ma version mais je te laisse vérifier et en plus j'ai pas
                                       //réfléchis aux cas limites
        for(int i = 0; i < 8; i++){
            boolean ouiOuNon = ((value>>i&1)==1);
            if (ouiOuNon){
                tab[tab.length-1-i]=true;
            }else {
                tab[tab.length-1-i] = false;
            }
        }
        return tab;

    }

    /**
     * Convert a boolean array to a byte
     * <p>
     * Suppose we have the following input :
     * <b>[<code>false</code>,
     *     <code>false</code>,
     *     <code>true</code>,
     *     <code>true</code>,
     *     <code>false</code>,
     *     <code>true</code>,
     *     <code>true</code>,
     *     <code>false</code>]</b>
     * We can expect this function to return the following byte : <b><code>0b00_11_01_10</code></b>.
     * @param bitArray bit array representation to convert
     * @return the byte representation of the bit array
     */
    public static byte toByte(boolean[] bitArray){ //transforme un tableau de boolean en une chaine de byte
        int value = 0;
        for (int i = 0; i < bitArray.length; i++){
            if (i != 0) {
                value = value << 1;
            }
            if (bitArray[i]){
                value = (value | 1);
            }
            else{
                value = value & ~1;
                }
            }
        return (byte)value;
    }

}
