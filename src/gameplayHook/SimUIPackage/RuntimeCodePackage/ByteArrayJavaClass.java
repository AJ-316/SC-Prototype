package gameplayHook.SimUIPackage.RuntimeCodePackage;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;

// compiled bytecode in memory
class ByteArrayJavaClass extends SimpleJavaFileObject {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    ByteArrayJavaClass(String name) {
        super(URI.create("bytes:///" + name), Kind.CLASS);
    }

    @Override
    public OutputStream openOutputStream() {
        return outputStream;
    }

    byte[] getBytes() {
        return outputStream.toByteArray();
    }
}