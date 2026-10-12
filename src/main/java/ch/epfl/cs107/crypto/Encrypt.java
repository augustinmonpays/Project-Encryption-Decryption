package ch.epfl.cs107.crypto;

import ch.epfl.cs107.Helper;

import java.util.ArrayList;

import static ch.epfl.cs107.Helper.generateRandomBytes;
import static ch.epfl.cs107.utils.Text.*;
import static ch.epfl.cs107.utils.Image.*;
import static ch.epfl.cs107.utils.Bit.*;
import static ch.epfl.cs107.stegano.ImageSteganography.*;
import static ch.epfl.cs107.stegano.TextSteganography.*;
import static ch.epfl.cs107.crypto.Encrypt.*;
import static ch.epfl.cs107.crypto.Decrypt.*;
import static ch.epfl.cs107.Main.*;

/**
 * <b>Task 2: </b>Utility class to encrypt a given plain text.
 *
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.0.0
 * @since 1.0.0
 */
public final class Encrypt {

    // DO NOT CHANGE THIS, MORE ON THAT ON WEEK 7
    private Encrypt() {
    }

    // ============================================================================================
    // ================================== CAESAR'S ENCRYPTION =====================================
    // ============================================================================================

    /**
     * Method to encode a byte array message using a single character key
     * the key is simply added to each byte of the original message
     *
     * @param plainText The byte array representing the string to encode
     * @param key       the byte corresponding to the char we use to shift
     * @return an encoded byte array
     */
    public static byte[] caesar(byte[] plainText, byte key) {
        assert plainText != null;
        for (int i = 0; i < plainText.length; i++) {
            plainText[i] = (byte) (plainText[i] + key);
        }
        return plainText;

        //return Helper.fail("NOT IMPLEMENTED");
    }

    // ============================================================================================
    // =============================== VIGENERE'S ENCRYPTION ======================================
    // ============================================================================================

    /**
     * Method to encode a byte array using a byte array keyword
     * The keyword is repeated along the message to encode
     * The bytes of the keyword are added to those of the message to encode
     *
     * @param plainText the byte array representing the message to encode
     * @param keyword   the byte array representing the key used to perform the shift
     * @return an encoded byte array
     */
    public static byte[] vigenere(byte[] plainText, byte[] keyword) {
        assert plainText != null;
        int compteur = 0;
        boolean resetCompteur = false;
        for (int i = 0; i < plainText.length; i++) {
            plainText[i] = (byte) (plainText[i] + keyword[compteur]);
            resetCompteur = (compteur == keyword.length - 1);
            if (resetCompteur) {
                compteur = 0;
            } else {
                compteur++;
            }
        }
        return plainText;

        //return Helper.fail("NOT IMPLEMENTED");

    }

    // ============================================================================================
    // =================================== CBC'S ENCRYPTION =======================================
    // ============================================================================================

    /**
     * Method applying a basic chain block counter of XOR without encryption method.
     * @param plainText the byte array representing the string to encode
     * @param iv the pad of size BLOCKSIZE we use to start the chain encoding
     * @return an encoded byte array
     */
    public static byte[] cbc(byte[] plainText, byte[] iv) {
        assert plainText != null;
        assert iv != null;
        int tailleUnBloc = iv.length;
        byte[] cleSuivante = iv;
        byte [] cbc = new byte[plainText.length];

        for (int k = 0; k < plainText.length; k = k + tailleUnBloc) {
            byte[] extraitPlainText = new byte[tailleUnBloc];

            for (int i = k; i < k + tailleUnBloc; i++) {
                extraitPlainText[i-k] = plainText[i];
            }

            byte []extraitCrypte = oneTimePad(extraitPlainText,cleSuivante);
            cleSuivante = extraitCrypte;

            for (int j = 0; j < extraitCrypte.length; j++) {
                cbc[j+k] = extraitCrypte[j];
            }

        }
        return cbc;
    }

    // ============================================================================================
    // =================================== XOR'S ENCRYPTION =======================================
    // ============================================================================================

    /**
     * Method to encode a byte array using a XOR with a single byte long key
     * @param plainText the byte array representing the string to encode
     * @param key the byte we will use to XOR
     * @return an encoded byte array
     */
    public static byte[] xor(byte[] plainText, byte key) {
        assert key >= -128 && key <= 127;
        assert plainText != null;
        for (int i = 0; i < plainText.length; i++) {
            plainText[i] ^= key;
        }
        return plainText;
    }
    // ============================================================================================
    // =================================== ONETIME'S PAD ENCRYPTION ===============================
    // ============================================================================================

    /**
     * Method to encode a byte array using a one-time pad of the same length.
     *  The method XOR them together.
     * @param plainText the byte array representing the string to encode
     * @param pad the one-time pad
     * @return an encoded byte array
     */
    public static byte[] oneTimePad(byte[] plainText, byte[] pad) {
        assert plainText != null;
        assert pad != null;
        assert pad.length == plainText.length;
        byte [] oneTimePad = new byte[plainText.length];
        for (int i = 0; i < plainText.length; i++) {
            oneTimePad[i] = (byte)(plainText[i] ^ pad[i]);
        }
        return oneTimePad;

    }

    /**
     * Method to encode a byte array using a one-time pad
     * @param plainText Plain text to encode
     * @param pad Array containing the used pad after the execution
     * @param result Array containing the result after the execution
     */
    public static void oneTimePad(byte[] plainText, byte[] pad, byte[] result) {
        assert plainText != null;
        assert pad != null;
        assert pad.length == plainText.length;
        for (int i = 0; i < plainText.length; i++) {
            plainText[i] ^= pad[i];
        }
        result = plainText;
    }

}