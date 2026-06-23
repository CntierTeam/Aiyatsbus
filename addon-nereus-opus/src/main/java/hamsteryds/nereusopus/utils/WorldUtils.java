package hamsteryds.nereusopus.utils;

import taboolib.library.xseries.XAttribute;
import taboolib.library.xseries.particles.XParticle;
import dev.lone.itemsadder.api.CustomBlock;
import hamsteryds.nereusopus.ConfigReader;
import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.enchants.internal.CustomEnchantment;
import hamsteryds.nereusopus.listeners.executors.entries.BlockListener;
import java.util.List;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class WorldUtils {
   public static <T> void spawnRNAParticles(Particle particle, Location loc, int amount, T option, double height, double range) {
      spawnRNAParticles(particle, loc, amount, option, height, range, 10, 1);
   }

   public static <T> void spawnRNAParticles(Particle particle, Location loc, int amount, T option, double height, double range, int factor, int circle) {
      World world = loc.getWorld();
      double i = 0.0;
      double currentH = -height;

      while (i <= 360.0 * circle) {
         double rad = Math.toRadians(i);
         double sin = Math.sin(rad) * range;
         double cos = Math.cos(rad) * range;
         world.spawnParticle(particle, loc.clone().add(sin, currentH, cos), amount, option);
         i += 360.0 / factor;
         currentH += 2.0 * height / factor / circle;
      }
   }

   public static <T> void spawnCircleParticles(Particle particle, Location loc, int amount, T option, double range) {
      spawnCircleParticles(particle, loc, amount, option, range, 10);
   }

   public static <T> void spawnCircleParticles(Particle particle, Location loc, int amount, T option, double range, int factor) {
      World world = loc.getWorld();
      double i = 0.0;

      while (i <= 360.0) {
         double rad = Math.toRadians(i);
         double sin = Math.sin(rad) * range;
         double cos = Math.cos(rad) * range;
         world.spawnParticle(particle, loc.clone().add(sin, 0.0, cos), amount, option);
         i += 360.0 / factor;
      }
   }

   public static <T> void spawnSimpleParticle(Particle particle, Location loc, int amount, T option) {
      Class<?> clazz = particle.getDataType();
      if (clazz != null && option != null) {
         loc.getWorld().spawnParticle(particle, loc, amount, clazz.cast(option));
      } else {
         loc.getWorld().spawnParticle(particle, loc, amount);
      }
   }

   public static void breakExtraBlock(Player player, Block block) {
      try {
         BlockListener.breakExtra(block);
         player.breakBlock(block);
      } catch (Exception var6) {
         var6.printStackTrace();
      } finally {
         if (block.getType() != Material.AIR) {
            if (ConfigReader.supportItemsAdder) {
               if (CustomBlock.byAlreadyPlaced(block) != null) {
                  CustomBlock.getLoot(block, player.getInventory().getItemInMainHand(), true)
                     .forEach(itemStack -> block.getLocation().getWorld().dropItem(block.getLocation(), itemStack));
                  CustomBlock.remove(block.getLocation());
               } else {
                  block.breakNaturally(player.getInventory().getItemInMainHand());
               }
            } else {
               block.breakNaturally(player.getInventory().getItemInMainHand());
            }
         }
      }
   }

   public static void fastMutilBreak(Player player, List<Location> breaks, Integer speed, CustomEnchantment ench) {
      FoliaUtils.runAtFixedRate(
         player,
         task -> {
            for (int i = 0; i < speed; i++) {
               Optional<Location> location = breaks.stream().findFirst();
               if (location.isEmpty()) {
                  task.cancel();
               } else {
                  Block block = location.get().getBlock();
                  breaks.remove(location.get());
                  if (!block.getLocation().getWorld().equals(player.getLocation().getWorld())) {
                     NereusOpus.getInstance()
                        .getLang()
                        .log
                        .warn(
                           "\u68c0\u6d4b\u5230\u73a9\u5bb6"
                              + player.getName()
                              + "\u4f7f\u7528\u9644\u9b54\u7834\u574f\u4e86\u4e00\u4e2a\u4e0d\u5728\u540c\u4e00\u4e16\u754c\u7684\u65b9\u5757\uff0c\u53ef\u80fd\u662f\u4f5c\u5f0a\u884c\u4e3a\u6216\u662f\u6781\u7aef\u60c5\u51b5\u4e0b\u7684\u6781\u5c0f\u6982\u7387\u884c\u4e3a\uff0c\u5df2\u5ffd\u7565\u8be5\u7834\u574f"
                        );
                     NereusOpus.getInstance()
                        .getLang()
                        .log
                        .warn(
                           "|- \u65b9\u5757\u4fe1\u606f: "
                              + block.getLocation().getWorld().getName()
                              + ","
                              + block.getLocation().getBlockX()
                              + ","
                              + block.getLocation().getBlockY()
                              + ","
                              + block.getLocation().getBlockZ()
                        );
                  } else if (MechanismUtils.checkPermission(ench, block, player)) {
                     breakExtraBlock(player, block);
                  }
               }
            }
         },
         0L,
         0L
      );
   }

   public static Particle getParticle(String name) {
      try {
         return XParticle.valueOf(name).get();
      } catch (Exception var2) {
         return XParticle.ASH.get();
      }
   }

   public static String locToString(Location loc) {
      return loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
   }

   public static Location stringToLoc(String string) {
      String[] splited = string.split(",");
      return new Location(Bukkit.getWorld(splited[0]), Integer.parseInt(splited[1]), Integer.parseInt(splited[2]), Integer.parseInt(splited[3]));
   }

   public static boolean isUnsafeVelocity(Vector vector) {
      double x = vector.getBlockX();
      double y = vector.getBlockY();
      double z = vector.getBlockZ();
      return x > 4.0 || y > 4.0 || z > 4.0 || x < -4.0 || y < -4.0 || z < -4.0;
   }

   private static double getSafeVelocity(double x) {
      return x > 4.0 ? 4.0 : Math.max(x, -4.0);
   }

   public static Vector convertToSafeVelocity(Vector vector) {
      double x = vector.getX();
      double y = vector.getY();
      double z = vector.getZ();
      return new Vector(getSafeVelocity(x), getSafeVelocity(y), getSafeVelocity(z));
   }

   public static void addVelocity(Entity entity, Vector vector, boolean checkKnockback) {
      if (checkKnockback && entity instanceof LivingEntity creature) {
         AttributeInstance instance = creature.getAttribute((Attribute)XAttribute.KNOCKBACK_RESISTANCE.get());
         if (instance != null) {
            double value = instance.getValue();
            if (value >= 1.0) {
               return;
            }

            if (value > 0.0) {
               vector.multiply(1.0 - value);
            }
         }
      }

      Vector newVelocity = vector;
      if (isUnsafeVelocity(vector)) {
         newVelocity = convertToSafeVelocity(vector);
      }

      try {
         entity.setVelocity(newVelocity);
      } catch (Error | Exception var7) {
      }
   }
}
