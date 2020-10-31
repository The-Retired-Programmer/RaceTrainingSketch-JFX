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

import uk.theretiredprogrammer.sketch.core.entity.PropertyGradient;
import java.util.function.Supplier;
import uk.theretiredprogrammer.sketch.core.entity.Area;
import uk.theretiredprogrammer.sketch.core.entity.Gradient;
import static uk.theretiredprogrammer.sketch.core.entity.Gradient.GRADIENTDEFAULT;
import static uk.theretiredprogrammer.sketch.core.entity.PropertyMap.PropertyConfig.OPTIONAL;

/*
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class PropertyGradientFlowComponent extends PropertyFlowComponent {

    private final PropertyConfig<PropertyGradient, Gradient> gradient = new PropertyConfig<>("gradient", OPTIONAL, (s) -> new PropertyGradient(s, GRADIENTDEFAULT));

    public PropertyGradientFlowComponent(Supplier<Area> getdisplayarea, String type) {
        super(getdisplayarea, type);
        this.addConfig(gradient);
    }

    public Gradient getGradient() {
        return gradient.get("PropertyGradientFlowComponent gradient");
    }
}
