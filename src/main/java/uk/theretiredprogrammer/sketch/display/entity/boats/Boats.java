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
package uk.theretiredprogrammer.sketch.display.entity.boats;

import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import uk.theretiredprogrammer.sketch.core.control.ParseFailure;
import uk.theretiredprogrammer.sketch.core.entity.ModelNamedList;
import uk.theretiredprogrammer.sketch.display.entity.base.SketchModel;
import uk.theretiredprogrammer.sketch.display.entity.course.CurrentLeg;

public class Boats extends ModelNamedList<Boat> {

    private final SketchModel model;

    public Boats(SketchModel model) {
        super("Boat");
        this.model = model;
    }

    @Override
    protected Boat createAndParse(JsonValue jval) {
        if (jval.getValueType() != JsonValue.ValueType.OBJECT) {
            throw new ParseFailure("Malformed Definition File - array contains items other than Objects");
        }
        JsonObject jobj = (JsonObject) jval;
        Boat boat = BoatFactory.createBoat(jobj.getString("type", "<undefined>"),
                new CurrentLeg(model.getCourse()),
                model.getWindFlow(),
                model.getWaterFlow());
        boat.parse(jobj);
        return boat;
    }
}
