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
package org.apache.plc4x.java.modbus.messages.items;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.plc4x.java.api.value.PlcList;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * default implementation for PlcValue for Usage within Modbus module
 * default ByteOrder is set to BIG_ENDIAN, can be selected on regarding get-method explicitly from user if needed
 */
public class DefaultModbusByteArrayPlcList extends PlcList {

    private static final int SHORT_BYTES = 2;
    private static final int INTEGER_BYTES = 4;
    private static final int FLOAT_BYTES = 4;
    private static final int LONG_BYTES = 8;
    private static final ByteOrder DEFAULT_ENDIANNESS = ByteOrder.BIG_ENDIAN;

    private ByteOrder byteOrder;

    private Byte[] completeByteArray;

    public DefaultModbusByteArrayPlcList(Byte[]... values) {
        super(Arrays.asList(values));
        this.byteOrder = DEFAULT_ENDIANNESS;
        this.completeByteArray = Arrays.stream(values).flatMap(Stream::of).toArray(Byte[]::new);
    }

    public boolean isValidShort(int index) {
        return this.completeByteArray.length >= shortIndexToByteIndex(index) + SHORT_BYTES;
    }

    /**
     * returns the short result for the given index with explicit chose of ByteOrder
     *
     * @param index     index in relation to the requested data-type (here Short), see comment of regarding index-transformation
     * @param byteOrder byte-order used for decoding of byte-array
     * @return resulting short value if valid data is given, null otherwise
     */
    public Short getShort(int index, ByteOrder byteOrder) {
        if (!isValidShort(index)) {
            return null;
        }
        return ByteBuffer
            .wrap(ArrayUtils.toPrimitive(getByteArrayFromIndex(shortIndexToByteIndex(index))))
            .order(byteOrder)
            .getShort();
    }

    /**
     * converts the starting index of a short array to source type Byte
     * e.g. user wants to request the 2nd long value --&gt; index=1 --&gt; byteIndex=2
     *
     * @param shortIndex index from users view
     * @return resulting byteArrayIndex
     */
    private static int shortIndexToByteIndex(int shortIndex) {
        return shortIndex * SHORT_BYTES;
    }

    public boolean isValidInteger(int index) {
        return this.completeByteArray.length >= intIndexToByteIndex(index) + INTEGER_BYTES;
    }

    /**
     * returns the int result for the given index with explicit chose of ByteOrder
     *
     * @param index     index in relation to the requested data-type (here Integer), see comment of regarding index-transformation
     * @param byteOrder byte-order used for decoding of byte-array
     * @return resulting int value if valid data is given, null otherwise
     */
    public Integer getInteger(int index, ByteOrder byteOrder) {
        if (!isValidInteger(index)) {
            return null;
        }
        return ByteBuffer
            .wrap(ArrayUtils.toPrimitive(getByteArrayFromIndex(intIndexToByteIndex(index))))
            .order(byteOrder)
            .getInt();
    }

    /**
     * converts the starting index of an int array to source type Byte
     * e.g. user wants to request the 4th integer value --&gt; index=3 --&gt; byteIndex=12
     *
     * @param intIndex index from users view
     * @return resulting byteArrayIndex
     */
    private static int intIndexToByteIndex(int intIndex) {
        return intIndex * INTEGER_BYTES;
    }

    public boolean isValidLong(int index) {
        return this.completeByteArray.length >= longIndexToByteIndex(index) + LONG_BYTES;
    }

    /**
     * returns the long result for the given index with explicit chose of ByteOrder
     *
     * @param index     index in relation to the requested data-type (here Long), see comment of regarding index-transformation
     * @param byteOrder byte-order used for decoding of byte-array
     * @return resulting long value if valid data is given, null otherwise
     */
    public Long getLong(int index, ByteOrder byteOrder) {
        if (!isValidLong(index)) {
            return null;
        }
        return ByteBuffer
            .wrap(ArrayUtils.toPrimitive(getByteArrayFromIndex(longIndexToByteIndex(index))))
            .order(byteOrder)
            .getLong();
    }

    /**
     * converts the starting index of a long array to source type Byte
     * e.g. user wants to request the 3rd long value --&gt; index=2 --&gt; byteIndex=16
     *
     * @param longIndex index from users view
     * @return resulting byteArrayIndex
     */
    private static int longIndexToByteIndex(int longIndex) {
        return longIndex * LONG_BYTES;
    }

    public boolean isValidFloat(int index) {
        return this.completeByteArray.length >= floatIndexToByteIndex(index) + FLOAT_BYTES;
    }

    /**
     * returns the float result for the given index with explicit chose of ByteOrder
     *
     * @param index     index in relation to the requested data-type (here Float), see comment of regarding index-transformation
     * @param byteOrder byte-order used for decoding of byte-array
     * @return resulting float value if valid data is given, null otherwise
     */
    public Float getFloat(int index, ByteOrder byteOrder) {
        if (!isValidFloat(index)) {
            return null;
        }
        return ByteBuffer
            .wrap(ArrayUtils.toPrimitive(getByteArrayFromIndex(floatIndexToByteIndex(index))))
            .order(byteOrder)
            .getFloat();
    }

    /**
     * converts the starting index of an float array to source type Byte
     * e.g. user wants to request the 4th float value --&gt; index=3 --&gt; byteIndex=12
     *
     * @param floatIndex index from users view
     * @return resulting byteArrayIndex
     */
    private static int floatIndexToByteIndex(int floatIndex) {
        return floatIndex * FLOAT_BYTES;
    }

    /**
     * returns a subarray with the wanted index first
     *
     * @param index start-index of wanted value
     * @return the sub-array
     */
    private Byte[] getByteArrayFromIndex(int index) {
        if (index > this.completeByteArray.length) {
            return new Byte[0];
        }
        return Arrays.copyOfRange(this.completeByteArray, index, this.completeByteArray.length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        DefaultModbusByteArrayPlcList that = (DefaultModbusByteArrayPlcList) o;
        return Arrays.equals(completeByteArray, that.completeByteArray);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(completeByteArray);
        return result;
    }

    //ToDo: Implement conversion for Unsigned-Datatypes

    //ToDo: Add exceptions to avoid unwanted states --> e.g. neg indexes

}
