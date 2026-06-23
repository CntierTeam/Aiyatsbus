package hamsteryds.nereusopus.enchants.skill;

import taboolib.library.configuration.ConfigurationSection;
import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.enchants.internal.entries.SkillEnchantment;
import hamsteryds.nereusopus.listeners.ListenerRegisterer;
import hamsteryds.nereusopus.utils.FoliaUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffectType;

public class Xray extends SkillEnchantment implements Listener {
   public static HashMap<Location, UUID> shulkers = new HashMap<>();
   public HashMap<Material, ChatColor> ores = new HashMap<>();

   public Xray(File file) {
      super(file);
      ConfigurationSection blocks = this.getConfig().getConfigurationSection("params.blocks");

      for (String color : blocks.getKeys(false)) {
         for (String type : blocks.getStringList(color)) {
            try {
               this.ores.put(Material.valueOf(type), ChatColor.valueOf(color));
            } catch (Exception var8) {
               NereusOpus.getInstance()
                  .getLang()
                  .log
                  .error(
                     "\u2716\u9644\u9b54Xray\u914d\u7f6e\u51fa\u73b0\u95ee\u9898(\u4e0d\u5b58\u5728\u7684\u65b9\u5757\u7c7b\u578b:"
                        + type
                        + ") (\u5df2\u7ecf\u81ea\u52a8\u5ffd\u7565)"
                  );
            }
         }
      }

      ListenerRegisterer.register(this);
   }

   @EventHandler(priority = EventPriority.HIGH)
   public void onBreakBlock(BlockBreakEvent event) {
      if (!event.isCancelled()) {
         Location loc = event.getBlock().getLocation();
         if (shulkers.containsKey(loc) && Bukkit.getEntity(shulkers.get(loc)) instanceof Shulker) {
            Shulker shulker = (Shulker)Bukkit.getEntity(shulkers.get(loc));
            shulker.remove();
         }
      }
   }

   @Override
   public void run(Player player, int level) {
      double range = this.getValue("range", level);
      List<UUID> shulkers = new ArrayList<>();

      for (double x = -range; x <= range; x++) {
         for (double y = -range; y <= range; y++) {
            for (double z = -range; z <= range; z++) {
               Location loc = player.getLocation().add(x, y, z);
               Block block = loc.getBlock();
               ChatColor color = this.ores.get(block.getType());
               if (color != null) {
                  Shulker shulker = (Shulker)loc.getWorld().spawnEntity(loc, EntityType.SHULKER);
                  shulker.setInvulnerable(true);
                  shulker.setSilent(true);
                  shulker.setAI(false);
                  shulker.setGravity(false);
                  shulker.setGlowing(true);
                  shulker.setInvisible(true);
                  shulker.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(100000, 0));
                  shulkers.add(shulker.getUniqueId());
                  Xray.shulkers.put(block.getLocation(), shulker.getUniqueId());
               }
            }
         }
      }

      FoliaUtils.runDelayed(task -> {
         for (UUID uuid : shulkers) {
            Entity entity = Bukkit.getEntity(uuid);
            if (entity != null) {
               entity.remove();
            }
         }
      }, (long)this.getValue("duration", level));
   }
}
