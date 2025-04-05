package io.lionpa.lionpaitemapi.item;

import io.lionpa.lionpaitemapi.LionPaItemAPI;
import io.lionpa.lionpaitemapi.item.events.ItemBreakBlock;
import io.lionpa.lionpaitemapi.item.events.ItemSwap;
import io.lionpa.lionpaitemapi.item.events.ItemUse;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.lang.reflect.Method;
import java.util.HashMap;

public interface ItemConstructor extends Listener {

    default CustomItem register() {
        Bukkit.getPluginManager().registerEvents(this, LionPaItemAPI.getPlugin());

        ItemInfo itemInfo = this.getClass().getAnnotation(ItemInfo.class);
        String id = itemInfo.id();

        HashMap<String, ItemVariantBuilder> variants = new HashMap<>();

        for (Method method : this.getClass().getMethods()) {
            ItemVariant variant = method.getAnnotation(ItemVariant.class);
            if (variant == null) continue;

            try {
                ItemVariantBuilder builder = (ItemVariantBuilder) method.invoke(null);
                builder.setItemId(id);
                builder.setItemVariant(variant.variantName());
                variants.put(variant.variantName(), builder);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return new CustomItem(id, variants, this);
    }

    default void use(ItemUse use){}
    default void swap(ItemSwap swap){}
    default void breakBlockWithItem(ItemBreakBlock breakBlock){}

    default String getId(){
        return CustomItem.getItemByClass(this.getClass()).getId();
    }

    default CustomItem get(){
        return CustomItem.getItemByClass(this.getClass());
    }
}
