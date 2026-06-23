package hamsteryds.nereusopus.utils.items;

import taboolib.library.xseries.XItemFlag;
import taboolib.library.xseries.XMaterial;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtils {
   public static ItemStack make(Material type, String displayName, String... lore) {
      return make(type, displayName, Arrays.asList(lore));
   }

   public static ItemStack make(Material type, int durability, String displayName, String... lore) {
      ItemStack item = make(type, displayName, Arrays.asList(lore));
      item.setDurability((short)durability);
      return item;
   }

   public static ItemStack make(Material type, int amount, short durability, String displayName, String... lore) {
      ItemStack item = make(type, displayName, Arrays.asList(lore));
      item.setAmount(amount);
      item.setDurability(durability);
      return item;
   }

   public static ItemStack make(Material type, String displayName, List<String> lore) {
      ItemStack item = new ItemStack(type);
      ItemMeta meta = item.getItemMeta();
      if (!displayName.equalsIgnoreCase("")) {
         meta.setDisplayName(displayName.replace("&", "\u00a7"));
      }

      if (lore.size() != 0) {
         for (int i = 0; i < lore.size(); i++) {
            lore.set(i, lore.get(i).replace("&", "\u00a7"));
         }

         meta.setLore(lore);
      }

      meta.addItemFlags(new ItemFlag[]{XItemFlag.HIDE_ADDITIONAL_TOOLTIP.get()});
      item.setItemMeta(meta);
      return item;
   }

   public static Material getMaterial(String name) {
      return XMaterial.matchXMaterial(name).orElse(XMaterial.STONE).get();
   }
}
