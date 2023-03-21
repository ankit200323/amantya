package com.vrsu.subscriber.mqttdatamodel;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;

/*****************************************************************************/
/* Copyright (C) ###RELEASE_YEAR### OSS Nokalva, Inc.  All rights reserved.                */
/*****************************************************************************/
/* THIS FILE IS PROPRIETARY MATERIAL OF OSS NOKALVA, INC.                    */
/* AND MAY BE USED ONLY BY DIRECT LICENSEES OF OSS NOKALVA, INC.             */
/* THIS FILE MAY NOT BE DISTRIBUTED.                                         */
/* THIS COPYRIGHT STATEMENT MAY NOT BE REMOVED.                              */
/*****************************************************************************/
/* THIS SAMPLE PROGRAM IS PROVIDED AS IS. THE SAMPLE PROGRAM AND ANY RESULTS */
/* OBTAINED FROM IT ARE PROVIDED WITHOUT ANY WARRANTIES OR REPRESENTATIONS,  */
/* EXPRESS, IMPLIED OR STATUTORY.                                            */
/*****************************************************************************/
/*
 * $Id: bfde6f804fa8fea16200fa42390e93b0087d86b3 $
 */

/*
 * Demonstrates decoding of the DP
x
cSRC MessageFrame PDU containing the
 * BasicSafetyMessage.
 */

import com.oss.asn1.Coder;
import com.oss.asn1.DecodeFailedException;
import com.oss.asn1.DecodeNotSupportedException;
import com.oss.util.ASN1ValueFormat;

import j2735.J2735;
import j2735.dsrc.BSMcoreData;
import j2735.dsrc.BasicSafetyMessage;
import j2735.dsrc.MessageFrame;
import j2735.dsrc.PathHistory;
import j2735.dsrc.PathHistoryPoint;
import j2735.dsrc.VehicleSafetyExtensions;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BSMUPERDecoder {

    static Coder coder;
    static String border = "-------------------------------------------------------";

    public BSMUPERDecoder() {
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

    public BasicSafetyMessage decodeBSM(InputStream in){


    	BasicSafetyMessage basicSafetyMessage = null;
        /* * Decode and process the MessageFrame PDU.*/

        try (BufferedInputStream source = new BufferedInputStream(in)) {
            MessageFrame msg = coder.decode(source, new MessageFrame());
            basicSafetyMessage = processMessageFrame(msg);
        } catch (DecodeNotSupportedException | DecodeFailedException | IOException e) {
        	log.info("Decoding failed: " + e);
        }
        return basicSafetyMessage;
    }

    // Demonstrates how particular components of the BasicSafetyMessage are
    // accessed. Namely, the BSMcoreData and the crumb data (if present)
    private static BasicSafetyMessage processMessageFrame(MessageFrame msg) {

         /** Print the decoded MessageFrame PDU.*/

    	BasicSafetyMessage basicSafetyMessage = null;
    	log.info("\nDecoded PDU...\n");
    	log.info(""+msg);

            /* * Access the BSM core data*/

    	log.info("The decoded PDU contains the BasicSafetyMessage.");
    	log.info("Access and display the BSM core data " +
                    "and the crumb data");
            basicSafetyMessage  = (BasicSafetyMessage) msg.getValue().getDecodedValue();
            BSMcoreData bsmCoreData = basicSafetyMessage.getCoreData();
            log.info("The BSM core data:");
            log.info(""+bsmCoreData);
            log.info("BSM Latitude value: "+ bsmCoreData.getLat().toString());
            if (basicSafetyMessage.hasRegional())
            	log.info("The BSM contains regional extensions.");
            else
            	log.info("The BSM does not contain regional extensions.");

            return basicSafetyMessage;
        }
}


