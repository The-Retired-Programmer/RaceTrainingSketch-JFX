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
import static uk.theretiredprogrammer.sketch.display.control.strategy.Decision.PORT;
import static uk.theretiredprogrammer.sketch.display.control.strategy.Decision.STARBOARD;
import uk.theretiredprogrammer.sketch.display.entity.flows.WaterFlow;
import uk.theretiredprogrammer.sketch.display.entity.flows.WindFlow;
import uk.theretiredprogrammer.sketch.display.entity.base.SketchModel;
import uk.theretiredprogrammer.sketch.display.entity.boats.Boat;
import uk.theretiredprogrammer.sketch.display.entity.course.CurrentLeg;

class WindwardPortRoundingDecisions extends RoundingDecisions {

    private final Function<PropertyDegrees, PropertyDegrees> getDirectionAfterTurn;

    WindwardPortRoundingDecisions(Function<PropertyDegrees, PropertyDegrees> getDirectionAfterTurn) {
        this.getDirectionAfterTurn = getDirectionAfterTurn;
    }

    @Override
    final String nextTimeInterval(Boat boat, Decision decision, SketchModel sketchproperty, CurrentLeg leg, Strategy strategy, WindFlow windflow, WaterFlow waterflow) {
        PropertyDegrees winddirection = windflow.getFlow(boat.getLocation()).getDegreesProperty();
        if (boat.isPort(winddirection)) {
            if (boat.isPortRear90Quadrant(strategy.getMarkLocation())) {
                decision.setTURN(boat.getStarboardCloseHauledCourse(winddirection), PORT);
                return "pre markrounding action - tack to starboard - port tack - port rounding";
            }
            if (adjustPortDirectCourseToWindwardMarkOffset(boat, decision, strategy, winddirection)) {
                return "course adjustment - approaching mark - port tack - port rounding";
            }
            decision.setTURN(boat.getPortCloseHauledCourse(winddirection), STARBOARD);
            return "course adjustment - bearing away to hold port c/h - port tack - port rounding";
        } else {
            if (atPortRoundingTurnPoint(boat, strategy)) {
                return executePortRounding(boat, decision, getDirectionAfterTurn, winddirection, strategy);
            }
            if (adjustStarboardDirectCourseToWindwardMarkOffset(boat, decision, strategy, winddirection)) {
                return "course adjustment - approaching mark - starboard tack - port rounding";
            }
            if (tackifonportlayline(boat, decision, strategy, winddirection)) {
                return "tacking on port layline - starboard->port";
            }
            decision.setTURN(boat.getStarboardCloseHauledCourse(winddirection), PORT);
            return "course adjustment - bearing away to hold starboard c/h - starboard tack - port rounding";
        }
    }
}
