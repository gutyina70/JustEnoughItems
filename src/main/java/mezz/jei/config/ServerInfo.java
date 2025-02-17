package mezz.jei.config;

import javax.annotation.Nullable;

import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;

public final class ServerInfo {
	private static boolean jeiOnServer = false;
	@Nullable
	private static String worldUid = null;

	private ServerInfo() {

	}

	public static boolean isJeiOnServer() {
		return jeiOnServer;
	}

	public static void onConnectedToServer(boolean jeiOnServer) {
		ServerInfo.jeiOnServer = jeiOnServer;
		ServerInfo.worldUid = null;
	}

	public static String getWorldUid(@Nullable Connection networkManager) {
		if (worldUid == null) {
			if (networkManager == null) {
				worldUid = "default"; // we get here when opening the in-game config before loading a world
			} else if (networkManager.isMemoryConnection()) {
				MinecraftServer minecraftServer = ServerLifecycleHooks.getCurrentServer();
				if (minecraftServer != null) {
					worldUid = minecraftServer.storageSource.getLevelId();
				}
			} else {
				ServerData serverData = Minecraft.getInstance().getCurrentServer();
				if (serverData != null) {
					worldUid = serverData.ip + ' ' + serverData.name;
				}
			}

			if (worldUid == null) {
				worldUid = "default";
			}
			worldUid = "world" + worldUid.hashCode();
		}
		return worldUid;
	}
}
