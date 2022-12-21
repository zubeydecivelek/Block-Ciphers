import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Main {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {

        String mode = args[1].substring(1);
        String op;

        Inputs inputs = FileOperations.readKeyFile(args[8], mode);
        byte[] inputFile = FileOperations.readInputFile(args[3]);
        if (mode.equals("e")){
            inputs.setPlainText(inputFile);
            op = "enc";
        } else {
            inputs.setCipherText(inputFile);
            op = "dec";
        }
        BlockCipher b = new BlockCipher(inputs,args[6], args[7], mode);
        long start = System.currentTimeMillis();
        byte[] result = b.startOperations();
        long finish = System.currentTimeMillis();
        long time = finish - start;
        FileOperations.write(args[5], result);
        FileOperations.logFile(args[3], args[5], op, args[6], args[7], String.valueOf(time));
    }

}
