package players;

import main.MainWindow;

/**
 * Loads a given string of a Player into memory
 * @author Randy
 */
public class PlayerLoader {
    public static PlayerInterface load(String classname, MainWindow applet) throws ClassNotFoundException {
        //check that class exists
        if (!checkClassName(classname)) {
            System.out.println("ouch");
            throw new ClassNotFoundException("Invalid class name: "
                    + classname);
        }

        //get full Player string and init ClassLoader
        String glName = PlayerLoader.class.getName();
        String pkgName = glName.substring(0, glName.lastIndexOf('.'));
        String fullName = pkgName + "." + classname;
        ClassLoader cl = ClassLoader.getSystemClassLoader();

        //Load Player
        @SuppressWarnings("rawtypes")
        Class PlayerClass = cl.loadClass(fullName);
        PlayerInterface cp;
        try {
            cp = (PlayerInterface) PlayerClass.newInstance();
            cp.init(applet);
        } catch (Exception e) {
            System.out.println(e);
            throw new ClassNotFoundException(classname);
        }
        return cp;
    }

    //currently not implemented
    private static boolean checkClassName(String classname) {
        return true;
    }
}
