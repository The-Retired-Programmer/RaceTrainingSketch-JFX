/*
 * Copyright 2014-2021 Richard Linsdale (richard at theretiredprogrammer.uk).
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

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import java.util.Optional;
import uk.theretiredprogrammer.sketch.core.entity.ModelMap;
import uk.theretiredprogrammer.sketch.core.entity.Location;

public class Course extends ModelMap {

    private final Location start = new Location();
    private final Legs legs;

    private Runnable onchange;

    public Course(Marks marks) {
        legs = new Legs(marks);
        this.addProperty("start", start);
        this.addProperty("legs", legs);
        legs.setOnChange(() -> updatelegs());
        legs.addListChangeListener((c) -> updatelegs());
        marks.setOnChange(()-> updatelegs());
    }

    private void updatelegs() {
        Location startpos = start;
        for (var leg : legs.get()) {
            leg.update(startpos);
            startpos = leg.getEndLocation();
        }
        if (onchange != null) {
            onchange.run();
        }
    }

    @Override
    protected void parseValues(JsonObject jobj) {
        parseMandatoryProperty("start", start, jobj);
        parseOptionalProperty("legs", legs, jobj);
    }

    @Override
    public void setOnChange(Runnable onchange) {
        this.onchange = onchange;
    }

    @Override
    public JsonObject toJson() {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("start", start.toJson());
        job.add("legs", legs.toJson());
        return job.build();
    }

    public Legs getLegs() {
        return legs;
    }

    public Optional<Leg> getLeg(int legno) {
        return Optional.ofNullable(legs.get(legno));
    }
}
