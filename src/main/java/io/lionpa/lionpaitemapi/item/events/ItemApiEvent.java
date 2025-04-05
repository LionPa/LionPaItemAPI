package io.lionpa.lionpaitemapi.item.events;

import io.lionpa.lionpaitemapi.LionPaItemAPI;
import io.lionpa.lionpaitemapi.NamespacedKeys;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

public class ItemApiEvent {

    private final Player player;

    private final ItemStack itemStack;

    private final String itemId;
    private final String variant;

    public ItemApiEvent(Player player, ItemStack itemStack) {
        this.player = player;
        this.itemStack = itemStack;

        PersistentDataContainer data = itemStack.getItemMeta().getPersistentDataContainer();

        itemId = data.get(NamespacedKeys.ITEM_ID_KEY, PersistentDataType.STRING);
        variant = data.get(NamespacedKeys.ITEM_VARIANT_ID_KEY, PersistentDataType.STRING);
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getId() {
        return itemId;
    }

    public String getVariant() {
        return variant;
    }

    private static final HashMap<String, NamespacedKey> hashedKeys = new HashMap<>();

    public <T> T getField(String id, PersistentDataType<T,T> type){
        return itemStack.getPersistentDataContainer().get(getKey(id), type);
    }

    public NamespacedKey getKey(String fieldId){
        NamespacedKey key;
        String s = LionPaItemAPI.getPlugin().getName().toLowerCase()+"."+itemId+":"+fieldId;
        if (hashedKeys.containsKey(s)){
            key = hashedKeys.get(s);
        } else {
            key = new NamespacedKey(LionPaItemAPI.getPlugin().getName().toLowerCase()+"."+itemId,fieldId);
            hashedKeys.put(s,key);
        }
        return key;
    }

    public <T> void setField(String id, PersistentDataType<T,T> type, T value){
        ItemMeta meta = itemStack.getItemMeta();
        NamespacedKey key = new NamespacedKey(LionPaItemAPI.getPlugin().getName().toLowerCase()+"."+itemId,id);
        meta.getPersistentDataContainer().set(key, type, value);
        itemStack.setItemMeta(meta);

    }
}
