// [ ]
metadata {
  definition (
    name: "Aeotec Door/Window Sensor 7 Pro EU",
    namespace: "hubitat.antonov",
    author: "Antonov, Oleksiy",
    importUrl: "https://raw.githubusercontent.com/OleksiyAntonov/hubitatDriversApps/main/drivers/aeotec/zwa-012/zwa-012.groovy"
  ) {
    capability "Contact Sensor"
		capability "Sensor"
		capability "Battery"
		capability "Configuration"
		capability "Health Check"

		fingerprint deviceId: "0x2001", inClusters: "0x30,0x80,0x84,0x85,0x86,0x72"
		fingerprint deviceId: "0x07", inClusters: "0x30"
		fingerprint deviceId: "0x0701", inClusters: "0x5E,0x98"
		fingerprint deviceId: "0x0701", inClusters: "0x5E,0x86,0x72,0x98", outClusters: "0x5A,0x82"
		fingerprint deviceId: "0x0701", inClusters: "0x5E,0x80,0x71,0x85,0x70,0x72,0x86,0x30,0x31,0x84,0x59,0x73,0x5A,0x8F,0x98,0x7A", outClusters: "0x20"

    // [ ]
		fingerprint mfr: "0371", prod: "0102", model: "0007", deviceJoinName: "Aeotec Door/Window Sensor 7 Pro EU"
	}
  
  // [X]
  preferences {
      // none in this driver
   }
}

// [ ]
def installed() {
   log.debug "installed()"
}

// [ ]
def updated() {
   log.debug "updated()"
}

// [ ]
private getCommandClassVersions() {
	[0x20: 1, 0x25: 1, 0x30: 1, 0x31: 5, 0x80: 1, 0x84: 1, 0x71: 3, 0x9C: 1]
}

// [ ]
def parse(String description) {
  /*
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

// [X]
def configure() {
	// stub
}

// [ ]
def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicReport cmd) {
	sensorValueEvent(cmd.value)
}

// [ ]
def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicSet cmd) {
	sensorValueEvent(cmd.value)
}
