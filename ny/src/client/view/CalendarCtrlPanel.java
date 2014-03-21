package view;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Week;

import resources.AppConstants;
import utils.RelativeLayout;

@SuppressWarnings("serial")
public class CalendarCtrlPanel extends JPanel {

	private Calendar calendar;
	private JLabel weekLabel;
	private JButton nextWeekButton, previousWeekButton;
	private JComboBox<Week> weekSelector;
	private DefaultComboBoxModel<Week> weeks;

	private RelativeLayout rl;
	
	public CalendarCtrlPanel() {
		
		this.calendar = new GregorianCalendar();
		this.nextWeekButton = new JButton(">>");
		this.previousWeekButton = new JButton("<<");
		this.weekLabel = new JLabel();
		this.weekSelector = new JComboBox<Week>();
		this.weeks = new DefaultComboBoxModel<Week>();
		
		this.rl = new RelativeLayout(RelativeLayout.X_AXIS, 0);
		this.rl.setAlignment(RelativeLayout.CENTER);
		
		weekSelector.setModel(weeks);
		
		setLayout(rl);
		setBackground(AppConstants.HEADER_BG_COLOR);
		
		add(weekLabel);
		add(weekSelector);
		add(previousWeekButton);
		add(nextWeekButton);
		
	}
	
	public void addNextWeekButtonListener(ActionListener listener) {
		nextWeekButton.addActionListener(listener);
	}
	
	public void addPreviousWeekButtonListener(ActionListener listener) {
		previousWeekButton.addActionListener(listener);
	}
	
	public void setWeek(int week) {
		weekLabel.setText(Integer.toString(week));
	}	

	public List<Week> getWeeks(int numberOfWeeks) {

		List<Week> weeks = new ArrayList<Week>();

		int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
		int numberOfWeeksInYear = calendar.getWeeksInWeekYear();

		int numberOfWeeksAddedThisYear = 0;

		for(int i = 0; i < numberOfWeeks; i++) {

			if (currentWeek + i <= numberOfWeeksInYear) {
				weeks.add(new Week(currentWeek + i));
				numberOfWeeksAddedThisYear++;
			} else {
				weeks.add(new Week(i - numberOfWeeksAddedThisYear));
			}
		}
		return weeks;
	}
	
	@Override
	public Container getParent() {
		return super.getParent();
	}

	public void fillSizeOfParent() {
		setPreferredSize(new Dimension((int)(getParent().getPreferredSize().width * (1 - AppConstants.TITLE_BAR_SCALE_WIDTH)/2)+1, getParent().getPreferredSize().height));
	}
}
