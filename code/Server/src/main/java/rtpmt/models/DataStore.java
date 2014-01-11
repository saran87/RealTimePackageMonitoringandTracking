/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rtpmt.models;

import rtpmt.database.access.IDataStore;
import rtpmt.network.packet.NetworkMessage;
import rtpmt.network.packet.NetworkMessage.PackageInformation;

/**
 *
 * @author Kumar
 */
public final class DataStore {

    private static IDataStore getSensorDataStore(PackageInformation packageInformation) {
        for (NetworkMessage.PackageInformation.Sensor sensor : packageInformation.getSensorsList()) {
            switch (sensor.getSensorType()) {
                case TEMPERATURE:
                    return new Temperature(packageInformation);
                case HUMIDITY:
                    return new Humidity(packageInformation);
                case VIBRATIONX:
                case VIBRATIONY:
                case VIBRATIONZ:
                    return new Vibration(packageInformation);
                case SHOCKX:
                case SHOCKY:
                case SHOCKZ:
                    return new Shock(packageInformation);
            }
        }
        return null;
    }

    private DataStore() {

    }

    public static IDataStore getDataObject(PackageInformation packageInformation) {

        IDataStore dataStore;

        if (packageInformation.getMessageType() == PackageInformation.MessageType.CONFIG) {
            dataStore = new Config(packageInformation);
        } else {

            dataStore = getSensorDataStore(packageInformation);
        }

        return dataStore;
    }

}
