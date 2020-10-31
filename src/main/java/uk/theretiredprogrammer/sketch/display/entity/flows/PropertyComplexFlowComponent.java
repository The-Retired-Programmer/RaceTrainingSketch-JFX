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
package uk.theretiredprogrammer.sketch.display.entity.flows;

import java.util.function.Supplier;
import uk.theretiredprogrammer.sketch.core.entity.Area;
import static uk.theretiredprogrammer.sketch.core.entity.PropertyMap.PropertyConfig.OPTIONAL;
import uk.theretiredprogrammer.sketch.core.entity.PropertySpeedPolar;
import uk.theretiredprogrammer.sketch.core.entity.SpeedPolar;
import static uk.theretiredprogrammer.sketch.core.entity.SpeedPolar.FLOWZERO;

/*
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class PropertyComplexFlowComponent extends PropertyFlowComponent {

    private final PropertyConfig<PropertySpeedPolar, SpeedPolar> northwestflow = new PropertyConfig<>("northwestflow", OPTIONAL, (s) -> new PropertySpeedPolar(s, FLOWZERO));
    private final PropertyConfig<PropertySpeedPolar, SpeedPolar> northeastflow = new PropertyConfig<>("northeastflow", OPTIONAL, (s) -> new PropertySpeedPolar(s, FLOWZERO));
    private final PropertyConfig<PropertySpeedPolar, SpeedPolar> southeastflow = new PropertyConfig<>("southeastflow", OPTIONAL, (s) -> new PropertySpeedPolar(s, FLOWZERO));
    private final PropertyConfig<PropertySpeedPolar, SpeedPolar> southwestflow = new PropertyConfig<>("southwestflow", OPTIONAL, (s) -> new PropertySpeedPolar(s, FLOWZERO));

    public PropertyComplexFlowComponent(Supplier<Area> getdisplayarea, String type) {
        super(getdisplayarea, type);
        this.addConfig(northwestflow, northeastflow, southeastflow, southwestflow);
    }

    public SpeedPolar getNorthwestflow() {
        return northwestflow.get("PropertyComplexFlowcomponent northwestflow");
    }

    public SpeedPolar getNortheastflow() {
        return northeastflow.get("PropertyComplexFlowcomponent northeastflow");
    }

    public SpeedPolar getSoutheastflow() {
        return southeastflow.get("PropertyComplexFlowcomponent southeastflow");
    }

    public SpeedPolar getSouthwestflow() {
        return southwestflow.get("PropertyComplexFlowcomponent southwestflow");
    }
}
