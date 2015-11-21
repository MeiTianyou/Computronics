package pl.asie.computronics.tile;

import gnu.trove.set.hash.TIntHashSet;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import pl.asie.computronics.audio.AudioPacket;
import pl.asie.computronics.audio.IAudioReceiver;
import pl.asie.computronics.audio.IAudioSource;
import pl.asie.computronics.util.ColorUtils;
import pl.asie.computronics.util.internal.IColorable;
import pl.asie.lib.block.TileEntityBase;

public class TileAudioCable extends TileEntityBase implements IAudioReceiver, IColorable {
	private final TIntHashSet packetIds = new TIntHashSet();

	private int connectionMap = 0;
	private boolean initialConnect = false;

	private void updateConnections() {
		connectionMap = 0;
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			if(worldObj.blockExists(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ)) {
				TileEntity tile = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
				if(tile instanceof IAudioSource || tile instanceof IAudioReceiver) {
					if(tile instanceof IColorable && ((IColorable) tile).canBeColored()
						&& !ColorUtils.isSameOrDefault(this, (IColorable) tile)) {
						continue;
					}
					connectionMap |= 1 << dir.ordinal();
				}
			}
		}
	}

	public boolean connects(ForgeDirection dir) {
		if(!initialConnect) {
			updateConnections();
			initialConnect = true;
		}
		return ((connectionMap >> dir.ordinal()) & 1) == 1;
	}

	@Override
	public void updateEntity() {
		packetIds.clear();
		updateConnections();
	}

	@Override
	public void receivePacket(AudioPacket packet, ForgeDirection side) {
		if(packetIds.contains(packet.id)) {
			return;
		}

		packetIds.add(packet.id);
		for(int i = 0; i < 6; i++) {
			ForgeDirection dir = ForgeDirection.getOrientation(i);
			if(dir == side) {
				continue;
			}

			if(!worldObj.blockExists(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ)) {
				continue;
			}

			TileEntity tile = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
			if(tile instanceof IAudioReceiver && connects(dir)) {
				((IAudioReceiver) tile).receivePacket(packet, dir.getOpposite());
			}
		}
	}

	@Override
	public World getSoundWorld() {
		return null;
	}

	@Override
	public int getSoundX() {
		return 0;
	}

	@Override
	public int getSoundY() {
		return 0;
	}

	@Override
	public int getSoundZ() {
		return 0;
	}

	@Override
	public int getSoundDistance() {
		return 0;
	}

	protected int overlayColor = getDefaultColor();

	@Override
	public boolean canBeColored() {
		return true;
	}

	@Override
	public int getColor() {
		return overlayColor;
	}

	@Override
	public int getDefaultColor() {
		return ColorUtils.Color.LightGray.color;
	}

	@Override
	public void setColor(int color) {
		this.overlayColor = color;
		this.markDirty();
	}

	@Override
	public void readFromRemoteNBT(NBTTagCompound nbt) {
		super.readFromRemoteNBT(nbt);
		int oldColor = this.overlayColor;
		if(nbt.hasKey("computronics:color")) {
			overlayColor = nbt.getInteger("computronics:color");
		}
		if(this.overlayColor < 0) {
			this.overlayColor = getDefaultColor();
		}
		if(oldColor != this.overlayColor) {
			this.worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
		}
	}

	@Override
	public void writeToRemoteNBT(NBTTagCompound nbt) {
		super.writeToRemoteNBT(nbt);
		if(overlayColor != getDefaultColor()) {
			nbt.setInteger("computronics:color", overlayColor);
		}
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if(nbt.hasKey("computronics:color")) {
			overlayColor = nbt.getInteger("computronics:color");
		}
		if(this.overlayColor < 0) {
			this.overlayColor = getDefaultColor();
		}
	}

	@Override
	public void writeToNBT(final NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		if(overlayColor != getDefaultColor()) {
			nbt.setInteger("computronics:color", overlayColor);
		}
	}
}