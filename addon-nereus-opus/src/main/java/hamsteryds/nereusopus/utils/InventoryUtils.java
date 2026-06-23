package hamsteryds.nereusopus.utils;

import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InventoryUtils {
   public static final EquipmentSlot[] ARMORS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
   public static final EquipmentSlot[] HANDS = new EquipmentSlot[]{EquipmentSlot.HAND, EquipmentSlot.OFF_HAND};
   public static final EquipmentSlot[] ALL = new EquipmentSlot[]{
      EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND
   };

   public static HashMap<EquipmentSlot, ItemStack> getEquippedItems(Entity entity) {
      HashMap<EquipmentSlot, ItemStack> equippedItems = new HashMap<>();
      if (entity instanceof LivingEntity creature) {
         EntityEquipment equipment = creature.getEquipment();
         if (equipment != null) {
            for (EquipmentSlot slot : ALL) {
               equippedItems.put(slot, equipment.getItem(slot));
            }
         }
      }

      return equippedItems;
   }

   public static void giveItemOrDrop(Player player, ItemStack item) {
      if (item != null) {
         if (item.getType() != Material.AIR) {
            PlayerInventory inv = player.getInventory();

            for (int i = 0; i < 36; i++) {
               if (inv.getItem(i) == null) {
                  inv.addItem(new ItemStack[]{item});
                  return;
               }
            }

            player.getWorld().dropItem(player.getLocation(), item);
         }
      }
   }

   public static boolean canContainMore(Player player, ItemStack item) {
      PlayerInventory inv = player.getInventory();

      for (int i = 0; i < 36; i++) {
         if (inv.getItem(i) == null) {
            return true;
         }

         ItemStack slotItem = inv.getItem(i);
         if (slotItem.isSimilar(item) && slotItem.getAmount() < slotItem.getType().getMaxStackSize()) {
            return true;
         }
      }

      return false;
   }
}
