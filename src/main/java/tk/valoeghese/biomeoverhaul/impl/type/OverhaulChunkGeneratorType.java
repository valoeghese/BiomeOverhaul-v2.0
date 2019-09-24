package tk.valoeghese.biomeoverhaul.impl.type;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.world.World;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;

public class OverhaulChunkGeneratorType implements InvocationHandler
{
    private Object factoryProxy;
    private Class<?> factoryClass;

    public OverhaulChunkGeneratorType()
    {
        //reflection hack, dev = mapped in dev enviroment, prod = intermediate value
        String dev_name = "net.minecraft.world.gen.chunk.ChunkGeneratorFactory";
        String prod_name = "net.minecraft.class_2801";

        try {
            factoryClass = Class.forName(dev_name);
        } catch (ClassNotFoundException e1) {
            try {
                factoryClass = Class.forName(prod_name);
            } catch (ClassNotFoundException e2) {
                throw (new RuntimeException("Unable to find " + dev_name));
            }
        }
        factoryProxy = Proxy.newProxyInstance(factoryClass.getClassLoader(), new Class[]{factoryClass}, this);
    }

    public OverhaulChunkGenerator createProxy(World w, OverhaulBiomeSource biomesource, OverhaulChunkGeneratorConfig gensettings) {
        return new OverhaulChunkGenerator(w, biomesource, gensettings);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        if (args.length == 3 && args[0] instanceof World && args[1] instanceof OverhaulBiomeSource && args[2] instanceof OverhaulChunkGeneratorConfig)
        {
            return createProxy((World) args[0], (OverhaulBiomeSource) args[1], (OverhaulChunkGeneratorConfig) args[2]);
        }
        throw (new UnsupportedOperationException("Unknown Method: " + method.toString()));
    }
    
	@SuppressWarnings("unchecked")
	public ChunkGeneratorType<OverhaulChunkGeneratorConfig, OverhaulChunkGenerator> getChunkGeneratorType(Supplier<OverhaulChunkGeneratorConfig> supplier) {
        Constructor<?>[] initlst = ChunkGeneratorType.class.getDeclaredConstructors();
        final Logger log = LogManager.getLogger("ChunkGenErr");

        for (Constructor<?> init : initlst) {
            init.setAccessible(true);
            if (init.getParameterCount() != 3) {
                continue; //skip
            }
            //lets try it
            try {
                return (ChunkGeneratorType<OverhaulChunkGeneratorConfig, OverhaulChunkGenerator>) init.newInstance(factoryProxy, true, supplier);
            } catch (Exception e) {
                log.error("Error in calling Chunk Generator Type", e);
            }
        }
        log.error("Unable to find constructor for ChunkGeneratorType");
        return null;
    }
}
