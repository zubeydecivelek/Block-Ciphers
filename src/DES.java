import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

public class DES {

    public String key;
    public String IV;

    public DES() throws IOException {
        Read read = new Read();
        this.key = read.readKeyFile().get(1);
        this.IV = read.readKeyFile().get(2);
    }

    public void xor(){

    }

    public byte[] CBC_Encryption() throws IOException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
        ArrayList<String> keyFile = new Read().readKeyFile();
        byte[] theResult = null;

        String key = keyFile.get(1); // Key
        byte[] keyArray = key.getBytes(StandardCharsets.UTF_8); // Byte array of key
        SecretKeySpec keySpec = new SecretKeySpec(keyArray, "DES");
        DESKeySpec keySpecG = new DESKeySpec(keyArray);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey fInal = keyFactory.generateSecret(keySpecG);

        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, fInal);
            theResult = cipher.doFinal("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum varius urna egestas volutpat tempus. Etiam molestie velit eget justo vehicula ornare. Vivamus at felis dui. Suspendisse rutrum libero neque, viverra facilisis arcu euismod et. Vivamus pharetra ac lorem a egestas. Aenean sit amet gravida lorem. Sed mauris libero, pellentesque vitae justo eu, laoreet cursus mauris. Nam non libero ac erat lobortis dignissim ut et nunc. Praesent congue mauris ac ultrices maximus. Donec molestie lacus libero, a auctor justo aliquet sit amet. Etiam cursus dui libero, sit amet ultricies est rhoncus sed. Donec quis justo lectus. Duis porttitor libero ac arcu dictum faucibus. Phasellus convallis fringilla quam. In ut nunc et dui volutpat bibendum id vel arcu.".getBytes());

        }catch (IllegalBlockSizeException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException e){
            e.printStackTrace();
        }
        return theResult;
    }

}