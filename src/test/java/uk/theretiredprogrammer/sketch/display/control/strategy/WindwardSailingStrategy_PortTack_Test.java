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

import uk.theretiredprogrammer.sketch.display.entity.course.Decision;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class WindwardSailingStrategy_PortTack_Test extends SailingStrategyTest {

    @Test
    public void test1() throws IOException {
        System.out.println("on port - sailon");
        Decision decision = makeDecision("/upwind-porttack-portrounding.json");
        assertSAILON(decision);
    }

    @Test
    public void test2() throws IOException {
        System.out.println("don't luff to closehauled (on port) - sailon");
        Decision decision = makeDecision("/upwind-porttack-portrounding.json",
                () -> setwindflow(4.0, 265));
        assertSAILON(decision);
    }

    @Test
    public void test3() throws IOException {
        System.out.println("luff to closehauled (on port) - turn");
        Decision decision = makeDecision("/upwind-porttack-portrounding.json",
                () -> setwindflow(4.0, 265),
                () -> setboattrue("upwindluffupiflifted"));
        assertTURN(decision, 310, false);
    }

    @Test
    public void test4() throws IOException {
        System.out.println("bearaway to closehauled (stay on port) - turn");
        Decision decision = makeDecision("/upwind-porttack-portrounding.json",
                () -> setwindflow(4.0, 275),
                () -> setboattrue("upwindbearawayifheaded"));
        assertTURN(decision, 320, true);
    }

    @Test
    public void test5() throws IOException {
        System.out.println("tack if headed (to starboard) - turn");
        Decision decision = makeDecision("/upwind-porttack-portrounding.json",
                () -> setwindflow(4.0, 275),
                () -> setboattrue("upwindtackifheaded"));
        assertTURN(decision, 230, false);
    }

    @Test
    public void test6() throws IOException {
        System.out.println("still best tack (port) - sailon");
        Decision decision = makeDecision("/upwind-porttack-portrounding.json",
                () -> setwindflow(4.0, 265),
                () -> setboattrue("upwindsailonbesttack", "upwindbearawayifheaded", "upwindluffupiflifted"),
                () -> setboatdirection(310));
        assertSAILON(decision);
    }

    @Test
    public void test7() throws IOException {
        System.out.println("still best tack (port), bearway to closehauled - turn");
        Decision decision = makeDecision("/upwind-porttack-portrounding.json",
                () -> setwindflow(4.0, 265),
                () -> setboattrue("upwindsailonbesttack", "upwindbearawayifheaded", "upwindluffupiflifted"),
                () -> setboatdirection(305));
        assertTURN(decision, 310, true);
    }

    @Test
    public void test8() throws IOException {
        System.out.println("still best tack (port), luff to closehauled - turn");
        Decision decision = makeDecision("/upwind-porttack-portrounding.json",
                () -> setwindflow(4.0, 265),
                () -> setboattrue("upwindsailonbesttack", "upwindbearawayifheaded", "upwindluffupiflifted"),
                () -> setboatdirection(315));
        assertTURN(decision, 310, false);
    }

    @Test
    public void test9() throws IOException {
        System.out.println("tack onto best tack (starboard), from closehauled - turn");
        Decision decision = makeDecision("/upwind-porttack-portrounding.json",
                () -> setwindflow(4.0, 275),
                () -> setboattrue("upwindsailonbesttack", "upwindbearawayifheaded", "upwindluffupiflifted"),
                () -> setboatdirection(320));
        assertTURN(decision, 230, false);
    }

    @Test
    public void test10() throws IOException {
        System.out.println("tack onto best tack (starboard), above closehauled - turn");
        Decision decision = makeDecision("/upwind-porttack-portrounding.json",
                () -> setwindflow(4.0, 275),
                () -> setboattrue("upwindsailonbesttack", "upwindbearawayifheaded", "upwindluffupiflifted"));
        assertTURN(decision, 230, false);
    }

    @Test
    public void test11() throws IOException {
        System.out.println("tack onto best tack (starboard), below closehauled - turn");
        Decision decision = makeDecision("/upwind-porttack-portrounding.json",
                () -> setwindflow(4.0, 275),
                () -> setboattrue("upwindsailonbesttack", "upwindbearawayifheaded", "upwindluffupiflifted"),
                () -> setboatdirection(315));
        assertTURN(decision, 230, false);
    }

    @Test
    public void test12() throws IOException {
        System.out.println("sail near to starboard corner - sailon");
        Decision decision = makeDecision("/upwind-porttack-portrounding.json",
                () -> setboatlocation(88, 52));
        assertSAILON(decision);
    }

    @Test
    public void test13() throws IOException {
        System.out.println("sail to starboard corner (on line of mark - sail on");
        Decision decision = makeDecision("/upwind-porttack-portrounding.json",
                () -> setboatlocation(90, 50));
        assertSAILON(decision);
    }

    @Test
    public void test14() throws IOException {
        System.out.println("sail beyond starboard corner - tack onto starboard - turn");
        Decision decision = makeDecision("/upwind-porttack-portrounding.json",
                () -> setboatlocation(92.2, 47.8));
        assertTURN(decision, 225, false);
    }

    @Test
    public void test15() throws IOException {
        System.out.println("sail beyond starboard corner - tack onto starboard - turn");
        Decision decision = makeDecision("/upwind-porttack-portrounding.json",
                () -> setboatlocation(94, 46));
        assertTURN(decision, 225, false);
    }
}
