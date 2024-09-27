package com.Nxer.TwistSpaceTechnology.common.ship;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.Nxer.TwistSpaceTechnology.common.ship.component.ShipComponent;
import com.Nxer.TwistSpaceTechnology.common.ship.system.ControlSystem;
import com.Nxer.TwistSpaceTechnology.common.ship.system.EnergySystem;
import com.Nxer.TwistSpaceTechnology.common.ship.system.PropulsionSystem;
import com.Nxer.TwistSpaceTechnology.common.ship.system.RadarSystem;
import com.Nxer.TwistSpaceTechnology.common.ship.system.ShieldSystem;
import com.Nxer.TwistSpaceTechnology.common.ship.system.WeaponSystem;

import gregtech.api.util.GTUtility;

public class Ship extends EntityMob implements Runnable {

    public EntityPlayer owner;
    public World world;
    public String className;
    public String shipName;
    public Integer length = 0, height = 0, width = 0;
    public Integer dockyard = -1;
    public List<Integer> validDimId = new ArrayList<>();
    public boolean forceTravel = false;
    public static final Integer maxLength = 64, maxHeight = 64, maxWidth = 64;
    public List<ShipComponent> structure = new ArrayList<>(maxHeight * maxLength * maxWidth);
    public PropulsionSystem propulsionSystem = new PropulsionSystem();
    public EnergySystem energySystem = new EnergySystem();
    public ControlSystem controlSystem = new ControlSystem();
    public WeaponSystem weaponSystem = new WeaponSystem();
    public ShieldSystem shieldSystem = new ShieldSystem();
    public RadarSystem radarSystem = new RadarSystem();

    public Ship(World worldIn) {
        super(worldIn);
        this.world = worldIn;
        init();
    }

    public void init() {

    }

    @Override
    public void travelToDimension(int dimensionId) {
        if (!validDimId.contains(dimensionId) && !forceTravel) {
            GTUtility.sendChatToPlayer(
                owner,
                "your ship :" + shipName + "is trying to travel to a dimension which can destroy it immediately");
        }
        super.travelToDimension(dimensionId);
        if (forceTravel) {
            setDead();
        }
        var t = new TileEntity();
    }

    public ShipComponent getComponent(int x, int y, int z) {
        return structure.get(getIndex(x, y, z));
    }

    public int getIndex(int x, int y, int z) {
        return x * length * height + y * length + z;
    }

    public void setComponent(int x, int y, int z, ShipComponent shipComponent) {
        shipComponent.setPosition(x, y, z);
        structure.set(getIndex(x, y, z), shipComponent);
    }

    public boolean checkComponentStatus() {
        // TODO
        return reConstructSystems();
    }

    public boolean reConstructSystems() {
        // TODO
        return true;
    }

    // return false if control system have no energy
    public boolean checkEnergy() {
        return true;
    }

    public void charge() {

    }

    public void runEnergy() {

    }

    public void addBulletForWeapons() {

    }

    public void addFuelForEngine() {

    }

    public void move() {

    }

    public void openFire() {

    }

    public void damageShield(double startX, double startY, double startZ, double radio, double damage) {
        int cnt = 0;
        for (var shield : shieldSystem.shields) {
            if (radio >= shield.distanceTo(startX, startY, startZ)) {
                cnt++;
            }
        }
        for (var shield : shieldSystem.shields) {
            if (radio >= shield.distanceTo(startX, startY, startZ)) {
                if (shield.isNumberShield) {
                    shield.shieldPoint -= 1;
                    cnt--;
                    damage -= damage / cnt;
                } else {
                    var damaged = Math.min(damage / cnt, shield.shieldPoint);
                    shield.shieldPoint -= damage / cnt;
                    cnt--;
                    damage -= damaged;
                }
            }
        }
        if (damage > 0.1) {
            damage(startX, startY, startZ, radio, damage);
        }
    }

    public void damage(double startX, double startY, double startZ, double radio, double damage) {

    }

    @Override
    protected void entityInit() {
        super.entityInit();
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompound) {
        super.readEntityFromNBT(tagCompound);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
    }

    @Override
    public String toString() {
        return "MyShip";
    }

    @Override
    public void run() {
        while (checkComponentStatus()) {

            long time = System.currentTimeMillis();
            charge();
            if (checkEnergy()) {
                continue;
            }
            runEnergy();
            addBulletForWeapons();
            addFuelForEngine();
            move();
            openFire();// !!
            try {
                wait(time + 50 - System.currentTimeMillis());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
        setDead();
    }
}
