package p455w0rd.danknull.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import p455w0rd.danknull.DankNull;
import p455w0rd.danknull.blocks.tiles.TileDankNullDock;
import p455w0rd.danknull.container.ContainerDankNullItem;

import javax.annotation.Nonnull;

/**
 * @author p455w0rd
 */
public class PacketEmptyDock implements IMessage {

    private BlockPos pos;

    public PacketEmptyDock() {
    }

    public PacketEmptyDock(@Nonnull final BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        final int x = buf.readInt();
        final int y = buf.readInt();
        final int z = buf.readInt();
        pos = new BlockPos(x, y, z);
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    public static class Handler implements IMessageHandler<PacketEmptyDock, IMessage> {
        @Override
        public IMessage onMessage(final PacketEmptyDock message, final MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                EntityPlayerSP player = Minecraft.getMinecraft().player;
                if (player.openContainer instanceof ContainerDankNullItem) {
                    player.closeScreen();
                }
                final World world = player.getEntityWorld();
                final TileEntity te = world.getTileEntity(message.pos);
                if (te instanceof TileDankNullDock) {
                    ((TileDankNullDock) te).removeDankNull();
                }
            });
            return null;
        }
    }

}
