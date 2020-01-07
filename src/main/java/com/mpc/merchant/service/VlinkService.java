package com.mpc.merchant.service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.ISOSource;
import org.jpos.iso.channel.ASCIIChannel;
import org.jpos.iso.packager.GenericPackager;
import org.jpos.transaction.TransactionParticipant;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;

public class VlinkService implements TransactionParticipant {
    private Logger log = LogManager.getLogger(getClass());

    @Override
    public int prepare(long id, Serializable context) {
        return 0;
    }

    @Override
    public void commit(long id, Serializable context) {

    }

    @Override
    public void abort(long id, Serializable context) {

    }
}
