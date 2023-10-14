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
    name: "oaa@power/energy meter summarizer",
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
}

def installed() {
    initialize()
    subscribeToEvents()
}

def updated() {
    unsubscribe()
    subscribeToEvents()
}

def initialize() {
    calculateSummary()
}

def subscribeToEvents() {
    powerMeters.each {
        subscribe(it, "power", powerHandler)
        subscribe(it, "energy", powerHandler)
    }
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

        totalEnergy += it.currentEnergy ?: 0
        totalPower += it.currentPower ?: 0
    }

    powerEnergyMeterSummary.setEnergy(totalEnergy)
    powerEnergyMeterSummary.setPower(totalPower)

    powerEnergyMeterSummaryDatazentrum.setEnergy(totalEnergyDatazentrum)
    powerEnergyMeterSummaryDatazentrum.setPower(totalPowerDatazentrum)

    powerEnergyMeterSummaryKueche.setEnergy(totalEnergyKueche)
    powerEnergyMeterSummaryKueche.setPower(totalPowerKueche)

    powerEnergyMeterSummaryWohnzimmer.setEnergy(totalEnergyWohnzimmer)
    powerEnergyMeterSummaryWohnzimmer.setPower(totalPowerWohnzimmer)
}

def powerHandler(evt) {
    calculateSummary()
}
