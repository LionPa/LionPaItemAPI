package io.lionpa.lionpaitemapi.item.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

public class ItemEntityDamage extends ItemApiEvent implements Cancellable {

    private final Entity damagedEntity;
    private boolean cancelled;

    public ItemEntityDamage(Player player, ItemStack itemStack, Entity damagedEntity) {
        super(player, itemStack);
        this.damagedEntity = damagedEntity;
    }

    public Entity getDamagedEntity() {
        return damagedEntity;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
