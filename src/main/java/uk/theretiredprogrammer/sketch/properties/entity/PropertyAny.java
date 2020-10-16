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
package uk.theretiredprogrammer.sketch.properties.entity;

import jakarta.json.JsonValue;
import java.util.Optional;
import uk.theretiredprogrammer.sketch.core.control.IllegalStateFailure;

/**
 *
 * @author richard
 * @param <C>
 */
public abstract class PropertyAny<C> {

    public abstract JsonValue toJson();

    public abstract C get();

    public abstract void parse(JsonValue jvalue);

    private Optional<String> key = Optional.empty();

    final void setKey(String key) {
        this.key = Optional.ofNullable(key);
    }

    final String getKey() {
        return key
                .orElseThrow(() -> new IllegalStateFailure("PropertyAny: getKey() called on uninitiaised key"));
    }

    final String getKeyForInfo() {
        return key.orElse("Undefined");
    }
}
