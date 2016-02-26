package pl.asie.computronics.integration.railcraft.signalling;

import mods.railcraft.api.core.WorldCoordinate;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.api.signals.SignalController;
import mods.railcraft.api.signals.SignalReceiver;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;
import pl.asie.computronics.util.collect.SimpleInvertibleDualMap;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Vexatos
 */
public class MassiveSignalController extends SignalController {

	private boolean needsInit;
	private final Map<WorldCoordinate, SignalAspect> aspects = new HashMap<WorldCoordinate, SignalAspect>();
	private final SimpleInvertibleDualMap<String, WorldCoordinate> signalNames = SimpleInvertibleDualMap.create();

	public MassiveSignalController(String locTag, TileEntity tile) {
		super(locTag, tile, 32);
		this.needsInit = true;

	}

	@Override
	public SignalAspect getAspectFor(WorldCoordinate coord) {
		return this.aspects.get(coord);
	}

	public void setAspectFor(WorldCoordinate coord, SignalAspect aspect) {
		if(this.aspects.get(coord) != aspect) {
			this.aspects.put(coord, aspect);
			this.updateReceiver(coord);
		}
	}

	@Override
	public void tickServer() {
		super.tickServer();
		if(this.needsInit) {
			this.needsInit = false;
			this.updateReceivers();
		}
	}

	private void updateReceiver(WorldCoordinate coord, SignalReceiver receiver) {
		SignalAspect aspect = this.aspects.get(coord);
		if(receiver != null && aspect != null) {
			receiver.onControllerAspectChange(this, aspect);
		}
	}

	private void updateReceiver(WorldCoordinate coord) {
		SignalReceiver receiver = this.getReceiverAt(coord);
		if(receiver != null) {
			updateReceiver(coord, receiver);
		}
	}

	private void updateReceivers() {
		for(WorldCoordinate coord : this.getPairs()) {
			updateReceiver(coord);
		}
	}

	@Override
	public void cleanPairings() {
		super.cleanPairings();
		this.aspects.keySet().retainAll(getPairs());
	}

	protected void saveNBT(NBTTagCompound data) {
		super.saveNBT(data);
		NBTTagList list = new NBTTagList();

		for(Map.Entry<WorldCoordinate, SignalAspect> entry : this.aspects.entrySet()) {
			NBTTagCompound tag = new NBTTagCompound();
			WorldCoordinate key = entry.getKey();
			tag.setIntArray("coords", new int[] { key.dimension, key.x, key.y, key.z });
			tag.setByte("aspect", (byte) entry.getValue().ordinal());
			String s = signalNames.inverse().get(key);
			if(s != null) {
				tag.setString("name", s);
			}
			list.appendTag(tag);
		}
		data.setTag("aspects", list);
	}

	protected void loadNBT(NBTTagCompound data) {
		super.loadNBT(data);
		NBTTagList list = data.getTagList("aspects", Constants.NBT.TAG_COMPOUND);

		for(byte entry = 0; entry < list.tagCount(); ++entry) {
			NBTTagCompound tag = list.getCompoundTagAt(entry);
			int[] c = tag.getIntArray("coords");
			WorldCoordinate coord = new WorldCoordinate(c[0], c[1], c[2], c[3]);
			this.aspects.put(coord, SignalAspect.fromOrdinal(data.getByte("aspect")));
			if(tag.hasKey("name")) {
				signalNames.put(tag.getString("name"), coord);
			}
		}
	}
}
