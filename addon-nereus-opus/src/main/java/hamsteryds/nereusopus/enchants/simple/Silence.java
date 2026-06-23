package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.FoliaNeeded;
import java.io.File;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Silence extends EventExecutor implements FoliaNeeded {
   public static HashMap<UUID, Long> stamps = new HashMap<>();

   public Silence(File file) {
      super(file);
   }

   @Override
   public void damagedByEntity(int level, EntityDamageByEntityEvent event) {
      if (event.getDamager() instanceof Player) {
         Player player = (Player)event.getDamager();
         stamps.put(player.getUniqueId(), (long)(System.currentTimeMillis() + this.getValue("duration", level) * 50.0));
         player.sendTitle("\u00a77", "\u00a7e\u60a8\u88ab\u5bf9\u65b9\u6c89\u9ed8\u4e86\uff01");
      }
   }
}
