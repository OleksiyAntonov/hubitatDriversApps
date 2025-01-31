definition(
    name: 'oaa@illumination string value converter',
    namespace: 'oaa.hubitat.zwave',
    author: 'Oleksiy Antonov',
    description: 'Summarizes current values of selected energy meter devices',
    category: 'Convenience',
    iconUrl: '',
    iconX2Url: ''
)

preferences {
    section('Select the illuminance sensors:') {
        input 'illuminationSensors', 'capability.illuminanceMeasurement', title: 'Which illumination sensors?', required: true, multiple: true
    }
}

def installed() {
    subscribeToEvents()
}

def updated() {
    unsubscribe()
    subscribeToEvents()
}

def subscribeToEvents() {
    illuminationSensors.each {
        subscribe(it, 'illuminance', illuminationHandler)
        log.debug "Subscribed ${it.name}"
    }
}

def illuminationHandler(evt) {
    def recalcultedIllumination = 0
    def stringIllumination = ''
    def globalVarName = "${evt.displayName}_illuminance"
    stringIllumination = evt.value ?: 0
    recalcultedIllumination = stringIllumination.toInteger()
    log.debug "${globalVarName} oi:${stringIllumination} ri:${recalcultedIllumination}"
    setGlobalVar(globalVarName, recalcultedIllumination)

// log.debug "Power Meter '${evt.displayName}' reports ${evt.value}kWh energy usage."
// log.debug "Total energy usage: ${totalEnergy}kWh"
// setGlobalVar("globalEnergyConsumption", totalEnergy)
}
