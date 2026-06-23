package hamsteryds.nereusopus.enchants.curse;

import cc.polarastrum.aiyatsbus.core.AiyatsbusUtilsKt;
import cc.polarastrum.aiyatsbus.core.event.AiyatsbusPrepareAnvilEvent;
import hamsteryds.nereusopus.listeners.ListenerRegisterer;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import java.io.File;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class PermanenceCurse extends EventExecutor implements Listener {
   public PermanenceCurse(File file) {
      super(file);
      ListenerRegisterer.register(this);
   }

   @EventHandler
   public void onAnvil(AiyatsbusPrepareAnvilEvent event) {
      ItemStack first = event.getLeft();
      if (!first.isEmpty() && !first.getType().isAir()) {
         if (AiyatsbusUtilsKt.etLevel(first, AiyatsbusUtilsKt.aiyatsbusEt("permanence_curse")) >= 1) {
            event.setCancelled(true);
         }
      }
   }
}
