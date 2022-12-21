import java.nio.charset.StandardCharsets;

public class Inputs {    private String keyString;
    private String IVString;
    private String nonceString;
    private String plainTextString = "";
    private String cipherTextString = "";



    private byte[] key;
    private byte[] IV;
    private byte[] nonce;
    private byte[] plainText;
    private byte[] cipherText;

    public byte[] getKey() {
        return key;
    }

    public byte[] getIV() {
        return IV;
    }

    public byte[] getNonce() {
        return nonce;
    }

    public byte[] getPlainText() {
        return plainText;
    }

    public byte[] getCipherText() {
        return cipherText;
    }

    public Inputs(String keyString, String IVString, String nonceString) {
        this.keyString = keyString;
        this.IVString= IVString;
        this.nonceString = nonceString;
        key = keyString.getBytes(StandardCharsets.UTF_8);
        IV = IVString.getBytes(StandardCharsets.UTF_8);
        nonce = nonceString.getBytes(StandardCharsets.UTF_8);

    }

    public String getPlainTextString() {
        return plainTextString;
    }

    public String getCipherTextString() {
        return cipherTextString;
    }

    public Inputs(byte[] key, byte[] IV, byte[] nonce, byte[] cipher) {
        this.key = key;
        this.IV = IV;
        this.nonce = nonce;
        this.cipherText = cipher;
    }

    public void setPlainTextString(String plainTextString) {
        this.plainTextString = plainTextString;
        plainText = plainTextString.getBytes(StandardCharsets.UTF_8);
    }

    public void setCipherTextString(String cipherTextString) {
        this.cipherTextString = cipherTextString;
        cipherText = cipherTextString.getBytes(StandardCharsets.UTF_8);

    }

    public void setPlainText(byte[] plainText) {
        this.plainText = plainText;
    }

    public void setCipherText(byte[] cipherText) {
        this.cipherText = cipherText;
    }
}
