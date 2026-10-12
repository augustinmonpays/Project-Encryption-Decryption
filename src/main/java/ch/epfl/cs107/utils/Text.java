package ch.epfl.cs107.utils;

import ch.epfl.cs107.Helper;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static ch.epfl.cs107.utils.Text.*;
import static ch.epfl.cs107.utils.Image.*;
import static ch.epfl.cs107.utils.Bit.*;
import static ch.epfl.cs107.stegano.ImageSteganography.*;
import static ch.epfl.cs107.stegano.TextSteganography.*;
import static ch.epfl.cs107.crypto.Encrypt.*;
import static ch.epfl.cs107.crypto.Decrypt.*;
import static ch.epfl.cs107.Main.*;


/**
 * <b>Task 1.2: </b>Utility class to manipulate texts ({@link String}) objects.
 *
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.0.0
 * @since 1.0.0
 */
public final class Text {

    // DO NOT CHANGE THIS, MORE ON THAT ON WEEK 7
    private Text(){}

    // ============================================================================================
    // ==================================== STRING MANIPULATION ===================================
    // ============================================================================================

    /**
     * Convert a given <b>String</b> into a <b>byte[]</b> following the <b>UTF-8</b> convention
     * @param str String to convert
     * @return bytes representation of the String in the <b>UTF-8</b> format
     */
    public static byte[] toBytes(String str){return str.getBytes(StandardCharsets.UTF_8);}

    /**
     * Convert a given String into a boolean array representation
     * @implNote the bit array should be based on an <b>UTF-8</b> representation
     * @param str String to convert
     * @return <b>UTF-8</b> representation of the string in the <b>bit array</b> format
     */
    //je n'ai pas fais les cas limites --> si tu les fais effaces ce commentaire (jsp si il y en a)
    public static boolean[] toBitArray(String str){ //prend une String et la converti en tableau de booleans
        byte[] tableauDeBytes = toBytes(str);  //on utilise la fonction qu'ils ont donnés pour convertir la string en tableau de Bytes
                                               //j'ai réussis a le faire seul et c'était très très long, et je me rendais compte que ca donnait pas exactement les résultats attendus ==> et en relisant l'énoncé en fait il fallait juste utiliser leur fonction pour pallier a ce problème
        ArrayList<Boolean>listeDeBooleans = new ArrayList<>(); //tableau dynamique pour stocker tous les boleans
        for (int i = 0; i < tableauDeBytes.length; i++){ //remplit le tableau dynamique
            boolean [] elementDuTableau = Bit.toBitArray(tableauDeBytes[i]); //on prend chaque élément du tableau de byte du debut, qui vont chacun donner a leur tour un tableau de boleans
            for (int j = 0; j < elementDuTableau.length; j++){//on ajoute tous les boleans a ce tableau dynamique
                listeDeBooleans.add(elementDuTableau[j]);
            }
        }
        boolean[] tableauDeBooleans = new boolean[listeDeBooleans.size()]; //on creer un tableau de boleans pour pouvoir mettre toutes les boleans du tableau dynamique dans celui ci, afin de pouvoir renvoyer ce que veut la méthode ==> un tableau de bolean
        for (int i = 0; i < listeDeBooleans.size(); i++){ //on convertit le tableau dynamique en table de boleans
            tableauDeBooleans[i] = listeDeBooleans.get(i);
        }
        return(tableauDeBooleans);
        //return Helper.fail("NOT IMPLEMENTED");
    }

    /**
     * Convert a given <b>byte[]</b> into a <b>String</b> following the <b>UTF-8</b> convention
     * @param bytes String in the byte array format
     * @return String representation using the {@link String} type
     */
    public static String toString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * Convert a given bit array to a String
     * @param bitArray <b>UTF-8</b> compatible bit array
     * @return <b>UTF-8 String</b> representation of the bit array
     */
    public static String toString(boolean[] bitArray) {
        assert bitArray != null :"le tableau de booleans n'existe pas";

        ArrayList<Byte>aConvertirEnTableauDeBytes = new ArrayList<>();

        for (int j = 0; j < 8*(bitArray.length/Byte.SIZE); j= j + 8) {
            boolean[] unOctet = new boolean[8];
            for (int i = 0; i < 8; i++) {
                unOctet[i] = bitArray[i+j];
            }
            aConvertirEnTableauDeBytes.add (Bit.toByte(unOctet));

        }

        byte [] aConvertirEnString = new byte[aConvertirEnTableauDeBytes.size()];
        for (int i = 0; i < aConvertirEnTableauDeBytes.size(); i++){
            aConvertirEnString[i] = aConvertirEnTableauDeBytes.get(i);
        }

        return Text.toString (aConvertirEnString);

        //return Helper.fail("NOT IMPLEMENTED");
    }

}
