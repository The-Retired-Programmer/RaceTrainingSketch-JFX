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
package uk.theretiredprogrammer.sketch.flows;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import uk.theretiredprogrammer.sketch.properties.PropertyItem;
import uk.theretiredprogrammer.sketch.ui.Controller;

/**
 *
 * @author richard
 */
public class WindFlow extends Flow {

    public static WindFlow create(Supplier<Controller> controllersupplier, JsonObject parsedjson) throws IOException {
        JsonArray windarray = parsedjson.getJsonArray("wind");
        FlowComponentSet flowcomponents = new FlowComponentSet();
        if (windarray != null) {
            for (JsonValue windv : windarray) {
                if (windv.getValueType() == JsonValue.ValueType.OBJECT) {
                    JsonObject wind = (JsonObject) windv;
                    flowcomponents.add(FlowComponentFactory.createflowelement(controllersupplier, wind));
                } else {
                    throw new IOException("Malformed Definition File - <wind> array contains items other that wind objects");
                }
            }
        }
        JsonObject windshiftparams = parsedjson.getJsonObject("windshifts");
        return new WindFlow(controllersupplier, windshiftparams, flowcomponents);
    }

    private WindFlow(Supplier<Controller> controllersupplier, JsonObject params, FlowComponentSet flowcomponents) throws IOException {
        super(controllersupplier, params, flowcomponents);
    }

    @Override
    public Map<String, PropertyItem> properties() {
        LinkedHashMap<String, PropertyItem> map = new LinkedHashMap<>();
        super.properties(map);
        return map;
    }
}
