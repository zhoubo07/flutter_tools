package com.dengyun.baselibrary.utils.encode.base64;

/**
 * @author yangshu
 * @date 2020/9/22 17:48
 */
public interface Encoder {

    /**
     * Encodes an "Object" and returns the encoded content as an Object. The Objects here may just be
     * <code>byte[]</code> or <code>String</code>s depending on the implementation used.
     *
     * @param source
     *            An object to encode
     * @return An "encoded" Object
     * @throws EncoderException
     *             An encoder exception is thrown if the encoder experiences a failure condition during the encoding
     *             process.
     */
    Object encode(Object source) throws EncoderException;
}
