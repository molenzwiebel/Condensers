package nl.thijsmolendijk.Condensers;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class EMCConfig {
	private Main main;
	private FileConfiguration emcConfig = null;
	private File emcConfigFile = null;

	public EMCConfig(Main main) {
		this.main = main;
	}

	public float getEMC(ItemStack i) {
		if (i == null || i.getTypeId() == 0) return 0;
		if (i.hasItemMeta() && i.getItemMeta().hasDisplayName()) {
			if (i.getItemMeta().getDisplayName().equals(ChatColor.GOLD+"Energy Condenser"))
				return 106752;
		}
		String firstKey = i.getTypeId()+"-"+i.getDurability();
		String firstValue = emcConfig.getString(firstKey);
		if (firstValue == null) {
			String secondKey = i.getTypeId()+"-A";
			String secondValue = emcConfig.getString(secondKey);
			if (secondValue == null) {
				String thirdKey = i.getTypeId()+"-X";
				String thirdValue = emcConfig.getString(thirdKey);
				System.out.println(i.getDurability());
				System.out.println(this.getMaxDur(i));
				System.out.println(Float.valueOf(thirdValue));
				float value = (1 - (i.getDurability() / this.getMaxDur(i))) * Float.valueOf(thirdValue);
				if (thirdValue != null)
					return value;
				else
					return 0;
			}
			return Float.valueOf(secondValue);
		} else {
			return Float.valueOf(firstValue);
		}
	}
	


	public void loadEMCConfig() throws Exception {
		if (emcConfigFile == null) {
			//Create the File object if it doesn't exist
			emcConfigFile = new File(main.getDataFolder(), "emcConfig.yml");
		}
		if (!emcConfigFile.exists()){
			//Create the file if it doesn't exist
			emcConfigFile.createNewFile();	
		}
		emcConfig = YamlConfiguration.loadConfiguration(emcConfigFile);
		emcConfig.load(emcConfigFile);
		
		//Load the defaults
		YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(main.getResource("emcConfig.yml"));
		emcConfig.options().copyDefaults(true);
		emcConfig.setDefaults(defConfig);
		
		//Save and load once again
		emcConfig.save(emcConfigFile);
		emcConfig.load(emcConfigFile);
	}

	public float getMaxDur(ItemStack i) {
		switch (i.getTypeId()) {
		case 256: return 251.0F; //Iron Shovel
		case 257: return 251.0F; //Iron Pick
		case 258: return 251.0F; //Iron Axe
		case 259: return 65.0F; //Flint and Steel
		case 261: return 385.0F; //Bow
		case 267: return 251.0F; //Iron Sword
		case 268: return 60.0F; //Wooden Sword
		case 269: return 60.0F; //Wooden Shovel
		case 270: return 60.0F; //Wooden Pick
		case 271: return 60.0F; //Wooden Axe
		case 272: return 132.0F; //Stone Sword
		case 273: return 132.0F; //Stone Shove
		case 274: return 132.0F; //Stone Pick
		case 275: return 132.0F; //Stone Axe
		case 276: return 1562.0F; //Diamond Sword
		case 277: return 1562.0F; //Diamond Shovel
		case 278: return 1562.0F; //Diamond Pick
		case 279: return 1562.0F; //Diamond Axe
		case 283: return 33.0F; //Gold Sword
		case 284: return 33.0F; //Gold Shovel
		case 285: return 33.0F; //Gold Pick
		case 286: return 33.0F; //Gold Axe
		case 290: return 60.0F; //Wooden Hoe
		case 291: return 132.0F; //Stone Hoe
		case 292: return 251.0F; //Iron Hoe
		case 293: return 1562.0F; //Diamond Hoe
		case 294: return 33.0F; //Gold Hoe
		case 298: return 56.0F; //Leather Cap
		case 299: return 82.0F; //Leather Tunic
		case 300: return 76.0F; //Leather Pants
		case 301: return 66.0F; //Leather Boots
		case 306: return 166.0F; //Iron Helm
		case 307: return 242.0F; //Iron Chestplate
		case 308: return 226.0F; //Iron Leggings
		case 309: return 196.0F; //Iron Boots
		case 310: return 364.0F; //Diamond Helm
		case 311: return 529.0F; //Diamond Chestplate
		case 312: return 496.0F; //Diamond Pants
		case 313: return 430.0F; //Diamond Boots
		case 314: return 78.0F; //Golden Helm
		case 315: return 114.0F; //Golden Chestplate
		case 316: return 106.0F; //Golden Pants
		case 317: return 92.0F; //Golden Boots
		case 346: return 65.0F; //Fishing Rod
		case 359: return 239.0F; //Shears
		}
		return 1.0F;
	}

}