package io.lionpa.lionpaitemapi.item.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class ItemUse extends ItemApiEvent {

    private boolean canceled;

    private final Action action;
    private final Block interactedBlock;
    private final EquipmentSlot hand;

    public ItemUse(Player player, ItemStack itemStack, Action action, Block interactedBlock, EquipmentSlot hand) {
        super(player, itemStack);
        this.action = action;
        this.interactedBlock = interactedBlock;
        canceled = false;
        this.hand = hand;
    }

    public Action getAction() {
        return action;
    }

    public Block getInteractedBlock() {
        return interactedBlock;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public EquipmentSlot getHand() {
        return hand;
    }
}
