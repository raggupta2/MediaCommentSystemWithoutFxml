package sample.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumUtil {
    /**
     *  A method to verify whether the checksum of input file is equal with stored one.
     * @param verifiedFilePath : A absolute path of file to be verified by checksum.
     * @param storedChecksum : A checksum value stored.
     * @return true if storedChecksum equals with one from input file.
     */
    public static boolean verifyChecksum(String verifiedFilePath, String storedChecksum){
        boolean ret = false;

        // Get the checksum
        String checksum = "";
        try {
            checksum = checksum(verifiedFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }  finally {
            // print out the checksum
            System.out.println("current checksum : " + checksum + "\nstored checksum : " + storedChecksum);
            return storedChecksum.contentEquals(checksum);
        }
    }

    /**
     * A method to generate a checksum from file.
     * @param verifiedFilePath : A absolute path of file to be verified by checksum.
     * @return string : checksum
     * @throws IOException
     */
    public static String checksum(String verifiedFilePath)
            throws IOException
    {
        // create a file object referencing any file from
        // the system of which checksum is to be generated
        File file = new File(verifiedFilePath);

        // instantiate a MessageDigest Object by passing
        // string "MD5" this means that this object will use
        // MD5 hashing algorithm to generate the checksum
        MessageDigest mdigest = null;
        try {
            mdigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // Get file input stream for reading the file
        // content
        FileInputStream fis = new FileInputStream(file);

        // Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        // read the data from file and update that data in
        // the message digest
        while ((bytesCount = fis.read(byteArray)) != -1)
        {
            mdigest.update(byteArray, 0, bytesCount);
        };

        // close the input stream
        fis.close();

        // store the bytes returned by the digest() method
        byte[] bytes = mdigest.digest();

        // this array of bytes has bytes in decimal format
        // so we need to convert it into hexadecimal format

        // for this we create an object of StringBuilder
        // since it allows us to update the string i.e. its
        // mutable
        StringBuilder sb = new StringBuilder();

        // loop through the bytes array
        for (int i = 0; i < bytes.length; i++) {

            // the following line converts the decimal into
            // hexadecimal format and appends that to the
            // StringBuilder object
            sb.append(Integer
                    .toString((bytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }

        // finally we return the complete hash
        return sb.toString();
    }
}
