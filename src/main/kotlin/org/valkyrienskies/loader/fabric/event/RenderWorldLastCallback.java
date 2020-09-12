package org.valkyrienskies.loader.fabric.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

@FunctionalInterface
public interface RenderWorldLastCallback {

    Event<RenderWorldLastCallback> EVENT = EventFactory.createArrayBacked(RenderWorldLastCallback.class,
        (listeners) -> () -> {
            for (RenderWorldLastCallback listener : listeners) {
                listener.onRenderWorld();
            }
        });

    void onRenderWorld();
}
