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
package uk.theretiredprogrammer.sketch.core.entity;

import jakarta.json.Json;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import java.lang.reflect.Field;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import uk.theretiredprogrammer.sketch.core.control.ParseFailure;

/**
 *
 * @author richard
 */
public class PropertyColour extends PropertyElement<Color> {

    private SimpleObjectProperty<Color> colourproperty;

    public PropertyColour(Color defaultvalue) {
        this(null, defaultvalue);
    }

    public PropertyColour(String key, Color defaultvalue) {
        setKey(key);
        colourproperty = new SimpleObjectProperty(defaultvalue);
    }

    public void setOnChange(Runnable onchange) {
        //setOnChange((c) -> onchange.run());
    }

    public void setOnChange(ChangeListener cl) {
        colourproperty.addListener(cl);
    }

    @Override
    public final Color get() {
        return colourproperty.get();
    }

    @Override
    public final void set(Color colour) {
        colourproperty.set(colour);
    }

    @Override
    public Color parsevalue(JsonValue value) {
        if (value != null && value.getValueType() == JsonValue.ValueType.STRING) {
            Color color = string2color(((JsonString) value).getString());
            if (color != null) {
                return color;
            }
        }
        throw new ParseFailure("Malformed Definition file - Colour name or hex string expected");
    }

    @Override
    public JsonValue toJson() {
        return Json.createValue(color2String(get()));
    }

    @Override
    public Node getField() {
        return getField(0);
    }

    @Override
    public Node getField(int size) {
        ColorPicker picker = new ColorPicker();
        picker.setValue(get());
        picker.setOnAction(actionEvent -> {
            set(picker.getValue());
        });
        return picker;
    }

    private Color string2color(String value) {
        try {
            return Color.valueOf(value.toLowerCase());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private String color2String(Color color) {
        final Field[] fields = Color.class.getFields(); // only want public
        for (final Field field : fields) {
            if (field.getType() == Color.class) {
                try {
                    final Color clr = (Color) field.get(null);
                    if (color.equals(clr)) {
                        return field.getName();
                    }
                } catch (IllegalAccessException ex) {
                    return "Securty Manager does not allow access to field '" + field.getName() + "'.";
                }
            }
        }
        return color.toString();
    }

    @Override
    public final void parse(JsonValue jvalue) {
        set(parsevalue(jvalue));
    }
}
