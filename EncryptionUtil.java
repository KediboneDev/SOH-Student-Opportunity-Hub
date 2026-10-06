
/**
 * Write a description of class EncryptionUtil here.
 *
 * @author Sinqobile Mabaso 
 * @version 30 April 2026
 */
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionUtil
{
     // AES key size: 256 bits = strongest standard option
    private static final int KEY_SIZE = 256;
 
    // Algorithm: AES with CBC mode and PKCS5 padding
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
 
    // IV (Initialisation Vector) size for AES CBC = 16 bytes
    private static final int IV_SIZE = 16;

    private EncryptionUtil()
    {
        throw new UnsupportedOperationException("EncryptionUtil is a utility class.");
    }

    public static SecretKey generateKey() throws Exception
    {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(KEY_SIZE, new SecureRandom());

        return keyGen.generateKey();
    }

    public static SecretKey keyFromBytes(byte[] keyBytes)
    {
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    //Encrypts a plaintext string using AES-256 CBC.
    public static String encryptString(String plainText, SecretKey secretKey) throws Exception
    {
        if (plainText == null || plainText.isEmpty())
        {
            throw new IllegalArgumentException("plainText cannot be null or empty.");
        }
 
        // Generate a fresh random IV for every encryption
        byte[] iv = new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
 
        // Encrypt
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
 
        // Prepend IV to ciphertext so we can recover it during decryption
        byte[] ivPlusCiphertext = new byte[IV_SIZE + encrypted.length];
        System.arraycopy(iv, 0, ivPlusCiphertext, 0, IV_SIZE);
        System.arraycopy(encrypted, 0, ivPlusCiphertext, IV_SIZE, encrypted.length);
 
        // Base64 encode so it's safe to store as a plain string
        return Base64.getEncoder().encodeToString(ivPlusCiphertext);
    }

    //Decrypts a Base64-encoded AES-256 CBC encrypted string.
    public static String decryptString(String encryptedText, SecretKey secretKey) throws Exception
    {
        if (encryptedText == null || encryptedText.isEmpty())
        {
            throw new IllegalArgumentException("encryptedText cannot be null or empty.");
        }
 
        // Decode from Base64
        byte[] ivPlusCiphertext = Base64.getDecoder().decode(encryptedText);
 
        // Extract IV (first 16 bytes)
        byte[] iv = new byte[IV_SIZE];
        byte[] ciphertext = new byte[ivPlusCiphertext.length - IV_SIZE];
        System.arraycopy(ivPlusCiphertext, 0, iv, 0, IV_SIZE);
        System.arraycopy(ivPlusCiphertext, IV_SIZE, ciphertext, 0, ciphertext.length);
 
        // Decrypt
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        byte[] decrypted = cipher.doFinal(ciphertext);
 
        return new String(decrypted, "UTF-8");
    }

    //Converts a SecretKey to a Base64 string so it can be saved to a file.
    public static String keyToBase64(SecretKey key)
    {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    //Rebuilds a SecretKey from a Base64 string (reverse of keyToBase64).
    public static SecretKey keyFromBase64(String base64Key)
    {
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        return keyFromBytes(keyBytes);
    }
}