package hamsteryds.nereusopus.enchants.internal;

import cc.polarastrum.aiyatsbus.core.AiyatsbusEnchantmentBase;
import cc.polarastrum.aiyatsbus.core.data.trigger.Mechanism;
import taboolib.library.configuration.ConfigurationSection;
import taboolib.module.configuration.Configuration;
import taboolib.module.configuration.Type;
import hamsteryds.nereusopus.library.parrotx.utils.FileUtil;
import hamsteryds.nereusopus.utils.StringUtils;
import hamsteryds.nereusopus.utils.stats.MathUtils;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

public class CustomEnchantment extends AiyatsbusEnchantmentBase {
   private HashMap<String, String> params;

   public CustomEnchantment(File file) {
      super(FileUtil.getNoExFilename(file), file, Configuration.Companion.loadFromFile(file, Type.YAML, true));
      this.loadData();
   }

   public void loadData() {
      this.params = new HashMap<>();
      this.getVariables().getLeveled().forEach((key, value) -> {
         Map<Integer, String> data = (Map<Integer, String>)value.getSecond();
         this.params.put(key, data.get(1).replace("{", "").replace("}", ""));
      });
      this.getVariables().getOrdinary().forEach((key, value) -> this.params.put(key, value.toString()));
      ConfigurationSection paramSection = this.getConfig().getConfigurationSection("params");
      if (paramSection != null) {
         for (String path : paramSection.getKeys(true)) {
            if (paramSection.getList(path) == null && paramSection.getConfigurationSection(path) == null) {
               this.params.put(path, String.valueOf(paramSection.get(path)));
            }
         }
      }
   }

   public HashMap<String, String> params() {
      return this.params;
   }

   public double getValue(String path, String... defaultValue) {
      return this.params.containsKey(path) ? MathUtils.calculate(this.params.get(path)) : MathUtils.calculate(defaultValue[0]);
   }

   public double getValue(String path, int level, String... defaultValue) {
      return this.params.containsKey(path) ? MathUtils.calculate(this.params.get(path), "level", level) : MathUtils.calculate(defaultValue[0]);
   }

   public String getText(String path, String... replacers) {
      return StringUtils.replace(this.params.get(path), replacers).replace("{enchant}", this.getRarity().displayName(this.getBasicData().getName()));
   }

   public boolean getBool(String path, boolean... defaultValue) {
      return this.params.containsKey(path) ? Boolean.parseBoolean(this.params.get(path)) : defaultValue[0];
   }

   public int getInt(String path, int... defaultValue) {
      return this.params.containsKey(path) ? Integer.parseInt(this.params.get(path)) : defaultValue[0];
   }

   @Nullable
   public Mechanism getMechanism() {
      return null;
   }
}
