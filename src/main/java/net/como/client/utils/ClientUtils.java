package net.como.client.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mojang.authlib.minecraft.client.ObjectMapper;

import joptsimple.internal.Strings;
import net.como.client.ComoClient;
import net.como.client.interfaces.mixin.IEntity;
import net.como.client.utils.RotationUtils.Rotation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.Window;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.Language;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.GameMode;

public class ClientUtils {
    public static boolean isInStorage() {
        Screen screen = ComoClient.getClient().currentScreen;
        if (screen == null) return false;

        switch (screen.getClass().getSimpleName()) {
            case "ShulkerBoxScreen":
            case "GenericContainerScreen": return true;
            default: return false;
        }
    
    }

    public static boolean hasElytraEquipt() {
        ItemStack chestSlot = ComoClient.me().getEquippedStack(EquipmentSlot.CHEST);
		return (chestSlot.getItem() == Items.ELYTRA);
    }

    public static void applyRotation(Rotation rot) {
        ComoClient.me().setYaw((float)rot.yaw);
        ComoClient.me().setPitch((float)rot.pitch);
    }

    public static void lookAtPos(Vec3d pos) {
        applyRotation(
            RotationUtils.getRequiredRotation(pos)
        );
    }

    public static void hitEntity(Entity target) {
        ComoClient.getClient().interactionManager.attackEntity(ComoClient.me(), target);
        ComoClient.me().swingHand(Hand.MAIN_HAND);
    }

    public static Rotation getRotation() {
        return new Rotation(
            (double)ComoClient.me().getYaw(),
            (double)ComoClient.me().getPitch()
        );
    }

    public static void sendPos(double x, double y, double z, boolean onGround) {
        ComoClient.me().networkHandler.sendPacket(
            new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, onGround)
        );
    }

    public static void sendPos(Vec3d pos, boolean onGround) {
        sendPos(pos.x, pos.y, pos.z, onGround);
    }

    public static Boolean inGame() {
        return ComoClient.me() != null && ComoClient.getClient().getNetworkHandler() != null;
    }

    public static Boolean isInNetherPortal() {
        IEntity me = (IEntity)ComoClient.me();
        
        return me.getInNetherPortal();
    }

    public static Boolean isThirdperson() {
        return ComoClient.getClient().gameRenderer.getCamera().isThirdPerson();
    }

    public static void openChatScreen(String text) {
        if (!inGame()) return;

        ComoClient.getClient().setScreen(new ChatScreen(text));
    }

    public static void openChatScreen() {
        openChatScreen("");
    }

    public static ItemStack getHandlerSlot(int i) {
        return ComoClient.me().currentScreenHandler.getSlot(i).getStack();
    }

    public static Integer getPing() {
        ClientPlayNetworkHandler lv = ComoClient.me().networkHandler;
        PlayerListEntry entry = lv.getPlayerListEntry(ComoClient.me().getUuid());
        
        if (entry == null) return 0;

        return entry.getLatency();
    }

    public static String getGameModeName() {
        if (ComoClient.me().isSpectator()) return "Spectator";
        if (ComoClient.me().isCreative()) return "Creative";

        return "Survival";
    }

    public static Vec3d getControlVelocity(Entity ent, Double speed, Boolean allowFlight) {
        // Initialize as still, or somewhat still.
        Vec3d velocity = new Vec3d(0, allowFlight ? 0 : ent.getVelocity().getY(), 0);

        // We only need these two velocities since the other you can calculate just by multiplying these out by -1 :P
        Vec3d forward = MathsUtils.getForwardVelocity(ent);
        Vec3d right   = MathsUtils.getRightVelocity(ent);

        // Forward + Back
        if (ComoClient.me().input.pressingForward) velocity = velocity.add(forward.multiply(new Vec3d(speed, 0, speed)));
        if (ComoClient.me().input.pressingBack)    velocity = velocity.add(forward.multiply(new Vec3d(-speed, 0, -speed)));

        // Right + Left
        if (ComoClient.me().input.pressingRight) velocity = velocity.add(right.multiply(new Vec3d(speed, 0, speed)));
        if (ComoClient.me().input.pressingLeft)  velocity = velocity.add(right.multiply(new Vec3d(-speed, 0, -speed)));

        if (allowFlight) {
            // Up + Down
            if (ComoClient.me().input.jumping)  velocity = velocity.add(0, speed, 0);
            if (ComoClient.me().input.sneaking) velocity = velocity.add(0, -speed, 0);
        }

        // Set the velocity
        return velocity;
    }

    public static void entitySpeedControl(Entity ent, Double speed, Boolean allowFlight) {
        // Set the velocity
        ent.setVelocity(getControlVelocity(ent, speed, allowFlight));
    }

    public static Vec2f getMousePosition() {
        MinecraftClient client = ComoClient.getClient();
        Mouse mouse = client.mouse;
        Window window = client.getWindow();

        if (mouse.isCursorLocked()) return new Vec2f(window.getScaledWidth()/2, window.getScaledHeight()/2);

        double scaleFactor = window.getScaleFactor();
        int posX = (int)(mouse.getX()/scaleFactor);
        int posY = (int)(mouse.getY()/scaleFactor);

        return new Vec2f(posX, posY);
    }

    public static String getTextString(Text text) {
        if (text instanceof TranslatableText) {
            Language language = Language.getInstance();

            TranslatableText transText = (TranslatableText)text;

            String str = language.get(transText.getKey());
            return str.equals("%s") ? transText.asString() : str;
        }

        return text.asString();
    }

    /**
     * Checks to see if an entity can see a given position
     * @param entity
     */
    public static boolean canSee(Entity ent, Vec3d pos, float tickDelta) {
        return ent.world.raycast(
            new RaycastContext(
                ent.getCameraPosVec(tickDelta), // My position
                pos, // The position to check
                RaycastContext.ShapeType.COLLIDER, // The shape type
                RaycastContext.FluidHandling.NONE, // The fluid handling
                ent // The entity that is doing the checking
            )
        ).getType() == HitResult.Type.MISS;
    }

    /**
     * Checks to see if an entity can see a given position
     * @param entity
     */
    public static boolean canSee(Entity ent, Vec3d pos) {
        return canSee(ent, pos, 1);
    }

    /**
     * Checks to see if an entity can see a given position
     * @param entity
     */
    public static boolean canSee(Vec3d pos) {
        return canSee(ComoClient.me(), pos, 1);
    }

    public static void disconnect(Screen prev) {
        MinecraftClient client = ComoClient.getClient();
        
        client.world.disconnect();
        client.setScreen(prev);
    }

    public static void openInventory() {
        ComoClient.getClient().setScreen(new InventoryScreen(ComoClient.me()));
    }

    public static void refreshInventory() {
        MinecraftClient client = ComoClient.getClient();

        Boolean wasMouseLocked = client.mouse.isCursorLocked();

        ClientUtils.openInventory();
        client.currentScreen = null;
        
        if (wasMouseLocked) client.mouse.lockCursor();
    }

    /**
     * 
     * @return the username of the player
     */
    public static String getUsername() {
        return ComoClient.me().getName().asString();
    }

    private static HashMap<UUID, String> usernames = new HashMap<>();
    private static class UsernameResponse {
        String name;
    }
    public static String getPlayerUsername(UUID uuid) {
        if (usernames.containsKey(uuid)) return usernames.get(uuid);

        PlayerEntity player = ComoClient.getClient().world.getPlayerByUuid(uuid);
        if (player != null) {
            String name = player.getEntityName();

            usernames.put(uuid, name);
            return name;
        }

        usernames.put(uuid, "N/A");

        // Pain.
        Thread thread = new Thread(() -> {
            String data = WebUtils.getJSON("https://api.mojang.com/user/profiles/" + uuid.toString().replace("-", "") + "/names", 60000);

            Gson gson = new Gson();
            ArrayList<UsernameResponse> d = gson.fromJson(data, new TypeToken<ArrayList<UsernameResponse>>() {}.getType());

            for (UsernameResponse u : d) {
                usernames.put(uuid, u.name);
                return;
            }
        });
        thread.start();

        return usernames.get(uuid);
    }

    public static GameMode getGameMode() {
        // Get the localplayer in the tab list
        PlayerListEntry playerListEntry = ComoClient.getClient().getNetworkHandler().getPlayerListEntry(ComoClient.me().getGameProfile().getId());

        return playerListEntry.getGameMode();
    }

    public static int getHealth(LivingEntity ent) {
        return (int)Math.ceil(ent.getHealth());
    }
}
