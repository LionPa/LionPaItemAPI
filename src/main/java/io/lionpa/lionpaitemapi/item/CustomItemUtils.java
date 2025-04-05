package io.lionpa.lionpaitemapi.item;

import io.lionpa.lionpaitemapi.NamespacedKeys;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class CustomItemUtils {

    public static boolean is(ItemStack item, Class<? extends ItemConstructor> clazz){
        if (item == null) return false;
        if (!item.hasItemMeta()) return false;
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        if (!container.has(NamespacedKeys.ITEM_ID_KEY)) return false;

        String id = container.get(NamespacedKeys.ITEM_ID_KEY, PersistentDataType.STRING);

        return CustomItem.getItemByClass(clazz).getId().equals(id);
    }
}
