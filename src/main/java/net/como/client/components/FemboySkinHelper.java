package net.como.client.components;

import java.util.Random;
import java.util.UUID;

import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.util.Identifier;

public class FemboySkinHelper extends DefaultSkinHelper {
    private static class FemboySkin {
        public boolean isSlim;
        public Identifier id;

        public FemboySkin(Identifier id, boolean isSlim) {
            this.isSlim = isSlim;
            this.id = id;
        }

        public FemboySkin(Identifier id) {
            this(id, false);
        }
    }

    private static final FemboySkin[] FEMBOY_SKINS = {
        // Felix
        new FemboySkin(new Identifier("como-client:textures/entity/femboy/felix_1.png")),
        new FemboySkin(new Identifier("como-client:textures/entity/femboy/felix_2.png")),
        new FemboySkin(new Identifier("como-client:textures/entity/femboy/felix_3.png")),
        new FemboySkin(new Identifier("como-client:textures/entity/femboy/felix_4.png"))
    };

    private static final FemboySkin[] SLIM_FEMBOY_SKINS = {
        // Astolfo
        new FemboySkin(new Identifier("como-client:textures/entity/femboy/slim/astolfo_1.png"), true),
        new FemboySkin(new Identifier("como-client:textures/entity/femboy/slim/astolfo_2.png"), true),

        // My Fav Polish Gent <3
        new FemboySkin(new Identifier("como-client:textures/entity/femboy/slim/shm11.png"), true)
    };

    /**
     * A random slim femboy skin
     * @param random
     * @return random femboy skin from the 'slim' set
     */
    private static FemboySkin randomSlimSkin(Random random) {
        return SLIM_FEMBOY_SKINS[random.nextInt(0, SLIM_FEMBOY_SKINS.length)];
    }


    /**
     * A random default femboy skin
     * @param random
     * @return random femboy skin from the 'default' set
     */
    private static FemboySkin randomDefaultSkin(Random random) {
        return FEMBOY_SKINS[random.nextInt(0, SLIM_FEMBOY_SKINS.length)];
    }

    /**
     * Get default skin texture
     * @return default skin texture
     */
    public static Identifier getTexture() {
        return randomDefaultSkin(new Random()).id;
    }
    
    public static Random randomFromUuid(UUID uuid) {
        return new Random(uuid.getLeastSignificantBits());
    }

    /**
     * Get a random texture with a specific random object
     * @param uuid
     * @param random
     * @return random id
     */
    public static Identifier getTexture(UUID uuid, Random random) {
        FemboySkin skin;
        
        switch (DefaultSkinHelper.getModel(uuid)) {
            case "slim": {
                skin = randomSlimSkin(random);
            }
            default: {
                skin = randomDefaultSkin(random);
            }
        }

        return skin.id;
    }
}
