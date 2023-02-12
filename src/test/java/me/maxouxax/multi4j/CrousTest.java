package me.maxouxax.multi4j;

import me.maxouxax.multi4j.crous.RestoCrous;
import me.maxouxax.multi4j.exceptions.MultiLoginException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

public class CrousTest {

    @Test
    public void testCrous() throws MultiLoginException, IOException, URISyntaxException, InterruptedException {
        MultiConfig config = new MultiConfig();
        config.setUsername("id");
        config.setPassword("mdp");
        MultiClient.Builder bd = new MultiClient.Builder();
        MultiClient mc = bd.build();
        mc.setMultiConfig(config);
        RestoCrous c = MultiHelper.getMenuRu(mc, "resto u name");
        Assertions.assertNotNull(c);
    }
}
