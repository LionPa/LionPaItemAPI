package io.lionpa.lionpaitemapi.item.events;

import io.lionpa.lionpaitemapi.LionPaItemAPI;
import io.lionpa.lionpaitemapi.NamespacedKeys;
import io.lionpa.lionpaitemapi.item.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class EventsInvoker implements Listener {

    public void init(){
        Bukkit.getPluginManager().registerEvents(this, LionPaItemAPI.getPlugin());
    }

    @EventHandler
    public static void itemUse(PlayerInteractEvent e) {
        if (e.getItem() == null) return;
        if (!e.getItem().hasItemMeta()) return;
        if (!e.getItem().getItemMeta().getPersistentDataContainer().has(NamespacedKeys.ITEM_ID_KEY)) return;

        Player player = e.getPlayer();
        PersistentDataContainer data = e.getItem().getItemMeta().getPersistentDataContainer();

        String itemId = data.get(NamespacedKeys.ITEM_ID_KEY, PersistentDataType.STRING);

        CustomItem customItem = CustomItem.getItem(itemId);

        ItemUse use = new ItemUse(player, e.getItem(), e.getAction(), e.getClickedBlock(), e.getHand());
        customItem.getConstructor().use(use);

        if (use.isCanceled()) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    public static void swapHands(PlayerSwapHandItemsEvent e){
        ItemStack main = e.getMainHandItem();
        ItemStack off = e.getOffHandItem();

        boolean b1 = itemSwap(e.getPlayer(), main, EquipmentSlot.OFF_HAND);
        boolean b2 = itemSwap(e.getPlayer(), off, EquipmentSlot.HAND);

        boolean cancel = b1;

        if (!cancel){
            cancel = b2;
        }

        e.setCancelled(cancel);

    }

    private static boolean itemSwap(Player player, ItemStack item, EquipmentSlot newHand){
        if (item.getType() != Material.AIR){
            if (!item.hasItemMeta()) return false;
            if (!item.getItemMeta().getPersistentDataContainer().has(NamespacedKeys.ITEM_ID_KEY)) return false;

            PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();

            String itemId = data.get(NamespacedKeys.ITEM_ID_KEY, PersistentDataType.STRING);

            CustomItem customItem = CustomItem.getItem(itemId);

            ItemSwap swap = new ItemSwap(player, item, newHand);
            customItem.getConstructor().swap(swap);

            return swap.isCanceled();
        }
        return false;
    }

    @EventHandler
    public static void blockBreakWithItem(BlockBreakEvent e){
        Player player = e.getPlayer();
        ItemStack item = e.getPlayer().getEquipment().getItemInMainHand();

        if (customItemCheck(item)) return;

        PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();

        String itemId = data.get(NamespacedKeys.ITEM_ID_KEY, PersistentDataType.STRING);

        CustomItem customItem = CustomItem.getItem(itemId);


        ItemBreakBlock breakBlock = new ItemBreakBlock(player, item, e.getBlock());
        customItem.getConstructor().breakBlockWithItem(breakBlock);

        if (breakBlock.isCanceled()) e.setCancelled(true);
    }

    private static boolean customItemCheck(ItemStack item){
        if (item == null) return true;
        if (!item.hasItemMeta()) return true;
        if (!item.getItemMeta().getPersistentDataContainer().has(NamespacedKeys.ITEM_ID_KEY)) return true;

        return false;
    }
}
