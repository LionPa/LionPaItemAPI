package io.lionpa.lionpaitemapi.speedium;

import io.lionpa.speedium.compiler.CodeBuilder;
import io.lionpa.speedium.util.BlockType;
import io.lionpa.speedium.util.Log;
import io.lionpa.speedium.util.yml.YamlWrapper;

public class ItemBlockType extends BlockType {

    public ItemBlockType() {
        super("item");
    }

    @Override
    public void run(CodeBuilder codeBuilder, String s, YamlWrapper yamlWrapper) {
        Log.error("ItemBlockType is not supported yet");
    }
}
