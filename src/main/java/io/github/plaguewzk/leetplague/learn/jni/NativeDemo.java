package io.github.plaguewzk.leetplague.learn.jni;

/**
 * JNI 入门练习：先调用 C++，再通过 C++ 桥接调用 Python。
 */
public class NativeDemo {

    // TODO 第 1 步：声明两个静态 native 方法：addCpp 和 addPython。
    // 两个方法均接收 int a、int b，返回 int。
    public static native int addCpp(int a, int b);
    public static native int addPython(int a, int b);

    // TODO 第 3 步：加载自己编译的动态链接库。
    static {
        System.loadLibrary("native_demo");
    }
    public static void main(String[] args) {
        // TODO 第 3 步：调用 addCpp(3, 5)，期望得到 8。
        int result = addCpp(3, 5);
        System.out.println("C++ 计算结果：" + result);
        // TODO 第 4 步：调用 addPython(3, 5)，期望得到 8。
    }
}
