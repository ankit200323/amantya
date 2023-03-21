package com.vrsu.subscriber.mqttdatamodel;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;

import com.oss.asn1.Coder;
import com.oss.asn1.DecodeFailedException;
import com.oss.asn1.DecodeNotSupportedException;

import j2735.J2735;
import j2735.dsrc.MessageFrame;
import j2735.dsrc.SPAT;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SPATUPERDecoder {

    static Coder coder;
    static String border = "-------------------------------------------------------";

    public SPATUPERDecoder() {
        // Initialize the project
        try {
        	J2735.initialize();
        } catch (Exception e) {
        	log.info("Initialization exception: " + e);
        }

        coder = J2735.getPERUnalignedCoder();
        coder.enableAutomaticEncoding();
        coder.enableAutomaticDecoding();
        coder.enableEncoderDebugging();
        coder.enableDecoderDebugging();

        // Enable relaxed decoding mode if needed
        String relax = System.getProperty("oss.samples.relaxedmode");
        if (relax != null && relax.equalsIgnoreCase("on")) {
            coder.enableRelaxedDecoding();
        }


    }

    public SPAT decodeJERSPAT(InputStream in) {
        SPAT spat = null;
        /*;
         * Decode and process the MessageFrame PDU.
         */
        try (BufferedInputStream source = new BufferedInputStream(in)) {
            MessageFrame msg = coder.decode(source, new MessageFrame());
            spat = processMessageFrame(msg);
        } catch (DecodeNotSupportedException | DecodeFailedException | IOException e) {
        	log.info("Decoding failed: " + e);
        }
        return spat;
    }

    public SPAT decodeSPAT(InputStream in) {
        SPAT spat = null;
        /*;
         * Decode and process the MessageFrame PDU.
         */
        try (BufferedInputStream source = new BufferedInputStream(in)) {
            MessageFrame msg = coder.decode(source, new MessageFrame());
            spat = processMessageFrame(msg);
        } catch (DecodeNotSupportedException | DecodeFailedException | IOException e) {
        	log.info("Decoding failed: " + e);
        }
        return spat;
    }

    // Demonstrates how particular components of the BasicSafetyMessage are
    // accessed. Namely, the BSMcoreData and the crumb data (if present)
    private static SPAT processMessageFrame(MessageFrame msg) {
        /*
         * Print the decoded MessageFrame PDU.
         */
        SPAT spat = null;
        log.info("\nDecoded PDU...\n"+msg.getValue().getTypeName() + " "+msg.getValue().getClass().getName());
        log.info(""+msg);



        spat = (SPAT) msg.getValue().getDecodedValue();



        return spat;
    }
}
