package com.deinyon.aip.jobhub.util;

import java.security.*;

/**
 * Stores a SHA256 checksum for password hashing.
 *
 * Based on code from the UTS: AIP Week 5 Practice Page:
 * https://benatuts.github.io/aip/05-practice.html
 */
public class ShaHash
{
    /**
     * The base16-encoded hash value
     */
    private String hashValue;
    
    /**
     * Creates a new SHA256 Hash
     * @param data The data to hash
     * @throws IllegalStateException If the system does not support the SHA256
     * hashing algorithm
     */
    public ShaHash(String data) throws IllegalStateException
    {
        try
        {
            this.hashValue = hash256(data);
        }
        catch(NoSuchAlgorithmException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Generates a checksum of the specified data
     * @param data The data to hash
     * @return The hash of the specified data
     * @throws NoSuchAlgorithmException If the system does not support the SHA256
     * hashing algorithm
     */
    private String hash256(String data) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(data.getBytes());
        return bytesToHex(md.digest());
    }

    /**
     * Prints an array of bytes into a base16 (Hex) encoded String
     * @param bytes The bytes to be converted to a String
     * @return The base16 (Hex) encoded String representation of the byte array
     */
    private String bytesToHex(byte[] bytes)
    {
        StringBuffer result = new StringBuffer();
        for (byte byt : bytes) {
            result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }
    
    /**
     * @return The base16-encoded String value of the hash
     */
    public String toString()
    {
        return hashValue;
    }
}
