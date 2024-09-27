package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.OverclockControllers;

import net.minecraft.client.renderer.texture.IIconRegister;

import com.Nxer.TwistSpaceTechnology.common.misc.OverclockType;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;

public class StaticOverclockController extends StaticOverclockControllerBase {

    public StaticOverclockController(int aID, String aName, String aNameRegional, int aTier, int timeReduction,
        int powerIncrease) {
        super(aID, aName, aNameRegional, aTier);
        this.overclockType = OverclockType.checkOverclockType(timeReduction, powerIncrease);
    }

    public StaticOverclockController(String aName, int aTier, OverclockType overclockType, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        this.overclockType = overclockType;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new StaticOverclockController(
            this.mName,
            this.mTier,
            this.overclockType,
            this.mDescriptionArray,
            this.mTextures);
    }

    protected final OverclockType overclockType;

    @Override
    public OverclockType getOverclockType() {
        return overclockType;
    }

    // region General

    // spotless:off
    protected String[] description;

    @Override
    public String[] getDescription() {
        if (description == null || description.length == 0) {
            description =
                new String[]{
                    // #tr Tooltips.StaticOverclockController.01
                    // # The overclock controller module determines the machine overclocking efficiency.
                    // #zh_CN 决定机器超频效率的超频控制器模块.
                    TextEnums.tr("Tooltips.StaticOverclockController.01"),
                    getOverclockType().getDescription(),
                    // #tr Tooltips.StaticOverclockController.03
                    // # Each machine can only install one overclock controller.
                    // #zh_CN 每台机器只可安装一个超频控制器.
                    TextEnums.tr("Tooltips.StaticOverclockController.03"),
                    TextEnums.AddByTwistSpaceTechnology.getText(),
                    TextEnums.ModularizedMachineSystem.getText(),
                };
        }
        return description;
    }
    // spotless:on

    // region Texture
    protected static Textures.BlockIcons.CustomIcon ActiveFaceLowSpeed;
    protected static Textures.BlockIcons.CustomIcon ActiveFacePerfect;
    protected static Textures.BlockIcons.CustomIcon ActiveFaceSingularity;

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ActiveFaceLowSpeed = new Textures.BlockIcons.CustomIcon(
            "gtnhcommunitymod:ModularHatchOverlay/OVERLAY_OverClockController_Pri");
        ActiveFacePerfect = new Textures.BlockIcons.CustomIcon(
            "gtnhcommunitymod:ModularHatchOverlay/OVERLAY_OverClockController_Adv");
        ActiveFaceSingularity = new Textures.BlockIcons.CustomIcon(
            "gtnhcommunitymod:ModularHatchOverlay/OVERLAY_OverClockController_Per");
        super.registerIcons(aBlockIconRegister);
    }

    protected ITexture overlay;

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        if (null == overlay) {
            overlay = switch (overclockType) {
                case PerfectOverclock -> TextureFactory.of(ActiveFacePerfect);
                case SingularityPerfectOverclock -> TextureFactory.of(ActiveFaceSingularity);
                default -> TextureFactory.of(ActiveFaceLowSpeed);
            };
        }
        return new ITexture[] { aBaseTexture, overlay };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return getTexturesActive(aBaseTexture);
    }

    // endregion
}
