/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.util;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.json.JsonValue;

/**
 * The <a href="http://www.delayattributionboard.co.uk/documents/dag_pdac/April%202013%20DAG.pdf">Delay Attribution
 * Guide</a> contains a list of delay attribution codes and reasons. The same codes are used in Train Cancellation
 * messages sent on the Train Movements feed. The current (April 2013) codes are listed here for reference.
 * <p>
 * Codes are grouped in to several sections:
 * <ul>
 * <li>A* - Freight terminal operating company causes</li>
 * <li>F* - Freight operating company causes</li>
 * <li>I* and J* - Infrastructure causes</li>
 * <li>M* and N* - Mechanical and Fleet Engineer causes</li>
 * <li>O* - Network Rail operating causes</li>
 * <li>P* - Planned or Excluded delays or cancellations</li>
 * <li>Q* - Network Rail non-operating causes</li>
 * <li>R* - Station operating company causes</li>
 * <li>T* - Passenger operating company causes</li>
 * <li>V* - Passenger Charter excludable (TOC responsibility) causes</li>
 * <li>X* - Passenger Charter excludable (Network Rail responsibility) causes</li>
 * <li>Y* - Reactionary delays</li>
 * <li>Z* - Unexplained delays and cancellations</li>
 * </ul>
 * <p>
 * @author Peter T Mount
 */
public enum DelayAttributionCode
{

    AA( "AA", "Waiting Terminal/Yard acceptance", "ACCEPTANCE" ),
    AB( "AB", "Waiting Customer release of documentation", "DOCUMENTS" ),
    AC( "AC", "Waiting train preparation or completion of TOPS list/RT3973", "TRAIN PREP" ),
    AD( "AD", "Terminal/Yard staff shortage including reactionary congestion caused by the shortage", "WTG STAFF" ),
    AE( "AE", "Congestion in Terminal/Yard", "CONGESTION" ),
    AF( "AF", "Terminal/Yard equipment failure - cranes etc", "EQUIPMENT" ),
    AG( "AG", "Adjusting Loaded wagons", "LOAD INCDT" ),
    AH( "AH", "Customer equipment breakdown/reduced capacity", "BREAKDOWN" ),
    AJ( "AJ", "Waiting Customer's traffic including ship/road/air connections and Mail deliveries.", "TRAFFIC" ),
    AK( "AK",
        "Fire in freight yard / terminal (including private sidings, and stations Ð where it affects FOC services)",
        "INF FIRE" ),
    AX( "AX", "Failure of FOC-owned infrastructure", "FOC INFRA" ),
    AY( "AY", "Mishap in Terminal/Yard or on Terminal/Yard infrastructure", "FTO MISHAP" ),
    AZ( "AZ", "Other Freight Operating Company, cause to be specified", "FTO OTHER" ),
    FA( "FA", "Dangerous goods incident", "DGI INCDT" ),
    FB( "FB", "Train stopped on route due to incorrect marshalling", "REMARSHALL" ),
    FC( "FC", "Freight train driver", "FCDRIVER" ),
    FD( "FD", "Booked loco used on additional/other service", "LOCO USED" ),
    FE( "FE", "Traincrew rostering error/not available, including crew relief errors", "NO T/CREW" ),
    FG( "FG", "Driver adhering to company professional driving standards or policy", "PRO DVR" ),
    FH( "FH", "Train crew/loco diagram/planning error (See also DAG Section 4.31.2)", "DIAG ERROR" ),
    FI( "FI",
        "Delay in running due to the incorrect operation of the on- board ETCS/ ERTMS equipment Ð i.e. wrong input by driver.",
        "ETCS INPUT" ),
    FJ( "FJ", "Train held at Customer's request", "RETIME REQ" ),
    FK( "FK", "Train diverted/re-routed at Customer's request", "DIVERT REQ" ),
    FL( "FL", "Train cancelled at Customer's request", "CANCEL REQ" ),
    FM( "FM", "Tail lamp/head lamp out or incorrectly shown", "TAIL LAMP" ),
    FN( "FN", "Late presentation from Europe", "LATE CHUNL" ),
    FO( "FO", "Delay in running believed to be due to Operator, but no information available from that Operator",
        "FOC UNEX" ),
    FP( "FP", "Incorrect route taken or route wrongly challenged by driver, including SPADÕs", "FTO MISRTE" ),
    FS( "FS", "Delay due to ETCS/ ERTMS on-board overriding driver command", "ETCS OVRD" ),
    FT( "FT", "Freight Operator autumn-attribution Neutral Zone delays", "LF NEUTRAL" ),
    FU( "FU", "Formal Inquiry Incident - possible Operator responsibility", "JOINT INQ" ),
    FW( "FW", "Late start/yard overtime not explained by Operator", "LATE START" ),
    FX( "FX", "Freight train running at lower than planned classification", "LOW CLASS" ),
    FY( "FY", "Mishap caused by Freight Operating Company or on FOC-owned infrastructure", "FOC MISHAP" ),
    FZ( "FZ",
        "Other Freight Operating Company causes, including Freight Operating Company Control directive, cause to be specified",
        "FOC OTHER" ),
    I0( "I0", "Telecom equipment failures legacy (inc. NRN/CSR/RETB link)", "RADIO FLR" ),
    I1( "I1", "Overhead line/third rail defect", "OHL/3 RAIL" ),
    I2( "I2", "AC/DC trip", "AC/DC TRIP" ),
    I3( "I3", "Obstruction on OHL, cause of which is not known", "ON OHL" ),
    I4( "I4", "OHL/third rail power supply failure/reduction", "SUPPLY FLR" ),
    I5( "I5", "Possession over-run from planned work", "OVERRUN" ),
    I6( "I6", "Track Patrolling", "TRK PATROL" ),
    I7( "I7", "Engineer's train late/failed in possession", "ENGNRS TRN" ),
    I8( "I8", "Animal Strike/Incursion within the control of Network Rail", "ANIMAL" ),
    I9( "I9", "Fires starting on Network Rail Infrastructure", "NR FIRE" ),
    IA( "IA", "Signal failure", "SIGNAL FLR" ),
    IB( "IB", "Points failure", "POINTS FLR" ),
    IC( "IC", "Track circuit failure", "TC FAILURE" ),
    ID( "ID", "Level crossing failure incl. barrow/foot crossings and crossing treadles", "LEVEL XING" ),
    IE( "IE", "Power failure", "POWER FLR" ),
    IF( "IF", "Train Describer/Panel/ARS/SSI/TDM/Remote Control failure", "PANEL/TDM/FLR" ),
    IG( "IG", "Block failure", "BLOCK FLR" ),
    II( "II", "Power Supply cable fault/fire due to cable fault", "CABLE FLR" ),
    IJ( "IJ", "AWS/ATP/TPWS/Train Stop/On-track equipment failure", "AWS/ATP" ),
    IK( "IK", "Telephone equipment failure", "PHONE/SPT" ),
    IL( "IL", "Token equipment failure", "TOKEN FLR" ),
    IM( "IM", "Infrastructure Balise Failure", "BALISE" ),
    IN( "IN", "HABD/Panchex/WILD/Wheelchex", "HABD FAULT" ),
    IO( "IO", "No fault found/HABD/Panchex/WILD/Wheelchex", "NFF" ),
    IP( "IP",
        "Points failure caused by snow or frost where heaters are fitted but found to be not operative or defective",
        "PNT HEATER" ),
    IQ( "IQ", "Trackside sign blown down/light out etc", "TRACK SIGN" ),
    IR( "IR", "Broken/cracked/twisted/buckled/flawed rail", "RAIL FLAW" ),
    IS( "IS", "Track defects (other than rail defects i.e. fish plates, wet beds etc)", "TRACK FLT" ),
    IT( "IT", "Bumps reported - cause not known", "BUMP RPRTD" ),
    IU( "IU", "Engineers on-track plant affecting possession", "ONTRACK EQ" ),
    IV( "IV", "Earthslip/subsidence/breached sea defences not the result of severe weather", "EARTHSLIP" ),
    IW( "IW", "Non severe- Snow/Ice/Frost affecting infrastructure equipment", "COLD" ),
    IY( "IY", "Mishap - Network Rail causes", "INF MISHAP" ),
    IZ( "IZ", "Other infrastructure causes", "INF OTHER" ),
    J0( "J0", "Telecom radio failures IVRS/GSM-R", "GSM-R FLR" ),
    J2( "J2", "TRTS Failure", "TRTS FLR" ),
    J3( "J3", "Axle Counter Failure", "AXLE FLR" ),
    J4( "J4", "Safety Issue No Fault Found", "INF NFF" ),
    J5( "J5", "NR DOO monitor/mirror failure", "DOO MON FLR" ),
    J6( "J6", "Lightning strike against unprotected assets", "LIGHTNING" ),
    J7( "J7", "ETCS/ ERTMS Equipment Failure (excluding communications link and balises)", "ETCS FLR" ),
    J8( "J8", "Damage to infrastructure caused by on-track machine whilst operating in a possession", "ONTRK DMG" ),
    JA( "JA", "TSR speeds for Track-work outside the Rules of the Route", "TSR O-ROTR" ),
    JB( "JB", "Reactionary Delay to 'P' coded TSR", "PLND TSR" ),
    JC( "JC", "Telecom cable failure (transmission sys & cable failures )", "COMM LINKS" ),
    JD( "JD", "Bridges/tunnels/buildings (other than bridge strikes)", "STRUCTURES" ),
    JG( "JG", "ESR/TSR due to cancelled possession/work not completed", "ESR/TSR" ),
    JH( "JH", "Critical Rail Temperature speeds, (other than buckled rails)", "HEAT SPEED" ),
    JI( "JI", "Swing/lifting bridge failure", "SWING BDGE" ),
    JK( "JK", "Flooding not due to exceptional weather", "FLOODING" ),
    JL( "JL", "Network Rail/TRC Staff error", "STAFF" ),
    JM( "JM", "Change of Signal Aspects - no fault found", "ASPECT CHG" ),
    JN( "JN", "Possession cancellation", "POSSN CANC" ),
    JO( "JO", "Rolling Contact Fatigue", "RCF" ),
    JP( "JP",
        "Failure to maintain vegetation within network boundaries in accordance with prevailing Network Rail standards",
        "VEG STD" ),
    JQ( "JQ", "Trains striking overhanging branches/vegetation (not weather- related)", "TREE OHANG" ),
    JR( "JR", "Signals/track signs obscured by vegetation", "HIDDEN SIG" ),
    JS( "JS", "Condition of Track TSR Outside Rules of Route", "COTTSR ORR" ),
    JT( "JT", "Points failure caused by snow or frost where heaters are not fitted", "NO PNT HTR" ),
    JX( "JX",
        "Miscellaneous items (including trees) causing obstructions, not the result of trespass, vandalism, weather or fallen/thrown from trains",
        "MISC OBS" ),
    M0( "M0", "Safety systems failure (DSD/OTMR/Vigilance)", "DSD" ),
    M1( "M1", "Pantograph fault or PANCHEX activation (positive)", "PANTOGRAPH" ),
    M2( "M2", "Automatic Dropper Device activation", "ADD" ),
    M3( "M3", "Diesel loco failure/defect/attention: other", "DIESL OTH" ),
    M4( "M4", "EMU failure/defect/attention: brakes", "EMU BRAKE" ),
    M5( "M5", "EMU failure/defect/attention: doors (including SDO equipment failure)", "EMU DOOR" ),
    M6( "M6", "EMU failure/defect/attention: other", "EMU OTHER" ),
    M7( "M7",
        "DMU (inc. HST/MPV) failure/defect/attention: doors (including SDO equipment failure and excluding Railhead Conditioning trains).",
        "DMU DOOR" ),
    M8( "M8", "DMU (inc. HST/MPV) failure/defect/attention: other (excluding Railhead Conditioning trains)", "DMU OTHER" ),
    M9( "M9", "Reported fleet equipment defect - no fault found", "NFF" ),
    MA( "MA", "Electric loco (inc. IC225) failure/defect/attention: brakes", "ELEC BRAKE" ),
    MB( "MB", "Electric loco (inc. IC225) failure/defect/attention: traction", "ELEC TRAC" ),
    MC( "MC", "Diesel loco failure/defect/attention: traction", "DIESL TRAC" ),
    MD( "MD", "DMU (inc. HST)/MPV failure/defect/attention: traction (excluding Railhead Conditioning trains)",
        "DMU TRAC" ),
    ME( "ME", "Steam locomotive failure/defect/attention", "STEAM LOCO" ),
    MF( "MF", "International/Channel Tunnel locomotive failure/defect/attention", "CHUNL LOCO" ),
    MG( "MG", "Coach (inc. Intl/IC225) failure/defect/attention: brakes", "COACH BRKE" ),
    MH( "MH", "Coach (inc. Intl/IC225) failure/defect/attention: doors", "COACH DOOR" ),
    MI( "MI", "Coach (inc. Intl/IC225) failure/defect/attention: other", "COACH OTHR" ),
    MJ( "MJ", "Parcels vehicle failure/defect/attention", "PARCEL VEH" ),
    MK( "MK", "DVT/PCV failure/defect/attention", "DVT PCV" ),
    ML( "ML", "Freight vehicle failure/defect attention (inc. private wagons)", "FRGHT VEH" ),
    MM( "MM", "EMU failure/defect/attention: traction", "EMU TRAC" ),
    MN( "MN", "DMU (inc. HST/MPV) failure/defect/attention: brakes (excluding Railhead Conditioning trains)",
        "DMU BRAKE" ),
    MO( "MO", "Loco/unit/vehicles late off depot (cause not known)", "STOCK LATE" ),
    MP( "MP", "Loco/unit adhesion problems", "ADHESION" ),
    MQ( "MQ", "Electric loco (inc. IC225) failure/defect/attention: other", "ELEC OTHER" ),
    MR( "MR", "Hot Box or HABD/WILD activation (positive)", "HOT BOX" ),
    MS( "MS", "Stock change or replacement by slower vehicles (all vehicle types)", "STOCK CHNG" ),
    MT( "MT", "Safety systems failure (AWS/TPWS/ATP)", "AWS TPWS" ),
    MU( "MU", "Depot operating problem", "DEPOT" ),
    MV( "MV", "EngineerÕs on-track equipment failure outside possession", "ON-TRACK" ),
    MW( "MW", "Weather Ð effect on T&RS equipment", "WEATHER" ),
    MX( "MX", "Diesel loco failure/defect/attention: brakes", "DIESL BRKE" ),
    MY( "MY", "Mishap Ð T&RS cause", "TRS MISHAP" ),
    MZ( "MZ", "Other Fleet Engineer causes/initial attribution", "T+RS OTHER" ),
    NA( "NA", "Ontrain TASS Failure", "TASS" ),
    NB( "NB", "TASS Ð No fault found", "TASS NFF" ),
    NC( "NC", "Fire in fleet depot not caused by vandals (includes caused by vandals in respect of freight depots)",
        "DEP FIRE" ),
    ND( "ND", "On train ETCS/ ERTMS failure", "ETCS" ),
    O2( "O2", "ACI Failures", "ACI FAIL" ),
    OB( "OB", "Delayed by signaller not applying applicable regulating policy", "Regulation" ),
    OC( "OC", "Signaller, including wrong routing and wrong ETCS/ ERTMS instruction", "SIGNALLER" ),
    OD( "OD", "Delayed as a result of Route Control directive", "NR CONTROL" ),
    OE( "OE", "Failure to lay Sandite or operate Railhead Conditioning train as programmed", "RHC PROG" ),
    OG( "OG", "Ice on conductor rail/OLE", "ICE" ),
    OH( "OH", "ARS software problem (excluding scheduling error and technical failures)", "ARS" ),
    OI( "OI", "Formal Inquiry Incident - other operators", "JOINT INQ" ),
    OJ( "OJ", "Fire in station building or on platform, affecting operators not booked to call at that station",
        "STN FIRE" ),
    OK( "OK", "Delay caused by Operating staff oversight, error or absence (excluding signallers and Control)",
        "OPTG STAFF" ),
    OL( "OL", "Signal Box not open during booked hours", "BOX CLOSED" ),
    OM( "OM", "Technical failure associated with a Railhead Conditioning train", "RHC FLR" ),
    ON( "ON", "Delays not properly investigated by Network Rail", "Mis-invest" ),
    OO( "OO", "Late start of a RHC", "RHC" ),
    OP( "OP", "Failure of TRUST/SMART systems", "TRUST FLR" ),
    OQ( "OQ", "Incorrect Simplifier", "SIMP ERR" ),
    OS( "OS",
        "Delays to other trains caused by a Railhead Conditioning train taking unusually long time in section or at a location",
        "RHC LATE" ),
    OU( "OU", "Delays un-investigated", "Un-invest" ),
    OV( "OV", "Fire or evacuation due to fire alarm of Network Rail buildings other than stations not due to vandalism",
        "NR FIRE" ),
    OW( "OW",
        "Connections held where the prime incident causing delay to the incoming train is a FOC owned incident and service is more frequent than hourly",
        "FOC CONN" ),
    OY( "OY", "Mishap - Network Rail Operating cause", "NR MISHAP" ),
    OZ( "OZ", "Other Network Rail Operating causes", "OPTG OTHER" ),
    PA( "PA", "Trackwork TSR within Rules of the Route", "PLANND TSR" ),
    PB( "PB", "Condition of Track TSR within Rules of the Route", "PLANND COT" ),
    PC( "PC", "Condition of Bridge TSR within rules of the route", "PLANND COB" ),
    PD( "PD", "TPS cancellation (Not to be input in TSI/TRUST)", "TPS CANC" ),
    PE( "PE", "Cancelled due to planned engineering work", "ENGNRG WRK" ),
    PF( "PF", "Planned engineering work - diversion/SLW not timetabled (within Rules of the Route)", "DIVRSN/SLW" ),
    PG( "PG", "Planned cancellation by Train Operator", "PLAND CANC" ),
    PH( "PH", "Condition of Earthworks TSR within Rules of the Route", "PLND COE" ),
    PI( "PI", "TSR for Schedule 4 Possession", "SCH 4 TSR" ),
    PJ( "PJ", "Duplicate delay", "DUPLICATE" ),
    PK( "PK", "Bank Holiday Cancellation", "BANK HOL" ),
    PL( "PL", "Exclusion agreed between Network Rail and Train Operator", "AGREED EXC" ),
    PN( "PN", "Minor delays to VSTP service caused by regulation / time lost in running.", "VSTP DELAY" ),
    PS( "PS", "Cancellation of a duplicate or erroneous schedule", "DUPE SCHLD" ),
    PT( "PT", "TRUST Berth Offset Errors", "OFFSET ERR" ),
    PZ( "PZ", "Other contractual exclusion", "OTH EXC" ),
    QA( "QA", "WTT Schedule / LTP process", "WTT SCHED" ),
    QB( "QB", "Planned engineering work - diversion/SLW not timetabled (outside rules of the route)", "DIVRSN/SLW" ),
    QH( "QH", "Adhesion problems due to leaf contamination", "LEAF SLIP" ),
    QI( "QI", "Cautioning due to railhead leaf contamination", "RLHD CONT" ),
    QJ( "QJ", "Special working for leaf-fall track circuit operation", "LEAVES T/C" ),
    QM( "QM", "STP schedule / STP process", "STP SCHED" ),
    QN( "QN", "VSTP schedule / VSTP process (TSI created schedule)", "TSI SCHED" ),
    QP( "QP", "Reactionary Delay to ÒPÓ coded Possession", "PLND LOP" ),
    QQ( "QQ", "Simplifier Error Ops Planning", "OPS S ERR" ),
    QT( "QT",
        "Delay accepted by Network Rail as part of a commercial agreement where no substantive delay reason is identified",
        "TAKEBACK" ),
    QZ( "QZ", "Other Network Rail non-Operating causes", "COMM OTHER" ),
    R1( "R1", "Incorrect train dispatch by station staff", "DISPATCH" ),
    R2( "R2", "Late TRTS given by station staff", "LATE TRTS" ),
    R3( "R3", "Station staff unavailable - missing or uncovered", "STAFF MSN" ),
    R4( "R4", "Station staff split responsibility - unable to cover all duties", "STAFF DUTY" ),
    R5( "R5", "Station staff error - e.g. wrong announcements, misdirection", "STAFF ERR" ),
    R6( "R6", "Overtime at stations normally unstaffed.", "UNSTAFFED" ),
    R7( "R7", "Station delays due to special events e.g. sports fixtures", "SPORTS" ),
    RB( "RB", "Passengers joining/alighting", "PASSENGERS" ),
    RC( "RC", "Assisting a disabled person joining/alighting, pre-booked", "DISAB 1" ),
    RD( "RD", "Attaching/detaching/shunter/watering", "ATT/DETACH" ),
    RE( "RE", "Lift/escalator defect/failure", "LIFT/ESC" ),
    RF( "RF", "Loading/unloading letter mails/parcels", "MAIL/PRCLS" ),
    RH( "RH", "Station evacuated due to fire alarm", "FIRE ALARM" ),
    RI( "RI", "Waiting connections - not authorised by TOC Control", "UNAUTH CON" ),
    RJ( "RJ", "Special Stop Orders - not authorised by TOC Control", "UNAUTH SSO" ),
    RK( "RK", "Waiting connections - authorised by TOC Control", "AUTH CON" ),
    RL( "RL", "Special Stop Orders - authorised by TOC Control", "AUTH SSO" ),
    RM( "RM", "Waiting connections from other transport modes", "XTNL CONN" ),
    RN( "RN", "Passengers ÒforcingÓ connections between trains outside connectional allowances", "PASS CONN" ),
    RO( "RO", "Passengers taken ill on platform", "PASS ILL" ),
    RP( "RP", "Passengers dropping items on track (not vandalism)", "PASS DROP" ),
    RQ( "RQ", "Assisting a disabled person joining/alighting, unbooked", "DISAB 2" ),
    RR( "RR", "Loading reserved bicycles presented late", "BIKE 1" ),
    RS( "RS", "Loading unreserved bicycles", "BIKE 2" ),
    RT( "RT", "Loading excessive luggage", "LUGGAGE 1" ),
    RU( "RU", "Locating lost luggage", "LUGGAGE 2" ),
    RV( "RV", "Customer Information System failure", "PASS INFO" ),
    RW( "RW",
        "Station flooding (including issues with drains) not the result of weather, where the water has not emanated from Network Rail maintained infrastructure/network",
        "STN FLOOD" ),
    RY( "RY", "Mishap - Station Operator cause", "STN MISHAP" ),
    RZ( "RZ", "Other Station Operator causes", "STN OTHER" ),
    T1( "T1", "Delay at unstaffed station to DOO train", "DOO STN" ),
    T2( "T2", "Delay at unstaffed station to non-DOO train", "NONDOO STN" ),
    T3( "T3", "Waiting connections from other transport modes", "XTNL CONN" ),
    T4( "T4", "Loading Supplies (including catering)", "SUPPLIES" ),
    TA( "TA", "Traincrew/loco/stock/unit diagram error (See also DAG Section 4.31.2)", "DIAG ERROR" ),
    TB( "TB", "Train cancelled/delayed at Train OperatorÕs request", "TOC REQST" ),
    TC( "TC", "Booked Traincrew used for additional/other service", "CREW USED" ),
    TD( "TD", "Booked loco/stock/unit used for additional/other service", "STOCK USED" ),
    TE( "TE", "Injury to passenger on train", "PASS INJRY" ),
    TF( "TF", "Seat reservation problems", "SEAT RESVN" ),
    TG( "TG", "Driver", "DRIVER" ),
    TH( "TH", "(Senior) Conductor/Train Manager", "(SNR) COND" ),
    TI( "TI", "Traincrew rostering problem", "ROSTERING" ),
    TJ( "TJ", "Tail lamp/headlamp out", "TAIL LAMP" ),
    TK( "TK", "Train catering staff (including Contractors)", "CATERING" ),
    TL( "TL", "Door open / not properly secured incident", "DOOR OPEN" ),
    TM( "TM", "Connection authorised by TOC but outwith connection policy", "AUTH CONN" ),
    TN( "TN", "Late presentation from the continent", "LATE CHUNL" ),
    TO( "TO", "Delay believed to be due to Operator, but no information available from Operator", "TOC UNEX" ),
    TP( "TP", "Special Stop Orders", "AUTH SSO" ),
    TR( "TR", "Train Operating Company Directive", "TOC DIRECT" ),
    TS( "TS", "Delay due to ETCS/ ERTMS on-board overriding driver command", "ETCS OVRD" ),
    TT( "TT", "Autumn-attribution Neutral Zone delays (See Supplementary Autumn Attribution Guidance)", "LF NEUTRAL" ),
    TU( "TU", "Formal Inquiry Incident - possible Operator responsibility", "JOINT INQ" ),
    TW( "TW", "Driver adhering to company professional driving standards or policy", "PRO DVR" ),
    TX( "TX", "Delays incurred on non-Network Rail running lines including London Underground causes (except T&RS)",
        "LUL CAUSES" ),
    TY( "TY", "Mishap-Train Operating Company cause", "TOC MISHAP" ),
    TZ( "TZ", "Other Passenger Train Operating Company causes", "TOC OTHER" ),
    V8( "V8", "Train striking other birds", "OTH BIRDS" ),
    VA( "VA", "Disorder/drunks/trespass etc", "DISORDER" ),
    VB( "VB", "Vandalism/theft", "VANDALS" ),
    VC( "VC",
        "Fatalities/injuries sustained whilst on a platform as the result of being struck by a train or falling from a train",
        "FATALITIES" ),
    VD( "VD", "Passenger taken ill on train", "ILL PASS" ),
    VE( "VE", "Ticket irregularities/refusals to pay", "TICKET IRR" ),
    VF( "VF", "Fire caused by vandalism", "VDL FIRE" ),
    VG( "VG", "Police searching train", "POLICE-TRN" ),
    VH( "VH", "Communication cord/emergency train alarm operated", "COM CORD" ),
    VI( "VI", "Security alert affecting stations and depots", "SEC ALERT" ),
    VR( "VR",
        "Driver adhering to company professional driving standards or policies during severe weather conditions that are not fleet related",
        "PRO DVR" ),
    VW( "VW",
        "Severe weather affecting passenger fleet equipment including following company standards/policies or Rule Book instructions",
        "WEATHER" ),
    VX( "VX", "Passenger charter excludable events occurring on the LUL or other non NR running lines", "LUL CAUSES" ),
    VZ( "VZ", "Other passenger/external causes the responsibility of TOC", "EXT OTHER" ),
    X1( "X1",
        "Visibility in semaphore signalled areas, or special working for fog and falling snow implemented by Network Rail Ð in all signalling areas",
        "SPL REGS" ),
    X2( "X2", "Severe flooding beyond that which could be mitigated on Network Rail infrastructure", "SEV FLOOD" ),
    X3( "X3", "Lightning Strike Ð damage to protected systems.", "LGHTNG" ),
    X4( "X4", "Blanket speed restriction for extreme heat or high wind in accordance with the Group Standards",
        "BLNK REST" ),
    X8( "X8", "Animal Strike/Incursion not within the control of Network Rail", "EXT ANIMAL" ),
    X9( "X9", "Points failure caused by severe snow where heaters are working as designed", "SEV SNOW" ),
    XA( "XA", "Trespass", "TRESPASS" ),
    XB( "XB", "Vandalism/theft (including the placing of objects on the line)", "VANDALS" ),
    XC( "XC", "Fatalities/ injuries caused by being hit by train", "FATALITIES" ),
    XD( "XD", "Level Crossing Incidents", "XING INCDT" ),
    XF( "XF", "Police searching line", "POLICE-RLY" ),
    XH( "XH", "Severe heat affecting infrastructure the responsibility of Network Rail (excluding Heat speeds)",
        "SEV HEAT" ),
    XI( "XI", "Security alert affecting Network Rail Network", "SEC ALERT" ),
    XK( "XK", "External Power Supply Failure Network Rail Infrastructure", "EXTL POWER" ),
    XL( "XL", "Fire external to railway infrastructure", "EXTL FIRES" ),
    XM( "XM", "Gas/water mains/overhead power lines", "GAS/WATER" ),
    XN( "XN", "Road related - excl bridge strikes/level crossing incident", "ROAD INCDT" ),
    XO( "XO",
        "External trees, building or objects encroaching onto Network Rail infrastructure (not due to weather or vandalism)",
        "EXT OBJECT" ),
    XP( "XP", "Bridge Strike", "BDG STRIKE" ),
    XQ( "XQ", "Swing bridge open for river or canal traffic", "BDGE OPEN" ),
    XR( "XR", "Cable vandalism/theft", "CABLE CUT" ),
    XS( "XS", "Level Crossing misuse", "XNG MISUSE" ),
    XT( "XT", "Severe cold weather affecting infrastructure the responsibility of Network Rail", "SEV COLD" ),
    XU( "XU", "Sunlight on signal", "SUN OBSCUR" ),
    XV( "XV", "Fire or evacuation due to fire alarm of Network Rail buildings other than stations due to vandalism",
        "NR FIRE" ),
    XW( "XW",
        "High winds affecting infrastructure the responsibility of Network Rail including objects on the line due to the effect of weather",
        "WIND" ),
    XZ( "XZ", "Other external causes the responsibility of Network Rail", "EXT OTHER" ),
    YA( "YA", "Lost path - regulated for train running on time", "REG-ONTIME" ),
    YB( "YB", "Lost path - regulated for another late running train", "REG-LATE" ),
    YC( "YC", "Lost path - following train running on time", "FOL-ONTIME" ),
    YD( "YD", "Lost path - following another late running train", "FOL-LATE" ),
    YE( "YE", "Lost path - waiting acceptance to single line", "TO S/LINE" ),
    YF( "YF", "Waiting for late running train off single line", "OFF SLINE" ),
    YG( "YG", "Regulated in accordance with Regulation Policy", "CORRCT REG" ),
    YH( "YH", "Late arrival of inward loco", "INWD LOCO" ),
    YI( "YI", "Late arrival of inward stock/unit", "INWD STOCK" ),
    YJ( "YJ", "Late arrival of Traincrew on inward working", "INWD CREW" ),
    YK( "YK", "Waiting connecting Freight or Res traffic to attach", "CNNCTN TFC" ),
    YL( "YL", "Waiting passenger connections within Connection Policy", "AUTHSD CON" ),
    YM( "YM", "Special stop orders agreed by Control", "AUTHSD SSO" ),
    YN( "YN", "Booked traincrew not available for late running train", "FIND CREW" ),
    YO( "YO", "Waiting platform/station congestion/platform change", "PLATFORM" ),
    YP( "YP", "Delayed by diverted train", "DIVERSION" ),
    YQ( "YQ", "Passenger overcrowding caused by a train being of short- formation", "SHRT FRMD" ),
    YU( "YU", "Prime cause of most unit swaps", "UNIT SWAPS" ),
    YX( "YX", "Passenger overcrowding caused by delay/cancellation of another train", "OVER CRWD" ),
    ZW( "ZW", "Unattributed Cancellations", "UNATR CAN" ),
    ZX( "ZX", "Unexplained late start", "UNEX L/S" ),
    ZY( "ZY", "Unexplained Station overtime", "UNEX O/T" ),
    ZZ( "ZZ", "Unexplained loss in running", "UNEX L/R" );

    private static final Map<String, DelayAttributionCode> LOOKUP = new ConcurrentHashMap<>();

    static
    {
        for( DelayAttributionCode dac : values() )
        {
            LOOKUP.put( dac.getCode(), dac );
            LOOKUP.put( dac.getAbbreviation(), dac );
        }
    }

    /**
     * Lookup the DelayAttributionCode based on either the code or abbreviation.
     * <p>
     * @param code code or abbreviation
     * <p>
     * @return DelayAttributionCode or null if not found
     */
    public static DelayAttributionCode lookup( String code )
    {
        return code == null ? null : LOOKUP.get( code );
    }

    /**
     * Return the DelayAttributionCode for a {@link JsonValue}
     * <p>
     * @param v JsonValue
     * <p>
     * @return DelayAttributionCode or null if v is null or not a string
     */
    public static DelayAttributionCode lookup( JsonValue v )
    {
        if( v != null && v.getValueType() == JsonValue.ValueType.STRING )
        {
            return lookup( ((JsonString) v).getString() );
        }
        else
        {
            return null;
        }
    }

    /**
     * Convenience method to return a DelayAttributionCode from a {@link JsonObject}
     * @param o object
     * @param n name in object
     * @return DelayAttributionCode or null
     */
    public static DelayAttributionCode get( JsonObject o, String n )
    {
        Objects.requireNonNull( o );
        Objects.requireNonNull( n );
        return lookup( o.get( n ) );
    }

    /**
     * Add a DelayAttributionCode to a {@link JsonObjectBuilder}.
     * <p>
     * If dac is null then this adds a null entry to the builder, otherwise a value suitable for
     * {@link #lookup(javax.json.JsonValue)} is set.
     * <p>
     * @param b   builder
     * @param n   name of field
     * @param dac DelayAttributionCode or null
     */
    public static void add( JsonObjectBuilder b, String n, DelayAttributionCode dac )
    {
        if( dac == null )
        {
            b.addNull( n );
        }
        else
        {
            b.add( n, dac.getCode() );
        }
    }

    private final String code;
    private final String cause;
    private final String abbreviation;

    private DelayAttributionCode( String code, String cause, String abbreviation )
    {
        this.code = code;
        this.cause = cause;
        this.abbreviation = abbreviation;
    }

    /**
     * The DelayAttributionCode code
     * <p>
     * @return DelayAttributionCode code
     */
    public String getCode()
    {
        return code;
    }

    /**
     * Human readable cause
     * <p>
     * @return human readable cause
     */
    public String getCause()
    {
        return cause;
    }

    /**
     * The abbreviation for this DelayAttributionCode
     * <p>
     * @return DelayAttributionCode abbreviation
     */
    public String getAbbreviation()
    {
        return abbreviation;
    }

}
