package io.lionpa.lionpaitemapi.item;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.WeakHashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CustomItem {
    private static final HashMap<String, CustomItem> items = new HashMap<>();
    private static final HashMap<Class<? extends ItemConstructor>, CustomItem> itemsByClass = new HashMap<>();
    private final String id;
    private final HashMap<String, ItemVariantBuilder> variants;
    private final ItemConstructor constructor;

    public static void register(CustomItem item){
        items.put(item.id,item);
        itemsByClass.put(item.constructor.getClass(), item);
    }

    public CustomItem(String id, HashMap<String, ItemVariantBuilder> variants, ItemConstructor constructor){
        this.id = id;
        this.variants = variants;
        this.constructor = constructor;
    }

    private final HashMap<String, ItemStack> hashedVariants = new HashMap<>();

    public ItemStack getItem(){
        return getItemVariant("default");
    }

    public ItemStack getItemVariant(String variant){
        if (hashedVariants.containsKey(variant)){
            return hashedVariants.get(variant).clone();
        } else {
            ItemStack item = variants.get(variant).build();
            hashedVariants.put(variant,item);
            return item.clone();
        }
    }

    public void modifyItem(Consumer<ItemVariantBuilder> change){
        modifyItemVariant("default", change);
    }
    public void modifyItemVariant(String variant, Consumer<ItemVariantBuilder> change){
        hashedVariants.remove(variant);
        change.accept(variants.get(variant));
    }

    public ItemConstructor getConstructor() {
        return constructor;
    }

    public static CustomItem getItem(String id){
        return items.get(id);
    }

    public static CustomItem getItemByClass(Class<? extends ItemConstructor> clazz){
        return itemsByClass.get(clazz);
    }

    public String getId() {
        return id;
    }

    private static final WeakHashMap<String, Supplier<CustomItem>> suppliersHash = new WeakHashMap<>();

    public static Supplier<CustomItem> getItemSupplier(String id){
        if (suppliersHash.containsKey(id)) return suppliersHash.get(id);
        Supplier<CustomItem> supplier = () -> getItem(id);
        suppliersHash.put(id, supplier);
        return supplier;
    }




}
