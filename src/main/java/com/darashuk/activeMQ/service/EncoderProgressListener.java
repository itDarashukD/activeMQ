package com.darashuk.activeMQ.service;

public interface EncoderProgressListener {

    public void encode(java.io.File source,
                       java.io.File target,
                       it.sauronsoftware.jave.EncodingAttributes attributes,
                       it.sauronsoftware.jave.EncoderProgressListener listener)
            throws java.lang.IllegalArgumentException,
            it.sauronsoftware.jave.InputFormatException,
            it.sauronsoftware.jave.EncoderException;
}
