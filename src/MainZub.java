import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class MainZub {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        /// FileCipher −e −i inputf ile.txt −o encrypted.txt DES CBC key.txt
        Reading reading = new Reading();
        Inputs inputs = reading.readFiles(args[3],args[8],args[1].substring(1));
        BlockCipher blockCipher = new BlockCipher(inputs,args[6], args[7], args[1].substring(1));

        byte[]arr = blockCipher.startOperations();
        String encrypted = new String(arr, StandardCharsets.UTF_8);
        System.out.println(encrypted);
        System.out.println();

        Inputs deneme = new Inputs(inputs.getKey(), inputs.getIV(), inputs.getNonce(),arr);
        //deneme.setCipherText(arr);
        BlockCipher cipherDeneme = new BlockCipher(deneme,"DES","CTR", "d");
        byte[] dec = cipherDeneme.startOperations();
        System.out.println(new String(dec,StandardCharsets.UTF_8));

    }
}
