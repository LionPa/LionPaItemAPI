package io.lionpa.lionpaitemapi.item.events;

import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class ItemSwap extends ItemApiEvent {

    private boolean canceled;

    private final EquipmentSlot newHand;

    public ItemSwap(Player player, ItemStack itemStack, EquipmentSlot newHand) {
        super(player, itemStack);
        canceled = false;

        this.newHand = newHand;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public EquipmentSlot getNewHand() {
        return newHand;
    }
}
