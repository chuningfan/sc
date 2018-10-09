package vic.sc.util;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.Set;

public class Utils {
	
	private static final String CLASS_EXTENSION = ".class";
	
	public static void scan(String basePath, String path, Set<Class<?>> set, Class<? extends Annotation> annotationClass) throws ClassNotFoundException {
		if (path.trim().length() > 0) {
			path = path.replace(".", "/");
		}
		if ("".equals(path.trim()) || !path.startsWith(basePath)) {
			path = basePath + path;
		}
		File dir = new File(path);
		if (dir.exists() && dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (File f: files) {
				String pkg = path.replace(basePath, "").replace("/", ".");
				if (f.isFile()) {
					if (f.getName().toLowerCase().endsWith(CLASS_EXTENSION)) {
						Class<?> clazz = Class.forName(pkg + "." + (f.getName().substring(0, f.getName().lastIndexOf("."))));
						Object[] wc = clazz.getAnnotationsByType(annotationClass);
						if (wc != null && wc.length > 0) {
							set.add(clazz);
						}
					}
				} else {
					String p = f.getAbsolutePath();
					scan(basePath, p, set, annotationClass);
				}
			}
		} else {
			throw new RuntimeException("Incorrect Path!");
		}
	}
	
	public static Class<?>[] getParameterTypes(Object[] parameters) {
		Class<?>[] classes = new Class<?>[parameters.length];
		for (int i = 0; i < classes.length; i++) {
			classes[i] = parameters[i].getClass();
		}
		return classes;
	}
	
	
	public static String lowerCaseFirstLetter(String str) {
		String firstLetter = str.substring(0,1).toLowerCase();
		String other = str.substring(1);
		return firstLetter + other;
	}
	
}
