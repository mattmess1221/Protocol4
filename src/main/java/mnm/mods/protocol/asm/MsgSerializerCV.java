package mnm.mods.protocol.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MsgSerializerCV extends ClassVisitor {

    public MsgSerializerCV(ClassWriter cw) {
        super(Opcodes.ASM5, cw);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        int encode = PacketIOTransformer.getEnv(PacketObf.encode, name);
        // ensure name and desc are correct
        if (encode != -1) {
            String desc2 = PacketIOTransformer.buildDescriptor(encode, "V",
                    "io.netty.channel.ChannelHandlerContext", PacketObf.Packet, "io.netty.buffer.ByteBuf");
            if (desc.equals(desc2)) {
                mv = new EncodeMV(mv);
            }
        }
        return mv;
    }

    private class EncodeMV extends MethodVisitor {

        public EncodeMV(MethodVisitor mv) {
            super(Opcodes.ASM5, mv);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            super.visitMethodInsn(opcode, owner, name, desc, itf);

            // inject after
            int pkt = PacketIOTransformer.getEnv(PacketObf.Packet, owner);
            int write = PacketIOTransformer.getEnv(PacketObf.writePacketData, name);
            if (write != -1 && pkt != -1 && itf) {
                String newdesc = PacketIOTransformer
                        .buildDescriptor(pkt, "V", PacketObf.Packet, PacketObf.PacketBuffer);

                visitVarInsn(Opcodes.ALOAD, 2);
                visitVarInsn(Opcodes.ALOAD, 5);
                visitMethodInsn(Opcodes.INVOKESTATIC, "mnm/mods/protocol/PacketIO", "writePacketData", newdesc, false);
            }
        }
    }

}
