package hamsteryds.nereusopus.enchants.artifact;

import taboolib.library.xseries.particles.XParticle;
import hamsteryds.nereusopus.enchants.internal.entries.ArtifactEnchantment;
import java.io.File;
import org.bukkit.Particle;

public class AngerArtifact extends ArtifactEnchantment {
   public AngerArtifact(File file) {
      super(file);
   }

   @Override
   public Particle getParticle() {
      return XParticle.ANGRY_VILLAGER.get();
   }
}
