/* -----------------------------------------------------------------------------
# Name: illumination-string-value-converter.groovy
# Description: Recalculate illumination sensors values into numeric format
# for smarthome "hubitat" (https://hubitat.com/)
#
# Copyright (C) 2023 Oleksiy Antonov. All rights reserved.
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all
# copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.
# ----------------------------------------------------------------------------- */

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
