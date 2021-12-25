package bplusdevelopment.addons;

import bplusdevelopment.utils.BPlusDamageUtils;
import bplusdevelopment.utils.Loader;
import bplusdevelopment.utils.Loading;
import bplusdevelopment.utils.Loadinqq;
import bplusdevelopment.utils.bussing.Addinqq;
import bplusdevelopment.utils.bussing.Hidinqq;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddModule extends MeteorAddon {
    public static final Logger LOG = LogManager.getLogger();
    public static final Category BANANAPLUS = new Category("Banana+ Combat");
    public static final Category BANANAMINUS = new Category("Banana+ Misc");

    @Override
    public void onInitialize() {
        LOG.info("Initializing Banana+ Addon");
        Addinqq.addinqq();
        Hidinqq.hidinqq();
        Loader.load();
        Loading.loading();
        Loadinqq.loadinqq();
        BPlusDamageUtils.init();
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(BANANAPLUS);
        Modules.registerCategory(BANANAMINUS);
    }
}
