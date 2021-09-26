package com.dfsek.terra.addons.terrascript.script.builders;

import java.util.List;

import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.StructureScript;
import com.dfsek.terra.addons.terrascript.script.functions.LootFunction;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.structure.LootTable;


public class LootFunctionBuilder implements FunctionBuilder<LootFunction> {
    private final Platform main;
    private final Registry<LootTable> registry;
    private final StructureScript script;
    
    public LootFunctionBuilder(Platform main, Registry<LootTable> registry, StructureScript script) {
        this.main = main;
        this.registry = registry;
        this.script = script;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public LootFunction build(List<Returnable<?>> argumentList, Position position) {
        return new LootFunction(registry, (Returnable<Number>) argumentList.get(0), (Returnable<Number>) argumentList.get(1),
                                (Returnable<Number>) argumentList.get(2), (Returnable<String>) argumentList.get(3), main, position, script);
    }
    
    @Override
    public int argNumber() {
        return 4;
    }
    
    @Override
    public Returnable.ReturnType getArgument(int position) {
        switch(position) {
            case 0:
            case 1:
            case 2:
                return Returnable.ReturnType.NUMBER;
            case 3:
                return Returnable.ReturnType.STRING;
            default:
                return null;
        }
    }
}
