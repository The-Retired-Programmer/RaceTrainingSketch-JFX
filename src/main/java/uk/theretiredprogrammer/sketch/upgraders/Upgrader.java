/*
 * Copyright 2020-2021 Richard Linsdale (richard at theretiredprogrammer.uk).
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
package uk.theretiredprogrammer.sketch.upgraders;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonPointer;

public abstract class Upgrader {

    public abstract JsonObject upgrade(JsonObject oldversion);

    JsonObject copyobj(JsonObject oldroot, String oldname, JsonObject newroot, JsonPointer insertpoint) {
        return addobj( oldroot.getJsonObject(oldname), newroot, insertpoint);
    }
    
    JsonObject addobj(JsonObject newobj, JsonObject newroot, JsonPointer insertpoint) {
        return newobj == null ? newroot : insertpoint.add(newroot, newobj);
    }

    JsonObject copyarray(JsonObject oldroot, String oldname, JsonObject newroot, JsonPointer insertpoint) {
        return addarray(oldroot.getJsonArray(oldname), newroot, insertpoint);
    }

    JsonObject addarray(JsonArray newarray, JsonObject newroot, JsonPointer insertpoint) {
        return newarray == null ? newroot : insertpoint.add(newroot, newarray);
    }

}
