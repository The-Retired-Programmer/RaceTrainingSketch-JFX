/*
 * Copyright 2020 richard linsdale.
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
package uk.theretiredprogrammer.sketch.display.control;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import uk.theretiredprogrammer.sketch.core.control.ExecuteAndCatch;
import uk.theretiredprogrammer.sketch.decisionslog.control.DecisionController;
import uk.theretiredprogrammer.sketch.display.entity.base.SketchModel;

/**
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class SimulationController implements Runnable {

    private int simulationtime = 0;
    private boolean isRunning = false;
    private final ScheduledExecutorService scheduler;
    private ScheduledFuture<?> ticker;
    private final DisplayController controller;

    public SimulationController(DisplayController controller) {
        this.controller = controller;
        scheduler = Executors.newScheduledThreadPool(1);
    }

    public void close() {
        stop();
        scheduler.shutdown();
    }

    public void start() {
        SketchModel model = controller.getModel();
        if (isRunning) {
            return;
        }
        int rate = (int) (model.getDisplay().getSecondsperdisplay() * 1000 / model.getDisplay().getSpeedup());
        ticker = scheduler.scheduleAtFixedRate(this, rate, rate, TimeUnit.MILLISECONDS);
        isRunning = true;
    }

    public void stop() {
        if (!isRunning) {
            return;
        }
        isRunning = false;
        ticker.cancel(false);
    }

    @Override
    public void run() {
        new ExecuteAndCatch().setExceptionHandler(() -> stop())
                .run(() -> {
                    SketchModel model = controller.getModel();
                    int secondsperdisplay = model.getDisplay().getSecondsperdisplay();
                    while (secondsperdisplay > 0) {
                        DecisionController decisioncontroller = controller.getDecisionController();
                        decisioncontroller.setTime(simulationtime);
                        model.getWindFlow().timerAdvance(simulationtime, decisioncontroller);
                        model.getWaterFlow().timerAdvance(simulationtime, decisioncontroller);
                        model.getBoats().timerAdvance(model, simulationtime, decisioncontroller);
                        secondsperdisplay--;
                        simulationtime++;
                    }
                    controller.updateTimeField(simulationtime);
                    ExecuteAndCatch.runLater(() -> controller.repaint());
                });
    }
}
