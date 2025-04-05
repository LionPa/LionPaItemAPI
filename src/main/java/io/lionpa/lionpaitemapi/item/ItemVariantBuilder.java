package io.lionpa.lionpaitemapi.item;

import io.lionpa.lionpaitemapi.LionPaItemAPI;
import io.lionpa.lionpaitemapi.NamespacedKeys;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.inventory.meta.components.ToolComponent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.checkerframework.common.value.qual.IntRange;
import org.jetbrains.annotations.Range;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ItemVariantBuilder {

    private String itemId;
    private String itemVariant;
    private Material type;
    private String stringDisplay;
    private Component componentDisplay;
    private int customModelData;
    private final ArrayList<FieldData> fields = new ArrayList<>();
    private float damage;
    private final ArrayList<ItemFlag> flags = new ArrayList<>();
    private final ArrayList<PersistentDefaultData<?>> persistentDefaultData = new ArrayList<>();

    // Food
    private boolean isFood;
    private float eatSeconds;
    private float saturation;
    private int nutrition;
    private ArrayList<EatPotionEffect> effects;

    // Tool
    private boolean isTool;
    private ArrayList<SingleToolRule> singleToolRules;
    private ArrayList<MultiToolRule> multiToolRules;
    private ArrayList<TagToolRule> tagToolRules;
    private float miningSpeed;

    private int maxStack = 64;

    private ArrayList<EnchantmentData> enchantments;

    private ArrayList<AttributeData> attributeData;

    private ArrayList<String> stringLore;
    private ArrayList<Component> componentLore;
    private int maxDamage = -1;
    private int damagee = -1;

    public ItemVariantBuilder type(Material type){
        this.type = type;
        return this;
    }

    public ItemVariantBuilder display(String display){
        stringDisplay = display.replace('&','§');
        return this;
    }
    public ItemVariantBuilder display(Component display){
        componentDisplay = display;
        return this;
    }

    public ItemVariantBuilder customModelData(@IntRange(from = 0) int customModelData){
        this.customModelData = customModelData;
        return this;
    }

    public ItemVariantBuilder field(String name, PersistentDataType type, Object defaultValue){
        fields.add(new FieldData(name,type,defaultValue));
        return this;
    }

    public ItemVariantBuilder damage(float damage){
        this.damage = damage;
        return this;
    }

    public ItemVariantBuilder flag(ItemFlag flag){
        flags.add(flag);
        return this;
    }

    // Food
    public ItemVariantBuilder food(){
        isFood = true;
        return this;
    }
    public ItemVariantBuilder eatSeconds(float seconds){
        this.eatSeconds = seconds;
        return this;
    }
    public ItemVariantBuilder saturation(float saturation){
        this.saturation = saturation;
        return this;
    }
    public ItemVariantBuilder nutrition(int nutrition){
        this.nutrition = nutrition;
        return this;
    }
    public ItemVariantBuilder eatEffect(PotionEffect effect, float chance){
        if (effects == null)
            effects = new ArrayList<>();
        effects.add(new EatPotionEffect(effect,chance));
        return this;
    }
    public ItemVariantBuilder eatEffect(PotionEffect effect){
        return eatEffect(effect,1);
    }

    // Tool
    public ItemVariantBuilder tool(){
        isTool = true;
        return this;
    }

    public ItemVariantBuilder toolRule(Material material, float speed, boolean drop){
        if (singleToolRules == null)
            singleToolRules = new ArrayList<>();
        singleToolRules.add(new SingleToolRule(material,speed,drop));
        return this;
    }
    public ItemVariantBuilder toolRule(List<Material> material, float speed, boolean drop){
        if (multiToolRules == null)
            multiToolRules = new ArrayList<>();
        multiToolRules.add(new MultiToolRule(material,speed,drop));
        return this;
    }
    public ItemVariantBuilder toolRule(Tag<Material> material, float speed, boolean drop){
        if (tagToolRules == null)
            tagToolRules = new ArrayList<>();
        tagToolRules.add(new TagToolRule(material,speed,drop));
        return this;
    }
    public ItemVariantBuilder toolMiningSpeed(float speed){
        this.miningSpeed = speed;
        return this;
    }
    public ItemVariantBuilder stack(@Range(from = 1, to = 99) int max){
        this.maxStack = max;
        return this;
    }

    public ItemVariantBuilder durability(int durability){
        this.maxDamage = durability;
        return this;
    }
    public ItemVariantBuilder defaultDurability(int durability){
        this.damagee = durability;
        return this;
    }

    public ItemVariantBuilder enchantment(Enchantment enchantment, int level, boolean b){
        if (enchantments == null)
            enchantments = new ArrayList<>();
        enchantments.add(new EnchantmentData(enchantment,level,b));
        return this;
    }

    public ItemVariantBuilder attribute(Attribute attribute, double amount, AttributeModifier.Operation operation){
        if (attributeData == null){
            attributeData = new ArrayList<>();
        }
        attributeData.add(new AttributeData(attribute,amount,operation));
        return this;
    }
    public ItemVariantBuilder attribute(Attribute attribute, double amount){
        return attribute(attribute,amount, AttributeModifier.Operation.ADD_NUMBER);
    }

    public ItemVariantBuilder lore(String line){
        if (stringLore == null){
            stringLore = new ArrayList<>();
        }
        stringLore.add(line.replace('&','§'));
        return this;
    }

    public ItemVariantBuilder lore(Component line){
        if (componentLore == null){
            componentLore = new ArrayList<>();
        }
        componentLore.add(line);
        return this;
    }

    public <T> ItemVariantBuilder data(NamespacedKey key, PersistentDataType<?,T> type, T data){
        persistentDefaultData.add(new PersistentDefaultData<>(key, type, data));
        return this;
    }


    public ItemStack build(){
        ItemStack item = new ItemStack(type);
        ItemMeta meta = item.getItemMeta();

        meta.setCustomModelData(customModelData);
        // Food
        if (isFood){
            FoodComponent foodComponent = meta.getFood();
            foodComponent.setCanAlwaysEat(true);
            foodComponent.setEatSeconds(eatSeconds);
            foodComponent.setNutrition(nutrition);
            foodComponent.setSaturation(saturation);

            for (EatPotionEffect e : effects){
                foodComponent.addEffect(e.effect,e.chance);
            }

            meta.setFood(foodComponent);
        }
        // Tool
        if (isTool) {
            ToolComponent toolComponent = meta.getTool();
            if (singleToolRules != null)
                for (SingleToolRule toolRule : singleToolRules) {
                    toolComponent.addRule(toolRule.material,toolRule.speed,toolRule.drop);
                }
            if (multiToolRules != null)
                for (MultiToolRule toolRule : multiToolRules) {
                    toolComponent.addRule(toolRule.material,toolRule.speed,toolRule.drop);
                }
            if (tagToolRules != null)
                for (TagToolRule toolRule : tagToolRules) {
                    toolComponent.addRule(toolRule.material,toolRule.speed,toolRule.drop);
                }

            toolComponent.setDefaultMiningSpeed(miningSpeed);

            meta.setTool(toolComponent);
        }

        meta.setMaxStackSize(maxStack);

        if (enchantments != null){
            for (EnchantmentData data : enchantments){
                meta.addEnchant(data.enchantment,data.level,data.b);
            }
        }

        //if (true){
        //    JukeboxPlayableComponent musicComponent = meta.getJukeboxPlayable();
        //    musicComponent.setSongKey(NamespacedKey.minecraft("pigstep"));
        //    musicComponent.setShowInTooltip(true);
        //
        //    meta.setJukeboxPlayable(musicComponent);
        //}

        // Flags
        for (ItemFlag flag : flags){
            meta.addItemFlags(flag);
        }

        // Attributes
        setupAttribute(meta,Attribute.GENERIC_ATTACK_DAMAGE,"damage",damage, AttributeModifier.Operation.ADD_NUMBER);



        if (attributeData != null){
            for (AttributeData data : attributeData){
                setupAttribute(meta, data.attribute, data.attribute.name().toLowerCase(), data.amount, data.operation);
            }
        }

        // PersistentData
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(NamespacedKeys.ITEM_ID_KEY, PersistentDataType.STRING, itemId);
        data.set(NamespacedKeys.ITEM_VARIANT_ID_KEY, PersistentDataType.STRING, itemVariant);

        for (PersistentDefaultData<?> data1 : persistentDefaultData) data1.apply(data);

        for (FieldData field : fields){
            data.set(new NamespacedKey(LionPaItemAPI.getPlugin().getName().toLowerCase()+"."+itemId,field.id),field.type,field.defaultValue);
        }

        if (stringDisplay != null) {
            meta.setDisplayName(stringDisplay);
        } else if (componentDisplay != null){
            meta.displayName(componentDisplay);
        }

        if (stringLore != null){
            meta.setLore(stringLore);
        } else if (componentLore != null){
            meta.lore(componentLore);
        }

        if (maxStack == 1 && maxDamage != -1){
            Damageable damageable = (Damageable) meta;
            damageable.setMaxDamage(maxDamage);
            if (damagee != -1) damageable.setDamage(damagee);
        }

        item.setItemMeta(meta);

        return item;
    }

    private static void setupAttribute(ItemMeta meta, Attribute attribute, String key, double amount, AttributeModifier.Operation operation){
        if (amount == 0) return;
        meta.addAttributeModifier(attribute, new AttributeModifier(new NamespacedKey(LionPaItemAPI.getPlugin(), key), amount, operation));
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setItemVariant(String itemVariant) {
        this.itemVariant = itemVariant;
    }

    private record FieldData(String id, PersistentDataType type, Object defaultValue){}

    private record EatPotionEffect(PotionEffect effect, float chance){}
    private record SingleToolRule(Material material, float speed, boolean drop){}
    private record MultiToolRule(Collection<Material> material, float speed, boolean drop){}
    private record TagToolRule(Tag<Material> material, float speed, boolean drop){}
    private record EnchantmentData(Enchantment enchantment, int level, boolean b){}
    private record AttributeData(Attribute attribute, double amount, AttributeModifier.Operation operation){}
    private record PersistentDefaultData<T>(NamespacedKey key, PersistentDataType<?,T> type, T value) {
        public void apply(PersistentDataContainer dataContainer){
            dataContainer.set(key, type, value);
        }
    }
}
