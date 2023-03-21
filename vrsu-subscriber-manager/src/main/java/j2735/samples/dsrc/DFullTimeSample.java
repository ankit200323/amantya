
/* THIS SAMPLE PROGRAM IS PROVIDED AS IS. THE SAMPLE PROGRAM AND ANY RESULTS
 * OBTAINED FROM IT ARE PROVIDED WITHOUT ANY WARRANTIES OR REPRESENTATIONS,
 * EXPRESS, IMPLIED OR STATUTORY. */

package j2735.samples.dsrc;

import java.util.Enumeration;
import java.io.PrintStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import com.oss.asn1.*;
import com.oss.util.*;
import j2735.dsrc.*;

/**
 * Define sample code for the DFullTime ASN.1 type included in the DSRC ASN.1 module.
 * @see DFullTime
 */

public class DFullTimeSample extends SampleUtil {

    /**
     * The default constructor. The class is not instantiable.
     */
    private DFullTimeSample() {}

    /**
     * Create Sample Value.
     */
    public static DFullTime createSampleValue()
    {
	DFullTime value = new DFullTime();
	value.setYear(new DYear(0));
	value.setMonth(new DMonth(0));
	value.setDay(new DDay(0));
	value.setHour(new DHour(0));
	value.setMinute(new DMinute(0));
	return value;
    }
    
    public static void printValue(DFullTime value, PrintStream s)
    {
	{
	    s.print("{");
	    ++indentlevel;
	    newline(s, indentlevel);
	    s.print("year ");
	    s.print(value.getYear().longValue());
	    s.print(",");
	    newline(s, indentlevel);
	    s.print("month ");
	    s.print(value.getMonth().longValue());
	    s.print(",");
	    newline(s, indentlevel);
	    s.print("day ");
	    s.print(value.getDay().longValue());
	    s.print(",");
	    newline(s, indentlevel);
	    s.print("hour ");
	    s.print(value.getHour().longValue());
	    s.print(",");
	    newline(s, indentlevel);
	    s.print("minute ");
	    s.print(value.getMinute().longValue());
	    newline(s, --indentlevel);
	    s.print("}");
	}
    }
    
    public static int encodeDecodeAndPrint(DFullTime value, int run)
    {
	Coder coder = j2735.J2735.getDefaultCoder();
	ByteArrayInputStream source;
	ByteArrayOutputStream sink;
	byte[] encoding = null;
	boolean passed = true;
	
	/* Print input value using AbstractData.toString() method*/
	System.out.println("\n--------------- Test run " + run + "---------------");
	System.out.println("\nEncoder input value:\n");
	System.out.print(value);
	
	/* Set coder properties */
	coder.enableEncoderDebugging();
	coder.enableDecoderDebugging();
	coder.enableEncoderConstraints();
	coder.enableDecoderConstraints();
	coder.enableAutomaticEncoding();
	coder.enableAutomaticDecoding();
	coder.enableContainedValueEncoding();
	coder.enableContainedValueDecoding();
	
	/* Encode the value */
	sink = new ByteArrayOutputStream();
	try {
	    System.out.print("\n\tTracing Information from Encoder...\n\n");
	    coder.encode(value, sink);
	    encoding = sink.toByteArray();
	    System.out.print("\nPDU successfully encoded, in " + encoding.length + " bytes:\n");
	    
	    if ((coder instanceof XERCoder)
		|| (coder instanceof CXERCoder)) {
		System.out.write(encoding, 0, encoding.length);
	    } else {
		HexTool.printHex(encoding);
	    }
	} catch(EncodeFailedException e) {
	    System.out.println("Encoding failed with return code = " + e.getReason());
	    System.out.print(e);
	    passed = false;
	} catch(EncodeNotSupportedException e) {
	    System.out.println("Encoding not supported for the value");
	    System.out.print(e);
	    passed = false;
	}
	
	if (!passed)
	    return 1;
	
	/* Decode the PDU that was just encoded */
	source = new ByteArrayInputStream(encoding);
	DFullTime decoded = null;
	try {
	    System.out.print("\n\tTracing Information from Decoder...\n\n");
	    decoded = (DFullTime)coder.decode(source, value);
	    System.out.print("\nPDU successfully decoded.\n");
	} catch (DecodeFailedException e) {
	    System.out.println("Decoding failed with return code = " + e.getReason());
	    System.out.print(e);
	    passed = false;
	} catch (DecodeNotSupportedException e) {
	    System.out.println("Decoding not supported for the value");
	    System.out.print(e);
	    passed = false;
	}
	
	if (!passed)
	    return 1;
	/* Print decoded value using sample printValue() method */
	System.out.print("\n\tDecoded PDU...\n\n");
	printValue(decoded, System.out);
	System.out.print("\n");
	
	return 0;
    }
    
    public static void main(String[] arg)
    {
	int run = 0;
	int failures = 0;
	
	failures += encodeDecodeAndPrint(createSampleValue(), ++run);
	newline(System.out, 0);
	
	if (failures > 0)
	    System.out.println(failures + " values failed.");
	else
	    System.out.println("All values encoded and decoded successfully.");
    }
    
}
