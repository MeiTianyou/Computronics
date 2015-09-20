package pl.asie.computronics.integration.forestry.nanomachines;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeHousing;
import li.cil.oc.api.Nanomachines;
import li.cil.oc.api.nanomachines.Behavior;
import li.cil.oc.api.nanomachines.Controller;
import li.cil.oc.api.prefab.AbstractProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import pl.asie.computronics.integration.forestry.IntegrationForestry;
import pl.asie.lib.util.RayTracer;

import java.util.Collections;
import java.util.HashMap;

/**
 * @author Vexatos
 */
public class SwarmProvider extends AbstractProvider {

	public SwarmProvider() {
		super("computronics:forestry-swarmprovider");
	}

	@Override
	protected Behavior readBehaviorFromNBT(EntityPlayer player, NBTTagCompound nbt) {
		SwarmBehavior behavior = new SwarmBehavior(player);
		behavior.readFromNBT(nbt);
		return behavior;
	}

	protected void writeBehaviorToNBT(Behavior behavior, NBTTagCompound nbt) {
		if(behavior instanceof SwarmBehavior) {
			((SwarmBehavior) behavior).writeToNBT(nbt);
		}
	}

	@Override
	public Iterable<Behavior> createBehaviors(EntityPlayer player) {
		return Collections.<Behavior>singletonList(new SwarmBehavior(player));
	}

	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent e) {
		EntityPlayer player = e.entityPlayer;
		if(player != null && !player.worldObj.isRemote) {
			if((e.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR || e.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
				&& player.getHeldItem() != null && player.getHeldItem().getItem() == IntegrationForestry.itemStickImpregnated) {
				SwarmBehavior behavior = getSwarmBehavior(player);
				if(behavior != null) {
					if(behavior.entity != null) {
						RayTracer.instance().fire(player, 30);
						MovingObjectPosition target = RayTracer.instance().getTarget();
						if((target != null) && (target.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY)) {
							Entity entity = target.entityHit;
							if(entity != null && entity instanceof EntityLivingBase) {
								behavior.entity.setAttackTarget((EntityLivingBase) entity);
								swingItem(player);
							}
						} else if(behavior.entity.getAttackTarget() != null) {
							behavior.entity.setAttackTarget(null);
							swingItem(player);
						}
					} else {
						behavior.spawnNewEntity(player.posX, player.posY + 2f, player.posZ);
					}
				}
			} else if(e.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && player.getHeldItem() == null
				&& player.isSneaking() && e.world.blockExists(e.x, e.y, e.z)) {
				TileEntity te = e.world.getTileEntity(e.x, e.y, e.z);
				if(te instanceof IBeeHousing) {
					IBeeHousing tile = (IBeeHousing) te;
					// TODO make this use IBeekeepingLogic in Forestry 4
					if(tile.getQueen() != null) {
						IBee member = BeeManager.beeRoot.getMember(tile.getQueen());
						if(member != null && member.getCanWork(tile).size() <= 0 && member.hasFlower(tile)) {
							SwarmBehavior behavior = getSwarmBehavior(player);
							if(behavior != null) {
								if(behavior.entity != null) {
									behavior.entity.setDead();
								}
								behavior.spawnNewEntity(e.x + 0.5, e.y + 0.5, e.z + 0.5,
									BeeManager.beeRoot.getMember(tile.getQueen()).getGenome().getPrimary().getIconColour(0),
									member.getGenome().getTolerantFlyer());
							}
						}
					}
				}
			}
		}
	}

	private static void swingItem(EntityPlayer player) {
		player.swingItem();
		if(player instanceof EntityPlayerMP && ((EntityPlayerMP) player).playerNetServerHandler != null) {
			((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S0BPacketAnimation(player, 0));
		}
	}

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent e) {
		SwarmBehavior behavior = getSwarmBehavior(e.player);
		if(behavior != null && !e.player.worldObj.isRemote && e.phase == TickEvent.PlayerTickEvent.Phase.START) {
			behavior.update();
		}
	}

	private final HashMap<String, SwarmBehavior> behaviors = new HashMap<String, SwarmBehavior>();

	private SwarmBehavior getSwarmBehavior(EntityPlayer player) {
		Controller controller = Nanomachines.getController(player);
		if(controller != null) {
			Iterable<Behavior> behaviors = controller.getActiveBehaviors();
			for(Behavior behavior : behaviors) {
				if(behavior instanceof SwarmBehavior) {
					return (SwarmBehavior) behavior;
				}
			}
		}
		//return null

		// TODO remove
		SwarmBehavior behavior = behaviors.get(player.getCommandSenderName());
		if(behavior == null) {
			behavior = new SwarmBehavior(player);
			behavior.onEnable();
			behaviors.put(player.getCommandSenderName(), behavior);
		}
		return behavior;
	}

}