package com.dfsek.terra.carving;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.biome.grid.TerraBiomeGrid;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.generation.GenerationPhase;
import org.polydev.gaea.math.MathUtil;
import org.polydev.gaea.population.ChunkCoordinate;
import org.polydev.gaea.util.FastRandom;
import org.polydev.gaea.util.GlueList;
import org.polydev.gaea.world.carving.Worm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CarverCache {
    private final Map<ChunkCoordinate, List<Worm.WormPoint>> carvers = new HashMap<>();

    public List<Worm.WormPoint> getPoints(int chunkX, int chunkZ, World w, ChunkCoordinate look, UserDefinedCarver carver) {

        ChunkCoordinate req = new ChunkCoordinate(chunkX, chunkZ, w.getUID());

        return carvers.computeIfAbsent(req, key -> {
            TerraBiomeGrid grid = TerraWorld.getWorld(w).getGrid();
            long seed = MathUtil.getCarverChunkSeed(chunkX, chunkZ, w.getSeed());
            carver.getSeedVar().setValue(seed);
            Random r = new FastRandom(seed);
            Worm carving = carver.getWorm(seed, new Vector((chunkX << 4) + r.nextInt(16), carver.getConfig().getHeight().get(r), (chunkZ << 4) + r.nextInt(16)));
            Vector origin = carving.getOrigin();
            List<Worm.WormPoint> points = new GlueList<>();
            for(int i = 0; i < carving.getLength(); i++) {
                carving.step();
                Biome biome = grid.getBiome(carving.getRunning().toLocation(w), GenerationPhase.POPULATE);
                if(!((UserDefinedBiome) biome).getConfig().getCarvers().containsKey(carver)) { // Stop if we enter a biome this carver is not present in
                    return new GlueList<>();
                }
                points.add(carving.getPoint());
            }

            return points;
        });
    }
}