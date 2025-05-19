package io.lionpa.lionpaitemapi.item.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

//FIXME
public class ItemSwap extends ItemApiEvent implements Cancellable {

    private boolean cancelled;

    private final EquipmentSlot newHand;

    public ItemSwap(Player player, ItemStack itemStack, EquipmentSlot newHand) {
        super(player, itemStack);
        cancelled = false;

        this.newHand = newHand;
    }


    public EquipmentSlot getNewHand() {
        return newHand;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
