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

import uk.theretiredprogrammer.sketch.core.entity.PropertyDegrees;
import static uk.theretiredprogrammer.sketch.display.control.strategy.Decision.PORT;
import static uk.theretiredprogrammer.sketch.display.control.strategy.Decision.STARBOARD;
import uk.theretiredprogrammer.sketch.display.entity.flows.WaterFlow;
import uk.theretiredprogrammer.sketch.display.entity.flows.WindFlow;
import uk.theretiredprogrammer.sketch.display.entity.base.SketchModel;

class WindwardPortSailingDecisions extends SailingDecisions {

    @Override
    String nextTimeInterval(SketchModel sketchproperty, Strategy strategy, WindFlow windflow, WaterFlow waterflow) {
        PropertyDegrees winddirection = windflow.getFlow(strategy.boat.getLocation()).getDegreesProperty();
        PropertyDegrees meanwinddirection = windflow.getMeanFlowAngle();
        PropertyDegrees boatangletowind = strategy.boat.getDirection().absDegreesDiff(winddirection);
        if (tackifonstarboardlayline(strategy, winddirection)) {
            return "tacking on starboard layline - port->starboard";
        }
        if (adjustPortDirectCourseToWindwardMarkOffset(strategy, winddirection)) {
            return "Beating on Port Layline to windward mark - course adjustment";
        }
        // stay in channel
        if (strategy.boat.upwindchannel != null) {
            if (strategy.getDistanceToMark(strategy.boat.getLocation()) > strategy.boat.upwindchannel.getInneroffset(strategy.getMarkLocation()) * 1.5) {
                if (!strategy.boat.upwindchannel.isInchannel(strategy.boat.getLocation())) {
                    strategy.decision.setTURN(strategy.boat.getStarboardCloseHauledCourse(winddirection), PORT);
                    return "Tacking onto starboard to stay within channel";
                }
            }
        }
        // check if need to tack onto best tack
        if (strategy.boat.isUpwindsailonbesttack()) {
            if (winddirection.gt(meanwinddirection)) {
                strategy.decision.setTURN(strategy.boat.getStarboardCloseHauledCourse(winddirection), PORT);
                return "Tack onto best tack - starboard";
            }
        }
        // check if pointing high
        if (boatangletowind.lt(strategy.boat.metrics.upwindrelative)) {
            if (strategy.boat.isUpwindtackifheaded()) {
                strategy.decision.setTURN(strategy.boat.getStarboardCloseHauledCourse(winddirection), PORT);
                return "Tack onto starboard when headed";
            }
            if (strategy.boat.isUpwindbearawayifheaded()) {
                strategy.decision.setTURN(strategy.boat.getPortCloseHauledCourse(winddirection), STARBOARD);
                return "Bearaway when headed";
            }
        }
        // check if pointing low
        if (boatangletowind.gt(strategy.boat.metrics.upwindrelative)) {
            if (strategy.boat.isUpwindluffupiflifted()) {
                strategy.decision.setTURN(strategy.boat.getPortCloseHauledCourse(winddirection), PORT);
                return "Luff when lifted";
            }
        }
        return "Sail ON";
    }
}
