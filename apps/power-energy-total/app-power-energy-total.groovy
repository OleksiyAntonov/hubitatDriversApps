/* -----------------------------------------------------------------------------
# Name: app-power-energy-total.groovy
# Description: Calculates total power and energy for selected devices
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
    name: "oaa@power/energy total",
    namespace: "oaa.hubitat.zwave",
    author: "Oleksiy Antonov",
    description: "Calculates total power and energy for selected devices",
    category: "Convenience",
    iconUrl: "",
    iconX2Url: ""
)

preferences {
    section("Select the power/energy meters:") {
        input "powerMeters", "capability.powerMeter, capability.energyMeter", title: "Power meters", required: true, multiple: true
    }
    section("Select the power/energy meters /Datazentrum/:") {
        input "powerMetersDatazentrum", "capability.powerMeter, capability.energyMeter", title: "Power meters in the Datazentrum", required: true, multiple: true
    }
    section("Select the power/energy meters /Kueche/:") {
        input "powerMetersKueche", "capability.powerMeter, capability.energyMeter", title: "Power meters in the Kueche", required: true, multiple: true
    }
    section("Select the power/energy meters /Wohnzimmer/:") {
        input "powerMetersWohnzimmer", "capability.powerMeter, capability.energyMeter", title: "Power meters in the Wohnzimmer", required: true, multiple: true
    }
    section("Select the summary meter device:") {
        input "powerEnergyMeterSummary", "capability.powerMeter, capability.energyMeter", title: "Summary power/energy meter device", required: true, multiple: false
    }
    section("Select the summary meter device /Kueche/:") {
        input "powerEnergyMeterSummaryKueche", "capability.powerMeter, capability.energyMeter", title: "Summary power/energy meter device /Kueche/", required: true, multiple: false
    }
    section("Select the summary meter device /Datazentrum/:") {
        input "powerEnergyMeterSummaryDatazentrum", "capability.powerMeter, capability.energyMeter", title: "Summary power/energy meter device /Datazentrum/", required: true, multiple: false
    }
    section("Select the summary meter device /Wohnzimmer/:") {
        input "powerEnergyMeterSummaryWohnzimmer", "capability.powerMeter, capability.energyMeter", title: "Summary power/energy meter device /Wohnzimmer/", required: true, multiple: false
    }

    section("Set the period of data refresh in sec:") {
        input name: "calculationTimeout",
            type: "enum",
            title: "Sec",
            options: ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10"],
            required: true
    }
}

def installed() {
    initialize()
}

def updated() {
    unschedule()
    initialize()
}

def initialize() {
    def timeoutSeconds = calculationTimeout.toInteger()
    schedule("0/${timeoutSeconds} * * * * ?", calculateSummary)
}

def calculateSummary() {
    // Warning: Same device can presents only in one list

    // Kueche
    def _powerMetersKueche = [:]
    powerMetersKueche.each {
        _powerMetersKueche[it.displayName] = it
    }

    def totalPowerKueche = 0
    def totalEnergyKueche = 0

    // Datazentrum
    def _powerMetersDatazentrum = [:]
    powerMetersDatazentrum.each {
        _powerMetersDatazentrum[it.displayName] = it
    }

    def totalPowerDatazentrum = 0
    def totalEnergyDatazentrum = 0

    // Wohnzimmer
    def _powerMetersWohnzimmer = [:]
    powerMetersWohnzimmer.each {
        _powerMetersWohnzimmer[it.displayName] = it
    }

    def totalPowerWohnzimmer = 0
    def totalEnergyWohnzimmer = 0

    // Total
    def totalPower = 0
    def totalEnergy = 0

    def currentPartialDevice = null

    powerMeters.each {
        if (_powerMetersDatazentrum.containsKey(it.displayName)) {
    
            // log.debug "0Power Meter Name: ${it.displayName} value: ${it.currentPower}W"
            
            currentPartialDevice = _powerMetersDatazentrum[it.displayName]
            totalEnergyDatazentrum += currentPartialDevice.currentEnergy ?: 0
            totalPowerDatazentrum += currentPartialDevice.currentPower ?: 0
            currentPartialDevice = null
        }
        else
        {
            if (_powerMetersKueche.containsKey(it.displayName)) {

                currentPartialDevice = _powerMetersKueche[it.displayName]
                totalEnergyKueche += currentPartialDevice.currentEnergy ?: 0
                totalPowerKueche += currentPartialDevice.currentPower ?: 0
                currentPartialDevice = null
            }
            else
            {
                if (_powerMetersWohnzimmer.containsKey(it.displayName)) {

                    currentPartialDevice = _powerMetersWohnzimmer[it.displayName]
                    totalEnergyWohnzimmer += currentPartialDevice.currentEnergy ?: 0
                    totalPowerWohnzimmer += currentPartialDevice.currentPower ?: 0
                    currentPartialDevice = null
                }
            }
        }

        totalPower += it.currentPower ?: 0
        totalEnergy += it.currentEnergy ?: 0
    }

   if (powerEnergyMeterSummary.currentPower != totalPower) {
        powerEnergyMeterSummary.setPower(totalPower)

        powerEnergyMeterSummaryDatazentrum.setPower(totalPowerDatazentrum)
        powerEnergyMeterSummaryKueche.setPower(totalPowerKueche)
        powerEnergyMeterSummaryWohnzimmer.setPower(totalPowerWohnzimmer)

        // log.debug "Power Meter Current: ${powerEnergyMeterSummary.currentPower}W"
        // log.debug "Power Meter: ${totalPower}W"
    }

   if (powerEnergyMeterSummary.currentEnergy != totalEnergy) {
        powerEnergyMeterSummary.setEnergy(totalEnergy)

        powerEnergyMeterSummaryDatazentrum.setEnergy(totalEnergyDatazentrum)
        powerEnergyMeterSummaryKueche.setEnergy(totalEnergyKueche)
        powerEnergyMeterSummaryWohnzimmer.setEnergy(totalEnergyWohnzimmer)

        // log.debug "Energy Meter Current: ${powerEnergyMeterSummary.currentEnergy}kWh"
        // log.debug "Energy Meter: ${totalEnergy}kWh"
   }
}