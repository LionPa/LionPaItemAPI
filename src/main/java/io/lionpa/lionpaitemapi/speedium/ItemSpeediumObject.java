package io.lionpa.lionpaitemapi.speedium;

import io.lionpa.speedium.compiler.CodeBuilder;
import io.lionpa.speedium.parts.SpeediumObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemSpeediumObject extends SpeediumObject {

    public static List<String> variants = new ArrayList<>();

    protected ItemSpeediumObject(String name) {
        super(name, new HashMap<>());
    }

    @Override
    public String compile(CodeBuilder codeBuilder) {

        StringBuilder builder = new StringBuilder();

        builder.append("@ItemInfo(id = \"{ID}\")".replace("{ID}", name));
        builder.append("public static class ").append(name).append("implements ItemConstructor {\n");

        for (String variant : variants) builder.append(variant);

        builder.append("}");
        return builder.toString();
    }
}
