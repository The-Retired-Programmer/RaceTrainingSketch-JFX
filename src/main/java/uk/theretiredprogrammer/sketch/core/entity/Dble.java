/*
 * Copyright 2020 Richard Linsdale (richard at theretiredprogrammer.uk).
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

import jakarta.json.JsonValue;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.TextField;
import uk.theretiredprogrammer.sketch.core.ui.UI;

public class Dble extends SimpleDoubleProperty implements ModelProperty<Double> {

    public Dble(Double value) {
        set(value);
    }
    
    public Dble(double value) {
        set(value);
    }
    
    public Dble(Dble clonefrom){
        set(clonefrom.get());
    }

    @Override
    public void setOnChange(Runnable onchange) {
        addListener((o, oldval, newval) -> onchange.run());
    }

    @Override
    public Double parsevalue(JsonValue jvalue) {
        return FromJson.dble(jvalue);
    }

    @Override
    public JsonValue toJson() {
        return ToJson.serialise(this);
    }

    @Override
    public TextField getControl() {
        return getControl(10);
    }

    @Override
    public TextField getControl(int size) {
        return UI.control(size, this);
    }

    @Override
    public final void parse(JsonValue jvalue) {
        set(parsevalue(jvalue));
    }
}
