package com.test;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.project.authority.Authority;
import com.project.entity.admin.SysMenu;
import com.project.service.admin.ISysMenuService;
import com.project.utils.SpringContextUtil;

/**
 * 反射解析指定包下的所有类中的自定义注解
 *
 * @author GuoZhiLong
 * @date 2015年12月31日 下午4:01:30
 */
public class ReflexTest {

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName
     * @param packagePath
     * @param recursive
     * @param classes
     * @return void
     * @author GuoZhiLong
     */
    public static void findClassesByFile(String packageName, String packagePath, final boolean recursive,
                                         Set<Class<?>> classes) {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            // log.warn("用户定义包名 " + packageName + " 下没有任何文件");
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        // 循环所有文件
        for (File file : dirfiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                findClassesByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    // 添加到集合中
                    classes.add(
                            Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从包package中获取所有的Class
     *
     * @param pack
     * @return Set<Class<?>>
     * @author GuoZhiLong
     */
    public static Set<Class<?>> getClasses(String pack) {
        // 第一个class类的集合
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        // 是否循环迭代
        boolean recursive = true;
        // 获取包的名字 并进行替换
        String packageName = pack;
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    System.err.println("file类型的扫描");
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    findClassesByFile(packageName, filePath, recursive, classes);
                } else if ("jar".equals(protocol)) {
                    // 如果是jar包文件
                    // 定义一个JarFile
                    System.err.println("jar类型的扫描");
                    JarFile jar;
                    try {
                        // 获取jar
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        // 从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        // 同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            // 如果是以/开头的
                            if (name.charAt(0) == '/') {
                                // 获取后面的字符串
                                name = name.substring(1);
                            }
                            // 如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                // 如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    // 获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                // 如果可以迭代下去 并且是一个包
                                if ((idx != -1) || recursive) {
                                    // 如果是一个.class文件 而且不是目录
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        // 去掉后面的".class" 获取真正的类名
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try {
                                            // 添加到classes
                                            classes.add(Class.forName(packageName + '.' + className));
                                        } catch (ClassNotFoundException e) {
                                            // log
                                            // .error("添加用户自定义视图类错误
                                            // 找不到此类的.class文件");
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        // log.error("在扫描用户定义视图时从jar包获取文件出错");
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    }

    public static void main(String[] ar) {
        new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});
        ISysMenuService sysMenuService = (ISysMenuService) SpringContextUtil.getBean("sysMenuService");

        Set<Class<?>> classSet = getClasses("com.ronsai.web.manage");
        for (Class<?> class1 : classSet) {
            for (Method method : class1.getMethods()) {
                if (method.isAnnotationPresent(Authority.class)) {
                    for (Annotation annotation : method.getAnnotations()) {
                        System.out.println(annotation + " in method:" + method);
                    }
                    Authority authority = method.getAnnotation(Authority.class);
                    if (StringUtils.isNotBlank(authority.parentVal())) {
                        // 查询父级权限是否存在，不存在则先创建
                        SysMenu parentSysMenu = sysMenuService.queryByCodeAndPid(authority.parentVal(), 0);
                        int parentId = 0;
                        if (parentSysMenu == null) {//父级菜单
                            SysMenu sysMenu = new SysMenu();
                            sysMenu.setCodeType(0);
                            sysMenu.setParentId(0);
                            sysMenu.setMenuCode(authority.parentVal());
                            sysMenu.setMenuTitle(authority.pAliasesVal());
                            sysMenu.setMenuUrl("javascript:void(0);");
                            sysMenu.setCreateDate(new Date());
                            sysMenu.setUpdateDate(new Date());
                            parentId = sysMenuService.addReturnID(sysMenu);
                        } else {
                            parentId = parentSysMenu.getId();
                        }
                        // 根据权限编码、父权限ID查询是否存在，不存在则创建，存在则不管
                        SysMenu cSysMenu = sysMenuService.queryByCodeAndPid(authority.purviewVal(), parentId);
                        if (cSysMenu == null) {
                            SysMenu sysMenu = new SysMenu();
                            Integer codeType = 1;
                            switch (authority.resultType()) {
                                case PAGE:
                                    codeType = 1;
                                    break;
                                case JSON:
                                    codeType = 2;
                                    break;
                            }
                            sysMenu.setCodeType(codeType);
                            sysMenu.setParentId(parentId);
                            sysMenu.setMenuCode(authority.purviewVal());
                            sysMenu.setMenuTitle(authority.aliasesVal());
                            sysMenu.setSort(authority.sort());
                            sysMenu.setMenuUrl(authority.menuUrl());
                            sysMenu.setCreateDate(new Date());
                            sysMenu.setUpdateDate(new Date());
                            sysMenuService.saveOrUpdate(sysMenu);
                        }
                    }
                }
            }

        }

    }
}
