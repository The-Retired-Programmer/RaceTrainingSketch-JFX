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

import jakarta.json.JsonValue;
import java.io.IOException;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import uk.theretiredprogrammer.sketch.controller.Controller;

/**
 *
 * @author richard
 */
public class PropertyBoolean extends PropertyElement<Boolean> {

    private final SimpleBooleanProperty booleanproperty;

    public PropertyBoolean(Boolean defaultvalue) {
        this(null, defaultvalue);
    }

    public PropertyBoolean(String key, Boolean defaultvalue) {
        setKey(key);
        booleanproperty = new SimpleBooleanProperty(defaultvalue);
    }

    @Override
    public final Boolean get() {
        return booleanproperty.get();
    }

    @Override
    public final void set(Boolean newboolean) {
        booleanproperty.set(newboolean);
    }

    public SimpleBooleanProperty propertyBoolean() {
        return booleanproperty;
    }

    @Override
    public final Boolean parsevalue(JsonValue value) throws IOException {
        if (value != null) {
            switch (value.getValueType()) {
                case TRUE -> {
                    return true;
                }
                case FALSE -> {
                    return false;
                }
            }
        }
        throw new IOException("Malformed Definition file - Boolean expected");
    }

    @Override
    public JsonValue toJson() {
        return get() ? JsonValue.TRUE : JsonValue.FALSE;
    }

    @Override
    public Node getField(Controller controller) {
        CheckBox booleanfield = new CheckBox();
        booleanfield.setSelected(get());
        booleanfield.selectedProperty().bindBidirectional(booleanproperty);
        return booleanfield;
    }

    @Override
    public Node getField(Controller controller, int size) {
        return getField(controller);
    }

    public final void parse(JsonValue jvalue) throws IOException {
        set(parsevalue(jvalue));
    }
}