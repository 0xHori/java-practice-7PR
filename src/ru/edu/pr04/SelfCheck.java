package ru.edu.pr04;

public class SelfCheck {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        testProductValid();
        testProductInvalidId();
        testProductInvalidName();
        testProductInvalidPrice();
        testOrderItemValid();
        testOrderItemLowerBound();
        testOrderItemUpperBound();
        testOrderItemAboveUpperBound();
        testOrderItemZero();
        testOrderItemNegative();
        testIncreaseValid();
        testIncreaseBeyondMax();
        testIncreaseNonPositive();
        testLineTotal();
        testAddProductNew();
        testAddProductMerge();
        testAddProductAfterConfirm();
        testRemoveProduct();
        testRemoveMissingProduct();
        testTotal();
        testConfirmEmpty();
        testConfirmAndLock();
        testGetItemsImmutable();
        testOrderServiceCreate();
        testOrderServiceValidate();

        System.out.println();
        System.out.println("Пройдено: " + passed + ", провалено: " + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void testProductValid() {
        Product p = new Product("P1", "Mouse", 1000);
        check("Product: корректные данные", p.getPrice() == 1000);
    }

    private static void testProductInvalidId() {
        checkThrows("Product: пустой id", () -> new Product("", "Mouse", 1000));
    }

    private static void testProductInvalidName() {
        checkThrows("Product: пустое имя", () -> new Product("P1", " ", 1000));
    }

    private static void testProductInvalidPrice() {
        checkThrows("Product: цена <= 0", () -> new Product("P1", "Mouse", 0));
    }

    private static void testOrderItemValid() {
        Product p = new Product("P1", "Mouse", 1000);
        OrderItem item = new OrderItem(p, 5);
        check("OrderItem: корректное количество", item.getQuantity() == 5);
    }

    private static void testOrderItemLowerBound() {
        Product p = new Product("P1", "Mouse", 1000);
        check("OrderItem: нижняя граница 1", new OrderItem(p, 1).getQuantity() == 1);
    }

    private static void testOrderItemUpperBound() {
        Product p = new Product("P1", "Mouse", 1000);
        check("OrderItem: верхняя граница 110", new OrderItem(p, 110).getQuantity() == 110);
    }

    private static void testOrderItemAboveUpperBound() {
        Product p = new Product("P1", "Mouse", 1000);
        checkThrows("OrderItem: 111 отклонено", () -> new OrderItem(p, 111));
    }

    private static void testOrderItemZero() {
        Product p = new Product("P1", "Mouse", 1000);
        checkThrows("OrderItem: 0 отклонено", () -> new OrderItem(p, 0));
    }

    private static void testOrderItemNegative() {
        Product p = new Product("P1", "Mouse", 1000);
        checkThrows("OrderItem: -1 отклонено", () -> new OrderItem(p, -1));
    }

    private static void testIncreaseValid() {
        Product p = new Product("P1", "Mouse", 1000);
        OrderItem item = new OrderItem(p, 5);
        item.increase(3);
        check("OrderItem: increase(3) -> 8", item.getQuantity() == 8);
    }

    private static void testIncreaseBeyondMax() {
        Product p = new Product("P1", "Mouse", 1000);
        OrderItem item = new OrderItem(p, 100);
        checkThrows("OrderItem: increase за максимум", () -> item.increase(20));
    }

    private static void testIncreaseNonPositive() {
        Product p = new Product("P1", "Mouse", 1000);
        OrderItem item = new OrderItem(p, 5);
        checkThrows("OrderItem: increase(0)", () -> item.increase(0));
    }

    private static void testLineTotal() {
        Product p = new Product("P1", "Mouse", 1000);
        OrderItem item = new OrderItem(p, 3);
        check("OrderItem: lineTotal = 3000", item.getLineTotal() == 3000);
    }

    private static void testAddProductNew() {
        Order order = new Order(new Employee("E1", "Ivanov"));
        order.addProduct(new Product("P1", "Mouse", 1000), 2);
        check("Order: добавлена новая позиция", order.getItems().size() == 1);
    }

    private static void testAddProductMerge() {
        Order order = new Order(new Employee("E1", "Ivanov"));
        Product p = new Product("P1", "Mouse", 1000);
        order.addProduct(p, 2);
        order.addProduct(p, 3);
        check("Order: повторный товар объединён", order.getItems().size() == 1
                && order.getItems().get(0).getQuantity() == 5);
    }

    private static void testAddProductAfterConfirm() {
        Order order = new Order(new Employee("E1", "Ivanov"));
        order.addProduct(new Product("P1", "Mouse", 1000), 2);
        order.confirm();
        checkThrows("Order: добавление после confirm",
                () -> order.addProduct(new Product("P2", "Keyboard", 500), 1));
    }

    private static void testRemoveProduct() {
        Order order = new Order(new Employee("E1", "Ivanov"));
        order.addProduct(new Product("P1", "Mouse", 1000), 2);
        order.removeProduct("P1");
        check("Order: позиция удалена", order.getItems().isEmpty());
    }

    private static void testRemoveMissingProduct() {
        Order order = new Order(new Employee("E1", "Ivanov"));
        checkThrows("Order: удаление отсутствующего товара",
                () -> order.removeProduct("NOPE"));
    }

    private static void testTotal() {
        Order order = new Order(new Employee("E1", "Ivanov"));
        order.addProduct(new Product("P1", "Mouse", 1000), 2);
        order.addProduct(new Product("P2", "Keyboard", 500), 3);
        check("Order: total = 3500", order.total() == 3500);
    }

    private static void testConfirmEmpty() {
        Order order = new Order(new Employee("E1", "Ivanov"));
        checkThrows("Order: подтверждение пустого заказа", order::confirm);
    }

    private static void testConfirmAndLock() {
        Order order = new Order(new Employee("E1", "Ivanov"));
        order.addProduct(new Product("P1", "Mouse", 1000), 2);
        order.confirm();
        check("Order: статус CONFIRMED", order.getStatus() == OrderStatus.CONFIRMED);
        checkThrows("Order: повторный confirm", order::confirm);
    }

    private static void testGetItemsImmutable() {
        Order order = new Order(new Employee("E1", "Ivanov"));
        order.addProduct(new Product("P1", "Mouse", 1000), 2);
        checkThrows("Order: getItems() неизменяем",
                () -> order.getItems().add(new OrderItem(new Product("P2", "Keyboard", 500), 1)));
    }

    private static void testOrderServiceCreate() {
        OrderService service = new OrderService();
        Order order = service.createOrder(new Employee("E1", "Ivanov"));
        check("OrderService: создан заказ", order != null && order.getStatus() == OrderStatus.DRAFT);
    }

    private static void testOrderServiceValidate() {
        OrderService service = new OrderService();
        Order order = service.createOrder(new Employee("E1", "Ivanov"));
        order.addProduct(new Product("P1", "Mouse", 1000), 2);
        service.validateForConfirm(order);
        check("OrderService: валидация прошла", true);
    }

    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("[OK]   " + name);
        } else {
            failed++;
            System.out.println("[FAIL] " + name);
        }
    }

    private static void checkThrows(String name, Runnable action) {
        try {
            action.run();
            failed++;
            System.out.println("[FAIL] " + name + " (исключение не брошено)");
        } catch (RuntimeException e) {
            passed++;
            System.out.println("[OK]   " + name + " -> " + e.getMessage());
        }
    }
}
