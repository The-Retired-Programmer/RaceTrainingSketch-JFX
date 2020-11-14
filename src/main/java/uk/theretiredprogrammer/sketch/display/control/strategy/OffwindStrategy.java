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

import uk.theretiredprogrammer.sketch.display.entity.course.Leg;
import java.util.Optional;
import uk.theretiredprogrammer.sketch.display.entity.boats.Boat;
import uk.theretiredprogrammer.sketch.core.entity.PropertyDegrees;
import uk.theretiredprogrammer.sketch.core.control.IllegalStateFailure;
import uk.theretiredprogrammer.sketch.display.entity.flows.WaterFlow;
import uk.theretiredprogrammer.sketch.display.entity.flows.WindFlow;
import uk.theretiredprogrammer.sketch.display.entity.base.SketchModel;

public class OffwindStrategy extends Strategy {

    private final OffwindSailingDecisions decisions;
    private final RoundingDecisions roundingdecisions;
    private boolean useroundingdecisions = false;

    public OffwindStrategy(Boat boat, Leg leg, WindFlow windflow, WaterFlow waterflow) {
        super(boat, leg, leg.getAngleofLeg().plus(90), leg.getAngleofLeg().sub(90));
        decisions = new OffwindSailingDecisions();
        LegType followinglegtype = getLegType(boat, leg.getFollowingLeg(), windflow);
        switch (followinglegtype) {
            case WINDWARD ->
                roundingdecisions = leg.isPortRounding()
                        ? new OffwindPortRoundingDecisions((windangle) -> boat.getPortCloseHauledCourse(windangle))
                        : new OffwindStarboardRoundingDecisions((windangle) -> boat.getStarboardCloseHauledCourse(windangle));
            case OFFWIND ->
                roundingdecisions = leg.isPortRounding()
                        ? new OffwindPortRoundingDecisions((windangle) -> leg.getFollowingLeg().getAngleofLeg())
                        : new OffwindStarboardRoundingDecisions((windangle) -> leg.getFollowingLeg().getAngleofLeg());
            case GYBINGDOWNWIND ->
                roundingdecisions = leg.isPortRounding()
                        ? new OffwindPortRoundingDecisions((windangle) -> boat.getPortReachingCourse(windangle))
                        : new OffwindStarboardRoundingDecisions((windangle) -> boat.getStarboardReachingCourse(windangle));
            case NONE ->
                roundingdecisions = leg.isPortRounding()
                        ? new OffwindPortRoundingDecisions((windangle) -> windangle.plus(90))
                        : new OffwindStarboardRoundingDecisions((windangle) -> windangle.sub(90));
            default ->
                throw new IllegalStateFailure("Illegal/unknown/Unsupported LEGTYPE combination: Offwind to "
                        + followinglegtype.toString());
        }
    }

    public OffwindStrategy(OffwindStrategy clonefrom, Boat newboat) {
        super(clonefrom, newboat);
        this.decisions = clonefrom.decisions;
        this.roundingdecisions = clonefrom.roundingdecisions;
        this.useroundingdecisions = clonefrom.useroundingdecisions;
    }

    @Override
    String nextBoatStrategyTimeInterval(SketchModel sketchproperty, WindFlow windflow, WaterFlow waterflow) {
        PropertyDegrees markMeanwinddirection = leg.endLegMeanwinddirection(windflow);
        if (useroundingdecisions) {
            return roundingdecisions.nextTimeInterval(sketchproperty, this, windflow, waterflow);
        }
        if (isNear2Mark(boat, markMeanwinddirection)) {
            useroundingdecisions = true;
            return roundingdecisions.nextTimeInterval(sketchproperty, this, windflow, waterflow);
        }
        return decisions.nextTimeInterval(sketchproperty, this, windflow, waterflow);
    }

    boolean isNear2Mark(Boat boat, PropertyDegrees markMeanwinddirection) {
        Optional<Double> refdistance = getRefDistance(boat.getLocation(), leg.getEndLocation(), markMeanwinddirection.sub(180));
        return refdistance.isPresent() ? refdistance.get() <= boat.metrics.getWidth() * 20 : true;
    }
}
