package Lab1;

import java.util.LinkedHashMap;

public class Core {

    private Stack stack;
    private LinkedHashMap<Integer, SystemCall> sysCalls;

    public void doSysCalling(int index) {

        try {
            //проверка на наличие сисвызова по индексу
            if (!sysCalls.containsKey(index)) throw new KeyNotFoundException();
            //доп переменная количества аргументов сисвызова для удобства
            int callArgsCount = sysCalls.get(index).getArgumentCount();

            System.out.println("Системный вызов " + index + " работает с параметрами:");
            for (int i = 0; i < callArgsCount; i++) {
                //если стек пуст, делаем исключение
                if (stack.isEmpty()) throw new ArgumentsErrorException();
                //проверка на тип
                switch (sysCalls.get(index).getArguments()[i]) {
                    case Integer -> {
                        int temp = (int) stack.peek();
                        System.out.println("Типа int со значением " + stack.pop());
                    }
                    case String -> {
                        String temp = (String) stack.peek();
                        System.out.println("Типа String со значением " + stack.pop());
                    }
                }
            }
            System.out.println("Вызов завершен");
            //обрабатываем исключения
        } catch (ClassCastException e) {
            System.out.println("ERROR\nСистемный вызов " + index + " завершился с ошибкой:\nНеверные типы входных параметров");
            stack.clear();
        } catch (KeyNotFoundException e) {
            System.out.println("ERROR\nСистемный вызов " + index + " завершился с ошибкой:\nСистемный вызов с идентификатором " + index + " не найден");
            stack.clear();
        } catch (ArgumentsErrorException e) {
            System.out.println("ERROR\nСистемный вызов " + index + " завершился с ошибкой:\nНеверное количество входных параметров");
            stack.clear();
        }
    }

    public void getListOfSysCalls() {
        System.out.println("Список системных вызовов:");
        for (int key : sysCalls.keySet()) {
            System.out.print("Системный вызов " + key + ". Принимает параметры: ");
            System.out.println(sysCalls.get(key).getArgumentsString());
        }
    }

    private void initSysCalls() {
        sysCalls = new LinkedHashMap<>(5);
        sysCalls.put(101, new SystemCall(new Types[]{Types.Integer, Types.String}));
        sysCalls.put(102, new SystemCall(new Types[]{Types.Integer, Types.String, Types.Integer}));
        sysCalls.put(103, new SystemCall(new Types[]{Types.Integer, Types.String, Types.String}));
        sysCalls.put(104, new SystemCall(new Types[]{Types.String, Types.String}));
        sysCalls.put(105, new SystemCall(new Types[]{Types.Integer, Types.Integer}));
    }

    public Core(Stack stack) {
        initSysCalls();
        setStack(stack);
    }

    public void setStack(Stack stack) {
        this.stack = stack;
    }

    static class KeyNotFoundException extends Exception { }
    static class ArgumentsErrorException extends Exception { }
}