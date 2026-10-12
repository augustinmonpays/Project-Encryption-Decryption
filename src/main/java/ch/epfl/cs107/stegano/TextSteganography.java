package ch.epfl.cs107.stegano;

import ch.epfl.cs107.Helper;
import ch.epfl.cs107.utils.Bit;

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
 * <b>Task 3.2: </b>Utility class to perform Text Steganography
 *
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.0.0
 * @since 1.0.0
 */
public class TextSteganography {

    // DO NOT CHANGE THIS, MORE ON THAT ON WEEK 7
    private TextSteganography(){}

    // ============================================================================================
    // =================================== EMBEDDING BIT ARRAY ====================================
    // ============================================================================================

    /**
     * Embed a bitmap message in an ARGB image
     * @param cover Cover image
     * @param message Embedded message
     * @return ARGB image with the message embedded
     */
    public static int[][] embedBitArray(int[][] cover, boolean[] message) {
        int[][] newCover = new int[cover.length][cover[0].length];
        for (int i = 0; i < cover.length; i++) {
            for (int j = 0; j < cover[0].length; j++) {
                newCover[i][j] = cover[i][j];
            }
        }
        int k = 0;
        for(int i = 0; i < cover.length; i++) {
            for (int j = 0; j < cover[0].length; j++) {
                if (k < message.length && k < (cover.length * cover[0].length)) {
                    newCover[i][j] = embedInLSB(newCover[i][j], message[k]);
                    k++;
                }
                else {
                    return newCover;
                }
                }
            }
        return newCover;
        }
//la je capte pas comment faire pour la taille de la boucle c'est bizarre
        // go regarder la méthode d'en dessous et demander à gpt si ça marche

    /**
     * Extract a bitmap from an image
     * @param image Image to extract from
     * @return extracted message
     */
    public static boolean[] revealBitArray(int[][] image) {
        assert image != null && image.length != 0 && image[0].length != 0;
        int longueurImage = image.length * image[0].length;
        boolean[] revealed = new boolean[longueurImage];
        int k = 0;
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                revealed[k] = Bit.getLSB(image[i][j]);
                k++;
            }
        }
        return revealed;
    }



    // ============================================================================================
    // ===================================== EMBEDDING STRING =====================================
    // ============================================================================================

    /**
     * Embed a String message in an ARGB image
     * @param cover Cover image
     * @param message Embedded message
     * @return ARGB image with the message embedded
     */
    public static int[][] embedText(int[][] cover, byte[] message) {
        assert cover != null && cover.length != 0 && cover[0].length != 0;
        assert message != null && message.length != 0;
        int[][] newCover = new int[cover.length][cover[0].length];
        for (int i = 0; i < cover.length; i++) {
            for (int j = 0; j < cover[0].length; j++) {
                newCover[i][j] = cover[i][j];
            }
        }
        int m = 0;
        boolean[] tab = new boolean[8 * message.length];
        for(int l = 0; l < message.length; l++) {
            boolean[] tabTemporaire = toBitArray(message[l]);
            for(int p = 0; p < message.length; p++) {
                if (m < tab.length) {
                    tab[m] = tabTemporaire[p];
                    m++;
                }
            }
        }
        int k = 0;
        for(int i = 0; i < cover.length; i++) {
            for (int j = 0; j < cover[0].length; j++) {
                if (k < tab.length && k < (cover.length * cover[0].length)) {
                    newCover[i][j] = embedInLSB(newCover[i][j], tab[k]);
                    k++;
                }
                else{
                    return newCover;
                }
            }
        }
        return newCover;
    }
    //embedBW : cache une image binaire dans une image rgb
    //embedGray : cache une image grise dans une image rgb
    //embedARGB : modifie image -> binaire puis cache dans image rgb
    //revealBW : recréer image binaire à partir d'image rgb
    //embedBitArray : cache message écrit dans image
    //revealBitArray : retourne un tab de bool représentant les lsb d'une image
    //embedText : cache un tab de byte dans une image
    //revealText : révèle un tab de byte d'une image

    /**
     * Extract a String from an image
     * @param image Image to extract from
     * @return extracted message
     */
    public static byte[] revealText(int[][] image) {
        assert image != null && image.length != 0 && image[0].length != 0;
        ArrayList<Boolean> array = new ArrayList<Boolean>();
        boolean[] tab = revealBitArray(image);
        for(int i = 0; i < tab.length; i++) {
            array.add(tab[i]);
        }
        byte[] reveal = new byte[tab.length/8];
        int k = 0;
        for (int i = 0; i < tab.length; i += 8) {
            boolean[] provisoryTab = new boolean[8];
            for (int j = 0; j < 8 && (i + j) < tab.length; j++) {
                provisoryTab[j] = tab[i + j];
            }
            reveal[k] = Bit.toByte(provisoryTab);
            k++;
        }
        return reveal;
        //on veut prendre le tableau de boolean, prendre les 8 premiers, en faire un byte, puis les 8 suivants
        //puis mettre tout ces bytes dans un tableau
        //on met tab dans array
        //on fait un for loop, on créer un tableau provisoire de taille 8 qui prend les 8premiers d'array
        //puis on supprime les 8 premiers de array
        //on utilise toByte avec
        //et on créer un autre tableau de byte qui prendre les éléments k (k étant un nombre qui augmente
        //indépendamment de la boucle
    }

}
