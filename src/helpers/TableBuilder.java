package helpers;

import java.util.List;
import java.util.function.Function;
import models.entity.*;
import interfaces.ITableDesign;

public class TableBuilder {
    private static final int COLUMN_SIZE = 42;

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

    @SafeVarargs
    private static String buildTableBorder(ITableDesign<?>... formatters) {
        StringBuilder border = new StringBuilder();
        for (ITableDesign<?> formatter : formatters) {
            border.append(formatter.formatBorder());
        }
        return border + "+";
    }

    @SafeVarargs
    private static String buildHeaderRow(ITableDesign<?>... formatters) {
        StringBuilder header = new StringBuilder();
        for (ITableDesign<?> formatter : formatters) {
            header.append(formatter.formatHeader());
        }
        return header + "|";
    }

    private static <T> ITableDesign<T> createEntityFormatter(String headerName, Function<T, String> formatFunction) {
        return new ITableDesign<T>() {
            public String formatHeader() {
                return String.format("| %-" + COLUMN_SIZE + "s", headerName);
            }

            public String formatRow(T entity) {
                return String.format("| %-" + COLUMN_SIZE + "s", formatFunction.apply(entity));
            }

            public String formatEmpty() {
                return String.format("| %-" + COLUMN_SIZE + "s", "");
            }

            public String formatBorder() {
                return "+" + "-".repeat(COLUMN_SIZE + 2);
            }

            public int getColumnWidth() {
                return COLUMN_SIZE + 2;
            }
        };
    }

    private static <T1, T2, T3> String buildDataRow(
            int index,
            List<T1> entities1, ITableDesign<T1> formatter1,
            List<T2> entities2, ITableDesign<T2> formatter2,
            List<T3> entities3, ITableDesign<T3> formatter3) {

        StringBuilder row = new StringBuilder();
        row.append(index < entities1.size() ? formatter1.formatRow(entities1.get(index)) : formatter1.formatEmpty());
        row.append(index < entities2.size() ? formatter2.formatRow(entities2.get(index)) : formatter2.formatEmpty());
        row.append(index < entities3.size() ? formatter3.formatRow(entities3.get(index)) : formatter3.formatEmpty());
        return row + "|";
    }

    private static <T1, T2, T3> String buildTable(
            List<T1> entities1, ITableDesign<T1> formatter1,
            List<T2> entities2, ITableDesign<T2> formatter2,
            List<T3> entities3, ITableDesign<T3> formatter3) {

        StringBuilder table = new StringBuilder();
        ITableDesign<?>[] formatters = {formatter1, formatter2, formatter3};
        String border = buildTableBorder(formatters);

        table.append(border).append("\n");
        table.append(buildHeaderRow(formatters)).append("\n");
        table.append(border).append("\n");

        int maxRows = Math.max(Math.max(entities1.size(), entities2.size()), entities3.size());
        for (int i = 0; i < maxRows; i++) {
            table.append(buildDataRow(i,
                            entities1, formatter1,
                            entities2, formatter2,
                            entities3, formatter3))
                    .append("\n");
        }

        table.append(border);
        return table.toString();
    }

    public static String CreateTable(List<Customer> customers, List<Waiter> waiters, List<Chef> chefs) {
        ITableDesign<Customer> customerFormatter = createEntityFormatter("Customer",
                customer -> {
                    String stateInfo = customer.getState() != null ?
                            formatStateDetails(customer.getState().getStateName()) : "waiting";
                    return String.format("%s(%d) ~> %s", customer.getInitial(), customer.getTolerance(), stateInfo);
                });

        ITableDesign<Waiter> waiterFormatter = createEntityFormatter("Waiter",
                waiter -> {
                    String stateInfo = waiter.getState() != null ?
                            formatStateDetails(waiter.getState().getStateName()) : "waiting";
                    return String.format("%s ~> %s", waiter.getInitial(), stateInfo);
                });

        ITableDesign<Chef> chefFormatter = createEntityFormatter("Chef",
                chef -> {
                    String stateInfo = chef.getState() != null ?
                            formatStateDetails(chef.getState().getStateName()) : "waiting";
                    return String.format("%s ~> %s", chef.getInitial(), stateInfo);
                });

        return buildTable(customers, customerFormatter, waiters, waiterFormatter, chefs, chefFormatter);
    }
}