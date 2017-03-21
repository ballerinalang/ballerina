/*
 * Copyright (c) 2017, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 * <p>
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.ballerinalang.natives.typemappers;

import org.ballerinalang.model.types.BTypes;
import org.ballerinalang.model.values.BBoolean;
import org.ballerinalang.model.values.BDouble;
import org.ballerinalang.model.values.BFloat;
import org.ballerinalang.model.values.BInteger;
import org.ballerinalang.model.values.BLong;
import org.ballerinalang.model.values.BString;
import org.ballerinalang.model.values.BValueType;
import org.ballerinalang.util.exceptions.BLangExceptionHelper;
import org.ballerinalang.util.exceptions.RuntimeErrors;

import java.util.function.Function;

/**
 * Type mapper for converting across native types.
 */
public class NativeCastMapper {

    public static final Function<BValueType, BValueType> INT_TO_LONG_FUNC =
            (rVal) -> new BLong(rVal.longValue());

    public static final Function<BValueType, BValueType> INT_TO_FLOAT_FUNC =
            (rVal) -> new BFloat(rVal.floatValue());

    public static final Function<BValueType, BValueType> INT_TO_DOUBLE_FUNC =
            (rVal) -> new BDouble(rVal.doubleValue());

    public static final Function<BValueType, BValueType> INT_TO_STRING_FUNC =
            (rVal) -> new BString(rVal.stringValue());

    public static final Function<BValueType, BValueType> INT_TO_BOOLEAN_FUNC =
            (rVal) -> new BBoolean(rVal.intValue() != 0);

    public static final Function<BValueType, BValueType> INT_TO_INT_FUNC =
            (rVal) -> rVal;

    public static final Function<BValueType, BValueType> LONG_TO_FLOAT_FUNC =
            (rVal) -> new BFloat(rVal.floatValue());

    public static final Function<BValueType, BValueType> LONG_TO_DOUBLE_FUNC =
            (rVal) -> new BDouble(rVal.doubleValue());

    public static final Function<BValueType, BValueType> LONG_TO_STRING_FUNC =
            (rVal) -> new BString(rVal.stringValue());

    public static final Function<BValueType, BValueType> LONG_TO_INT_FUNC =
            (rVal) -> new BInteger(rVal.intValue());

    public static final Function<BValueType, BValueType> LONG_TO_LONG_FUNC =
            (rVal) -> rVal;

    public static final Function<BValueType, BValueType> FLOAT_TO_DOUBLE_FUNC =
            (rVal) -> new BDouble(rVal.doubleValue());

    public static final Function<BValueType, BValueType> FLOAT_TO_STRING_FUNC =
            (rVal) -> new BString(rVal.stringValue());

    public static final Function<BValueType, BValueType> FLOAT_TO_BOOLEAN_FUNC =
            (rVal) -> new BBoolean(rVal.floatValue() != 0.0);

    public static final Function<BValueType, BValueType> FLOAT_TO_FLOAT_FUNC =
            (rVal) -> rVal;

    public static final Function<BValueType, BValueType> FLOAT_TO_LONG_FUNC =
            (rVal) -> new BLong(rVal.longValue());

    public static final Function<BValueType, BValueType> FLOAT_TO_INT_FUNC =
            (rVal) -> new BInteger(rVal.intValue());

    public static final Function<BValueType, BValueType> DOUBLE_TO_STRING_FUNC =
            (rVal) -> new BString(rVal.stringValue());

    public static final Function<BValueType, BValueType> DOUBLE_TO_DOUBLE_FUNC =
            (rVal) -> rVal;

    public static final Function<BValueType, BValueType> DOUBLE_TO_FLOAT_FUNC =
            (rVal) -> new BFloat(rVal.floatValue());

    public static final Function<BValueType, BValueType> DOUBLE_TO_INT_FUNC =
            (rVal) -> new BInteger(rVal.intValue());

    public static final Function<BValueType, BValueType> DOUBLE_TO_LONG_FUNC =
            (rVal) -> new BLong(rVal.longValue());

    public static final Function<BValueType, BValueType> STRING_TO_INT_FUNC =
            (rVal) -> new BInteger(rVal.intValue());

    public static final Function<BValueType, BValueType> STRING_TO_LONG_FUNC =
            (rVal) -> new BLong(rVal.longValue());

    public static final Function<BValueType, BValueType> STRING_TO_FLOAT_FUNC =
            (rVal) -> new BFloat(rVal.floatValue());

    public static final Function<BValueType, BValueType> STRING_TO_DOUBLE_FUNC =
            (rVal) -> new BDouble(rVal.doubleValue());

    public static final Function<BValueType, BValueType> STRING_TO_BOOLEAN_FUNC =
            (rVal) -> new BBoolean(rVal.booleanValue());

    public static final Function<BValueType, BValueType> STRING_TO_STRING_FUNC =
            (rVal) -> rVal;

    public static final Function<BValueType, BValueType> BOOLEAN_TO_STRING_FUNC =
            (rVal) -> new BString(rVal.stringValue());

    public static final Function<BValueType, BValueType> BOOLEAN_TO_INT_FUNC =
            (rVal) -> rVal.booleanValue() ? new BInteger(1) : new BInteger(0);

    public static final Function<BValueType, BValueType> BOOLEAN_TO_FLOAT_FUNC =
            (rVal) -> rVal.booleanValue() ? new BFloat(1.0f) : new BFloat(0.0f);

    public static final Function<BValueType, BValueType> BOOLEAN_TO_BOOLEAN_FUNC =
            (rVal) -> rVal;

    public static final Function<BValueType, BValueType> JSON_TO_STRING_FUNC =
            (rVal) -> new BString(rVal.stringValue());

    public static final Function<BValueType, BValueType> XML_TO_STRING_FUNC =
            (rVal) -> new BString(rVal.stringValue());


    public static final Function<BValueType, BValueType> ANY_TO_INT_FUNC =
            (rVal) -> {
                if (rVal.getType() == BTypes.typeInt) {
                    return rVal;
                }
                BLangExceptionHelper.throwRuntimeError(RuntimeErrors.CASTING_ANY_TYPE_TO_WRONG_VALUE_TYPE,
                                                       rVal.getType(), BTypes.typeInt);
                return null; //this is unreachable practically, because above line throws an exception
            };

    public static final Function<BValueType, BValueType> ANY_TO_LONG_FUNC =
            (rVal) -> {
                if (rVal.getType() == BTypes.typeLong) {
                    return rVal;
                }
                BLangExceptionHelper.throwRuntimeError(RuntimeErrors.CASTING_ANY_TYPE_TO_WRONG_VALUE_TYPE,
                                                       rVal.getType(), BTypes.typeLong);
                return null; //this is unreachable practically, because above line throws an exception
            };

    public static final Function<BValueType, BValueType> ANY_TO_FLOAT_FUNC =
            (rVal) -> {
                if (rVal.getType() == BTypes.typeFloat) {
                    return rVal;
                }
                BLangExceptionHelper.throwRuntimeError(RuntimeErrors.CASTING_ANY_TYPE_TO_WRONG_VALUE_TYPE,
                                                       rVal.getType(), BTypes.typeFloat);
                return null; //this is unreachable practically, because above line throws an exception
            };

    public static final Function<BValueType, BValueType> ANY_TO_DOUBLE_FUNC =
            (rVal) -> {
                if (rVal.getType() == BTypes.typeDouble) {
                    return rVal;
                }
                BLangExceptionHelper.throwRuntimeError(RuntimeErrors.CASTING_ANY_TYPE_TO_WRONG_VALUE_TYPE,
                                                       rVal.getType(), BTypes.typeDouble);
                return null; //this is unreachable practically, because above line throws an exception
            };

    public static final Function<BValueType, BValueType> ANY_TO_STRING_FUNC =
            (rVal) -> {
                if (rVal.getType() == BTypes.typeString) {
                    return rVal;
                }
                BLangExceptionHelper.throwRuntimeError(RuntimeErrors.CASTING_ANY_TYPE_TO_WRONG_VALUE_TYPE,
                                                       rVal.getType(), BTypes.typeString);
                return null; //this is unreachable practically, because above line throws an exception
            };

    public static final Function<BValueType, BValueType> ANY_TO_BOOLEAN_FUNC =
            (rVal) -> {
                if (rVal.getType() == BTypes.typeBoolean) {
                    return rVal;
                }
                BLangExceptionHelper.throwRuntimeError(RuntimeErrors.CASTING_ANY_TYPE_TO_WRONG_VALUE_TYPE,
                                                       rVal.getType(), BTypes.typeBoolean);
                return null; //this is unreachable practically, because above line throws an exception
            };

    public static final Function<BValueType, BValueType> ANY_TO_JSON_FUNC =
            (rVal) -> {
                if (rVal.getType() == BTypes.typeJSON) {
                    return rVal;
                }
                BLangExceptionHelper.throwRuntimeError(RuntimeErrors.CASTING_ANY_TYPE_TO_WRONG_VALUE_TYPE,
                                                       rVal.getType(), BTypes.typeJSON);
                return null; //this is unreachable practically, because above line throws an exception
            };

    public static final Function<BValueType, BValueType> ANY_TO_XML_FUNC =
            (rVal) -> {
                if (rVal.getType() == BTypes.typeXML) {
                    return rVal;
                }
                BLangExceptionHelper.throwRuntimeError(RuntimeErrors.CASTING_ANY_TYPE_TO_WRONG_VALUE_TYPE,
                                                       rVal.getType(), BTypes.typeXML);
                return null; //this is unreachable practically, because above line throws an exception
            };

}
