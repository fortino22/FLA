package helpers;

import java.util.List;
import java.util.function.Function;
import models.entity.*;
import interfaces.ITableDesign;

public class TableBuilder {
    private static final int columnWidth = 35;

    public static <T> String joinInitials(List<T> items, Function<T, String> extractInitial) {
        StringBuilder result = new StringBuilder();
        for (T item : items) {
            result.append(extractInitial.apply(item));
        }
        return result.toString();
    }

    public static String CreateTable(List<Customer> customers, List<Waiter> waiters, List<Chef> chefs) {
        ITableDesign<Customer> customerFormatter = createCustomerFormatter();
        ITableDesign<Waiter> waiterFormatter = createWaiterFormatter();
        ITableDesign<Chef> chefFormatter = createChefFormatter();

        StringBuilder table = new StringBuilder();
        String border = buildTableBorder(customerFormatter, waiterFormatter, chefFormatter);

        table.append(border).append("\n");
        table.append(buildHeaderRow(customerFormatter, waiterFormatter, chefFormatter)).append("\n");
        table.append(border).append("\n");

        int maxRows = Math.max(Math.max(customers.size(), waiters.size()), chefs.size());
        for (int i = 0; i < maxRows; i++) {
            table.append(buildDataRow(i, customers, customerFormatter,
                    waiters, waiterFormatter,
                    chefs, chefFormatter)).append("\n");
        }

        table.append(border);
        return table.toString();
    }

    private static String buildDataRow(int index, List<Customer> customers, ITableDesign<Customer> customerFormatter, List<Waiter> waiters, ITableDesign<Waiter> waiterFormatter, List<Chef> chefs, ITableDesign<Chef> chefFormatter) {
        StringBuilder row = new StringBuilder();
        row.append(index < customers.size() ? customerFormatter.formatRow(customers.get(index)) : customerFormatter.formatEmpty());
        row.append(index < waiters.size() ? waiterFormatter.formatRow(waiters.get(index)) : waiterFormatter.formatEmpty());
        row.append(index < chefs.size() ? chefFormatter.formatRow(chefs.get(index)) : chefFormatter.formatEmpty());
        return row + "|";
    }

    private static String buildHeaderRow(ITableDesign<?>... formatters) {
        StringBuilder header = new StringBuilder();
        for (ITableDesign<?> formatter : formatters) {
            header.append(formatter.formatHeader());
        }
        return header + "|";
    }

    private static String buildTableBorder(ITableDesign<?>... formatters) {
        StringBuilder border = new StringBuilder();
        for (ITableDesign<?> formatter : formatters) {
            border.append(formatter.formatBorder());
        }
        return border + "+";
    }

    private static ITableDesign<Customer> createCustomerFormatter() {
        return new ITableDesign<Customer>() {
            public String formatHeader() {
                return String.format("| %-" + columnWidth + "s", "Customer");
            }

            public String formatRow(Customer customer) {
                String stateInfo = customer.getState() != null ? formatStateDetails(customer.getState().getStateName()) : "waiting";
                return String.format("| %-" + columnWidth + "s", String.format("%s(%d) ~> %s", customer.getInitial(), customer.getTolerance(), stateInfo));
            }

            public String formatEmpty() {
                return String.format("| %-" + columnWidth + "s", "");
            }

            public String formatBorder() {
                return "+" + "-".repeat(columnWidth + 2);
            }

            public int getColumnWidth() {
                return columnWidth + 2;
            }
        };
    }

    private static ITableDesign<Waiter> createWaiterFormatter() {
        return new ITableDesign<Waiter>() {
            public String formatHeader() {
                return String.format("| %-" + columnWidth + "s", "Waiter");
            }

            public String formatRow(Waiter waiter) {
                String stateInfo = waiter.getState() != null ? formatStateDetails(waiter.getState().getStateName()) : "waiting";
                return String.format("| %-" + columnWidth + "s", String.format("%s ~> %s", waiter.getInitial(), stateInfo));
            }

            public String formatEmpty() {
                return String.format("| %-" + columnWidth + "s", "");
            }

            public String formatBorder() {
                return "+" + "-".repeat(columnWidth + 2);
            }

            public int getColumnWidth() {
                return columnWidth + 2;
            }
        };
    }

    private static ITableDesign<Chef> createChefFormatter() {
        return new ITableDesign<Chef>() {
            public String formatHeader() {
                return String.format("| %-" + columnWidth + "s", "Chef");
            }

            public String formatRow(Chef chef) {
                String stateInfo = chef.getState() != null ? formatStateDetails(chef.getState().getStateName()) : "waiting";
                return String.format("| %-" + columnWidth + "s", String.format("%s ~> %s", chef.getInitial(), stateInfo));
            }

            public String formatEmpty() {
                return String.format("| %-" + columnWidth + "s", "");
            }

            public String formatBorder() {
                return "+" + "-".repeat(columnWidth + 2);
            }

            public int getColumnWidth() {
                return columnWidth + 2;
            }
        };
    }

    private static String formatStateDetails(String stateName) {
        if (stateName == null) return "waiting";
        int bracketIndex = stateName.indexOf('(');

        if (bracketIndex != -1) {
            String state = stateName.substring(0, bracketIndex).trim();
            String target = stateName.substring(bracketIndex + 1, stateName.length() - 1);
            return String.format("%s %s", state, target);
        }

        return stateName;
    }
}
