package hamsteryds.nereusopus.enchants.skill;

import cc.polarastrum.aiyatsbus.core.compat.AntiGriefChecker;
import hamsteryds.nereusopus.enchants.internal.entries.SkillEnchantment;
import hamsteryds.nereusopus.listeners.ListenerRegisterer;
import java.io.File;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

public class Missile extends SkillEnchantment implements Listener {
   public static final NamespacedKey NAMESPACED_KEY = new NamespacedKey("summericebearstore", "missile");

   public Missile(File file) {
      super(file);
      ListenerRegisterer.register(this);
   }

   @Override
   public void run(Player player, int level) {
      double speed = this.getValue("velocity", level);
      Vector direction = player.getEyeLocation().getDirection().multiply(speed);
      WitherSkull skull = (WitherSkull)player.launchProjectile(WitherSkull.class, direction);
      skull.setShooter(player);
      skull.setCharged(true);
      skull.getPersistentDataContainer().set(NAMESPACED_KEY, PersistentDataType.BOOLEAN, true);
   }

   @EventHandler
   public void onEntityExplode(EntityExplodeEvent e) {
      if (e.getEntity() instanceof WitherSkull skull) {
         if (!skull.getPersistentDataContainer().has(NAMESPACED_KEY)) {
            return;
         }

         if (skull.getShooter() instanceof Player player) {
            e.blockList().removeIf(block -> !AntiGriefChecker.INSTANCE.canBreak(player, block.getLocation()));
         }
      }
   }
}
