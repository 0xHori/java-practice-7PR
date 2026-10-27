package ru.edu.pr04;

public final class App {

    /** Сценарий по умолчанию: id товара:количество */
    private static final String[] DEFAULT_ITEMS = {"M01:2", "K01:3", "M01:1"};

    /** Сотрудник, оформляющий заказ */
    private static final Employee EMPLOYEE = new Employee("E1", "Ivanov Ivan");

    /** Каталог товаров */
    private static final Product[] CATALOG = {
            new Product("M01", "Monitor 27", 32990),
            new Product("K01", "Keyboard", 4490)
    };

    private App() {
    }

    public static void main(String[] args) {
        String[] items = (args.length > 0) ? args : DEFAULT_ITEMS;

        OrderService service = new OrderService();
        Order order = service.createOrder(EMPLOYEE);

        System.out.println("Сотрудник: " + EMPLOYEE.fullName() + " (" + EMPLOYEE.id() + ")");
        System.out.println("Статус: " + order.getStatus());
        System.out.println();

        System.out.println("Добавление позиций:");
        for (String line : items) {
            try {
                addLine(order, line);
                System.out.printf("  + %-15s -> позиций: %d, total: %.0f%n",
                        line, order.getItems().size(), order.total());
            } catch (IllegalArgumentException e) {
                System.out.printf("  [ошибка] %-10s -> %s%n", line, e.getMessage());
            }
        }

        System.out.println();
        System.out.printf("Итого: %.0f руб., позиций: %d%n", order.total(), order.getItems().size());

        try {
            service.validateForConfirm(order);
            order.confirm();
            System.out.println("Заказ подтверждён, статус: " + order.getStatus());
        } catch (IllegalStateException e) {
            System.out.println("Не удалось подтвердить: " + e.getMessage());
            return;
        }

        System.out.println();
        System.out.println("Попытка изменить подтверждённый заказ:");
        try {
            order.addProduct(CATALOG[0], 1);
            System.out.println("  [FAIL] изменение прошло (не должно было)");
        } catch (IllegalStateException e) {
            System.out.println("  [OK] " + e.getMessage());
        }
    }

    private static void addLine(Order order, String line) {
        int sep = line.indexOf(':');
        if (sep <= 0 || sep == line.length() - 1) {
            throw new IllegalArgumentException("Ожидается формат id:qty, получено: " + line);
        }
        String id = line.substring(0, sep);
        int quantity;
        try {
            quantity = Integer.parseInt(line.substring(sep + 1));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Количество не число: " + line.substring(sep + 1));
        }
        order.addProduct(findProduct(id), quantity);
    }

    private static Product findProduct(String id) {
        for (Product p : CATALOG) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Товар не найден в каталоге: " + id);
    }
}
