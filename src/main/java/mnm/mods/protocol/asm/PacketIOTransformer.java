package mnm.mods.protocol.asm;

import net.minecraft.launchwrapper.IClassTransformer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import com.mumfrey.liteloader.core.runtime.Obf;

public class PacketIOTransformer implements IClassTransformer {

    private static final Logger logger = LogManager.getLogger("Protocol4");

    public PacketIOTransformer() {}

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        try {
            basicClass = transform(transformedName, basicClass);
        } catch (Throwable t) {
            logger.warn("An error occured while trying to process " + transformedName, t);
        }
        return basicClass;
    }

    private byte[] transform(String name, byte[] basicClass) {
        if (basicClass != null) {
            ClassReader cr = new ClassReader(basicClass);
            ClassWriter cw = new ClassWriter(cr, 0);
            ClassVisitor cv;

            int read = getEnv(PacketObf.MessageDeserializer, name);
            int write = getEnv(PacketObf.MessageSerializer, name);

            if (read != -1) {
                // read packet
                cv = new MsgDeserializerCV(cw);
            } else if (write != -1) {
                // write packet
                cv = new MsgSerializerCV(cw);
            } else {
                return basicClass;
            }

            cr.accept(cv, 0);

            basicClass = cw.toByteArray();
        }
        return basicClass;
    }

    public static int getEnv(Obf obf, String name) {
        name = name.replace('/', '.');
        int env;
        if (name.equals(obf.name)) {
            // mcp
            env = Obf.MCP;
        } else if (name.equals(obf.obf)) {
            // obf
            env = Obf.OBF;
        } else if (name.equals(obf.srg)) {
            // srg
            env = Obf.SRG;
        } else {
            env = -1;
        }
        return env;
    }

    public static String buildDescriptor(int env, String ret, Object... args) {
        StringBuilder sb = new StringBuilder("(");
        for (Object obf : args) {
            sb.append('L');
            String name;
            if (obf instanceof Obf) {
                name = ((Obf) obf).names[env];
            } else {
                name = obf.toString();
            }
            sb.append(name.replace('.', '/'));
            sb.append(';');
        }
        sb.append(')').append(ret);
        return sb.toString();
    }
}
