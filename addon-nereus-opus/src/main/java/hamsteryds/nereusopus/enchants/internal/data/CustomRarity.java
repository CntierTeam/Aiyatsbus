package hamsteryds.nereusopus.enchants.internal.data;

import taboolib.library.configuration.ConfigurationSection;
import hamsteryds.nereusopus.utils.ItemUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.inventory.ItemStack;

public class CustomRarity {
   public static HashMap<String, CustomRarity> rarities = new HashMap<>();
   public static List<CustomRarity> ordered = new ArrayList<>();
   private final String rarityId;
   private final String displayName;
   private final String colorFormat;
   private final double weight;
   private final Set<AttainSource> attainSources;
   private final boolean showed;
   private final int order;
   private final ItemStack displayItem;

   public CustomRarity(
      String rarityId,
      String displayName,
      String colorFormat,
      double weight,
      List<AttainSource> attainSources,
      boolean showed,
      int order,
      ItemStack displayItem
   ) {
      this.rarityId = rarityId;
      this.displayName = displayName;
      this.colorFormat = colorFormat;
      this.weight = weight;
      this.attainSources = new HashSet<>(attainSources);
      this.showed = showed;
      this.order = order;
      this.displayItem = displayItem;
      boolean flag = false;

      for (int i = 0; i < ordered.size(); i++) {
         if (ordered.get(i).order > order) {
            ordered.add(i, this);
            flag = true;
            break;
         }
      }

      if (!flag) {
         ordered.add(this);
      }
   }

   public static void registerRarity(String rarityId, ConfigurationSection config) {
      ItemStack item = ItemUtils.fromString(config.getString("display.item", "STONE;;"));
      if (config.contains("display.skull")) {
         ItemUtils.setSkull(item, config.getString("display.skull"));
      }

      List<AttainSource> sources = new ArrayList<>();
      if (config.getBoolean("accessibleFromTable")) {
         sources.add(AttainSource.ENCHANTING_TABLE);
      }

      if (config.getBoolean("accessibleFromLoot")) {
         sources.add(AttainSource.LOOT_CHEST);
      }

      if (config.getBoolean("accessibleFromVillager")) {
         sources.add(AttainSource.VILLAGER);
      }

      CustomRarity rarity = new CustomRarity(
         rarityId,
         config.getString("displayName"),
         config.getString("colorFormat"),
         config.getDouble("weight", 0.0),
         sources,
         config.getBoolean("display.enable"),
         config.getInt("display.order"),
         item
      );
      rarities.put(rarityId, rarity);
   }

   public static CustomRarity fromId(String rarityId) {
      return rarities.get(rarityId);
   }

   public String displayName() {
      return this.colorFormat + this.displayName;
   }

   public String name() {
      return this.displayName;
   }

   public String id() {
      return this.rarityId;
   }

   public String color() {
      return this.colorFormat;
   }

   public double weight() {
      return this.weight;
   }

   public Set<AttainSource> attainSources() {
      return this.attainSources;
   }

   public boolean canAttainFrom(AttainSource source) {
      return this.attainSources.contains(source);
   }

   public ItemStack displayItem() {
      return this.displayItem;
   }

   public boolean displayEnabled() {
      return this.showed;
   }
}
