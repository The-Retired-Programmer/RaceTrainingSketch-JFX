/*
 * Copyright 2014-2020 Richard Linsdale.
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
package uk.theretiredprogrammer.sketch.display.entity.base;

import uk.theretiredprogrammer.sketch.core.entity.PropertyInteger;
import uk.theretiredprogrammer.sketch.core.entity.PropertyDouble;
import uk.theretiredprogrammer.sketch.core.entity.PropertyArea;
import uk.theretiredprogrammer.sketch.core.entity.PropertyMap;
import uk.theretiredprogrammer.sketch.core.entity.Area;
import static uk.theretiredprogrammer.sketch.core.entity.Area.AREAZERO;
import uk.theretiredprogrammer.sketch.core.entity.Location;
import static uk.theretiredprogrammer.sketch.core.entity.PropertyMap.PropertyConfig.OPTIONAL;

/**
 * The Information to describe the Simulation "Field of play and display
 * pararmeters
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class PropertyDisplay extends PropertyMap {

    public static final Area DISPLAYAREADEFAULT = new Area(new Location(-500, -500), 1000, 1000);

    private final PropertyConfig<PropertyDouble, Double> zoom = new PropertyConfig<>("zoom", OPTIONAL, (s) -> new PropertyDouble(s, 1.0));
    private final PropertyConfig<PropertyInteger, Integer> secondsperdisplay = new PropertyConfig<>("secondsperdisplay", OPTIONAL, (s) -> new PropertyInteger(s, 1));
    private final PropertyConfig<PropertyDouble, Double> speedup = new PropertyConfig<PropertyDouble, Double>("speedup", OPTIONAL, (s) -> new PropertyDouble(s, 1.0));
    private final PropertyConfig<PropertyArea, Area> displayarea = new PropertyConfig<PropertyArea, Area>("displayarea", OPTIONAL, (s) -> new PropertyArea(s, DISPLAYAREADEFAULT));
    private final PropertyConfig<PropertyArea, Area> sailingarea = new PropertyConfig<PropertyArea, Area>("sailingarea", OPTIONAL, (s) -> new PropertyArea(s, AREAZERO));

    public PropertyDisplay(String key) {
        setKey(key);
        addConfig(zoom, secondsperdisplay, speedup, displayarea, sailingarea);
    }

    @Override
    public PropertyDisplay get() {
        return this;
    }

    public double getZoom() {
        return zoom.get("PropertyDisplay zoom");
    }

    public int getSecondsperdisplay() {
        return secondsperdisplay.get("PropertyDisplay secondsperdisplay");
    }

    public double getSpeedup() {
        return speedup.get("PropertyDisplay speedup");
    }

    public Area getSailingarea() {
        Area area = sailingarea.get("PropertyDisplay sailingarea");
        return area.equals(AREAZERO) ? getDisplayarea() : area;
    }

    public Area getDisplayarea() {
        return displayarea.get("PropertyDisplay displayarea");
    }
}
