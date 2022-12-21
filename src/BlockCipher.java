import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

public class BlockCipher {
    Inputs inputs;
    String algorithm;
    String EcbMode;
    String mode;


    public BlockCipher(Inputs inputs, String algorithm, String EcbMode,String mode) {
        this.inputs = inputs;
        this.EcbMode = EcbMode;
        this.mode = mode;

        if(algorithm.equals("3DES"))
            this.algorithm = "TripleDES";
        else
            this.algorithm = algorithm;
    }


    public byte[] startOperations() throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        byte[] resultBytes = null;

        Cipher cipher = null;

        if (EcbMode.equals("OFB")) {
            cipher = CreateCipher(algorithm, "e");
            resultBytes = OFB(cipher);
        } else if (EcbMode.equals("CBC")) {
            cipher = CreateCipher(algorithm, mode);
            resultBytes = CBC(cipher);
        } else if (EcbMode.equals("CFB")) {
            cipher = CreateCipher(algorithm, "e");
            resultBytes = CFB(cipher);
        }else if (EcbMode.equals("CTR")) {
            cipher = CreateCipher(algorithm, "e");
            resultBytes = CTR(cipher);
        }
        return resultBytes;
    }

    public byte[] CTR(Cipher cipher){
        byte[] result = null;
        byte counter = 0;
        byte[] concat = concatNonceAndCounter(counter);

        try {
            result = cipher.doFinal(concat);
            if(mode.equals("e"))
                result = xor_op(inputs.getPlainText(),result);
            else if (mode.equals("d"))
                result = xor_op(inputs.getCipherText(),result);
        }catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public byte[] CFB(Cipher cipher){
        byte[] IV, result = null;
        if (inputs.getIV().length % 8 != 0)
            IV = padding(inputs.getIV());
        else
            IV = inputs.getIV();

        try {
            result = cipher.doFinal(IV);
            if(mode.equals("e"))
                result = xor_op(inputs.getPlainText(), result);
            else if (mode.equals("d"))
                result = xor_op(inputs.getCipherText(), result);

        }catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        return result;
    }

    public byte[] OFB(Cipher cipher){
        byte[] IV = inputs.getIV(), result = null;
        if (inputs.getIV().length % 8 != 0)
            IV = padding(inputs.getIV());

        try {
            result = cipher.doFinal(IV);
            if (mode.equals("e"))
                result = xor_op(inputs.getPlainText(),result);
            else if (mode.equals("d")) {
                result = xor_op(inputs.getCipherText(), result);
            }
        }catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public byte[] CBC(Cipher cipher){
        byte[] result  = null;

        try {
            if(mode.equals("e")){
                result = xor_op(inputs.getPlainText(),inputs.getIV());
                if (result.length % 8 != 0)
                    result = padding(result);
                result = cipher.doFinal(result);
            }
            else if (mode.equals("d")) {
                byte[] cipherText = inputs.getCipherText();
                if (cipherText.length % 8 != 0)
                    cipherText = padding(cipherText);
                result = cipher.doFinal(cipherText);
                result = xor_op(result,inputs.getIV());
            }
        }catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        return result;
    }

    public Cipher CreateCipher(String algorithm, String mode) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
        Cipher cipher = null;
        SecretKey generatedKey = null;
        if(algorithm.equals("DES")){
            DESKeySpec keySpecG = new DESKeySpec(inputs.getKey());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
            generatedKey = keyFactory.generateSecret(keySpecG);
        } else if (algorithm.equals("TripleDES")) {
            generatedKey = new SecretKeySpec(pad_key_24_byte(inputs.getKey()), "TripleDES");
        }

        try{
            cipher = Cipher.getInstance(algorithm + "/ECB/NoPadding");
            if(mode.equals("e"))
                cipher.init(Cipher.ENCRYPT_MODE, generatedKey);
            else {
                cipher.init(Cipher.DECRYPT_MODE, generatedKey);
            }
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        return cipher;
    }

    public byte[] concatNonceAndCounter(byte counter){
        byte[] final_array = new byte[8];

        byte[] nonce_start = inputs.getNonce();
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

    public byte[] padding(byte[] pad_will){
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
}
