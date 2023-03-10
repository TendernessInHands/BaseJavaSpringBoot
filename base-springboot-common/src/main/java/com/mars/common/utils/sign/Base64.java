package com.mars.common.utils.sign;

/**
 * Base64工具类
 *
 * @author mars
 */
public final class Base64 {
    private static final int BASELENGTH = 128;
    private static final int LOOKUPLENGTH = 64;
    private static final int TWENTYFOURBITGROUP = 24;
    private static final int EIGHTBIT = 8;
    private static final int SIXTEENBIT = 16;
    private static final int FOURBYTE = 4;
    private static final int SIGN = -128;
    private static final char PAD = '=';
    private static final char DA = 'A';
    private static final char XA = 'a';
    private static final char DZ = 'Z';
    private static final char XZ = 'z';
    private static final char D0 = '0';
    private static final char D9 = '9';
    private static final byte[] BASE64ALPHABET = new byte[BASELENGTH];
    private static final char[] LOOKUPBASE64ALPHABET = new char[LOOKUPLENGTH];

    static {
        for (int i = 0; i < BASELENGTH; ++i) {
            BASE64ALPHABET[i] = -1;
        }
        for (int i = DZ; i >= DA; i--) {
            BASE64ALPHABET[i] = (byte) (i - DA);
        }
        for (int i = XZ; i >= XA; i--) {
            BASE64ALPHABET[i] = (byte) (i - XA + 26);
        }

        for (int i = D9; i >= D0; i--) {
            BASE64ALPHABET[i] = (byte) (i - D0 + 52);
        }

        BASE64ALPHABET['+'] = 62;
        BASE64ALPHABET['/'] = 63;
        int size = 25;
        for (int i = 0; i <= size; i++) {
            LOOKUPBASE64ALPHABET[i] = (char) (DA + i);
        }
        int size51 = 51;
        for (int i = 26, j = 0; i <= size51; i++, j++) {
            LOOKUPBASE64ALPHABET[i] = (char) (XA + j);
        }
        int size61 = 61;
        for (int i = 52, j = 0; i <= size61; i++, j++) {
            LOOKUPBASE64ALPHABET[i] = (char) (D0 + j);
        }
        LOOKUPBASE64ALPHABET[62] = (char) '+';
        LOOKUPBASE64ALPHABET[63] = (char) '/';
    }

    private static boolean isWhiteSpace(char octect) {
        return octect == 0x20 || octect == 0xd || octect == 0xa || octect == 0x9;
    }

    private static boolean isPad(char octect) {
        return octect == PAD;
    }

    private static boolean isData(char octect) {
        return octect < BASELENGTH && BASE64ALPHABET[octect] != -1;
    }

    /**
     * Encodes hex octects into Base64
     *
     * @param binaryData Array containing binaryData
     * @return Encoded Base64 array
     */
    public static String encode(byte[] binaryData) {
        if (binaryData == null) {
            return null;
        }

        int lengthDataBits = binaryData.length * EIGHTBIT;
        if (lengthDataBits == 0) {
            return "";
        }

        int fewerThan24bits = lengthDataBits % TWENTYFOURBITGROUP;
        int numberTriplets = lengthDataBits / TWENTYFOURBITGROUP;
        int numberQuartet = fewerThan24bits != 0 ? numberTriplets + 1 : numberTriplets;
        char[] encodedData = null;

        encodedData = new char[numberQuartet * 4];

        byte k = 0;
        byte l = 0;
        byte b1 = 0;
        byte b2 = 0;
        byte b3 = 0;

        int encodedIndex = 0;
        int dataIndex = 0;

        for (int i = 0; i < numberTriplets; i++) {
            b1 = binaryData[dataIndex++];
            b2 = binaryData[dataIndex++];
            b3 = binaryData[dataIndex++];

            l = (byte) (b2 & 0x0f);
            k = (byte) (b1 & 0x03);

            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) (b1 >> 2 ^ 0xc0);
            byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4) : (byte) (b2 >> 4 ^ 0xf0);
            byte val3 = ((b3 & SIGN) == 0) ? (byte) (b3 >> 6) : (byte) (b3 >> 6 ^ 0xfc);

            encodedData[encodedIndex++] = LOOKUPBASE64ALPHABET[val1];
            encodedData[encodedIndex++] = LOOKUPBASE64ALPHABET[val2 | (k << 4)];
            encodedData[encodedIndex++] = LOOKUPBASE64ALPHABET[(l << 2) | val3];
            encodedData[encodedIndex++] = LOOKUPBASE64ALPHABET[b3 & 0x3f];
        }

        // form integral number of 6-bit groups
        if (fewerThan24bits == EIGHTBIT) {
            b1 = binaryData[dataIndex];
            k = (byte) (b1 & 0x03);
            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) (b1 >> 2 ^ 0xc0);
            encodedData[encodedIndex++] = LOOKUPBASE64ALPHABET[val1];
            encodedData[encodedIndex++] = LOOKUPBASE64ALPHABET[k << 4];
            encodedData[encodedIndex++] = PAD;
            encodedData[encodedIndex++] = PAD;
        } else if (fewerThan24bits == SIXTEENBIT) {
            b1 = binaryData[dataIndex];
            b2 = binaryData[dataIndex + 1];
            l = (byte) (b2 & 0x0f);
            k = (byte) (b1 & 0x03);

            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) (b1 >> 2 ^ 0xc0);
            byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4) : (byte) (b2 >> 4 ^ 0xf0);

            encodedData[encodedIndex++] = LOOKUPBASE64ALPHABET[val1];
            encodedData[encodedIndex++] = LOOKUPBASE64ALPHABET[val2 | (k << 4)];
            encodedData[encodedIndex++] = LOOKUPBASE64ALPHABET[l << 2];
            encodedData[encodedIndex++] = PAD;
        }
        return new String(encodedData);
    }

    /**
     * Decodes Base64 data into octects
     *
     * @param encoded string containing Base64 data
     * @return Array containind decoded data.
     */
    public static byte[] decode(String encoded) {
        if (encoded == null) {
            return null;
        }

        char[] base64Data = encoded.toCharArray();
        // remove white spaces
        int len = removeWhiteSpace(base64Data);

        if (len % FOURBYTE != 0) {
            // should be divisible by four
            return null;
        }

        int numberQuadruple = len / FOURBYTE;

        if (numberQuadruple == 0) {
            return new byte[0];
        }

        byte[] decodedData = null;
        byte b1 = 0;
        byte b2 = 0;
        byte b3 = 0;
        byte b4 = 0;
        char d1 = 0;
        char d2 = 0;
        char d3 = 0;
        char d4 = 0;

        int i = 0;
        int encodedIndex = 0;
        int dataIndex = 0;
        decodedData = new byte[numberQuadruple * 3];

        for (; i < numberQuadruple - 1; i++) {

            if (!isData(d1 = base64Data[dataIndex++]) || !isData(d2 = base64Data[dataIndex++])
                    || !isData(d3 = base64Data[dataIndex++]) || !isData(d4 = base64Data[dataIndex++])) {
                return null;
            } // if found "no data" just return null

            b1 = BASE64ALPHABET[d1];
            b2 = BASE64ALPHABET[d2];
            b3 = BASE64ALPHABET[d3];
            b4 = BASE64ALPHABET[d4];

            decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
            decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
            decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);
        }

        if (!isData(d1 = base64Data[dataIndex++]) || !isData(d2 = base64Data[dataIndex++])) {
            // if found "no data" just return null
            return null;
        }

        b1 = BASE64ALPHABET[d1];
        b2 = BASE64ALPHABET[d2];

        d3 = base64Data[dataIndex++];
        d4 = base64Data[dataIndex++];
        Byte byte0xf = 0xf;
        Byte byte0x3 = 0x3;
        String characters4 = "";
        // Check if they are PAD characters
        if (!isData(d3) || !isData(d4)) {
            if (isPad(d3) && isPad(d4)) {
                // last 4 bits should be zero
                if ((b2 & byte0xf) != 0) {
                    return null;
                }
                byte[] tmp = new byte[i * 3 + 1];
                System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                tmp[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
                return tmp;
            } else if (!isPad(d3) && isPad(d4)) {
                b3 = BASE64ALPHABET[d3];
                // last 2 bits should be zero
                if ((b3 & byte0x3) != 0) {
                    return null;
                }
                byte[] tmp = new byte[i * 3 + 2];
                System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                tmp[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
                tmp[encodedIndex] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
                return tmp;
            } else {
                return null;
            }
        } else { // No PAD e.g 3cQl
            b3 = BASE64ALPHABET[d3];
            b4 = BASE64ALPHABET[d4];
            decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
            decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
            decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);

        }
        return decodedData;
    }

    /**
     * remove WhiteSpace from MIME containing encoded Base64 data.
     *
     * @param data the byte array of base64 data (with WS)
     * @return the new length
     */
    private static int removeWhiteSpace(char[] data) {
        if (data == null) {
            return 0;
        }

        // count characters that's not whitespace
        int newSize = 0;
        int len = data.length;
        for (int i = 0; i < len; i++) {
            if (!isWhiteSpace(data[i])) {
                data[newSize++] = data[i];
            }
        }
        return newSize;
    }
}
