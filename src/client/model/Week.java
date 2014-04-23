package model;

public class Week {
	private int weekNumber;

	public Week(int weekNumber) {
		this.weekNumber = weekNumber;
	}

	public void setWeek(int week) {
		weekNumber = week;
	}

	public int getWeekNumber() {
		return weekNumber;
	}

	@Override
	public String toString() {
		return Integer.toString(weekNumber);
	}
}