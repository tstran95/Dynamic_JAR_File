import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class Animal {
    public static final String PATH = "./jar/flyWithMe.jar";
    public static final String CLASS_NAME = "Bird";
    public static final String METHOD = "flying";

    public Class<?> getCurrentClass() {
        try {
            File fileName = new File(PATH);
            URLClassLoader child = new URLClassLoader(
                    new URL[] {fileName.toURI().toURL()},
                    this.getClass().getClassLoader()
            );
           return Class.forName(CLASS_NAME, true, child);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public void  fly(Class<?> classToLoad) {
        try {
            Method method = classToLoad.getDeclaredMethod(METHOD);
            Object instance = classToLoad.getDeclaredConstructor().newInstance();
            method.invoke(instance);
            Thread.sleep(1000);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Animal animal = new Animal();
        Class<?> classLoader = animal.getCurrentClass();
        int count = 0;
        while (true) {
            animal.fly(classLoader);
            count++;
            System.out.println("-------------- " +count+" ----------------");
        }
    }
}
