package org.firstinspires.ftc.teamcode.layer;

import java.util.List;

public final class BlockingMultiplexLayer extends MultiplexLayer {
    /**
     * Constructs a BlockingMultiplexLayer.
     *
     * @param layers - the layers this BlockingMultiplexLayer will contain.
     */
    public BlockingMultiplexLayer(List<Layer> layers) {
        super(layers);
    }

    @Override
    protected final boolean isBlocking() {
        return true;
    }
}
