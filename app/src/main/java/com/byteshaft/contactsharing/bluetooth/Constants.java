package com.byteshaft.contactsharing.bluetooth;

class Constants {
    protected static final int CHUNK_SIZE = 4192;
    protected static final int HEADER_MSB = 0x10;
    protected static final int HEADER_LSB = 0x55;
    protected static final int HEADER_TEXT = 0x22;
    protected static final int HEADER_LTEXT = 0x60;
    protected static final String NAME = "Contact sharing";
    protected static final String UUID_STRING = "00001101-0000-1000-8000-00805F9B34AC";
}
