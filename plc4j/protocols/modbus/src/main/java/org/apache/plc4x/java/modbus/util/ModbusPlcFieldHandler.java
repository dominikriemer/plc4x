/*
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at
 
     http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */
package org.apache.plc4x.java.modbus.util;

import org.apache.plc4x.java.api.exceptions.PlcInvalidFieldException;
import org.apache.plc4x.java.api.model.PlcField;
import org.apache.plc4x.java.api.value.PlcBoolean;
import org.apache.plc4x.java.api.value.PlcList;
import org.apache.plc4x.java.api.value.PlcValue;
import org.apache.plc4x.java.modbus.model.CoilModbusField;
import org.apache.plc4x.java.modbus.model.MaskWriteRegisterModbusField;
import org.apache.plc4x.java.modbus.model.ModbusField;
import org.apache.plc4x.java.modbus.model.ReadDiscreteInputsModbusField;
import org.apache.plc4x.java.modbus.model.ReadHoldingRegistersModbusField;
import org.apache.plc4x.java.modbus.model.ReadInputRegistersModbusField;
import org.apache.plc4x.java.modbus.model.RegisterModbusField;
import org.apache.plc4x.java.spi.connection.DefaultPlcFieldHandler;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;

public class ModbusPlcFieldHandler extends DefaultPlcFieldHandler {

    @Override
    public PlcField createField(String fieldQuery) throws PlcInvalidFieldException {
        if (MaskWriteRegisterModbusField.ADDRESS_PATTERN.matcher(fieldQuery).matches()) {
            return MaskWriteRegisterModbusField.of(fieldQuery);
        } else if (ReadDiscreteInputsModbusField.ADDRESS_PATTERN.matcher(fieldQuery).matches()) {
            return ReadDiscreteInputsModbusField.of(fieldQuery);
        } else if (ReadHoldingRegistersModbusField.ADDRESS_PATTERN.matcher(fieldQuery).matches()) {
            return ReadHoldingRegistersModbusField.of(fieldQuery);
        } else if (ReadInputRegistersModbusField.ADDRESS_PATTERN.matcher(fieldQuery).matches()) {
            return ReadInputRegistersModbusField.of(fieldQuery);
        } else if (CoilModbusField.ADDRESS_PATTERN.matcher(fieldQuery).matches()) {
            return CoilModbusField.of(fieldQuery);
        } else if (RegisterModbusField.ADDRESS_PATTERN.matcher(fieldQuery).matches()) {
            return RegisterModbusField.of(fieldQuery);
        }
        throw new PlcInvalidFieldException(fieldQuery);
    }

    @Override
    public PlcValue encodeBoolean(PlcField field, Object[] values) {
        ModbusField modbusField = (ModbusField) field;
        List<Boolean> booleanValues = new LinkedList<>();
        for (Object value : values) {
            if (value instanceof Boolean) {
                Boolean booleanValue = (Boolean) value;
                booleanValues.add(booleanValue);
            } else if (value instanceof Byte) {
                Byte byteValue = (Byte) value;
                BitSet bitSet = BitSet.valueOf(new byte[]{byteValue});
                for (int i = 0; i < 8; i++) {
                    booleanValues.add(bitSet.get(i));
                }
            } else if (value instanceof Short) {
                Short shortValue = (Short) value;
                BitSet bitSet = BitSet.valueOf(new long[]{shortValue});
                for (int i = 0; i < 16; i++) {
                    booleanValues.add(bitSet.get(i));
                }
            } else if (value instanceof Integer) {
                Integer integerValue = (Integer) value;
                BitSet bitSet = BitSet.valueOf(new long[]{integerValue});
                for (int i = 0; i < 32; i++) {
                    booleanValues.add(bitSet.get(i));
                }
            } else if (value instanceof Long) {
                long longValue = (Long) value;
                BitSet bitSet = BitSet.valueOf(new long[]{longValue});
                for (int i = 0; i < 64; i++) {
                    booleanValues.add(bitSet.get(i));
                }
            } else {
                throw new IllegalArgumentException(
                    "Value of type " + value.getClass().getName() +
                        " is not assignable to " + modbusField + " fields.");
            }
        }
        if(booleanValues.size() == 1) {
            return new PlcBoolean(booleanValues.get(0));
        } else {
            return new PlcList(booleanValues);
        }
    }

    /*@Override
    public PlcValue encodeByteArray(PlcField field, Object[] values) {
        ModbusField modbusField = (ModbusField) field;
        List<Byte[]> byteArrays = new LinkedList<>();
        for (Object value : values) {
            if (value instanceof byte[]) {
                byte[] byteArray = (byte[]) value;
                byteArrays.add(ArrayUtils.toObject(byteArray));
            } else if (value instanceof Byte[]) {
                Byte[] byteArray = (Byte[]) value;
                byteArrays.add(byteArray);
            } else {
                throw new IllegalArgumentException(
                    "Value of type " + value.getClass().getName() +
                        " is not assignable to " + modbusField + " fields.");
            }
        }
        return new DefaultModbusByteArrayPlcValue(byteArrays.toArray(new Byte[0][0]));
    }*/
}
