package utilities;

import java.io.File;


import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;

import jdbc.JDriver;

public class driverLoader {

	final static String JDRIVER_PACK = "jdbc.JDriver";
	Constructor<?>[] constructor;
	URL url;
	URLClassLoader ucl;
	Class<?> loadedClass;

	public void loadDriver(final File newJar) {
		try {
			url = newJar.toURI().toURL();
			ucl = new URLClassLoader(new URL[] { url });
			loadedClass = ucl.loadClass(JDRIVER_PACK);
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
