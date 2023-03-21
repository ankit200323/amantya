/*************************************************************/
/* Copyright (C) 2023 OSS Nokalva, Inc.  All rights reserved.*/
/*************************************************************/

/* THIS FILE IS PROPRIETARY MATERIAL OF OSS NOKALVA, INC.
 * AND MAY BE USED ONLY BY DIRECT LICENSEES OF OSS NOKALVA, INC.
 * THIS FILE MAY NOT BE DISTRIBUTED.
 * THIS COPYRIGHT STATEMENT MAY NOT BE REMOVED. */

/* Generated for: Amantya Technologies Inc., Wilmington, Delaware - Project Start-up, Expiring January 31, 2024, License 17530 17530,
 * only for project "Development and support of Automotive Data Orchestration Platform (ADOP) for client Verizon". */
/* Abstract syntax: j2735 */
/* ASN.1 Java project: j2735.J2735 */
/* Created: Tue Feb 28 15:03:34 2023 */
/* ASN.1 Compiler for Java version: 8.6 */
/* ASN.1 compiler options and file names specified:
 * -toed -output j2735 -uper -root -sampleCode pdus -messageFormat msvc
 * C:/Users/CHAUHAN/Downloads/J2735.asn
 */


package j2735;

import com.oss.asn1.ASN1Project;
import com.oss.asn1.Coder;
import com.oss.asn1.JSONCoder;
import com.oss.asn1.PERAlignedCoder;
import com.oss.asn1.PERUnalignedCoder;
import com.oss.metadata.ProjectInfo;

/**
  A class for the J2735 ASN.1/Java  project.
*/
public class J2735 extends ASN1Project {

    /**
     * Initialize the pdu decoder.
     */
    private static final ProjectInfo c_projectInfo = new ProjectInfo (
	null,
	new byte[] {
	    (byte)0x0b, (byte)0x77, (byte)0x48, (byte)0x19, (byte)0xa2,
	    (byte)0x81, (byte)0x33, (byte)0xc5, (byte)0x20, (byte)0x01,
	    (byte)0x2a, (byte)0x01, (byte)0x45, (byte)0x3b, (byte)0x93,
	    (byte)0x13, (byte)0x7d, (byte)0x28, (byte)0x9c, (byte)0x52,
	    (byte)0x8c, (byte)0xc9, (byte)0x9e, (byte)0xa1, (byte)0x1f,
	    (byte)0xbd, (byte)0xbe, (byte)0x27, (byte)0x60, (byte)0xf9,
	    (byte)0x7d, (byte)0x9a, (byte)0x35, (byte)0x1c, (byte)0x7d,
	    (byte)0x11, (byte)0x98, (byte)0xe8, (byte)0x07, (byte)0xb5,
	    (byte)0x1f, (byte)0x87, (byte)0x5b, (byte)0x82, (byte)0x58,
	    (byte)0x70, (byte)0x59, (byte)0x0a, (byte)0x3e, (byte)0x2c,
	    (byte)0xd7, (byte)0xcd, (byte)0xda, (byte)0x49, (byte)0xe4,
	    (byte)0xff, (byte)0x01, (byte)0x1f, (byte)0x8b, (byte)0xbd,
	    (byte)0xee, (byte)0x1e, (byte)0x8f, (byte)0x72, (byte)0x1c,
	    (byte)0xba, (byte)0xb3, (byte)0xfa, (byte)0x1a, (byte)0xaa,
	    (byte)0x28, (byte)0xeb, (byte)0xfc, (byte)0x16, (byte)0xc6,
	    (byte)0xce, (byte)0xfb, (byte)0x9a, (byte)0x3b, (byte)0xd6,
	    (byte)0x68, (byte)0x55, (byte)0xa8, (byte)0x7f, (byte)0x5a,
	    (byte)0x5a, (byte)0x97, (byte)0x0b, (byte)0x9f, (byte)0xa9,
	    (byte)0xac, (byte)0xaf, (byte)0x33, (byte)0x55, (byte)0xe5,
	    (byte)0xce, (byte)0x69, (byte)0x64, (byte)0xe5, (byte)0xe1,
	    (byte)0xde, (byte)0x61, (byte)0xe0, (byte)0xb5, (byte)0x5f,
	    (byte)0x61, (byte)0x50, (byte)0xac, (byte)0x35, (byte)0x9d,
	    (byte)0x39, (byte)0x0c, (byte)0x4f, (byte)0xd3, (byte)0xb8,
	    (byte)0xc2, (byte)0x30, (byte)0xf6, (byte)0x84, (byte)0x89,
	    (byte)0x5a, (byte)0xd7, (byte)0x3e, (byte)0x9c, (byte)0xf1,
	    (byte)0xc2, (byte)0x5a, (byte)0x3e, (byte)0x33, (byte)0x86,
	    (byte)0x5a, (byte)0x84, (byte)0xf5, (byte)0x0a, (byte)0xf2,
	    (byte)0xc4, (byte)0xd1, (byte)0x79, (byte)0x48, (byte)0x06,
	    (byte)0x6c, (byte)0x97, (byte)0x8b, (byte)0xd9, (byte)0xf3,
	    (byte)0x9c, (byte)0x7e, (byte)0x84, (byte)0xeb, (byte)0x79,
	    (byte)0x7b, (byte)0xeb, (byte)0xe7, (byte)0x20, (byte)0xb9,
	    (byte)0xcb, (byte)0x3c, (byte)0xf5, (byte)0x35, (byte)0x60,
	    (byte)0xab, (byte)0xcd, (byte)0xbf, (byte)0xa7, (byte)0xc6,
	    (byte)0x9d, (byte)0xa1, (byte)0xea, (byte)0xd1, (byte)0xc4,
	    (byte)0xd0, (byte)0x99, (byte)0x8e, (byte)0x3a, (byte)0xde,
	    (byte)0xdf, (byte)0x82, (byte)0xb6, (byte)0x4e, (byte)0xb7,
	    (byte)0x90, (byte)0x8b, (byte)0xde, (byte)0xb4, (byte)0xfc,
	    (byte)0xd9, (byte)0x76, (byte)0xc5, (byte)0xf4, (byte)0x3c,
	    (byte)0x87, (byte)0xce, (byte)0xeb, (byte)0x82, (byte)0x19,
	    (byte)0x94, (byte)0x3a, (byte)0x09, (byte)0x40, (byte)0x3d,
	    (byte)0x9a, (byte)0xf9, (byte)0xb0, (byte)0x52, (byte)0xe2,
	    (byte)0x96, (byte)0xb8, (byte)0xd5, (byte)0xce, (byte)0x59,
	    (byte)0xf9, (byte)0xa2, (byte)0x29, (byte)0x05, (byte)0xf6,
	    (byte)0x2c, (byte)0x77, (byte)0x1f, (byte)0xee, (byte)0x97,
	    (byte)0x54, (byte)0x7f, (byte)0x13, (byte)0x8a, (byte)0x12,
	    (byte)0xef, (byte)0x57, (byte)0xd6, (byte)0xe0, (byte)0x3e,
	    (byte)0x7a, (byte)0xd8, (byte)0x1f, (byte)0xa1, (byte)0x9e,
	    (byte)0xc9, (byte)0x84, (byte)0xa4, (byte)0x39, (byte)0x53,
	    (byte)0xd8, (byte)0xcf, (byte)0x36, (byte)0xb9, (byte)0x87,
	    (byte)0x83, (byte)0xbb, (byte)0xad, (byte)0x79, (byte)0x4f,
	    (byte)0xe4, (byte)0x98
	},
	"2024/02/07"
    );
    
    /**
     * Get the project descriptor of 'this' object.
     */
    @Override
	public ProjectInfo getProjectInfo()
    {
	return c_projectInfo;
    }
    
    private static final ASN1Project c_project = new J2735();

    /**
     * Methods for accessing Coders.
     */
    public static Coder getDefaultCoder()
    {
	return createPERUnalignedCoder(c_project);
    }
    
    public static PERAlignedCoder getPERAlignedCoder()
    {
	return createPERAlignedCoder(c_project);
    }
    
    public static PERUnalignedCoder getPERUnalignedCoder()
    {
	return createPERUnalignedCoder(c_project);
    }
    public static JSONCoder getJSONCoder() {
        return createJSONCoder(c_project);
    }
}
