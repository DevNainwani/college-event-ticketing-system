import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.*;

public class CollegeEventTicketingSystem extends JFrame {
    private final java.util.List<Event> events = new ArrayList<>();
    private final java.util.List<Booking> bookings = new ArrayList<>();
    private final String DATA_FILE = "bookings.dat";
    private JPanel cards;
    private JComboBox<Event> eventBox;
    private JTextField nameField, emailField;
    private JSpinner quantitySpinner;
    private JTable table;
    private DefaultTableModel model;
    private JLabel status;

    public CollegeEventTicketingSystem() {
        setTitle("CampusTix - College Event Ticketing System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(950, 650);
        setMinimumSize(new Dimension(800, 550));
        setLocationRelativeTo(null);
        loadEvents();
        loadBookings();

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(246, 248, 252));
        root.add(header(), BorderLayout.NORTH);
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.BOLD, 13));
        tabs.addTab("Browse Events", eventsTab());
        tabs.addTab("Book Tickets", bookingTab());
        tabs.addTab("My Bookings", bookingsTab());
        root.add(tabs, BorderLayout.CENTER);
        status = new JLabel("  Welcome to CampusTix!");
        status.setBorder(new EmptyBorder(8, 10, 8, 10));
        root.add(status, BorderLayout.SOUTH);
        setContentPane(root);
        refreshCards();
        refreshEventBox();
        refreshTable();
    }

    private JPanel header() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(34, 48, 94));
        p.setBorder(new EmptyBorder(18, 24, 18, 24));
        JLabel title = new JLabel("<html><b style='font-size:26px'>CampusTix</b><br><span style='font-size:10px'>COLLEGE EVENT TICKETING SYSTEM</span></html>");
        title.setForeground(Color.WHITE);
        p.add(title, BorderLayout.WEST);
        JLabel tag = new JLabel("JAVA SWING");
        tag.setForeground(Color.WHITE);
        tag.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(120, 135, 190)), new EmptyBorder(8, 12, 8, 12)));
        p.add(tag, BorderLayout.EAST);
        return p;
    }

    private JPanel eventsTab() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(new Color(246, 248, 252));
        p.setBorder(new EmptyBorder(18, 18, 18, 18));
        JLabel heading = new JLabel("<html><b style='font-size:20px'>Upcoming campus events</b><br><span style='font-size:12px'>Explore events, prices, venues and ticket availability.</span></html>");
        p.add(heading, BorderLayout.NORTH);
        cards = new JPanel(new GridLayout(0, 2, 12, 12));
        cards.setBackground(new Color(246, 248, 252));
        JScrollPane scroll = new JScrollPane(cards);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private JPanel bookingTab() {
        JPanel page = new JPanel(new GridBagLayout());
        page.setBackground(new Color(246, 248, 252));
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(225, 230, 240)), new EmptyBorder(22, 26, 22, 26)));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 6, 8, 6); c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        JLabel title = new JLabel("Reserve your tickets");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(new Color(34, 48, 94));
        form.add(title, c);
        c.gridy++; JLabel sub = new JLabel("Enter your details to create a booking.");
        sub.setForeground(Color.GRAY); form.add(sub, c);
        c.gridwidth = 1; c.gridy++; c.gridx = 0; form.add(label("Select event"), c);
        c.gridx = 1; eventBox = new JComboBox<>(); form.add(eventBox, c);
        eventBox.addActionListener(e -> updateInfo());
        c.gridy++; c.gridx = 0; form.add(label("Student name"), c);
        c.gridx = 1; nameField = new JTextField(20); form.add(nameField, c);
        c.gridy++; c.gridx = 0; form.add(label("Email address"), c);
        c.gridx = 1; emailField = new JTextField(20); form.add(emailField, c);
        c.gridy++; c.gridx = 0; form.add(label("Number of tickets"), c);
        c.gridx = 1; quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1)); form.add(quantitySpinner, c);
        c.gridy++; c.gridx = 0; c.gridwidth = 2;
        JLabel info = new JLabel("Select an event to view availability."); info.setName("eventInfo");
        info.setForeground(new Color(59, 83, 164)); form.add(info, c);
        c.gridy++; JButton book = new JButton("Confirm booking");
        book.setBackground(new Color(59, 83, 164)); book.setForeground(Color.WHITE);
        book.setFont(new Font("SansSerif", Font.BOLD, 13));
        book.addActionListener(e -> bookTickets()); form.add(book, c);
        c.gridy++; JLabel note = new JLabel("Bookings are stored locally for demonstration.");
        note.setFont(new Font("SansSerif", Font.ITALIC, 11)); note.setForeground(Color.GRAY); form.add(note, c);
        page.add(form);
        return page;
    }

    private JPanel bookingsTab() {
        JPanel p = new JPanel(new BorderLayout(10, 12));
        p.setBackground(new Color(246, 248, 252));
        p.setBorder(new EmptyBorder(18, 18, 18, 18));
        JLabel heading = new JLabel("<html><b style='font-size:20px'>Booking records</b><br><span style='font-size:12px'>Select a confirmed booking to cancel it.</span></html>");
        p.add(heading, BorderLayout.NORTH);
        model = new DefaultTableModel(new String[]{"Booking ID", "Student", "Event", "Tickets", "Total (Rs.)", "Status"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model); table.setRowHeight(27);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT)); actions.setOpaque(false);
        JButton cancel = new JButton("Cancel selected booking");
        cancel.addActionListener(e -> cancelBooking());
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> { refreshTable(); refreshCards(); refreshEventBox(); });
        actions.add(cancel); actions.add(refresh); p.add(actions, BorderLayout.SOUTH);
        return p;
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text); l.setFont(new Font("SansSerif", Font.BOLD, 12)); return l;
    }

    private void loadEvents() {
        events.add(new Event("E101", "TechFest 2026", "12 Nov 2026", "Main Auditorium", 150, 120, "Technology showcases, coding challenges and innovation."));
        events.add(new Event("E102", "Cultural Night", "18 Nov 2026", "Open Air Theatre", 250, 200, "Music, dance, performances and campus celebrations."));
        events.add(new Event("E103", "Robotics Workshop", "22 Nov 2026", "Engineering Lab 2", 100, 45, "Hands-on robotics activities for curious makers."));
        events.add(new Event("E104", "Battle of Bands", "2 Dec 2026", "College Ground", 180, 150, "A live music competition featuring student bands."));
        events.add(new Event("E105", "Entrepreneurship Summit", "8 Dec 2026", "Seminar Hall", 120, 80, "Talks, networking and startup idea presentations."));
        events.add(new Event("E106", "Annual Sports Meet", "15 Dec 2026", "Sports Complex", 80, 300, "Cheer for teams across a range of sporting events."));
    }

    private void refreshCards() {
        if (cards == null) return;
        cards.removeAll();
        for (Event e : events) {
            JPanel card = new JPanel(new BorderLayout(6, 8));
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 230, 240)), new EmptyBorder(13, 14, 13, 14)));
            JLabel name = new JLabel(e.name); name.setFont(new Font("SansSerif", Font.BOLD, 16));
            name.setForeground(new Color(34, 48, 94));
            JLabel details = new JLabel("<html><span style='font-size:10px'>" + e.date + " | " + e.venue + "</span></html>");
            details.setForeground(Color.GRAY);
            JTextArea desc = new JTextArea(e.description); desc.setWrapStyleWord(true); desc.setLineWrap(true);
            desc.setEditable(false); desc.setFocusable(false); desc.setBackground(Color.WHITE); desc.setRows(2);
            JLabel price = new JLabel("Rs. " + e.price + " per ticket");
            price.setFont(new Font("SansSerif", Font.BOLD, 13)); price.setForeground(new Color(40, 120, 85));
            JLabel seats = new JLabel(seatsLeft(e) + " seats left"); seats.setForeground(Color.GRAY);
            JPanel top = new JPanel(); top.setOpaque(false); top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
            top.add(name); top.add(Box.createVerticalStrut(5)); top.add(details); top.add(Box.createVerticalStrut(8)); top.add(desc);
            JPanel bottom = new JPanel(new BorderLayout()); bottom.setOpaque(false); bottom.add(price, BorderLayout.WEST); bottom.add(seats, BorderLayout.EAST);
            card.add(top, BorderLayout.CENTER); card.add(bottom, BorderLayout.SOUTH); cards.add(card);
        }
        cards.revalidate(); cards.repaint();
    }

    private void refreshEventBox() {
        if (eventBox == null) return;
        String selectedId = eventBox.getSelectedItem() instanceof Event ? ((Event)eventBox.getSelectedItem()).id : "";
        eventBox.removeAllItems();
        for (Event e : events) eventBox.addItem(e);
        for (int i = 0; i < eventBox.getItemCount(); i++)
            if (eventBox.getItemAt(i).id.equals(selectedId)) eventBox.setSelectedIndex(i);
        updateInfo();
    }

    private void updateInfo() {
        if (eventBox == null) return;
        Event e = (Event) eventBox.getSelectedItem();
        Container parent = eventBox.getParent();
        for (Component comp : parent.getComponents()) {
            if (comp instanceof JLabel && "eventInfo".equals(comp.getName())) {
                ((JLabel)comp).setText(e == null ? "No events available." : "Rs. " + e.price + " per ticket | " + seatsLeft(e) + " seats available");
            }
        }
    }

    private void bookTickets() {
        Event e = (Event) eventBox.getSelectedItem();
        String name = nameField.getText().trim(), email = emailField.getText().trim();
        int qty = (Integer) quantitySpinner.getValue();
        if (e == null) { error("Please select an event."); return; }
        if (name.isEmpty()) { error("Please enter the student's name."); return; }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) { error("Please enter a valid email address."); return; }
        if (qty > seatsLeft(e)) { error("Only " + seatsLeft(e) + " ticket(s) are available."); return; }
        String id = "BK" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        Booking b = new Booking(id, name, email, e.id, e.name, qty, qty * e.price, "Confirmed");
        bookings.add(b); saveBookings(); refreshTable(); refreshCards(); refreshEventBox();
        status.setText("  Booking confirmed! Booking ID: " + id);
        JOptionPane.showMessageDialog(this, "Booking confirmed!\\n\\nBooking ID: " + id + "\\nEvent: " + e.name + "\\nTickets: " + qty + "\\nTotal: Rs. " + (qty * e.price), "Success", JOptionPane.INFORMATION_MESSAGE);
        nameField.setText(""); emailField.setText(""); quantitySpinner.setValue(1);
    }

    private void cancelBooking() {
        int row = table.getSelectedRow();
        if (row < 0) { error("Please select a booking from the table."); return; }
        String id = String.valueOf(model.getValueAt(row, 0));
        for (Booking b : bookings) {
            if (b.id.equals(id)) {
                if (!b.status.equals("Confirmed")) { error("Only confirmed bookings can be cancelled."); return; }
                int answer = JOptionPane.showConfirmDialog(this, "Cancel booking " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.YES_OPTION) {
                    b.status = "Cancelled"; saveBookings(); refreshTable(); refreshCards(); refreshEventBox();
                    status.setText("  Booking " + id + " has been cancelled.");
                }
                return;
            }
        }
    }

    private int seatsLeft(Event e) {
        int used = 0;
        for (Booking b : bookings) if (b.eventId.equals(e.id) && b.status.equals("Confirmed")) used += b.quantity;
        return Math.max(0, e.capacity - used);
    }

    private void refreshTable() {
        if (model == null) return;
        model.setRowCount(0);
        for (Booking b : bookings) model.addRow(new Object[]{b.id, b.student, b.eventName, b.quantity, b.total, b.status});
    }

    private void error(String message) {
        JOptionPane.showMessageDialog(this, message, "Please check", JOptionPane.WARNING_MESSAGE);
    }

    @SuppressWarnings("unchecked")
    private void loadBookings() {
        File f = new File(DATA_FILE);
        if (!f.exists()) return;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            Object obj = in.readObject();
            if (obj instanceof ArrayList<?>) for (Object o : (ArrayList<?>)obj) if (o instanceof Booking) bookings.add((Booking)o);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Saved bookings could not be loaded. A fresh list will be used.", "Data notice", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void saveBookings() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(new ArrayList<>(bookings));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not save bookings: " + ex.getMessage(), "File error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class Event {
        final String id, name, date, venue, description;
        final int price, capacity;
        Event(String id, String name, String date, String venue, int price, int capacity, String description) {
            this.id=id; this.name=name; this.date=date; this.venue=venue; this.price=price; this.capacity=capacity; this.description=description;
        }
        public String toString() { return name + " - Rs. " + price; }
    }

    private static class Booking implements Serializable {
        private static final long serialVersionUID = 1L;
        final String id, student, email, eventId, eventName;
        final int quantity, total;
        String status;
        Booking(String id, String student, String email, String eventId, String eventName, int quantity, int total, String status) {
            this.id=id; this.student=student; this.email=email; this.eventId=eventId; this.eventName=eventName;
            this.quantity=quantity; this.total=total; this.status=status;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
            new CollegeEventTicketingSystem().setVisible(true);
        });
    }
}
