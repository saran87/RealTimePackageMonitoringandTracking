/*****************************************************************************************
 * Copyright (c) 2000-2003 The Regents of the University of California.  
 * All rights reserved.
 * Copyright (c) 2005 Arch Rock Corporation
 * All rights reserved.
 * Copyright (c) 2006, Technische Universitaet Berlin
 * All rights reserved.
 * Copyright (c) 2010, ADVANTIC Sistemas y Servicios S.L.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are 
 * permitted provided that the following conditions are met:
 *
 *    * Redistributions of source code must retain the above copyright notice, this list  
 * of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright notice, this  
 * list of conditions and the following disclaimer in the documentation and/or other 
 * materials provided with the distribution.
 *    * Neither the name of ADVANTIC Sistemas y Servicios S.L. nor the names of its 
 * contributors may be used to endorse or promote products derived from this software 
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL 
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * - Revision -------------------------------------------------------------
 * $Revision: 1.0 $
 * $Date: 2010/12/12 18:24:06 $
 * @author: Manuel Fernandez <manuel.fernandez@advanticsys.com>
*****************************************************************************************/

generic configuration MTS_EM1000C() 
{
   provides 
   {
			interface DeviceMetadata;
				
			interface Read<uint16_t> as S1087_PAR_VL_Calibration_off;
			interface Read<uint16_t> as S1087_PAR_VL_Calibration_on;
			interface Read<uint16_t> as S1087_01_TSR_IR;
			interface Read<uint16_t> as ADXL321_ACC_Axis_X;
			interface Read<uint16_t> as ADXL321_ACC_Axis_Y;
			interface Read<uint16_t> as Sensirion_Temperature;
			interface Read<uint16_t> as Sensirion_Humidity;

		
			interface ReadStream<uint16_t> as S1087_PAR_VL_Calibration_off_Stream;
			interface ReadStream<uint16_t> as S1087_PAR_VL_Calibration_on_Stream;
			interface ReadStream<uint16_t> as S1087_01_TSR_IR_Stream;
			interface ReadStream<uint16_t> as ADXL321_ACC_Axis_X_Stream;
			interface ReadStream<uint16_t> as ADXL321_ACC_Axis_Y_Stream;

   }
}
implementation {
	components       MTS_EM1000P;
    DeviceMetadata = MTS_EM1000P;
        //--Read--
	components new AdcReadClientC() as AdcRead_S1087_PAR_VL_Calibration_off;
	components new AdcReadClientC() as AdcRead_S1087_PAR_VL_Calibration_on;
	components new AdcReadClientC() as AdcRead_S1087_01_TSR_IR;
	components new AdcReadClientC() as AdcRead_ADXL321_ACC_Axis_X;
	components new AdcReadClientC() as AdcRead_ADXL321_ACC_Axis_Y;
	components new SensirionSht11C() as Sensirion;

  
	S1087_PAR_VL_Calibration_off = AdcRead_S1087_PAR_VL_Calibration_off;
	S1087_PAR_VL_Calibration_on  = AdcRead_S1087_PAR_VL_Calibration_on;
	S1087_01_TSR_IR    = AdcRead_S1087_01_TSR_IR;
	ADXL321_ACC_Axis_X = AdcRead_ADXL321_ACC_Axis_X;
	ADXL321_ACC_Axis_Y = AdcRead_ADXL321_ACC_Axis_Y;

    Sensirion_Temperature = Sensirion.Temperature;
    Sensirion_Humidity    = Sensirion.Humidity;

	AdcRead_S1087_PAR_VL_Calibration_off.AdcConfigure -> MTS_EM1000P.S1087_PAR_Calibration_off;
	AdcRead_S1087_PAR_VL_Calibration_on .AdcConfigure -> MTS_EM1000P.S1087_PAR_Calibration_on;
	AdcRead_S1087_01_TSR_IR   .AdcConfigure -> MTS_EM1000P.S1087_01_TSR_IR;
	AdcRead_ADXL321_ACC_Axis_X.AdcConfigure -> MTS_EM1000P.ADXL321_ACC_Axis_X;
	AdcRead_ADXL321_ACC_Axis_Y.AdcConfigure -> MTS_EM1000P.ADXL321_ACC_Axis_Y;

	
	//--Read Stream--
	components new AdcReadStreamClientC() as AdcReadStream_S1087_PAR_VL_Calibration_off_Stream;
	components new AdcReadStreamClientC() as AdcReadStream_S1087_PAR_VL_Calibration_on_Stream;
	components new AdcReadStreamClientC() as AdcReadStream_S1087_01_TSR_IR_Stream;
	components new AdcReadStreamClientC() as AdcReadStream_ADXL321_ACC_Axis_X_Stream;
	components new AdcReadStreamClientC() as AdcReadStream_ADXL321_ACC_Axis_Y_Stream;
  
	S1087_PAR_VL_Calibration_off_Stream = AdcReadStream_S1087_PAR_VL_Calibration_off_Stream;
	S1087_PAR_VL_Calibration_on_Stream  = AdcReadStream_S1087_PAR_VL_Calibration_on_Stream;
	S1087_01_TSR_IR_Stream    = AdcReadStream_S1087_01_TSR_IR_Stream;
	ADXL321_ACC_Axis_X_Stream = AdcReadStream_ADXL321_ACC_Axis_X_Stream;
	ADXL321_ACC_Axis_Y_Stream = AdcReadStream_ADXL321_ACC_Axis_Y_Stream;
  
        AdcReadStream_S1087_PAR_VL_Calibration_off_Stream.AdcConfigure -> MTS_EM1000P.S1087_PAR_Calibration_off;
	AdcReadStream_S1087_PAR_VL_Calibration_on_Stream .AdcConfigure -> MTS_EM1000P.S1087_PAR_Calibration_on;
	AdcReadStream_S1087_01_TSR_IR_Stream  .AdcConfigure  -> MTS_EM1000P.S1087_01_TSR_IR;
	AdcReadStream_ADXL321_ACC_Axis_X_Stream.AdcConfigure -> MTS_EM1000P.ADXL321_ACC_Axis_X;
	AdcReadStream_ADXL321_ACC_Axis_Y_Stream.AdcConfigure -> MTS_EM1000P.ADXL321_ACC_Axis_Y;

	
}
