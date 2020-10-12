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
import jakarta.json.JsonArray;
import jakarta.json.JsonValue;
import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import uk.theretiredprogrammer.sketch.core.LegValue;
import uk.theretiredprogrammer.sketch.controller.Controller;

/**
 *
 * @author richard
 */
public class PropertyLegValue extends PropertyElement<LegValue> {

    private final PropertyConstrainedString marknameproperty;
    private final PropertyConstrainedString roundingproperty;

    public PropertyLegValue(ObservableList<String> marknames, ObservableList<String> roundings) {
        marknameproperty = new PropertyConstrainedString(marknames);
        roundingproperty = new PropertyConstrainedString(roundings);
    }

    public PropertyLegValue(LegValue defaultvalue, ObservableList<String> marknames, ObservableList<String> roundings) {
        marknameproperty = new PropertyConstrainedString(marknames, defaultvalue.getMarkname());
        roundingproperty = new PropertyConstrainedString(roundings, defaultvalue.getRoundingdirection());
    }

    public String getMarkname() {
        return marknameproperty.get();
    }

    public String getRounding() {
        return roundingproperty.get();
    }

    @Override
    public final LegValue get() {
        return new LegValue(marknameproperty.get(), roundingproperty.get());
    }

    @Override
    public final void set(LegValue newleg) throws IOException {
        marknameproperty.setValue(newleg.getMarkname());
        roundingproperty.set(newleg.getRoundingdirection());
    }

    @Override
    public final LegValue parsevalue(JsonValue jvalue) throws IOException {
        if (jvalue != null && jvalue.getValueType() == JsonValue.ValueType.ARRAY) {
            JsonArray values = (JsonArray) jvalue;
            if (values.size() == 2) {
                return new LegValue(
                        marknameproperty.parsevalue(values.get(0)),
                        roundingproperty.parsevalue(values.get(1))
                );
            }
        }
        throw new IOException("Malformed Definition file - List of 2 Strings expected");
    }

    @Override
    public JsonArray toJson() {
        return Json.createArrayBuilder()
                .add(marknameproperty.get())
                .add(roundingproperty.get())
                .build();
    }

    @Override
    public Node getField(Controller controller) {
        return new HBox(marknameproperty.getField(controller), roundingproperty.getField(controller));
    }

    @Override
    public Node getField(Controller controller, int size) {
        return getField(controller, size, size);
    }

    private Node getField(Controller controller, int sizemark, int sizerounding) {
        return new HBox(marknameproperty.getField(controller, sizemark), roundingproperty.getField(controller, sizerounding));
    }

    @Override
    public final void parse(JsonValue jvalue) throws IOException {
        set(parsevalue(jvalue));
    }
}