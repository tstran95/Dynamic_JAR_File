import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

public class Animal {
    public static final String PATH = "./jar/flyWithMe.jar";
    public static final String CLASS_NAME = "Bird";
    public static final String METHOD = "flying";

    /**
     * Find Class in JAR file by Path
     *
     * @return Class
     */
    public Class<?> getCurrentClass() {
        try {
            File fileName = new File(PATH);
            // get class loader parent
            ClassLoader parent = this.getClass().getClassLoader();
            // get url of file
            URL[] url = new URL[]{fileName.toURI().toURL()};
            //get URL Class loader child
            URLClassLoader child = new URLClassLoader(url , parent);

            return Class.forName(CLASS_NAME, true, child);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Run method in JAR file
     */
    private void fly(Class<?> classLoaded) {
        try {
            // get Method in class by name
            Method method = classLoaded.getDeclaredMethod(METHOD);
            // create instance of class
            Object instance = classLoaded.getDeclaredConstructor().newInstance();
            method.invoke(instance);
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Run JAR File
     */
    public void runJARFile() {
        try {
            int count = 0;
            File fileName = new File(PATH);

            // get time modified file
            BasicFileAttributes attributes = Files.readAttributes(Paths.get(fileName.toURI()), BasicFileAttributes.class);
            FileTime fileTime = attributes.lastAccessTime();
            // get current class
            Class<?> classLoader = this.getCurrentClass();

            while (true) {
                FileTime currentAccessFileTime = Files.readAttributes(Paths.get(fileName.toURI()), BasicFileAttributes.class)
                                                        .lastAccessTime();
                // check access time of this JAR file
                // if 2 time diff -> file replaced and get class in current JAR file again
                if (!currentAccessFileTime.equals(fileTime)) {
                    classLoader = this.getCurrentClass();
                }
                this.fly(classLoader);
                count++;
                System.out.println("-------------- " + count + " ----------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Animal animal = new Animal();
        animal.getCurrentClass();
    }
}
