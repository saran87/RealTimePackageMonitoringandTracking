/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rtpmt.motes.packet;

import rtpmt.sensor.util.Constants;



/**
 *
 * @author Saravana Kumar
 * @version 1.0
 */
class SensorPacket {

        byte[] escaped;
        int escapePtr;
        int crc;

        // We're building a length-byte packet
        SensorPacket(int length) {
            escaped = new byte[2 * length];
            escapePtr = 0;
            crc = 0;
            while (escapePtr < Constants.FRAME_SYNC.length) {
                escaped[escapePtr] = (byte) Constants.FRAME_SYNC[escapePtr];
                escapePtr++;
            }
        }

        void nextByte(int b) {
            b = b & 0xff;
            crc = Crc.calcByte(crc, b);

            escaped[escapePtr++] = (byte) b;
        }

        void terminate() {
            crc = crc & 0xff;
            escaped[escapePtr++] = (byte) (crc >> 8);
            escaped[escapePtr++] = (byte) crc;
        }
}
