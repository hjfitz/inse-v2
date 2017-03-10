package main;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.EventQueue;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComboBox;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Timetable extends JFrame {

	// create sql date and time types to compare in the query
	private static java.util.Date today = new java.util.Date();
	// private Time curTime = new Time(today.getTime());
	private static Date curDate = new Date(today.getTime());
	// TODO need to get a date for a week away and a month away

	// need to read this from a text file
	static String thisStop = "101";

	// prepare the query to get all of the bus routes that arrive at this stop
	private static String qryGetRouteNumsStart = "select distinct Route_ID, Route_Name from Arrival_Stop natural join Route natural join Arrival_Times where Stop_ID = ";
	private static String qryGetRouteNumsEnd = " and Arrival_Time >= '";// +
																		// curDate
																		// +
																		// "'";

	// prepare the query that gets all of the times. this will be nested in a
	// loop which goes through the IDs found in the previous query
	static String qryGetRoutes = "Select Arrival_time from Arrival_Stop natural join Route natural join Arrival_Times natural join Stop where Route_ID = ";

	// generated by windowBuilder
	private JPanel contentPane;
	private JTable table;
	JTextPane txtpnHints;
	private JTextField txtWeek;

	@SuppressWarnings("rawtypes")
	public Timetable() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1280, 768);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(6, 6, 1248, 713);
		contentPane.add(panel);

		JLabel lblNextBus = new JLabel("Next Bus: ?? Minutes");
		lblNextBus.setHorizontalAlignment(SwingConstants.CENTER);

		DefaultTableModel ourTable = new DefaultTableModel(new Object[][] { { null, null }, },
				new String[] { "Route Name", "Times" });

		String[] data = new String[2];
		data[0] = "Route Name";
		data[1] = "Times";
		ourTable.addRow(data);

		JButton btnDay = new JButton("Day");
		btnDay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				getTimes("1 DAY", ourTable);

			}
		});
	
		btnDay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				txtpnHints.setText("Set your view to daily");
				
			}
		});

		JButton btnWeek = new JButton("Week");
		btnWeek.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				getTimes("1 WEEK", ourTable);
			}
		});

		btnWeek.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				txtpnHints.setText("Set your view to weekly");
				
			}
		});

		JButton btnMonth = new JButton("Month");
		btnMonth.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				getTimes("1 MONTH", ourTable);
			}
		});

		
		btnMonth.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				txtpnHints.setText("Set your view to monthly");
				
			}
		});
		
		
		JComboBox cmbRoutes = new JComboBox();
		cmbRoutes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				txtpnHints.setText("Here you can change your current viewed stop");
				
			}
		});

		JButton btnTemp = new JButton("All Times");
		btnTemp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});

		// start adding code here

		btnTemp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				getTimes("", ourTable);

			}
		});

		JButton btnRefresh = new JButton("refresh");
		btnRefresh.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				changeRouteNo(cmbRoutes);
				getTimes("1 DAY", ourTable);
			}
		});
		getStops(cmbRoutes);
		

		JScrollPane scrollPane = new JScrollPane();
		
		txtpnHints = new JTextPane();
		txtpnHints.setFont(new Font("Arial", Font.PLAIN, 23));
		
		txtpnHints.setText("Hints: Welcome to Flashcloud, here you can view current stop's timetable, filter your searches by choosing a daily"
				+ ",weekly or monthly timetable");

		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main mainForm = new Main();
				mainForm.setVisible(true);

			}
		});

		JButton btnUpdateRoutes = new JButton("Update Routes");
		btnUpdateRoutes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ParseForm parser = new ParseForm();
				parser.setVisible(true);
			}
		});
		
		JComboBox cmbStops = new JComboBox();
		
		JButton btnSearchByRoute = new JButton("search by stop");
		btnSearchByRoute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String routeName = cmbStops.getSelectedItem().toString();
				getByRoute(routeName, ourTable);
				
			}
		});
		
		JButton btnViewForSpecific = new JButton("View for specific week");
		btnViewForSpecific.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getWeek(txtWeek.getText(), ourTable);
			}
		});
		
		txtWeek = new JTextField();
		txtWeek.setColumns(10);

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(txtpnHints, GroupLayout.DEFAULT_SIZE, 1238, Short.MAX_VALUE)
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblNextBus, GroupLayout.DEFAULT_SIZE, 1228, Short.MAX_VALUE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(12)
							.addComponent(cmbRoutes, 0, 1075, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnRefresh, GroupLayout.PREFERRED_SIZE, 141, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 1228, Short.MAX_VALUE))
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(btnUpdateRoutes)
									.addGap(116)
									.addComponent(btnViewForSpecific)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(txtWeek, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(btnSearch)
									.addGap(655)
									.addComponent(btnDay, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
									.addGap(7)
									.addComponent(btnWeek, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
									.addGap(3)
									.addComponent(btnMonth, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnTemp, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE))))
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(cmbStops, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSearchByRoute)))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(22)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(cmbRoutes, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnRefresh, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(cmbStops, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSearchByRoute))
					.addGap(8)
					.addComponent(lblNextBus, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(19)
							.addComponent(btnUpdateRoutes)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(btnTemp, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
									.addComponent(btnDay, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
									.addComponent(btnSearch))
								.addComponent(btnWeek, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnMonth, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_panel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnViewForSpecific)
								.addComponent(txtWeek, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 359, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
					.addComponent(txtpnHints, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);

		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(ourTable);
		panel.setLayout(gl_panel);
		getRoutes(cmbStops);
	}

	// show the window when this file is run
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Timetable frame = new Timetable();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	private static String parseDate(String date) throws ParseException {
		SimpleDateFormat iso8601 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date parsedDate = iso8601.parse(date);
		iso8601.applyPattern("EEE dd-MMM");
		String day = iso8601.format(parsedDate);
		//System.out.println(day);
		return day;
	}

	/**
	 * 
	 * @param updPane
	 *            the text pane to append the new text
	 * @param query
	 *            the query to search
	 */
	@SuppressWarnings("unused")
	public static void getTimes(String constraints, DefaultTableModel routeTable) {
		routeTable.setRowCount(1);
		DatabaseConnection dbConn = new DatabaseConnection();
		dbConn.connect();
		String qryGetRouteNums = qryGetRouteNumsStart + thisStop;// +
																	// qryGetRouteNumsEnd
																	// + curDate
																	// + "'";
		ResultSet routeNumbers = dbConn.runQuery(qryGetRouteNums);
		String newTextField = "";
		String date = "";
		try {

			while (routeNumbers.next()) {
				String routeTimes = "";
				String[] routeInfo = new String[2];
				routeInfo[0] = routeNumbers.getString("Route_Name");

				newTextField += routeNumbers.getString("Route_Name") + ":\t";
				String qryGetTimes = qryGetRoutes + routeNumbers.getInt("Route_ID") + " and Stop_ID = " + thisStop
						+ qryGetRouteNumsEnd + curDate + "'";
				if (constraints != "") {
					qryGetTimes += "and Arrival_Time <= (SELECT '" + curDate + "' + INTERVAL " + constraints + ")";
				}
				qryGetTimes += " order by Arrival_Time ";
				ResultSet times = dbConn.runQuery(qryGetTimes);
				while (times.next()) {
					try {
						date = parseDate(times.getDate("Arrival_Time").toString());
					} catch (ParseException e) {
						e.printStackTrace();
					}
					String out = date + " " + times.getTime("Arrival_Time").toString();
					newTextField += out;
					routeTimes += out;
					routeInfo[1] = out;
					routeTable.addRow(routeInfo);
					routeInfo[0] = "";
					// newTextField += times.getDate("Arrival_Time").toString()
					// + " " + times.getTime("Arrival_Time").toString() + "\t";
					// newTextField += " " +
					// times.getTime("Arrival_Time").toString() + "\t";

				}
				// routeInfo[1] = routeTimes;
				newTextField += "\n\n\n";
				// routeTable.addRow(routeInfo);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbConn.closeConnection();
	}
	
	@SuppressWarnings("unused")
	//TODO make this fucker work
	public static void getByRoute(String routeName, DefaultTableModel routeTable) {
		routeTable.setRowCount(1);
		DatabaseConnection dbConn = new DatabaseConnection();
		dbConn.connect();
		String query = "SELECT distinct Stop_Name, Arrival_Time from Arrival_Stop natural join Arrival_Times natural join Stop natual join Route where Route_Name = '" + routeName + "'";
		ResultSet arrivalTimes = dbConn.runQuery(query);
		String newTextField = "";
		String date = "";
		try {
			
			while (arrivalTimes.next()) {
				String stopInfo[] = new String[2];
				stopInfo[0] = arrivalTimes.getString("Stop_Name");
				try {
					date = parseDate(arrivalTimes.getDate("Arrival_Time").toString());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				stopInfo[1] = date + " " + arrivalTimes.getTime("Arrival_Time").toString();
				routeTable.addRow(stopInfo);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbConn.closeConnection();
	}

	@SuppressWarnings("rawtypes")
	public static void changeRouteNo(JComboBox cmb) {
		String qry = "Select distinct Stop_ID from Stop where Stop_Name = '" + cmb.getSelectedItem().toString() + "'";
		DatabaseConnection dbConn = new DatabaseConnection();
		dbConn.connect();
		ResultSet stopID = dbConn.runQuery(qry);
		String stop = thisStop;
		// System.out.println(stop);
		try {
			while (stopID.next()) {
				stop = stopID.getString("Stop_ID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// System.out.println(stop);
		dbConn.closeConnection();
		thisStop = stop;

	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void getRoutes(JComboBox box) {
		String query = "Select distinct * from Route";
		DatabaseConnection dbConn = new DatabaseConnection();
		dbConn.connect();
		ResultSet routeNames = dbConn.runQuery(query);
		try {
			while (routeNames.next()) {
				String route = routeNames.getString("Route_Name");
				box.addItem(route);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void getStops(JComboBox box) {
		String qry = "Select distinct * from Stop";
		String curStopName = "";
		DatabaseConnection dbConn = new DatabaseConnection();
		dbConn.connect();
		ResultSet stopNames = dbConn.runQuery(qry);
		try {
			while (stopNames.next()) {
				String curStopID = "" + stopNames.getInt("Stop_ID");
				String curStop = stopNames.getString("Stop_Name");
				box.addItem(curStop);
				if (curStopID.equals(thisStop)) {
					curStopName = curStop;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbConn.closeConnection();
		box.setSelectedItem(curStopName);
	}
	
	@SuppressWarnings("deprecation")
	private static void getWeek(String weekStart, DefaultTableModel routeTable) {
		routeTable.setRowCount(1);
		String weekFormat = "yyyy-MM-dd";
		SimpleDateFormat df = new SimpleDateFormat(weekFormat);
		java.util.Date date = new java.util.Date();
		try {
			date = df.parse(weekStart);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 7);
		//wontfix 
		//TODO
		String nextDate = "2017-" + (cal.getTime().getMonth() + 1) + "-" + cal.getTime().getDate();
		System.out.println(nextDate);
		DatabaseConnection dbconn = new DatabaseConnection();
		dbconn.connect();
		String query = "Select distinct * from Route natural join Stop natural join Arrival_Times natural join Arrival_Stop where Stop_ID = " + thisStop;
		query += " and Arrival_Time >= '" + weekStart + "' and Arrival_Time <= '" + nextDate + "'";
		System.out.println(query);
		ResultSet stops = dbconn.runQuery(query);
		try {
			while (stops.next()) {
				String name = stops.getString("Route_Name");
				Date dateStop = stops.getDate("Arrival_Time");
				String time = stops.getTime("Arrival_Time").toString();
				String parsedDate = parseDate(dateStop.toString());
				String timeCol = parsedDate + " " + time;
				String[] data = new String[2];
				data[0] = name;
				data[1] = timeCol;
				routeTable.addRow(data);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
