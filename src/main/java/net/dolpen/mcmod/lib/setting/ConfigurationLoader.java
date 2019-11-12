package net.dolpen.mcmod.lib.setting;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Arrays;
import java.util.function.Supplier;

public class ConfigurationLoader {
    private final Configuration configuration;
    private boolean isSaveNeeded = false;

    public ConfigurationLoader(@Nonnull File file) {
        configuration = new Configuration(file);
        configuration.load();
    }

    public <T> T load(String category, Supplier<T> supplier) {
        T obj = supplier.get();
        Arrays.stream(obj.getClass().getFields()).forEach(f -> {
            Class type = f.getType();
            String name = f.getName();
            isSaveNeeded |= !configuration.hasKey(category, name);
            try {
                if (type.equals(Boolean.TYPE)) {
                    Property p = configuration.get(category, name, f.getBoolean(obj));
                    boolean v = p.getBoolean();
                    f.setBoolean(obj, v);
                    p.set(v);
                } else if (type.equals(Integer.TYPE)) {
                    Property p = configuration.get(category, name, f.getInt(obj));
                    int v = p.getInt();
                    f.setInt(obj, v);
                    p.set(v);
                } else if (type.equals(Double.TYPE)) {
                    Property p = configuration.get(category, name, f.getDouble(obj));
                    double v = p.getDouble();
                    f.setDouble(obj, v);
                    p.set(v);
                } else if (type.equals(String.class)) {
                    Property p = configuration.get(category, name, f.get(obj).toString());
                    String v = p.getString();
                    f.set(obj, v);
                    p.set(v);
                } else {
                    FMLLog.log.printf(
                            Level.INFO,
                            "error in loading %s.%s : unrecognizable type %s",
                            category,
                            name,
                            type.getCanonicalName()
                    );
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                FMLLog.log.printf(
                        Level.WARN,
                        "error in loading %s.%s : %s",
                        category,
                        name,
                        e.getLocalizedMessage()
                );
            }
        });
        return obj;
    }

    public void saveIfNeeded() {
        if (isSaveNeeded) configuration.save();
        isSaveNeeded = false;
    }

}
