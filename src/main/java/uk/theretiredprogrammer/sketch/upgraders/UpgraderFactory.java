/*
 * Copyright 2020 richard.
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
package uk.theretiredprogrammer.sketch.upgraders;

import java.io.IOException;

/**
 *
 * @author richard
 */
public class UpgraderFactory {

    public static Upgrader createUpgrader(int version) throws IOException {
        switch (version) {
            case 0 -> {
                return new Upgrader0();
            }
            default ->
                throw new IOException("no upgrader available: " + version + " to " + version + 1);
        }
    }
}