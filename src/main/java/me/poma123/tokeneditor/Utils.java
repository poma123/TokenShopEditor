package me.poma123.tokeneditor;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Utils {

    public static String toBase64(Inventory paramInventory) {
        return toBase64(paramInventory.getContents());
    }

    public static String toBase64(ItemStack paramItemStack) {
        ItemStack[] arrayOfItemStack = new ItemStack[1];
        arrayOfItemStack[0] = paramItemStack;
        return toBase64(arrayOfItemStack);
    }

    public static String toBase64(List<ItemStack> paramList) {
        return toBase64((ItemStack[]) paramList.toArray(new ItemStack[paramList.size()]));
    }

    public static List<ItemStack> stacksFromData(String paramString) throws IOException {

        return Arrays.asList(stacksFromBase64(paramString));


    }

    public static String toBase64(ItemStack[] paramArrayOfItemStack) {
        try {
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream localBukkitObjectOutputStream = new BukkitObjectOutputStream(localByteArrayOutputStream);

            localBukkitObjectOutputStream.writeInt(paramArrayOfItemStack.length);

            ItemStack[] arrayOfItemStack = paramArrayOfItemStack;
            int j = paramArrayOfItemStack.length;
            for (int i = 0; i < j; i++) {
                ItemStack localItemStack = arrayOfItemStack[i];
                localBukkitObjectOutputStream.writeObject(localItemStack);
            }
            localBukkitObjectOutputStream.close();
            return Base64Coder.encodeLines(localByteArrayOutputStream.toByteArray());
        } catch (Exception localException) {
            throw new IllegalStateException("Unable to save item stacks.", localException);
        }
    }

    public static Inventory inventoryFromBase64(String paramString) throws IOException {
        ItemStack[] arrayOfItemStack = stacksFromBase64(paramString);
        Inventory localInventory = Bukkit.createInventory(null, (int) Math.ceil(arrayOfItemStack.length / 9.0D) * 9);
        for (int i = 0; i < arrayOfItemStack.length; i++) {
            localInventory.setItem(i, arrayOfItemStack[i]);
        }
        return localInventory;
    }

    public static ItemStack[] stacksFromBase64(String paramString) throws IOException {
        try {
            if ((paramString == null) || (Base64Coder.decodeLines(paramString) == null)) {
                return new ItemStack[0];
            }
            ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(
                    Base64Coder.decodeLines(paramString));

            BukkitObjectInputStream localBukkitObjectInputStream = new BukkitObjectInputStream(
                    localByteArrayInputStream);
            ItemStack[] arrayOfItemStack = new ItemStack[localBukkitObjectInputStream.readInt()];
            for (int i = 0; i < arrayOfItemStack.length; i++) {
                arrayOfItemStack[i] = ((ItemStack) localBukkitObjectInputStream.readObject());
            }
            localBukkitObjectInputStream.close();
            return arrayOfItemStack;
        } catch (ClassNotFoundException localClassNotFoundException) {
            throw new IOException("Unable to decode class type.", localClassNotFoundException);
        }
    }

    public static String toBase64(Collection<PotionEffect> paramCollection) {
        return toBase64((PotionEffect[]) paramCollection.toArray(new PotionEffect[paramCollection.size()]));
    }

   /* public static Collection<PotionEffect> effectsFromData(String paramString)
    {
        return Arrays.asList(effectsFromBase64(paramString));
    }*/

    public static String toBase64(PotionEffect[] paramArrayOfPotionEffect) {
        try {
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream localBukkitObjectOutputStream = new BukkitObjectOutputStream(localByteArrayOutputStream);

            localBukkitObjectOutputStream.writeInt(paramArrayOfPotionEffect.length);

            PotionEffect[] arrayOfPotionEffect = paramArrayOfPotionEffect;
            int j = paramArrayOfPotionEffect.length;
            for (int i = 0; i < j; i++) {
                PotionEffect localPotionEffect = arrayOfPotionEffect[i];
                localBukkitObjectOutputStream.writeObject(localPotionEffect);
            }
            localBukkitObjectOutputStream.close();
            return Base64Coder.encodeLines(localByteArrayOutputStream.toByteArray());
        } catch (Exception localException) {
            throw new IllegalStateException("Unable to save potion effects.", localException);
        }
    }

}
