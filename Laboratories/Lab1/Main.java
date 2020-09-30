package Lab1;

public class Main {

    public static void main(String[] args) {

        Stack stack = new Stack(100);
        Core core = new Core(stack);

        core.getListOfSysCalls();
        System.out.println();

        stack.push("read");
        stack.push(123);
        core.doSysCalling(101);
        System.out.println();

        stack.push(0);
        stack.push("delete");
        stack.push(50);
        core.doSysCalling(102);
        System.out.println();

        stack.push("reboot");
        stack.push("now");
        stack.push("1");
        core.doSysCalling(103);
        System.out.println();

        stack.push("boot");
        stack.push("later");
        core.doSysCalling(104);
        System.out.println();

        stack.push(404);
        stack.push(1);
        core.doSysCalling(105);
        System.out.println();

        core.doSysCalling(101);
        System.out.println();

        stack.push(123);
        stack.push(123);
        core.doSysCalling(101);
        System.out.println();

        core.doSysCalling(99);
        System.out.println();
    }
}
