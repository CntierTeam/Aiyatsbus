package hamsteryds.nereusopus.enchants.artifact;

import taboolib.library.xseries.particles.XParticle;
import hamsteryds.nereusopus.enchants.internal.entries.ArtifactEnchantment;
import java.io.File;
import org.bukkit.Particle;

public class TotemArtifact extends ArtifactEnchantment {
   public TotemArtifact(File file) {
      super(file);
   }

   @Override
   public Particle getParticle() {
      return XParticle.TOTEM_OF_UNDYING.get();
   }
}
