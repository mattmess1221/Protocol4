package mnm.mods.protocol.asm;

import com.mumfrey.liteloader.core.runtime.Obf;
import com.mumfrey.liteloader.transformers.ClassTransformer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.ListIterator;

public class PacketIOTransformer extends ClassTransformer implements Opcodes {

    private static final Logger logger = LogManager.getLogger("Protocol4");

    private static final String packetIO = "mnm/mods/protocol/PacketIO";
    private static final String READ_PACKET = "readPacketData";
    private static final String WRITE_PACKET = "writePacketData";

    private static final Obf readPacketData = new Obf4("func_148837_a", "a", "readPacketData");
    private static final Obf writePacketData = new Obf4("func_148840_b", "b", "writePacketData");
    private static final Obf PacketBuffer = new Obf4("net.minecraft.network.PacketBuffer", "et");
    private static final Obf Packet = new Obf4("net.minecraft.network.Packet", "ft");

    public static class Obf4 extends Obf {

        protected Obf4(String seargeName, String obfName, String mcpName) {
            super(seargeName, obfName, mcpName);
        }

        protected Obf4(String seargeName, String obfName) {
            super(seargeName, obfName);
        }
    }

    public PacketIOTransformer() {}

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        try {
            basicClass = transform(basicClass);
        } catch (Throwable t) {
            logger.warn("An error occured while trying to process " + transformedName, t);
        }
        return basicClass;
    }

    private byte[] transform(byte[] basicClass) {
        if (basicClass != null) {
            ClassNode cn = readClass(basicClass, false);

            if (hasSuper(cn, Packet) && (cn.access & ACC_ABSTRACT) != ACC_ABSTRACT) {
                boolean read = false;
                boolean write = false;

                for (MethodNode mn : cn.methods) {
                    if (arrayHas(readPacketData.names, mn.name)) {
                        int env = getEnv(readPacketData, mn.name);
                        String desc = createDescriptor(env, PacketBuffer);
                        // Test for correct descriptor
                        if (desc.equals(mn.desc)) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("Transforming " + cn.name + "." + mn.name + mn.desc);
                            }
                            transformReadPacket(mn);
                            read = true;
                        }
                    }

                    if (arrayHas(writePacketData.names, mn.name)) {
                        int env = getEnv(writePacketData, mn.name);
                        String desc = createDescriptor(env, PacketBuffer);
                        // Test for correct descriptor
                        if (desc.equals(mn.desc)) {
                            transformWritePacket(mn);
                            write = true;
                        }
                    }
                }

                if (!read || !write) {
                    logger.error("Transforming failed for " + cn.name);
                } else {
                    basicClass = writeClass(cn);
                }
            }

        }
        return basicClass;
    }

    private boolean hasSuper(ClassNode cn, Obf packet) {
        boolean has = false;
        String superName = cn.superName;
        if (arrayHas(makeRef(packet.names), superName)) {
            has = true;
        }

        return has;
    }

    private String[] makeRef(String[] names) {
        String[] newNames = new String[names.length];
        for (int i = 0; i < names.length; i++) {
            newNames[i] = names[i].replace('.', '/');
        }
        return newNames;
    }

    private <T> boolean arrayHas(T[] names, T target) {
        boolean result = false;
        for (T name : names) {
            if (name.equals(target)) {
                result = true;
                break;
            }
        }
        return result;
    }

    private String createDescriptor(int env, Obf... params) {
        StringBuilder strParams = new StringBuilder();
        for (Obf obf : params) {
            strParams.append(obf.getDescriptor(env));
        }
        String ret = "V";
        return String.format("(%s)%s", strParams.toString(), ret);
    }

    private int getEnv(Obf obf, String name) {
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
            throw new IllegalArgumentException("Given method name is not in Obf table: " + name);
        }
        return env;
    }

    private void transformReadPacket(MethodNode mn) {
        int env = getEnv(readPacketData, mn.name);
        String desc = createDescriptor(env, Packet, PacketBuffer);
        InsnList insn = mn.instructions;

        InsnList list = new InsnList();
        {
            list.add(new VarInsnNode(ALOAD, 0)); // this
            list.add(new VarInsnNode(ALOAD, 1)); // buffer
            list.add(new MethodInsnNode(INVOKESTATIC, packetIO, READ_PACKET, desc, false));
        }
        // Insert at beginning
        insn.insert(list);
    }

    private void transformWritePacket(MethodNode mn) {
        int env = getEnv(writePacketData, mn.name);
        String desc = createDescriptor(env, Packet, PacketBuffer);
        InsnList insn = mn.instructions;

        InsnList list = new InsnList();
        {
            list.add(new VarInsnNode(ALOAD, 0)); // this
            list.add(new VarInsnNode(ALOAD, 1)); // buffer
            list.add(new MethodInsnNode(INVOKESTATIC, packetIO, WRITE_PACKET, desc, false));
        }
        // Insert before return
        ListIterator<AbstractInsnNode> iter = insn.iterator();
        while (iter.hasNext()) {
            AbstractInsnNode node = iter.next();
            if (node.getOpcode() == RETURN) {
                insn.insertBefore(node, list);
            }
        }
    }
}
