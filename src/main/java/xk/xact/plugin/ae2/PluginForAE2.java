package xk.xact.plugin.ae2;

import appeng.api.storage.IStorageMonitorable;
import cpw.mods.fml.common.Loader;
import xk.xact.api.plugin.XACTPlugin;
import xk.xact.plugin.PluginManager;
import xk.xact.plugin.ae2.inventory.AEInventory;
import xk.xact.util.Utils;

/**
 * Created by universallp on 03.01.2016 23:20.
 */
public class PluginForAE2 implements XACTPlugin {

    @Override
    public void initialize() {
        if(Loader.isModLoaded("appliedenergistics2")) {
            Utils.log( "Applied Energistics mod detected. Initializing plug-in...");
            PluginManager.registerInventoryAdapter(IStorageMonitorable.class, new AEInventory.Provider());
        } else {
            Utils.log( "Applied Energistics mod not detected. Plug-in not initialized.");
        }
    }
}
