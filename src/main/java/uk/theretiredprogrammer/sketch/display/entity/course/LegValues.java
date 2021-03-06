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
package uk.theretiredprogrammer.sketch.display.entity.course;

import jakarta.json.JsonArray;
import jakarta.json.JsonValue;
import javafx.scene.Node;
import uk.theretiredprogrammer.sketch.core.control.IllegalStateFailure;
import uk.theretiredprogrammer.sketch.core.entity.ModelProperty;
import uk.theretiredprogrammer.sketch.core.entity.FromJson;

public class LegValues implements ModelProperty<LegValues> {

    final String markname;
    final String passing;

    public LegValues(String markname, String passing) {
        this.markname = markname;
        this.passing = passing;
        // need to check values here !!
    }

    @Override
    public final LegValues parsevalue(JsonValue jvalue) {
        return FromJson.legvalues(jvalue);
    }

    @Override
    public final void parse(JsonValue jvalue) {
        throw new IllegalStateFailure("LegValues: is not processing model so does not suport SetOnChange");
    }

    @Override
    public JsonArray toJson() {
        throw new IllegalStateFailure("LegValues: is not processing model so does not suport SetOnChange");
    }

    @Override
    public Node getControl() {
        throw new IllegalStateFailure("LegValues: is not processing model so does not suuport SetOnChange");
    }

    @Override
    public Node getControl(int size) {
        throw new IllegalStateFailure("LegValues: is not processing model so does not suport SetOnChange");
    }

    @Override
    public void setOnChange(Runnable onchange) {
        throw new IllegalStateFailure("LegValues: is not processing model so does not suuport SetOnChange");
    }
}
