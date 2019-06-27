/*
 * This file is part of NyanClans Bukkit plug-in.
 *
 *  From plug-in LuckPerms under MIT license.
 *  Copyright (c) lucko (Luck) <luck@lucko.me>
 *
 * NyanClans is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NyanClans is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NyanClans. If not, see <https://www.gnu.org/licenses/>.
 */
package nyanclans.db;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

/** @author lucko (Luck) */
final class ReflectionClassLoader {
    private static final Method ADD_URL_METHOD;

    static {
        try {
            ADD_URL_METHOD = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            ReflectionClassLoader.ADD_URL_METHOD.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private final URLClassLoader classLoader;

    public ReflectionClassLoader(final Object plugin) throws IllegalStateException {
        ClassLoader classLoader = plugin.getClass().getClassLoader();
        if (classLoader instanceof URLClassLoader)
            this.classLoader = (URLClassLoader) classLoader;
        else
            throw new IllegalStateException("ClassLoader is not instance of URLClassLoader");
    }

    /**
     * Loads jar from given path into java runtime.
     * <p>
     * Will return <tt>false</tt> if some exception occurred.
     *
     * @param   jarPath     Path to jar to load.
     * @return <tt>true</tt> if loaded successfully.
     */
    public boolean loadJar(final Path jarPath) {
        try {
            ReflectionClassLoader.ADD_URL_METHOD.invoke(classLoader, jarPath.toUri().toURL());
            return true;
        } catch (IllegalAccessException | InvocationTargetException | MalformedURLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
