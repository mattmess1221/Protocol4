package mnm.mods.protocol.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MsgDeserializerCV extends ClassVisitor {

    public MsgDeserializerCV(ClassWriter cw) {
        super(Opcodes.ASM5, cw);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        int decode = PacketIOTransformer.getEnv(PacketObf.decode, name);
        // ensure name and desc is right
        if (decode != -1) {
            String desc2 = PacketIOTransformer.buildDescriptor(decode, "V",
                    "io.netty.channel.ChannelHandlerContext", "io.netty.buffer.ByteBuf", "java.util.List");
            if (desc.equals(desc2)) {
                mv = new DecodeMV(mv);
            }
        }
        return mv;
    }

    private class DecodeMV extends MethodVisitor {

        public DecodeMV(MethodVisitor mv) {
            super(Opcodes.ASM5, mv);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            // inject before
            int pkt = PacketIOTransformer.getEnv(PacketObf.Packet, owner);
            int read = PacketIOTransformer.getEnv(PacketObf.readPacketData, name);
            if (read != -1 && pkt != -1) {
                String newdesc = PacketIOTransformer
                        .buildDescriptor(pkt, "V", PacketObf.Packet, PacketObf.PacketBuffer);

                visitFrame(Opcodes.F_APPEND, 3, new Object[] {
                        PacketObf.PacketBuffer.names[pkt].replace('.', '/'),
                        Opcodes.INTEGER,
                        PacketObf.Packet.names[pkt].replace('.', '/') },
                        0, null);
                visitMethodInsn(Opcodes.INVOKESTATIC, "mnm/mods/protocol/PacketIO", "readPacketData", newdesc, false);
                // these were already loaded. Load them again
                visitVarInsn(Opcodes.ALOAD, 6);
                visitVarInsn(Opcodes.ALOAD, 4);
            }

            // continue as normal
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }
    }
}
