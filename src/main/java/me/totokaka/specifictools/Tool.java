package me.totokaka.specifictools;

import org.bukkit.Material;

public enum Tool{
	
	WOOD_PICKAXE	(Material.WOOD_PICKAXE),
	STONE_PICKAXE	(Material.STONE_PICKAXE),
	IRON_PICKAXE	(Material.IRON_PICKAXE),
	GOLD_PICKAXE	(Material.GOLD_PICKAXE),
	DIAMOND_PICKAXE	(Material.DIAMOND_PICKAXE),
	WOOD_SHOVEL		(Material.WOOD_SPADE),
	STONE_SHOVEL	(Material.STONE_SPADE),
	IRON_SHOVEL		(Material.IRON_SPADE),
	GOLD_SHOVEL		(Material.	GOLD_SPADE),
	DIAMOND_SHOVEL	(Material.DIAMOND_SPADE),
	WOOD_SWORD		(Material.WOOD_SWORD),
	STONE_SWORD		(Material.	STONE_SWORD),
	IRON_SWORD		(Material.IRON_SWORD),
	GOLD_SWORD		(Material.GOLD_SWORD),
	DIAMOND_SWORD	(Material.DIAMOND_SWORD),
	WOOD_HOE		(Material.WOOD_HOE),
	STONE_HOE		(Material.STONE_HOE),
	IRON_HOE		(Material.IRON_HOE),
	GOLD_HOE		(Material.GOLD_HOE),
	DIAMOND_HOE		(Material.DIAMOND_HOE),
	WOOD_AXE		(Material.WOOD_AXE),
	STONE_AXE		(Material.STONE_AXE),
	IRON_AXE		(Material.IRON_AXE),
	GOLD_AXE		(Material.GOLD_AXE),
	DIAMOND_AXE		(Material.DIAMOND_AXE),
	SHEARS			(Material.SHEARS),
	HAND			(null);
	
	private Material m;
	
	
	private Tool(Material m){
		this.setMaterial(m);
	}


	public Material getMaterial() {
		return m;
	}


	private void setMaterial(Material m) {
		this.m = m;
	}


	public static Tool getFromMaterial(Material type) {
		if(type == null){
			return HAND;
		}else{
			for(Tool t : Tool.values()){
				if(t != HAND && t.getMaterial().equals(type)){
					return t;
				}
			}
		}
		return null;
	}
}
