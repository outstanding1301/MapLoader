package com.yeomryo.map;

import org.bukkit.Material;
import org.bukkit.World;

public class Build {
	
	World world;
	Material material;
	byte data;
	int x;
	int y;
	int z;
	
	
	public World getWorld() {
		return world;
	}
	public void setWorld(World world) {
		this.world = world;
	}
	public Material getMaterial() {
		return material;
	}
	public void setMaterial(Material material) {
		this.material = material;
	}
	public byte getData() {
		return data;
	}
	public void setData(byte data) {
		this.data = data;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getZ() {
		return z;
	}
	public void setZ(int z) {
		this.z = z;
	}
	
}
