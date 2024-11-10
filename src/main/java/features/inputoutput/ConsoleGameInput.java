package features.inputoutput;

import java.util.Scanner;

public class ConsoleGameInput implements IGameInput {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public String getInput() {
        return scanner.nextLine();
    }
}
