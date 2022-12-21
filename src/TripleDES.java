import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class TripleDES {

    /**
     * TripleDES
     */

    String key;
    String IV;
    String nonce;
    String input;

    public TripleDES() {
        Read r = new Read();
        ArrayList<String> keyArr = r.readKeyFile();
        this.key = keyArr.get(1);
        this.IV = keyArr.get(0);
        this.nonce = keyArr.get(2);
        this.input = r.readInputFile().get(0);
    }

    public byte[] pad_key_24_byte(byte[] key){
        byte[] result = new byte[24];
        int len = key.length;
        if (len > 24){
            System.arraycopy(key, 0, result, 0, 24);
        } else if (len == 24){
            result = key;
        } else {
            int start_index_key = 0;
            int differ = result.length - len;
            for (int i = 0; i < differ; i++) {
                result[i] = 0;
            }
            for (int i = differ; i < result.length; i++) {
                result[i] = key[start_index_key];
                start_index_key++;
            }
        }
        return result;
    }

    public byte[] xor_op(byte[] input, byte[] result){
        ArrayList<Byte> out = new ArrayList<>();
        byte[] finalArray = new byte[input.length];
        int input_counter;
        int result_counter = 0;

        for (input_counter = 0; input_counter < input.length; input_counter++) {
            out.add((byte) (input[input_counter] ^ result[result_counter]));
            result_counter++;
            if (result_counter >= result.length) result_counter = 0;
        }
        int c = 0;
        for (byte i : out){
            finalArray[c] = i;
            c ++ ;
        }
        return finalArray;
    }

    public byte[] padding_IV(byte[] pad_will){
        int len = pad_will.length;
        int original_size = len;
        while (len % 8 != 0) {
            len++;
        }
        byte[] paddedArray = new byte[len];
        int start = 0;
        for (int i = 0; i < paddedArray.length; i++){
            if ((len - original_size) != 0){
                paddedArray[i] = 0;
                len--;
            } else {
                paddedArray[i] = pad_will[start];
                start++;
            }
        }
        return paddedArray;
    }

    public byte[] OFB_Encryption() throws InvalidKeyException, NoSuchAlgorithmException{
        byte[] cipherText = null;
        byte[] keyArray = this.key.getBytes(StandardCharsets.UTF_8);
        byte[] fixedArray = pad_key_24_byte(keyArray);

        SecretKeySpec generatedKey = new SecretKeySpec(fixedArray, "TripleDES");
        try{
            Cipher cipher = Cipher.getInstance("TripleDES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, generatedKey);
            byte[] paddedIV ;
            if (this.IV.getBytes(StandardCharsets.UTF_8).length % 8 != 0) {
                paddedIV = padding_IV(this.IV.getBytes(StandardCharsets.UTF_8));
            } else {
                paddedIV = this.IV.getBytes(StandardCharsets.UTF_8);
            }
            cipherText = cipher.doFinal(paddedIV);
            cipherText = xor_op(this.input.getBytes(StandardCharsets.UTF_8), cipherText);

        } catch (NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        return cipherText;
    }

    public byte[] OFB_Decryption(byte[] theCipherText) throws InvalidKeyException, NoSuchAlgorithmException{
        byte[] thePlainText = null;

        byte[] keyArray = this.key.getBytes(StandardCharsets.UTF_8);
        byte[] fixedArray = pad_key_24_byte(keyArray);

        SecretKeySpec generatedKey = new SecretKeySpec(fixedArray, "TripleDES");
        try{
            Cipher cipher = Cipher.getInstance("TripleDES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, generatedKey);
            byte[] paddedIV ;
            if (this.IV.getBytes(StandardCharsets.UTF_8).length % 8 != 0) {
                paddedIV = padding_IV(this.IV.getBytes(StandardCharsets.UTF_8));
            } else {
                paddedIV = this.IV.getBytes(StandardCharsets.UTF_8);
            }
            thePlainText = cipher.doFinal(paddedIV);
            thePlainText = xor_op(theCipherText, thePlainText);

        } catch (NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return thePlainText;
    }

    public byte[] concatNonceAndCounter(byte counter){
        byte[] final_array = new byte[8];

        byte[] nonce_start = this.nonce.getBytes(StandardCharsets.UTF_8);
        byte[] counter_byte = String.valueOf(counter).getBytes(StandardCharsets.UTF_8);

        for (int i = 0; i < final_array.length; i ++){
            if (i == final_array.length - 1){
                final_array[i] = counter_byte[counter_byte.length - 1];
            } else {
                final_array[i] = nonce_start[i];
            }
        }
        return final_array;
    }

    public byte[] CTR_Encryption() throws InvalidKeyException, NoSuchAlgorithmException{
        byte[] cipherText = null;
        byte counter = 0;
        byte[] concat = concatNonceAndCounter(counter);

        byte[] keyArray = this.key.getBytes(StandardCharsets.UTF_8);
        byte[] fixedArray = pad_key_24_byte(keyArray);

        SecretKeySpec generatedKey = new SecretKeySpec(fixedArray, "TripleDES");

        try{
            Cipher cipher = Cipher.getInstance("TripleDES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, generatedKey);

            cipherText = cipher.doFinal(concat);
            cipherText = xor_op(this.input.getBytes(StandardCharsets.UTF_8), cipherText);
        }catch (NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        return cipherText;
    }

    public byte[] CTR_Decryption(byte[] cipherText) throws InvalidKeyException, NoSuchAlgorithmException{
        byte[] plainText = null;

        byte counter = 0;
        byte[] concat = concatNonceAndCounter(counter);

        byte[] keyArray = this.key.getBytes(StandardCharsets.UTF_8);
        byte[] fixedArray = pad_key_24_byte(keyArray);

        SecretKeySpec generatedKey = new SecretKeySpec(fixedArray, "TripleDES");

        try{
            Cipher cipher = Cipher.getInstance("TripleDES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, generatedKey);
            plainText = cipher.doFinal(concat);
            plainText = xor_op(cipherText, plainText);
        }catch (NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        return plainText;
    }


}
