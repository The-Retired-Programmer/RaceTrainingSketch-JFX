/*
 * Copyright 2014-2020 Richard Linsdale (richard at theretiredprogrammer.uk).
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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import uk.theretiredprogrammer.sketch.core.entity.PropertyConstrainedString;

public class LegEnding {

    private static final ObservableList<String> roundingdirections;

    static {
        roundingdirections = FXCollections.observableArrayList();
        roundingdirections.addAll("port", "starboard");
    }

    public static ObservableList<String> getRoundingdirections() {
        return roundingdirections;
    }

    private final PropertyConstrainedString mark;
    private final PropertyConstrainedString passing = new PropertyConstrainedString(roundingdirections);

    public LegEnding(String mark, String passing, ObservableList<String> marknames) {
        this.mark = new PropertyConstrainedString(mark, marknames);
        this.passing.set(passing.toLowerCase());
    }

    public String getRoundingdirection() {
        return passing.get();
    }

    public PropertyConstrainedString getRoundingdirectionProperty() {
        return passing;
    }

    public boolean isPortRounding() {
        return passing.get().equals("port");
    }

    public String getMarkname() {
        return mark.get();
    }

    public PropertyConstrainedString getMarknameProperty() {
        return mark;
    }

    @Override
    public String toString() {
        return mark.get() + " to " + passing.get();
    }
}
