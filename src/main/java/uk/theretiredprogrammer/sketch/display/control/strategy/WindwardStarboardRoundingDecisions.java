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
package uk.theretiredprogrammer.sketch.display.control.strategy;

import java.util.function.Function;
import uk.theretiredprogrammer.sketch.core.entity.PropertyDegrees;
import uk.theretiredprogrammer.sketch.display.entity.flows.WaterFlow;
import static uk.theretiredprogrammer.sketch.display.control.strategy.Decision.PORT;
import static uk.theretiredprogrammer.sketch.display.control.strategy.Decision.STARBOARD;
import uk.theretiredprogrammer.sketch.display.entity.flows.WindFlow;
import uk.theretiredprogrammer.sketch.display.entity.base.SketchModel;

class WindwardStarboardRoundingDecisions extends RoundingDecisions {

    private final Function<PropertyDegrees, PropertyDegrees> getDirectionAfterTurn;

    WindwardStarboardRoundingDecisions(Function<PropertyDegrees, PropertyDegrees> getDirectionAfterTurn) {
        this.getDirectionAfterTurn = getDirectionAfterTurn;
    }

    @Override
    final String nextTimeInterval(SketchModel sketchproperty, Strategy strategy, WindFlow windflow, WaterFlow waterflow) {
        PropertyDegrees winddirection = windflow.getFlow(strategy.boat.getLocation()).getDegreesProperty();
        if (!strategy.boat.isPort(winddirection)) {
            if (strategy.boat.isStarboardRear90Quadrant(strategy.getMarkLocation())) {
                strategy.decision.setTURN(strategy.boat.getPortCloseHauledCourse(winddirection), STARBOARD);
                return "pre markrounding action - tack to port - starboard tack - starboard rounding";
            }
            if (adjustStarboardDirectCourseToWindwardMarkOffset(strategy, winddirection)) {
                return "course adjustment - approaching mark - starboard tack - starboard rounding";
            }
            strategy.decision.setTURN(strategy.boat.getStarboardCloseHauledCourse(winddirection), PORT);
            return "course adjustment - bearing away to hold port c/h - port tack - port rounding";
        }
        if (atStarboardRoundingTurnPoint(strategy)) {
            return executeStarboardRounding(getDirectionAfterTurn, winddirection, strategy);
        }
        if (adjustPortDirectCourseToWindwardMarkOffset(strategy, winddirection)) {
            return "course adjustment - approaching mark - port tack - starboard rounding";
        }
        if (tackifonstarboardlayline(strategy, winddirection)) {
            return "tacking on starboard layline - port->starboard";
        }
        strategy.decision.setTURN(strategy.boat.getPortCloseHauledCourse(winddirection), PORT);
        return "course adjustment - bearing away to hold port c/h - port tack - starboard rounding";
    }
}
