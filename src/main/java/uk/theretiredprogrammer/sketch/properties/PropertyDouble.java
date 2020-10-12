/*
 * Copyright 2020 richard.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.theretiredprogrammer.sketch.properties;

import jakarta.json.Json;
import jakarta.json.JsonNumber;
import jakarta.json.JsonValue;
import java.io.IOException;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.NumberStringConverter;
import uk.theretiredprogrammer.sketch.controller.Controller;

/**
 *
 * @author richard
 */
public class PropertyDouble extends PropertyElement<Double> {

    private final SimpleDoubleProperty doubleproperty;

    public PropertyDouble(double defaultvalue) {
        this(null, defaultvalue);
    }

    public PropertyDouble(String key, Double defaultvalue) {
        setKey(key);
        doubleproperty = new SimpleDoubleProperty(defaultvalue);
    }

//    public PropertyValueDouble(JsonValue jvalue) throws IOException {
//        this(jvalue, null);
//    }
//
//    public PropertyValueDouble(String key, JsonValue jvalue, Double defaultvalue) throws IOException {
//        this(key, defaultvalue);
//        set(parsevalue(jvalue));
//    }
    public final Double get() {
        return doubleproperty.get();
    }

    public final void set(Double newdouble) {
        doubleproperty.set(newdouble);
    }

    public SimpleDoubleProperty propertyDouble() {
        return doubleproperty;
    }

    @Override
    public Double parsevalue(JsonValue jvalue) throws IOException {
        if (jvalue != null && jvalue.getValueType() == JsonValue.ValueType.NUMBER) {
            return ((JsonNumber) jvalue).doubleValue();
        }
        throw new IOException("Malformed Definition file - Decimal expected");
    }

    @Override
    public JsonValue toJson() {
        return Json.createValue(get());
    }

    @Override
    public TextField getField(Controller controller) {
        return getField(controller, 10);
    }

    @Override
    public TextField getField(Controller controller, int size) {
        TextField doublefield = new TextField(Double.toString(doubleproperty.get()));
        doublefield.setPrefColumnCount(size);
        TextFormatter<Number> textformatter = new TextFormatter<>(new NumberStringConverter(), 0.0, doubleFilter);
        doublefield.setTextFormatter(textformatter);
        textformatter.valueProperty().bindBidirectional(doubleproperty);
        return doublefield;
    }

    public final void parse(JsonValue jvalue) throws IOException {
        set(parsevalue(jvalue));
    }
}