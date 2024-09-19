package communication;

public final class PlayerInfo {
	private final String studentFirstName;
	private final String studentLastName;
	private final String studentUAccount;
	
	public PlayerInfo(String studentFirstName, String studentLastName, String studentUAccount) {
		this.studentFirstName = studentFirstName;
		this.studentLastName = studentLastName;
		this.studentUAccount = studentUAccount;
	}
	
	public String getStudentFirstName() {
		return studentFirstName;
	}
	
	public String getStudentLastName() {
		return studentLastName;
	}
	
	public String getStudentUAccount() {
		return studentUAccount;
	}
	
}
