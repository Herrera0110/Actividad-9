import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bienvenido al validador de contraseñas.");

        ExecutorService executorService = Executors.newCachedThreadPool();

        while (true) {
            System.out.print("Ingrese una contraseña (o 'exit' para salir): ");
            String password = scanner.nextLine();

            if (password.equalsIgnoreCase("exit")) {
                break;
            }

            executorService.execute(new PasswordValidatorTask(password));
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class PasswordValidatorTask implements Runnable {
        private final String password;
        private final Matcher matcher;

        public PasswordValidatorTask(String password) {
            this.password = password;
            Pattern pattern = Pattern.compile("^(?=.*[a-z].*[a-z])(?=.*[A-Z].*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
            matcher = pattern.matcher(password);
        }

        @Override
        public void run() {
            if (matcher.matches()) {
                System.out.println("La contraseña '" + password + "' es válida.");
            } else {
                System.out.println("La contraseña '" + password + "' no cumple con los requisitos.");
                System.out.println("Los requisitos son: ");
                System.out.println("- Longitud mínima de 8 caracteres");
                System.out.println("- Al menos 2 letras mayúsculas");
                System.out.println("- Al menos 3 letras minúsculas");
                System.out.println("- Al menos 1 número");
                System.out.println("- Al menos 1 carácter especial: @$!%*?&");
            }
        }
    }
}
