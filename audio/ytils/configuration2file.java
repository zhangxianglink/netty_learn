package audio.ytils;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.ConfigurationBuilderEvent;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.event.EventListener;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.reloading.PeriodicReloadingTrigger;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * x.z
 * Create in 2023/3/22
 */
public class configuration2file {
    public static void main(String[] args) throws InterruptedException, ConfigurationException {
        Parameters params = new Parameters();
        ReloadingFileBasedConfigurationBuilder<PropertiesConfiguration> builder =
                new ReloadingFileBasedConfigurationBuilder<PropertiesConfiguration>(PropertiesConfiguration.class)
                        .configure(params.fileBased()
                                .setFile(new File("C:\\Users\\admin\\Desktop\\test\\config\\ip.properties")));
        PeriodicReloadingTrigger trigger = new PeriodicReloadingTrigger(builder.getReloadingController(),
                null, 1, TimeUnit.SECONDS);
        trigger.start();

        builder.addEventListener(ConfigurationBuilderEvent.ANY, new EventListener<ConfigurationBuilderEvent>() {

            public void onEvent(ConfigurationBuilderEvent event) {
                System.out.println("Event:" + event);
            }
        });

        while (true) {
            Thread.sleep(1000);
            System.out.println(builder.getConfiguration().getString("whiteList"));
        }
    }
}
