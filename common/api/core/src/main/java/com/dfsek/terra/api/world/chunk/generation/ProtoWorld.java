package com.dfsek.terra.api.world.chunk.generation;

import com.dfsek.terra.api.world.access.World;
import com.dfsek.terra.api.world.access.WorldAccess;


public interface ProtoWorld extends WorldAccess {
    int centerChunkX();
    
    int centerChunkZ();
    
    /**
     * Get the world object this ProtoWorld represents
     *
     * <b>Do not read from/write to this world!</b>
     * @return The world
     */
    World getWorld();
}
