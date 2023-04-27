package com.dengyun.baselibrary.utils.encode.base64;

/**
 * @author yangshu
 * @date 2020/9/22 17:50
 */
public interface BinaryDecoder extends Decoder {

    /**
     * Decodes a byte array and returns the results as a byte array.
     *
     * @param source
     *            A byte array which has been encoded with the appropriate encoder
     * @return a byte array that contains decoded content
     * @throws DecoderException
     *             A decoder exception is thrown if a Decoder encounters a failure condition during the decode process.
     */
    byte[] decode(byte[] source) throws DecoderException;
}
