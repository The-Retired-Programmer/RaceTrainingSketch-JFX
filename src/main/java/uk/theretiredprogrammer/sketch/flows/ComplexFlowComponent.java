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
package uk.theretiredprogrammer.sketch.flows;

import jakarta.json.JsonObject;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.function.Supplier;
import uk.theretiredprogrammer.sketch.core.Angle;
import static uk.theretiredprogrammer.sketch.core.Angle.ANGLE0;
import uk.theretiredprogrammer.sketch.core.DoubleParser;
import uk.theretiredprogrammer.sketch.core.Location;
import uk.theretiredprogrammer.sketch.core.PropertySpeedPolar;
import uk.theretiredprogrammer.sketch.core.SpeedPolar;
import uk.theretiredprogrammer.sketch.ui.Controller;

/**
 * The ComplexFlow Class - represents a flow which is described by flows (speed
 * and direction) at the four corners points. Flows within the described area
 * are interpolated.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class ComplexFlowComponent extends FlowComponent {
    
    @Override
    public final String getFlowType() {
        return "Complex Flow";
    }

    private final PropertySpeedPolar nwflowproperty = new PropertySpeedPolar();
    private final PropertySpeedPolar neflowproperty = new PropertySpeedPolar();
    private final PropertySpeedPolar seflowproperty = new PropertySpeedPolar();
    private final PropertySpeedPolar swflowproperty = new PropertySpeedPolar();

    public ComplexFlowComponent(Supplier<Controller> controllersupplier, JsonObject paramsobj) throws IOException {
        super(controllersupplier, paramsobj);
        nwflowproperty.set(new SpeedPolar(
                DoubleParser.parse(paramsobj, "northwestspeed").orElse(0.0),
                Angle.parse(paramsobj, "northwestfrom").orElse(ANGLE0)
        ));
        neflowproperty.set(new SpeedPolar(
                DoubleParser.parse(paramsobj, "northeastspeed").orElse(0.0),
                Angle.parse(paramsobj, "northeastfrom").orElse(ANGLE0)
        ));
        seflowproperty.set(new SpeedPolar(
                DoubleParser.parse(paramsobj, "southeastspeed").orElse(0.0),
                Angle.parse(paramsobj, "southeastfrom").orElse(ANGLE0)
        ));
        swflowproperty.set(new SpeedPolar(
                DoubleParser.parse(paramsobj, "southwestspeed").orElse(0.0),
                Angle.parse(paramsobj, "southwestfrom").orElse(ANGLE0)
        ));
    }

    @Override
    public void change(JsonObject paramsobj) throws IOException {
        super.change(paramsobj);
        nwflowproperty.set(new SpeedPolar(
                DoubleParser.parse(paramsobj, "northwestspeed").orElse(nwflowproperty.getSpeed()),
                Angle.parse(paramsobj, "northwestfrom").orElse(nwflowproperty.getAngle())
        ));
        neflowproperty.set(new SpeedPolar(
                DoubleParser.parse(paramsobj, "northeastspeed").orElse(neflowproperty.getSpeed()),
                Angle.parse(paramsobj, "northeastfrom").orElse(neflowproperty.getAngle())
        ));
        seflowproperty.set(new SpeedPolar(
                DoubleParser.parse(paramsobj, "southeastspeed").orElse(seflowproperty.getSpeed()),
                Angle.parse(paramsobj, "southeastfrom").orElse(seflowproperty.getAngle())
        ));
        swflowproperty.set(new SpeedPolar(
                DoubleParser.parse(paramsobj, "southwestspeed").orElse(swflowproperty.getSpeed()),
                Angle.parse(paramsobj, "southwestfrom").orElse(swflowproperty.getAngle())
        ));
    }
    
    @Override
    public LinkedHashMap<String,Object> properties() {
        LinkedHashMap<String,Object> map = new LinkedHashMap<>();
        super.properties(map);
        map.put("northwestflow", nwflowproperty);
        map.put("northeastflow", neflowproperty);
        map.put("southwestflow", swflowproperty);
        map.put("southeastflow", seflowproperty);
        return map;
    }

    @Override
    public SpeedPolar getFlow(Location pos) throws IOException {
        testLocationWithinArea(pos);
        Location bottomleft = getArea().getBottomleft();
        double xfraction = (pos.getX() - bottomleft.getX()) / getArea().getWidth();
        double yfraction = (pos.getY() - bottomleft.getY()) / getArea().getHeight();
        Location fractions = new Location(xfraction, yfraction);
        return swflowproperty.get().extrapolate(nwflowproperty.get(), neflowproperty.get(), seflowproperty.get(), fractions);
    }
}
