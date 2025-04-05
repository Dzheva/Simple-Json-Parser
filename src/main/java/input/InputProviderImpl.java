package input;

import java.util.Scanner;

public class InputProviderImpl implements InputProvider{
    private final Scanner scanner;

    public InputProviderImpl(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public String getNextLine(){
        return scanner.nextLine().trim();
    }
}
