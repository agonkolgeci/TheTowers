package fr.jielos.thetowers.game;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

public class Map {

	final Game game;
	
	World world;
	
	public Map(final Game game) {
		this.game = game;
		
		try {
			this.unzip(game.getInstance().getResource("TheTowers.zip"), new File(game.getInstance().getServer().getWorldContainer().getName()+"/TheTowers"));
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
    public void unzip(final InputStream source, final File target) throws IOException {
    	if(!target.exists()) target.mkdir();
    	else {
    		if(Bukkit.getWorld(target.getName()) != null) {
    			Bukkit.unloadWorld(target.getName(), false);
    		}
    		
    		FileUtils.deleteDirectory(target);
    		if(!target.exists()) target.mkdir();
    	}
    	
    	byte[] buffer = new byte[1024];
    	final ZipInputStream zipInputStream = new ZipInputStream(source);
    	
    	ZipEntry zipEntry = zipInputStream.getNextEntry();
    	while(zipEntry != null) {
    		final File newFile = newFile(target, zipEntry);
    		if(zipEntry.isDirectory()) {
    			if(!newFile.isDirectory() && !newFile.mkdirs()) {
    				throw new IOException("Failed to create directory " + newFile);
    			}
    		} else {
    			final File parent = newFile.getParentFile();
    			if(!parent.isDirectory() && !parent.mkdirs()) {
    				throw new IOException("Failed to create directory " + parent);
    			}
    			
    			final FileOutputStream fileOutputStream = new FileOutputStream(newFile);
    			
    			int length;
    			while ((length = zipInputStream.read(buffer)) > 0) fileOutputStream.write(buffer, 0, length);
    			
    			fileOutputStream.close();
    		}
    		
    		zipEntry = zipInputStream.getNextEntry();
    	}
    	
    	zipInputStream.close();
    	
    	this.world = Bukkit.createWorld(new WorldCreator(target.getName()));
    }
    
    private File newFile(final File destinationDir, final ZipEntry zipEntry) throws IOException {
        final File destFile = new File(destinationDir, zipEntry.getName());

        final String destDirPath = destinationDir.getCanonicalPath();
        final String destFilePath = destFile.getCanonicalPath();

        if(!destFilePath.startsWith(destDirPath + File.separator)) throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        return destFile;
    }
    
    public World getWorld() {
		return world;
	}
}
