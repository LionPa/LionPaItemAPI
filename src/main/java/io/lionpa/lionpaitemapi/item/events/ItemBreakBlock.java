package io.lionpa.lionpaitemapi.item.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemBreakBlock extends ItemApiEvent{

    private boolean canceled;

    private final Block breakedBlock;

    public ItemBreakBlock(Player player, ItemStack itemStack, Block breakedBlock) {
        super(player, itemStack);
        this.breakedBlock = breakedBlock;
        canceled = false;
    }

    public Block getBreakedBlock() {
        return breakedBlock;
    }


    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

}
