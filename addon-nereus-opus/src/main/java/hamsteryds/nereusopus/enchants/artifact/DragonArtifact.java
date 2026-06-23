package hamsteryds.nereusopus.enchants.artifact;

import taboolib.library.xseries.particles.XParticle;
import hamsteryds.nereusopus.enchants.internal.entries.ArtifactEnchantment;
import java.io.File;
import org.bukkit.Particle;

public class DragonArtifact extends ArtifactEnchantment {
   public DragonArtifact(File file) {
      super(file);
   }

   @Override
   public Particle getParticle() {
      return XParticle.DRAGON_BREATH.get();
   }
}
