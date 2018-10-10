package vic.sc.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import com.google.common.collect.Lists;

public class Utils {
	
	private static final String CLASS_EXTENSION = ".class";
	
	private static final String JAVA_EXTENSION = ".java";
	
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
					if (p.endsWith("/META-INF")) {
						continue;
					}
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
	
	public static boolean createClassDynamic(String source, String className, String filePath) throws IOException {
		File tempDir = new File(filePath);
		if (!tempDir.isDirectory()) {
			throw new RuntimeException("Incorrect DIR");
		}
		if (!tempDir.exists()) {
			tempDir.mkdirs();
		}
		String javaFileName = className + JAVA_EXTENSION;
		File javaFile = new File(filePath + "/" + javaFileName);
		if (javaFile.exists()) {
			javaFile.delete();
		}
		javaFile.createNewFile();
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		try(FileWriter fw = new FileWriter(javaFile); 
			StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, null);) {
			fw.write(source);
			fw.flush();	
			Iterable<? extends JavaFileObject> itr = manager.getJavaFileObjects(javaFile);
			List<String> options = Lists.newArrayList("-d", filePath);
			CompilationTask task = compiler.getTask(null, manager, null, options, null, itr);
			return task.call();
		}
	}
	
}
