package hamsteryds.nereusopus.utils;

import cc.polarastrum.aiyatsbus.core.compat.AntiGriefChecker;
import cc.polarastrum.aiyatsbus.core.compat.NPCChecker;
import taboolib.library.xseries.XEntityType;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class PermissionUtils implements Listener {
   public static HashMap<Entity, Boolean> cache = new HashMap<>();
   public static List<EntityType> neverCheck = Stream.of(
         XEntityType.ITEM_FRAME, XEntityType.GLOW_ITEM_FRAME, XEntityType.ITEM, XEntityType.DRAGON_FIREBALL, XEntityType.WITHER_SKULL, XEntityType.ARMOR_STAND
      )
      .<EntityType>map(XEntityType::get)
      .filter(Objects::nonNull)
      .toList();

   public static boolean hasDamagePermission(Entity damager, Entity entity) {
      return !(damager instanceof Player) ? false : AntiGriefChecker.INSTANCE.canDamage((Player)damager, entity);
   }

   public static boolean checkIfIsNPC(Entity entity) {
      return NPCChecker.Companion.checkIfIsNPC(entity);
   }

   public static void delayClear(Entity entity) {
      FoliaUtils.runDelayed(task -> {}, 5L);
   }
}
