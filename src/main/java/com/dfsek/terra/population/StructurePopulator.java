package com.dfsek.terra.population;

import com.dfsek.terra.Terra;
import com.dfsek.terra.TerraProfiler;
import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.genconfig.BiomeConfig;
import com.dfsek.terra.config.genconfig.StructureConfig;
import com.dfsek.terra.structure.GaeaStructure;
import com.dfsek.terra.structure.StructureSpawn;
import com.dfsek.terra.structure.StructureSpawnRequirement;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.generation.GenerationPhase;
import org.polydev.gaea.population.GaeaBlockPopulator;
import org.polydev.gaea.profiler.ProfileFuture;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class StructurePopulator extends BlockPopulator {

    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        try(ProfileFuture ignored = TerraProfiler.fromWorld(world).measure("StructureTime")) {
            int cx = (chunk.getX() << 4);
            int cz = (chunk.getZ() << 4);
            UserDefinedBiome b = (UserDefinedBiome) TerraBiomeGrid.fromWorld(world).getBiome(cx+ 8, cz + 8, GenerationPhase.POPULATE);
            structure: for(StructureConfig conf : BiomeConfig.fromBiome(b).getStructures()) {
                GaeaStructure struc = conf.getStructure();
                Location spawn = conf.getSpawn().getNearestSpawn(cx + 8, cz + 8, world.getSeed()).toLocation(world);
                Random r2 = new Random(spawn.hashCode());
                main: for(int y = conf.getSearchStart().get(r2); y > 0; y--) {
                    if(y > conf.getBound().getMax() || y < conf.getBound().getMin()) continue structure;
                    spawn.setY(y);
                    for(StructureSpawnRequirement s : struc.getSpawns()) {
                        if(! s.isValidSpawn(spawn)) continue main;
                        if(!b.equals(TerraBiomeGrid.fromWorld(world).getBiome(spawn.clone().add(s.getX(), s.getY(), s.getZ()), GenerationPhase.POPULATE))) {
                            Bukkit.getLogger().info("PREVENTED invalid spawn at " + spawn);
                            continue structure;
                        }
                    }
                    double horizontal = struc.getStructureInfo().getMaxHorizontal();
                    Bukkit.getLogger().info("Valid spawn at " + spawn);
                    if(Math.abs((cx + 8) - spawn.getBlockX()) <= horizontal && Math.abs((cz + 8) - spawn.getBlockZ()) <= horizontal) {
                        try(ProfileFuture ignore = TerraProfiler.fromWorld(world).measure("StructurePasteTime")) {
                            struc.paste(spawn, chunk, GaeaStructure.Rotation.fromDegrees(r2.nextInt(4) * 90), Collections.emptyList());
                            break;
                        }
                    }
                }
            }
        }
    }
    public enum SearchType {
        UP, DOWN
    }
}