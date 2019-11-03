package com.yeomryo.map;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class SaveThread extends Thread{
	private LinkedList<Build> list;
	private String name;
	private File dataFolder;
	public SaveThread(File dataFolder, String name, LinkedList<Build> list) {
		this.dataFolder=dataFolder;
		this.name=name;
		this.list=list;
	}
	@Override
	public void run() {
		try{
			File f = new File(dataFolder, "");
			if(!f.exists())
				f.mkdirs();
			f = new File(dataFolder ,name+".yr");
			if(f.exists())
				f.delete();
			FileWriter fw = new FileWriter(f);
			for(Build b : list){
				fw.append(b.getWorld().getName()+"*");
				fw.append(b.getMaterial().toString()+"*");//0
				fw.append(b.getData()+"*");//1
				if(list.getLast().equals(b))
					fw.append(b.getX()+"/"+b.getY()+"/"+b.getZ());//2
				else
					fw.append(b.getX()+"/"+b.getY()+"/"+b.getZ()+"*");//2
			}
			fw.close();
		}catch(IOException e){
			
		}
	}
}
