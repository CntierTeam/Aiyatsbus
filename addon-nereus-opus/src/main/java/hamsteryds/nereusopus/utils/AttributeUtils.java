package hamsteryds.nereusopus.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import java.util.UUID;

public class AttributeUtils {
   public static NamespacedKey getKey(AttributeModifier modifier) {
      return NamespacedKey.minecraft(modifier.getName());
   }

   public static AttributeModifier newByKey(NamespacedKey key, double amount, Operation operation) {
      return new AttributeModifier(UUID.randomUUID(), key.getKey(), amount, operation);
   }
}
