# JNI 入门练习

配合 Java 入口 `src/main/java/io/github/plaguewzk/leetplague/learn/jni/NativeDemo.java`，逐步手写并执行命令。

## 目标

- `addCpp(int a, int b)`：Java → JNI → C++ 加法 → Java。
- `addPython(int a, int b)`：Java → JNI → C++ 桥接 → 嵌入的 CPython → Python 加法 → Java。

Python 文件不能直接作为 JNI 动态库加载。第二个方法的 JNI 入口仍由 C++ 实现，计算逻辑放在 Python 函数中。

## 学习顺序

1. 确认 JDK、Python 和 C++ 编译器；手写 Java native 方法声明。
2. 使用 `javac -h` 生成 JNI 头文件，阅读声明并编写 C++ 加法实现。
3. 编译 Windows DLL，加载动态库，在 Java 中调用 C++ 方法。
4. 编写 Python 加法函数，加入 CPython 桥接，在 Java 中调用第二个方法。

后续 C++ 和 Python 源文件放在本目录；生成的头文件、class 和 DLL 放在项目根目录的 `target/native-demo/` 下。

当前只有练习骨架，尚未编译或验证跨语言调用。每次先完成当前步骤，再根据实际输出继续。
