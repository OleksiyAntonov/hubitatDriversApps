/*
    Status:
        [ ] In Progress
	[T] In Test Process
        [X] Done
*/

// [ ]
metadata {
  definition (
    name: "Aeotec Door/Window Sensor 7 Pro EU",
	  // ?????
    namespace: "hubitat.antonov",
	  // ?????
    author: "Antonov, Oleksiy",
    importUrl: "https://raw.githubusercontent.com/OleksiyAntonov/hubitatDriversApps/main/drivers/aeotec/zwa-012/zwa-012.groovy"
  ) {
    capability "Contact Sensor"
		capability "Sensor"
		// capability "Battery"
		// capability "Configuration"
		// capability "Health Check"

		// fingerprint deviceId: "0x0701", inClusters: "0x5E,0x86,0x72,0x98", outClusters: "0x5A,0x82"

    // [ ]
		fingerprint mfr: "0371", prod: "0102", model: "0007", deviceJoinName: "Aeotec Door/Window Sensor 7 Pro EU"
	}
  
  // [X]
  preferences {
      // none in this driver
   }
}

// [X]
def installed() {
   log.debug "installed()"
}

// [X]
def updated() {
    log.debug "updated()"
}

// [X]
def parse(String description) {
    log.debug "parse: $description"
    
    def cmd = zwave.parse(description)
    zwaveEvent(cmd)	
}

/* ZWave Handlers */

// [ ]
/* Basic Report */
def zwaveEvent(hubitat.zwave.commands.basicv1.BasicReport cmd) {
   if (enableDebug)
	log.debug "BasicReport:  ${cmd}"
}

/*
********** 0x30 Sensor Binary **********
 Supported Get Sensor : 0x01
----------------------------------------
     List<Short> getPayload()
     String format()
*/
def zwaveEvent(hubitat.zwave.commands.sensorbinaryv2.SensorBinarySupportedGetSensor cmd) {
   if (enableDebug)
	log.debug "SensorBinarySupportedGetSensor:  ${cmd}"
}

/*
********** 0x30 Sensor Binary **********
 Supported Get Sensor : 0x02
----------------------------------------
     Short sensorType
     
     List<Short> getPayload()
     String format()
*/
def zwaveEvent(hubitat.zwave.commands.sensorbinaryv2.SensorBinaryGet cmd) {
   if (enableDebug)
	log.debug "SensorBinaryGet:  ${cmd}"
}

/*
********** 0x30 Sensor Binary **********
 Sensor Binary Report : 0x03
----------------------------------------
     Short sensorType
     Short sensorValue
     static Short SENSOR_TYPE_AUX = 9
     static Short SENSOR_TYPE_CO = 3
     static Short SENSOR_TYPE_CO2 = 4
     static Short SENSOR_TYPE_DOOR_WINDOW = 10
     static Short SENSOR_TYPE_FIRST = 255
     static Short SENSOR_TYPE_FREEZE = 7
     static Short SENSOR_TYPE_GENERAL_PURPOSE = 1
     static Short SENSOR_TYPE_GLASS_BREAK = 13
     static Short SENSOR_TYPE_HEAT = 5
     static Short SENSOR_TYPE_MOTION = 12
     static Short SENSOR_TYPE_SMOKE = 2
     static Short SENSOR_TYPE_TAMPER = 8
     static Short SENSOR_TYPE_TILT = 11
     static Short SENSOR_TYPE_WATER = 6
     static Short SENSOR_VALUE_DETECTED_AN_EVENT = 255
     static Short SENSOR_VALUE_IDLE = 0     
     
     List<Short> getPayload()
     String format()
*/
def zwaveEvent(hubitat.zwave.commands.sensorbinaryv2.SensorBinaryReport cmd) {
   if (enableDebug)
	log.debug "SensorBinaryReport:  ${cmd}"
}

def zwaveEvent(hubitat.zwave.Command cmd){
   log.debug "not handling: ${cmd}"
}

// [NT]
private getCommandClassVersions() {
/*
    0x20 Basic
    0x30 Sensor Binary
    0x70 Configuration
    0x71 Alarm
    0x72 Manufacturer Specific
    0x73 Powerlevel
    0x80 Battery
    0x84 Wake Up
    0x85 Association
    0x86 Version
*/

/*
0x20 Basic V2
0x70 Configuration V4
0x9F Security S2
0x6C Supervision
0x25 Switch Binary V2
0x86 Version V3 
*/
	
	[0x20: 1,
	 0x30: 1,
	 0x70: 1,
	 0x71: 1,
	 0x72: 1,
	 0x73: 1,
	 0x80: 1,
	 0x84: 1,
	 0x85: 1,
	 0x86: 1
	]
}

/*
def parse(String description) {
	def result = null
	if (description.startsWith("Err 106")) {
		if ((zwaveInfo.zw == null && state.sec != 0) || zwaveInfo?.zw?.endsWith("s")) {
			log.debug description
		} else {
			result = createEvent(
				descriptionText: "This sensor failed to complete the network security key exchange. If you are unable to control it via SmartThings, you must remove it from your network and add it again.",
				eventType: "ALERT",
				name: "secureInclusion",
				value: "failed",
				isStateChange: true,
			)
		}
	} else if (description != "updated") {
		def cmd = zwave.parse(description, commandClassVersions)
		if (cmd) {
			result = zwaveEvent(cmd)
		}
	}
	log.debug "parsed '$description' to $result"
	return result
 */
}
