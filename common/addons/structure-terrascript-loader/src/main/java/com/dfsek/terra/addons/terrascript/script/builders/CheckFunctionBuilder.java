package com.dfsek.terra.addons.terrascript.script.builders;

import java.util.List;

import com.dfsek.terra.addons.terrascript.parser.exceptions.ParseException;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.functions.CheckFunction;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;
import com.dfsek.terra.api.Platform;


public class CheckFunctionBuilder implements FunctionBuilder<CheckFunction> {
    private final Platform main;
    
    public CheckFunctionBuilder(Platform main) {
        this.main = main;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public CheckFunction build(List<Returnable<?>> argumentList, Position position) throws ParseException {
        return new CheckFunction(main, (Returnable<Number>) argumentList.get(0), (Returnable<Number>) argumentList.get(1),
                                 (Returnable<Number>) argumentList.get(2), position);
    }
    
    @Override
    public int argNumber() {
        return 3;
    }
    
    @Override
    public Returnable.ReturnType getArgument(int position) {
        switch(position) {
            case 0:
            case 1:
            case 2:
                return Returnable.ReturnType.NUMBER;
            default:
                return null;
        }
    }
}
