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
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import uk.theretiredprogrammer.sketch.core.ui.UI;

public class Booln extends SimpleBooleanProperty implements ModelProperty<Boolean> {

    public Booln(Boolean defaultvalue) {
        super(defaultvalue);
    }
    
    public Booln(boolean defaultvalue) {
        super(defaultvalue);
    }
    
    public Booln(Booln clonefrom) {
        super(clonefrom.get());
    }

    @Override
    public void setOnChange(Runnable onchange) {
        addListener((o, oldval, newval) -> onchange.run());
    }

    @Override
    public final Boolean parsevalue(JsonValue value) {
        return FromJson.booln(value);
    }

    @Override
    public JsonValue toJson() {
        return ToJson.serialise(this);
    }

    @Override
    public Node getControl() {
        return UI.control(this);
    }

    @Override
    public Node getControl(int size) {
        return getControl();
    }

    @Override
    public final void parse(JsonValue jvalue) {
        set(parsevalue(jvalue));
    }
}
