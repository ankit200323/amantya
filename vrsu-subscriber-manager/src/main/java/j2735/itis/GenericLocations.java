/*************************************************************/
/* Copyright (C) 2022 OSS Nokalva, Inc.  All rights reserved.*/
/*************************************************************/

/* THIS FILE IS PROPRIETARY MATERIAL OF OSS NOKALVA, INC.
 * AND MAY BE USED ONLY BY DIRECT LICENSEES OF OSS NOKALVA, INC.
 * THIS FILE MAY NOT BE DISTRIBUTED.
 * THIS COPYRIGHT STATEMENT MAY NOT BE REMOVED. */

/* Generated for: Amantya tech (Trial), License 84978Z 84978Z. */
/* Abstract syntax: j2735 */
/* ASN.1 Java project: j2735.J2735 */
/* Created: Sun Oct 16 13:39:13 2022 */
/* ASN.1 Compiler for Java version: 8.5.0.1 */
/* ASN.1 compiler options and file names specified:
 * -toed -output j2735 -uper -json -root -sampleCode pdus -messageFormat msvc
 * C:/Users/Admin/Downloads/J2735.asn
 */


package j2735.itis;

import com.oss.asn1.*;
import com.oss.metadata.*;
import java.io.IOException;
import com.oss.coders.EncoderException;
import com.oss.coders.DecoderException;
import com.oss.util.ExceptionDescriptor;
import com.oss.asn1printer.DataPrinter;
import com.oss.asn1printer.DataPrinterException;
import com.oss.coders.json.JsonWriter;
import com.oss.coders.json.JSONEncodable;
import com.oss.coders.json.JsonReader;
import com.oss.coders.json.JSONDecodable;
import com.oss.coders.json.JsonCoder;
import com.oss.coders.OutputBitStream;
import com.oss.coders.per.PEREncodable;
import com.oss.coders.InputBitStream;
import com.oss.coders.per.PERDecodable;
import com.oss.coders.per.PerCoder;

/**
 * Define the GenericLocations ASN.1 type included in the ITIS ASN.1 module.
 * @see Enumerated
 */

public final class GenericLocations extends Enumerated {
    
    /**
     * The default constructor.
     */
    private GenericLocations()
    {
	super(7937);
    }
    
    private GenericLocations(long value)
    {
	super(value);
    }
    
    /**
      An inner class that contains numeric values for ASN.1 ENUMERATED type.
      The values can be used in switch/case statements.
    */
    public static final class Value {
	public static final long on_bridges = 7937;
	public static final long in_tunnels = 7938;
	public static final long entering_or_leaving_tunnels = 7939;
	public static final long on_ramps = 7940;
	public static final long in_road_construction_area = 7941;
	public static final long around_a_curve = 7942;
	public static final long on_minor_roads = 7943;
	public static final long in_the_opposing_lanes = 7944;
	public static final long adjacent_to_roadway = 7945;
	public static final long on_bend = 7946;
	public static final long entire_intersection = 7947;
	public static final long in_the_median = 7948;
	public static final long moved_to_side_of_road = 7949;
	public static final long moved_to_shoulder = 7950;
	public static final long on_the_roadway = 7951;
	public static final long in_shaded_areas = 7952;
	public static final long in_low_lying_areas = 7953;
	public static final long in_the_downtown_area = 7954;
	public static final long in_the_inner_city_area = 7955;
	public static final long in_parts = 7956;
	public static final long in_some_places = 7957;
	public static final long in_the_ditch = 7958;
	public static final long in_the_valley = 7959;
	public static final long on_hill_top = 7960;
	public static final long near_the_foothills = 7961;
	public static final long at_high_altitudes = 7962;
	public static final long near_the_lake = 7963;
	public static final long near_the_shore = 7964;
	public static final long over_the_crest_of_a_hill = 7965;
	public static final long other_than_on_the_roadway = 7966;
	public static final long near_the_beach = 7967;
	public static final long near_beach_access_point = 7968;
	public static final long lower_level = 7969;
	public static final long upper_level = 7970;
	public static final long airport = 7971;
	public static final long concourse = 7972;
	public static final long gate = 7973;
	public static final long baggage_claim = 7974;
	public static final long customs_point = 7975;
	public static final long station = 7976;
	public static final long platform = 7977;
	public static final long dock = 7978;
	public static final long depot = 7979;
	public static final long ev_charging_point = 7980;
	public static final long information_welcome_point = 7981;
	public static final long at_rest_area = 7982;
	public static final long at_service_area = 7983;
	public static final long at_weigh_station = 7984;
	public static final long picnic_areas = 7985;
	public static final long rest_area = 7986;
	public static final long service_stations = 7987;
	public static final long toilets = 7988;
	public static final long on_the_right = 7989;
	public static final long on_the_left = 7990;
	public static final long in_the_center = 7991;
	public static final long in_the_opposite_direction = 7992;
	public static final long cross_traffic = 7993;
	public static final long northbound_traffic = 7994;
	public static final long eastbound_traffic = 7995;
	public static final long southbound_traffic = 7996;
	public static final long westbound_traffic = 7997;
	public static final long north = 7998;
	public static final long south = 7999;
	public static final long east = 8000;
	public static final long west = 8001;
	public static final long northeast = 8002;
	public static final long northwest = 8003;
	public static final long southeast = 8004;
	public static final long southwest = 8005;
	public static final long mountain_pass = 8006;
	public static final long reservation_center = 8007;
	public static final long nearby_basin = 8008;
	public static final long on_tracks = 8009;
	public static final long dip = 8010;
	public static final long traffic_circle = 8011;
	public static final long park_and_ride_lot = 8012;
	public static final long to = 8014;
	public static final long by = 8015;
	public static final long through = 8016;
	public static final long area_of = 8017;
	public static final long under = 8018;
	public static final long over = 8019;
	public static final long from = 8020;
	public static final long approaching = 8021;
	public static final long entering_at = 8022;
	public static final long exiting_at = 8023;
	public static final long across_tracks = 8024;
	public static final long in_street = 8025;
	public static final long on_curve = 8026;
	public static final long shoulder = 8027;
	public static final long crossover = 8028;
	public static final long cross_road = 8029;
	public static final long side_road = 8030;
	public static final long bus_stop = 8031;
	public static final long intersection = 8032;
	public static final long roadside_park = 8033;
	
    }
    // Named list definitions.
    
    /**
     * List of enumerators (reserved for internal use).
     * This member is reserved for internal use and must not be used in the application code.
     * @exclude
     */
    public final static GenericLocations cNamedNumbers[] = {
	new GenericLocations(), 
	new GenericLocations(7938), 
	new GenericLocations(7939), 
	new GenericLocations(7940), 
	new GenericLocations(7941), 
	new GenericLocations(7942), 
	new GenericLocations(7943), 
	new GenericLocations(7944), 
	new GenericLocations(7945), 
	new GenericLocations(7946), 
	new GenericLocations(7947), 
	new GenericLocations(7948), 
	new GenericLocations(7949), 
	new GenericLocations(7950), 
	new GenericLocations(7951), 
	new GenericLocations(7952), 
	new GenericLocations(7953), 
	new GenericLocations(7954), 
	new GenericLocations(7955), 
	new GenericLocations(7956), 
	new GenericLocations(7957), 
	new GenericLocations(7958), 
	new GenericLocations(7959), 
	new GenericLocations(7960), 
	new GenericLocations(7961), 
	new GenericLocations(7962), 
	new GenericLocations(7963), 
	new GenericLocations(7964), 
	new GenericLocations(7965), 
	new GenericLocations(7966), 
	new GenericLocations(7967), 
	new GenericLocations(7968), 
	new GenericLocations(7969), 
	new GenericLocations(7970), 
	new GenericLocations(7971), 
	new GenericLocations(7972), 
	new GenericLocations(7973), 
	new GenericLocations(7974), 
	new GenericLocations(7975), 
	new GenericLocations(7976), 
	new GenericLocations(7977), 
	new GenericLocations(7978), 
	new GenericLocations(7979), 
	new GenericLocations(7980), 
	new GenericLocations(7981), 
	new GenericLocations(7982), 
	new GenericLocations(7983), 
	new GenericLocations(7984), 
	new GenericLocations(7985), 
	new GenericLocations(7986), 
	new GenericLocations(7987), 
	new GenericLocations(7988), 
	new GenericLocations(7989), 
	new GenericLocations(7990), 
	new GenericLocations(7991), 
	new GenericLocations(7992), 
	new GenericLocations(7993), 
	new GenericLocations(7994), 
	new GenericLocations(7995), 
	new GenericLocations(7996), 
	new GenericLocations(7997), 
	new GenericLocations(7998), 
	new GenericLocations(7999), 
	new GenericLocations(8000), 
	new GenericLocations(8001), 
	new GenericLocations(8002), 
	new GenericLocations(8003), 
	new GenericLocations(8004), 
	new GenericLocations(8005), 
	new GenericLocations(8006), 
	new GenericLocations(8007), 
	new GenericLocations(8008), 
	new GenericLocations(8009), 
	new GenericLocations(8010), 
	new GenericLocations(8011), 
	new GenericLocations(8012), 
	new GenericLocations(8014), 
	new GenericLocations(8015), 
	new GenericLocations(8016), 
	new GenericLocations(8017), 
	new GenericLocations(8018), 
	new GenericLocations(8019), 
	new GenericLocations(8020), 
	new GenericLocations(8021), 
	new GenericLocations(8022), 
	new GenericLocations(8023), 
	new GenericLocations(8024), 
	new GenericLocations(8025), 
	new GenericLocations(8026), 
	new GenericLocations(8027), 
	new GenericLocations(8028), 
	new GenericLocations(8029), 
	new GenericLocations(8030), 
	new GenericLocations(8031), 
	new GenericLocations(8032), 
	new GenericLocations(8033)
    };
    public static final GenericLocations on_bridges = cNamedNumbers[0];
    public static final GenericLocations in_tunnels = cNamedNumbers[1];
    public static final GenericLocations entering_or_leaving_tunnels = cNamedNumbers[2];
    public static final GenericLocations on_ramps = cNamedNumbers[3];
    public static final GenericLocations in_road_construction_area = cNamedNumbers[4];
    public static final GenericLocations around_a_curve = cNamedNumbers[5];
    public static final GenericLocations on_minor_roads = cNamedNumbers[6];
    public static final GenericLocations in_the_opposing_lanes = cNamedNumbers[7];
    public static final GenericLocations adjacent_to_roadway = cNamedNumbers[8];
    public static final GenericLocations on_bend = cNamedNumbers[9];
    public static final GenericLocations entire_intersection = cNamedNumbers[10];
    public static final GenericLocations in_the_median = cNamedNumbers[11];
    public static final GenericLocations moved_to_side_of_road = cNamedNumbers[12];
    public static final GenericLocations moved_to_shoulder = cNamedNumbers[13];
    public static final GenericLocations on_the_roadway = cNamedNumbers[14];
    public static final GenericLocations in_shaded_areas = cNamedNumbers[15];
    public static final GenericLocations in_low_lying_areas = cNamedNumbers[16];
    public static final GenericLocations in_the_downtown_area = cNamedNumbers[17];
    public static final GenericLocations in_the_inner_city_area = cNamedNumbers[18];
    public static final GenericLocations in_parts = cNamedNumbers[19];
    public static final GenericLocations in_some_places = cNamedNumbers[20];
    public static final GenericLocations in_the_ditch = cNamedNumbers[21];
    public static final GenericLocations in_the_valley = cNamedNumbers[22];
    public static final GenericLocations on_hill_top = cNamedNumbers[23];
    public static final GenericLocations near_the_foothills = cNamedNumbers[24];
    public static final GenericLocations at_high_altitudes = cNamedNumbers[25];
    public static final GenericLocations near_the_lake = cNamedNumbers[26];
    public static final GenericLocations near_the_shore = cNamedNumbers[27];
    public static final GenericLocations over_the_crest_of_a_hill = cNamedNumbers[28];
    public static final GenericLocations other_than_on_the_roadway = cNamedNumbers[29];
    public static final GenericLocations near_the_beach = cNamedNumbers[30];
    public static final GenericLocations near_beach_access_point = cNamedNumbers[31];
    public static final GenericLocations lower_level = cNamedNumbers[32];
    public static final GenericLocations upper_level = cNamedNumbers[33];
    public static final GenericLocations airport = cNamedNumbers[34];
    public static final GenericLocations concourse = cNamedNumbers[35];
    public static final GenericLocations gate = cNamedNumbers[36];
    public static final GenericLocations baggage_claim = cNamedNumbers[37];
    public static final GenericLocations customs_point = cNamedNumbers[38];
    public static final GenericLocations station = cNamedNumbers[39];
    public static final GenericLocations platform = cNamedNumbers[40];
    public static final GenericLocations dock = cNamedNumbers[41];
    public static final GenericLocations depot = cNamedNumbers[42];
    public static final GenericLocations ev_charging_point = cNamedNumbers[43];
    public static final GenericLocations information_welcome_point = cNamedNumbers[44];
    public static final GenericLocations at_rest_area = cNamedNumbers[45];
    public static final GenericLocations at_service_area = cNamedNumbers[46];
    public static final GenericLocations at_weigh_station = cNamedNumbers[47];
    public static final GenericLocations picnic_areas = cNamedNumbers[48];
    public static final GenericLocations rest_area = cNamedNumbers[49];
    public static final GenericLocations service_stations = cNamedNumbers[50];
    public static final GenericLocations toilets = cNamedNumbers[51];
    public static final GenericLocations on_the_right = cNamedNumbers[52];
    public static final GenericLocations on_the_left = cNamedNumbers[53];
    public static final GenericLocations in_the_center = cNamedNumbers[54];
    public static final GenericLocations in_the_opposite_direction = cNamedNumbers[55];
    public static final GenericLocations cross_traffic = cNamedNumbers[56];
    public static final GenericLocations northbound_traffic = cNamedNumbers[57];
    public static final GenericLocations eastbound_traffic = cNamedNumbers[58];
    public static final GenericLocations southbound_traffic = cNamedNumbers[59];
    public static final GenericLocations westbound_traffic = cNamedNumbers[60];
    public static final GenericLocations north = cNamedNumbers[61];
    public static final GenericLocations south = cNamedNumbers[62];
    public static final GenericLocations east = cNamedNumbers[63];
    public static final GenericLocations west = cNamedNumbers[64];
    public static final GenericLocations northeast = cNamedNumbers[65];
    public static final GenericLocations northwest = cNamedNumbers[66];
    public static final GenericLocations southeast = cNamedNumbers[67];
    public static final GenericLocations southwest = cNamedNumbers[68];
    public static final GenericLocations mountain_pass = cNamedNumbers[69];
    public static final GenericLocations reservation_center = cNamedNumbers[70];
    public static final GenericLocations nearby_basin = cNamedNumbers[71];
    public static final GenericLocations on_tracks = cNamedNumbers[72];
    public static final GenericLocations dip = cNamedNumbers[73];
    public static final GenericLocations traffic_circle = cNamedNumbers[74];
    public static final GenericLocations park_and_ride_lot = cNamedNumbers[75];
    public static final GenericLocations to = cNamedNumbers[76];
    public static final GenericLocations by = cNamedNumbers[77];
    public static final GenericLocations through = cNamedNumbers[78];
    public static final GenericLocations area_of = cNamedNumbers[79];
    public static final GenericLocations under = cNamedNumbers[80];
    public static final GenericLocations over = cNamedNumbers[81];
    public static final GenericLocations from = cNamedNumbers[82];
    public static final GenericLocations approaching = cNamedNumbers[83];
    public static final GenericLocations entering_at = cNamedNumbers[84];
    public static final GenericLocations exiting_at = cNamedNumbers[85];
    public static final GenericLocations across_tracks = cNamedNumbers[86];
    public static final GenericLocations in_street = cNamedNumbers[87];
    public static final GenericLocations on_curve = cNamedNumbers[88];
    public static final GenericLocations shoulder = cNamedNumbers[89];
    public static final GenericLocations crossover = cNamedNumbers[90];
    public static final GenericLocations cross_road = cNamedNumbers[91];
    public static final GenericLocations side_road = cNamedNumbers[92];
    public static final GenericLocations bus_stop = cNamedNumbers[93];
    public static final GenericLocations intersection = cNamedNumbers[94];
    public static final GenericLocations roadside_park = cNamedNumbers[95];
    
    /**
     * Constant name list definition (reserved for internal use).
     * This member is reserved for internal use and must not be used in the application code.
     * @exclude
     */
    public final static String cConstantNameList[] = {
	"on-bridges",
	"in-tunnels",
	"entering-or-leaving-tunnels",
	"on-ramps",
	"in-road-construction-area",
	"around-a-curve",
	"on-minor-roads",
	"in-the-opposing-lanes",
	"adjacent-to-roadway",
	"on-bend",
	"entire-intersection",
	"in-the-median",
	"moved-to-side-of-road",
	"moved-to-shoulder",
	"on-the-roadway",
	"in-shaded-areas",
	"in-low-lying-areas",
	"in-the-downtown-area",
	"in-the-inner-city-area",
	"in-parts",
	"in-some-places",
	"in-the-ditch",
	"in-the-valley",
	"on-hill-top",
	"near-the-foothills",
	"at-high-altitudes",
	"near-the-lake",
	"near-the-shore",
	"over-the-crest-of-a-hill",
	"other-than-on-the-roadway",
	"near-the-beach",
	"near-beach-access-point",
	"lower-level",
	"upper-level",
	"airport",
	"concourse",
	"gate",
	"baggage-claim",
	"customs-point",
	"station",
	"platform",
	"dock",
	"depot",
	"ev-charging-point",
	"information-welcome-point",
	"at-rest-area",
	"at-service-area",
	"at-weigh-station",
	"picnic-areas",
	"rest-area",
	"service-stations",
	"toilets",
	"on-the-right",
	"on-the-left",
	"in-the-center",
	"in-the-opposite-direction",
	"cross-traffic",
	"northbound-traffic",
	"eastbound-traffic",
	"southbound-traffic",
	"westbound-traffic",
	"north",
	"south",
	"east",
	"west",
	"northeast",
	"northwest",
	"southeast",
	"southwest",
	"mountain-pass",
	"reservation-center",
	"nearby-basin",
	"on-tracks",
	"dip",
	"traffic-circle",
	"park-and-ride-lot",
	"to",
	"by",
	"through",
	"area-of",
	"under",
	"over",
	"from",
	"approaching",
	"entering-at",
	"exiting-at",
	"across-tracks",
	"in-street",
	"on-curve",
	"shoulder",
	"crossover",
	"cross-road",
	"side-road",
	"bus-stop",
	"intersection",
	"roadside-park"
    };
    
    
    /**
     * Returns the array of enumerators (reserved for internal use).
     * This method is reserved for internal use and must not be invoked from the application code.
     * @exclude
     */
    public Enumerated[] getNamedNumbers()
    {
	return cNamedNumbers;
    }
    
    /**
     * Returns the name of this enumerator.
     */
    public String name()
    {
	int index = indexOf();
	return (index < 0 || index >= 96 || cConstantNameList == null) ? null : cConstantNameList[index];
    }
    
    /**
     * This method is reserved for internal use and must not be invoked from the application code.
     * @exclude
     */
    public static int indexOfValue(long value)
    {
	if (value >= 7937 && value <= 8012)
	    return (int)(value - 7937);
	if (value >= 8014 && value <= 8033)
	    return (int)(value - 7938);
	else
	    return -1;
    }
    
    /**
     * Returns an enumerator with a specified value or null if the value
     * is not associated with any enumerators.
     *  @param value the value of the enumerator to return.
     *  @return an enumerator with a specified value.
     */
    
    public static GenericLocations valueOf(long value)
    {
	int inx = indexOfValue(value);
	
	if (inx < 0)
	    return null;
	else
	    return cNamedNumbers[inx];
    }
    
    /**
     * This method is reserved for internal use and must not be invoked from the application code.
     * @exclude
     */
    public static GenericLocations valueAt(int index)
    {
	if (index < 0)
	    throw new IndexOutOfBoundsException();
	else if (index >= 96)
	    return null;
	
	return cNamedNumbers[index];
    }
    
    /**
     * This method is reserved for internal use and must not be invoked from the application code.
     * @exclude
     */
    public int indexOf()
    {
	if (isUnknownEnumerator())
	    return -1;
	return indexOfValue(mValue);
    }
    
    /**
     * Clone 'this' object.
     */
    public GenericLocations clone() {
	return (GenericLocations)super.clone();
    }

    /**
     * Methods for "unknownEnumerator"
     */
    private static final GenericLocations cUnknownEnumerator = 
	new GenericLocations(-1);
    
    public boolean isUnknownEnumerator()
    {
	return this == cUnknownEnumerator;
    }
    
    /**
     * This method is reserved for internal use and must not be invoked from the application code.
     * @exclude
     */
    public static GenericLocations unknownEnumerator()
    {
	return cUnknownEnumerator;
    }
    
    /**
     * This method is reserved for internal use and must not be invoked from the application code.
     * @exclude
     */
    public GenericLocations getUnknownEnumerator()
    {
	return cUnknownEnumerator;
    }
    
} // End class definition for GenericLocations
