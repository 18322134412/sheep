package com.xpple.sheep.view.sKB;

public class Crypter {
    private static Crypter mInstance = null;
    private static byte[] desKey;

    static {
        System.loadLibrary("encypt-jni");
    }

    public Crypter(byte[] desKey) {
        Crypter.desKey = desKey;
    }

    public static Crypter getInstance() {
        if (mInstance == null) {
            mInstance = new Crypter(desKey);
        }
        return mInstance;
    }

    private native int saveConfig(String paramString, byte[] paramArrayOfByte);

    private native int loadConfig(String paramString);

    private native byte[] encryptDes(byte[] paramArrayOfByte1,
                                     byte[] paramArrayOfByte2);

    private native byte[] decryptDes(byte[] paramArrayOfByte1,
                                     byte[] paramArrayOfByte2);

    private native String encodeBase64(byte[] paramArrayOfByte);

    private native byte[] decodeBase64(String paramString);

    public byte[] encryptDes(byte[] bts) {
        return encryptDes(desKey, bts);
    }

    public String decryptDes(byte[] bts) {
        return new String(decryptDes(desKey, bts));
    }
}