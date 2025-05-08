package gameplayHook.SimUIPackage.RuntimeCodePackage;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.security.SecureClassLoader;
import java.util.HashMap;
import java.util.Map;

// in memory java file manager
class MemoryJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {
    private final Map<String, ByteArrayJavaClass> classData = new HashMap<>();

    MemoryJavaFileManager(JavaFileManager fileManager) {
        super(fileManager);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
        ByteArrayJavaClass outFile = new ByteArrayJavaClass(className);
        classData.put(className, outFile);
        return outFile;
    }

    @Override
    public ClassLoader getClassLoader(Location location) {
        return new SecureClassLoader() {
            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                ByteArrayJavaClass javaClass = classData.get(name);
                if (javaClass == null) throw new ClassNotFoundException(name);
                byte[] bytes = javaClass.getBytes();
                return defineClass(name, bytes, 0, bytes.length);
            }
        };
    }

    public void clear() {
        classData.clear();
    }
}