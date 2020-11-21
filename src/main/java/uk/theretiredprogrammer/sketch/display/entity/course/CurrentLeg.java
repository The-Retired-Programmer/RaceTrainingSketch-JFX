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
package uk.theretiredprogrammer.sketch.display.entity.course;

import java.util.Optional;
import uk.theretiredprogrammer.sketch.core.control.IllegalStateFailure;
import uk.theretiredprogrammer.sketch.core.entity.PropertyDegrees;
import uk.theretiredprogrammer.sketch.core.entity.PropertyDistanceVector;
import uk.theretiredprogrammer.sketch.core.entity.PropertyLocation;
import uk.theretiredprogrammer.sketch.decisionslog.control.DecisionController;
import uk.theretiredprogrammer.sketch.decisionslog.entity.BoatLogEntry;
import uk.theretiredprogrammer.sketch.decisionslog.entity.DecisionLogEntry;
import uk.theretiredprogrammer.sketch.decisionslog.entity.ReasonLogEntry;
import uk.theretiredprogrammer.sketch.display.entity.boats.Boat;
import uk.theretiredprogrammer.sketch.display.entity.boats.BoatMetrics;
import static uk.theretiredprogrammer.sketch.display.entity.course.CurrentLeg.LegType.GYBINGDOWNWIND;
import static uk.theretiredprogrammer.sketch.display.entity.course.CurrentLeg.LegType.NONE;
import static uk.theretiredprogrammer.sketch.display.entity.course.CurrentLeg.LegType.OFFWIND;
import static uk.theretiredprogrammer.sketch.display.entity.course.CurrentLeg.LegType.WINDWARD;
import static uk.theretiredprogrammer.sketch.display.entity.course.Decision.DecisionAction.SAILON;
import uk.theretiredprogrammer.sketch.display.entity.flows.WindFlow;

public class CurrentLeg {

    public static enum LegType {
        WINDWARD, OFFWIND, GYBINGDOWNWIND, NONE
    }

    public static Optional<Double> getRefDistance(PropertyLocation location, PropertyLocation marklocation, PropertyDegrees refangle) {
        return getRefDistance(location, marklocation, refangle.get());
    }

    public static Optional<Double> getRefDistance(PropertyLocation location, PropertyLocation marklocation, double refangle) {
        PropertyDistanceVector tomark = new PropertyDistanceVector(location, marklocation);
        PropertyDegrees refangle2mark = tomark.getDegreesProperty().absDegreesDiff(refangle);
        if (refangle2mark.gt(90)) {
            return Optional.empty();
        }
        return Optional.of(refdistancetomark(tomark.getDistance(), refangle2mark));
    }

    public static LegType getLegType(BoatMetrics metrics, PropertyDegrees legangle, WindFlow windflow, boolean reachesdownwind) {
        if (legangle == null) {
            return NONE;
        }
        PropertyDegrees legtowind = legangle.absDegreesDiff(windflow.getMeanFlowAngle());
        if (legtowind.lteq(metrics.upwindrelative)) {
            return WINDWARD;
        }
        if (reachesdownwind && legtowind.gteq(metrics.downwindrelative)) {
            return GYBINGDOWNWIND;
        }
        return OFFWIND;
    }

    private static PropertyDegrees refangletomark(PropertyDegrees tomarkangle, PropertyDegrees refangle) {
        return tomarkangle.absDegreesDiff(refangle);
    }

    private static double refdistancetomark(double distancetomark, PropertyDegrees refangle2mark) {
        return distancetomark * Math.cos(refangle2mark.getRadians());
    }

    public static Strategy get(Strategy clonefrom) {
        return new Strategy(clonefrom);
    }

    public static Strategy get(Params params, CurrentLeg leg) {
        Strategy strategy = new Strategy();
        LegType legtype = getLegType(params.boat.metrics, leg.getAngleofLeg(), params.windflow, params.boat.isReachdownwind());
        switch (legtype) {
            case WINDWARD ->
                strategy.setWindwardStrategy(params);
            case OFFWIND ->
                strategy.setOffwindStrategy(params);
            case GYBINGDOWNWIND ->
                strategy.setGybingDownwindStrategy(params);
            default ->
                throw new IllegalStateFailure("Illegal/unknown LEGTYPE: " + legtype.toString());
        }
        return strategy;
    }

    private int legno = 0;

    private PropertyLeg currentleg;
    private final Course course;
    private Strategy strategy;
    public final Decision decision;

    public CurrentLeg(Course course) {
        this(course, 0);
    }

    public CurrentLeg(CurrentLeg clonefrom) {
        this(clonefrom.course, clonefrom.legno);
    }

    private CurrentLeg(Course course, int legno) {
        this.decision = new Decision();
        this.course = course;
        this.legno = legno;
        currentleg = course.getLeg(legno);
        course.setOnChange(() -> refresh());
    }

    private void refresh() {
        currentleg = course.getLeg(legno);
    }

    public Decision getDecision() {
        return decision;
    }

    public Strategy getStrategy(Params params) {
        if (strategy == null) {
            strategy = get(params, this);
        }
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public boolean isFollowingLeg() {
        return course.getLegsProperty().size() > legno + 1;
    }

    public CurrentLeg toFollowingLeg() {
        if (isFollowingLeg()) {
            currentleg = course.getLeg(++legno);
        }
        return this;
    }

    public PropertyDegrees getAngleofFollowingLeg() {
        return isFollowingLeg()
                ? course.getLeg(legno + 1).getAngleofLeg()
                : null;
    }

    // proxies to current PropertyLeg
    public boolean isPortRounding() {
        return currentleg.isPortRounding();
    }

    public double getDistanceToMark(PropertyLocation here) {
        return currentleg.getDistanceToEnd(here);
    }

    public PropertyLocation getMarkLocation() {
        return currentleg.getEndLocation();
    }

    public PropertyDegrees getAngleofLeg() {
        return currentleg.getAngleofLeg();
    }

    public PropertyLocation getSailToLocation(boolean onPort) {
        return strategy.getOffsetVector(onPort)
                .toLocation(getMarkLocation());
    }

    public PropertyDegrees getAngletoSail(PropertyLocation here, boolean onPort) {
        return here.angleto(getSailToLocation(onPort));
    }

    public Strategy nextTimeInterval(Params params, int simulationtime, DecisionController timerlog) {
        if (decision.getAction() == SAILON) {
            getStrategy(params).strategyTimeInterval(params);
            timerlog.add(new BoatLogEntry(params.boat));
            timerlog.add(new DecisionLogEntry(params.boat.getName(), decision));
            timerlog.add(new ReasonLogEntry(params.boat.getName(), decision.getReason()));
        }
        if (params.boat.moveUsingDecision(params)) {
            return isFollowingLeg()
                    ? get(params, toFollowingLeg())
                    : getAfterFinishingStrategy(params);
        }
        return null;
    }

    private Strategy getAfterFinishingStrategy(Params params) {
        Strategy newstrategy = new Strategy();
        newstrategy.setAfterFinishStrategy(params);
        return newstrategy;
    }

    public boolean isNear2WindwardMark(Boat boat, PropertyDegrees markMeanwinddirection) {
        Optional<Double> refdistance = getRefDistance(boat.getLocation(), getMarkLocation(), markMeanwinddirection.get());
        return refdistance.isPresent() ? refdistance.get() <= boat.metrics.getLength() * 5 : true;
    }

    public boolean isNear2LeewardMark(Boat boat, PropertyDegrees markMeanwinddirection) {
        Optional<Double> refdistance = getRefDistance(boat.getLocation(), getMarkLocation(), markMeanwinddirection.sub(180).get());
        return refdistance.isPresent() ? refdistance.get() <= boat.metrics.getLength() * 5 : true;
    }

}
