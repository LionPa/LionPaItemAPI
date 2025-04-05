package io.lionpa.lionpaitemapi;


import io.lionpa.lionpaitemapi.item.ItemConstructor;
import io.lionpa.lionpaitemapi.item.ItemInfo;
import io.lionpa.lionpaitemapi.item.ItemVariant;
import io.lionpa.lionpaitemapi.item.ItemVariantBuilder;
import io.lionpa.lionpaitemapi.item.events.ItemBreakBlock;
import io.lionpa.lionpaitemapi.item.events.ItemSwap;
import io.lionpa.lionpaitemapi.item.events.ItemUse;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.WindCharge;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.Random;

@ItemInfo(id = "test_item")
public class TestItem implements ItemConstructor {

    @ItemVariant()
    public static ItemVariantBuilder testItem() {
        return new ItemVariantBuilder()
                .type(Material.PAPER)
                .display("Тестовый предмет")
                .customModelData(1)
                .field("fuel", PersistentDataType.INTEGER, 100)
                .field("max_fuel", PersistentDataType.INTEGER, 100)
                .field("fuel_speed", PersistentDataType.INTEGER, 3);
    }

    @ItemVariant(variantName = "second_variant")
    public static ItemVariantBuilder testItemSecondVariant() {
        return new ItemVariantBuilder()
                .type(Material.NETHERITE_HOE)
                .display("Типа пушка")
                .customModelData(2)
                .damage(100)
                .field("fuel", PersistentDataType.INTEGER, 150)
                .field("max_fuel", PersistentDataType.INTEGER, 150)
                .field("fuel_speed", PersistentDataType.INTEGER, 5);
    }
    @ItemVariant(variantName = "choco_gun")
    public static ItemVariantBuilder chocoGun(){
        return new ItemVariantBuilder()
                .type(Material.DIAMOND)
                .display("Шоколадный пистолет")
                .customModelData(3)

                .food()
                .eatEffect(new PotionEffect(PotionEffectType.SPEED,20 * 20, 1, false,false,false))
                .eatSeconds(0.00000001f)
                .saturation(10)
                .nutrition(10)

                .tool()
                .toolMiningSpeed(0)
                .toolRule(Tag.DIAMOND_ORES,500,true)
                .damage(0.01f)

                .stack(99)

                .enchantment(Enchantment.KNOCKBACK, 1000, true)
                //.attribute(Attribute.GENERIC_SCALE,10)
                .attribute(Attribute.GENERIC_STEP_HEIGHT,1000)
                .attribute(Attribute.GENERIC_MAX_HEALTH,10000000)
                .attribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE,-10);
    }

    @ItemVariant(variantName = "shovel")
    public static ItemVariantBuilder shovel(){
        return new ItemVariantBuilder()
                .type(Material.PAPER)
                .customModelData(1)
                .display(ChatColor.RESET + "Лопата")
                .lore(ChatColor.RESET + "Копает 3x3")
                .stack(1)

                .tool()
                .toolMiningSpeed(1)
                .toolRule(Tag.MINEABLE_SHOVEL, 3, true)
                .damage(4);
    }
    @ItemVariant(variantName = "knockStick")
    public static ItemVariantBuilder stick(){
        return new ItemVariantBuilder()
                .type(Material.WOODEN_SWORD)
                .display("Палочка")
                .damage(0.1f)
                .enchantment(Enchantment.KNOCKBACK,5,true)
                .enchantment(Enchantment.SWEEPING_EDGE,5,true)
                .attribute(Attribute.GENERIC_MOVEMENT_SPEED,0.15f)
                .flag(ItemFlag.HIDE_UNBREAKABLE);
    }




    public void breakBlockWithItem(ItemBreakBlock breakBlock) {
        if (!breakBlock.getVariant().equals("shovel")) return;

        Location blockLoc = breakBlock.getBreakedBlock().getLocation().add(new Vector(-1,-1,-1));
        for (int x = 0; x < 3; x++)
            for (int y = 0; y < 3; y++)
                for (int z = 0; z < 3; z++) {
                    Block block = blockLoc.clone().add(x,y,z).getBlock();
                }
    }

    public void use(ItemUse use) {
        if (!use.getAction().isRightClick()) return;
        if (use.getVariant().equals("knockStick")) {
            Random r = new Random();
            for (int i = 0; i < 10; i++) {
                Location location = use.getPlayer().getEyeLocation().add(new Vector(r.nextFloat()-0.5,r.nextFloat()-0.5,r.nextFloat()-0.5));
                WindCharge charge = location.getWorld().spawn(location, WindCharge.class);
                charge.setShooter(use.getPlayer());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (charge.isDead()) return;
                        TNTPrimed tnt = charge.getWorld().spawn(charge.getLocation(), TNTPrimed.class);
                        tnt.setFuseTicks(0);
                        charge.remove();
                    }
                }.runTaskLater(LionPaItemAPI.getPlugin(),40);
            }
            return;
        }

        if (use.getVariant().equals("choco_gun")) return;
        if (use.getVariant().equals("shovel")) return;

        use.setCanceled(true);

        Player player = use.getPlayer();

        int fuel = use.getField("fuel", PersistentDataType.INTEGER);

        if (fuel > 0) {
            use.setField("fuel", PersistentDataType.INTEGER, fuel - 1);

            Location location = player.getEyeLocation().add(0,-0.3,0);
            RayTraceResult result = player.rayTraceBlocks(20, FluidCollisionMode.NEVER);

            if (result != null){
                if (result.getHitBlock() != null) {
                    result.getHitBlock().breakNaturally();
                }
            }

            for (int i = 0; i < 100; i++){
                location.getWorld().spawnParticle(Particle.FLAME,location,1,0.01,0.01,0.01,0);
                location.add(location.getDirection().multiply(0.15));
            }
        } else {
            player.sendMessage("Нету топлива");
        }

    }



    // Не работает
    public void swap(ItemSwap swap) {
        int maxFuel = swap.getField("max_fuel", PersistentDataType.INTEGER);
        swap.getPlayer().sendMessage(maxFuel+"");

        swap.setField("fuel", PersistentDataType.INTEGER, maxFuel);
        //swap.setCanceled(true);
    }
}
