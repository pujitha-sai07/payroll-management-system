import java.util.*;
import java.io.*;

// Abstract base class representing an employee
abstract class Employee {
    private String name;
    private int id;
    private String department;

    public Employee(String name, int id, String department) {
        this.name = name;
        this.id = id;
        this.department = department;
    }

    public String getName() { return name; }
    public int getID() { return id; }
    public String getDepartment() { return department; }

    // Abstract method to calculate salary – enables polymorphism
    public abstract double calculateSalary();

    @Override
    public String toString() {
        return "Employee [Name=" + name + ", ID=" + id + ", Department=" + department +
               ", Salary=" + calculateSalary() + "]";
    }
}

// Interface for bonus-eligible employees
interface BonusEligible {
    double getBonus();
}

// Full-time employee with fixed salary and optional bonus
class FullTimeEmployee extends Employee implements BonusEligible {
    private double monthlySalary;
    private double bonus;

    public FullTimeEmployee(String name, int id, String dept, double salary, double bonus) {
        super(name, id, dept);
        this.monthlySalary = salary;
        this.bonus = bonus;
    }

    public double calculateSalary() {
        return monthlySalary + bonus; // salary includes bonus
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public double getMonthlySalary() {
        return monthlySalary;
    }
}

// Part-time employee paid by the hour
class PartTimeEmployee extends Employee {
    private int hoursWorked;
    private double hourlyRate;

    public PartTimeEmployee(String name, int id, String dept, int hours, double rate) {
        super(name, id, dept);
        this.hoursWorked = hours;
        this.hourlyRate = rate;
    }

    public double calculateSalary() {
        return hoursWorked * hourlyRate;
    }

    public int getHoursWorked() {
        return hoursWorked;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double rate) {
        this.hourlyRate = rate;
    }
}

// Manages employees, with search, sort, department grouping, export, etc.
class PayrollSystem {
    private List<Employee> employeeList = new ArrayList<>();

    public void addEmployee(Employee e) {
        employeeList.add(e);
    }

    public void removeEmployee(int id) {
        employeeList.removeIf(e -> e.getID() == id);
    }

    public void displayEmployees() {
        if (employeeList.isEmpty()) {
            System.out.println("No employees to display.");
            return;
        }

        for (Employee e : employeeList) {
            System.out.println(e);
        }
    }

    // Grouping employees by department
    public void displayByDepartment() {
        Map<String, List<Employee>> deptMap = new HashMap<>();
        for (Employee e : employeeList) {
            deptMap.computeIfAbsent(e.getDepartment(), k -> new ArrayList<>()).add(e);
        }
        for (String dept : deptMap.keySet()) {
            System.out.println("Department: " + dept);
            for (Employee e : deptMap.get(dept)) {
                System.out.println("  " + e);
            }
        }
    }

    // Searching by ID or Name
    public void searchEmployee(String query) {
        for (Employee e : employeeList) {
            if (e.getName().equalsIgnoreCase(query) || String.valueOf(e.getID()).equals(query)) {
                System.out.println("Found: " + e);
                return;
            }
        }
        System.out.println("Employee not found.");
    }

    // Sorting employees by salary (descending)
    public void sortBySalary() {
        employeeList.sort(Comparator.comparing(Employee::calculateSalary).reversed());
        System.out.println("Sorted by salary:");
        displayEmployees();
    }

    // Exporting report to text file
    public void exportReport(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Employee e : employeeList) {
                writer.write(e.toString());
                writer.newLine();
            }
            System.out.println("Report exported to " + filename);
        } catch (IOException e) {
            System.out.println("Error writing report: " + e.getMessage());
        }
    }

    // Promotion logic for eligible employees
    public void promoteEmployees() {
        for (Employee e : employeeList) {
            if (e instanceof FullTimeEmployee fte) {
                if (fte.getMonthlySalary() > 50000) {
                    fte.setBonus(fte.getBonus() + 2000);
                    System.out.println("Promoted (Bonus increased): " + fte.getName());
                }
            } else if (e instanceof PartTimeEmployee pte) {
                if (pte.getHoursWorked() > 160) {
                    pte.setHourlyRate(pte.getHourlyRate() + 50);
                    System.out.println("Promoted (Hourly rate increased): " + pte.getName());
                }
            }
        }
    }
}


public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //admin login authentication
        System.out.println("Login Required");
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        if (!username.equals("admin") || !password.equals("admin123")) {
            System.out.println("Invalid credentials. Exiting.");
            return;
        }

        System.out.println("Login successful!\n");

        PayrollSystem payroll = new PayrollSystem();

        // Menu-driven interface
        while (true) {
            System.out.println("\n--- Payroll System Menu ---");
            System.out.println("1. Add Full-Time Employee");
            System.out.println("2. Add Part-Time Employee");
            System.out.println("3. Remove Employee");
            System.out.println("4. Display All Employees");
            System.out.println("5. Display by Department");
            System.out.println("6. Search Employee");
            System.out.println("7. Sort by Salary");
            System.out.println("8. Export Report");
            System.out.println("9. Exit");
            System.out.println("10. Apply Promotions");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception ex) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Name: ");
                    String fname = sc.nextLine();
                    System.out.print("ID: ");
                    int fid = sc.nextInt();
                    System.out.print("Department: ");
                    sc.nextLine(); // Consume leftover newline
                    String fdept = sc.nextLine();
                    System.out.print("Monthly Salary: ");
                    double salary = sc.nextDouble();
                    System.out.print("Bonus: ");
                    double bonus = sc.nextDouble();
                    payroll.addEmployee(new FullTimeEmployee(fname, fid, fdept, salary, bonus));
                    break;

                case 2:
                    System.out.print("Name: ");
                    String pname = sc.nextLine();
                    System.out.print("ID: ");
                    int pid = sc.nextInt();
                    System.out.print("Department: ");
                    sc.nextLine();
                    String pdept = sc.nextLine();
                    System.out.print("Hours Worked: ");
                    int hours = sc.nextInt();
                    System.out.print("Hourly Rate: ");
                    double rate = sc.nextDouble();
                    payroll.addEmployee(new PartTimeEmployee(pname, pid, pdept, hours, rate));
                    break;

                case 3:
                    System.out.print("Enter ID to remove: ");
                    int removeId = sc.nextInt();
                    payroll.removeEmployee(removeId);
                    break;

                case 4:
                    payroll.displayEmployees();
                    break;

                case 5:
                    payroll.displayByDepartment();
                    break;

                case 6:
                    System.out.print("Enter name or ID: ");
                    String search = sc.nextLine();
                    payroll.searchEmployee(search);
                    break;

                case 7:
                    payroll.sortBySalary();
                    break;

                case 8:
                    payroll.exportReport("payroll_report.txt");
                    break;

                case 9:
                    System.out.println("Exiting...");
                    return;

                case 10:
                    payroll.promoteEmployees();
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
