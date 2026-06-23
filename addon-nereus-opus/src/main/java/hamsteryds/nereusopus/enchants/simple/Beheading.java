package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import hamsteryds.nereusopus.utils.ConfigUtils;
import hamsteryds.nereusopus.utils.ItemUtils;
import java.io.File;
import java.util.Map;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Beheading extends EventExecutor implements FoliaNeeded {
   public Map<EntityType, String> heads = ConfigUtils.getMapFromList(
      this.getConfig().getConfigurationSection("params"), "custom-heads", ":", EntityType.class, String.class
   );

   public Beheading(File file) {
      super(file);
   }

   @Override
   public void attackEntity(int level, EntityDamageByEntityEvent event) {
      EntityType type = event.getEntity().getType();
      if (event.getEntity() instanceof LivingEntity) {
         LivingEntity creature = (LivingEntity)event.getEntity();
         if (event.getDamage() >= creature.getHealth()) {
            ItemStack item = type == EntityType.WITHER_SKELETON
               ? ItemUtils.make(Material.WITHER_SKELETON_SKULL, "")
               : (
                  type == EntityType.ZOMBIE
                     ? ItemUtils.make(Material.ZOMBIE_HEAD, "")
                     : (
                        type == EntityType.SKELETON
                           ? ItemUtils.make(Material.SKELETON_SKULL, "")
                           : (
                              type == EntityType.CREEPER
                                 ? ItemUtils.make(Material.CREEPER_HEAD, "")
                                 : (
                                    this.heads.containsKey(type)
                                       ? ItemUtils.setSkull(
                                          ItemUtils.make(
                                             Material.PLAYER_HEAD,
                                             Component.translatable(creature.getType().translationKey()).append(Component.text("\u5934\u9885"))
                                          ),
                                          this.heads.get(type)
                                       )
                                       : null
                                 )
                           )
                     )
               );
            if (item != null) {
               creature.getWorld().dropItem(creature.getLocation(), item);
            }
         }
      }
   }
}
