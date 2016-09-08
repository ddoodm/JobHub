/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinyon.aip.jobhub.util;

import java.security.*;

/**
 * Stores a SHA256 checksum for password hashing.
 *
 * Based on code from the AIP Week 5 Practice Page:
 * https://benatuts.github.io/aip/05-practice.html
 */
public class ShaHash
{
    private String hashValue;
    
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
    
    private String hash256(String data) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(data.getBytes());
        return bytesToHex(md.digest());
    }

    private String bytesToHex(byte[] bytes)
    {
        StringBuffer result = new StringBuffer();
        for (byte byt : bytes) {
            result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }
    
    public String toString()
    {
        return hashValue;
    }
}
