package utilities;

import java.io.File;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;

import jdbc.JDriver;

public class driverLoader {
		
		 Constructor<?>[] constructor;
	     URL url;
	     URLClassLoader ucl;
	     Class<?> loadedClass;
		public void loadDriver(final File newJar) {
			try {
				 url = newJar.toURI().toURL();
				 ucl = new URLClassLoader(new URL[] { url });
				loadedClass = ucl.loadClass("eg.edu.alexu.csd.oop.jdbc.JDriver");
				constructor = loadedClass.getConstructors();
			} catch (Exception e) {
				
			}
		}
		public JDriver instansiateDriver() {
			try {
				JDriver driver = (JDriver) constructor[0].newInstance();
				return driver;
			} catch (Exception e) {
				return null;
			}
		}
}
