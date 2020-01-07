package me.egg82.ae.services.skin;

import java.util.UUID;
import org.bukkit.inventory.ItemStack;
import org.mineskin.data.Skin;
import org.mineskin.data.SkinData;
import org.mineskin.data.Texture;

public interface SkinInfo {
    UUID getUUID();
    Skin getSkin();

    ItemStack getSkull();
    ItemStack getSkull(int amount);

    default Skin getDefaultSkin() {
        Skin retVal = new Skin();
        retVal.id = 214324;
        retVal.name = "";
        retVal.data = new SkinData();
        retVal.data.uuid = UUID.fromString("8667ba71-b85a-4004-af54-457a9734eed7"); // Steve
        retVal.data.texture = new Texture();
        retVal.data.texture.value = "eyJ0aW1lc3RhbXAiOjE1MjY5MzE4NzU2MjAsInByb2ZpbGVJZCI6Ijg2NjdiYTcxYjg1YTQwMDRhZjU0NDU3YTk3MzRlZWQ3IiwicHJvZmlsZU5hbWUiOiJTdGV2ZSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGMxYzc3Y2U4ZTU0OTI1YWI1ODEyNTQ0NmVjNTNiMGNkZDNkMGNhM2RiMjczZWI5MDhkNTQ4Mjc4N2VmNDAxNiJ9LCJDQVBFIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjc2N2Q0ODMyNWVhNTMyNDU2MTQwNmI4YzgyYWJiZDRlMjc1NWYxMTE1M2NkODVhYjA1NDVjYzIifX19";
        retVal.data.texture.signature = "Fc4JsPPtXUVjHCQt09enmbAvL5MN04WgoWMK5xaH6GDZdu9RN1ky3kQ9xOa9z/ARGzo3tiFZ9rwfZ8/Yz5lSwsJabNJybpSi+r1eFwKB2cc1h6Nx8sU6TXu4YhDkwC/fgoeNeHlluN4IaSMU+wTKK2mr0KO0EbTKxksGS4Okn5owAM10opW4IGsGTKEZ/4HvmEnt/8f4KZ2xiTXk35Bfw46d96gEu3REP0g6UhMJoOHVM5/+KRjkdCvTbXonUVy2KnyfHWvppnVBASp1Qf9m3zMWb3kT4qQ2te+Pz2sctNq5YqXuHx9gHg8eJ8ERKvSdVfG+rDR+4Q6dbYsCE0oh8eAgKm12CdI69IOQRl0DJmZn4lafi1wRsYB8Q1CBrLywW7iWX8k3oXEkVrzUCHEQFYQBV4xC6MthhJ5cRtjS/4vzWywE6yur+dQLulWKqzc5oCarpwAorGzdX4/KF1CXfzet0WZCgEbaM0xQr8a4vzk9K9KJg80kdVfU2M1+RIeFXBrypYOH0XYX94L+avfSGOFawilJdVJNETMU6Slvb3pgPVfed6DGffHAfG53bDN6RzVS1hqY0M3T9iDPG687ejrlK3cefL9uKn0F6qebF0ufNmGbJQkTDeLJ03JTxgTbqFYo4CLbni6TrjUR0LB08xB/pEZdgwoH/OVHYxzYZG4=";
        retVal.data.texture.url = "http://textures.minecraft.net/texture/dc1c77ce8e54925ab58125446ec53b0cdd3d0ca3db273eb908d5482787ef4016";
        retVal.timestamp = System.currentTimeMillis();
        retVal.prvate = false;
        retVal.views = 1;
        retVal.nextRequest = 0.0d;
        return retVal;
    }
}
