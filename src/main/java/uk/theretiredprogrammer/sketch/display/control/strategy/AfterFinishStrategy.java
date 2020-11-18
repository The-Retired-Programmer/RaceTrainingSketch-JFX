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

import uk.theretiredprogrammer.sketch.core.control.IllegalStateFailure;
import uk.theretiredprogrammer.sketch.core.entity.PropertyDistanceVector;
import uk.theretiredprogrammer.sketch.display.entity.course.CurrentLeg;
import uk.theretiredprogrammer.sketch.display.entity.boats.Boat;
import uk.theretiredprogrammer.sketch.display.entity.flows.WaterFlow;
import uk.theretiredprogrammer.sketch.display.entity.flows.WindFlow;
import uk.theretiredprogrammer.sketch.display.entity.base.SketchModel;

class AfterFinishStrategy extends Strategy {

    AfterFinishStrategy(Boat boat, CurrentLeg previousleg) {
        super(previousleg);
    }

    @Override
    String strategyTimeInterval(Boat boat, Decision decision, CurrentLeg leg, SketchModel sketchproperty, WindFlow windflow, WaterFlow waterflow) {
        double fromfinishmark = boat.getLocation().to(leg.getEndLocation());
        if (fromfinishmark > boat.metrics.getLength() * 5) {
            decision.setSTOP(boat.getDirection());
            return "Stopping at end of course";
        } else {
            decision.setSAILON(boat.getDirection());
            return "Sail ON";
        }
    }

    @Override
    PropertyDistanceVector getOffsetVector(boolean onPort) {
        throw new IllegalStateFailure("attempting to getOffsetVector on the afterFinish leg");
    }

}
