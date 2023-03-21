package com.vrsu.subscriber.mqttdatamodel;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.oss.asn1.Coder;
import com.oss.asn1.DecodeFailedException;
import com.oss.asn1.DecodeNotSupportedException;

import j2735.J2735;
import j2735.dsrc.EmergencyVehicleAlert;
import j2735.dsrc.MessageFrame;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EVAJERDecoder {

    static Coder coder;
    static String border = "-------------------------------------------------------";

    public EVAJERDecoder() {
        // Initialize the project
        try {
        	J2735.initialize();
        } catch (Exception e) {
        	log.info("Initialization exception: " + e);
        }

        coder = J2735.getJSONCoder();
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

    public EmergencyVehicleAlert decodeJEREVA(InputStream in) {

        EmergencyVehicleAlert evadata=null;

        /*
         * Decode and process the MessageFrame PDU.
         */
        try (BufferedInputStream source = new BufferedInputStream(in)) {
            MessageFrame msg = coder.decode(source, new MessageFrame());
            evadata= processMessageFrame(msg);
        } catch (DecodeNotSupportedException | DecodeFailedException | IOException e) {
            System.out.println("Decoding failed: " + e);
        }
        return evadata;
    }

    // Demonstrates how particular components of the BasicSafetyMessage are
    // accessed. Namely, the BSMcoreData and the crumb data (if present)
    private static EmergencyVehicleAlert processMessageFrame(MessageFrame msg) {
        /*
         * Print the decoded MessageFrame PDU.
         */
        EmergencyVehicleAlert evadata=null;
        log.info("\nDecoded PDU...\n");
        log.info(""+msg);

        if (msg.getValue().getTypeName()==EmergencyVehicleAlert.class.getName()) {
            /*
             * Print the decoded MessageFrame PDU.
             */
        	log.info("\nDecoded PDU...\n");
        	log.info(""+msg);
            evadata = (EmergencyVehicleAlert) msg.getValue().getDecodedValue();
            log.info("########", "processMessageFrame: "+evadata.timeStamp);


        }
        return evadata;

    }
}


