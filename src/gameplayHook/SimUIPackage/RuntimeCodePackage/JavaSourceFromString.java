package gameplayHook.SimUIPackage.RuntimeCodePackage;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;

// holds Java source in memory
class JavaSourceFromString extends SimpleJavaFileObject {
    final String code;

    JavaSourceFromString(String name, String code) {
        super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.code = code;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return code;
    }
}